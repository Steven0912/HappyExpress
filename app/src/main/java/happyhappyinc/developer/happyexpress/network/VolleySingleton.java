package happyhappyinc.developer.happyexpress.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Steven on 06/07/2017.
 */

public class VolleySingleton {
    // Atributos
    private static VolleySingleton singleton;
    private RequestQueue requestQueue;
    private static Context context;


    private VolleySingleton(Context context) {
        VolleySingleton.context = context;
        requestQueue = getRequestQueue();
    }

    /**
     * Retorna la instancia unica del singleton
     *
     * @param context contexto donde se ejecutarán las peticiones
     * @return Instancia
     */
    // Controlamos que solo sea una instancia global para trabajar con un solo objeto
    public static synchronized VolleySingleton getInstance(Context context) {
        if (singleton == null) {
            singleton = new VolleySingleton(context.getApplicationContext());
        }
        return singleton;
    }

    /**
     * Obtiene la instancia de la cola de peticiones
     *
     * @return cola de peticiones
     */
    // Inicializamos la pila que va a contener los datos retornados
    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * Añade la petición a la cola
     *
     * @param req petición
     * @param <T> Resultado final de tipo T
     */
    // Pila que se alimenta de acuerdo a la cantidad de datos que retorne el servicio
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
