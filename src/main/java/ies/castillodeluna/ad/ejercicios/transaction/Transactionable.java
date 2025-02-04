package ies.castillodeluna.ad.ejercicios.transaction;
import java.sql.Connection;

import ies.castillodeluna.ad.ejercicios.errors.*;

/**
 *  Interfaz funcional para lambdas que se usan en transacciones.
 */
@FunctionalInterface
public interface Transactionable {
    void run(Connection conn) throws DataAccessException;
}