package per.rpc.serializer;
/**
 * @author sparkle6979l
 * @data 2023/6/10 17:29
 * @version 1.0
 *
 */
public interface CommonSerializer {

    byte[] serialize(Object obj);

    Object deserialize(byte[] bytes, Class<?> clazz);

    int getCode();

    static CommonSerializer getByCode(int code){
        switch (code) {
            case 1:
                return new JsonSerializer();
            default:
                return null;
        }
    }

}
