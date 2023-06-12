package per.rpc.loadbalancer;

import java.util.List;
import java.util.Random;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/12 16:31
 */
public class RandomLoadBalancer implements LoadBalancer{
    @Override
    public String select(List<String> znodes) {
        int randomIndex = new Random().nextInt(znodes.size());
        return znodes.get(randomIndex);
    }
}
