package mx.com.vialogika.mistclient.Utils;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

import mx.com.vialogika.mistclient.Apostamiento;
import mx.com.vialogika.mistclient.Client;

public class EditElementDialog extends MaterialDialog.Builder {

    final public static String EDIT_CLIENT = "Cliente";
    final public static String EDIT_APOSTAMIENTO = "Apostaminto";


    private Apostamiento ap;
    private Client cl;

    public EditElementDialog(Context context){
        super(context);
    }

    private void setTitle(String mode){

    }
}
