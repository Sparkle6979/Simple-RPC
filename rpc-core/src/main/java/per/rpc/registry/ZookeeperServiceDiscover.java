package per.rpc.registry;

import com.alibaba.fastjson2.JSON;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import per.rpc.loadbalancer.LoadBalancer;
import per.rpc.loadbalancer.RoundRobinLoadBalancer;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/12 15:52
 */
public class ZookeeperServiceDiscover implements ServiceDiscover{

    private static final Logger logger = LoggerFactory.getLogger(ZookeeperServiceDiscover.class);

    private ZkClient zkClient;
    private static final String service_prefix = "/per/rpc/service/";
    // 本地Zookeeper端口
    private String ZOO_SERVER_ADDR = "localhost:2181";

    private LoadBalancer loadBalancer;

    public ZookeeperServiceDiscover(String ZOO_SERVER_ADDR){
        this.ZOO_SERVER_ADDR = ZOO_SERVER_ADDR;
        this.loadBalancer = new RoundRobinLoadBalancer();
        init(ZOO_SERVER_ADDR);
    }

    public ZookeeperServiceDiscover(String ZOO_SERVER_ADDR,LoadBalancer loadBalancer){
        this.ZOO_SERVER_ADDR = ZOO_SERVER_ADDR;
        this.loadBalancer = loadBalancer;
        init(ZOO_SERVER_ADDR);
    }

    private void init(String ZOO_SERVER_ADDR ){

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
    public InetSocketAddress lookupService(String serviceName) {
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
