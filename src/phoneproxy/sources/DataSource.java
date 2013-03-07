package phoneproxy.sources;

import phoneproxy.xml.XmlDocument;

/**
 * Ruft Daten von einem Server/WebService ab und liefert sie als XmlDocument
 * zurück
 * 
 * @author Fabian Dillmeier <fabian at dillmeier.de>
 */
public interface DataSource {

    public abstract XmlDocument request(String data);

}
