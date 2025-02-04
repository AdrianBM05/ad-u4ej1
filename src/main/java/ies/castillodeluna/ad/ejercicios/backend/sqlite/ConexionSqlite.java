package ies.castillodeluna.ad.ejercicios.backend.sqlite;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.stream.Stream;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import ies.castillodeluna.ad.ejercicios.SqlUtils;
import ies.castillodeluna.ad.ejercicios.DAO.Crud;
import ies.castillodeluna.ad.ejercicios.backend.AbstractDsCache;
import ies.castillodeluna.ad.ejercicios.backend.Conexion;
import ies.castillodeluna.ad.ejercicios.errors.DataAccessException;
import ies.castillodeluna.ad.ejercicios.model.Cliente;
import ies.castillodeluna.ad.ejercicios.model.Pedido;
import ies.castillodeluna.ad.ejercicios.model.ZonaEnvio;
import ies.castillodeluna.ad.ejercicios.transaction.TransactionManager;

public class ConexionSqlite extends AbstractDsCache implements Conexion{

    /*
     * Crear la conexión con la base de datos.
     */
    final static Path esquema = Path.of(System.getProperty("user.dir"), "src", "main", "resources", "pedidos.sql");
    final static String protocol = "jdbc:sqlite";
    final static short maxConn = 10;
    final static short minConn = 1;

    private final HikariDataSource ds;


    // Constructor
    public ConexionSqlite(Map<String, Object> opciones)throws DataAccessException {
        ds = (HikariDataSource) getDataSource(opciones);
        initDB();
    }


    @Override
    public Crud<Cliente> getClienteDao() {
        return new ClienteSqlite(ds);
    }

    @Override
    public Crud<Pedido> getPedidoDao() {
        return new PedidoSqlite(ds);
    }

    @Override
    public Crud<ZonaEnvio> getZonaEnvioDao() {
        return new ZonaEnvioSqlite(ds);
    }

    @Override
    public void transaccion(Transaccionable operaciones) throws DataAccessException {
        try(Connection conn = ds.getConnection()) {
            TransactionManager.transactionSQL(conn, c -> {
                operaciones.run(new ClienteSqlite(c), new PedidoSqlite(c), new ZonaEnvioSqlite(c)); // PONER ZONA ENVIO ?????
            });
        }
        catch(SQLException err) {
            throw new DataAccessException(err);
        }
    }

    @Override
    protected String generateKey(Map<String, Object> opciones) {
        return (String) opciones.get("url");
    }
    
    /**
     * Crear la fuente de datos
     */
    @Override
    protected DataSource createDataSource(Map<String, Object> opciones) {
        String path = (String) opciones.get("url");
        if(path == null) throw new IllegalArgumentException("No se ha fijado la url de la base de datos");

        String dbUrl = path.startsWith("jdbc:sqlite:") ? path : protocol + ":" + path;

        Short maxConn = (Short) opciones.getOrDefault("maxconn", ConexionSqlite.maxConn);
        Short minConn = (Short) opciones.getOrDefault("minconn", ConexionSqlite.minConn);


        HikariConfig hconfig = new HikariConfig();
        hconfig.setJdbcUrl(dbUrl);
        hconfig.setMaximumPoolSize(maxConn);
        hconfig.setMinimumIdle(minConn);

        return new HikariDataSource(hconfig);
    }

    /**
     * InitDB
     * Comprobar acceso a base de datos
     * Crear la conexión si es posible
     */
    public void initDB() throws DataAccessException{
        try(Stream<Cliente> clientes = getClienteDao().get()){
            clientes.close();
        }
        catch(DataAccessException err){
            
            try(
                Connection conn = ds.getConnection();
                InputStream st = Files.newInputStream(esquema);
            ){
                SqlUtils.executeSQL(conn, st);
            }
            catch(SQLException e){
                throw new DataAccessException("No puede crearse el esquema de la base de datos",e);
            }
            catch(IOException e){
                throw new DataAccessException(String.format("No puede acceder al esquema : %s", esquema));
            }

        }
    }
    
}
