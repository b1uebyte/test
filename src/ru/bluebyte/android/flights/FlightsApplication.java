package ru.bluebyte.android.flights;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import android.app.Application;

public class FlightsApplication extends Application {

	private ArrayList<FlightInfo> flights;
	
	public static final int NO_SORTING = 0;
	public static final int DURATION_SORTING = 1;
	public static final int DURATION_SORTING_DESC = 2;
	public static final int PRICE_SORTING = 3;
	public static final int PRICE_SORTING_DESC = 4;
	
	private int sortingState = NO_SORTING;
	
	private int emptyState = R.string.empty_loading;

	@Override
	public void onCreate() {
		super.onCreate();

		if (flights == null) {
			flights = new ArrayList<FlightInfo>();
		}
	}

	public void parseXMLFromURL(String url) {
		try {
			flights = new XmlPullFlightParser(url).parse();
		} catch (Exception e) {
			flights = null;
		}
	}
	
	public ArrayList<FlightInfo> getFlights() {
		return(flights);
	}

	public int getSortingState() {
		return sortingState;
	}

	public void setSortingState(int state) {
		sortingState = state;
	}
	
	public void sortFlights() {
		sortingState = getSortingState();
		Comparator<FlightInfo> comparator = new Comparator<FlightInfo>() {
			public int compare(FlightInfo lhs, FlightInfo rhs) {
				switch (sortingState) {
				case DURATION_SORTING:
					return lhs.getTripDuration() - rhs.getTripDuration();
				case DURATION_SORTING_DESC:
					return rhs.getTripDuration() - lhs.getTripDuration();
				case PRICE_SORTING:
					return (int) (lhs.getFlightPrice() * 100 - rhs.getFlightPrice() * 100);
				case PRICE_SORTING_DESC:
					return (int) (rhs.getFlightPrice() * 100 - lhs.getFlightPrice() * 100);
				}
				return 0;
			}
		};
		Collections.sort(getFlights(), comparator);
	}

	public int getEmptyState() {
		return emptyState;
	}

	public void setEmptyState(int emptyState) {
		this.emptyState = emptyState;
	}

	
}
