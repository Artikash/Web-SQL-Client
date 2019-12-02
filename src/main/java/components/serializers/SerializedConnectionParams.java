package components.serializers;

import java.util.Optional;

public final class SerializedConnectionParams {
	public SerializedConnectionParams(final String fullDatabaseUrl, final Optional<Integer> timeout) {
		this.jdbcUrl = "jdbc:" + fullDatabaseUrl;
		this.timeout = timeout.orElse(300);
	}

	public final String jdbcUrl;
	public final int timeout;
}