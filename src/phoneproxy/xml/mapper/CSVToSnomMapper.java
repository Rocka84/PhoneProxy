package phoneproxy.xml.mapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import phoneproxy.sources.CSVDataSource;
import phoneproxy.xml.snom.SnomDocument;

/**
 * Mapped die Daten von einer CSVDataSource zu einem SnomDocument
 *
 * @author Fabian Dillmeier <fabian at dillmeier.de>
 */
public class CSVToSnomMapper extends Mapper {

    public CSVToSnomMapper(CSVDataSource client) {
        super(client);
    }
    public CSVToSnomMapper(String file) throws FileNotFoundException, IOException {
        this(new CSVDataSource(file));
    }

    @Override
    public CSVDataSource getClient() {
        return (CSVDataSource) client;
    }

    @Override
    public SnomDocument request(String data) {
//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException ex) {
//        }
        //CSVDataSource gibt _bis jetzt_  einfach ein SnomDocument zurück...
        return ((SnomDocument) this.getClient().request(data));
    }
}
