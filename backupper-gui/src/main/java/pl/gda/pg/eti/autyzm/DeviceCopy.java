package pl.gda.pg.eti.autyzm;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDate;

/**
 * Created by Gosia on 2016-06-27.
 */
public class DeviceCopy {

    public final StringProperty name;
    public final ObjectProperty<LocalDate> createDate;

    public DeviceCopy(){
        this(null, null);
    }

    public DeviceCopy(String name, LocalDate createDate) {
        this.name =  new SimpleStringProperty(name);
        this.createDate =  new SimpleObjectProperty<LocalDate>(createDate);
    }

    public StringProperty getNameProperty(){
        return this.name;
    }

    public String getName(){
        return this.name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public ObjectProperty getCreateDateProperty(){
        return this.createDate;
    }

    public LocalDate getCreateDate(){
        return this.createDate.get();
    }

    public void setCreateDate(LocalDate createDate) {
        this.createDate.set(createDate);
    }


}
