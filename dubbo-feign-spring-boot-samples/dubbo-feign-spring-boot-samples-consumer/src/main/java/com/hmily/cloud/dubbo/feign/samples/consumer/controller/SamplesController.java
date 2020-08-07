package com.hmily.cloud.dubbo.feign.samples.consumer.controller;

import com.hmily.cloud.dubbo.feign.samples.consumer.integration.SamplesFacadeClient;
import com.hmily.cloud.dubbo.feign.samples.consumer.integration.bean.OrderInfoBean;
import com.hmily.cloud.dubbo.feign.samples.consumer.integration.bean.QueryOrderReqBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;

/**
 * 请输入一句美丽的描述话语
 *
 * @author hmilyylimh
 * ^_^
 * @version 0.0.1
 * ^_^
 * @date 2020-08-01
 */
@RestController
public class SamplesController {

    @Autowired
    private SamplesFacadeClient samplesFacadeClient;

    @RequestMapping("/dubbo")
    public String dubbo2(){
        try {
            QueryOrderReqBean reqBean = new QueryOrderReqBean();
            OrderInfoBean orderInfoBean = new OrderInfoBean();
            reqBean.setOrderInfo(orderInfoBean);

            orderInfoBean.setOrderAmt(new BigDecimal("1000000"));
            orderInfoBean.setOrderId("HH20200101");
            orderInfoBean.setOrderName("hmilyylimh");

            return samplesFacadeClient.queryRemoteOrder(reqBean).toString();
        } catch (Exception e) {
            return "异常";
        }
    }

    @PostConstruct
    public void refresh(){

        try {
            QueryOrderReqBean reqBean = new QueryOrderReqBean();
            OrderInfoBean orderInfoBean = new OrderInfoBean();
            reqBean.setOrderInfo(orderInfoBean);

            orderInfoBean.setOrderAmt(new BigDecimal("1000000"));
            orderInfoBean.setOrderId("HH20200101");
            orderInfoBean.setOrderName("hmilyylimh");

            System.out.println(samplesFacadeClient.queryRemoteOrder(reqBean).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
