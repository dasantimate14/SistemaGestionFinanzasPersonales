package sistemagestionfinanzas;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class Cliente {
    private String nombre;
    private String email;
    private String password;
    private String id;
    private ArrayList<FinanceItem> activos;
    private ArrayList<FinanceItem> pasivos;

    // Lista estática para almacenar todos los clientes
    public static ArrayList<Cliente> instancias_clientes = new ArrayList<>();

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

    public void setId(String nuevoId) {
        this.id = nuevoId;
    }

    // Métodos relacionados con la gestión de activos y pasivos
    public void obtenerActivos() {
        // Lógica para obtener y mostrar activos
    }

    public void obtenerPasivos() {
        // Lógica para obtener y mostrar pasivos
    }

    public void agregarFinanceItem(FinanceItem item, boolean esActivo) {
        if (esActivo) {
            this.activos.add(item);
        } else {
            this.pasivos.add(item);
        }
    }

    public void eliminarFinanceItem(FinanceItem item, boolean esActivo) {
        if (esActivo) {
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
        double totalActivos = activos.stream().mapToDouble(item -> item.getMontoActual()).sum();
        double totalPasivos = pasivos.stream().mapToDouble(item -> item.getMontoActual()).sum();
        double balance = totalActivos - totalPasivos;
        System.out.println("Balance total: " + balance);
    }

    // Método para guardar el cliente en la base de datos
    public void guardarClienteEnBaseDatos() {
        String consultaRegistro = "INSERT INTO clientes (id, nombre, email, password) VALUES (?, ?, ?, ?)";

        String[] parametros = new String[]{getId(), getNombre(), getEmail(), getPassword()};

        try {
            BaseDeDatos.establecerConexion();
            boolean registroExitoso = BaseDeDatos.ejecutarActualizacion(consultaRegistro, parametros);
            if (registroExitoso) {
                System.out.println("Registro exitoso del cliente.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDeDatos.cerrarConexion();
        }
    }

    // Método estático para cargar todos los clientes desde la base de datos
    public static void cargarClientesDesdeBaseDeDatos() {
        String consulta = "SELECT * FROM clientes";
        try {
            BaseDeDatos.establecerConexion();
            ResultSet rs = BaseDeDatos.realizarConsultaSelectInterna(consulta);
            while (rs.next()) {
                // Leer cada uno de los campos del ResultSet
                String id = rs.getString("id");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                String password = rs.getString("password");

                // Crear el objeto Cliente con los datos capturados
                Cliente cliente = new Cliente(nombre, email, password);
                cliente.setId(id);

                // Agregar el cliente a la lista estática
                instancias_clientes.add(cliente);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            BaseDeDatos.cerrarConexion();
        }
    }

    // Método para buscar un cliente por ID
    public static Cliente buscarClientePorId(String idBuscado) {
        for (Cliente cliente : instancias_clientes) {
            if (cliente.getId().equals(idBuscado)) {
                return cliente;
            }
        }
        return null; // Si no se encuentra el cliente
    }
}