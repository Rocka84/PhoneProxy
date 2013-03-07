package phoneproxy.sources;

import java.util.HashMap;
import phoneproxy.xml.XmlDocument;
import phoneproxy.xml.snom.SnomDocument;
import phoneproxy.xml.snom.SnomIPPhoneText;

/**
 *
 * @author Fabian Dillmeier <fabian at dillmeier.de>
 */
public class ActiveCallSource implements DataSource {

    private String active_caller="";

    @Override
    public SnomDocument request(String data) {
        String[] data_parts = data.split("&");
        HashMap<String, String> args = new HashMap<String, String>();
        for (int i = 0; i < data_parts.length; i++) {
            String[] arg_parts = data_parts[i].split("=");
            if (arg_parts.length == 2) {
                args.put(arg_parts[0], arg_parts[1]);
            }
            if (arg_parts.length == 1) {
                args.put(arg_parts[0], "");
            }
        }
        
//        System.out.println(args);
        
        SnomDocument out=null;

        if (args.containsKey("incoming") && !args.get("incoming").isEmpty()) {
            out = new SnomIPPhoneText("Eingehender Anruf", "Anruf von ".concat(args.get("incoming")));
        }
        if (args.containsKey("answered") && !args.get("answered").isEmpty()) {
            active_caller=args.get("answered");
            out = new SnomIPPhoneText("Anruf angenommen", "Anruf von ".concat(active_caller));
        }
        if (args.containsKey("end")) {
            out = new SnomIPPhoneText("Anruf Beendet", active_caller.isEmpty()?"Telefonat beendet.":"Telefonat mit ".concat(active_caller).concat(" beendet."));
            active_caller="";
        }
        
        if (args.containsKey("info")) {
            out = new SnomIPPhoneText("Aktiver Anruf", active_caller.isEmpty()?"Im moment ist kein Anruf aktiv.":"Max Mustermann (".concat(active_caller).concat(")"));
        }else if (out == null){
            out = new SnomIPPhoneText("Fehler", "ungueltige daten");
        }

        return out;
    }
}
