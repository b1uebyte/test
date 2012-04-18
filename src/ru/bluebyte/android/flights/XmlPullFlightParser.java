package ru.bluebyte.android.flights;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class XmlPullFlightParser {
	
	InputStream inputStream;
	
	private static final String TRIP = "trip";
	private static final String DURATION = "duration";
	private static final String TAKEOFF = "takeoff";
	private static final String LANDING = "landing";
	private static final String FLIGHT = "flight";
	private static final String PRICE = "price";
	
	private XmlPullParser parser = null;
	
	public XmlPullFlightParser(String urlString) throws IOException {
		URL url = new URL(urlString);
		inputStream = url.openConnection().getInputStream();
	}

	public ArrayList<FlightInfo> parse() {
		ArrayList<FlightInfo> flights = null;
		parser = Xml.newPullParser();
		
		try {
			parser.setInput(inputStream, null);
			
			int eventType = parser.getEventType();
			FlightInfo currentFlight = null;
			while (eventType != XmlPullParser.END_DOCUMENT) {
				String name = null;
				switch (eventType) {
				
				case XmlPullParser.START_DOCUMENT:
					flights = new ArrayList<FlightInfo>();
					break;
					
				case XmlPullParser.START_TAG:
					name = parser.getName();
					if (name.equalsIgnoreCase(TRIP)) {
						currentFlight = new FlightInfo();
						
						parseTripDuration(currentFlight);
						
					} else if (currentFlight != null) {
						if (name.equalsIgnoreCase(TAKEOFF)) {
							parseTransmissionInfo(currentFlight, TAKEOFF);
							
						} else if (name.equalsIgnoreCase(LANDING)) {
							parseTransmissionInfo(currentFlight, LANDING);
							
						} else if (name.equalsIgnoreCase(FLIGHT)) {
							parseFlightInfo(currentFlight);
							
						} else if (name.equalsIgnoreCase(PRICE)) {
							parseTripPrice(currentFlight);							
						}
					}
					break;
					
				case XmlPullParser.END_TAG:
					name = parser.getName();
					if (name.equalsIgnoreCase(TRIP) && currentFlight != null) {
						flights.add(currentFlight);
					}
					break;
				}
				eventType = parser.next();
			}
        } catch (Exception e) {
            flights = null;
        }
		
        return flights;
	}

	private void parseTripPrice(FlightInfo currentFlight) throws NumberFormatException, XmlPullParserException, IOException {
		Double price = Double.parseDouble(parser.nextText());
		currentFlight.setFlightPrice(price);
	}

	private void parseFlightInfo(FlightInfo currentFlight) {
		String carrier = parser.getAttributeValue(null, "carrier");
		Integer number = Integer.parseInt(parser.getAttributeValue(null, "number"));
		Integer eq = Integer.parseInt(parser.getAttributeValue(null, "eq"));
		
		currentFlight.setFlightCarrier(carrier);
		currentFlight.setFlightNumber(number);
		currentFlight.setFlightEq(eq);
	}

	private void parseTripDuration(FlightInfo currentFlight) {
		String time = parser.getAttributeValue(null, DURATION);
		String[] timeStrings = time.split(":");
		int hours = Integer.parseInt(timeStrings[0]);
		int minutes = Integer.parseInt(timeStrings[1]);
		
		currentFlight.setTripDuration((hours * 60) + minutes);
	}

	private void parseTransmissionInfo(FlightInfo currentFlight, String transmissionType) {
		String date = parser.getAttributeValue(null, "date");
		String time = parser.getAttributeValue(null, "time");
		String city = parser.getAttributeValue(null, "city");
		
		if (transmissionType == TAKEOFF) {
			currentFlight.setTakeoffDate(date);
			currentFlight.setTakeoffTime(time);
			currentFlight.setTakeoffCity(city);			
		} else if (transmissionType == LANDING) {
			currentFlight.setLandingDate(date);
			currentFlight.setLandingTime(time);
			currentFlight.setLandingCity(city);
		}
		
	}

}
