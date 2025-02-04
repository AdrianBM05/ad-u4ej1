package ies.castillodeluna.ad.ejercicios.ui;

import ies.castillodeluna.ad.ejercicios.backend.Conexion;
import ies.castillodeluna.ad.ejercicios.model.Cliente;
import ies.castillodeluna.ad.ejercicios.model.Pedido;
import ies.castillodeluna.ad.ejercicios.model.ZonaEnvio;
import ies.castillodeluna.ad.ejercicios.DAO.Crud;

import java.util.Scanner;

public class Interfaz {
    private static final Scanner scanner = new Scanner(System.in);
    private Conexion conexion;

    // Constructor que inicializa la conexión con la base de datos
    public Interfaz(Conexion conexion) {
        this.conexion = conexion;
    }

    // Mostrar el menú principal de la aplicación
    public void mostrarMenu() {
        while (true) {
            System.out.println("\n=== MENÚ PRINCIPAL ===");
            System.out.println("1. Agregar Cliente");
            System.out.println("2. Borrar Cliente");
            System.out.println("3. Consultar Zonas de Envío");
            System.out.println("4. Consultar Clientes");
            System.out.println("5. Consultar Pedidos por Cliente");
            System.out.println("0. Salir");

            int opcion = leerEntero("Seleccione una opción: ");
            switch (opcion) {
                case 1:
                    agregarCliente();  // Llama a la función para agregar un cliente
                    break;
                case 2:
                    borrarCliente();  // Llama a la función para borrar un cliente
                    break;
                case 3:
                    consultarZonasEnvio();  // Llama a la función para consultar zonas de envío
                    break;
                case 4:
                    consultarClientes();  // Llama a la función para consultar los clientes
                    break;
                case 5:
                    consultarPedidosPorCliente();  // Llama a la función para consultar los pedidos por cliente
                    break;
                case 0:
                    return;  // Sale del programa
                default:
                    System.out.println("Opción no válida.");
            }
        }
    }

    /**
     * Función para leer Integer de forma más rápida
     * @param mensaje String con el mensaje que se va a mostrar
     * @return Integer ingresado por el usuario
     */
    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);  // Muestra el mensaje al usuario
                return Integer.parseInt(scanner.nextLine().trim());  // Lee y convierte el valor a Integer
            } catch (NumberFormatException e) {
                System.out.println("Por favor, introduce un número válido");
            }
        }
    }

    /**
     * Función para leer un String de forma más rápida
     * @param mensaje String que se va a mostrar al usuario
     * @return String ingresado por el usuario
     */
    private static String leerTexto(String mensaje){
        System.out.println(mensaje);  // Muestra el mensaje
        return scanner.nextLine();  // Lee y retorna el texto ingresado por el usuario
    }

    // Agregar un nuevo cliente a la base de datos
    private void agregarCliente() {
        System.out.println("\nAgregar Cliente:");
    
        // Solicitar los datos del cliente al usuario
        String nombre = leerTexto("Nombre del cliente: ");
        String correo = leerTexto("Correo del cliente: ");
        String telefono = leerTexto("Teléfono del cliente: ");
        int zonaEnvioId = leerEntero("ID de zona de envío: ");  // Lee el ID de la zona de envío
    
        // Usamos el DAO de clientes para agregar el cliente a la base de datos
        Crud<Cliente> clienteDao = conexion.getClienteDao();
        Cliente cliente = new Cliente();  // Creamos un nuevo objeto Cliente
    
        // Establecemos los datos en el cliente
        cliente.setNombre(nombre);
        cliente.setEmail(correo);
        cliente.setTelefono(telefono);
        cliente.setIdZonaEnvio(zonaEnvioId);
    
        try {
            // Insertamos el cliente en la base de datos
            clienteDao.insert(cliente);  // Llama al método insert del DAO
            System.out.println("Cliente agregado correctamente.");
        } catch (Exception e) {
            // En caso de error, mostramos el mensaje de error
            System.out.println("Error al agregar cliente: " + e.getMessage());
        }
    }

    /**
     * Solicita el ID de un cliente y elimina el cliente con ese ID de la base de datos
     */
    private void borrarCliente() {
        int clienteId = leerEntero("Introduce ID del cliente a eliminar: ");
        Crud<Cliente> clienteDao = conexion.getClienteDao();
    
        try {
            // Intentamos eliminar el cliente directamente por su ID
            boolean eliminado = clienteDao.delete(clienteId);
            
            if (eliminado) {
                System.out.println("Cliente eliminado correctamente.");
            } else {
                System.out.println("No se pudo eliminar el cliente.");
            }
        } catch (Exception e) {
            System.out.println("Error al borrar cliente: " + e.getMessage());
        }
    }
    
    // Consultar todas las zonas de envío disponibles
    private void consultarZonasEnvio() {
        // Usamos el DAO para obtener las zonas de envío
        Crud<ZonaEnvio> zonaEnvioDao = conexion.getZonaEnvioDao();
        try {
            zonaEnvioDao.get().forEach(zona -> {
                System.out.println("ID: " + zona.getId() + " | Nombre: " + zona.getNombre() +
                                    " | Tarifa: " + zona.getTarifa());  // Muestra los datos de la zona de envío
            });
        } catch (Exception e) {
            System.out.println("Error al consultar zonas de envío: " + e.getMessage());
        }
    }

    // Consultar todos los clientes registrados
    private void consultarClientes() {
        Crud<Cliente> clienteDao = conexion.getClienteDao();
        try {
            // Obtener todos los clientes y mostrar sus datos completos
            clienteDao.get().forEach(cliente -> {
                System.out.println("ID: " + cliente.getId() + 
                                   " | Nombre: " + cliente.getNombre() + 
                                   " | Email: " + cliente.getEmail() + 
                                   " | Teléfono: " + cliente.getTelefono() + 
                                   " | ID Zona: " + cliente.getIdZonaEnvio());
            });
        } catch (Exception e) {
            System.out.println("Error al consultar clientes: " + e.getMessage());
        }
    }
    

    // Consultar los pedidos de un cliente y su dinero total
    private void consultarPedidosPorCliente() {
        int clienteId = leerEntero("Introduce ID del cliente para ver sus pedidos: ");
        Crud<Pedido> pedidoDao = conexion.getPedidoDao();

        try {
            // Filtrar y mostrar los pedidos del cliente con el ID proporcionado
            pedidoDao.get()
                    .filter(pedido -> pedido.getIdCliente() == clienteId)
                    .forEach(pedido -> {
                        System.out.println("Pedido ID: " + pedido.getId() + " | Dinero: " + pedido.getImporte());
                    });
        } catch (Exception e) {
            System.out.println("Error al consultar pedidos: " + e.getMessage());
        }
    }
}
