package per.rpc.loadbalancer;

import java.util.List;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/12 16:31
 */
public interface LoadBalancer {
    String select(List<String> znodes);
}
