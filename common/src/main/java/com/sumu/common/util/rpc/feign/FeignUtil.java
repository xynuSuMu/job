package com.sumu.common.util.rpc.feign;

import com.sumu.common.util.rpc.RpcAddress;
import com.sumu.common.util.rpc.RpcResult;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;

import java.util.function.Function;

/**
 * @author 陈龙
 * @version 1.0
 * @date 2020-12-22 20:44
 */
public class FeignUtil {


    private static Feign.Builder builder;


    static {
        builder = Feign.builder()
                .encoder(new JacksonEncoder())//使用Jackson进行参数处理
                .decoder(new JacksonDecoder());
    }

    public static RpcResult jobNotify(RpcAddress rpcAddress, String handlerName) {
        String address = rpcAddress.getRpcAddress();
        Function<FeignRequest, RpcResult> function = (feignRequest) -> {
            return feignRequest.fire(handlerName);
        };
        return send(address, function);
    }


    private static RpcResult send(String address, Function<FeignRequest, RpcResult> function) {
        FeignRequest request = builder.target(FeignRequest.class, address);
        RpcResult rpcResult = function.apply(request);
        return rpcResult;
    }
}
