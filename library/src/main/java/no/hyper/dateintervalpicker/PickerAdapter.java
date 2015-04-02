package no.hyper.dateintervalpicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by espenalmdahl on 28/10/14.
 */
public class PickerAdapter extends BaseAdapter {

    private Calendar calendar;
    private Locale locale = Locale.getDefault();
    private ArrayList<String> days;
    private ArrayList<String> dates;

    // TODO: Not a very good practise to holding to the view layer components
    private HashMap<Integer, TextView> containers;
    private int visibleMonth;
    private int visibleYear;
    private Context context;
    private int startPosition;

    private int dateTextSize;

    public PickerAdapter(Context context, TypedArray attributes) {
        this.context = context;
        calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTime(new Date());
        locale = Locale.getDefault();

        dateTextSize = attributes.getDimensionPixelSize(R.styleable.DateIntervalPicker_dateTextSize, 14);

        //first set day initials for use in top row
        days = new ArrayList<String>();
        DateFormatSymbols symbols = new DateFormatSymbols(locale);
        String[] dayNames = symbols.getShortWeekdays();
        for ( int i = 0 ; i < dayNames.length; i++) {
            int index = (i + calendar.getFirstDayOfWeek()) % dayNames.length;
            if (index > 0) {
                days.add("" + dayNames[index].charAt(0));
            }
        }
        dates = new ArrayList<String>();
        containers = new HashMap<Integer, TextView>();
        buildArray(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));

    }


    public static class ViewHolder {
        public final TextView date;

        public ViewHolder(TextView dView) {
            this.date = dView;
        }
    }

    @Override
    public int getCount() {
        return dates.size();
    }

    @Override
    public TextView getItem(int i) {
        return containers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView dateView;
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.date_item, parent, false);

            dateView = (TextView) convertView;
            dateView.setTextSize(TypedValue.COMPLEX_UNIT_SP, dateTextSize);
            convertView.setTag(new ViewHolder(dateView));
        } else {
            ViewHolder viewHolder = (ViewHolder) convertView.getTag();
            dateView = viewHolder.date;
        }
        String content = dates.get(position);
        dateView.setText(content);

        if ( content.equals("") || position < 7) {
            dateView.setTextColor(context.getResources().getColor(R.color.gray));
            convertView.setEnabled(false);
        }
        else {
            dateView.setTextColor(context.getResources().getColor(R.color.gray_dark));
            convertView.setEnabled(true);
        }

        containers.put(position, dateView);
        return convertView;
    }


    public void buildArray(int month, int year) {
        visibleMonth = month;
        visibleYear = year;

        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);

        dates.clear();
        startPosition = 7; //minumum starting position is Monday after day row.

//        for (LinearLayout ll : containers.values()) {
//            ll.setBackgroundColor(Color.TRANSPARENT);
//        }

        //add day names for first row
        for (String day : days) {
            dates.add(day);
        }

        //find first day of month
        calendar.set(Calendar.DAY_OF_MONTH,1);
        int firstDay = calendar.get(Calendar.DAY_OF_WEEK);


        int emptySpaces = (firstDay - calendar.getFirstDayOfWeek());
        if (emptySpaces < 0) emptySpaces = 7 - Math.abs(emptySpaces);

        for (int i = 0 ; i < emptySpaces; i++) {
            dates.add("");
            startPosition++;
        }
        for (int i = calendar.get(Calendar.DAY_OF_MONTH); i <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
            dates.add("" + i);
        }
    }

    public void setDate(Calendar calendar) {
        buildArray(calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR));
        notifyDataSetChanged();
    }

    public void nextMonth() {
        if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER) {
            buildArray(Calendar.JANUARY, calendar.get(Calendar.YEAR) + 1);
        }
        else {
            buildArray(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
        }
        notifyDataSetChanged();
    }

    public void previousMonth() {
        if (calendar.get(Calendar.MONTH) == Calendar.JANUARY) {
            buildArray(Calendar.DECEMBER, calendar.get(Calendar.YEAR) - 1);
        }
        else {
            buildArray(calendar.get(Calendar.MONTH) - 1, calendar.get(Calendar.YEAR));
        }
        notifyDataSetChanged();
    }

    public String getMonthString() {
        return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, locale);
    }

    public String getYearString() {
        return "" + calendar.get(Calendar.YEAR);
    }

    public int getYear() {
        return visibleYear;
    }

    public int getMonth() {
        return visibleMonth;
    }

    public int getStartPosition() {
        return startPosition;
    }
}
