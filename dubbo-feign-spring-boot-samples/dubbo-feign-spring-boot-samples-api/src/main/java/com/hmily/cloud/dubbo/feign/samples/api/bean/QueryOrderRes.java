package com.hmily.cloud.dubbo.feign.samples.api.bean;

import java.io.Serializable;
import java.util.List;

/**
 * 请输入一句美丽的描述话语
 *
 * @author hmilyylimh
 * ^_^
 * @version 0.0.1
 * ^_^
 * @date 2020/7/4
 */
public class QueryOrderRes implements Serializable {

    private static final long serialVersionUID = -1646683356677606409L;

    private List<OrderInfo> orderInfoList;

    public List<OrderInfo> getOrderInfoList() {
        return orderInfoList;
    }

    public void setOrderInfoList(List<OrderInfo> orderInfoList) {
        this.orderInfoList = orderInfoList;
    }

    @Override
    public String toString() {
        return "QueryOrderRes{" +
                "orderInfoList=" + orderInfoList +
                '}';
    }
}
