package com.sixpoints.entity.recharge;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "re_pricing")
public class Pricing implements Serializable {
    private Integer pricingId;//定价标识符
    private Integer pricingMoney;//金额
    private Integer pricingCount;//积分数量

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public Integer getPricingId() {
        return pricingId;
    }

    public void setPricingId(Integer pricingId) {
        this.pricingId = pricingId;
    }

    @Column
    public Integer getPricingMoney() {
        return pricingMoney;
    }

    public void setPricingMoney(Integer pricingMoney) {
        this.pricingMoney = pricingMoney;
    }

    @Column
    public Integer getPricingCount() {
        return pricingCount;
    }

    public void setPricingCount(Integer pricingCount) {
        this.pricingCount = pricingCount;
    }
}
