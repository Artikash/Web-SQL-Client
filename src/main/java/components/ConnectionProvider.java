package components;

import java.sql.Connection;
import static java.sql.DriverManager.getConnection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.rowset.RowSetProvider;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import components.serializers.SerializedConnectionParams;
import components.serializers.SerializedQueryParams;

@RestController
public final class ConnectionProvider {
	public ConnectionProvider() {
		new Thread(() -> {
			while (true) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {}
				for (final var connection : connections.entrySet()) {
					if (connection.getValue().expired()) {
						connections.remove(connection.getKey());
					}
				}
			}
		}).start();
	}

	@PostMapping("/api/connect")
	public String addConnection(@RequestBody final SerializedConnectionParams params) {
		final String connectionId = UUID.randomUUID().toString();
		try {
			connections.put(connectionId, new ExpiringConnection(getConnection(params.jdbcUrl), params.timeout));
		} catch (final SQLException e) {
			throw new BadRequest(e);
		}
		return connectionId;
	}

	@PostMapping("/api/query")
	public CompletableFuture<ResultSet> findConnectionExecuteQuery(@RequestBody final SerializedQueryParams params) {
		final var connection = connections.computeIfAbsent(
			params.connectionId,
			connectionId -> { throw new BadRequest("Couldn't find connection with id " + connectionId); }
		).get();
		return CompletableFuture.supplyAsync(() -> {
			try (
				final var statement = connection.createStatement();
				final var resultSet = statement.executeQuery(params.query);
			) {
				final var cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
				cachedRowSet.populate(resultSet);
				return cachedRowSet;
			} catch (final SQLException e) {
				throw new CompletionException(new BadRequest(e));
			}
		});
	}

	private final Map<String, ExpiringConnection> connections = new ConcurrentHashMap<>();

	@ResponseStatus(code = HttpStatus.BAD_REQUEST)
	private static final class BadRequest extends RuntimeException {
		public BadRequest(final String reason) {
			super(reason);
		}

		public BadRequest(final Throwable cause) {
			super(cause.getMessage(), cause.getCause());
		}
	}

	private static final class ExpiringConnection {
		public ExpiringConnection(final Connection connection, final long lifetime) {
			this.connection = connection;
			lifetimeMs = lifetime * 1000;
			expireAt = System.currentTimeMillis() + lifetimeMs;
		}

		public Connection get() {
			if (expired()) {
				throw new BadRequest("Connection expired");
			}
			expireAt = System.currentTimeMillis() + lifetimeMs;
			return connection;
		}

		public boolean expired() {
			if (System.currentTimeMillis() > expireAt) {
				try {
					connection.close();
				} catch (SQLException e) {}
				return true;
			}
			return false;
		}

		private final Connection connection;
		private final long lifetimeMs;
		private long expireAt;
	}
}