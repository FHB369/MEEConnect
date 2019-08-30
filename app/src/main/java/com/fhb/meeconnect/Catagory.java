package com.fhb.meeconnect;

import java.util.ArrayList;

/**
 * Created by Faisal Haque Bappy on 30-Aug-19.
 */
public class Catagory {
    private String catagoryName, coverImageURL;
    private ArrayList<Faculty> faculties;
    private ArrayList<Student> students;

    public Catagory(String catagoryName, String coverImageURL, ArrayList<Faculty> faculties, ArrayList<Student> students) {
        this.catagoryName = catagoryName;
        this.coverImageURL = coverImageURL;
        this.faculties = faculties;
        this.students = students;
    }

    public String getCatagoryName() {
        return catagoryName;
    }

    public String getCoverImageURL() {
        return coverImageURL;
    }

    public ArrayList<Faculty> getFaculties() {
        return faculties;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }
}
