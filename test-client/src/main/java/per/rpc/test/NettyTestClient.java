package per.rpc.test;

import per.rpc.transport.RpcClient;
import per.rpc.transport.RpcClientProxy;
import per.rpc.api.HelloObject;
import per.rpc.api.HelloService;
import per.rpc.transport.netty.client.NettyClient;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/10 19:03
 */
public class NettyTestClient {
    public static void main(String[] args) {
        RpcClient client = new NettyClient("localhost", 9999);
        RpcClientProxy rpcClientProxy = new RpcClientProxy(client);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        HelloObject object = new HelloObject(12, "This is a message");
        String res = helloService.Hello(object);
        System.out.println(res);

    }


}
