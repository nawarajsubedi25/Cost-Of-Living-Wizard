package edu.gatech.seclass.sdpsalarycomp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.EditText;
import android.text.Editable;
import android.text.TextWatcher;
import java.util.ArrayList;
import java.util.List;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;
import android.view.inputmethod.InputMethodManager;
import android.content.Context;

/**
 * Description : This is a simple android app to calculate the salary required to have a standard of
 * living in the New City which is equivalent to the standard of living that the Base
 * Salary provides in the Current City.
 *
 * @author : Nawaraj Subedi
 * @since : Sep 9, 2018
 */

public class SDPSalaryCompActivity extends AppCompatActivity implements OnItemSelectedListener {
    // Data Fields
    private EditText baseSalaryField;
    private EditText finalSalaryField;
    private Spinner currentCitySpinner;
    private Spinner newCitySpinner;
    private String newCityName;
    private String currentCityName;
    private int baseSalaryInput;
    MotionEvent event;

    /*
     * Method was called Called when the activity is starting
     * @param Bundle: If the activity is being re-initialized after previously being shut down then
     * this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note:
     * Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // to inflate the activity's UI
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sdpsalary_comp);

        //to programmatically interact with widgets in the UI
        baseSalaryField = (EditText) findViewById(R.id.baseSalary);

        // Initialized EditText base salary Input
        baseSalaryField.setText(String.valueOf(0));

        //to programmatically interact with widgets in the UI
        finalSalaryField = (EditText) findViewById(R.id.targetSalary);
        // Initialized EditText final salary Input
        finalSalaryField.setText(String.valueOf(0));

        baseSalaryField.addTextChangedListener(baseSalaryFieldWatcher);
        currentCitySpinner = (Spinner) findViewById(R.id.currentCity);
        newCitySpinner = (Spinner) findViewById(R.id.newCity);

        // Spinner click listener
        currentCitySpinner.setOnItemSelectedListener(this);
        newCitySpinner.setOnItemSelectedListener(this);


        // Spinner Drop down elements
        List<String> CityName = new ArrayList<String>();
        CityName.add("Atlanta, GA");
        CityName.add("Austin, TX");
        CityName.add("Boston, MA");
        CityName.add("Honolulu, HI");
        CityName.add("Las Vegas, NV");
        CityName.add("Mountain View, CA");
        CityName.add("New York City, NY");
        CityName.add("San Francisco, CA");
        CityName.add("Seattle, WA");
        CityName.add("Springfield, MO");
        CityName.add("Tampa, FL");
        CityName.add("Washington D.C.");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, CityName);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        currentCitySpinner.setAdapter(dataAdapter);
        newCitySpinner.setAdapter(dataAdapter);
    }

    /*
     * When an object of a type is attached to an Editable, its methods will be called when the text
     * is changed.
     */
    private TextWatcher baseSalaryFieldWatcher = new TextWatcher() {
        /*
         * This method is called to notify you that, within s, the count characters beginning at
         * start have just replaced old text that had length before. It is an error to attempt to
         * make changes to s from this callback
         * @param s CharSequence
         * @param Start An int
         * @param count An int
         * @param after An int
         */
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        /*
         * This method is called to notify you that, somewhere within s, the text has been changed.
         * @param s Editable input values
         */
        public void afterTextChanged(Editable s) {

            try {
                /*
                If length of editable string and base salary value  is greater than or equal to
                zero and value provided
                */
                if ((s.length() >= 0) && (Integer.parseInt(s.toString()) >= 0)) {
                    baseSalaryField.setError(null);
                    baseSalaryInput = Integer.parseInt(s.toString());
                    // Call method to calculate standard of living
                    // Call in every change user made
                    result(baseSalaryInput, newCityName, currentCityName);

                } else {
                    baseSalaryField.setError("Invalid salary");
                    finalSalaryField.setText("");
                }

            } catch (NumberFormatException E) {
                baseSalaryField.setError("Invalid salary");
                finalSalaryField.setText("");

            }

        }

        /*
         * This method is called to notify you that, within s, the count characters beginning at
         * start are about to be replaced by new text with length after. It is an error to attempt
         * to make changes to s from this callback.
         * @param s CharSequence
         * @param Start An int
         * @param count An int
         * @param after An int
         */
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

    };

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        // Initialized the spinner
        newCityName = newCitySpinner.getSelectedItem().toString();
        currentCityName = currentCitySpinner.getSelectedItem().toString();
        onTouchEvent(event);
        // Call to calculate the standard of the living
        result(baseSalaryInput, currentCityName, newCityName);


    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // Auto generated method
    }

    /*
     * A void method which takes the base salary, name of current city and new city as argument
     * and calculate the standard of living based on the  Base Salary provides in the Current City.
     * @param n An int which is the base salary
     * @param currentCity An String which contain name of current city
     * @param currentCity An String which contain name of new city
     */
    public void result(int n, String currentCity, String newCity) {

        int currentCityOfLiving = costOfLivingIndex(currentCity);

        /*
         Call to get cost of living indexes of the respective cities
         */
        int newCityOfLiving = costOfLivingIndex(newCity);
        int standardOfLiving;
        standardOfLiving = (int) Math.round((double) (n * newCityOfLiving) / currentCityOfLiving);
        /*
         If input base salary is empty
         */
        if (baseSalaryField.getText().toString().equals("")) {
            finalSalaryField.setText("");
            baseSalaryField.setError("Invalid salary");
        } else {
            finalSalaryField.setText(String.valueOf(standardOfLiving));
        }
    }

    /*
     * A static method which takes the name of city as argument and return the value of living index
     * of given city.
     * @param cityName A string value which contains the name of city
     * @return costOfLivingIndex An int value of cost of living index of given city.
     */
    public static int costOfLivingIndex(String cityName) {
        int costOfLivingIndex;
        switch (cityName) {
            case ("Atlanta, GA"):
                costOfLivingIndex = 160;
                break;
            case ("Austin, TX"):
                costOfLivingIndex = 152;
                break;
            case ("Boston, MA"):
                costOfLivingIndex = 197;
                break;
            case ("Honolulu, HI"):
                costOfLivingIndex = 201;
                break;
            case ("Las Vegas, NV"):
                costOfLivingIndex = 153;
                break;
            case ("Mountain View, CA"):
                costOfLivingIndex = 244;
                break;
            case ("New York City, NY"):
                costOfLivingIndex = 232;
                break;
            case ("San Francisco, CA"):
                costOfLivingIndex = 241;
                break;
            case ("Seattle, WA"):
                costOfLivingIndex = 198;
                break;
            case ("Springfield, MO"):
                costOfLivingIndex = 114;
                break;
            case ("Tampa, FL"):
                costOfLivingIndex = 139;
                break;
            default:
                costOfLivingIndex = 217;
                break;
        }
        return costOfLivingIndex;
    }

    /*
     * Hide keyBoard when user click out side of EditText and after choosing spinner.
     * @copyright: https://stackoverflow.com/questions/8697499/hide-keyboard-when-user-taps-anywhere-else-on-the-screen-in-android/40797927
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {

            InputMethodManager hideKeyboard = (InputMethodManager) getSystemService(Context.
                    INPUT_METHOD_SERVICE);
            hideKeyboard.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            return true;

        } catch (NullPointerException E)

        {
            return false;
        }
    }

}
