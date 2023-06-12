package per.rpc.container;

import java.net.InetSocketAddress;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/12 22:00
 */
public class ZookeeperContainer extends ServiceContainer{
    public ZookeeperContainer(InetSocketAddress inetSocketAddress) {
        super(inetSocketAddress);
        this.serviceRegistry = new ZookeeperServiceRegistry(inetSocketAddress);
        this.serviceDiscover = new ZookeeperServiceDiscover(inetSocketAddress);
    }

    public ZookeeperContainer(String host, int port) {
        this(new InetSocketAddress(host,port));
    }

    public ServiceRegistry getServiceRegistry(){
        return this.serviceRegistry;
    }


    public ServiceDiscover getserviceDiscover(){
        return this.serviceDiscover;
    }

}
