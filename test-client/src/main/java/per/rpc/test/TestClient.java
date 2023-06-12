package per.rpc.test;

import per.rpc.transport.RpcClient;
import per.rpc.api.HelloObject;
import per.rpc.api.HelloService;
import per.rpc.transport.RpcClientProxy;
import per.rpc.registry.ServiceDiscover;
import per.rpc.registry.ZookeeperServiceDiscover;
import per.rpc.transport.socket.client.SocketClient;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/9 16:12
 */
public class TestClient {
    public static void main(String[] args) {

        ServiceDiscover serviceDiscover = new ZookeeperServiceDiscover("localhost:2181");
        RpcClient client = new SocketClient(serviceDiscover);

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
