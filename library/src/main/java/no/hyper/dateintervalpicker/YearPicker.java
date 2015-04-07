package no.hyper.dateintervalpicker;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2015/4/1.
 */
public class YearPicker extends GridView {
    public YearPicker(Context context) {
        this(context, null, 0);
    }

    public YearPicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YearPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        onCreate();
    }

    public void onCreate() {
        setAdapter(new MonthAdapter(getContext(), Calendar.getInstance()));
    }


    private static class MonthAdapter extends ArrayAdapter<Date> {
        private Calendar yearToDisplay;
        public MonthAdapter(Context context, Calendar calendar) {
            super(context, 0, new Date[12]);
            yearToDisplay = calendar;
        }

        public void setYearToDisplay(Calendar c) {
            this.yearToDisplay = c;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.month_item_view, parent, false);
            }

            DateIntervalPicker view = (DateIntervalPicker)convertView;

            int tmp = yearToDisplay.get(Calendar.MONTH);
            yearToDisplay.set(Calendar.MONTH, position);
            view.changeDate(yearToDisplay);
            yearToDisplay.set(Calendar.MONTH, tmp);
            convertView = view;

            return convertView;
        }
    }
}
