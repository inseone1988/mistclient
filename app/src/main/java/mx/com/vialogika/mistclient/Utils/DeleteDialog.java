package mx.com.vialogika.mistclient.Utils;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;

public class DeleteDialog extends MaterialDialog.Builder {

    public final static String DELETE_GUARD = "Guardia";
    public final static String DELETE_APOSTAMIENTO = "Apostamiento";
    public final static String DELETE_CLIENT = "Cliente";
    public final static String DELETE_PROVIDER = "Proveedor";

    private String operationType;

    public DeleteDialog(Context context, String type, MaterialDialog.SingleButtonCallback positive, MaterialDialog.SingleButtonCallback negative){
        super(context);
        this.operationType = type;
        onPositiveCallback = positive;
        onNegativeCallback = negative;
        deleteItem();
        show();
    }

    private void deleteItem(){
        switch(operationType){
            case DELETE_GUARD:
                this.title("Dar de baja oficial");
                this.content("Estas a punto de borrar a un guardia esta operacion se guardara como baja del oficial. ¿Continuar?");
                break;
            case DELETE_APOSTAMIENTO:
                this.title("Eliminar apostamiento");
                this.content("Esta operacion eliminara el apostamiento para volverlo a usar tendra que agregar un nuevo apostamiento");
                break;
            case DELETE_CLIENT:
                this.title("Eliminar cliente");
                this.content("Esta operacion no puede deshacerse, tendra que dar de alta un nuevo clinte si desa volver a utilizarlo");
                break;
            case DELETE_PROVIDER:
                this.title("Eliminar proveedor");
                this.content("Esta accion solo lo removera de la base datos local, para volver a usarlo solo agregelo desde la pantalla de proveedores");
                break;
        }
        this.positiveText("OK");
        this.negativeText("Cancelar");
    }


}
