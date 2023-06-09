package per.rpc.registry;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/9 21:52
 */
public interface ServiceRegistry {

    /**
     * 将某个服务（实现类）注册进注册表中
     * @param service
     * @param <T>
     */
    <T> void register(T service);

    /**
     * 根据服务名称获得服务实体
     * @param serviceName
     * @return
     */
    Object getService(String serviceName);
}
