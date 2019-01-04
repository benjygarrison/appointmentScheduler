
package Model;

/**
 *
 * @author Ben Garrison
 */
public class Report {
    
    private String start;
    private String end;
    private String userName;
    private String userId;
    private String type;
    private String month;
    private String year;

    public Report(String month, String year, String type) {        
        this.month = month;
        this.year = year;
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Report(String start, String end, String userName, String userId) {
        this.start = start;
        this.end = end;
        this.userName = userName;
        this.userId = userId;
    }
    
public Report(String userName){
        this.userName = userName;
}    

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    
}
