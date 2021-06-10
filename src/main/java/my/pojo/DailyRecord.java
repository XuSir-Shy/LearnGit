package my.pojo;

public class DailyRecord {
    private int Id;
    private String name;
    private String reTime;
    private String Remarks;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return reTime;
    }

    public void setTime(String retime) {
        reTime = retime;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    @Override
    public String toString() {
        return "DailyRecord{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", reTime='" + reTime + '\'' +
                ", Remarks='" + Remarks + '\'' +
                '}';
    }
}
