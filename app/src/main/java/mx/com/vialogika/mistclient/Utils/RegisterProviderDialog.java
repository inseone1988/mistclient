package mx.com.vialogika.mistclient.Utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;

import mx.com.vialogika.mistclient.R;

public class RegisterProviderDialog extends MaterialDialog.Builder {

    public RegisterProviderDialog(Context context){
        super(context);
        init();
    }

    private void init(){
        title(R.string.new_provider);
        customView(R.layout.new_provider_dialog,true);
        positiveText(R.string.dsc_save);
        negativeText(R.string.cancel);
    }

    @Override
    public MaterialDialog.Builder onPositive(@NonNull MaterialDialog.SingleButtonCallback callback) {
        return super.onPositive(callback);
    }
}
