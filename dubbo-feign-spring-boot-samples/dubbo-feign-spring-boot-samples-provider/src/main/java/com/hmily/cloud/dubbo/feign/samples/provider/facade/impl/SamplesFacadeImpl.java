package com.hmily.cloud.dubbo.feign.samples.provider.facade.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.hmily.cloud.dubbo.feign.samples.api.SamplesFacade;
import com.hmily.cloud.dubbo.feign.samples.api.bean.OrderInfo;
import com.hmily.cloud.dubbo.feign.samples.api.bean.QueryOrderReq;
import com.hmily.cloud.dubbo.feign.samples.api.bean.QueryOrderRes;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 请输入一句美丽的描述话语
 *
 * @author hmilyylimh
 * ^_^
 * @version 0.0.1
 * ^_^
 * @date 2020-08-01
 */
@Service
@Component
public class SamplesFacadeImpl implements SamplesFacade {

    @Override
    public String hello(String name) {
        String s1 = "Hello " + name;
        System.out.printf(s1);
        return s1;
    }

    @Override
    public QueryOrderRes queryOrder(QueryOrderReq req) {
        QueryOrderRes res = new QueryOrderRes();

        List<OrderInfo> orderInfoList = new ArrayList<>();
        res.setOrderInfoList(orderInfoList);

        OrderInfo orderInfo = req.getOrderInfo();
        orderInfo.setOrderId("Resp_" + orderInfo.getOrderId());
        orderInfo.setOrderName("Resp_" + orderInfo.getOrderName());
        orderInfoList.add(orderInfo);

        return res;
    }
}
