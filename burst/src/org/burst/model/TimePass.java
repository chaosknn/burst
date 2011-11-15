package org.burst.model;

import java.util.Date;

public class TimePass {
	
	private long dateStart = 0;
	private long dateEnd = 0;
	
	public TimePass(boolean autoStart){
		if(autoStart){
			start();
		}
	}
	
	public void start(){
		dateStart = (new Date()).getTime();
	}
	
	public void end(){
		dateEnd = (new Date()).getTime();
	}

	public long getTimePass(boolean autoEnd) throws Exception{
		if(autoEnd){
			end();
		}
		
		if(dateStart == 0){
			throw new Exception("TimePass not started!");
		}
		if(dateEnd == 0){
			throw new Exception("TimePass not ended!");
		}
		long pass = dateEnd - dateStart;
		return pass;
	}
	
	
}