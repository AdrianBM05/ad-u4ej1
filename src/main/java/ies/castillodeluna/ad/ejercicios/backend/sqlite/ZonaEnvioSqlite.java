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
import ies.castillodeluna.ad.ejercicios.model.ZonaEnvio;

public class ZonaEnvioSqlite  extends AbstractDao implements Crud<ZonaEnvio>{
    /**
     * Constructor de la zona de envío
     * @param ds Fuente de datos
     */
    public ZonaEnvioSqlite(DataSource ds) {
        super(ds);
    }
    /**
     * Constructor de la clase
     * @param conn Conexión a la base de datos
     */
    public ZonaEnvioSqlite(Connection conn) {
        super(conn);
    }
    /**
     * Constructor de la clase
     * @param cp Proveedor de conexiones
     */
    public ZonaEnvioSqlite(ConnectionProvider cp) {
        super(cp);
    }

    /**
     * 
     * @param rs
     * @return
     * @throws SQLException
     */
    private static ZonaEnvio resultToZonaEnvio(ResultSet rs) throws SQLException {
        int id = rs.getInt("id_zona");
        String nombre = rs.getString("nombre_zona");
        double tarifa = rs.getDouble("tarifa_envio");
        return new ZonaEnvio(id, nombre, tarifa);
    }
    /**
     * 
     * @param zonaEnvio
     * @param stmt
     * @throws SQLException
     */
    private static void setZonaEnvioParams(ZonaEnvio zonaEnvio, PreparedStatement stmt) throws SQLException {
        stmt.setString(1, zonaEnvio.getNombre());
        stmt.setDouble(2, zonaEnvio.getTarifa());
        stmt.setInt(3, zonaEnvio.getId());
    }


    @Override
    public Optional<ZonaEnvio> get(int id) throws DataAccessException {
        final String sqlString = "SELECT * FROM Zonas_Envio WHERE id_zona = ?";
        try (
            Connection conn = cp.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? Optional.of(resultToZonaEnvio(rs)) : Optional.empty();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public Stream<ZonaEnvio> get() throws DataAccessException {
        final String sqlString = "SELECT * FROM Zonas_Envio";
        try {
            Connection conn = cp.getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sqlString);
            return SqlUtils.resultSetToStream(cp.isCloseable() ? conn : stmt, rs, ZonaEnvioSqlite::resultToZonaEnvio);
        } catch (SQLException err) {
            throw new DataAccessException(err);
        }
    }

    @Override
    public boolean delete(int id) throws DataAccessException {
        final String sqlString = "DELETE FROM Zonas_Envio WHERE id_zona = ?";
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
    public void insert(ZonaEnvio zonaEnvio) throws DataAccessException {
        final String sqlString = "INSERT INTO Zonas_Envio (nombre_zona, tarifa_envio) VALUES (?, ?)";
        try (
            Connection conn = cp.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {
            pstmt.setString(1, zonaEnvio.getNombre());
            pstmt.setDouble(2, zonaEnvio.getTarifa());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public boolean update(ZonaEnvio zonaEnvio) throws DataAccessException {
        final String sqlString = "UPDATE Zonas_Envio SET nombre_zona = ?, tarifa_envio = ? WHERE id_zona = ?";
        try (
            Connection conn = cp.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sqlString);
        ) {
            setZonaEnvioParams(zonaEnvio, pstmt);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DataAccessException(e);
        }
    }

    @Override
    public boolean update(int oldId, int newId) throws DataAccessException {
        final String sqlString = "UPDATE Zonas_Envio SET id_zona = ? WHERE id_zona = ?";
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
