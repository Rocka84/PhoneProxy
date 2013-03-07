package phoneproxy.providers;

import phoneproxy.controlers.snom.SnomControler;
import phoneproxy.sources.DataSource;
import phoneproxy.xml.XmlDocument;
import phoneproxy.xml.snom.SnomIPPhoneText;

/**
 * Ruft die angeforderten Daten von einer DataSource ab und gibt sie als SnomDocument
 * zurück und/oder sendet Befehle ein SnomIPPhone Telefone
 *
 * @author Fabian Dillmeier <fabian at dillmeier.de>
 */
public class SnomProvider extends Provider {

    public SnomProvider(DataSource source) {
        super(source);
    }

    public SnomProvider() {
    }

    @Override
    public String getContentType() {
        return "text/plain";
    }

    @Override
    public String getContent(String target,String source, String data) {
        //System.out.println("SnomProvider.getContent( action: "+action+", data: "+data+" )");
        if (source.equals("incoming_call") && this.getControler(target)!=null) {
            this.getControler(target).showUrl("http://".concat(phoneproxy.PhoneProxy.getServer().getAddressString()).concat("/foobar"));
            return "";
        } else if (this.sources.containsKey(source)) {
            XmlDocument out = this.sources.get(source).request(data);
            return Provider.marshal(out);
        } else {
            return Provider.marshal(new SnomIPPhoneText("Fehler", "Unbekannte Quelle \""+source+"\""));
        }

    }

    @Override
    public void addControler(String target) {
        this.addControler(target,new SnomControler(target));
    }
}
