package mx.com.vialogika.mistclient.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
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
import com.android.volley.VolleyError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import mx.com.vialogika.mistclient.Apostamiento;
import mx.com.vialogika.mistclient.R;
import mx.com.vialogika.mistclient.Utils.DeleteDialog;
import mx.com.vialogika.mistclient.Utils.EditElementDialog;
import mx.com.vialogika.mistclient.Utils.NetworkRequest;
import mx.com.vialogika.mistclient.Utils.NetworkRequestCallbacks;

public class ApostamientoAdapter extends RecyclerView.Adapter<ApostamientoAdapter.ApViewHolder> {


    public static final int IS_VALID        = 0;
    public static final int ABOUT_TO_EXPIRE = 1;
    public static final int EXPIRED         = 2;

    private FragmentManager fragmentManager;

    private List<Apostamiento> daataset;

    public static class ApViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView apname,apalias,apclient,apcreted,aptype;
        ImageView delete,edit;
        TextView consignationExpdate,consignationValidity;

        ApViewHolder(View itemView){
            super(itemView);
            cv = itemView.findViewById(R.id.ap_cv);
            apname = itemView.findViewById(R.id.apname);
            apalias = itemView.findViewById(R.id.ap_key);
            apclient = itemView.findViewById(R.id.ap_client);
            aptype = itemView.findViewById(R.id.ap_type);
            delete = itemView.findViewById(R.id.apdletebtn);
            edit = itemView.findViewById(R.id.apeditbtn);
            consignationExpdate = itemView.findViewById(R.id.cons_ex_date);
            consignationValidity = itemView.findViewById(R.id.cons_sem);
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
    public void onBindViewHolder(@NonNull final ApViewHolder apViewHolder, final int i) {
        Resources          res          = apViewHolder.cv.getContext().getResources();
        final String             place        = res.getString(R.string.ap_place);
        final String             clientString = res.getString(R.string.ap_client);
        final String             clientType   = res.getString(R.string.ap_type);
        final String             ap_clave     = res.getString(R.string.ap_key_name);
        final Apostamiento current      = daataset.get(i);
        apViewHolder.apalias.setText(String.format(ap_clave,current.getPlantillaPlaceApostamientoAlias()));
        apViewHolder.apname.setText(String.format(place,current.getPlantillaPlaceApostamientoName()));
        apViewHolder.apclient.setText(String.format(clientString,current.getClientName()));
        apViewHolder.aptype.setText(String.format(clientType,current.getPlantillaPlaceType()));
        apViewHolder.consignationExpdate.setText(current.getPlantillaPlaceConsExp());
        apViewHolder.consignationValidity.setBackgroundColor(res.getColor(checkValidConsigna(current.getPlantillaPlaceConsExp())));
        apViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new DeleteDialog(view.getContext(), DeleteDialog.DELETE_APOSTAMIENTO, new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        NetworkRequest.deleteApostamiento(view.getContext(), current.getPlantillaPlaceId(), new NetworkRequestCallbacks() {
                            @Override
                            public void onNetworkRequestResponse(Object response) {
                                apDeleted(i);
                                Toast.makeText(view.getContext(), "Apostamiento borrado", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onNetworkRequestError(VolleyError error) {

                            }
                        });

                    }
                }, new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                });
            }
        });
        apViewHolder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
               EditElementDialog dialog = new EditElementDialog(v.getContext(),EditElementDialog.EDIT_APOSTAMIENTO);
               dialog.setFragmentManager(getFragmentManager());
               dialog.setAp(current);
               dialog.setCb(new EditElementDialog.callbacks() {
                   @Override
                   public void onSaved(Apostamiento apostamiento) {
                       Toast.makeText(v.getContext().getApplicationContext(), "Apostamiento guardado exitosamente", Toast.LENGTH_SHORT).show();
                       apViewHolder.apalias.setText(String.format(ap_clave,apostamiento.getPlantillaPlaceApostamientoAlias()));
                       apViewHolder.apname.setText(String.format(place,apostamiento.getPlantillaPlaceApostamientoName()));
                       apViewHolder.apclient.setText(String.format(clientString,apostamiento.getClientName()));
                       apViewHolder.aptype.setText(String.format(clientType,apostamiento.getPlantillaPlaceType()));
                   }
               });
               dialog.show();
            }
        });


    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    private int checkValidConsigna(String dueDate) {
        if (dueDate != null){
            try {
                Date mDueDate = new SimpleDateFormat("yyyy-MM-dd").parse(dueDate);
                switch(checkValidity(mDueDate)){
                    case EXPIRED:
                        return R.color.dsc_danger;
                    case ABOUT_TO_EXPIRE :
                        return R.color.dsc_warning;
                    case IS_VALID :
                        return R.color.dsc_success;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return R.color.dsc_danger;
    }

    private int checkValidity(Date duedate) {
        Date today = new Date();
        if (today.before(duedate)){
            long difference = duedate.getTime() - today.getTime();
            float daysBetween = (difference / (1000*60*60*24));
            if (daysBetween < 90){
                return ABOUT_TO_EXPIRE;
            }
            if (daysBetween > 90){
                return IS_VALID;
            }
        }
        return EXPIRED;
    }

    public void apDeleted(int position){
        daataset.remove(position);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return daataset.size();
    }
}
