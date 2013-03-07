package phoneproxy.gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import phoneproxy.PhoneProxy;

/**
 *
 * @author Fabian Dillmeier <fabian at dillmeier.de>
 */
public class GUI extends JFrame implements ActionListener {

    JLabel lbl_status;
    TrayIcon trayIcon;
    MenuItem itemStartStopServer;

    private final static Logger logger = Logger.getLogger("phoneproxy.gui");

    public GUI() {
        super(PhoneProxy.getLanguageString("MAINGUI_TITLE"));
        
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds(d.width / 2 - 150, d.height / 2 - 100, 300, 200);

        this.setLayout(new GridBagLayout());
        GridBagConstraints c=new GridBagConstraints();
        
        c.gridwidth=2;
        c.gridheight=2;
        c.anchor=GridBagConstraints.SOUTH;
        c.fill=GridBagConstraints.BOTH;
        
        c.gridx=1;
        c.gridy=1;

        this.lbl_status = new JLabel("Server" + (PhoneProxy.getServer().isRunning() ? "" : " not ") + "running",JLabel.CENTER);
        this.add(this.lbl_status,c);

        c.gridwidth=1;
        c.gridheight=1;
        c.gridx=1;
        c.gridy=3;
        JButton btn_start_server = new JButton("Start Server");
        btn_start_server.setActionCommand("btn_start_server");
        btn_start_server.addActionListener(this);
        this.add(btn_start_server,c);

        c.gridx=2;
        JButton btn_stop_server = new JButton("Stop Server");
        btn_stop_server.setActionCommand("btn_stop_server");
        btn_stop_server.addActionListener(this);
        this.add(btn_stop_server,c);

        c.gridx=1;
        c.gridy++;
        JButton btn_csv = new JButton("Push CSV");
        btn_csv.setActionCommand("btn_csv");
        btn_csv.addActionListener(this);
        this.add(btn_csv,c);

        c.gridx=2;
        JButton btn_dial = new JButton("Dial");
        btn_dial.setActionCommand("btn_dial");
        btn_dial.addActionListener(this);
        this.add(btn_dial,c);

        c.gridwidth=2;
        c.gridx=1;
        c.gridy++;
        JButton btn_exit = new JButton("Exit");
        btn_exit.setActionCommand("exit");
        btn_exit.addActionListener(this);
        this.add(btn_exit,c);

//        this.setVisible(true);

        this.initTray();
    }

    private void initTray() {
//Check the SystemTray is supported
        if (!SystemTray.isSupported()) {
            logger.log(Level.WARNING,"No SystemTray");
            return;
        }
        PopupMenu popup = new PopupMenu();
        this.trayIcon = new TrayIcon(new ImageIcon("phoneproxy/resources/8ball.gif").getImage(), PhoneProxy.getLanguageString("TRAY_TITLE"));

        MenuItem showGuiItem = new MenuItem("Show Gui");
        showGuiItem.setActionCommand("tray_show_gui");
        showGuiItem.addActionListener(this);
        popup.add(showGuiItem);

        this.itemStartStopServer = new MenuItem("Start Server");
        this.itemStartStopServer.setActionCommand("tray_start_stop_server");
        this.itemStartStopServer.addActionListener(this);
        popup.add(this.itemStartStopServer);

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setActionCommand("exit");
        exitItem.addActionListener(this);
        popup.add(exitItem);

        this.trayIcon.setPopupMenu(popup);
        this.trayIcon.setImageAutoSize(true);

        this.trayIcon.setActionCommand("tray_icon");
        this.trayIcon.addActionListener(this);

        try {
            SystemTray.getSystemTray().add(trayIcon);
            logger.log(Level.INFO,"TrayIcon added");
        } catch (AWTException e) {
            logger.log(Level.SEVERE,"Adding TrayIcon failed.");
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("tray_icon") || cmd.equals("tray_show_gui")) {
            this.setVisible(true);
        } else if (cmd.equals("exit")) {
            if (!PhoneProxy.getServer().isRunning() || JOptionPane.showConfirmDialog(this, PhoneProxy.getLanguageString("MSG_EXIT_RUNNING_QUESTION"), PhoneProxy.getLanguageString("DIALOG_TITLE"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                logger.log(Level.INFO,"Exiting via GUI");
                PhoneProxy.getServer().stopServer();
                this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                this.dispose();
                SystemTray.getSystemTray().remove(this.trayIcon);
            }
        } else if (cmd.equals("tray_start_stop_server")) {
            if (PhoneProxy.getServer().isRunning()) {
                PhoneProxy.getServer().stopServer();
                this.itemStartStopServer.setLabel("Start Server");
            } else {
                PhoneProxy.getServer().startServer();
                this.itemStartStopServer.setLabel("Stop Server");
            }
            
        } else if (cmd.equals("btn_csv")) {
            PhoneProxy.getServer().getProvider().getControler("").showUrl("http://".concat(PhoneProxy.getServer().getAddressString()).concat("/csv"));
        } else if (cmd.equals("btn_dial")) {
            PhoneProxy.getServer().getProvider().getControler("").dial("017660999515");
            
        } else if (cmd.equals("btn_start_server")) {
            PhoneProxy.getServer().startServer();
            this.lbl_status.setText("Server running");
        } else if (cmd.equals("btn_stop_server")) {
            PhoneProxy.getServer().stopServer();
            this.lbl_status.setText("Server not running");
        }
    }
}