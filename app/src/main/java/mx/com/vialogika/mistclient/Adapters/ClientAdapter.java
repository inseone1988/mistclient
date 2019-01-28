package mx.com.vialogika.mistclient.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.VolleyError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mx.com.vialogika.mistclient.Client;
import mx.com.vialogika.mistclient.R;
import mx.com.vialogika.mistclient.Utils.DeleteDialog;
import mx.com.vialogika.mistclient.Utils.EditElementDialog;
import mx.com.vialogika.mistclient.Utils.NetworkRequest;
import mx.com.vialogika.mistclient.Utils.NetworkRequestCallbacks;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ClientViewHolder> {

    private List<Client> dataset;

    public ClientAdapter(List<Client> clients) {
        this.dataset = clients;
    }

    public static class ClientViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView clientName, clientAlias, clientSocial,securityResp;
        ImageView delete, clientEdit;

        public ClientViewHolder(View itemView) {
            super(itemView);
            //TODO:Load card Items
            cv = itemView.findViewById(R.id.client_cv);
            clientName = itemView.findViewById(R.id.clientname);
            clientSocial = itemView.findViewById(R.id.clientsocial);
            delete = itemView.findViewById(R.id.deleteclient);
            clientEdit = itemView.findViewById(R.id.client_edit);
            securityResp = itemView.findViewById(R.id.securityResp);
        }
    }

    @NonNull
    @Override
    public ClientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.client_view, viewGroup, false);
        return new ClientViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull final ClientViewHolder clientViewHolder, final int i) {
        Context context = clientViewHolder.cv.getContext();
        Resources    res        = context.getResources();
        final Client client     = dataset.get(i);
        String       clientName = res.getString(R.string.client_key);
        String       social     = res.getString(R.string.client_social);
        String secResp = client.getClientSecurityResponsable().equals("null") ? "N/A" : client.getClientSecurityResponsable();
        clientViewHolder.clientName.setText(String.format(clientName, client.getClientName()));
        clientViewHolder.clientSocial.setText(String.format(social, client.getClientSocial()));
        clientViewHolder.securityResp.setText(secResp);
        clientViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new DeleteDialog(view.getContext(), DeleteDialog.DELETE_CLIENT, new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        NetworkRequest.deleteClient(dialog.getContext().getApplicationContext(), client.getClientId(), new NetworkRequestCallbacks() {
                            @Override
                            public void onNetworkRequestResponse(Object response) {
                                removeClient(i);
                                Log.d("Room", "Deleted client id " + (int) response);
                                Toast.makeText(view.getContext().getApplicationContext(), "Cliente borrado con exito", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNetworkRequestError(VolleyError error) {

                            }
                        });
                        Toast.makeText(dialog.getContext().getApplicationContext(), "Cliente deleted", Toast.LENGTH_SHORT).show();
                    }
                }, new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                });
            }
        });
        clientViewHolder.clientEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditElementDialog dialog = new EditElementDialog(v.getContext(), EditElementDialog.EDIT_CLIENT);
                dialog.setCl(client);
                dialog.setClcb(new EditElementDialog.clientCallback() {
                    @Override
                    public void onSaved(Client rClient) {
                        clientViewHolder.clientName.setText(rClient.getClientName());
                        clientViewHolder.clientSocial.setText(rClient.getClientSocial());
                    }
                });
                dialog.show();
            }
        });
    }

    public void removeClient(int position) {
        dataset.remove(position);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
