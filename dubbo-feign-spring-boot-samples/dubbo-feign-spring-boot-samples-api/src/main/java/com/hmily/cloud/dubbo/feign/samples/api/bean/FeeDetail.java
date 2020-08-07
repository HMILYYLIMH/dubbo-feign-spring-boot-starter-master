package com.hmily.cloud.dubbo.feign.samples.api.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 请输入一句美丽的描述话语
 *
 * @author hmilyylimh
 * ^_^
 * @version 0.0.1
 * ^_^
 * @date 2020/7/4
 */
public class FeeDetail implements Serializable {

    private static final long serialVersionUID = -577851853036087944L;

    private BigDecimal feeAmt;

    private String feeDesc;

    public BigDecimal getFeeAmt() {
        return feeAmt;
    }

    public void setFeeAmt(BigDecimal feeAmt) {
        this.feeAmt = feeAmt;
    }

    public String getFeeDesc() {
        return feeDesc;
    }

    public void setFeeDesc(String feeDesc) {
        this.feeDesc = feeDesc;
    }

    @Override
    public String toString() {
        return "FeeDetail{" +
                "feeAmt=" + feeAmt +
                ", feeDesc='" + feeDesc + '\'' +
                '}';
    }
}
