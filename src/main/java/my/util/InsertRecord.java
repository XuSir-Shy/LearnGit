package my.util;

import my.dao.RecordDao;
import my.pojo.DailyRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class InsertRecord {
    private String time;

    public InsertRecord() {
        Date date =new Date();
        this.time = date.toString();
    }

    public String getTime() {
        return time;
    }

    @Autowired(required = false)
    RecordDao recordDao;
    public void insert(String name,String remarks){
        DailyRecord dailyRecord = new DailyRecord();
        dailyRecord.setRemarks(remarks);
        dailyRecord.setTime(this.time);
        dailyRecord.setName(name);
        recordDao.insertRecord(dailyRecord);
    }
}
