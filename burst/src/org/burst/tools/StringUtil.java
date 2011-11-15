package org.burst.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
	public static final String NAME_BAD_CHAR = "\"'!@#$%^&*(){}|  ¡¡";
	public static final String COMMON_BAD_CHAR = "";
	
	public static boolean hasChars(String str){
		if (str == null) {
			return false;
		}
		
		if (str.trim().length() == 0) {
			return false;
		}
		
		return true;
	}
	
	public static boolean hasBlank(String str){
		if (str == null) {
			return false;
		}
		
		if (str.trim().indexOf(" ") < 0) {
			return false;
		}else{
			return true;
		}
	}
		
	public static String addYin(String str){
		if(hasBlank(str)){
			return "\"" + str + "\"";
		}else{
			return str;
		}
	}
	
	public static boolean matchStr(String str, String regex) {
        if (regex == null || regex.length() == 0) {
            return true;
        }
        String s = regex;
        s = s.replace('?', '.');

        s = s.replace('*', '#');
        s = s.replaceAll("#", ".*");

        Pattern p = Pattern.compile(s);

        Matcher fMatcher = null;
        fMatcher = p.matcher(str);
        return fMatcher.matches();
    }
	
	public static boolean hasNameBadChar(String str){
		if(str == null || str.length() == 0){
			return false;
		}
		
		String chr = null;
		for(int i=0;i<NAME_BAD_CHAR.length();i++){
			chr = NAME_BAD_CHAR.substring(i, i+1);
			if(str.indexOf(chr) >= 0){
				return true;
			}
		}
		
		return false;
	}
}
