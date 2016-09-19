package pl.gda.pg.eti.autyzm.backupper.core.Models;


public class Activity {

    private String id;
    private String name;

    public Activity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String value) {
        this.id = value;
    }
}
