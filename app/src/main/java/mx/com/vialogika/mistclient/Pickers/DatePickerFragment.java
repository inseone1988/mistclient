package mx.com.vialogika.mistclient.Pickers;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.app.DatePickerDialog;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private final Calendar c = Calendar.getInstance();
    private DatePicker cb;

    public void setCallback(DatePicker cb){
        this.cb = cb;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(),this,year,month,date);
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        c.set(year,month,dayOfMonth);
        Date from = c.getTime();
        c.add(Calendar.DAY_OF_MONTH,1);
        Date to = c.getTime();
        cb.onDateSet(from,to);
    }

    public interface DatePicker{
        void onDateSet(Date from, Date to);
    }
}
