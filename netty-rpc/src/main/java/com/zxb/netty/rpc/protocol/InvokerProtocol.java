package com.zxb.netty.rpc.protocol;

import lombok.Data;

import java.io.Serializable;

/**
 * 自定义传输协议
 * @author Mr.zxb
 * @date 2020-05-28 20:41:52
 */
@Data
public class InvokerProtocol implements Serializable {

    /**
     * 类名
     */
    private String className;

    /**
     * 函数名称
     */
    private String methodName;

    /**
     * 参数列表
     */
    private Class<?>[] params;

    /**
     * 参数值列表
     */
    private Object[] values;
}
