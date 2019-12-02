package components.serializers;

public class SerializedQueryParams {
	public SerializedQueryParams(final String connectionId, final String query) {
		this.connectionId = connectionId;
		this.query = query;
	}

	public final String connectionId;
	public final String query;
}