package com.zxb.netty.example.udp.pojo;

import java.net.InetSocketAddress;

/**
 * 消息组件：LogEvent
 * @author Mr.zxb
 * @date 2020-05-18 21:47:28
 */
public final class LogEvent {

    public static final byte SEPARATOR = ':';

    /**
     * 发送 LogEvent 的源的 InetSocketAddress
     */
    private final InetSocketAddress source;

    /**
     * 发送的 LogEvent 的日志文件的名称
     */
    private final String logFile;

    /**
     * 消息的内容
     */
    private final String msg;

    /**
     * 接收的时间
     */
    private final long received;

    public LogEvent(String logFile, String msg) {
        this(null, -1, logFile, msg);
    }

    public LogEvent(InetSocketAddress source, long received, String logFile, String msg) {
        this.source = source;
        this.logFile = logFile;
        this.msg = msg;
        this.received = received;
    }

    public InetSocketAddress getSource() {
        return source;
    }

    public String getLogFile() {
        return logFile;
    }

    public String getMsg() {
        return msg;
    }

    public long getReceivedTimestamp() {
        return received;
    }
}
