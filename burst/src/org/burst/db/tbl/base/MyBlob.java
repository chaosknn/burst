package org.burst.db.tbl.base;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class MyBlob {
	public static String UTF8 = "UTF-8";
	
	public static int TYPE_STRING = 0;
	public static int TYPE_BYTES = 1;
	public static int TYPE_STREAM = 2;
	
	private int type = 0;
	private String string = null;
	private byte[] bytes = null;
	private InputStream in = null;
	
	public void setValue(String value){
		type = TYPE_STRING;
		string = value;
	}
	
	public void setValue(byte[] value){
		type = TYPE_BYTES;
		bytes = value;
	}
	
	public void setValue(InputStream value){
		type = TYPE_STREAM;
		in = value;
	}
	
	public InputStream getInputStream() throws Exception{
		if(type == TYPE_STRING){
			if(string == null){
				return new ByteArrayInputStream(new byte[0]);
			}else{
				return new ByteArrayInputStream(string.getBytes(UTF8));
			}
		}else if(type == TYPE_BYTES){
			if(bytes == null){
				return new ByteArrayInputStream(new byte[0]);
			}else{
				return new ByteArrayInputStream(bytes);
			}
		}else if(type == TYPE_STREAM){
			return in;
		}else {
			throw new Exception("invalid MyBlob data type");
		}
	}
	
	public String readString() throws Exception{
		if(type == TYPE_STRING){
			return string;
		}else if(type == TYPE_BYTES){
			return new String(bytes, UTF8);
		}else {
			throw new Exception("MyBlob data type is not string");
		}
	}
	
	public byte[] readBytes() throws Exception{
		if(type == TYPE_BYTES){
			return bytes;
		}else {
			throw new Exception("MyBlob data type is not bytes");
		}
	}
	
}
