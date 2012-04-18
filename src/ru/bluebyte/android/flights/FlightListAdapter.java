package ru.bluebyte.android.flights;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class FlightListAdapter extends BaseAdapter {

	private ArrayList<FlightInfo> flights;
	private Context context;

	public FlightListAdapter(ArrayList<FlightInfo> flights, Context context) {
		super();
		this.flights = flights;
		this.context = context;
	}

	public int getCount() {
		if (flights == null) {
			return 0;
		} else {
			return flights.size();
		}
	}

	public FlightInfo getItem(int arg0) {
		return flights.get(arg0);
	}

	public long getItemId(int arg0) {
		return arg0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		FlightListItem fli;
		if (convertView == null) {
			fli = (FlightListItem) View.inflate(context, R.layout.flight_item, null);
		} else {
			fli = (FlightListItem) convertView;
		}
		fli.setFlight(flights.get(position));
		return fli;
	}

	public void setFights(ArrayList<FlightInfo> newFlights) {
		flights = newFlights;
	}
}
