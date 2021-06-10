package my.dao;

import my.pojo.DailyRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RecordDao {
    @Insert("insert into DailyRecord (name,reTime,Remarks) values (#{name},#{reTime},#{Remarks})")
    boolean insertRecord(DailyRecord dailyRecord);
}
