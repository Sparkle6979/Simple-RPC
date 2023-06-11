package per.rpc.socket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import per.rpc.RpcServer;
import per.rpc.registry.ServiceRegistry;
import per.rpc.RequestHandler;

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
    private RequestHandler requestHandler = new RequestHandler();

    private final ServiceRegistry serviceRegistry;

    private final ServiceRegistry serviceProvider;


    public SocketServer(ServiceRegistry serviceRegistry,ServiceRegistry serviceProvider){
        this.serviceRegistry = serviceRegistry;
        this.serviceProvider = serviceProvider;

        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(BLOCKING_QUEUE_CAPACITY);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,KEEP_ALIVE_TIME,TimeUnit.SECONDS,workingQueue,threadFactory);
    }


//    public void register(Object service,int port){
//        try (ServerSocket serverSocket = new ServerSocket(port)){
//            logger.info("服务器正在启动");
//            Socket socket;
//            while ((socket = serverSocket.accept())!=null){
//                logger.info("客户端连接！ IP为: " + socket.getInetAddress());
//                threadPool.execute(new WorkerThread(socket,service));
//            }
//        }catch (Exception e){
//            logger.error("连接时有错误发生",e);
//        }
//    }
    @Override
    public void start(int port){
        try (ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("服务器启动...");
            Socket socket;
            while ((socket = serverSocket.accept())!=null){
                logger.info("消费者连接: {}:{}",socket.getInetAddress(),socket.getPort());
                threadPool.execute(new RequestHandlerThread(socket,requestHandler,serviceProvider));

            }

            threadPool.shutdown();

        }catch (IOException e){
            logger.error("服务器启动时有错误发生",e);
        }
    }


}
