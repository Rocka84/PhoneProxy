package phoneproxy.xml.mapper;

import phoneproxy.sources.DataSource;

/**
 * Mapped Daten von einem XML-Format in ein anderes
 * 
 * @author Fabian Dillmeier <fabian at dillmeier.de>
 */
public abstract class Mapper implements DataSource{
    protected DataSource client;

    public Mapper(DataSource client) {
        this.client = client;
    }
    
    public DataSource getClient() {
        return client;
    }

}
