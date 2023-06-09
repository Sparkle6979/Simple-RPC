package per.rpc.registry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import per.rpc.enumeration.RpcError;
import per.rpc.exception.RpcException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/9 21:54
 */
public class DefaultServiceRegistry implements ServiceRegistry{

    private static Logger logger = LoggerFactory.getLogger(DefaultServiceRegistry.class);

    /**
     * 已经注册的服务实体
     */
    private final Map<String,Object> serviceMap = new ConcurrentHashMap<>();
    private final Set<String> registeredService = ConcurrentHashMap.newKeySet();


    @Override
    public synchronized <T> void register(T service) {

        String serviceName = service.getClass().getCanonicalName();
        // 如果已经注册过，直接返回
        if(registeredService.contains(serviceName)) return;


        registeredService.add(serviceName);

        // ?
        Class<?>[] interfaces = service.getClass().getInterfaces();
        if(interfaces.length == 0){
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }

        for (Class<?> anInterface : interfaces) {
            serviceMap.put(anInterface.getCanonicalName(),service);
        }

        logger.info("向接口: {} 注册服务: {}",interfaces,serviceName);

    }

    @Override
    public Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        if(service == null){
            throw new RpcException(RpcError.SERVICE_NOT_FOUND);
        }
        return service;
    }
}
