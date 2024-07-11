package sistemagestionfinanzas;

import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private String nombre;
    private String email;
    private String password;
    private String id;
    private List<FinanceItem> activos;
    private List<FinanceItem> pasivos;

    public Cliente(String nombre, String email, String password) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.activos = new ArrayList<>();
        this.pasivos = new ArrayList<>();
    }

    public void setNombre(String nuevoNombre) {
        this.nombre = nuevoNombre;
    }

    public void setEmail(String nuevoEmail) {
        this.email = nuevoEmail;
    }

    public void setPassword(String nuevaPassword) {
        this.password = nuevaPassword;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getId() {
        return id;
    }

    public void setId(String nuevoId) {
        this.id = nuevoId;
    }

    public void iniciarSesion() {
        System.out.println("Iniciando sesión para " + nombre);
    }

    public List<FinanceItem> obtenerActivos() {
        return activos;
    }

    public List<FinanceItem> obtenerPasivos() {
        return pasivos;
    }

    public void agregarFinanceItem(FinanceItem item, boolean esActivo) {
        if (esActivo) {
            activos.add(item);
        } else {
            pasivos.add(item);
        }
    }

    public void eliminarFinanceItem(FinanceItem item, boolean esActivo) {
        if (esActivo) {
            activos.remove(item);
        } else {
            pasivos.remove(item);
        }
    }

    public void verBalance() {
        double totalActivos = activos.stream().mapToDouble(item -> item.getMontoActual()).sum();
        double totalPasivos = pasivos.stream().mapToDouble(item -> item.getMontoActual()).sum();
        double balance = totalActivos - totalPasivos;
        System.out.println("Balance total: " + balance);
    }
}
