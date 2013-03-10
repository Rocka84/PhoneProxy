package phoneproxy.sources;

import java.util.HashMap;
import phoneproxy.PhoneProxy;
import phoneproxy.server.Server;
import phoneproxy.xml.snom.SnomDocument;
import phoneproxy.xml.snom.SnomIPPhoneInput;
import phoneproxy.xml.snom.SnomIPPhoneInput.InputItem;
import phoneproxy.xml.snom.SnomIPPhoneMenu;

/**
 *
 * @author Fabian Dillmeier <fabian at dillmeier.de>
 */
public class MenuSource implements DataSource {

    @Override
    public SnomDocument request(String data) {
        HashMap<String, String> args = Server.splitData(data);
        
        SnomDocument out=null;

        
        out=new SnomIPPhoneMenu();
        out.addSoftKeyItem("index","Index", PhoneProxy.getServer().getAddressString().concat("/"));
        if (!args.containsKey("menu") || args.get("menu").isEmpty()){
            ((SnomIPPhoneMenu) out).setTitle("Phone Proxy - Main menu");
            ((SnomIPPhoneMenu) out).addMenuItem("CSV", PhoneProxy.getServer().getAddressString().concat("/?menu=csv"));
            ((SnomIPPhoneMenu) out).addMenuItem("blau", PhoneProxy.getServer().getAddressString().concat("/blau"));
            ((SnomIPPhoneMenu) out).addMenuItem("Call", PhoneProxy.getServer().getAddressString().concat("/?menu=call"));
        }else if (!args.get("menu").isEmpty()){
            if (args.get("menu").equals("csv")){
                ((SnomIPPhoneMenu) out).setTitle("Phone Proxy - CSV");
                ((SnomIPPhoneMenu) out).addMenuItem("Alles", PhoneProxy.getServer().getAddressString().concat("/csv"));
                ((SnomIPPhoneMenu) out).addMenuItem("Suche", PhoneProxy.getServer().getAddressString().concat("/?menu=csv_suche"));
            }else if (args.get("menu").equals("csv_suche")){
                out= new SnomIPPhoneInput();
                ((SnomIPPhoneInput) out).setTitle("Phone Proxy - CSV");
                ((SnomIPPhoneInput) out).setURL(PhoneProxy.getServer().getAddressString().concat("/csv"));
                ((SnomIPPhoneInput) out).setInputItem("Suche", "", "", "a");
            }else if (args.get("menu").equals("call")){
                ((SnomIPPhoneMenu) out).setTitle("Phone Proxy - Anrufe");
                ((SnomIPPhoneMenu) out).addMenuItem("Eingehenden Anruf simulieren", PhoneProxy.getServer().getAddressString().concat("/call?incoming=0123456789"));
                ((SnomIPPhoneMenu) out).addMenuItem("Angenommenen Anruf simulieren", PhoneProxy.getServer().getAddressString().concat("/call?answered=0123456789"));
                ((SnomIPPhoneMenu) out).addMenuItem("Auflegen", PhoneProxy.getServer().getAddressString().concat("/call?end=1"));
                ((SnomIPPhoneMenu) out).addMenuItem("Anruf Info", PhoneProxy.getServer().getAddressString().concat("/call?info=1"));
            }
        }
        
        
        return out;
    }
}
