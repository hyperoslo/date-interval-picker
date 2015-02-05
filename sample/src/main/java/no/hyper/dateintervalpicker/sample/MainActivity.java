package no.hyper.dateintervalpicker.sample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import no.hyper.dateintervalpicker.DateIntervalPicker;


/**
 * Created by espenalmdahl on 31/10/14.
 *
 * This class is just a startup class for demo purpose.
 */
public class MainActivity extends Activity {

    private DateIntervalPicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        picker = (DateIntervalPicker) getFragmentManager().findFragmentById(R.id.picker_fragment);
    }

    /**
     * Called from the button in the main_activity layout.
     *
     * Shows how to access the to and from dates in the picker fragment and use them in your main activity
     */
    public void confirm(View v) {
        if ( picker.getFromDate() != null) {
            Intent intent = new Intent();

            intent.putExtra("from", picker.getFromDate().getTime());
            intent.putExtra("to", picker.getToDate().getTime());
            this.setResult(100, intent);
            Toast.makeText(this, picker.getFromDate() + " to " + picker.getToDate(), Toast.LENGTH_LONG).show();
        }
        else {
            this.setResult(-100);
            Toast.makeText(this, "No dates selected", Toast.LENGTH_LONG).show();
        }

        this.finish();
    }
}
