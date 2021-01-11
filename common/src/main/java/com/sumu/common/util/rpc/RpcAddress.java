package com.sumu.common.util.rpc;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-22 21:19
 */
public class RpcAddress {

    private String ip;

    private int port;

    public RpcAddress(String ip, int  port) {
        this.ip = ip;
        this.port = port;
    }

    public String getRpcAddress() {
        return "http://" + ip + ":" + port;
    }
}
