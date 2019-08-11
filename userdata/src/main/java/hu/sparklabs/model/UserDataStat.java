package hu.sparklabs.model;

import java.io.Serializable;

public class UserDataStat implements Serializable {

    private UserData userData;
    private Long count;

    public UserDataStat(UserData userData, Long count) {
        this.userData = userData;
        this.count = count;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "UserDataStat{" +
                "userData=" + userData +
                ", count=" + count +
                '}';
    }
}
