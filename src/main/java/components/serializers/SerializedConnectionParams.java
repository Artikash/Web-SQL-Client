package components.serializers;

import java.util.Optional;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public final class SerializedConnectionParams {
	public SerializedConnectionParams(final String fullDatabaseUrl, final Optional<Integer> timeout) {
		Optional<String> username = Optional.empty();
		Optional<String> password = Optional.empty();
		var jdbcUrl = "jdbc:" + fullDatabaseUrl;
		try {
			final var uri = new URI(fullDatabaseUrl);
			final var userInfo = uri.getUserInfo();
			jdbcUrl = "jdbc:" + uri.getScheme() + "://" + uri.getHost();
			if (uri.getPort() != -1) {
				jdbcUrl += ":" + uri.getPort();
			}
			jdbcUrl += uri.getPath();
			if (uri.getQuery() != null) {
				jdbcUrl += "?" + uri.getQuery();
			}
			if (userInfo != null) {
				final var userPass = userInfo.split(":");
				username = Optional.of(userPass[0]);
				if (userPass.length > 1) {
					password = Optional.of(userPass[1]);
				}
			}
		} catch (URISyntaxException e) {}
		this.username = username;
		this.password = password;
		this.jdbcUrl = jdbcUrl;
		this.timeout = timeout.orElse(300);
	}

	public final String jdbcUrl;
	public final Optional<String> username;
	public final Optional<String> password;
	public final int timeout;
}