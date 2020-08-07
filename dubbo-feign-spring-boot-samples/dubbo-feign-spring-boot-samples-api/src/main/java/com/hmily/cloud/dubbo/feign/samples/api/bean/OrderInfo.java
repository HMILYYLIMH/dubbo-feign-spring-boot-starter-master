package com.hmily.cloud.dubbo.feign.samples.api.bean;

import java.io.Serializable;
import java.math.BigDecimal;
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
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 3305106976287230779L;

    private String orderId;

    private String orderName;

    private BigDecimal orderAmt;

    private ProductInfo productInfo;

    private List<FeeDetail> feeDetailList;

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

    public ProductInfo getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }

    public List<FeeDetail> getFeeDetailList() {
        return feeDetailList;
    }

    public void setFeeDetailList(List<FeeDetail> feeDetailList) {
        this.feeDetailList = feeDetailList;
    }

    @Override
    public String toString() {
        return "OrderInfo{" +
                "orderId='" + orderId + '\'' +
                ", orderName='" + orderName + '\'' +
                ", orderAmt=" + orderAmt +
                ", productInfo=" + productInfo +
                ", feeDetailList=" + feeDetailList +
                '}';
    }
}
