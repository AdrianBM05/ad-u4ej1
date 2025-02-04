package ies.castillodeluna.ad.ejercicios.backend;

import ies.castillodeluna.ad.ejercicios.DAO.Crud;
import ies.castillodeluna.ad.ejercicios.errors.DataAccessException;
import ies.castillodeluna.ad.ejercicios.model.Cliente;
import ies.castillodeluna.ad.ejercicios.model.Pedido;
import ies.castillodeluna.ad.ejercicios.model.ZonaEnvio;


public interface Conexion {

    @FunctionalInterface
    public interface Transaccionable {
        void run(Crud<Cliente> clienteDao, Crud<Pedido> pedidoDao, Crud<ZonaEnvio> zonaEnvioDao) throws DataAccessException;
    }

    Crud<Cliente> getClienteDao();
    Crud<Pedido> getPedidoDao();
    Crud<ZonaEnvio> getZonaEnvioDao(); 

    void transaccion(Transaccionable operaciones) throws DataAccessException;
}

