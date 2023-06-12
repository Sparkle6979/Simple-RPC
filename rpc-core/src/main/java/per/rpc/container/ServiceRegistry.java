package per.rpc.container;

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
     * @param service 服务实体
     * @param inetSocketAddress 提供服务的地址
     */
    <T> void register(T service, InetSocketAddress inetSocketAddress);


}
