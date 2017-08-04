package net.amygdalum.tanteemmas.server;

import net.amygdalum.tanteemmas.sources.Daytime;
import net.amygdalum.tanteemmas.sources.DaytimeSource;

public class SimulatedDaytimeSource implements DaytimeSource {

	private double speed;
	private long start;

	public SimulatedDaytimeSource(double speed) {
		this.speed = speed;
		this.start = System.currentTimeMillis();
	}

	@Override
	public Daytime getDaytime() {
		long base = (long) ((System.currentTimeMillis() - start) * 24 / 1000 * speed);
		Daytime[] values = Daytime.values();
		int currentDaytime = (int) (base % values.length);
		return values[currentDaytime];
	}

}
