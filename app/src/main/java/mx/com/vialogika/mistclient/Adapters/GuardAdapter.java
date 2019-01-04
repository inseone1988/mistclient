package mx.com.vialogika.mistclient.Adapters;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mx.com.vialogika.mistclient.Guard;
import mx.com.vialogika.mistclient.R;
import mx.com.vialogika.mistclient.Utils.GuardDetailsDialog;
import mx.com.vialogika.mistclient.Utils.Provider;

public class GuardAdapter extends RecyclerView.Adapter<GuardAdapter.GuardViewHolder> {

    List<Guard> dataset;
    List<Provider> providers;


    public GuardAdapter(List<Guard> mdataset){
        this.dataset = mdataset;
    }

    public static class GuardViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView guardname,guardposition,active,gCreated,gProvider;
        ImageView guardPhoto,guardmenu;

        GuardViewHolder(View itemView){
            super(itemView);
            cv = itemView.findViewById(R.id.mcardview);
            guardname = itemView.findViewById(R.id.gname);
            guardposition = itemView.findViewById(R.id.guardposition);
            active  = itemView.findViewById(R.id.guardstatus);
            guardPhoto = itemView.findViewById(R.id.profile_image);
            guardmenu = itemView.findViewById(R.id.menu_kebab);
            gCreated = itemView.findViewById(R.id.alta);
        }
    }

    @NonNull
    @Override
    public GuardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootVieew = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.guard_view,viewGroup,false);
        return new GuardViewHolder(rootVieew);
    }

    @Override
    public void onBindViewHolder(@NonNull GuardViewHolder guardViewHolder, int i) {
        final Context context = guardViewHolder.guardPhoto.getContext();
        final Guard   current = dataset.get(i);
        guardViewHolder.guardname.setText(current.getGuardFname());
        guardViewHolder.guardposition.setText(current.getPersonPosition());
        guardViewHolder.active.setText(activeGuardText(current.isActive()));
        guardViewHolder.active.setBackgroundResource(setGuardActiveColor(current.isActive()));
        guardViewHolder.gCreated.setText(String.format(context.getResources().getString(R.string.guard_created),current.getPersonCreated()));
        guardViewHolder.guardmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuardDetailsDialog dialog = new GuardDetailsDialog(context);
                dialog.setGuard(current);
                dialog.show();
            }
        });
    }

    private String activeGuardText(boolean isActive){
        if (isActive){
            return "Activo";
        }else{
            return "Baja";
        }
    }

    private int setGuardActiveColor(boolean isActive){
        if (isActive){
            return R.color.active;
        }else{
            return R.color.inactive;
        }
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
