package per.rpc.exception;

import per.rpc.enumeration.RpcError;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/9 22:05
 */
public class RpcException extends RuntimeException{
    public RpcException(RpcError error, String detail) {
        super(error.getMessage() + ": " + detail);
    }
    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }
    public RpcException(RpcError error) {
        super(error.getMessage());
    }

}
