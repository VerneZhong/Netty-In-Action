package com.zxb.netty.channelhandler;

import io.netty.util.ResourceLeakDetector;

/**
 * {@link ResourceLeakDetector} 它将对你应用程序的缓冲区分配做大约1%的采样来检测内存泄漏，相关的开销是非常小的。
 * <p>
 *     Netty 提供了4种泄漏检测级别
 *     <ul>
 *         <li>DISABLED：禁用泄漏检测。只有在详尽的测试之后才应设置为这个值</li>
 *         <li>SIMPLE：使用1%的默认采样率检测并报告任何发现的泄漏。默认级别，适合大部分的情况</li>
 *         <li>ADVANCED：使用默认的采样率，报告所发现的任何的泄漏以及对应的消息被访问的位置</li>
 *         <li>PARANOID：类似于 ADVANCED，但是其将会对每次访问都进行采样。对性能有很大的影响，适用于调试阶段</li>
 *     </ul>
 *     开启：-Dio.netty.leakDetectionLevel=ADVANCED
 * </p>
 *
 * @author Mr.zxb
 * @date 2020-04-28
 **/
public class MemoryLeakDetection {

}
