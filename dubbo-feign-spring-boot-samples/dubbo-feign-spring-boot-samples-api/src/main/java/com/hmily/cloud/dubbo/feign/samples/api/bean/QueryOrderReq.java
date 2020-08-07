package com.hmily.cloud.dubbo.feign.samples.api.bean;

import java.io.Serializable;

/**
 * 请输入一句美丽的描述话语
 *
 * @author hmilyylimh
 * ^_^
 * @version 0.0.1
 * ^_^
 * @date 2020/7/4
 */
public class QueryOrderReq implements Serializable {

    private static final long serialVersionUID = -4491980766140942379L;

    private OrderInfo orderInfo;

    public OrderInfo getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(OrderInfo orderInfo) {
        this.orderInfo = orderInfo;
    }

    @Override
    public String toString() {
        return "QueryOrderReq{" +
                "orderInfo=" + orderInfo +
                '}';
    }
}
