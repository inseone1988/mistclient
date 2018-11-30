package mx.com.vialogika.mistclient.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mx.com.vialogika.mistclient.Utils.Provider;

public class ProviderAdapter extends RecyclerView.Adapter<ProviderAdapter.ProvViewHolder> {

    private List<Provider> dataset;

    public static class ProvViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView tv;

        ProvViewHolder(View itemView){
            super(itemView);
            //Todo:load cardview items
        }
    }

    @NonNull
    @Override
    public ProvViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
       // View rootview = LayoutInflater.from(viewGroup.getContext()).inflate()
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ProvViewHolder provViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
