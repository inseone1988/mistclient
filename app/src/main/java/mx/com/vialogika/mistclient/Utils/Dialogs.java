package mx.com.vialogika.mistclient.Utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

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

    public static void networkErrorDialog(Context context){
        new MaterialDialog.Builder(context)
                .title(R.string.networ_error_title)
                .content(R.string.network_error_advice)
                .positiveText(R.string.dsc_text_ok)
                .show();
    }

    public static void timeoutError(Context context){
        new MaterialDialog.Builder(context)
                .title(R.string.network_timeout_title)
                .content(R.string.network_timeout_advice)
                .positiveText(R.string.dsc_text_ok)
                .show();
    }

    public static void invalidServerResponse(Context context, String statuscode){
        Resources res = context.getResources();
        String message = String.format(res.getString(R.string.server_response_error),statuscode);
        new MaterialDialog.Builder(context)
                .title(R.string.network_response_error_title)
                .content(message)
                .positiveText(R.string.dsc_text_ok)
                .show();
    }

    public static void authFailedDialog(Context context){
        new MaterialDialog.Builder(context)
                .title(R.string.network_response_error_title)
                .content(R.string.failed_login_advice)
                .positiveText(R.string.dsc_text_ok)
                .show();
    }

    public static void exitAppDialog(Context context,final SimpleDialogCallback cb){
        new MaterialDialog.Builder(context)
                .title(R.string.leave_app)
                .content(R.string.leave_app_advice)
                .positiveText(R.string.dsc_text_ok)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        cb.AccountStayOpen();
                    }
                })
                .show();
    }

    public static MaterialDialog reportFilterDialog(Context context){
       return new MaterialDialog.Builder(context)
                .title(R.string.filter_reports)
                .customView(R.layout.reports_filter_layout,true)
                .positiveText(R.string.dsc_text_ok)
                .negativeText(R.string.cancel)
               .build();
    }

    public static MaterialDialog reportflagDialog(Context context,final GenericDialogCallback cb){
        return new MaterialDialog.Builder(context)
                .title(R.string.report_flag_title)
                .items(R.array.flag_options)
                .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        cb.onActionDone(which);
                        return false;
                    }
                })
                .positiveText(R.string.dsc_text_ok)
                .negativeText(R.string.cancel)
                .show();
    }

    public interface GenericDialogCallback{
        void onActionDone(@Nullable Object params);
    }

}
