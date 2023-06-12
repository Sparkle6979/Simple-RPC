package per.rpc.registry;

import java.net.InetSocketAddress;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/12 11:58
 */
public interface ServiceRegistry {

    /**
     * 将一个服务注册进注册表
     *
     * @param serviceName 服务名称
     * @param inetSocketAddress 提供服务的地址
     */
    <T> void register(T service, InetSocketAddress inetSocketAddress);


}
