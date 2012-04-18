package ru.bluebyte.android.flights;

import java.text.NumberFormat;

public class FlightInfo {

	private String takeoffCity = null;
	private String takeoffDate = null;
	private String takeoffTime = null;

	private String landingCity = null;
	private String landingDate = null;
	private String landingTime = null;

	private String flightCarrier = null;
	private Integer flightNumber = null;
	private Integer flightEq = null;
	private Double flightPrice = null;

	private Integer tripDuration;

	public FlightInfo(String takeoffCity, String takeoffDate,
			String takeoffTime, String landingCity, String landingDate,
			String landingTime, String flightCarrier, int flightNumber,
			int flightEq, double flightPrice, int tripDuration) {
		
		this.setTakeoffCity(takeoffCity);
		this.setTakeoffDate(takeoffDate);
		this.setTakeoffTime(takeoffTime);

		this.setLandingCity(landingCity);
		this.setLandingDate(landingDate);
		this.setLandingTime(landingTime);

		this.setFlightCarrier(flightCarrier);
		this.setFlightNumber(flightNumber);
		this.setFlightEq(flightEq);
		this.setFlightPrice(flightPrice);

		this.setTripDuration(tripDuration);
	}

	public FlightInfo() {
	}

	public Integer getTripDuration() {
		return tripDuration;
	}

	public void setTripDuration(Integer tripDuration) {
		this.tripDuration = tripDuration;
	}
	
	public String getDurationString() {
		int hours = tripDuration / 60;
		int minutes = tripDuration % 60;

		NumberFormat formatter = NumberFormat.getNumberInstance();
		formatter.setMinimumIntegerDigits(2);
		
		return formatter.format(hours) + ":" + formatter.format(minutes);
	}

	public String getTakeoffDate() {
		return takeoffDate;
	}

	public void setTakeoffDate(String takeoffDate) {
		this.takeoffDate = takeoffDate;
	}

	public String getLandingDate() {
		return landingDate;
	}

	public void setLandingDate(String landingDate) {
		this.landingDate = landingDate;
	}

	public String getFlightCarrier() {
		return flightCarrier;
	}

	public void setFlightCarrier(String flightCarrier) {
		this.flightCarrier = flightCarrier;
	}

	public Integer getFlightNumber() {
		return flightNumber;
	}

	public void setFlightNumber(Integer flightNumber) {
		this.flightNumber = flightNumber;
	}

	public Integer getFlightEq() {
		return flightEq;
	}

	public void setFlightEq(Integer flightEq) {
		this.flightEq = flightEq;
	}

	public Double getFlightPrice() {
		return flightPrice;
	}

	public void setFlightPrice(Double flightPrice) {
		this.flightPrice = flightPrice;
	}

	public String getTakeoffCity() {
		return takeoffCity;
	}

	public void setTakeoffCity(String takeoffCity) {
		this.takeoffCity = takeoffCity;
	}

	public String getLandingCity() {
		return landingCity;
	}

	public void setLandingCity(String landingCity) {
		this.landingCity = landingCity;
	}

	public void setTakeoffTime(String time) {
		this.takeoffTime = time;
	}

	public String getTakeoffTime() {
		return takeoffTime;
	}

	public void setLandingTime(String time) {
		this.landingTime = time;
	}

	public String getLandingTime() {
		return landingTime;
	}
}
