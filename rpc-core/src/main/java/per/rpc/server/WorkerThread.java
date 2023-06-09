package per.rpc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import per.rpc.entity.RpcRequest;
import per.rpc.entity.RpcResponse;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/9 15:44
 */
public class WorkerThread implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(WorkerThread.class);

    private Socket socket;

    // 接口
    private Object service;

    public WorkerThread(Socket socket,Object service){
        this.socket = socket;
        this.service = service;
    }


    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())){

            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParaTypes());

            Object returnObject = method.invoke(service,rpcRequest.getParameters());

            objectOutputStream.writeObject(RpcResponse.success(returnObject));
            objectOutputStream.flush();

        }catch (IOException | ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.error("调用或发送时有错误发生：", e);
        }

    }
}
