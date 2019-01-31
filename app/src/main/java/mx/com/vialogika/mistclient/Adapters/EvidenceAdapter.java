package mx.com.vialogika.mistclient.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mx.com.vialogika.mistclient.R;

public class EvidenceAdapter extends RecyclerView.Adapter<EvidenceAdapter.EvidenceViewHolder> {

    private List<String> dataset;

    public EvidenceAdapter(List<String> evidencePaths) {
        this.dataset = evidencePaths;
    }

    public static class EvidenceViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        ImageView thumbnail;
        TextView path;
        public EvidenceViewHolder(View itemView){
            super(itemView);
            cv = itemView.findViewById(R.id.ev_cv);
            thumbnail = itemView.findViewById(R.id.evthumbnail);
            path = itemView.findViewById(R.id.ev_path);
        }
    }

    @NonNull
    @Override
    public EvidenceViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.evidence_viewer,viewGroup,false);
        return new EvidenceViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull EvidenceViewHolder evidenceViewHolder, int i) {
        Context   context = evidenceViewHolder.cv.getContext();
        Bitmap    thumb = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(dataset.get(i)),100,100);
        evidenceViewHolder.thumbnail.setImageBitmap(thumb);
        evidenceViewHolder.path.setText(dataset.get(i));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}
