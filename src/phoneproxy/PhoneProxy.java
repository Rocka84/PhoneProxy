package phoneproxy;

import java.io.StringWriter;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.ResourceBundle;
import java.util.logging.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import phoneproxy.gui.GUI;
import phoneproxy.providers.SnomProvider;
import phoneproxy.server.Server;
import phoneproxy.sources.ActiveCallSource;
import phoneproxy.sources.BlauDataSource;
import phoneproxy.sources.CSVDataSource;
import phoneproxy.sources.MenuSource;
import phoneproxy.xml.mapper.BlauToSnomMapper;
import phoneproxy.xml.mapper.CSVToSnomMapper;
import phoneproxy.xml.snom.*;

/**
 * Main Controller
 * @author Fabian Dillmeier <fabian at dillmeier.de>
 */
public class PhoneProxy {

    private static Server server;
    private static ResourceBundle language;
    private final static Logger logger = Logger.getLogger("phoneproxy");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        initLogger(Level.ALL);

        PhoneProxy.language = ResourceBundle.getBundle("phoneproxy.resources.lang", java.util.Locale.getDefault());

        try {
            PhoneProxy.server = new Server(new SnomProvider());

            PhoneProxy.server.getProvider().addSource("", new MenuSource());
            PhoneProxy.server.getProvider().addSource("blau", new BlauToSnomMapper(new BlauDataSource()));
            PhoneProxy.server.getProvider().addSource("csv", new CSVToSnomMapper(new CSVDataSource("blau_data.csv")));
            PhoneProxy.server.getProvider().addSource("call", new ActiveCallSource());
            
            logger.log(Level.INFO, "Server created");
        } catch (Exception ex) {
            logger.log(Level.SEVERE, ex.getMessage());
        }

        
        /* Quick'n'Dirty */
        if (args.length>0 && args[0].equals("-nogui")){
            logger.log(Level.INFO, "No GUI mode");
            PhoneProxy.server.startServer();
        }else{
            new GUI();
        }
    }

    private static void initLogger(Level loglevel) {
        logger.setLevel(loglevel);
        logger.setUseParentHandlers(false);

        Handler handler = new ConsoleHandler();
        handler.setFormatter(new Formatter() {

            @Override
            public String format(LogRecord record) {
                GregorianCalendar cal = new GregorianCalendar();
                cal.setTimeInMillis(record.getMillis());

                StringBuilder out = new StringBuilder();
                out.append(String.format("[ %02d:%02d:%02d ] ", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND)));
                out.append(record.getLevel().getName()).append(": ").append(record.getMessage());
                if (logger.getLevel().intValue() <= Level.FINE.intValue()) {
                    out.append("  [ ").append(record.getSourceClassName()).append(".").append(record.getSourceMethodName()).append("() ]");
                }
                out.append(System.getProperty("line.separator"));

                return out.toString();
            }
        });
        logger.addHandler(handler);
    }

    public static Server getServer() {
        return server;
    }

    private static void testElems() {

        SnomDocument elem = new SnomIPPhoneText();

        ((SnomIPPhoneText) elem).setTitle("Hello World!");
        ((SnomIPPhoneText) elem).setText("Dies ist ein Text-Element.");
        elem.setFetch(server.getAddressString().concat("/asdf/"), 3000);

        dumpJAXB(elem);



        elem = new SnomIPPhoneInput();
        ((SnomIPPhoneInput) elem).setTitle("SnomIPPhoneInput");
        ((SnomIPPhoneInput) elem).setURL("http://127.0.0.1/asdf/");
        SnomIPPhoneInput.InputItem input = new SnomIPPhoneInput.InputItem();
        input.setDisplayName("Gib was ein;");
        input.setQueryStringParam("&action=foo&antwort=");
        input.setInputFlags("a");
        ((SnomIPPhoneInput) elem).setInputItem(input);

        dumpJAXB(elem);



        elem = new SnomIPPhoneDirectory();
        ((SnomIPPhoneDirectory) elem).setTitle("SnomIPPhoneDirectory");
        SnomIPPhoneDirectory.DirectoryEntry dir_entries[] = new SnomIPPhoneDirectory.DirectoryEntry[3];
        dir_entries[0] = new SnomIPPhoneDirectory.DirectoryEntry();
        dir_entries[0].setName("Name 1");
        dir_entries[0].setTelephone("0 11 1234");
        ((SnomIPPhoneDirectory) elem).getDirectoryEntry().add(dir_entries[0]);

        dir_entries[1] = new SnomIPPhoneDirectory.DirectoryEntry();
        dir_entries[1].setName("Name 2");
        dir_entries[1].setTelephone("0 22 1234");
        ((SnomIPPhoneDirectory) elem).getDirectoryEntry().add(dir_entries[1]);

        dir_entries[2] = new SnomIPPhoneDirectory.DirectoryEntry();
        dir_entries[2].setName("Name 3");
        dir_entries[2].setTelephone("0 33 1234");
        ((SnomIPPhoneDirectory) elem).getDirectoryEntry().add(dir_entries[2]);

        dumpJAXB(elem);



        elem = new SnomIPPhoneMenu();
        ((SnomIPPhoneMenu) elem).setTitle("SnomIPPhoneMenu");
        SnomIPPhoneMenu.MenuItem entries[] = new SnomIPPhoneMenu.MenuItem[3];
        entries[0] = new SnomIPPhoneMenu.MenuItem();
        entries[0].setName("Eintrag 1");
        entries[0].setURL("http://127.0.0.1/asdf/1");
        ((SnomIPPhoneMenu) elem).getMenuItem().add(entries[0]);

        entries[1] = new SnomIPPhoneMenu.MenuItem();
        entries[1].setName("Eintrag 2");
        entries[1].setURL("http://127.0.0.1/asdf/2");
        ((SnomIPPhoneMenu) elem).getMenuItem().add(entries[1]);

        entries[2] = new SnomIPPhoneMenu.MenuItem();
        entries[2].setName("Eintrag 3");
        entries[2].setURL("http://127.0.0.1/asdf/3");
        ((SnomIPPhoneMenu) elem).getMenuItem().add(entries[2]);


        dumpJAXB(elem);



        elem = new SnomIPPhoneImage();
        SnomIPPhoneImage.Data data = new SnomIPPhoneImage.Data();
        data.setValue(("blablab.base64.zeug.blubber").getBytes());
        ((SnomIPPhoneImage) elem).setData(data);


        dumpJAXB(elem);

    }

    public static void dumpJAXB(SnomDocument document) {
        // create JAXB context and instantiate marshaller
        JAXBContext context;
        try {
            context = JAXBContext.newInstance(document.getClass());
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            StringWriter w = new StringWriter();
            m.marshal(document, w);

            System.out.println(w.toString());
        } catch (JAXBException ex) {
            System.out.println(ex);
        }
    }

    public static String getLanguageString(String key) {
        return PhoneProxy.language.getString(key);
    }

    public static Logger getLogger() {
        return logger;
    }
}
