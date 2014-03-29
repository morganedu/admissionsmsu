/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.morgan.users;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.morgan.studentUser.Record;
import edu.morgan.studentUser.User;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author user
 */
public class IncompleteStudents {

    private Gson gson = new GsonBuilder().create();
    private String jsonObj = "";
    private ArrayList<IncompleteStudent> students = new ArrayList<>();

    public ArrayList<IncompleteStudent> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<IncompleteStudent> students) {
        this.students = students;
    }

    public void utility() throws Exception {

        BufferedReader reader = new BufferedReader(new FileReader("/Users/user/Desktop/BAFASE.json"));
        String line = "";
        String json = "";
        while ((line = reader.readLine()) != null) {
            json += line;
        }
        User user = gson.fromJson(json, User.class);
        ArrayList<Record> rec = (ArrayList) user.getRecords().getRecord();
        for (int i = 0; i < rec.size(); i++) {
            IncompleteStudent incompleteStudent = new IncompleteStudent();
            incompleteStudent.setLastName(rec.get(i).getRow().getE());
            incompleteStudent.setFirstName(rec.get(i).getRow().getC());
            incompleteStudent.setId(rec.get(i).getRow().getD());
            incompleteStudent.setChecklist(rec.get(i).getRow().getA());
            incompleteStudent.setDateOfBirth(rec.get(i).getRow().getB());
            incompleteStudent.setDateOfBirth(rec.get(i).getRow().getG());
            students.add(incompleteStudent);
        }

    }
}