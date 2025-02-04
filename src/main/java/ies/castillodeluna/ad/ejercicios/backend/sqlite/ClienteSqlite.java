package ies.castillodeluna.ad.ejercicios.backend.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.stream.Stream;

import javax.sql.DataSource;

import ies.castillodeluna.ad.ejercicios.SqlUtils;
import ies.castillodeluna.ad.ejercicios.DAO.AbstractDao;
import ies.castillodeluna.ad.ejercicios.DAO.ConnectionProvider;
import ies.castillodeluna.ad.ejercicios.DAO.Crud;
import ies.castillodeluna.ad.ejercicios.errors.DataAccessException;
import ies.castillodeluna.ad.ejercicios.model.Cliente;

public class ClienteSqlite extends AbstractDao implements Crud<Cliente>{

    /**
     * Constructor del cliente
     * @param ds Fuente de datos
     */
    public ClienteSqlite(DataSource ds) {
        super(ds);
    }

    /**
     * Constructor del cliente
     * @param conn Conexión a la base de datos
     */
    public ClienteSqlite(Connection conn) {
        super(conn);
    }

    /**
     * Constructor del cliente
     * @param cp Proveedor de conexiones
     */
    public ClienteSqlite(ConnectionProvider cp){
        super(cp);
    }


    /**
     * Recupera datos de un registro para convertirlos en un nuevo Cliente.
     * @param rs ResultSet con el registro
     * @return Objeto cliente que modela los datos del registro
     * @throws SQLException
     */
    private static Cliente resultToCliente(ResultSet rs) throws SQLException{
        int id = rs.getInt("id_cliente");
        String nombre = rs.getString("nombre");
        String email = rs.getString("email");
        String telefono = rs.getString("telefono");
        int idZonaEnvio = rs.getInt("id_zona");

        return new Cliente(id,nombre,email,telefono,idZonaEnvio);
    }

    /**
     * Recogemos como último dato el id del elemento, luego veremos porque
     * @param cliente
     * @param stmt
     * @throws SQLException
     */
    private static void setClienteParams(Cliente cliente, PreparedStatement stmt) throws SQLException{
        stmt.setString(1, cliente.getNombre());
        stmt.setString(2, cliente.getEmail());
        stmt.setString(3, cliente.getTelefono());
        stmt.setInt(4, cliente.getIdZonaEnvio());
    }


    // Recibir cliente mediante un ID
    @Override
    public Optional<Cliente> get(int id) throws DataAccessException {
        final String sqlString = "SELECT + FROM Clientes WHERE id_centro = ?";

        try(
            Connection conn = cp.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {
            
            pstmt.setInt(1,id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next()?Optional.of(resultToCliente(rs)):Optional.empty();

        } catch (SQLException e) {
            throw new DataAccessException(e);
        }

    }

    @Override
    public Stream<Cliente> get() throws DataAccessException {
        
        final String sqlString = "SELECT * FROM Clientes";

        try {
            
            Connection conn = cp.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlString);

            return SqlUtils.resultSetToStream(cp.isCloseable()?conn:stmt, rs, ClienteSqlite::resultToCliente);

        } catch (SQLException err) {
            throw new DataAccessException(err);
        }

    }

    @Override
    public boolean delete(int id) throws DataAccessException {
        final String sqlString = "DELETE FROM Clientes WHERE id_cliente = ?";

        try(
            Connection conn = cp.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }

    }

    @Override
    public void insert(Cliente cliente) throws DataAccessException {
        // Poner el id al final es para esto
        final String sqlString = "INSERT INTO Clientes (nombre, email, telefono, id_zona) VALUES (?,?,?,?)";

        try(
            Connection conn = cp.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {
            
            setClienteParams(cliente, pstmt);
            pstmt.executeUpdate();


        } catch (SQLException e) {
            throw new DataAccessException(e);
        }

    }

    @Override
    public boolean update(Cliente cliente) throws DataAccessException {
        final String sqlString = "UPDATE Clientes SET nombre = ?, correo = ?, telefono = ?, idZonaEnvio = ?, id_cliente = ?";

        try(
            Connection conn = cp.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {
            
            setClienteParams(cliente, pstmt);
            return pstmt.executeUpdate() > 0;


        } catch (SQLException e) {
            throw new DataAccessException(e);
        }

    }

    @Override
    public boolean update(int oldId, int newId) throws DataAccessException {
        final String sqlString = "UPDATE Clientes SET id_cliente = ? WHERE id_cliente = ?";

        try(
            Connection conn = cp.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {
            
            pstmt.setInt(1, newId);
            pstmt.setString(2, sqlString);
            return pstmt.executeUpdate() > 0;


        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
    
}
