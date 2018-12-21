package mx.com.vialogika.mistclient.Adapters;

import android.support.annotation.NonNull;
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

public class EdoGuardAdapter extends RecyclerView.Adapter<EdoGuardAdapter.EdoGuardViewHolder> {

    private List<GuardForceState> dataset;

    public static class EdoGuardViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView elementName, apostamiento, tiempo, obs;
        ImageView guardPhoto;

        public EdoGuardViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.guardedoview);
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
        GuardForceState item = dataset.get(i);
        edoGuardViewHolder.elementName.setText(item.getGuardName());
        edoGuardViewHolder.apostamiento.setText(item.getApName());
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
