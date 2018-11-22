package com.example.acmecurrencyconvertor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity {

	// Variables
	private static final String PICKER1 = "PICKER1";
	private static final String PICKER2 = "PICKER2";
	private static final String FONTTHEME = "FONTTHEME";
	private static final String COLORTHEME = "COLORTHEME";
	private static final String TEXTSIZE = "TEXTSIZE";
	private static String url = "http://data.fixer.io/api/latest"
			+ "?access_key=YOUR_KEY_HERE"
			+ "&symbols=AUD,JPY,GBP,USD,INR,CAD,HKD,BRL,NZD,EUR";
	private int fontTheme;
	private int colorTheme;
	private int txtSize;
	private int picker1;
	private int picker2;
	private double userInputAmount; // the converted amount to display to user
	private EditText etText1; // user inputs value
	private EditText etText2; // displays currentTotal
	private TextView updateText; // Update Text tells the date when was updated
	private TextView savedText; // Update Text tells the date when was saved
	private LinearLayout myBackgroundLayout;
	private NumberPicker numPick1 = null;
	private NumberPicker numPick2 = null;
	private Button buttonUpdate;
	private Button buttonSave;
	private ProgressDialog pd;
	public String currencyString;
	public JSONObject jsonOBJ;
	public String date;
	private Currency currency = new Currency();
	private double originRateValue;
	private double targetRateValue;
	private String fileName = "savedData.dat";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);	
				
		// TextViews for update and save
		savedText = (TextView) findViewById(R.id.textView1);
		updateText = (TextView) findViewById(R.id.textView2);

		// load from file
		if (isFilePresent(fileName)) {
			// Load currency and set text
			currency = loadFromFile(fileName);
			savedText.setText(currency.getDateSaved());
			updateText.setText(currency.getDate());
		} else {
			// first time running with no data
			savedText.setText(currency.getDateSaved());
			updateText.setText(currency.getDate());
		}

		// String Array for number pickers
		String[] currencyNames = { "Australian Dollar", "Japanese Yen", "British Pound", "US Dollar", "Indian Rupee",
				"Canadian Dollar", "HongKong Dollar", "Brazil Real", "NewZealand Dollar", "Euro" };
		
		//Set the values for target and oringin values
		originRateValue = Double.parseDouble(currency.getAUD());
		targetRateValue = Double.parseDouble(currency.getJPY());
		
		// Number Picker 1
		numPick1 = (NumberPicker) findViewById(R.id.numPicker1);
		numPick1.setMaxValue(currencyNames.length - 1);
		numPick1.setMinValue(0);		
		numPick1.setDisplayedValues(currencyNames);
		numPick1.setWrapSelectorWheel(true);
		numPick1.setValue(0); //1 is set too aud

		// Listener for Number picker 1 this will be our origin rate value
		numPick1.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				picker1 = newVal;
				switch (newVal) {				
				case 0:
					// aud
					originRateValue = Double.parseDouble(currency.getAUD());
					break;
				case 1:
					// jpy
					originRateValue = Double.parseDouble(currency.getJPY());
					break;
				case 2:
					// gbp
					originRateValue = Double.parseDouble(currency.getGBP());
					break;
				case 3:
					// usd
					originRateValue = Double.parseDouble(currency.getUSD());
					break;
				case 4:
					// inr
					originRateValue = Double.parseDouble(currency.getINR());
					break;
				case 5:
					// cad
					originRateValue = Double.parseDouble(currency.getCAD());
					break;
				case 6:
					// hkd
					originRateValue = Double.parseDouble(currency.getHKD());
					break;
				case 7:
					// brl
					originRateValue = Double.parseDouble(currency.getBRL());
					break;
				case 8:
					// nzd
					originRateValue = Double.parseDouble(currency.getNZD());
					break;
				case 9:
					// eur
					originRateValue = Double.parseDouble(currency.getEUR());
					break;
				}
				updateConversion();
			}
		});

		// Number Picker 2 this will be our target rate value
		numPick2 = (NumberPicker) findViewById(R.id.numPicker2);
		numPick2.setMaxValue(currencyNames.length - 1);
		numPick2.setMinValue(0);		
		numPick2.setDisplayedValues(currencyNames);
		numPick2.setWrapSelectorWheel(true);
		numPick2.setValue(1); //2 set to jpy

		// Listener for Number picker 2
		numPick2.setOnValueChangedListener(new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
				picker2 = newVal;
				switch (newVal) {
				case 0:
					// aud
					targetRateValue = Double.parseDouble(currency.getAUD());
					break;
				case 1:
					// jpy
					targetRateValue = Double.parseDouble(currency.getJPY());
					break;
				case 2:
					// gbp
					targetRateValue = Double.parseDouble(currency.getGBP());
					break;
				case 3:
					// usd
					targetRateValue = Double.parseDouble(currency.getUSD());
					break;
				case 4:
					// inr
					targetRateValue = Double.parseDouble(currency.getINR());
					break;
				case 5:
					// cad
					targetRateValue = Double.parseDouble(currency.getCAD());
					break;
				case 6:
					// hkd
					targetRateValue = Double.parseDouble(currency.getHKD());
					break;
				case 7:
					// brl
					targetRateValue = Double.parseDouble(currency.getBRL());
					break;
				case 8:
					// nzd
					targetRateValue = Double.parseDouble(currency.getNZD());
					break;
				case 9:
					// eur
					targetRateValue = Double.parseDouble(currency.getEUR());
					break;
				}
				updateConversion();
			}			
		});		
		

		// EditText1
		etText1 = (EditText) findViewById(R.id.editText1);
		etText1.addTextChangedListener(currencyEditTextWatcher);

		// EditText2
		etText2 = (EditText) findViewById(R.id.editText2);

		// Update Button
		buttonUpdate = (Button) findViewById(R.id.btnUpdate);
		buttonUpdate.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				new JsonTask().execute(url);
			}
		});

		// Save Button
		buttonSave = (Button) findViewById(R.id.btnSave);
		buttonSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {				
				currency.setDateSaved("Saved: "+ sayTimeDate());
				savedText.setText(currency.getDateSaved());
				saveFile(fileName, currency);
			}
		});
		
		// check if just started or is being restored from memory
				if (savedInstanceState != null) 
				{
					try {
						//get the values for pickers
						int pic1 = savedInstanceState.getInt(PICKER1);
						int pic2 = savedInstanceState.getInt(PICKER2);
						
						//Set the pickerValues on rotation
						numPick1.setMinValue(pic1);
						numPick2.setMinValue(pic2);
						
						//get the key values
						int cTheme = savedInstanceState.getInt(COLORTHEME);
						int fTheme = savedInstanceState.getInt(FONTTHEME);
						int txtSize = savedInstanceState.getInt(TEXTSIZE);						
						//Change the theme from last activity that was closed
						changeScheme(cTheme);
						changeTextSize(txtSize);
						changeFont(fTheme);
						
					} catch (NumberFormatException e) {
						e.printStackTrace();
					}
				} else {
					changeScheme(4);
					changeTextSize(1);
					changeFont(3);
				}
	}

