package per.rpc;

import lombok.AllArgsConstructor;
import per.rpc.entity.RpcRequest;
import per.rpc.entity.RpcResponse;
import per.rpc.socket.client.SocketClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;



/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/9 14:52
 */

public class RpcClientProxy implements InvocationHandler {
    private final RpcClient client;

    public RpcClientProxy(RpcClient rpcClient){
        this.client = rpcClient;
    }

    public <T> T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz},this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        RpcRequest rpcRequest = new RpcRequest(method.getDeclaringClass().getSimpleName(),
                method.getName(), method.getParameterTypes(),args);
        return client.sendRequest(rpcRequest);
    }
}
