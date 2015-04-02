package no.hyper.dateintervalpicker;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class DateIntervalPicker extends RelativeLayout implements View.OnTouchListener{

    private GridView calendar;
    private TextView month;
    private PickerAdapter adapter;

    private Date fromDate, toDate;

    private int downPos;  //grid position of touch start
    private ArrayList<LinearLayout> selectedItems;

    TypedArray attributes;

    public DateIntervalPicker(Context context) {
        this(context, null, 0);
    }

    public DateIntervalPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DateIntervalPicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        attributes = context.obtainStyledAttributes(attrs, R.styleable.DateIntervalPicker);
        onCreate();
    }


    public void onCreate() {
        View v = inflate(getContext(), R.layout.date_interval_picker_fragment, this);

        calendar = (GridView) v.findViewById(R.id.calendar_grid);

        adapter = new PickerAdapter(getContext(), attributes);

        calendar.setAdapter(adapter);
        if (attributes.getBoolean(R.styleable.DateIntervalPicker_datePickable, false))
            calendar.setOnTouchListener(this);
        selectedItems = new ArrayList<LinearLayout>();

        int headerFontSize = attributes.getDimensionPixelSize(R.styleable.DateIntervalPicker_headerTextSize, 14);

        month = (TextView) v.findViewById(R.id.month);
        month.setTextSize(TypedValue.COMPLEX_UNIT_SP, headerFontSize);
        month.setText(adapter.getMonthString());

        boolean showPreAndNext = attributes.getBoolean(R.styleable.DateIntervalPicker_showPreAndNext, true);

        if (showPreAndNext) {
            View.OnClickListener ocl = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeMonth(view);
                }
            };

            TextView tv = (TextView) v.findViewById(R.id.next_month);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, headerFontSize);
            tv.setOnClickListener(ocl);
            tv.setVisibility(View.VISIBLE);

            tv = (TextView) v.findViewById(R.id.prev_month);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, headerFontSize);
            tv.setOnClickListener(ocl);
            tv.setVisibility(View.VISIBLE);
        }
        // the calendar doesn't really work very well in landscape, so lock to portrait mode
        // activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void changeDate(Calendar cal) {
        adapter.setDate(cal);
        month.setText(adapter.getMonthString());
    }

    public void changeMonth(View v) {
        if (v.getId() == R.id.next_month) {
            adapter.nextMonth();
        }
        else if (v.getId() == R.id.prev_month) {
            adapter.previousMonth();
        }
        month.setText(adapter.getMonthString());
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
            ll.setBackgroundColor(Color.TRANSPARENT);
            ((TextView) ll.findViewById(R.id.date)).setTextColor(getContext().getResources().getColor(R.color.gray_dark));
        }
        selectedItems.clear();
    }

    public Date getFromDate() {
        return fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

}
