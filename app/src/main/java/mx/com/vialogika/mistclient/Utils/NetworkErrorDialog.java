package mx.com.vialogika.mistclient.Utils;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

public class NetworkErrorDialog extends MaterialDialog.Builder {

    private VolleyError error;

    public NetworkErrorDialog(Context context, VolleyError error){
        super(context);
        this.error = error;
        setContent();
        show();
    }

    private void setContent(){
        if (error instanceof TimeoutError || error instanceof NoConnectionError){
            title("Sin conexion");
            content("Se ha agotado el tiempo de espera de conexion con el servidor");
            positiveText("OK");
        }
        if (error instanceof ServerError){
            title("Error de servidor");
            content("Ha ocurrido un error de servidor favor de reportarlo lo antes posible");
            positiveText("OK");
        }
        if (error instanceof NetworkError){
            title("Problema de red");
            content("Ha habido un problema de red, favor de intentar mas tarde");
            positiveText("OK");
        }
        if (error instanceof ParseError){
            title("Parse error");
            content("La respuesta a la solicitud no es valida favor de reportar el problema lo antes posible");
            positiveText("OK");
        }
    }
}
