package phoneproxy.xml.mapper;

import phoneproxy.sources.BlauDataSource;
import phoneproxy.xml.blau.BlauDocument;
import phoneproxy.xml.snom.SnomDocument;
import phoneproxy.xml.snom.SnomIPPhoneText;

/**
 * Mapped die Daten aus einem BlauDocument zu einem SnomDocument
 *
 * @author Fabian Dillmeier <fabian at dillmeier.de>
 */
public class BlauToSnomMapper extends Mapper {

    public BlauToSnomMapper(BlauDataSource client) {
        super(client);
    }
    public BlauToSnomMapper() {
        this(new BlauDataSource());
    }

    @Override
    public BlauDataSource getClient() {
        return (BlauDataSource) client;
    }

    @Override
    public SnomDocument request(String data) {
        BlauDocument doc = this.getClient().request(data);
        SnomDocument out;

        /*
         *
         * Hier die Daten aus dem BlauDocument auslesen und in ein SnomDocument
         * einsetzen.das örtlk
         *
         */

        out = new SnomIPPhoneText("BlauToSnomMapper", "data: " + data);
        out.addSoftKeyItem("index","Index", phoneproxy.PhoneProxy.getServer().getAddressString().concat("/"));

        return out;
    }
}
