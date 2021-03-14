package com.sixpoints.entity.user.dynasty;

import com.sixpoints.entity.dynasty.Dynasty;

import javax.persistence.*;

@Entity
@Table(name = "us_user_unlock_dynasty")
public class UserUnlockDynasty {
    private Integer id;//流水号
    private Integer progress;//答对题目个数

    private Dynasty dynasty;//朝代

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
    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    @ManyToOne
    @JoinColumn(name = "dynasty_id",foreignKey = @ForeignKey(name = "none", value = ConstraintMode.NO_CONSTRAINT))
    public Dynasty getDynasty() {
        return dynasty;
    }

    public void setDynasty(Dynasty dynasty) {
        this.dynasty = dynasty;
    }
}
