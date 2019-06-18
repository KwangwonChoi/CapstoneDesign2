package healthcare.ckwpjt.com.healthcare.Objects;

import java.io.Serializable;

public class ClientLog implements Serializable{
    String date;
    String state;
    String details;

    public ClientLog(){}

    public ClientLog(String date, String details, String state){
        this.date = date;
        this.state = state;
        this.details = details;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
