//This class is created by vba tools
//creator:xuguobiao
//mail for help: thegod4204@163.com

package org.burst.db.tbl;

import java.util.Date;

import org.burst.db.def.TblDef;
import org.burst.db.tbl.base.MyBlob;
import org.burst.db.tbl.base.TblBase;

public class TblStudent extends TblBase{


    //Constructor
    public TblStudent (){
        super(TblDef.TblStudent_NAME, TblDef.TblStudent_FIELD, TblDef.TblStudent_INDEX, TblDef.TblStudent_PARTITION);
    }

    //newTbl
    public TblStudent newTbl(){
        return new TblStudent();
    }

    //id , data id
    public Integer getId() throws Exception{
        return getInt("id");
    }

    public void setId(Integer id) throws Exception{
        setInt("id", id);
    }

    //username , name to login
    public String getUsername() throws Exception{
        return getString("username");
    }

    public void setUsername(String username) throws Exception{
        setString("username", username);
    }

    //fullname , fullname
    public String getFullname() throws Exception{
        return getString("fullname");
    }

    public void setFullname(String fullname) throws Exception{
        setString("fullname", fullname);
    }

    //score , a double data
    public Double getScore() throws Exception{
        return getDouble("score");
    }

    public void setScore(Double score) throws Exception{
        setDouble("score", score);
    }

    //cdate , create date
    public Date getCdate() throws Exception{
        return getDate("cdate");
    }

    public void setCdate(Date cdate) throws Exception{
        setDate("cdate", cdate);
    }

    //udate , update date
    public Date getUdate() throws Exception{
        return getDate("udate");
    }

    public void setUdate(Date udate) throws Exception{
        setDate("udate", udate);
    }

    //active , active or not
    public Boolean isActive() throws Exception{
        return getBool("active");
    }

    public void setActive(Boolean active) throws Exception{
        setBool("active", active);
    }

    //image , a pic stored in db
    public MyBlob getImage() throws Exception{
        return getBlob("image");
    }

    public void setImage(MyBlob image) throws Exception{
        setBlob("image", image);
    }

}
