package com.fhb.meeconnect.DataElements;

/**
 * Created by Faisal Haque Bappy on 30-Aug-19.
 */
public class Student {

    private String registrationNo, bloodGroup, birthday, messengerID, name, nickname, phone, photoURL;

    public Student(String registrationNo, String bloodGroup, String birthday, String messengerID, String name, String nickname, String phone, String photoURL) {
        this.registrationNo = registrationNo;
        this.bloodGroup = bloodGroup;
        this.birthday = birthday;
        this.messengerID = messengerID;
        this.name = name;
        this.nickname = nickname;
        this.phone = phone;
        this.photoURL = photoURL;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getMessengerID() {
        return messengerID;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhotoURL() {
        return photoURL;
    }
}
