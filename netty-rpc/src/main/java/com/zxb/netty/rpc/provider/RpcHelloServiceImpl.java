package com.zxb.netty.rpc.provider;

import com.zxb.netty.rpc.api.IRpcHelloService;

/**
 * @author Mr.zxb
 * @date 2020-05-28 20:39:50
 */
public class RpcHelloServiceImpl implements IRpcHelloService {
    @Override
    public String hello(String name) {
        return "Hello " + name + "!";
    }
}
