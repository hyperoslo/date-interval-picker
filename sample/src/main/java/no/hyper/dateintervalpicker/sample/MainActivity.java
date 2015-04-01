package no.hyper.dateintervalpicker.sample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;

import no.hyper.dateintervalpicker.DateIntervalPicker;

/**
 * Demo activity for showing how to use the DateIntervalPicker fragment
 *
 * @author Espen Almdahl
 */
public class MainActivity extends ActionBarActivity {

    private DateIntervalPicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        picker = (DateIntervalPicker) findViewById(R.id.picker_fragment);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -12);
        picker.changeDate(cal);

    }

    /**
     * Callback from confirm button declared in layout
     * @param v
     */
    public void confirm(View v) {
        Toast.makeText(this, picker.getFromDate() + " to " + picker.getToDate() + " selected", Toast.LENGTH_LONG).show();
    }

}
