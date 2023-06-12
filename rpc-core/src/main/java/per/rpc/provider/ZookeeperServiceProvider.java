package per.rpc.provider;

import com.alibaba.fastjson2.JSON;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import per.rpc.enumeration.RpcError;
import per.rpc.exception.RpcException;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/11 18:31
 */
@Deprecated
public class ZookeeperServiceProvider implements ServiceProvider {

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperServiceProvider.class);
    private static final int HEART_TIME = 150000;
    private static final String service_prefix = "/per/rpc/service/";
    private ZkClient zkClient;
    private String ZOO_SERVER_ADDR;
    private InetSocketAddress inetSocketAddress;

    public ZookeeperServiceProvider(String ZOO_SERVER_ADDR, InetSocketAddress inetSocketAddress){
        init(ZOO_SERVER_ADDR,inetSocketAddress);
    }

    private void init(String ZOO_SERVER_ADDR,InetSocketAddress inetSocketAddress){
        this.inetSocketAddress = inetSocketAddress;
        this.ZOO_SERVER_ADDR = ZOO_SERVER_ADDR;
        zkClient = new ZkClient(ZOO_SERVER_ADDR);

        zkClient.setZkSerializer(new ZkSerializer() {
            @Override
            public byte[] serialize(Object o) throws ZkMarshallingError {
                return String.valueOf(o).getBytes(StandardCharsets.UTF_8);
            }

            @Override
            public Object deserialize(byte[] bytes) throws ZkMarshallingError {
                return new String(bytes, StandardCharsets.UTF_8);
            }
        });
    }



    @Override
    public <T> void register(T service) {


        // 创建 ZK 永久节点（服务节点）

        Class<?>[] interfaces = service.getClass().getInterfaces();
        if(interfaces.length == 0){
            throw new RpcException(RpcError.SERVICE_NOT_IMPLEMENT_ANY_INTERFACE);
        }

        for (Class<?> anInterface : interfaces) {
            String serviceName = anInterface.getSimpleName();
            String servicePath = service_prefix + serviceName;

            if (!zkClient.exists(servicePath)) {
                zkClient.createPersistent(servicePath, true);
                logger.info("Created node: {}", servicePath);
            }


            String uri = JSON.toJSONString(inetSocketAddress);
            try {
                uri = URLEncoder.encode(uri,"UTF-8");
                String uriPath = servicePath + "/" + uri;
                if(zkClient.exists(uriPath)){
                    zkClient.delete(uriPath);
                }
                zkClient.createEphemeral(uriPath);

            } catch (UnsupportedEncodingException e) {
                logger.error("注册服务时有错误发生",e);
            }
        }

    }

    @Override
    public Object getService(String serviceName) {
        String servicePath = service_prefix + serviceName;
        List<String> childrenNode = zkClient.getChildren(servicePath);
        String uri0 = childrenNode.get(0);
        try {
            String serviceInstanceJson = URLDecoder.decode(uri0,"UTF-8");
            return JSON.parseObject(serviceInstanceJson, InetSocketAddress.class);
        } catch (UnsupportedEncodingException e) {
            logger.error("获取服务时有错误发生",e);
        }
        return null;
    }
}
