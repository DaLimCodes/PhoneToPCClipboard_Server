import java.io.File;
import java.io.IOException;

public class P2PCc_Main {

	public static void main(String[] args) {
		SysTray tray = new SysTray("Printscreen");
		P2PCc_Server server = new P2PCc_Server(tray);
		while (true) {
			try {
				server.mSAR();
			} catch (IOException e) {
				System.out.println("Something happened");
			}
			if (tray.getServerSetting().equals("Printscreen")) {
				File file = new File("Captured Image\\temp.jpg");
                file.delete();
    			tray.notifyUser("", "Received image is in clipboard now.");
			} else {
				tray.notifyUser("", "Image saved.");
			}
		}
	}

}
