package entity;

public class Schedule {
    private int id;
    private int eId;
    private String workSchedule;
    private int totalLeave;

    public Schedule(int eId, String workSchedule, int totalLeave) {
        this.eId = eId;
        this.workSchedule = workSchedule;
        this.totalLeave = totalLeave;
    }

    public Schedule() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int geteId() {
        return eId;
    }

    public void seteId(int eId) {
        this.eId = eId;
    }

    public String getWorkSchedule() {
        return workSchedule;
    }

    public void setWorkSchedule(String workSchedule) {
        this.workSchedule = workSchedule;
    }

    public int getTotalLeave() {
        return totalLeave;
    }

    public void setTotalLeave(int totalLeave) {
        this.totalLeave = totalLeave;
    }
}
