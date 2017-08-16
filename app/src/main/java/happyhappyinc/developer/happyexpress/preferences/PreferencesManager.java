package happyhappyinc.developer.happyexpress.preferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Steven on 06/07/2017.
 */

public class PreferencesManager {
    // variables de control
    private static final String PREF_NAME = "AppDeliveryPref";
    private static final String IS_LOGIN = "isLoggedIn";
    private static final String ID_SESSION = "idUserSession";
    private static final String NAME_SESSION = "nameUserSession";
    private static final String TOKEN_SESSION = "idUserTokenSession";

    private SharedPreferences mShared;
    private SharedPreferences.Editor mEditor;

    // Inicializamos nuestras variables de control
    public PreferencesManager(Context context) {
        mShared = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mShared.edit();
        mEditor.apply();
    }

    // Método que crea una sesión cada que un usuario se loguea correctamente
    public void createLoginSession(String id, String name, String token) {
        mEditor.putBoolean(IS_LOGIN, true);
        mEditor.putString(ID_SESSION, id);
        mEditor.putString(NAME_SESSION, name);
        mEditor.putString(TOKEN_SESSION, token);
        mEditor.apply();
    }

    // Método para validar si el usuario tiene una sesión activa
    public boolean checkLogin() {
        return mShared.getBoolean(IS_LOGIN, false);
    }

    public String getToken() {
        return mShared.getString(TOKEN_SESSION, "null");
    }

    public void setToken(String token) {
        mEditor.putString(TOKEN_SESSION, token);
        mEditor.apply();
    }

    // Método para validar el id del usuario que tiene la sesión actual creada
    public String checkId() {
        return mShared.getString(ID_SESSION, "0");
    }

    // Método para validar el id del usuario que tiene la sesión actual creada
    public String checkName() {
        return mShared.getString(NAME_SESSION, null);
    }

    // Método para destruir la sesión
    public void logout() {
        mEditor.putBoolean(IS_LOGIN, false);
        mEditor.putString(ID_SESSION, "0");
        mEditor.putString(NAME_SESSION, null);
        mEditor.apply();
    }

    // Método para cambiar de actividad y poder destruir la anterior
    public void changeActivity(AppCompatActivity context, Class secondAct) {
        Intent intent = new Intent(context, secondAct);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        context.finish();
    }
}
