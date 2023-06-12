package per.rpc.test;

import per.rpc.api.HelloService;
import per.rpc.provider.DefaultServiceProvider;
import per.rpc.provider.ServiceProvider;
import per.rpc.registry.ServiceRegistry;
import per.rpc.registry.ZookeeperServiceRegistry;
import per.rpc.transport.socket.server.SocketServer;

import java.net.InetSocketAddress;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/9 16:18
 */
public class TestServer {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();


        ServiceRegistry serviceRegistry = new ZookeeperServiceRegistry(new InetSocketAddress("localhost",2181));
        serviceRegistry.register(helloService,new InetSocketAddress("localhost",9999));

        ServiceProvider serviceProvider = new DefaultServiceProvider();
        serviceProvider.register(helloService);
        SocketServer rpcServer = new SocketServer(serviceProvider);
        rpcServer.start(9999);


    }
}
