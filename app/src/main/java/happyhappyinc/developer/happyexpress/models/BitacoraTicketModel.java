package happyhappyinc.developer.happyexpress.models;

/**
 * Created by Steven on 22/07/2017.
 */

public class BitacoraTicketModel {
    private int id_bitacora_ticket, id_ticket, id_usuario_que_envia_mensaje, id_estado;
    private String fecha, mensaje;

    public int getId_bitacora_ticket() {
        return id_bitacora_ticket;
    }

    public void setId_bitacora_ticket(int id_bitacora_ticket) {
        this.id_bitacora_ticket = id_bitacora_ticket;
    }

    public int getId_ticket() {
        return id_ticket;
    }

    public void setId_ticket(int id_ticket) {
        this.id_ticket = id_ticket;
    }

    public int getId_usuario_que_envia_mensaje() {
        return id_usuario_que_envia_mensaje;
    }

    public void setId_usuario_que_envia_mensaje(int id_usuario_que_envia_mensaje) {
        this.id_usuario_que_envia_mensaje = id_usuario_que_envia_mensaje;
    }

    public int getId_estado() {
        return id_estado;
    }

    public void setId_estado(int id_estado) {
        this.id_estado = id_estado;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
