package happyhappyinc.developer.happyexpress.utils;

/**
 * Created by Steven on 06/07/2017.
 */

public class Constants {

    public static final int DETAIL_CODE = 100;

    /**
     * Transición Detalle -> Actualización
     */
    public static final int UPDATE_CODE = 101;
    /**
     * Puerto que utilizas para la conexión.
     * Dejalo en blanco si no has configurado esta carácteristica.
     */
    private static final String PUERTO_HOST = ":";
    /**
     * Dirección IP del SERVIDOR
     */
    private static final String IP = "happyhappyinc.com/ws";
    /**
     * URLs del Web Service
     */

    public static final String USERS_LOGIN = "http://" + IP + "/DeliveryWebService/checkLogin";
    public static final String USERS_VALIDATE = "http://" + IP + "/DeliveryWebService/validateUser";
    public static final String ORDERS_LIST = "http://" + IP + "/DeliveryWebService/orders";
    public static final String CHANGE_ORDER_STATE = "http://" + IP + "/DeliveryWebService/orders";
    public static final String VALIDATE_STATE_ORDER = "http://" + IP + "/DeliveryWebService/orders_states";

    /**
     * Clave para el valor extra que representa al identificador de una meta
     */
    public static final String EXTRA_ID = "IDEXTRA";
}
