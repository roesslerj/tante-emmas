package net.amygdalum.tanteemmas.sources;

public class Weather {

	public Precipitation precipitation;
	public Temperature temperature;
	public Wind wind;
	
	public Weather(Precipitation precipitation, Temperature temperature, Wind wind) {
		this.precipitation = precipitation;
		this.temperature = temperature;
		this.wind = wind;
	}

	@Override
	public String toString() {
		return temperature + "/" + precipitation + "/" + wind;
	}
		
	
}
