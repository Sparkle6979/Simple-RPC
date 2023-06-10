package per.rpc;

import per.rpc.entity.RpcRequest;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/10 15:00
 */
public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);
}
