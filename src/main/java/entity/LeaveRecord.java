package entity;

public class LeaveRecord {
    private int id;
    private String name;
    private String note;
    private long submitedAt;
    private int status;

    public LeaveRecord() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getSubmitedAt() {
        return submitedAt;
    }

    public void setSubmitedAt(long submitedAt) {
        this.submitedAt = submitedAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void saveRecord(){

    }
}
