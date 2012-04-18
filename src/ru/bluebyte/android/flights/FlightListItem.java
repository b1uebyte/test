package ru.bluebyte.android.flights;

import java.text.NumberFormat;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FlightListItem extends LinearLayout {

	private FlightInfo flight;
	private TextView cities;
	private TextView flightCarrier;
	private TextView takeoffDate;
	private TextView takeoffTime;
	private TextView landingDate;
	private TextView landingTime;
	private TextView flightDuration;
	private Button flightPrice;

	public FlightListItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();

		cities = (TextView) findViewById(R.id.cities);
		flightCarrier = (TextView) findViewById(R.id.flightCarrier);
		takeoffDate = (TextView) findViewById(R.id.takeoffDate);
		takeoffTime = (TextView) findViewById(R.id.takeoffTime);
		landingDate = (TextView) findViewById(R.id.landingDate);
		landingTime = (TextView) findViewById(R.id.landingTime);
		flightDuration = (TextView) findViewById(R.id.flightDuration);
		flightPrice = (Button) findViewById(R.id.priceButton);
		flightPrice.getBackground().setColorFilter(0xFF97aad3, PorterDuff.Mode.MULTIPLY);
	}

	public FlightInfo getFlight() {
		return flight;
	}

	public void setFlight(FlightInfo flight) {
		this.flight = flight;

		cities.setText(flight.getTakeoffCity() + " ― " + flight.getLandingCity());
		flightCarrier.setText(flight.getFlightCarrier() + "   " + "Number: " + flight.getFlightNumber().toString() + "   " + "EQ: " + flight.getFlightEq());

		takeoffDate.setText(flight.getTakeoffDate());
		takeoffTime.setText(flight.getTakeoffTime());

		landingDate.setText(flight.getLandingDate());
		landingTime.setText(flight.getLandingTime());

		flightDuration.setText(flight.getDurationString());

		NumberFormat formatter = NumberFormat.getNumberInstance();
		formatter.setGroupingUsed(true);
		
		flightPrice.setText(formatter.format(flight.getFlightPrice()));
	}

}
