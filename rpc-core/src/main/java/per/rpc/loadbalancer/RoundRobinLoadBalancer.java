package per.rpc.loadbalancer;

import java.util.List;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/12 16:36
 */
public class RoundRobinLoadBalancer implements LoadBalancer{
    private int nowindex = 0;

    @Override
    public String select(List<String> znodes) {
        if(nowindex >= znodes.size()){
            nowindex %= znodes.size();
        }
        return znodes.get(nowindex++);
    }
}
