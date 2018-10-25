package mx.com.vialogika.mistclient.Utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import mx.com.vialogika.mistclient.R;

public class Dialogs {
    public static void saveAccountDialog(Context context,final SimpleDialogCallback cb){
        new MaterialDialog.Builder(context)
                .title(R.string.sesion_opened_advice_title)
                .content(R.string.sesion_opened_advice)
                .positiveText(R.string.dsc_text_ok)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        cb.AccountStayOpen();
                    }
                })
                .show();
    }
}
