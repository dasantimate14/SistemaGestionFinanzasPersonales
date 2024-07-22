package sistemagestionfinanzas;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Usuario {
    private String nombre;
    private String email;
    private String contrasena;
    private String id;
    private ArrayList<FinanceItem> activos;
    private ArrayList<FinanceItem> pasivos;

    //Usuario estático que maneja el ususario de la sesión. El sistema solo maneja un objeto usuario a la vez
    public static Usuario usuario_actual;

    public static String getIdUsuarioActual(){
        if (usuario_actual != null) {
            return usuario_actual.getId();
        } else {
            throw new IllegalStateException("Usuario actual no está inicializado");
        }
    }
    public static void setUsuarioActual(Usuario usuario) {usuario_actual = usuario;}
    public static Usuario getUsuarioActual() {return usuario_actual;}

    // Constructor
    public Usuario(String nombre, String email, String contrasena) {
        this.nombre = Objects.requireNonNull(nombre, "Nombre no puede ser nulo");
        this.email = Objects.requireNonNull(email, "Email no puede ser nulo");
        this.contrasena = Objects.requireNonNull(contrasena, "Contraseña no puede ser nula");
        this.activos = new ArrayList<>();
        this.pasivos = new ArrayList<>();
    }

    // Métodos getter y setter
    public void setNombre(String nuevoNombre) {
        this.nombre = Objects.requireNonNull(nuevoNombre, "Nombre no puede ser nulo");
    }

    public void setEmail(String nuevoEmail) {
        this.email = Objects.requireNonNull(nuevoEmail, "Email no puede ser nulo");
    }

    public void setPassword(String nuevaPassword) {
        this.contrasena = Objects.requireNonNull(contrasena, "Contraseña no puede ser nula");
    }

    public String getNombre() {
        return this.nombre;
    }

    public String getEmail() {
        return this.email;
    }

    public String getCotrasena() {
        return this.contrasena;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String nuevoId) {
        this.id = nuevoId;
    }

    public List<FinanceItem> getActivos() {return this.activos;}
    public ArrayList<FinanceItem> getPasivos() {return this.pasivos;}

    // Métodos relacionados con la gestión de activos y pasivos
    public void agregarFinanceItem(FinanceItem item) {
        Objects.requireNonNull(item, "FinanceItem no puede ser nulo");
        if (item.getTipo().equals("Activo")) {
            this.activos.add(item);
        } else {
            this.pasivos.add(item);
        }
    }

    public void eliminarFinanceItem(FinanceItem item, boolean esActivo) {
        Objects.requireNonNull(item, "FinanceItem no puede ser nulo");
        if (item.getTipo().equals("Activo")) {
            this.activos.remove(item);
        } else {
            this.pasivos.remove(item);
        }
    }

    // Lógica para iniciar sesión del cliente
    public static boolean auntentificacionIniciarSesion(String email, String contrasena) {
        boolean es_usuario = false;
        String usuario_email = "";
        String usuario_contrasena = "";
        String consulta = "SELECT * FROM usuarios WHERE email = ? AND contrasena = ?";

        String[] parametros = {email, contrasena};
        try {
            BaseDeDatos.establecerConexion();
            ResultSet rs = BaseDeDatos.realizarConsultaSelect(consulta, parametros);
            if (rs == null) {
                return false;
            }
            while(rs.next()){
                usuario_email = rs.getString("email");
                usuario_contrasena = rs.getString("contrasena");
            }
            if (usuario_email.equals(email) && usuario_contrasena.equals(contrasena)) {
                es_usuario = true;
            } else {
                es_usuario = false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDeDatos.cerrarConexion();
        }
        return es_usuario;
    }

    // Método para obtener información general del cliente
    public StringBuilder obtenerInformacionGeneral() {
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
        String consultaRegistro = "INSERT INTO usuarios (id, nombre, email, contrasena) VALUES (UUID(), ?, ?, ?)";

        String[] parametros = new String[]{getNombre(), getEmail(), getCotrasena()};

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
    public static void cargarUsuariosDesdeBaseDeDatos(String email) throws SQLException {
        String consulta = "SELECT * FROM usuarios WHERE email = ?";
        String[] parametros = {email};
        try {
            BaseDeDatos.establecerConexion();
            ResultSet rs = BaseDeDatos.realizarConsultaSelect(consulta, parametros);
            if (rs == null) {
                throw new SQLException("No se encontró el usuario de la sesión iniciada");
            }
            while (rs.next()) {
                // Leer cada uno de los campos del ResultSet
                String id = rs.getString("id");
                String nombre = rs.getString("nombre");
                String password = rs.getString("contrasena");

                // Crear el objeto Cliente con los datos capturados
                Usuario usuario = new Usuario(nombre, email, password);
                usuario.setId(id);

                //Se establece como el usuario actual
                setUsuarioActual(usuario);

            }
        } catch (SQLException ex) {
            throw ex;
        } finally {
            BaseDeDatos.cerrarConexion();
        }
    }


    //Metodo para verificar si el email ingresado ya existe
    public static boolean correoExistente(String email){
        String consulta = "SELECT * FROM usuarios WHERE email = ?";
        String[] parametros = {email};
        boolean correo_existente = false;

        try{
            BaseDeDatos.establecerConexion();
            ResultSet rs = BaseDeDatos.realizarConsultaSelect(consulta, parametros);
            if(rs.next()){
                correo_existente = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            BaseDeDatos.cerrarConexion();
        }
        return correo_existente;
    }

    public float calcularBalance(){
        float total_activos = 0;
        float total_pasivos = 0;
        for(FinanceItem item : activos){
            total_activos += item.getMontoActual();
        }
        for(FinanceItem item : pasivos){
            total_pasivos += item.getMontoActual();
        }
        return total_activos - total_pasivos;
    }


}