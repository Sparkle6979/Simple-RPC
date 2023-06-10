package per.rpc.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import per.rpc.entity.RpcRequest;
import per.rpc.enumeration.PackageType;
import per.rpc.serializer.CommonSerializer;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/10 17:27
 */
public class CommonEncoder extends MessageToByteEncoder {
    // 用来表示版本？
    private static final int MAGIC_NUMBER = 0xCAFEBABE;
    private final CommonSerializer serializer;

    public CommonEncoder(CommonSerializer serializer){
        this.serializer = serializer;
    }


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf byteBuf) throws Exception {
        /**
         * 自定义协议？
         */
        byteBuf.writeInt(MAGIC_NUMBER);
        if(o instanceof RpcRequest){
            byteBuf.writeInt(PackageType.REQUEST_PACK.getCode());
        }else{
            byteBuf.writeInt(PackageType.RESPONSE_PACK.getCode());
        }

        byteBuf.writeInt(serializer.getCode());
        byte[] bytes = serializer.serialize(o);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

    }
}
