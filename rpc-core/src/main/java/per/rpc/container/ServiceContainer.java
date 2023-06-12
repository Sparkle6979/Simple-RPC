package per.rpc.container;

import java.net.InetSocketAddress;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/12 21:56
 */

public abstract class ServiceContainer {

    private InetSocketAddress inetSocketAddress;

    ServiceRegistry serviceRegistry;
    ServiceDiscover serviceDiscover;

    public ServiceContainer(InetSocketAddress inetSocketAddress){
        this.inetSocketAddress = inetSocketAddress;
    }

    public ServiceContainer(String host,int port){
        this(new InetSocketAddress(host,port));
    }



}
