package mx.com.vialogika.mistclient.Utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;

public class DownloadProfileImagesDialog extends MaterialDialog.Builder {

    public DownloadProfileImagesDialog(@NonNull Context context) {
        super(context);
        init();
    }

    private void init(){
        this.title = "Descargar Imagenes";
        this.content = "¿Descargar imagenes de perfil de guardias?";
        this.positiveText = "OK";
    }


}
