/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.morgan.users;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.morgan.studentUser.Record;
import edu.morgan.studentUser.Records;
import edu.morgan.studentUser.Row;
import edu.morgan.studentUser.User;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        BufferedReader reader = new BufferedReader(new FileReader("/Users/pablohpsilva/Downloads/BAFASE_min.json"));
        String line = "";
        String json = "";
        while ((line = reader.readLine()) != null) {
            json += line;
        }
        User user = gson.fromJson(json, User.class);
        ArrayList<Record> rec = (ArrayList) user.getRecords().getRecord();
        for (int i = 0; i < rec.size(); i++) {
            IncompleteStudent incompleteStudent = new IncompleteStudent();
            incompleteStudent.setLastName(rec.get(i).getRow().getE() != null ? rec.get(i).getRow().getE() : "");
            incompleteStudent.setFirstName(rec.get(i).getRow().getC() != null ? rec.get(i).getRow().getC() : "");
            incompleteStudent.setId(rec.get(i).getRow().getD() != null ? rec.get(i).getRow().getD() : "");
            incompleteStudent.setChecklist(rec.get(i).getRow().getA() != null ? rec.get(i).getRow().getA() : "");
            incompleteStudent.setDateOfBirth(rec.get(i).getRow().getB() != null ? rec.get(i).getRow().getB() : "");
            incompleteStudent.setTerm(rec.get(i).getRow().getG() != null ? rec.get(i).getRow().getG() : "");
            students.add(incompleteStudent);
        }
    }
    
    public User convertToUsers(ArrayList<IncompleteStudent> incompleteStudents) throws Exception {
        Records records = new Records();
        List<Record> record = new ArrayList<>();
        User user = new User();
        for(IncompleteStudent student : incompleteStudents){
            Record auxRecord = new Record();
            Row row = new Row();
            row.setE(student.getLastName());
            row.setC(student.getFirstName());
            row.setD(student.getId());
            row.setA(student.getChecklist());
            row.setB(student.getDateOfBirth());
            row.setG(student.getTerm());
            row.setT(student.getType());
            auxRecord.setRow(row);
            record.add(auxRecord);
        }
        records.setRecord(record);
        user.setRecords(records);
        return user;
    }
    
    public void generateJSON(User user, String name){
        String json = this.gson.toJson(user);
        try {
		//write converted json data to a file named "file.json"
		FileWriter writer = new FileWriter("/Users/pablohpsilva/Downloads/"+ name +".json");
		writer.write(json);
		writer.close();
 
	} catch (IOException e) {
		e.printStackTrace();
	}
        System.out.println(json);
    }
}
