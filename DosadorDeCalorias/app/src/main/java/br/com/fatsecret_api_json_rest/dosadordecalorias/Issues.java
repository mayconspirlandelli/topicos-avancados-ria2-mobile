package br.com.fatsecret_api_json_rest.dosadordecalorias;

/**
 * Created by Maycon on 02/03/2015.
 */
public class Issues {
    private String comment;
    private String issueCode;
    private String timeSpent;

    public String getComment(){
        return this.comment;
    }
    public void setComment(String comment){
        this.comment = comment;
    }
    public String getIssueCode(){
        return this.issueCode;
    }
    public void setIssueCode(String issueCode){
        this.issueCode = issueCode;
    }
    public String getTimeSpent(){
        return this.timeSpent;
    }
    public void setTimeSpent(String timeSpent){
        this.timeSpent = timeSpent;
    }
}
