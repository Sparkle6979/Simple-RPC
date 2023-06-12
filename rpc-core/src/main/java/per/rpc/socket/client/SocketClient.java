package per.rpc.socket.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import per.rpc.RpcClient;
import per.rpc.entity.RpcRequest;
import per.rpc.entity.RpcResponse;
import per.rpc.provider.ServiceProvider;
import per.rpc.registry.ServiceDiscover;

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

    public SocketClient(ServiceDiscover serviceDiscover){
        this.serviceDiscover = serviceDiscover;
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {

        InetSocketAddress inetSocketAddress  =  serviceDiscover.lookupService(rpcRequest.getInterfaceName());

        try(Socket socket = new Socket(inetSocketAddress.getHostName(),inetSocketAddress.getPort())){

            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());

            objectOutputStream.writeObject(rpcRequest);
            objectOutputStream.flush();

            return ((RpcResponse)objectInputStream.readObject()).getData();
        }catch (IOException | ClassNotFoundException e){
            logger.error("调用时有错误发生：",e);
            return null;
        }
    }
}
