package net.amygdalum.tanteemmas.external;

import static net.amygdalum.tanteemmas.sources.Precipitation.DRIZZLE;
import static net.amygdalum.tanteemmas.sources.Precipitation.DRY;
import static net.amygdalum.tanteemmas.sources.Precipitation.MIST;
import static net.amygdalum.tanteemmas.sources.Precipitation.NORMAL;
import static net.amygdalum.tanteemmas.sources.Precipitation.RAIN;
import static net.amygdalum.tanteemmas.sources.Precipitation.SNOW;
import static net.amygdalum.tanteemmas.sources.Temperature.COLD;
import static net.amygdalum.tanteemmas.sources.Temperature.COOL;
import static net.amygdalum.tanteemmas.sources.Temperature.FREEZING;
import static net.amygdalum.tanteemmas.sources.Temperature.HOT;
import static net.amygdalum.tanteemmas.sources.Temperature.MODERATE;
import static net.amygdalum.tanteemmas.sources.Temperature.WARM;

import net.amygdalum.tanteemmas.sources.DateSource;
import net.amygdalum.tanteemmas.sources.Precipitation;
import net.amygdalum.tanteemmas.sources.Season;
import net.amygdalum.tanteemmas.sources.Temperature;
import net.amygdalum.tanteemmas.sources.Weather;
import net.amygdalum.tanteemmas.sources.Weather‬Source;
import net.amygdalum.tanteemmas.sources.Wind;

public class SimulatedWeatherSource implements Weather‬Source {

	private TimeProvider time;
	private DateSource date;
	private Waves precipitation;
	private Waves temperature;
	private Waves wind;

	public SimulatedWeatherSource(TimeProvider time, DateSource date) {
		this.time = time;
		this.date = date;
		this.precipitation = new Waves(1.0, 5, 15.0);
		this.temperature = new Waves(1.5, 3.5, 10.0);
		this.wind = new Waves(1.0, 4.0, 10.0);
	}

	@Override
	public Weather getWeather() {
		double base = time.hours();

		Season season = date.getDate().getSeason();
		
		
		return new Weather(
			precipitation.value(base, precipitationForSeason(season)),
			temperature.value(base, temperatureForSeason(season)),
			wind.value(base, windForSeason(season)));
	}
	
	public static Precipitation[] precipitationForSeason(Season season) {
		switch(season) {
		case SPRING:
			return new Precipitation[] {DRY, DRY, NORMAL, NORMAL, NORMAL, MIST, MIST, DRIZZLE, DRIZZLE, RAIN};
		case SUMMER:
			return new Precipitation[] {DRY, DRY, DRY, NORMAL, NORMAL, NORMAL, NORMAL, MIST, DRIZZLE, RAIN};
		case FALL:
			return new Precipitation[] {DRY, DRY, NORMAL, NORMAL, NORMAL, MIST, DRIZZLE, DRIZZLE, RAIN, RAIN};
		case WINTER:
		default:
			return new Precipitation[] {DRY, DRY, NORMAL, NORMAL, NORMAL, MIST, DRIZZLE, RAIN, SNOW, SNOW};
		}
	}

	public static Temperature[] temperatureForSeason(Season season) {
		switch(season) {
		case SPRING:
			return new Temperature[] {WARM, MODERATE, COOL, COLD};
		case SUMMER:
			return new Temperature[] {HOT, WARM, MODERATE, COOL};
		case FALL:
			return new Temperature[] {WARM, MODERATE, COOL, COLD};
		case WINTER:
		default:
			return new Temperature[] {MODERATE, COOL, COLD, FREEZING};
		}
	}

	public static Wind[] windForSeason(Season season) {
		return Wind.values();
	}

	private static class Waves {

		private double f1;
		private double f2;
		private double f3;

		public Waves(double f1, double f2, double f3) {
			this.f1 = f1;
			this.f2 = f2;
			this.f3 = f3;
		}

		public <T> T value(double base, T[] values) {
			double value = Math.sin(base / f1 + Math.PI) * f1 +
				Math.sin(base / f2 + 2 * Math.PI) * f2 +
				Math.sin(base / f3 + 3 * Math.PI) * f3;

			double max = f1 + f2 + f3;
			double min = -max;
			
			double relativeValue = (value - min) / (max - min);
			int index = (int) (relativeValue * (double) values.length);
			
			return values[index];
		}

	}
}
