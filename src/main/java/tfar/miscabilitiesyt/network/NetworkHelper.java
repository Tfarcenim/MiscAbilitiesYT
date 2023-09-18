package tfar.miscabilitiesyt.network;

import net.minecraft.network.FriendlyByteBuf;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class NetworkHelper {

    static final Map<Class<?>, Function<FriendlyByteBuf,Object>> mapper_read = new HashMap<>();

    static final Map<Class<?>, BiConsumer<FriendlyByteBuf, Object>> mapper_write = new HashMap<>();


    static {
        mapper_read.put(boolean.class, FriendlyByteBuf::readBoolean);
        mapper_read.put(int.class, FriendlyByteBuf::readInt);

        mapper_write.put(boolean.class, (buf, o) -> buf.writeBoolean((boolean) o));
        mapper_write.put(int.class, (buf, o) -> buf.writeInt((int) o));
    }

    public static <T> void serialize(FriendlyByteBuf buf, T o) {
        Field[] fields = o.getClass().getFields();
        try {
            for (Field field : fields) {
                Class<?> type = field.getType();
                Object value = field.get(o);
                mapper_write.get(type).accept(buf, value);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T deserialize(FriendlyByteBuf buf, Class<T> type) {
        try {
            T o = type.getConstructor().newInstance();
            Field[] fields = o.getClass().getFields();
            for (Field field : fields) {
                Class<?> fieldType = field.getType();
                Object value = mapper_read.get(fieldType).apply(buf);
                field.set(o,value);
            }
            return o;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
