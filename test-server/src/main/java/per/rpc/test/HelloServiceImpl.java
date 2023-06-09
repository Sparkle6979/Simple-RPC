package per.rpc.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import per.rpc.api.HelloObject;
import per.rpc.api.HelloService;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/9 16:16
 */
public class HelloServiceImpl implements HelloService {

    private static final Logger logger = LoggerFactory.getLogger(HelloServiceImpl.class);


    @Override
    public String Hello(HelloObject object) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("接收到：{}", object.getMessage());
        return "这是调用的返回值，id=" + object.getId();
    }
}
