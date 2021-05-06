package com.sixpoints.entity.user.recharge;

import com.sixpoints.entity.recharge.Pricing;
import com.sixpoints.entity.user.User;

import javax.persistence.*;

@Entity
@Table(name = "us_user_recharge")
public class UserRecharge {
    private Integer id;//流水号
    private long createTime;//订单创建时间

    private User user;//用户
    private Pricing pricing;//充值类型

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column
    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @ManyToOne
    @JoinColumn(name = "user_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @JoinColumn(name = "pricing_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    public Pricing getPricing() {
        return pricing;
    }

    public void setPricing(Pricing pricing) {
        this.pricing = pricing;
    }
}
