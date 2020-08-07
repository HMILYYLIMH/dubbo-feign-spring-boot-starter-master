package com.hmily.cloud.dubbo.feign.samples.consumer.integration.bean;

import java.io.Serializable;
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
public class OrderInfoBean implements Serializable {

    private static final long serialVersionUID = 551337134397156918L;

    private String orderId;

    private String orderName;

    private BigDecimal orderAmt;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }

    @Override
    public String toString() {
        return "OrderInfoBean{" +
                "orderId='" + orderId + '\'' +
                ", orderName='" + orderName + '\'' +
                ", orderAmt=" + orderAmt +
                '}';
    }
}
