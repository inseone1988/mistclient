package mx.com.vialogika.mistclient.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import mx.com.vialogika.mistclient.Client;
import mx.com.vialogika.mistclient.R;
import mx.com.vialogika.mistclient.Utils.DeleteDialog;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {

    private List<Client> dataset;

    public ClientAdapter(List<Client> clients){
        this.dataset = clients;
    }

    public static class ClientViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView clientName,clientAlias,clientSocial;
        ImageView delete;

        public ClientViewHolder(View itemView){
            super(itemView);
            //TODO:Load card Items
            cv = itemView.findViewById(R.id.client_cv);
            clientName = itemView.findViewById(R.id.clientname);
            clientSocial = itemView.findViewById(R.id.clientsocial);
            delete = itemView.findViewById(R.id.deleteclient);
        }
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.client_view,viewGroup,false);
        return new ClientViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull ClientViewHolder clientViewHolder, int i) {
        Resources res = clientViewHolder.cv.getContext().getResources();
        Client client = dataset.get(i);
        String clientName = res.getString(R.string.client_key);
        String social = res.getString(R.string.client_social);
        clientViewHolder.clientName.setText(String.format(clientName,client.getClientName()));
        clientViewHolder.clientSocial.setText(String.format(social,client.getClientSocial()));
        clientViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DeleteDialog(view.getContext(), DeleteDialog.DELETE_CLIENT, new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Toast.makeText(dialog.getContext().getApplicationContext(), "Cliente deleted", Toast.LENGTH_SHORT).show();
                    }
                }, new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
