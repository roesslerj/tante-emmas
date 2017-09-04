package net.amygdalum.tanteemmas.sources;

public class Date {
	private Season season;
	private Weekday weekday;
	
	public Date(Season season, Weekday weekday) {
		this.season = season;
		this.weekday = weekday;
	}
	
	public Season getSeason() {
		return season;
	}
	
	public Weekday getWeekday() {
		return weekday;
	}
	
}
