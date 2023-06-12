package per.rpc.serializer;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import per.rpc.entity.RpcRequest;
import per.rpc.enumeration.SerializerCode;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/10 17:38
 */

/**
 * 使用fastjson来做序列化
 */
public class JsonSerializer implements CommonSerializer{

    private static final Logger logger = LoggerFactory.getLogger(JsonSerializer.class);

    @Override
    public byte[] serialize(Object obj) {
        return JSON.toJSONBytes(obj);
    }

    private Object handleRequest(Object obj){
        RpcRequest rpcRequest = (RpcRequest) obj;
        for (int i = 0; i < rpcRequest.getParaTypes().length; i++) {
            Class<?> clazz = rpcRequest.getParaTypes()[i];
            if(!clazz.isAssignableFrom(rpcRequest.getParameters()[i].getClass())){
                byte[] bytes = JSON.toJSONBytes(rpcRequest.getParameters()[i]);
                rpcRequest.getParameters()[i] = JSON.parseObject(bytes,clazz);
            }
        }
        return rpcRequest;
    }



    @Override
    public Object deserialize(byte[] bytes, Class<?> clazz) {
        Object object = JSON.parseObject(bytes, clazz, JSONReader.Feature.SupportClassForName);
        if(object instanceof RpcRequest){
            object = handleRequest(object);
        }
        return object;
    }

    @Override
    public int getCode() {
        return SerializerCode.valueOf("JSON").getCode();
    }
}
