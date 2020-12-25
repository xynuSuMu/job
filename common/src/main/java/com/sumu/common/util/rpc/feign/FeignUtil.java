package com.sumu.common.util.rpc.feign;

import com.sumu.common.util.rpc.RpcAddress;
import com.sumu.common.util.rpc.RpcResult;
import feign.Feign;
import feign.Request;
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
                .decoder(new JacksonDecoder())
                .options(new Request.Options(5000, 8000))
        ;
    }


    public static RpcResult jobNotify(RpcAddress rpcAddress, String handlerName, String jobInstanceID) {
        return jobNotify(rpcAddress, handlerName, jobInstanceID, null, null);
    }

    public static RpcResult jobNotify(RpcAddress rpcAddress, String handlerName, String jobInstanceID, String shardIndex, String shardTotal) {
        String address = rpcAddress.getRpcAddress();
        Function<FeignRequest, RpcResult> function = (feignRequest) -> {
            return feignRequest.jobNotify(handlerName, jobInstanceID, shardIndex, shardTotal);
        };
        return send(address, function);
    }


    private static RpcResult send(String address, Function<FeignRequest, RpcResult> function) {
        FeignRequest request = builder.target(FeignRequest.class, address);
        try {
            RpcResult rpcResult = function.apply(request);
            return rpcResult;
        } catch (Throwable e) {
            String msg = e.getMessage();
            return RpcResult.fail(msg);
        }

    }
}
