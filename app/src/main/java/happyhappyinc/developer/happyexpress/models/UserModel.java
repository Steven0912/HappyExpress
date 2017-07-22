package happyhappyinc.developer.happyexpress.models;

/**
 * Created by Steven on 04/07/2017.
 */

// Modelo que contiene toda la información de un usuario
public class UserModel {
    private int id, id_doc, id_termino, id_estado, id_roles;
    private String nombre_completo, nombre, apellido, genero, telefono, correo, password, numero_doc, foto_perfil, direccion, latitud, longitud, fecha, imagen_pin, pin, token;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_doc() {
        return id_doc;
    }

    public void setId_doc(int id_doc) {
        this.id_doc = id_doc;
    }

    public int getId_termino() {
        return id_termino;
    }

    public void setId_termino(int id_termino) {
        this.id_termino = id_termino;
    }

    public int getId_estado() {
        return id_estado;
    }

    public void setId_estado(int id_estado) {
        this.id_estado = id_estado;
    }

    public int getId_roles() {
        return id_roles;
    }

    public void setId_roles(int id_roles) {
        this.id_roles = id_roles;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumero_doc() {
        return numero_doc;
    }

    public void setNumero_doc(String numero_doc) {
        this.numero_doc = numero_doc;
    }

    public String getFoto_perfil() {
        return foto_perfil;
    }

    public void setFoto_perfil(String foto_perfil) {
        this.foto_perfil = foto_perfil;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getImagen_pin() {
        return imagen_pin;
    }

    public void setImagen_pin(String imagen_pin) {
        this.imagen_pin = imagen_pin;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
