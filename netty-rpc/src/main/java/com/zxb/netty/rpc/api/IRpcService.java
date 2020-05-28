package com.zxb.netty.rpc.api;

/**
 * RPC Service
 * @author Mr.zxb
 * @date 2020-05-28 20:35:00
 */
public interface IRpcService {

    /**
     * 加
     * @param a
     * @param b
     * @return
     */
    int add(int a, int b);

    /**
     * 减
     * @param a
     * @param b
     * @return
     */
    int subtract(int a, int b);

    /**
     * 乘
     * @param a
     * @param b
     * @return
     */
    int multiply(int a, int b);

    /**
     * 除
     * @param a
     * @param b
     * @return
     */
    int divide(int a, int b);
}
