package per.rpc.test;

import per.rpc.api.HelloService;
import per.rpc.transport.netty.server.NettyServer;
import per.rpc.provider.DefaultServiceProvider;
import per.rpc.provider.ServiceProvider;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/10 19:08
 */
public class NettyTestServer {

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        ServiceProvider registry = new DefaultServiceProvider();
        registry.register(helloService);
        NettyServer server = new NettyServer();
        server.start(9999);
    }

}
