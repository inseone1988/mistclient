package mx.com.vialogika.mistclient.Adapters;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mx.com.vialogika.mistclient.R;
import mx.com.vialogika.mistclient.Utils.Provider;

public class ProviderAdapter extends RecyclerView.Adapter<ProviderAdapter.ProvViewHolder> {

    private List<Provider> dataset;

    public static class ProvViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView fletter,provAlias,provSocial,provType;

        ProvViewHolder(View itemView){
            super(itemView);
            //Todo:load cardview items
            cv = itemView.findViewById(R.id.prov_cv);
            fletter = itemView.findViewById(R.id.prov_t);
            provAlias = itemView.findViewById(R.id.provider_alias);
            provSocial = itemView.findViewById(R.id.provider_social);
            provType = itemView.findViewById(R.id.provider_type);
        }
    }

    @NonNull
    @Override
    public ProvViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.provider_cv,viewGroup,false);
        return new ProvViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull ProvViewHolder provViewHolder, int i) {
        Provider provider = dataset.get(i);
        Resources res = provViewHolder.cv.getContext().getResources();
        String phprovAlias = res.getString(R.string.provider_alias);
        String phprovSocial = res.getString((R.string.provider_social));
        String phprovType = res.getString(R.string.provider_type);
        String letter = Character.toString(getFLetter(provider.getProviderAlias()));
        provViewHolder.fletter.setText(letter);
        provViewHolder.provAlias.setText(phprovAlias);
        provViewHolder.provSocial.setText(phprovSocial);
        provViewHolder.provType.setText(phprovType);
    }

    private char getFLetter(String providerName){
        return providerName.charAt(0);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
