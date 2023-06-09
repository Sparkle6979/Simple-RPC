package per.rpc.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/9 15:31
 */
public class RpcServer {
    private static final Logger logger = LoggerFactory.getLogger(RpcServer.class);

    private final ExecutorService threadPool;

    public RpcServer(){
        int corePoolSize = 5;
        int maximumPoolSize = 50;

        long keepAliveTime = 60;
        BlockingQueue<Runnable> workingQueue = new ArrayBlockingQueue<>(100);
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,TimeUnit.SECONDS,workingQueue,threadFactory);
    }


    public void register(Object service,int port){
        try (ServerSocket serverSocket = new ServerSocket(port)){
            logger.info("服务器正在启动");
            Socket socket;
            while ((socket = serverSocket.accept())!=null){
                logger.info("客户端连接！ IP为: " + socket.getInetAddress());
                threadPool.execute(new WorkerThread(socket,service));
            }
        }catch (Exception e){
            logger.error("连接时有错误发生",e);
        }
    }


}
