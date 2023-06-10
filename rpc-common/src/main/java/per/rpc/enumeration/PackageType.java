package per.rpc.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author sparkle6979l
 * @version 1.0
 * @data 2023/6/10 18:08
 */

@Getter
@AllArgsConstructor
public enum PackageType {

    REQUEST_PACK(0),
    RESPONSE_PACK(1);

    private final int code;
}
