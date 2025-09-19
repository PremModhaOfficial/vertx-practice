package learningVertX.eventBusCommunication.codec;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public class JacksonPojoCodec<T> implements MessageCodec<T, T> {

    private final Class<T> type;
    private final ObjectMapper mapper;

    public JacksonPojoCodec(Class<T> type) {
        this.type = type;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    @Override
    public void encodeToWire(Buffer buffer, T s) {
        try {
            byte[] jsonBytes = mapper.writeValueAsBytes(s);
            buffer.appendInt(jsonBytes.length);
            buffer.appendBytes(jsonBytes);
        } catch (Exception e) {
            throw new RuntimeException("Failed to encode object to JSON", e);
        }
    }

    @Override
    public T decodeFromWire(int pos, Buffer buffer) {
        try {
            int length = buffer.getInt(pos);
            byte[] jsonBytes = buffer.getBytes(pos + 4, pos + 4 + length);
            return mapper.readValue(jsonBytes, type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decode JSON to object", e);
        }
    }

    @Override
    public T transform(T s) {
        return s;
    }

    @Override
    public String name() {
        return type.getName() + "-JacksonCodec";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }
}
