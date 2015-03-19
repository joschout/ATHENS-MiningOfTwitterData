package utils;

public class Timer {
	
	private long start;
	private final String action;
	private String startString;
	
	public Timer(String action){
		this.start = 0;
		this.action = action;
	}
	
	public void start(String startString){
		this.start = System.currentTimeMillis();
		this.startString = startString;
	}
	
	public void start(){
		start("");
	}
	
	public void stop(String beforeColon, String afterColon){
		if(this.start == 0)
			throw new UnsupportedOperationException("Starting time was not defined!");
		long stop = System.currentTimeMillis();
		System.out.println(this.startString + "Time elapsed while " + this.action + beforeColon + ": \n" + formatElapsedMilliseconds(stop - this.start) + afterColon);
	}
	
	public void stop(){
		stop("", "");
	}
	
	public void stop(String beforeColon){
		stop(beforeColon, "");
	}
	
	public String formatElapsedMilliseconds(long milliseconds) {
		String result = "";
		if(milliseconds<0){
			result="-";
			milliseconds=Math.abs(milliseconds);
		}
		long hours = milliseconds/3600000;
		long minutes = (milliseconds%3600000)/60000;
		long seconds = (milliseconds%60000)/1000;
		result = result + ((hours == 0) ? "" : (hours + " hours ")) + ((minutes == 0) ? "" : (minutes + " minutes ")) + seconds + " seconds";
		return result;
	}
}