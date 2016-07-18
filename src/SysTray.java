import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;

public class SysTray {

	private SystemTray systemTray;
	private TrayIcon trayIcon;
	private String servSet, IP;
	private int portNum;
	private PopupMenu popMenu = new PopupMenu();
	
	public SysTray(String serverSetting) {
		if (SystemTray.isSupported()) {
			servSet = serverSetting;
			// Create SystemTray and TrayIcon (TrayIcon : It is icon that
			// display in SystemTray)
			systemTray = SystemTray.getSystemTray();
			trayIcon = new TrayIcon(new ImageIcon(getClass().getResource("/images/P2PCc-Icon.png")).getImage(), "PhoneToPCClip Server");
			// Autosize icon base on space available on tray
			trayIcon.setImageAutoSize(true);

			MouseAdapter mouseAdapter = new MouseAdapter() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(servSet.equals("Printscreen")){
						servSet = "Save";
					} else {
						servSet = "Printscreen";
					}
					updateDescription();
				}
			};

			trayIcon.addMouseListener(mouseAdapter);
			
			//Add "Close" menu to close server when done
			MenuItem closeItem = new MenuItem("Close");
			popMenu.add(closeItem);
			closeItem.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent arg0) {
					System.exit(0);
				}
			});
			trayIcon.setPopupMenu(popMenu);

			try {
				systemTray.add(trayIcon);
			} catch (Exception e) {
				e.printStackTrace();
			}
			trayIcon.displayMessage("PhoneToPCClip is running!", "Hover over this icon to see IP and Port details\nRight click and select \"Close\" to close the server", TrayIcon.MessageType.INFO);
		}
	}

	public Image getImage(String path) {
		ImageIcon icon = new ImageIcon(path, "System Tray Icon");
		return icon.getImage();
	}

	public void updateDescription(String IP, int portNum) {
		this.IP = IP;
		this.portNum = portNum;
		updateDescription();
	}
	
	public void updateDescription() {
		trayIcon.setToolTip("Server is running at: " + IP + ":" + portNum + "\nSetting is currently: " + servSet + "\nClick to switch between Save and Printscreen mode");
		if(servSet.equals("Save")){
			trayIcon.setImage(new ImageIcon(getClass().getResource("/images/P2PCc_ServerOnlySave.png")).getImage());
		} else {
			trayIcon.setImage(new ImageIcon(getClass().getResource("/images/P2PCc_ServerOnlyClip.png")).getImage());
		}
	}
	
	public String getServerSetting(){
		return servSet;
	}
	
	public void notifyUser(String caption, String text){
		trayIcon.displayMessage(caption, text, TrayIcon.MessageType.NONE);
	}
}
