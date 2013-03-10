package phoneproxy.providers;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import phoneproxy.controlers.snom.SnomControler;
import phoneproxy.sources.DataSource;
import phoneproxy.xml.XmlDocument;
import phoneproxy.xml.snom.SnomIPPhoneText;

/**
 * Ruft die angeforderten Daten von einer DataSource ab und gibt sie als
 * SnomDocument zurück und/oder sendet Befehle ein SnomIPPhone Telefone
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
        return "text/xml";
    }

    @Override
    public String getContent(String target, String source, String data) {
        System.out.println("SnomProvider.getContent( target: " + target + ", action: " + source + ", data: " + data + " )");
        if (source.equals("SnomIPPhone.xsl")) {
            try {
                FileInputStream fi = new FileInputStream(new File(SnomProvider.class.getClassLoader().getResource("phoneproxy/resources/SnomIPPhone.xsl").toURI()));
                FileChannel fc = fi.getChannel();
                return Charset.defaultCharset().decode(fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size())).toString();
            } catch (Exception ex) {
                Logger.getLogger(SnomProvider.class.getName()).log(Level.SEVERE, null, ex);
            }
            return "";
        } else if (source.equals("incoming_call") && this.getControler(target) != null) {
            this.getControler(target).showUrl(phoneproxy.PhoneProxy.getServer().getAddressString().concat("/foobar"));
//            return ""
            return Provider.marshal(new SnomIPPhoneText("URL pushed to ".concat(target), phoneproxy.PhoneProxy.getServer().getAddressString().concat("/active_call_info")));
        } else if (source.equals("active_call_info")) {
            return Provider.marshal(new SnomIPPhoneText("Aktueller Anruf", "Im Moment ist kein Anruf aktiv"));
        } else if (this.sources.containsKey(source)) {
            XmlDocument out = this.sources.get(source).request(data);
            return Provider.marshal(out);
        } else {
            return Provider.marshal(new SnomIPPhoneText("Fehler", "Unbekannte Quelle \"" + source + "\""));
        }

    }

    @Override
    public void addControler(String target) {
        this.addControler(target, new SnomControler(target));
    }
}
