package per.rpc.test;

import per.rpc.api.HelloService;
import per.rpc.registry.DefaultServiceRegistry;
import per.rpc.registry.ServiceRegistry;
import per.rpc.server.RpcServer;

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
        RpcServer rpcServer = new RpcServer(serviceRegistry);
        rpcServer.start(9000);


    }
}
