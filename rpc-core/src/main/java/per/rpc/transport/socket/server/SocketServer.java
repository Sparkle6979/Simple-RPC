package per.rpc.transport.socket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import per.rpc.serializer.CommonSerializer;
import per.rpc.serializer.KryoSerializer;
import per.rpc.transport.RpcServer;
import per.rpc.provider.ServiceProvider;
import per.rpc.transport.RequestHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/9 15:31
 */
public class SocketServer implements RpcServer {
    private static final Logger logger = LoggerFactory.getLogger(SocketServer.class);

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 50;
    private static final int KEEP_ALIVE_TIME = 60;
    private static final int BLOCKING_QUEUE_CAPACITY = 100;
    private final ExecutorService threadPool;
    private final ServiceProvider serviceProvider;
    private final CommonSerializer commonSerializer;

    private RequestHandler requestHandler = new RequestHandler();



    public SocketServer(ServiceProvider serviceProvider,CommonSerializer commonSerializer){
        this.commonSerializer = commonSerializer;
        this.serviceProvider = serviceProvider;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE_TIME,TimeUnit.SECONDS,workingQueue,threadFactory);
    }

    public SocketServer(ServiceProvider serviceProvider){
        this(serviceProvider,new KryoSerializer());
    }


    @Override
    public void start(int port){
        try (ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("服务器启动...");
            Socket socket;
            while ((socket = serverSocket.accept())!=null){
                logger.info("消费者连接: {}:{}",socket.getInetAddress(),socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket,requestHandler,serviceProvider,commonSerializer));

            }

            threadPool.shutdown();

        }catch (IOException e){
            logger.error("服务器启动时有错误发生",e);
        }
    }


}
