package mx.com.vialogika.mistclient.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import mx.com.vialogika.mistclient.ReportView;

public class LoadImages extends AsyncTask<String,Void,Bitmap> {

    private LoadImagesCallback callback;
    private int position;

    public LoadImages(LoadImagesCallback cb,int mPosition){
        this.callback = cb;
        this.position = mPosition;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        callback.onImagesLoaded(bitmap,position);
        super.onPostExecute(bitmap);
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bitmap = null;
        try{
            URL url = new URL(strings[0]);
            bitmap = BitmapFactory.decodeStream((InputStream)url.getContent());
        }catch (IOException e){
            e.printStackTrace();
        }
        position += 1;
        return bitmap;
    }
}
