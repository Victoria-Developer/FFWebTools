package app.util;

import app.dto.Area;
import app.dto.Coordinate;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class AreaKeyDeserializer extends KeyDeserializer {
    @Override
    public Area deserializeKey(String key, DeserializationContext context) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(key);
        Coordinate[] teleports = mapper.treeToValue(node.get("teleports"), Coordinate[].class);
        return new Area(node.get("name").asText(), teleports, node.get("fileName").asText());
    }
}
