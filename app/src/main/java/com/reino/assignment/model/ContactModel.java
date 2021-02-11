package com.reino.assignment.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.reino.assignment.db.ListConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity(tableName = "ContactDB")
public class ContactModel {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private Integer id;

    @ColumnInfo(name = "contact_photo")
    private String photo;

    @ColumnInfo(name = "contact_name")
    private String name;

    @TypeConverters(ListConverter.class)
    @ColumnInfo(name = "contact_numbers")
    private List<String> phone;

    @ColumnInfo(name = "timeStamp")
    private String timeStamp;

    public ContactModel() {}

    public ContactModel(Integer id, String photo, String name, List<String> phone, String timeStamp) {
        this.id = id;
        this.photo = photo;
        this.name = name;
        this.phone = phone;
        this.timeStamp = timeStamp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "ContactModel{" +
                "id='" + id + '\'' +
                ", photo='" + photo + '\'' +
                ", name='" + name + '\'' +
                ", phone=" + phone +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactModel that = (ContactModel) o;
        return id.equals(that.id) &&
                Objects.equals(photo, that.photo) &&
                Objects.equals(name, that.name) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(timeStamp, that.timeStamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, photo, name, phone, timeStamp);
    }
}
