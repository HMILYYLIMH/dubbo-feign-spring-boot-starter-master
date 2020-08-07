package com.hmily.cloud.dubbo.feign.samples.consumer.integration;

import com.hmily.cloud.dubbo.feign.core.DubboFeignClient;
import com.hmily.cloud.dubbo.feign.core.DubboMethod;
import com.hmily.cloud.dubbo.feign.samples.api.SamplesFacade;
import com.hmily.cloud.dubbo.feign.samples.api.bean.QueryOrderReq;
import com.hmily.cloud.dubbo.feign.samples.api.bean.QueryOrderRes;
import com.hmily.cloud.dubbo.feign.samples.consumer.integration.bean.QueryOrderReqBean;

/**
 * <h1>样例类。</h1>
 *
 * @author HEHUI231
 * ^_^
 * @version 0.0.1
 * ^_^
 * @date 2020-08-01
 */
@DubboFeignClient(remoteClass = SamplesFacade.class)
public interface SamplesFacadeClient {

    String hello(String name);

    @DubboMethod(remoteMethodName = "queryOrder",
                 remoteMethodParamsTypeName = {"com.hmily.cloud.dubbo.feign.samples.api.bean.QueryOrderReq"}
                 )
    QueryOrderRes queryRemoteOrder(QueryOrderReqBean req);
}
