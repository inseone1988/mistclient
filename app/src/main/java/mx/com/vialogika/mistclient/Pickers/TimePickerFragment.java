package mx.com.vialogika.mistclient.Pickers;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.app.TimePickerDialog;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private final Calendar c = Calendar.getInstance();
    private TimePicker cb;

    public void setCb(TimePicker cb) {
        this.cb = cb;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(),this,hour,minute,false);
    }

    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        c.set(Calendar.HOUR,hourOfDay);
        c.set(Calendar.MINUTE,minute);
        SimpleDateFormat tf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        cb.onTimeSet(tf.format(c.getTime()));
    }

    public interface TimePicker{
        void onTimeSet(String time);
    }
}
