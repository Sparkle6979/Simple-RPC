package per.rpc.transport.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import per.rpc.transport.RpcClient;
import per.rpc.codec.CommonDecoder;
import per.rpc.codec.CommonEncoder;
import per.rpc.entity.RpcRequest;
import per.rpc.entity.RpcResponse;
import per.rpc.serializer.JsonSerializer;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/10 15:40
 */
public class NettyClient implements RpcClient {
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

    private String host;
    private int port;
    private static final Bootstrap bootstarp;

    public NettyClient(String host,int port){
        this.host = host;
        this.port = port;
    }

    static {
        EventLoopGroup group = new NioEventLoopGroup();
        bootstarp = new Bootstrap();
        bootstarp.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new CommonDecoder())
                                .addLast(new CommonEncoder(new JsonSerializer()))
                                .addLast(new NettyClientHandler());
                    }
                });
    }

    @Override
    public Object sendRequest(RpcRequest rpcRequest) {
        try {
            ChannelFuture future = bootstarp.connect(host,port).sync();
            logger.info("客户端连接到服务器 {}:{}", host, port);
            Channel channel = future.channel();

            if(channel != null){
                channel.writeAndFlush(rpcRequest).addListener(
                        future1 -> {
                            if(future1.isSuccess()) {
                                logger.info(String.format("客户端发送消息: %s", rpcRequest.toString()));
                            } else {
                                logger.error("发送消息时有错误发生: ", future1.cause());
                            }
                        }
                );

                channel.closeFuture().sync();
                AttributeKey<RpcResponse> key = AttributeKey.valueOf("rpcResponse");
                RpcResponse response = channel.attr(key).get();
                return response.getData();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
