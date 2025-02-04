package ies.castillodeluna.ad.ejercicios;

import ies.castillodeluna.ad.ejercicios.backend.BackendFactory;
import ies.castillodeluna.ad.ejercicios.backend.Conexion;
import ies.castillodeluna.ad.ejercicios.ui.Interfaz;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class Main {
    private static Conexion conexion;
    private static final Path dbUrl = Path.of(System.getProperty("user.dir"), "src", "main", "resources", "pedidos.db");

    public static void main(String[] args) {
        try {
            inicializarConexion();
            Interfaz intefaz = new Interfaz(conexion);
            intefaz.mostrarMenu();
        } catch (Exception e) {
            System.err.println("Error fatal: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /*
     * Iniciar la conexión para trabajar con la BD
     */
    private static void inicializarConexion() throws Exception {
        // Verificar si la carpeta existe, si no, crearla
        Files.createDirectories(dbUrl.getParent());

        // Establecer la conexión a la base de datos
        Map<String, Object> opciones = Map.of(
            "base", "sqlite",
            "url", "jdbc:sqlite:" + dbUrl.toString(),
            "user", "",
            "password", ""
        );
        conexion = BackendFactory.crearConexion(opciones);
    }
}
