import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;

public class P2PCc_Server {
	static String currentDir = System.getProperty("user.dir");
	private SysTray tray;

	public P2PCc_Server(SysTray tray) {
		this.tray = tray;
	}

	// Make Server And Receive (MSAR)
	public void mSAR() throws IOException {
		ServerSocket servsock = null;
		Socket sock = null;
		InputStream inFromClient = null;
		FileOutputStream targetFile = null;
		BufferedOutputStream dataToFile = null;
		int bufferSize = 0;

		servsock = searchPort();
		if (servsock == null) {
			System.out.println("Server is not established");
			return;
		}
		System.out.println("Waiting for Client...");
		tray.updateDescription(InetAddress.getLocalHost().getHostAddress(), servsock.getLocalPort());
		
		try {
			sock = servsock.accept();
		} catch (IOException e) {
			System.out.println("Cannot connect to client");
			return;
		}
		System.out.println("Client connection is established");
		
		// Get the setting -> "Save" or "Printscreen"
		String setting = tray.getServerSetting();

		try {
			inFromClient = sock.getInputStream();
			bufferSize = sock.getReceiveBufferSize();
			// System.out.println("Buffer Size: " + bufferSize);
		} catch (IOException ex) {
			System.out.println("Cannot get stream from client");
		}

		// Byte buffer
		byte[] byteData = new byte[bufferSize];
		int count = 0;
		// Make Directory if it doesn't exist
		new File(currentDir + "\\Captured Image\\").mkdir();
		// byte[] test = { 0 };

		// Get Original Filename, but change filename to timestamp, if setting
		// is "Save"
		inFromClient.read(byteData);
		String filename = new String(byteData, "US-ASCII");
		filename = filename.trim();
		if (setting.equals("Save")) {
			Date date = new Date();
			Timestamp t = new Timestamp(date.getTime());
			filename = t.toString();
			String[] split = filename.split(" ");
			filename = split[0] + "_" + split[1].substring(0, 2)
					+ split[1].substring(3, 5) + split[1].substring(6, 8)
					+ ".jpg";
		}

		// Make file and streams
		try {
			targetFile = new FileOutputStream("Captured Image\\" + filename);
			dataToFile = new BufferedOutputStream(targetFile);
		} catch (FileNotFoundException ex) {
			System.out.println("Where's file?");
			return;
		}

		// Receive data
		while ((count = inFromClient.read(byteData)) > 0) {
			dataToFile.write(byteData, 0, count);
		}

		// Put image to Clipboard and delete it later after MSAR method returns,
		// if setting is "Printscreen"
		if (setting.equals("Printscreen")) {
			ClipOwner co = new ClipOwner();
			co.setClip("Captured Image\\" + filename);
			// test = byteData.clone();
		}

		// Closing connections and streams
		sock.close();
		servsock.close();
		targetFile.flush();
		dataToFile.flush();
		inFromClient.close();
		targetFile.close();
		dataToFile.close();
	}

	public ServerSocket searchPort() {
		for (int i = 8000; i < 65536; i++) {
			try {
				ServerSocket ss = new ServerSocket(i);
				return ss;
			} catch (IOException e) {
			}
		}
		return null;
	}
}
