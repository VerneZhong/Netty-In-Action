package com.zxb.netty.rpc.consumer;

import com.zxb.netty.rpc.api.IRpcHelloService;
import com.zxb.netty.rpc.consumer.proxy.RpcProxy;

/**
 * @author Mr.zxb
 * @date 2020-05-28 21:24:01
 */
public class RpcConsumer {

    public static void main(String[] args) {
        IRpcHelloService rpcHelloService = RpcProxy.create(IRpcHelloService.class);

        System.out.println(rpcHelloService.hello("zxb"));
    }
}
