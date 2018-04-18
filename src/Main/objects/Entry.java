package Main.objects;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class Entry implements Serializable{
    private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
    private StringProperty problem = new SimpleStringProperty();
    private StringProperty fix = new SimpleStringProperty();
    private String name;

    private Date date;

    public Entry(){}

    public Entry(String name){
        this.name = name;
        setFix("");
        setProblem("");
        date  = new Date();
    }

    public String getProblem() {return problem.get();}

    public StringProperty problemProperty() {return problem;}

    public void setProblem(String problem) {this.problem.set(problem);}

    public String getFix() {return fix.get();}

    public StringProperty fixProperty() {return fix;}

    public void setFix(String fix) {this.fix.set(fix);}

    public String getDateString(){return dateFormat.format(date);}

    public Date getDate(){return date;}

    public void setDate(Date newDate) {date = newDate;}

    public void setDate(LocalDate newDate) {date = Date.from(newDate.atStartOfDay(ZoneId.systemDefault()).toInstant());}

    public String getName() {return name;}

    public void setName(String newName) {name = newName;}

    public String toString(){return getName() + "\n" + getDateString();}

}
