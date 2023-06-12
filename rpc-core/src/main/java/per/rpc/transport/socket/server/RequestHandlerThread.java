package per.rpc.transport.socket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import per.rpc.serializer.CommonSerializer;
import per.rpc.transport.RequestHandler;
import per.rpc.entity.RpcRequest;
import per.rpc.entity.RpcResponse;
import per.rpc.provider.ServiceProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/10 14:03
 */
public class RequestHandlerThread implements Runnable{

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private Socket socket;
    private RequestHandler requestHandler;
    private ServiceProvider serviceProvider;
    private CommonSerializer commonSerializer;


    public RequestHandlerThread(Socket socket, RequestHandler requestHandler, ServiceProvider serviceProvider, CommonSerializer commonSerializer){
        this.socket = socket;
        this.requestHandler = requestHandler;
        this.serviceProvider = serviceProvider;
        this.commonSerializer = commonSerializer;
    }


    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())){

            byte[] bytes = (byte[])objectInputStream.readObject();
            RpcRequest rpcRequest = (RpcRequest) commonSerializer.deserialize(bytes,RpcRequest.class);

            String interfaceName = rpcRequest.getInterfaceName();
            Object service = serviceProvider.getService(interfaceName);
            Object result = requestHandler.handle(rpcRequest, service);
            objectOutputStream.writeObject(RpcResponse.success(result));
            objectOutputStream.flush();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
