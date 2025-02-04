package ies.castillodeluna.ad.ejercicios.backend;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

public abstract class AbstractDsCache {
    
    private static Map<String, DataSource> cache = new HashMap<>();

    /**
     * Genera una clave para identificar el DataSource y distinguirlo de otros.
     * @param opciones Las opciones de conexión.
     * @return La clave generada.
     */
    protected abstract String generateKey(Map<String, Object> opciones);
    /**
     * Implementa la creación de un nuevo DataSource.
     * @param opciones Las opciones de conexión.
     * @return El objeto creado.
     */
    protected abstract DataSource createDataSource(Map<String, Object> opciones);

    protected DataSource getDataSource(Map<String, Object> opciones) {
        String key = generateKey(opciones);
        if(cache.containsKey(key)) return cache.get(key);

        DataSource ds = createDataSource(opciones);
        cache.put(key, ds);
        return ds;
    }

}
