package sistemagestionfinanzas;

import java.util.ArrayList;

public class Cliente {
    private String nombre;
    private String email;
    private String password;
    private String id;
    private ArrayList<FinanceItem> activos;
    private ArrayList<FinanceItem> pasivos;

    // Constructor
    public Cliente(String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.activos = new ArrayList<>();
        this.pasivos = new ArrayList<>();
        // Generar un ID único para el cliente
        this.id = generarIdUnico();
    }

    // Métodos getter y setter
    public void setNombre(String nuevo_nombre) {
        this.nombre = nuevo_nombre;
    }

    public void setEmail(String nuevo_email) {
        this.email = nuevo_email;
    }

    public void setPassword(String nueva_password) {
        this.password = nueva_password;
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPassword() {
        return this.password;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String nuevo_id) {
        this.id = nuevo_id;
    }

    // Métodos relacionados con la gestión de activos y pasivos
    public void obtenerActivos() {
        // Lógica para obtener y mostrar activos
    }

    public void obtenerPasivos() {
        // Lógica para obtener y mostrar pasivos
    }

    public void agregarFinanceItem(FinanceItem item, boolean es_activo) {
        if (es_activo) {
            this.activos.add(item);
        } else {
            this.pasivos.add(item);
        }
    }

    public void eliminarFinanceItem(FinanceItem item, boolean es_activo) {
        if (es_activo) {
            this.activos.remove(item);
        } else {
            this.pasivos.remove(item);
        }
    }

    // Otros métodos
    public void iniciarSesion() {
        // Lógica para iniciar sesión del cliente
    }

    // Método privado para generar un ID único (ejemplo sencillo)
    private String generarIdUnico() {
        // Lógica para generar un ID único (puedes implementar según tus necesidades)
        return "ID_" + this.nombre.hashCode();
    }

    // Método para obtener información general del cliente
    protected StringBuilder obtenerInformacionGeneral() {
        StringBuilder sb = new StringBuilder();
        sb.append("Nombre: ").append(nombre).append("\n");
        sb.append("ID: ").append(id).append("\n");
        sb.append("Email: ").append(email).append("\n");
        sb.append("Número de Activos: ").append(activos.size()).append("\n");
        sb.append("Número de Pasivos: ").append(pasivos.size()).append("\n");
        return sb;
    }

    public void verBalance() {
        double total_activos = activos.stream().mapToDouble(item -> item.getMontoActual()).sum();
        double total_pasivos = pasivos.stream().mapToDouble(item -> item.getMontoActual()).sum();
        double balance = total_activos - total_pasivos;
        System.out.println("Balance total: " + balance);
    }
}
