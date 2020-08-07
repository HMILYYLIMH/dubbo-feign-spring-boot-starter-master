package com.hmily.cloud.dubbo.feign.samples.api;

import com.hmily.cloud.dubbo.feign.samples.api.bean.QueryOrderReq;
import com.hmily.cloud.dubbo.feign.samples.api.bean.QueryOrderRes;

/**
 * 请输入一句美丽的描述话语
 *
 * @author hmilyylimh
 * ^_^
 * @version 0.0.1
 * ^_^
 * @date 2020-08-01
 */
public interface SamplesFacade {

    String hello(String name);

    QueryOrderRes queryOrder(QueryOrderReq req);
}