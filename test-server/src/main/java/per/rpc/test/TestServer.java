package per.rpc.test;

import per.rpc.api.HelloService;
import per.rpc.registry.DefaultServiceRegistry;
import per.rpc.registry.ServiceRegistry;
import per.rpc.registry.ZookeeperServiceRegistry;
import per.rpc.socket.server.SocketServer;

import java.net.InetSocketAddress;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/9 16:18
 */
public class TestServer {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();

        ServiceRegistry serviceRegistry = new ZookeeperServiceRegistry("localhost:2181",new InetSocketAddress("localhost",9999));
        serviceRegistry.register(helloService);

        ServiceRegistry serviceProvider = new DefaultServiceRegistry();
        serviceProvider.register(helloService);

        SocketServer rpcServer = new SocketServer(serviceRegistry,serviceProvider);
        rpcServer.start(9999);


    }
}
