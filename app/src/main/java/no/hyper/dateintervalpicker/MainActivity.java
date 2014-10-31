package no.hyper.dateintervalpicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

/**
 * Created by espenalmdahl on 31/10/14.
 */
public class MainActivity extends Activity {

    private DateIntervalPicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        picker = (DateIntervalPicker) getFragmentManager().findFragmentById(R.id.picker_fragment);
    }

    public void confirm(View v) {
        if ( picker.fromDate != null) {
            Intent intent = new Intent();

            intent.putExtra("from", picker.fromDate.getTime());
            intent.putExtra("to", picker.toDate.getTime());
            this.setResult(100, intent);
            Toast.makeText(this, picker.fromDate.toString() + " to " + picker.toDate, Toast.LENGTH_LONG).show();
        }
        else {
            this.setResult(-100);
            Toast.makeText(this, "No dates selected", Toast.LENGTH_LONG).show();
        }

        this.finish();
    }
}