//	// Write to file on application closing
//  // Only used this for testing the API	
//	@Override
//	protected void onStop() {
//		currency.setDateSaved(sayTimeDate());
//		saveFile(fileName, currency);
//	}
	
	//gets time
	public String sayTimeDate() {
		Date currentTime = Calendar.getInstance().getTime();
        return currentTime.toString();
    }

	// Creates a file with the currency object
	private void saveFile(String fileName, Currency c) {
		try {
			FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(c);
			os.close();
			fos.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// reads from file
	private Currency loadFromFile(String fileName) {
		try {
			FileInputStream fis = openFileInput(fileName);
			ObjectInputStream isr = new ObjectInputStream(fis);
			Currency tempCurrency = (Currency) isr.readObject();

			return tempCurrency;
		} catch (FileNotFoundException fileNotFound) {
			return null;
		} catch (IOException ioException) {
			return null;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	// Checks if file exists
	public boolean isFilePresent(String fileName) {
		String path = getFilesDir().getAbsolutePath() + "/" + fileName;
		File file = new File(path);
		return file.exists();
	}
	
	//Class that connects to the URL of jSON array and then sets the values in the currency class
	private class JsonTask extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			//Display message
			pd = new ProgressDialog(MainActivity.this);
			pd.setMessage("Please wait");
			pd.setCancelable(false);
			pd.show();
		}

		@Override
		protected String doInBackground(String... arg) {

			HttpURLConnection connection = null;
			BufferedReader reader = null;

			try {
				URL url = new URL(arg[0]);
				connection = (HttpURLConnection) url.openConnection();
				connection.connect();

				InputStream stream = connection.getInputStream();

				reader = new BufferedReader(new InputStreamReader(stream));

				StringBuffer buffer = new StringBuffer();
				String line = "";

				while ((line = reader.readLine()) != null) {
					buffer.append(line + "\n");
					Log.d("Response: ", "> " + line);
				}
				currencyString = buffer.toString();
				return buffer.toString();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (connection != null) {
					connection.disconnect();
				}
				try {
					if (reader != null) {
						reader.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (pd.isShowing()) {
				pd.dismiss();
			}

			try {
				jsonOBJ = new JSONObject(currencyString);
				//time the update was made
				updateText.setText("Updated: " + sayTimeDate());
				currency.setDate("Updated: " + sayTimeDate());

				// String quotes = jsonOBJ.getString("quotes");
				String AUD = jsonOBJ.getJSONObject("rates").getString("AUD");
				String GBP = jsonOBJ.getJSONObject("rates").getString("GBP");
				String JPY = jsonOBJ.getJSONObject("rates").getString("JPY");
				String USD = jsonOBJ.getJSONObject("rates").getString("USD");
				String INR = jsonOBJ.getJSONObject("rates").getString("INR");
				String CAD = jsonOBJ.getJSONObject("rates").getString("CAD");
				String HKD = jsonOBJ.getJSONObject("rates").getString("HKD");
				String BRL = jsonOBJ.getJSONObject("rates").getString("BRL");
				String NZD = jsonOBJ.getJSONObject("rates").getString("NZD");
				String EUR = jsonOBJ.getJSONObject("rates").getString("EUR");

				// Set values;
				currency.setAUD(AUD);
				currency.setGBP(GBP);
				currency.setJPY(JPY);
				currency.setUSD(USD);
				currency.setINR(INR);
				currency.setCAD(CAD);
				currency.setHKD(HKD);
				currency.setBRL(BRL);
				currency.setNZD(NZD);
				currency.setEUR(EUR);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	// Returns a Date format to display to user
	@SuppressLint("SimpleDateFormat")
	private String getDate(long time) {
		SimpleDateFormat sdf = new SimpleDateFormat("E dd/MM/yyyy 'at' hh:mm:ss a zzz");
		String localTime = sdf.format(new Date(time * 1000));
		return localTime.toString();
	}

	// updates etText1
	private void updateConversion() {
		double dValue;
		// CrossRate		
		dValue = targetRateValue / originRateValue;
		
		// calculate conversion
		double convertTotal = userInputAmount * dValue;

		// set editText2's text to
		etText2.setText(String.format("%.04f", convertTotal));

	}

	private TextWatcher currencyEditTextWatcher = new TextWatcher() {
		// called when the user enters a number
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// convert editText1 text to a double
			try {
				userInputAmount = Double.parseDouble(s.toString());
			} catch (NumberFormatException e) {
				userInputAmount = 0.0; // default if an exception occurs
			}

			// update the editText2
			updateConversion();
		}

		@Override
		public void afterTextChanged(Editable s) {
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

	};

	// save values for rotating to landscape
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		try {
			outState.putInt(PICKER1, picker1);
			outState.putInt(PICKER2, picker2);
			outState.putInt(FONTTHEME, fontTheme);
			outState.putInt(COLORTHEME, colorTheme);
			outState.putInt(TEXTSIZE, txtSize);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// set the background color of the Relative Layout
	// set the color of the text
	public void changeScheme(int colorValue) {
		myBackgroundLayout = (LinearLayout) findViewById(R.id.backgroundLayout);
		etText1 = (EditText) findViewById(R.id.editText1);
		etText2 = (EditText) findViewById(R.id.editText2);
		savedText = (TextView) findViewById(R.id.textView1);
		updateText = (TextView) findViewById(R.id.textView2);
		buttonSave = (Button) findViewById(R.id.btnSave);
		buttonUpdate = (Button) findViewById(R.id.btnUpdate);
		numPick1 = (NumberPicker) findViewById(R.id.numPicker1);
		numPick2 = (NumberPicker) findViewById(R.id.numPicker2);
		
		
		colorTheme = colorValue;  // Keep track of color theme for onSave
		switch (colorValue) {
		case 0:
			numPick1.setBackgroundColor(getResources().getColor(R.color.NightBtnBG));
			numPick2.setBackgroundColor(getResources().getColor(R.color.NightBtnBG));
			myBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.NightBG)); // night scheme
			buttonSave.setBackgroundResource(R.drawable.night_button);			
			buttonUpdate.setBackgroundResource(R.drawable.night_button);
			etText1.setTextColor(getResources().getColor(R.color.NightTxt));
			etText2.setTextColor(getResources().getColor(R.color.NightTxt));
			updateText.setTextColor(getResources().getColor(R.color.NightTxt));
			savedText.setTextColor(getResources().getColor(R.color.NightTxt));
			break;
		case 1:
			numPick1.setBackgroundColor(getResources().getColor(R.color.NatureBtnBG));
			numPick2.setBackgroundColor(getResources().getColor(R.color.NatureBtnBG));
			myBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.NatureBG));
			buttonSave.setBackgroundResource(R.drawable.nature_button);
			buttonUpdate.setBackgroundResource(R.drawable.nature_button);// nature scheme
			etText1.setTextColor(getResources().getColor(R.color.NatureTxt));
			etText2.setTextColor(getResources().getColor(R.color.NatureTxt));
			updateText.setTextColor(getResources().getColor(R.color.NatureTxt));
			savedText.setTextColor(getResources().getColor(R.color.NatureTxt));
			break;
		case 2:
			numPick1.setBackgroundColor(getResources().getColor(R.color.FluroBtnBG));
			numPick2.setBackgroundColor(getResources().getColor(R.color.FluroBtnBG));
			myBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.FluroBG)); // fluro scheme
			buttonSave.setBackgroundResource(R.drawable.fluro_button);
			buttonUpdate.setBackgroundResource(R.drawable.fluro_button);
			etText1.setTextColor(getResources().getColor(R.color.FluroTxt));
			etText2.setTextColor(getResources().getColor(R.color.FluroTxt));
			updateText.setTextColor(getResources().getColor(R.color.FluroTxt));
			savedText.setTextColor(getResources().getColor(R.color.FluroTxt));
			break;
		case 3:
			numPick1.setBackgroundColor(getResources().getColor(R.color.ConstructionBtnBG));
			numPick2.setBackgroundColor(getResources().getColor(R.color.ConstructionBtnBG));
			myBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.ConstructionBG)); // construction scheme
			buttonSave.setBackgroundResource(R.drawable.constuction_button);
			buttonUpdate.setBackgroundResource(R.drawable.constuction_button);
			etText1.setTextColor(getResources().getColor(R.color.ConstructionTxt));
			etText2.setTextColor(getResources().getColor(R.color.ConstructionTxt));
			updateText.setTextColor(getResources().getColor(R.color.ConstructionTxt));
			savedText.setTextColor(getResources().getColor(R.color.ConstructionTxt));
			break;
		case 4:
			numPick1.setBackgroundColor(getResources().getColor(R.color.White));
			numPick2.setBackgroundColor(getResources().getColor(R.color.White));
			myBackgroundLayout.setBackgroundColor(getResources().getColor(R.color.White)); // Default scheme
			buttonSave.setBackgroundResource(android.R.drawable.btn_default);
			buttonUpdate.setBackgroundResource(android.R.drawable.btn_default);
			etText1.setTextColor(getResources().getColor(R.color.Black));
			etText2.setTextColor(getResources().getColor(R.color.Black));
			updateText.setTextColor(getResources().getColor(R.color.Black));
			savedText.setTextColor(getResources().getColor(R.color.Black));
			break;
		}
	} // end of changeScheme method

	// set the text size for the TextView
	public void changeTextSize(int size) {
		etText1 = (EditText) findViewById(R.id.editText1);
		etText2 = (EditText) findViewById(R.id.editText2);
		txtSize = size;  //Keep track of what text size for onSave
		switch (size) {
		case 0:
			etText1.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.text_size_sml));
			etText2.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.text_size_sml));
			break;
		case 1:
			etText1.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.text_size_med));
			etText2.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.text_size_med));
			break;
		case 2:
			etText1.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.text_size_lge));
			etText2.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources().getDimension(R.dimen.text_size_lge));
			break;
		}
	}
	
	// set the font
	public void changeFont(int font) {
		Typeface type;
		etText1 = (EditText) findViewById(R.id.editText1);
		etText2 = (EditText) findViewById(R.id.editText2);
		savedText = (TextView) findViewById(R.id.textView1);
		updateText = (TextView) findViewById(R.id.textView2);		
		fontTheme = font;  //Keep track of what font for onSave
		switch (font) {
		case 0:
			type = Typeface.createFromAsset(getAssets(),"fonts/arial_narrow_7.ttf");
			etText1.setTypeface(type);
			etText2.setTypeface(type);
			savedText.setTypeface(type);
			updateText.setTypeface(type);
			
			break;
		case 1:
			type = Typeface.createFromAsset(getAssets(),"fonts/tahoma.ttf");
			etText1.setTypeface(type);
			etText2.setTypeface(type);
			savedText.setTypeface(type);
			updateText.setTypeface(type);
			break;
		case 2:
			type = Typeface.createFromAsset(getAssets(),"fonts/calibri.ttf");
			etText1.setTypeface(type);
			etText2.setTypeface(type);
			savedText.setTypeface(type);
			updateText.setTypeface(type);
			break;
		case 3:
			type = Typeface.createFromAsset(getAssets(),"fonts/Roboto-LightItalic.ttf");
			etText1.setTypeface(type);
			etText2.setTypeface(type);
			savedText.setTypeface(type);
			updateText.setTypeface(type);
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// call four (4) colour scheme in the changeScheme method
		// to handle the background and text colour. Call three (3)
		// text sizes from the changeTextSize method.
		int id = item.getItemId();
		switch (id) {
		case R.id.night:
			changeScheme(0);
			return true;
		case R.id.nature:
			changeScheme(1);
			return true;
		case R.id.fluro:
			changeScheme(2);
			return true;
		case R.id.construction:
			changeScheme(3);
			return true;
		case R.id.standard:
			changeScheme(4);
			return true;
		case R.id.text_sml:
			changeTextSize(0);
			return true;
		case R.id.text_med:
			changeTextSize(1);
			return true;
		case R.id.text_lrg:
			changeTextSize(2);
			return true;
		case R.id.arial:
			changeFont(0);
			return true;
		case R.id.tahoma:
			changeFont(1);
			return true;
		case R.id.calibiri:
			changeFont(2);
			return true;
		case R.id.standard_font:
			changeFont(3);
			return true;
		case R.id.helpItem:
			File file = new File(Environment.getExternalStorageDirectory(),
					"UserManual.pdf");
			Uri path = Uri.fromFile(file);
			Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
			pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			pdfOpenintent.setDataAndType(path, "application/pdf");
			try {
				startActivity(pdfOpenintent);
			} catch (ActivityNotFoundException e) {
				
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
