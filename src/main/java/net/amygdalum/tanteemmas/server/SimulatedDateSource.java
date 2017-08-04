package net.amygdalum.tanteemmas.server;

import net.amygdalum.tanteemmas.sources.Date;
import net.amygdalum.tanteemmas.sources.DateSource;
import net.amygdalum.tanteemmas.sources.Season;
import net.amygdalum.tanteemmas.sources.Weekday;

public class SimulatedDateSource implements DateSource {

	private long speed;
	private long start;

	public SimulatedDateSource(long speed) {
		this.speed = speed;
		this.start = System.currentTimeMillis();
	}

	@Override
	public Date getDate() {
		long base = (long) ((System.currentTimeMillis() - start) / 1000 * speed);
		int dayOfYear = (int) (base % 365);
		if (dayOfYear < 0) {
			dayOfYear += 365;
		}
		int dayOfWeek = (int) (base % 7);
		if (dayOfWeek < 0) {
			dayOfWeek += 7;
		}
		return new Date(seasonFor(dayOfYear), weekdayFor(dayOfWeek));
	}

	private Season seasonFor(int dayOfYear) {
		if (dayOfYear < 81) {
			return Season.WINTER;
		} else if (dayOfYear < 162) {
			return Season.SPRING;
		} else if (dayOfYear < 243) {
			return Season.SUMMER;
		} else if (dayOfYear < 324) {
			return Season.FALL;
		} else {
			return Season.WINTER;
		}
	}

	private Weekday weekdayFor(int dayOfWeek) {
		return Weekday.values()[dayOfWeek];
	}

}
