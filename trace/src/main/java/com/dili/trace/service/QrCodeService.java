package com.dili.trace.service;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import org.springframework.stereotype.Service;

import cn.hutool.core.img.ImgUtil;

@Service
public class QrCodeService {
	private final String imageType = "png";
	public String getBase64QrCode(String content, int qrWidth, int qrHeight) throws Exception {

		byte[] bytes = this.getByteImage(content, qrWidth, qrHeight);
		return this.base64Image(bytes);
	}

	public String getBase64QrCode(String content, InputStream logoFileInputStream, int qrWidth, int qrHeight) throws Exception {
		if (logoFileInputStream==null ) {
			byte[] bytes = this.getByteImage(content, qrWidth, qrHeight);
			return this.base64Image(bytes);
		} else {
			byte[] bytes = getByteImage(content, logoFileInputStream, qrWidth, qrHeight);
			return this.base64Image(bytes);
		}

	}

	private String base64Image(byte[] bytes) {
		String base64 = Base64.getEncoder().encodeToString(bytes).trim();
		base64 = "data:image/"+imageType+";base64," + base64;
		return base64;	

	}



	private byte[] getByteImage(String content, int qrWidth, int qrHeight) throws Exception {
		// Create new configuration that specifies the error correction

		QRCodeWriter writer = new QRCodeWriter();
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 0);
		// Create a qr code with the url as content and a size of WxH px
		BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, qrWidth, qrHeight, hints);

		// Load QR image
		BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, getMatrixConfig());

		// Initialize combined image
		BufferedImage combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) combined.getGraphics();

		// Write QR code to new image at position 0/0
		g.drawImage(qrImage, 0, 0, null);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

		// Write logo into combine image at position (deltaWidth / 2) and
		// (deltaHeight / 2). Background: Left/Right and Top/Bottom must be
		// the same space for the logo to be centered

		// Write combined image as PNG to OutputStream
		ImageIO.write(combined, imageType, os);
		return os.toByteArray();
	}

	public byte[] getByteImage(String content, InputStream logoFileInputStream, int qrWidth, int qrHeight)
			throws WriterException, IOException {
		// Create new configuration that specifies the error correction

		QRCodeWriter writer = new QRCodeWriter();
		ByteArrayOutputStream os = new ByteArrayOutputStream();

		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, 0);
		// Create a qr code with the url as content and a size of WxH px
		BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, qrWidth, qrHeight, hints);

		// Load QR image
		BufferedImage qrImage = MatrixToImageWriter.toBufferedImage(bitMatrix, getMatrixConfig());

		// Load logo image
		BufferedImage overly = getOverly(logoFileInputStream, qrWidth, qrHeight);

		// Calculate the delta height and width between QR code and logo
		int deltaHeight = qrImage.getHeight() - overly.getHeight();
		int deltaWidth = qrImage.getWidth() - overly.getWidth();

		// Initialize combined image
		BufferedImage combined = new BufferedImage(qrImage.getHeight(), qrImage.getWidth(),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = (Graphics2D) combined.getGraphics();

		// Write QR code to new image at position 0/0
		g.drawImage(qrImage, 0, 0, null);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

		// Write logo into combine image at position (deltaWidth / 2) and
		// (deltaHeight / 2). Background: Left/Right and Top/Bottom must be
		// the same space for the logo to be centered
		g.drawImage(overly, (int) Math.round(deltaWidth / 2), (int) Math.round(deltaHeight / 2), null);

		// Write combined image as PNG to OutputStream
		ImageIO.write(combined, imageType, os);
		return os.toByteArray();
		// Store Image
//          Files.copy( new ByteArrayInputStream(os.toByteArray()), Paths.get(DIR + generateRandoTitle(new Random(), 9) +ext), StandardCopyOption.REPLACE_EXISTING);

	}

	private BufferedImage getOverly(InputStream logoFileInputStream, int qrWidth, int qrHeight) throws IOException {
		int min = qrHeight > qrWidth ? qrWidth : qrHeight;
		BufferedImage imageSrc = ImgUtil.read(logoFileInputStream);
		RenderedImage renderedImage = ImgUtil.toRenderedImage(imageSrc);
		float scale = ((min / 10.0F) / (renderedImage.getWidth() > renderedImage.getHeight() ? renderedImage.getHeight()
				: renderedImage.getWidth()));
		return (BufferedImage) ImgUtil.scale(imageSrc, scale);
	}

	private MatrixToImageConfig getMatrixConfig() {
		// ARGB Colors
		// Check Colors ENUM
		return new MatrixToImageConfig(Colors.WHITE.getArgb(), Colors.BLACK.getArgb());
	}

	public enum Colors {

		BLUE(0xFF40BAD0), RED(0xFFE91C43), PURPLE(0xFF8A4F9E), ORANGE(0xFFF4B13D), WHITE(0xFFFFFFFF), BLACK(0xFF000000);

		private final int argb;

		Colors(final int argb) {
			this.argb = argb;
		}

		public int getArgb() {
			return argb;
		}
	}
}