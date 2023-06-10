package per.rpc.test;

import per.rpc.api.HelloService;
import per.rpc.registry.DefaultServiceRegistry;
import per.rpc.registry.ServiceRegistry;
import per.rpc.socket.server.SocketServer;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/9 16:18
 */
public class TestServer {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.register(helloService);
        SocketServer rpcServer = new SocketServer(serviceRegistry);
        rpcServer.start(9000);


    }
}
