package com.zxb.netty.rpc.provider;

import com.zxb.netty.rpc.api.IRpcService;

/**
 * @author Mr.zxb
 * @date 2020-05-28 20:40:24
 */
public class RpcServiceImpl implements IRpcService {
    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int subtract(int a, int b) {
        return a - b;
    }

    @Override
    public int multiply(int a, int b) {
        return a * b;
    }

    @Override
    public int divide(int a, int b) {
        return a / b;
    }
}
