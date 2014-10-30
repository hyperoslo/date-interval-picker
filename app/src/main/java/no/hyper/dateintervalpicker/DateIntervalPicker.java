package no.hyper.dateintervalpicker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class DateIntervalPicker extends Activity implements View.OnTouchListener{

    private GridView calendar;
    private CalendarAdapter adapter;

    private Date fromDate, toDate;

    private int downPos;
    private ArrayList<LinearLayout> selectedItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_interval_picker);
        calendar = (GridView) findViewById(R.id.calendar_grid);

        adapter = new CalendarAdapter(this,
                R.layout.date_item);

        calendar.setAdapter(adapter);
        calendar.setOnTouchListener(this);
        selectedItems = new ArrayList<LinearLayout>();
        ((TextView) findViewById(R.id.month)).setText(adapter.getMonthString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.date_interval_picker, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void changeMonth(View v) {
        if (v.getId() == R.id.next_month) {
            adapter.nextMonth();
        }
        else if (v.getId() == R.id.prev_month) {
            adapter.previousMonth();
        }
        ((TextView) findViewById(R.id.month)).setText(adapter.getMonthString());
    }



    @Override
    public boolean onTouch(View view, MotionEvent e) {

        if (e.getActionMasked() == MotionEvent.ACTION_DOWN) {
            clearSelected();
            downPos = calendar.pointToPosition((int) e.getX(), (int) e.getY());

            if (downPos >= adapter.getStartPosition()) {
                selectItem(downPos);
            }
            else if ( downPos == -1 ) {
                downPos = adapter.getCount() -1;
            }
            return true;
        }
        if (e.getActionMasked() == MotionEvent.ACTION_MOVE) {
            int pos = calendar.pointToPosition((int)e.getX(),(int) e.getY());

            if (pos > downPos) {
                clearSelected();
                for (int i = downPos; i <= pos; i++) {
                    selectItem(i);
                }
            }
            //pos is -1 if inside grid but on empty cell
            else if (pos < downPos && pos > -1) {
                clearSelected();
                //reverse adding to have the lowest date at index 0
                for (int i = pos; i <= downPos; i++) {
                    selectItem(i);
                }
            }
            paintSelected();
            return true;
        }
        if (e.getActionMasked() == MotionEvent.ACTION_UP) {
            if ( ! selectedItems.isEmpty() ) {
                CharSequence dateDown = ((TextView) selectedItems.get(0).findViewById(R.id.date)).getText();
                CharSequence dateUp = ((TextView) selectedItems.get(selectedItems.size() - 1).findViewById(R.id.date)).getText();

                Calendar cal = Calendar.getInstance();

                cal.set(Calendar.YEAR, adapter.getYear());
                cal.set(Calendar.MONTH, adapter.getMonth());
                cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt((String) dateDown));

                fromDate = cal.getTime();
                cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt((String) dateUp));
                toDate = cal.getTime();
            }
            return true;
        }
        return false;
    }


    private void selectItem(int position) {
        LinearLayout ll = (LinearLayout) calendar.getItemAtPosition(position);
        if ( ll != null && ll.isEnabled()) {
            selectedItems.add(ll);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void paintSelected() {
        if ( ! selectedItems.isEmpty() ) {
            if (selectedItems.size() == 1) {
                View v = selectedItems.get(0);
                v.setBackground(getResources().getDrawable(R.drawable.selected_single_item));
                ((TextView) v.findViewById(R.id.date)).setTextColor(Color.WHITE);
            } else {
                View v = selectedItems.get(0);

                //make rounded corners on the edges of each week
                if (calendar.getPositionForView(v) % 7 == 6 ) {
                    v.setBackground(getResources().getDrawable(R.drawable.selected_single_item));
                }
                else {
                    v.setBackground(getResources().getDrawable(R.drawable.selected_left_edge));
                }
                ((TextView) v.findViewById(R.id.date)).setTextColor(Color.WHITE);
                for (int i = 1; i < selectedItems.size() - 1; i++) {
                    v = selectedItems.get(i);

                    //again, make rounded corners
                    if (calendar.getPositionForView(v) % 7 == 6) {
                        v.setBackground(getResources().getDrawable(R.drawable.selected_right_edge));
                    } else if (calendar.getPositionForView(v) % 7 == 0) {
                        v.setBackground(getResources().getDrawable(R.drawable.selected_left_edge));
                    } else {
                        v.setBackground(getResources().getDrawable(R.drawable.selected));
                    }
                    ((TextView) v.findViewById(R.id.date)).setTextColor(Color.WHITE);
                }
                v = selectedItems.get(selectedItems.size() - 1);

                if ( calendar.getPositionForView(v) % 7 == 0 ) {
                    v.setBackground(getResources().getDrawable(R.drawable.selected_single_item));
                }
                else {
                    v.setBackground(getResources().getDrawable(R.drawable.selected_right_edge));
                }

                ((TextView) v.findViewById(R.id.date)).setTextColor(Color.WHITE);
            }
        }
    }

    private void clearSelected() {
        for ( LinearLayout ll : selectedItems ) {
            ll.setBackgroundColor(Color.WHITE);
            ((TextView) ll.findViewById(R.id.date)).setTextColor(Color.DKGRAY);
        }
        selectedItems.clear();
    }


    public void confirm(View v) {
        if ( fromDate != null) {
            Intent intent = new Intent();

            intent.putExtra("from", fromDate.getTime());
            intent.putExtra("to", toDate.getTime());
            this.setResult(100, intent);
            Toast.makeText(this, fromDate.toString() + " to " + toDate, Toast.LENGTH_LONG).show();
        }
        else {
            this.setResult(-100);
            Toast.makeText(this, "No dates selected", Toast.LENGTH_LONG).show();
        }

        this.finish();
    }
}
