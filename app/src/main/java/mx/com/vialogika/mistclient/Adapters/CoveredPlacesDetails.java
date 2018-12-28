package mx.com.vialogika.mistclient.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mx.com.vialogika.mistclient.R;
import mx.com.vialogika.mistclient.Utils.APDetailsDataHolder;

public class CoveredPlacesDetails extends RecyclerView.Adapter<CoveredPlacesDetails.CoveredPlacesViewHolder> {

    private List<APDetailsDataHolder> mDataset;

    public CoveredPlacesDetails(List<APDetailsDataHolder> data){
        this.mDataset = data;
    }

    public static class CoveredPlacesViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView apostamientoname,percent,required,covered;

        CoveredPlacesViewHolder(View itemView){
            super(itemView);
            cv = itemView.findViewById(R.id.mcardview);
            apostamientoname = itemView.findViewById(R.id.apname);
            percent = itemView.findViewById(R.id.coveredpercent);
            required = itemView.findViewById(R.id.guardrequiredcount);
            covered = itemView.findViewById(R.id.guardcoveredcount);
        }
    }

    @NonNull
    @Override
    public CoveredPlacesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ap_assignment_details_view,viewGroup,false);
        return new CoveredPlacesViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull CoveredPlacesViewHolder holder, int position) {
        APDetailsDataHolder current = mDataset.get(position);
        holder.percent.setText(current.getPercent());
        holder.required.setText(String.valueOf(current.getGuardsRequired()));
        holder.covered.setText(String.valueOf(current.getGuardsCovered()));
        holder.apostamientoname.setText(current.getApostamientoName());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
