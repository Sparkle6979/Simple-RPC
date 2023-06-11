package per.rpc.test;

import per.rpc.RpcClient;
import per.rpc.api.HelloObject;
import per.rpc.api.HelloService;
import per.rpc.RpcClientProxy;
import per.rpc.registry.ServiceRegistry;
import per.rpc.registry.ZookeeperServiceRegistry;
import per.rpc.socket.client.SocketClient;

import java.net.InetSocketAddress;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/9 16:12
 */
public class TestClient {
    public static void main(String[] args) {
        ServiceRegistry serviceRegistry = new ZookeeperServiceRegistry("localhost:2181",new InetSocketAddress("localhost",9999));
        RpcClient client = new SocketClient(serviceRegistry);
        RpcClientProxy proxy = new RpcClientProxy(client);
        HelloService helloService = proxy.getProxy(HelloService.class);

        for (int i = 0; i < 1; i++) {
            new Thread(() -> {
                HelloObject object = new HelloObject(12, "This is a message");
                String res = helloService.Hello(object);
                System.out.println(res);
            }).start();
        }


    }

}
