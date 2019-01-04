package mx.com.vialogika.mistclient.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import mx.com.vialogika.mistclient.Guard;
import mx.com.vialogika.mistclient.R;

public class GuardDetailsDialog extends MaterialDialog.Builder {


    private Guard guard;
    private TextView guardName,guardAlta,guardPosition;
    private ImageView guardphoto;

    public GuardDetailsDialog(@NonNull Context context) {
        super(context);
        customView(R.layout.guard_details_view,true);
        getItems();
        setListeners();
    }

    private void getItems(){
        MaterialDialog dialog = this.build();
        View rootview = dialog.getCustomView();
        guardphoto = rootview.findViewById(R.id.guard_details_photo);
        guardName = rootview.findViewById(R.id.guard_name_placeholder);
        guardAlta = rootview.findViewById(R.id.guard_creted_placeholder);
        guardPosition = rootview.findViewById(R.id.guard_psition_placeholder);
        if (guard != null){
            getGuardPhoto();
        }
    }

    private void setListeners(){
        guardphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (guard != null){
                    getGuardPhoto();
                }
            }
        });
    }

    private void init(){

    }

    private void setValues(){
        Resources res = getContext().getResources();
        guardName.setText(String.format(res.getString(R.string.element_name),guard.getGuardFullname()));
        guardAlta.setText(String.format(res.getString(R.string.element_created),guard.getPersonCreated()));
        guardPosition.setText(String.format(res.getString(R.string.element_position),guard.getPersonPosition()));
    }

    private void getGuardPhoto(){
        String url = NetworkRequest.SERVER_URL_PHOTO_PREFIX + guard.getGuardPhotoPath();
        NetworkRequest.getImageFromURL(url, false, new NetworkRequest.NetworkBitmap() {
            @Override
            public void downloaded(Bitmap guardImage) {
                if (guardImage != null){
                    guardphoto.setImageBitmap(guardImage);
                }
            }
        });

    }

    public Guard getGuard() {
        return guard;
    }

    public void setGuard(Guard guard) {
        this.guard = guard;
        setValues();
    }
}
