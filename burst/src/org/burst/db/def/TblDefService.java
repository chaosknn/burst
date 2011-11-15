//This class is created by vba tools
//creator:xuguobiao
//mail for help: thegod4204@163.com

package org.burst.db.def;

import java.util.ArrayList;

import org.burst.db.conn.DbBase;
import org.burst.db.tbl.*;
import org.burst.db.tbl.base.TblBase;
import org.burst.db.tbl.base.TblBaseService;

public class TblDefService {

    private static ArrayList<TblBase> getAllTblList() throws Exception {
        ArrayList<TblBase> list = new ArrayList<TblBase>();
        TblBase tbl;

        //TblStudent
        tbl = new TblStudent();
        list.add(tbl);

        //TblStudentLessons
        tbl = new TblStudentLessons();
        list.add(tbl);

        //TblLesson
        tbl = new TblLesson();
        list.add(tbl);

        return list;

    }

    public static void createAllTbl(DbBase db) throws Exception {
        ArrayList<TblBase> list = getAllTblList();
        TblBase tbl = null;
        for(int i=0;i<list.size();i++){
            tbl = (TblBase)list.get(i);
            TblBaseService.createTable(db, tbl);
            TblBaseService.createSequence(db, tbl);
            TblBaseService.createIndex(db, tbl);
        }
    }

    public static void dropAllTbl(DbBase db) throws Exception {
        ArrayList<TblBase> list = getAllTblList();
        TblBase tbl = null;
        for(int i=0;i<list.size();i++){
            tbl = (TblBase)list.get(i);
            TblBaseService.dropTable(db, tbl);
            TblBaseService.dropSequence(db, tbl);
        }
    }

}
