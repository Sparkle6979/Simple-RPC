package per.rpc.test;

import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import per.rpc.api.HelloObject;
import per.rpc.api.HelloService;
import per.rpc.client.RpcClientProxy;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/9 16:12
 */
public class TestClient {
    public static void main(String[] args) {
        RpcClientProxy proxy = new RpcClientProxy("localhost",9000);
        HelloService helloService = proxy.getProxy(HelloService.class);

        for (int i = 0; i < 3; i++) {
            new Thread(() -> {
                HelloObject object = new HelloObject(12,"This is a message");
                String res = helloService.Hello(object);
                System.out.println(res);
            }).start();

        }


    }



}
