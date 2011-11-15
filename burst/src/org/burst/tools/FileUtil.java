package org.burst.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {

	//del dir and all files,subdirs
	public static void delDirWithAll(String filepath) throws IOException{
		File f = new File(filepath);
		if(f.exists() && f.isDirectory()){
			if(f.listFiles().length==0){
				f.delete();
			}else{
				File delFile[]=f.listFiles();
				int i =f.listFiles().length;
				for(int j=0;j<i;j++){
					if(delFile[j].isDirectory()){
						delDirWithAll(delFile[j].getAbsolutePath());
					}
					delFile[j].delete();
				}
				f.delete();
			}
		}
	}
	
	public static void writeFile(File file, OutputStream out) throws Exception{
		FileInputStream in = new FileInputStream(file);
		writeFile(in,out);
	}
	
	public static void writeFile(InputStream in, File file) throws Exception{
		FileOutputStream out = new FileOutputStream(file);
		writeFile(in,out);
	}
	
	public static void writeFile(InputStream in, OutputStream out) throws Exception{
		byte bt[] = new byte[1024];
		int c;
		while ((c = in.read(bt)) > 0) {
			out.write(bt, 0, c);
		}
		in.close();
		out.close();
	}
	
	public static byte[] readStreamBytes(InputStream in) throws Exception{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    int b=0;
	    while( (b = in.read())!=-1)
	        baos.write(b);
	    return baos.toByteArray();
	}
}
