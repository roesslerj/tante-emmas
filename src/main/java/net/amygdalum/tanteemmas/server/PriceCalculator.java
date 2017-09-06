package net.amygdalum.tanteemmas.server;

import static java.util.Collections.emptySet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;

import net.amygdalum.tanteemmas.sources.Date;
import net.amygdalum.tanteemmas.sources.DateSource;
import net.amygdalum.tanteemmas.sources.Daytime;
import net.amygdalum.tanteemmas.sources.DaytimeSource;
import net.amygdalum.tanteemmas.sources.Precipitation;
import net.amygdalum.tanteemmas.sources.Season;
import net.amygdalum.tanteemmas.sources.Temperature;
import net.amygdalum.tanteemmas.sources.Weather;
import net.amygdalum.tanteemmas.sources.Weather‬Source;
import net.amygdalum.tanteemmas.sources.Weekday;
import net.amygdalum.tanteemmas.sources.Wind;

public class PriceCalculator {

	public static Customer customer;

	private DateSource dateSource;
	private DaytimeSource daytimeSource;
	private Weather‬Source weatherSource;

	public PriceCalculator(DateSource date, DaytimeSource daytime, Weather‬Source weather) {
		this.dateSource = date;
		this.daytimeSource = daytime;
		this.weatherSource = weather;
	}

	public BigDecimal computePrice(Map<String, Object> product) {
		BigDecimal price = (BigDecimal) product.get("basePrice");
		if (price == null) {
			return null;
		}
		boolean netPrice = customer.name.equals("Gerd Grosskunde");
		boolean fairPrice = customer.name.equals("Otto Normalverbraucher") || customer.name.equals("Michaela Mustermann");
		if (customer.name.equals("Armer Schlucker")) {
			if (customer.debt.compareTo(BigDecimal.ZERO) > 0) {
				customer.notCreditable = true;
			}
		} else if (customer.status != null && customer.status.contains("rich")) {
			if (customer.debt.compareTo(BigDecimal.valueOf(10_000)) < 0) {
				customer.notCreditable = false;
			} else {
				customer.notCreditable = true;
			}
		}
		if (customer.notCreditable) {
			throw new RuntimeException("Please pay your debt");
		}
		if (!fairPrice) {
			Daytime daytime = daytimeSource.getDaytime();
			Date date = dateSource.getDate();
			Weather weather = weatherSource.getWeather();
			
			@SuppressWarnings("unchecked")
			Set<String> categories = (Set<String>) product.getOrDefault("categories", emptySet());
			if (categories.contains("rainwear")
				&& (weather.getPrecipitation().equals(Precipitation.DRIZZLE)
					|| weather.getPrecipitation().equals(Precipitation.RAIN))
				|| date.getSeason() == Season.FALL) {
				price = price.multiply(BigDecimal.valueOf(102, 2));
				product.put("bad circumstances", true);
			}
			if (weather.getWind() == Wind.STORM
				|| weather.getTemperature() == Temperature.FREEZING
				|| weather.getPrecipitation() == Precipitation.RAIN) {
				product.put("bad circumstances", true);
			}
			if (categories.contains("summercollection")
				&& (date.getSeason() == Season.SUMMER)) {
				price = price.multiply(BigDecimal.valueOf(105, 2));
			}
			if (date.getWeekday() == Weekday.SATURDAY || date.getWeekday() == Weekday.SUNDAY) {
				price = price.multiply(BigDecimal.valueOf(103, 2));
			}
			if (daytime == Daytime.MORNING || daytime == Daytime.EVENING) {
				price = price.multiply(BigDecimal.valueOf(101, 2));
			}
			if (customer.debt.compareTo(BigDecimal.ZERO) > 0) {
				if (categories.contains("spirits")) {
					price = price.multiply(BigDecimal.valueOf(99, 2));
				} else {
					price = price.multiply(BigDecimal.valueOf(101, 2));
				}
			}
		}
		if (((Boolean) product.getOrDefault("bad circumstances", false)).booleanValue()) {
			price = price.multiply(BigDecimal.valueOf(102, 2));
		}
		if (netPrice) {
			price = price.divide(BigDecimal.valueOf(119, 2), RoundingMode.HALF_UP);
		}
		return price.setScale(2, RoundingMode.HALF_UP);
	}

}
