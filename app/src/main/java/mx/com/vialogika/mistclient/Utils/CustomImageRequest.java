package mx.com.vialogika.mistclient.Utils;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;

import java.util.HashMap;
import java.util.Map;

public class CustomImageRequest extends ImageRequest {

    private Map<String,String> mParams;

    public CustomImageRequest(String url,String photoPath, Response.Listener<Bitmap> listener, int maxWidth, int maxHeight, ImageView.ScaleType scaleType, Bitmap.Config decodeConfig, @Nullable Response.ErrorListener errorListener) {
        super(url, listener, maxWidth, maxHeight, scaleType, decodeConfig, errorListener);
        mParams = new HashMap<String,String>();
        mParams.put("function","profileImage");
        mParams.put("path",photoPath);
    }

    @Override
    public int getMethod() {
        return Method.POST;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return mParams;
    }
}
