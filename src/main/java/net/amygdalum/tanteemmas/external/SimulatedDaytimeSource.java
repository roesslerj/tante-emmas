package net.amygdalum.tanteemmas.external;

import static net.amygdalum.tanteemmas.sources.Daytime.AFTERNOON;
import static net.amygdalum.tanteemmas.sources.Daytime.EVENING;
import static net.amygdalum.tanteemmas.sources.Daytime.MORNING;
import static net.amygdalum.tanteemmas.sources.Daytime.NIGHT;
import static net.amygdalum.tanteemmas.sources.Daytime.NOON;

import net.amygdalum.tanteemmas.sources.Daytime;
import net.amygdalum.tanteemmas.sources.DaytimeSource;

public class SimulatedDaytimeSource implements DaytimeSource {

	private TimeProvider time;

	public SimulatedDaytimeSource(TimeProvider time) {
		this.time = time;
	}

	@Override
	public Daytime getDaytime() {
		long base = (long) time.hours();
		int time = (int) (base % 24);
		if (time < 6) {
			return NIGHT;
		} else if (time < 11) {
			return MORNING;
		} else if (time < 14) {
			return NOON;
		} else if (time < 18) {
			return AFTERNOON;
		} else if (time < 22) {
			return EVENING;
		} else {
			return NIGHT;
		}
	}

}
