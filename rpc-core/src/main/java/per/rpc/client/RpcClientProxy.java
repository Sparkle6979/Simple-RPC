package per.rpc.client;

import lombok.AllArgsConstructor;
import per.rpc.entity.RpcRequest;
import per.rpc.entity.RpcResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;



/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/9 14:52
 */
@AllArgsConstructor
public class RpcClientProxy implements InvocationHandler {
    private String host;
    private int port;


    public <T> T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz},this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = RpcRequest.builder()
                .interfaceName(method.getDeclaringClass().getName())
                .methodName(method.getName())
                .paraTypes(method.getParameterTypes())
                .parameters(args)
                .build();
        RpcClient rpcClient = new RpcClient();

        return ((RpcResponse) rpcClient.sendRequest(rpcRequest,host,port)).getData();
    }
}
