package net.amygdalum.tanteemmas.external;

public class TimeProvider {

	private long hours;
	private long speed;

	public TimeProvider() {
		this.hours = 0;
		this.speed = 1;
	}

	public void setSpeed(long speed) {
		if (speed <= 0) {
			speed = 1;
			return;
		}
		this.speed = speed;
	}

	public long getSpeed() {
		return speed;
	}

	public long hours() {
		increase();
		return hours;
	}

	private void increase() {
		hours += speed;
	}

	public long days() {
		return hours() / 24;
	}

}
