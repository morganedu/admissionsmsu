/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.morgan.users;

/**
 *
 * @author pablohpsilva
 */
public class PrettyStudentPrint {
    private String studentInfo;
    private String foundChecklist = "";
    private String notFoundChecklist = "";

    public PrettyStudentPrint(String name){
        this.setStudentInfo(name);
    }
    
    /**
     * @return the studentInfo
     */
    public String getStudentInfo() {
        return studentInfo;
    }

    /**
     * @param studentInfo the studentInfo to set
     */
    public void setStudentInfo(String studentInfo) {
        this.studentInfo = studentInfo;
    }

    /**
     * @return the foundChecklist
     */
    public String getFoundChecklist() {
        return foundChecklist;
    }

    /**
     * @param foundChecklist the foundChecklist to set
     */
    public void setFoundChecklist(String foundChecklist) {
        if(this.foundChecklist.equals(""))
            this.foundChecklist = foundChecklist;
        else
            this.foundChecklist += ", " + foundChecklist;
    }

    /**
     * @return the notFoundChecklist
     */
    public String getNotFoundChecklist() {
        return notFoundChecklist;
    }

    /**
     * @param notFoundChecklist the notFoundChecklist to set
     */
    public void setNotFoundChecklist(String notFoundChecklist) {
        if(this.notFoundChecklist.equals(""))
            this.notFoundChecklist = notFoundChecklist;
        else
            this.notFoundChecklist += ", " + notFoundChecklist;
    }
}
