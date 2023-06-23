package com.schema_migrator.models;

import java.util.ArrayList;
import java.util.List;

import org.bson.codecs.pojo.annotations.BsonIgnore;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @JsonSerialize
    private String _id;

    private String firstName;
    private String lastName;
    @BsonIgnore
    private String address;
    private Integer age;
    private String emailAddress;
    private String phoneNumber;
    private ArrayList<String> preferences;
    private ArrayList<String> tags;
    private String createdDate;
    public static Integer schemaVersion = 2;

    public String get_id() {
        return _id;
    }

    public User set_id(String id) {
        this._id = id;
        return this;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public User setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return this.lastName;
    }

    public User setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getAddress() {
        return this.address;
    }

    public User setAddress(String address) {
        this.address = address;
        return this;
    }

    public Integer getAge() {
        return this.age;
    }

    public User setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public User setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
        return this;

    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public User setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;

    }

    public ArrayList<String> getPreferences() {
        return this.preferences;
    }

    public User setPreferences(ArrayList<String> preferences) {
        this.preferences = new ArrayList<String>(preferences);
        return this;

    }

    public List<String> getTags() {
        return this.tags;
    }

    public User setTags(ArrayList<String> tags) {
        this.tags = new ArrayList<String>(tags);
        return this;

    }

    public String getCreatedDate() {
        return this.createdDate;
    }

    public User setCreatedDate(String date) {
        this.createdDate = date;
        return this;
    }
}
