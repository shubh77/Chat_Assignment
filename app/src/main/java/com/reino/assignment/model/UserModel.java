package com.reino.assignment.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.reino.assignment.db.ListConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "UserDB")
public class UserModel implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "user_pic")
    private String pic;

    @ColumnInfo(name = "user_name")
    private String name;

    @TypeConverters(ListConverter.class)
    @ColumnInfo(name = "phone_number")
    private List<String> phone;

    @ColumnInfo(name = "dob")
    private String dob;

    @ColumnInfo(name = "timeStamp")
    private String timeStamp;

    public UserModel() {
    }

    public UserModel(String pic, String name, List<String> phone, String dob) {
        this.pic = pic;
        this.name = name;
        this.phone = phone;
        this.dob = dob;
        this.timeStamp = ""+System.currentTimeMillis();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserModel userModel = (UserModel) o;
        return id == userModel.id &&
                Objects.equals(pic, userModel.pic) &&
                Objects.equals(name, userModel.name) &&
                Objects.equals(phone, userModel.phone) &&
                Objects.equals(dob, userModel.dob) &&
                Objects.equals(timeStamp, userModel.timeStamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pic, name, phone, dob, timeStamp);
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", pic='" + pic + '\'' +
                ", name='" + name + '\'' +
                ", phone=" + phone +
                ", dob='" + dob + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                '}';
    }
}
