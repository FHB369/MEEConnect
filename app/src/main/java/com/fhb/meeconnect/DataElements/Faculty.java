package com.fhb.meeconnect.DataElements;

/**
 * Created by Faisal Haque Bappy on 30-Aug-19.
 */
public class Faculty {
    private String id, bloodGroup, birthday, designation, email, messengerID, name, phone, photoURL;

    public Faculty(String id, String bloodGroup, String birthday, String designation, String email, String messengerID, String name, String phone, String photoURL) {
        this.id = id;
        this.bloodGroup = bloodGroup;
        this.birthday = birthday;
        this.designation = designation;
        this.email = email;
        this.messengerID = messengerID;
        this.name = name;
        this.phone = phone;
        this.photoURL = photoURL;
    }

    public String getId() {
        return id;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getDesignation() {
        return designation;
    }

    public String getEmail() {
        return email;
    }

    public String getMessengerID() {
        return messengerID;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhotoURL() {
        return photoURL;
    }
}
