package io.tpd.kafkaexample;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class UserChangeDto {
    @JsonProperty("REGISTERTIME")
    private LocalDateTime registerTime;
    @JsonProperty("GENDER")
    private String gender;
    @JsonProperty("REGIONID")
    private String regionId;

    public UserChangeDto() {
    }

    public LocalDateTime getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(LocalDateTime registerTime) {
        this.registerTime = registerTime;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    @Override
    public String toString() {
        return "UserChangeDto{" +
                " registertime=" + registerTime +
                ", gender='" + gender + '\'' +
                ", regionid='" + regionId + '\'' +
                '}';
    }
}
