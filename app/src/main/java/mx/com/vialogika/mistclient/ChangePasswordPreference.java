package mx.com.vialogika.mistclient;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import mx.com.vialogika.mistclient.Utils.CryptoHash;
import mx.com.vialogika.mistclient.Utils.NetworkRequest;
import mx.com.vialogika.mistclient.Utils.NetworkRequestCallbacks;

public class ChangePasswordPreference extends DialogPreference {

    private EditText cPassword;
    private EditText nPassword;
    private EditText cNPassword;
    private TextView errorView;
    private String   hPass;
    private int      userId;
    private boolean  oldPassMatch = false;
    private boolean  nPassMatch = false;
    private Button   positiveButton;

    public ChangePasswordPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.pwd_layout);
        setNegativeButtonText(android.R.string.cancel);
        setDialogIcon(R.drawable.ic_stat_dhl_icon);
        userId = User.userId(context);
        getUserHP();
    }

    @Override
    protected View onCreateDialogView() {
        View rootview = super.onCreateDialogView();
        getItems(rootview);
        return rootview;
    }



    private void getItems(View rootView){
        cPassword = rootView.findViewById(R.id.cPass);
        cPassword.setOnFocusChangeListener(changeListener);
        nPassword = rootView.findViewById(R.id.npass);
        cNPassword = rootView.findViewById(R.id.cnpass);
        cNPassword.setOnFocusChangeListener(changeListener);
        errorView = rootView.findViewById(R.id.dError);
    }

    private void disablePositive(){
        positiveButton.setEnabled(false);
    }

    private void enablePositive(){
        positiveButton.setEnabled(true);
    }

    private void getUserHP(){
        NetworkRequest.getUserHashedpassword(userId, getContext(), new NetworkRequestCallbacks() {
            @Override
            public void onNetworkRequestResponse(Object response) {
                if (response != null){
                    JSONObject r = (JSONObject) response;
                    try{
                        if (r.getBoolean("success")){
                            hPass = r.getString("hp");
                        }else{
                            Toast.makeText(getContext(), "No es posible cambiar la cotraseña.", Toast.LENGTH_SHORT).show();
                        }
                    }catch(JSONException e){
                        e.printStackTrace();
                    }

                }
            }
            @Override
            public void onNetworkRequestError(VolleyError error) {

            }
        });
    }

    private View.OnFocusChangeListener changeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()){
                case R.id.cPass:
                    if (!hasFocus){
                        checkOldPasswordMatch();
                    }
                    break;
                case R.id.cnpass:
                    if (!hasFocus){
                        checkNPasswordsMatch();
                    }
            }
        }
    };




    private void checkOldPasswordMatch(){
        if (!cPasswordMatch()){
            errorView.setText(R.string.passwords_doesntmatch);
            new CountDownTimer(5000,1000){
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    errorView.setText("");
                }
            }.start();
        }else{
            errorView.setText(R.string.dsc_text_ok);
            oldPassMatch = true;
            new CountDownTimer(5000,1000){
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    errorView.setText("");
                }
            }.start();
        }
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        checkNPasswordsMatch();
        if (positiveResult){
            if (oldPassMatch && nPassMatch){
                saveNewPassword();
            }else{
                Toast.makeText(getContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveNewPassword(){
        int uid = User.userId(getContext());
        String pwHash = CryptoHash.sha1(nPassword.getText().toString());
        NetworkRequest.saveNewUserPassword(getContext(), uid, pwHash, new NetworkRequestCallbacks() {
            @Override
            public void onNetworkRequestResponse(Object response) {
                JSONObject r = (JSONObject) response;
                try{
                    if (r.getBoolean("success")){
                        Toast.makeText(getContext(), "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(),DscMainActivity.class);
                        intent.putExtra("cleanAndExit",true);
                        getContext().startActivity(intent);
                    }  
                }catch(JSONException e){
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNetworkRequestError(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkNPasswordsMatch(){
        if (!nPasswordMatch()){
            nPassMatch = false;
        }else{
            nPassMatch = true;
        }
    }

    private String getCPasswordText(){
        return cPassword.getText().toString();
    }

    private boolean cPasswordMatch(){
        String cp = getCPasswordText();
        String userInput = CryptoHash.sha1(cp);
        return userInput.equals(hPass);
    }

    private boolean nPasswordMatch(){
        String npass1 = nPassword.getText().toString();
        String npass2 = cNPassword.getText().toString();
        return npass1.equals(npass2);
    }
}
