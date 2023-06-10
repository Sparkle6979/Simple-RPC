package per.rpc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/9 12:03
 */
@Data
@Builder
@AllArgsConstructor
public class RpcRequest implements Serializable {

    /**
     * 调用的接口名称
     */
    private String interfaceName;
    /**
     * 调用的接口方法名称
     */
    private String methodName;

    /**
     * 方法参数的类型
     */
    private Class<?>[] paraTypes;
    /**
     * 方法参数
     */
    private Object[] parameters;

}
