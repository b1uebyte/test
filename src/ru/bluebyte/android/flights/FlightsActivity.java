package ru.bluebyte.android.flights;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FlightsActivity extends ListActivity {

	private static final String XML_URL = "http://sevkin.ru/flights.xml";
	private static final String XML_URL2 = "http://bluebyte.ru/flights.xml";
	private static final int REQUEST_SLEEP_TIME = 3000;

	public static final int NO_CLICK = 0;
	public static final int DURATION_BUTTON_CLICKED = 1;
	public static final int PRICE_BUTTON_CLICKED = 2;

	private ImageView refreshButton = null;
	private FlightsApplication application = null;
	private FlightListAdapter adapter = null;
	private ProgressBar progressBar = null;
	private Button durationButton = null;
	private Button priceButton = null;
	private Button preferencesButton = null;
	private boolean imitateLongTimeRequest = false;
	private boolean useAlternativeXml = false;
	private TextView empty = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		application = (FlightsApplication) getApplication();
		setUpViews();

		if ((application.getFlights() != null) && (application.getFlights().size() == 0)) {
			new DownloadXMLTask().execute(useAlternativeXml ? XML_URL2 : XML_URL);
		}

		adapter = new FlightListAdapter(application.getFlights(), this);
		setListAdapter(adapter);
		applySortingState(NO_CLICK);
	}

	private void setUpViews() {
		refreshButton = (ImageView) findViewById(R.id.refreshButton);
		refreshButton.getBackground().setColorFilter(0xFF7c89a4, PorterDuff.Mode.MULTIPLY);
		refreshButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				checkSettings();
				application.setSortingState(FlightsApplication.NO_SORTING);
				applySortingState(NO_CLICK);
				new DownloadXMLTask().execute(useAlternativeXml ? XML_URL2 : XML_URL);
			}
		});

		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		durationButton = (Button) findViewById(R.id.durationBtn);
		setSortingButtonOnClickListener(durationButton, DURATION_BUTTON_CLICKED);

		priceButton = (Button) findViewById(R.id.priceBtn);
		setSortingButtonOnClickListener(priceButton, PRICE_BUTTON_CLICKED);

		preferencesButton = (Button) findViewById(R.id.preferencesButton);
		preferencesButton.getBackground().setColorFilter(0xFF7c89a4, PorterDuff.Mode.MULTIPLY);
		preferencesButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(FlightsActivity.this, Preferences.class);
				startActivity(intent);
			}
		});

		checkSettings();

		empty = (TextView) findViewById(android.R.id.empty);
		empty.setText(application.getEmptyState());
		empty.setVisibility(View.VISIBLE);
	}

	private void setSortingButtonOnClickListener(Button sortingButton, final int clickEvent) {
		sortingButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				applySortingState(clickEvent);
				new SortFlightsTask().execute((Void) null);
			}
		});
	}

	private void checkSettings() {
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		imitateLongTimeRequest = settings.getBoolean("long_request", false);
		useAlternativeXml = settings.getBoolean("large_xml", false);
	}

	public void applySortingState(int clickedButton) {
		// set default table header texts
		durationButton.setText(R.string.duration_button);
		priceButton.setText(R.string.price_button);

		switch (clickedButton) {
		case DURATION_BUTTON_CLICKED:
			setButtonSortingState(durationButton, FlightsApplication.DURATION_SORTING, FlightsApplication.DURATION_SORTING_DESC, 
								  R.string.duration_button_asc, R.string.duration_button_desc);
			break;
			
		case PRICE_BUTTON_CLICKED:
			setButtonSortingState(priceButton, FlightsApplication.PRICE_SORTING, FlightsApplication.PRICE_SORTING_DESC, 
								  R.string.price_button_asc, R.string.price_button_desc);
			break;

		case NO_CLICK:
			switch (application.getSortingState()) {
			case FlightsApplication.DURATION_SORTING:
				durationButton.setText(R.string.duration_button_asc);
				break;
			case FlightsApplication.DURATION_SORTING_DESC:
				durationButton.setText(R.string.duration_button_desc);
				break;
			case FlightsApplication.PRICE_SORTING:
				priceButton.setText(R.string.price_button_asc);
				break;
			case FlightsApplication.PRICE_SORTING_DESC:
				priceButton.setText(R.string.price_button_desc);
				break;
			case FlightsApplication.NO_SORTING:
				break;
			}
			break;
		}
	}

	private void setButtonSortingState(Button clickedButton, int sortingStateAsc, int sortingStateDesc, int sortingStringAsc, int sortingStringDesc) {
		if (application.getSortingState() == sortingStateAsc) {
			application.setSortingState(sortingStateDesc);
			clickedButton.setText(sortingStringDesc);
		} else {
			application.setSortingState(sortingStateAsc);
			clickedButton.setText(sortingStringAsc);
		}	
	}

	class DownloadXMLTask extends AsyncTask<String, Void, Void> {

		@Override
		protected Void doInBackground(String... urls) {
			if (imitateLongTimeRequest) {
				try {
					Thread.sleep(REQUEST_SLEEP_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			application.parseXMLFromURL(urls[0]);
			return (null);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			startLoadingAnimation();
		}

		@Override
		protected void onPostExecute(Void unused) {
			reloadFlightsList();
			stopLoadingAnimation();
			if (application.getFlights() == null) {
				application.setEmptyState(R.string.empty_conn_error);
				empty.setText(R.string.empty_conn_error);
			} else if (application.getFlights().size() == 0) {
				application.setEmptyState(R.string.empty_no_flights);
				empty.setText(R.string.empty_no_flights);
			}
		}
	}

	class SortFlightsTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Void unused) {
			reloadFlightsList();
		}

		@Override
		protected Void doInBackground(Void... params) {
			application.sortFlights();
			return null;
		}
	}

	public void reloadFlightsList() {
		adapter.setFights(application.getFlights());
		adapter.notifyDataSetChanged();
	}

	private void startLoadingAnimation() {
		refreshButton.setVisibility(View.GONE);
		progressBar.setVisibility(View.VISIBLE);
	}

	private void stopLoadingAnimation() {
		progressBar.setVisibility(View.GONE);
		refreshButton.setVisibility(View.VISIBLE);
	}
}