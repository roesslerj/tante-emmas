package net.amygdalum.tanteemmas.server;

import static java.util.Collections.emptySet;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Set;

import net.amygdalum.tanteemmas.sources.DateSource;
import net.amygdalum.tanteemmas.sources.Daytime;
import net.amygdalum.tanteemmas.sources.DaytimeSource;
import net.amygdalum.tanteemmas.sources.Precipitation;
import net.amygdalum.tanteemmas.sources.Season;
import net.amygdalum.tanteemmas.sources.Temperature;
import net.amygdalum.tanteemmas.sources.Weather‬Source;
import net.amygdalum.tanteemmas.sources.Weekday;
import net.amygdalum.tanteemmas.sources.Wind;

public class PriceCalculator {

	private DateSource date;
	private DaytimeSource daytime;
	private Weather‬Source weather;

	private Customer customer;

	public static boolean netPrice;
	public static boolean fairPrice;

	public PriceCalculator(DateSource date, DaytimeSource daytime, Weather‬Source weather) {
		this.date = date;
		this.daytime = daytime;
		this.weather = weather;
	}

	public BigDecimal computePrice(Customer customer, Map<String, Object> product) {
		BigDecimal price = (BigDecimal) product.get("basePrice");
		if (price == null) {
			return null;
		}
		do {
			this.customer = customer;
			netPrice = customer.name.equals("Gerd Grosskunde");
			fairPrice = customer.name.equals("Otto Normalverbraucher") || customer.name.equals("Michaela Mustermann");
			if (customer.name.equals("Armer Schlucker")) {
				if (customer.debt) {
					throw new RuntimeException("Please pay your debt");
				}
			} else if (customer.name.equals("Reicher Schnösel")) {
				if (customer.debt) {
					customer.debt = false;
				}
			}
			if (!fairPrice) {
				@SuppressWarnings("unchecked")
				Set<String> categories = (Set<String>) product.getOrDefault("categories", emptySet());
				if (categories.contains("rainwear")
					&& (weather.getWeather().precipitation.equals(Precipitation.DRIZZLE)
						|| weather.getWeather().precipitation.equals(Precipitation.RAIN))
					|| date.getDate().season == Season.FALL) {
					price = price.multiply(BigDecimal.valueOf(102, 2));
					product.put("bad circumstances", true);
				}
				if (weather.getWeather().wind == Wind.STORM
					|| weather.getWeather().temperature == Temperature.FREEZING
					|| weather.getWeather().precipitation == Precipitation.RAIN) {
					product.put("bad circumstances", true);
				}
				if (categories.contains("summerdress")
					&& (date.getDate().season == Season.SUMMER)) {
					price = price.multiply(BigDecimal.valueOf(105, 2));
				}
				if (date.getDate().weekday == Weekday.SATURDAY || date.getDate().weekday == Weekday.SUNDAY) {
					price = price.multiply(BigDecimal.valueOf(103, 2));
				}
				if (daytime.getDaytime() == Daytime.MORNING || daytime.getDaytime() == Daytime.EVENING) {
					price = price.multiply(BigDecimal.valueOf(101, 2));
				}
				if (customer.debt) {
					price = price.multiply(BigDecimal.valueOf(101, 2));
				}
			}
			if (((Boolean) product.getOrDefault("bad circumstances", false)).booleanValue()) {
				price = price.multiply(BigDecimal.valueOf(102, 2));
			}
			if (netPrice) {
				price = price.divide(BigDecimal.valueOf(119, 2));
			}
		} while (customer != this.customer);
		return price.setScale(2, RoundingMode.HALF_UP);
	}

}
