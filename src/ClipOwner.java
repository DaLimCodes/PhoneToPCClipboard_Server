import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

class ClipOwner implements ClipboardOwner {

	public ClipOwner() {

	}

	public boolean setClip(String path) throws IOException {
		BufferedImage im = ImageIO.read(new File(path));
		TransferableImage toClip = new TransferableImage(im);
		Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
		c.setContents(toClip, this);
		return true;
	}

	@Override
	public void lostOwnership(Clipboard clpbrd, Transferable contents) {
		// System.out.println("Wrong");
	}

	private class TransferableImage implements Transferable {

		Image i;

		public TransferableImage(Image i) {
			this.i = i;
		}

		@Override
		public DataFlavor[] getTransferDataFlavors() {
			DataFlavor[] flavors = new DataFlavor[1];
			flavors[0] = DataFlavor.imageFlavor;
			return flavors;
		}

		@Override
		public Object getTransferData(DataFlavor arg0)
				throws UnsupportedFlavorException, IOException {
			// TODO Auto-generated method stub
			if (arg0.equals(DataFlavor.imageFlavor) && i != null) {
				return i;
			} else {
				throw new UnsupportedFlavorException(arg0);
			}
		}

		@Override
		public boolean isDataFlavorSupported(DataFlavor arg0) {
			// TODO Auto-generated method stub
			DataFlavor[] flavors = getTransferDataFlavors();
			for (int i = 0; i < flavors.length; i++) {
				if (arg0.equals(flavors[i])) {
					return true;
				}
			}
			return false;
		}
	}
}
