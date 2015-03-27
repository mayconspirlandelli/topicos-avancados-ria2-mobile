package br.ufg.inf.dosador.temp;

import java.util.List;

/**
 * Created by Maycon on 02/03/2015.
 */
public class WorkLog {

    private String date;
    private String dayTotal;
    private List<Issues> issues;


    public String getDate(){
        return this.date;
    }
    public void setDate(String date){
        this.date = date;
    }
    public String getDayTotal(){
        return this.dayTotal;
    }
    public void setDayTotal(String dayTotal){
        this.dayTotal = dayTotal;
    }
    public List<Issues> getIssues() {
        return issues;
    }
    public void setIssues(List<Issues> issues) {
        this.issues = issues;
    }
}
