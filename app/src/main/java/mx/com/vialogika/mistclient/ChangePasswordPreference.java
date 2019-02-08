package mx.com.vialogika.mistclient;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

public class ChangePasswordPreference extends DialogPreference {

    public ChangePasswordPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.pwd_layout);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(R.drawable.ic_stat_dhl_icon);
    }
}
