package ies.castillodeluna.ad.ejercicios.backend.sqlite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Stream;

import javax.sql.DataSource;

import ies.castillodeluna.ad.ejercicios.SqlUtils;
import ies.castillodeluna.ad.ejercicios.DAO.AbstractDao;
import ies.castillodeluna.ad.ejercicios.DAO.ConnectionProvider;
import ies.castillodeluna.ad.ejercicios.DAO.Crud;
import ies.castillodeluna.ad.ejercicios.errors.DataAccessException;
import ies.castillodeluna.ad.ejercicios.model.Pedido;

public class PedidoSqlite extends AbstractDao implements Crud<Pedido>{

    /**
     * Constructor del pedido
     * @param ds Fuente de datos
     */
    public PedidoSqlite(DataSource ds) {
        super(ds);
    }

    /**
     * Constructor del pedido
     * @param conn Conexión a la base de datos
     */
    public PedidoSqlite(Connection conn) {
        super(conn);
    }
    /**
     * Constructor del pedido
     * @param cp Proveedor de conexiones
     */
    public PedidoSqlite(ConnectionProvider cp){
        super(cp);
    }

    /**
     * Recupera datos de un registro para convertirlos en un nuevo Pedido.
     * @param rs ResultSet con el registro
     * @return Objeto Pedido que modela los datos del registro
     * @throws SQLException
     */
    private static Pedido resultToPedido(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_pedido");
        Date fecha = rs.getDate("fecha");
        double importe = rs.getDouble("importe");
        int idCliente = rs.getInt("id_cliente");
        return new Pedido(id, fecha, importe, idCliente);
    }

    /**
     * Establece los parámetros de un Pedido en un PreparedStatement.
     * @param pedido Objeto Pedido a insertar/actualizar
     * @param stmt PreparedStatement al que se asignan los valores
     * @throws SQLException
     */
    private static void setPedidoParams(Pedido pedido, PreparedStatement stmt) throws SQLException {
        if (pedido.getFecha() != null) {
            stmt.setDate(1, new java.sql.Date(pedido.getFecha().getTime()));
        } else {
            stmt.setNull(1, java.sql.Types.DATE);
        }
        stmt.setDouble(2, pedido.getImporte());
        stmt.setInt(3, pedido.getIdCliente());
        stmt.setInt(4, pedido.getId());
    }



    @Override
    public Optional<Pedido> get(int id) throws DataAccessException {
        final String sqlString = "SELECT * FROM Pedidos WHERE id_pedido = ?";
        try (
            Connection conn = cp.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? Optional.of(resultToPedido(rs)) : Optional.empty();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Stream<Pedido> get() throws DataAccessException {
        final String sqlString = "SELECT * FROM Pedidos";
        try {
            Connection conn = cp.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlString);
            return SqlUtils.resultSetToStream(cp.isCloseable() ? conn : stmt, rs, PedidoSqlite::resultToPedido);
        } catch (SQLException err) {
            throw new DataAccessException(err);
        }
    }

    @Override
    public boolean delete(int id) throws DataAccessException {
        final String sqlString = "DELETE FROM Pedidos WHERE id_pedido = ?";
        try (
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
    public void insert(Pedido pedido) throws DataAccessException {
        final String sqlString = "INSERT INTO Pedidos (fecha, importe, id_cliente) VALUES (?, ?, ?)";
        try (
            Connection conn = cp.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {
            pstmt.setDate(1, new java.sql.Date(pedido.getFecha().getTime()));
            pstmt.setDouble(2, pedido.getImporte());
            pstmt.setInt(3, pedido.getIdCliente());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public boolean update(Pedido pedido) throws DataAccessException {
        final String sqlString = "UPDATE Pedidos SET fecha = ?, importe = ?, id_cliente = ? WHERE id_pedido = ?";
        try (
            Connection conn = cp.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {
            setPedidoParams(pedido, pstmt);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public boolean update(int oldId, int newId) throws DataAccessException {
        final String sqlString = "UPDATE Pedidos SET id_pedido = ? WHERE id_pedido = ?";
        try (
            Connection conn = cp.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {
            pstmt.setInt(1, newId);
            pstmt.setInt(2, oldId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }
    
}
