package per.rpc.transport.socket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import per.rpc.serializer.CommonSerializer;
import per.rpc.serializer.KryoSerializer;
import per.rpc.transport.RpcClient;
import per.rpc.entity.RpcRequest;
import per.rpc.entity.RpcResponse;
import per.rpc.container.ServiceDiscover;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/9 15:16
 */
public class SocketClient implements RpcClient{
    private static final Logger logger = LoggerFactory.getLogger(RpcClient.class);

    private final ServiceDiscover serviceDiscover;

    private final CommonSerializer commonSerializer;



    public SocketClient(ServiceDiscover serviceDiscover){
        this(serviceDiscover,new KryoSerializer());
    }

    public SocketClient(ServiceDiscover serviceDiscover,CommonSerializer commonSerializer){
        this.commonSerializer = commonSerializer;
        this.serviceDiscover = serviceDiscover;
    }


    @Override
    public Object sendRequest(RpcRequest rpcRequest) {

        InetSocketAddress inetSocketAddress  =  serviceDiscover.lookupService(rpcRequest.getInterfaceName());

        try(Socket socket = new Socket(inetSocketAddress.getHostName(),inetSocketAddress.getPort())){

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            byte[] bytes = commonSerializer.serialize(rpcRequest);

            objectOutputStream.writeObject(bytes);
            objectOutputStream.flush();

            return ((RpcResponse)objectInputStream.readObject()).getData();
        }catch (IOException | ClassNotFoundException e){
            logger.error("调用时有错误发生：",e);
            return null;
        }
    }
}
