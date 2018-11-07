package com.example.acmecurrencyconvertor;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

public class MainActivity extends Activity {
	
	//Variables
	static final String TOTAL = "TOTAL";
	double currentTotal;
	EditText etNewTask;
	EditText etNewTask2;
	LinearLayout myBackgroundLayout;
	NumberPicker numPick1 = null;
	NumberPicker numPick2 = null;
	int color_scheme = 0;
	int text_size = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// check if app just started or is being restored from memory
        if ( savedInstanceState == null ) // the app just started running
        {
           currentTotal = 0.0; // initialize the amount to zero
        } // end if
        else // app is being restored from memory, not executed from scratch
        {
           // initialize the bill amount to saved amount
           currentTotal = savedInstanceState.getDouble(TOTAL); 
        }
        
		//Number Picker 1
		numPick1 = (NumberPicker)findViewById(R.id.numPicker1);
		numPick1.setMaxValue(100);
		numPick1.setMinValue(0);
		numPick1.setWrapSelectorWheel(false);
		
		// Number Picker 2
		numPick2 = (NumberPicker)findViewById(R.id.numPicker2);
		numPick2.setMaxValue(100);
		numPick2.setMinValue(0);
		numPick2.setWrapSelectorWheel(false);
		
		//EditText1
		etNewTask = (EditText) findViewById(R.id.editText1);
		etNewTask.addTextChangedListener(currencyEditTextWatcher);
		
		//EditText2
		etNewTask2 = (EditText) findViewById(R.id.editText2);
	}
	
	// updates 10, 15 and 20 percent tip EditTexts
    private void updateStandard() 
    {
       // calculate bill total with a ten percent tip
       double convert = currentTotal * 2;
       
       // set editText2's text to 
       etNewTask2.setText(String.format("%.02f", convert));

    } // end method updateStandard
    
	private TextWatcher currencyEditTextWatcher = new TextWatcher() 
    {
       // called when the user enters a number
       @Override
       public void onTextChanged(CharSequence s, int start, 
          int before, int count) 
       {         
          // convert editText1 text to a double
          try
          {
             currentTotal = Double.parseDouble(s.toString());
          } // end try
          catch (NumberFormatException e)
          {
             currentTotal = 0.0; // default if an exception occurs
          } // end catch 

          // update the standard and custom tip EditTexts
          updateStandard(); // update the editText2
       }
       @Override
       public void afterTextChanged(Editable s) 
       {
       } // end method afterTextChanged

       @Override
       public void beforeTextChanged(CharSequence s, int start, int count,
          int after) 
       {
       } // end method beforeTextChanged
    }; // end currencyEditTextWatcher
    
	// save values of billEditText and customSeekBar
    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
       super.onSaveInstanceState(outState);
       
       outState.putDouble(TOTAL, currentTotal);
    } // end method onSaveInstanceState
	
	// set the background color of the Relative Layout
		// set the color of the text
		@SuppressWarnings("deprecation")
		public void changeScheme(int colorValue) {
			myBackgroundLayout = (LinearLayout) findViewById(R.id.backgroundLayout);
			etNewTask = (EditText) findViewById(R.id.editText1);
			etNewTask2 = (EditText) findViewById(R.id.editText2);
			switch (colorValue) {
			case 0:
				myBackgroundLayout.setBackgroundColor(getResources().getColor(
						R.color.NightBG)); // night scheme
				etNewTask.setTextColor(getResources().getColor(R.color.NightTxt));
				etNewTask2.setTextColor(getResources().getColor(R.color.NightTxt));
				break;
			case 1:
				myBackgroundLayout.setBackgroundColor(getResources().getColor(
						R.color.NatureBG)); // nature scheme
				etNewTask.setTextColor(getResources().getColor(R.color.NatureTxt));
				etNewTask2.setTextColor(getResources().getColor(R.color.NatureTxt));
				break;
			case 2:
				myBackgroundLayout.setBackgroundColor(getResources().getColor(
						R.color.FluroBG)); // fluro scheme
				etNewTask.setTextColor(getResources().getColor(R.color.FluroTxt));
				etNewTask.setTextColor(getResources().getColor(R.color.FluroTxt));
				break;
			case 3:
				myBackgroundLayout.setBackgroundColor(getResources().getColor(
						R.color.ConstructionBG)); // construction scheme
				etNewTask.setTextColor(getResources().getColor(
						R.color.ConstructionTxt));
				etNewTask2.setTextColor(getResources().getColor(
						R.color.ConstructionTxt));
				break;
			case 4:
				myBackgroundLayout.setBackgroundColor(getResources().getColor(
						R.color.White)); // standard scheme
				etNewTask.setTextColor(getResources().getColor(R.color.Black));
				etNewTask2.setTextColor(getResources().getColor(R.color.Black));
				break;
			}
		} // end of changeScheme method
		
		// set the text size for the TextView
		public void changeTextSize(int size) {
			etNewTask = (EditText) findViewById(R.id.editText1);
			// text_size = size;
			switch (size) {
			case 0:
				etNewTask.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources()
						.getDimension(R.dimen.text_size_sml));
				etNewTask2.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources()
						.getDimension(R.dimen.text_size_sml));
				break;
			case 1:
				etNewTask.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources()
						.getDimension(R.dimen.text_size_med));
				etNewTask2.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources()
						.getDimension(R.dimen.text_size_med));
				break;
			case 2:
				etNewTask.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources()
						.getDimension(R.dimen.text_size_lge));
				etNewTask2.setTextSize(TypedValue.COMPLEX_UNIT_SP, getResources()
						.getDimension(R.dimen.text_size_lge));
				break;
			}
		} // end of changeTextSize method

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
				default:
					return super.onOptionsItemSelected(item);
				}
	}
}
