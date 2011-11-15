//This class is created by vba tools
//creator:xuguobiao
//mail for help: thegod4204@163.com

package org.burst.db.def;

public class TblDef {

    //TblStudent
    public static final String TblStudent_NAME = "TblStudent";
    public static final String TblStudent_FIELD = "id(1),username(4:20),fullname(4:40),score(2),cdate(3),udate(3),active(5),image(6)";
    public static final String TblStudent_INDEX = "";
    public static final String TblStudent_PARTITION = "id";

    //TblStudentLessons
    public static final String TblStudentLessons_NAME = "TblStudentLessons";
    public static final String TblStudentLessons_FIELD = "id(1),studentId(1),lessonId(1),startDate(3),endDate(3),testDate(3),description(4:400),score(2),pass(5)";
    public static final String TblStudentLessons_INDEX = "studentId,lessonId";
    public static final String TblStudentLessons_PARTITION = "id";

    //TblLesson
    public static final String TblLesson_NAME = "TblLesson";
    public static final String TblLesson_FIELD = "id(1),name(4:100),description(4:400)";
    public static final String TblLesson_INDEX = "";
    public static final String TblLesson_PARTITION = "";

}
