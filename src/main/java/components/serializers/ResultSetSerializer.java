package components.serializers;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public final class ResultSetSerializer extends JsonSerializer<ResultSet> {
	@Override
	public void serialize(
		final ResultSet resultSet,
		final JsonGenerator jsonGenerator,
		final SerializerProvider serializerProvider
	) throws IOException, JsonProcessingException {
		jsonGenerator.writeStartArray();
		try {
			final var resultSetMetaData = resultSet.getMetaData();
			final var columnCount = resultSetMetaData.getColumnCount();
			final var columnNames = new ArrayList<SerializableString>(columnCount);
			for (int i = 0; i < columnCount; ++i) {
				columnNames.add(new SerializedString(resultSetMetaData.getColumnName(i + 1)));
			}
			while (resultSet.next()) {
				jsonGenerator.writeStartObject();
				for (int i = 0; i < columnCount; ++i) {
					try {
						final var column = resultSet.getObject(i + 1);
						jsonGenerator.writeFieldName(columnNames.get(i));
						if (column != null) {
							final var serializer = serializerProvider.findValueSerializer(column.getClass());
							serializer.serialize(column, jsonGenerator, serializerProvider);
						} else {
							jsonGenerator.writeNull();
						}
					} catch (SQLException e) {}
				}
				jsonGenerator.writeEndObject();
			}
		} catch (SQLException e) {}
		jsonGenerator.writeEndArray();
	}
}