package mx.com.vialogika.mistclient.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mx.com.vialogika.mistclient.GuardForceState;
import mx.com.vialogika.mistclient.R;
import mx.com.vialogika.mistclient.Room.DatabaseOperations;

public class EdoGuardAdapter extends RecyclerView.Adapter<EdoGuardAdapter.EdoGuardViewHolder> {

    private List<GuardForceState> dataset;

    public static class EdoGuardViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView elementName, apostamiento, tiempo, obs;
        ImageView guardPhoto;

        public EdoGuardViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.guardedoview);
            guardPhoto = itemView.findViewById(R.id.guardProfilePhoto);
            elementName = itemView.findViewById(R.id.elementname);
            apostamiento = itemView.findViewById(R.id.elementapostamiento);
            tiempo = itemView.findViewById(R.id.elementtiempo);
            obs = itemView.findViewById(R.id.elementobs);
        }
    }

    public EdoGuardAdapter(List<GuardForceState> stateList) {
        this.dataset = stateList;
    }

    @NonNull
    @Override
    public EdoGuardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.guard_edo_view,viewGroup,false);
        return new EdoGuardViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull EdoGuardViewHolder edoGuardViewHolder, int i) {
        Context context = edoGuardViewHolder.elementName.getContext();
        Resources res = context.getResources();
        GuardForceState item = dataset.get(i);
        edoGuardViewHolder.guardPhoto.setImageDrawable(res.getDrawable(R.drawable.ic_guard));
        if (item.getGuardPhotoPath() != null){
            if (!item.getGuardPhotoPath().equals("null")){
                if(!item.getGuardPhotoPath().equals("")){
                    getGuardPhoto(item.getGuardPhotoPath(),edoGuardViewHolder.guardPhoto);
                }
            }
        }
        String obs  = item.getProviderIncidencesType().equals("null") ? "Sin incidencias" : String.format(res.getString(R.string.obs_guard),item.getProviderIncidencesType());
        edoGuardViewHolder.elementName.setText(item.getGuardName());
        edoGuardViewHolder.apostamiento.setText(item.getApName());
        edoGuardViewHolder.tiempo.setText(item.reportHour());
        edoGuardViewHolder.obs.setText(obs);
    }

    private void getGuardPhoto(final String path,final ImageView imageView){
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Bitmap profilePhoto = BitmapFactory.decodeFile(path);
                if(profilePhoto != null){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                        imageView.setImageBitmap(profilePhoto);
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
