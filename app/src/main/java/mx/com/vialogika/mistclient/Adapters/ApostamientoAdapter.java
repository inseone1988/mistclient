package mx.com.vialogika.mistclient.Adapters;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mx.com.vialogika.mistclient.Apostamiento;
import mx.com.vialogika.mistclient.R;

public class ApostamientoAdapter extends RecyclerView.Adapter<ApostamientoAdapter.ApViewHolder> {

    private List<Apostamiento> daataset;

    public static class ApViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView apname,apalias,apclient,apcreted,aptype;

        ApViewHolder(View itemView){
            super(itemView);
            cv = itemView.findViewById(R.id.ap_cv);
            apname = itemView.findViewById(R.id.apname);
            apalias = itemView.findViewById(R.id.ap_key);
            apclient = itemView.findViewById(R.id.ap_client);
            aptype = itemView.findViewById(R.id.ap_type);
        }

    }

    public ApostamientoAdapter(List<Apostamiento> apts){
        this.daataset = apts;
    }

    @NonNull
    @Override
    public ApViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.apostamiento_view,viewGroup,false);
        return new ApViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ApViewHolder apViewHolder, int i) {
        Resources res = apViewHolder.cv.getContext().getResources();
        String place = res.getString(R.string.ap_place);
        String clientString = res.getString(R.string.ap_client);
        String clientType = res.getString(R.string.ap_type);
        String ap_clave = res.getString(R.string.ap_key_name);
        Apostamiento current = daataset.get(i);
        apViewHolder.apalias.setText(String.format(ap_clave,current.getPlantillaPlaceApostamientoAlias()));
        apViewHolder.apname.setText(String.format(place,current.getPlantillaPlaceApostamientoName()));
        apViewHolder.apclient.setText(String.format(clientString,current.getClientName()));
        apViewHolder.aptype.setText(String.format(clientType,current.getPlantillaPlaceType()));
    }

    @Override
    public int getItemCount() {
        return daataset.size();
    }
}
