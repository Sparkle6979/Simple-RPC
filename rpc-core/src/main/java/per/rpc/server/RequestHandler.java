package per.rpc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import per.rpc.entity.RpcRequest;
import per.rpc.entity.RpcResponse;
import per.rpc.enumeration.ResponsCode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/10 14:09
 */
public class RequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    /**
     * 调用实现类的方法
     * @param rpcRequest
     * @param service
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private Object invokeTargetMethod(RpcRequest rpcRequest,Object service) throws InvocationTargetException, IllegalAccessException {
        Method method;
        try {
            method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParaTypes());
        } catch (NoSuchMethodException e) {
            return RpcResponse.fail(ResponsCode.METHOD_NOT_FOUND);
        }
        return method.invoke(service, rpcRequest.getParameters());
    }


    public Object handle(RpcRequest rpcRequest,Object service){
        Object result = null;

        try {
            result = invokeTargetMethod(rpcRequest,service);
            logger.info("服务:{} 成功调用方法:{}",rpcRequest.getInterfaceName(),rpcRequest.getMethodName());
        } catch (InvocationTargetException | IllegalAccessException e) {
            logger.info("调用或发送时有错误发生: ",e);
        }
        return result;
    }


}
