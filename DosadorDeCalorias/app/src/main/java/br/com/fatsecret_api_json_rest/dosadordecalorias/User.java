package br.com.fatsecret_api_json_rest.dosadordecalorias;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Maycon on 02/03/2015.
 */
public class User {


    private String timeSpent;
    private List<WorkLog> worklogs;
   // private String name;

    public List<WorkLog> getWorklogs() {
        return worklogs;
    }
    public void setWorklogs(List<WorkLog> worklogs) {
        this.worklogs = worklogs;
    }
    public String getTimeSpent() {
            return timeSpent;
        }
    public void setTimeSpent(String timeSpent) {
            this.timeSpent = timeSpent;
        }
    /*
    public String getName() {
            return name;
        }
    public void setName(String name) {
            this.name = name;
        }
        */
}
