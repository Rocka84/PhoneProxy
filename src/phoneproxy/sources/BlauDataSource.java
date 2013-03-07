package phoneproxy.sources;

import phoneproxy.xml.blau.BlauDocument;

/**
 * Ruft Daten vom blau direkt WebService ab und liefert sie als BlauDocument
 * zurück
 *
 * @author Fabian Dillmeier <fabian at dillmeier.de>
 */
public class BlauDataSource implements DataSource{

    @Override
    public BlauDocument request(String data) {
        
        /**
         * 
         * Hier den Webservice ansprechen
         * 
         */
        
        
        return new BlauDocument();
    }
      
}
