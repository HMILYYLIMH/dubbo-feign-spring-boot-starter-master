package com.hmily.cloud.dubbo.feign.samples.consumer.integration.bean;

import com.hmily.cloud.dubbo.feign.samples.api.bean.OrderInfo;

import java.io.Serializable;

/**
 * 请输入一句美丽的描述话语
 *
 * @author HEHUI231
 * ^_^
 * @version 0.0.1
 * ^_^
 * @date 2020-08-01
 */
public class QueryOrderReqBean implements Serializable {

    private static final long serialVersionUID = 9001515557295711773L;

    private OrderInfoBean orderInfo;

    public OrderInfoBean getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfoBean orderInfo) {
        this.orderInfo = orderInfo;
    }

    @Override
    public String toString() {
        return "QueryOrderReqBean{" +
                "orderInfo=" + orderInfo +
                '}';
    }
}
