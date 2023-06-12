package per.rpc.transport.netty.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import per.rpc.transport.RequestHandler;
import per.rpc.entity.RpcRequest;
import per.rpc.entity.RpcResponse;
import per.rpc.provider.DefaultServiceProvider;
import per.rpc.provider.ServiceProvider;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/10 18:55
 */
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {
    private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

    private static RequestHandler requestHandler;
    private static ServiceProvider serviceProvider;

    static {
        requestHandler = new RequestHandler();
        serviceProvider = new DefaultServiceProvider();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        try {
            logger.info("服务器接收到请求: {}", rpcRequest);
            String interfaceName = rpcRequest.getInterfaceName();
            Object service = serviceProvider.getService(interfaceName);
            Object result = requestHandler.handle(rpcRequest, service);
            ChannelFuture future = channelHandlerContext.writeAndFlush(RpcResponse.success(result));
            future.addListener(ChannelFutureListener.CLOSE);
        }finally {
            ReferenceCountUtil.release(rpcRequest);
        }

    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }
}
