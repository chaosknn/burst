package org.burst.model;

public class CountBox {

	private int nowCount = 0;
	private int maxCount = 0;
	private int minCount = 0;
	
	public void countUp(){
		nowCount++;
	}
	
	public void countDown(){
		nowCount--;
	}
	
	public boolean reachMax(){
		return nowCount >= maxCount;
	}
	
	public boolean reachMin(){
		return nowCount <= minCount;
	}

	public int getNowCount() {
		return nowCount;
	}

	public void setNowCount(int nowCount) {
		this.nowCount = nowCount;
	}

	public int getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	public int getMinCount() {
		return minCount;
	}

	public void setMinCount(int minCount) {
		this.minCount = minCount;
	}
}
