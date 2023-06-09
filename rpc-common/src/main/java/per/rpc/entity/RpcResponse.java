package per.rpc.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import per.rpc.enumeration.ResponsCode;

import java.io.Serializable;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/9 13:47
 */
@Data
@NoArgsConstructor
public class RpcResponse<T> implements Serializable {
    /**
     * 响应状态码
     */
    private Integer statusCode;
    /**
     * 响应状态的补充信息
     */
    private String message;

    /**
     * 方法返回值
     */
    private T data;


    public static <T> RpcResponse<T> success(T data){
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(ResponsCode.SUCCESS.getCode());
        response.setData(data);
        return response;
    }

    public static <T> RpcResponse<T> fail(ResponsCode code){
        RpcResponse<T> response = new RpcResponse<>();
        response.setStatusCode(code.getCode());
        response.setMessage(code.getMessage());
        return response;
    }
}
