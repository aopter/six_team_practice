package net.onest.timestoryprj.entity;

import net.onest.timestoryprj.entity.Dynasty;

public class UserUnlockDynasty {
    private Integer id;//流水号
    private Integer progress;//答对题目个数

    private String dynasty;//朝代


    @Override
    public String toString() {
        return "UserUnlockDynasty{" +
                "id=" + id +
                ", progress=" + progress +
                ", dynasty=" + dynasty +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public String getDynasty() {
        return dynasty;
    }

    public void setDynasty(String dynasty) {
        this.dynasty = dynasty;
    }
}
