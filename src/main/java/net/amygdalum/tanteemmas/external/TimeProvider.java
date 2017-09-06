package net.amygdalum.tanteemmas.external;

public class TimeProvider {

	private long start;
	private long speed;

	public TimeProvider() {
		this.start = System.currentTimeMillis();
		this.speed = 1;
	}
	
	public void setSpeed(long speed) {
		start -= System.currentTimeMillis() * (speed - this.speed);
		this.speed = speed;
	}

	public long millis() {
		return (System.currentTimeMillis() - start) * speed;
	}
		
	public long seconds() {
		return millis() / 1000;
	}
		
	public long minutes() {
		return seconds() / 60;
	}
		
	public long hours() {
		return minutes() / 60;
	}
	
	public long days() {
		return hours() / 24;
	}
		
}
