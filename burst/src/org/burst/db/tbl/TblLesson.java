//This class is created by vba tools
//creator:xuguobiao
//mail for help: thegod4204@163.com

package org.burst.db.tbl;

import java.util.Date;

import org.burst.db.def.TblDef;
import org.burst.db.tbl.base.MyBlob;
import org.burst.db.tbl.base.TblBase;

public class TblLesson extends TblBase{


    //Constructor
    public TblLesson (){
        super(TblDef.TblLesson_NAME, TblDef.TblLesson_FIELD, TblDef.TblLesson_INDEX, TblDef.TblLesson_PARTITION);
    }

    //newTbl
    public TblLesson newTbl(){
        return new TblLesson();
    }

    //id , data id
    public Integer getId() throws Exception{
        return getInt("id");
    }

    public void setId(Integer id) throws Exception{
        setInt("id", id);
    }

    //name , lesson name
    public String getName() throws Exception{
        return getString("name");
    }

    public void setName(String name) throws Exception{
        setString("name", name);
    }

    //description , Description
    public String getDescription() throws Exception{
        return getString("description");
    }

    public void setDescription(String description) throws Exception{
        setString("description", description);
    }

}
