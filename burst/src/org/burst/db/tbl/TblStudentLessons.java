//This class is created by vba tools
//creator:xuguobiao
//mail for help: thegod4204@163.com

package org.burst.db.tbl;

import java.util.Date;

import org.burst.db.def.TblDef;
import org.burst.db.tbl.base.MyBlob;
import org.burst.db.tbl.base.TblBase;

public class TblStudentLessons extends TblBase{


    //Constructor
    public TblStudentLessons (){
        super(TblDef.TblStudentLessons_NAME, TblDef.TblStudentLessons_FIELD, TblDef.TblStudentLessons_INDEX, TblDef.TblStudentLessons_PARTITION);
    }

    //newTbl
    public TblStudentLessons newTbl(){
        return new TblStudentLessons();
    }

    //id , data id
    public Integer getId() throws Exception{
        return getInt("id");
    }

    public void setId(Integer id) throws Exception{
        setInt("id", id);
    }

    //studentId , student id
    public Integer getStudentId() throws Exception{
        return getInt("studentId");
    }

    public void setStudentId(Integer studentId) throws Exception{
        setInt("studentId", studentId);
    }

    //lessonId , lesson id
    public Integer getLessonId() throws Exception{
        return getInt("lessonId");
    }

    public void setLessonId(Integer lessonId) throws Exception{
        setInt("lessonId", lessonId);
    }

    //startDate , start date
    public Date getStartDate() throws Exception{
        return getDate("startDate");
    }

    public void setStartDate(Date startDate) throws Exception{
        setDate("startDate", startDate);
    }

    //endDate , end date
    public Date getEndDate() throws Exception{
        return getDate("endDate");
    }

    public void setEndDate(Date endDate) throws Exception{
        setDate("endDate", endDate);
    }

    //testDate , test date
    public Date getTestDate() throws Exception{
        return getDate("testDate");
    }

    public void setTestDate(Date testDate) throws Exception{
        setDate("testDate", testDate);
    }

    //description , Description
    public String getDescription() throws Exception{
        return getString("description");
    }

    public void setDescription(String description) throws Exception{
        setString("description", description);
    }

    //score , score
    public Double getScore() throws Exception{
        return getDouble("score");
    }

    public void setScore(Double score) throws Exception{
        setDouble("score", score);
    }

    //pass , pass or not
    public Boolean isPass() throws Exception{
        return getBool("pass");
    }

    public void setPass(Boolean pass) throws Exception{
        setBool("pass", pass);
    }

}
