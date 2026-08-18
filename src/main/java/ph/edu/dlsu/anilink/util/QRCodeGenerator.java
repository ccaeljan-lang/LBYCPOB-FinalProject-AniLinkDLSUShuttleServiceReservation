package ph.edu.dlsu.anilink.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import java.awt.image.BufferedImage;

/**
 * Utility class for generating JavaFX-compatible QR code images.
 *
 * <p>Key components:
 * <ul>
 *   <li><b>ZXing Integration:</b> Encodes arbitrary string payloads into 2D matrix representations.</li>
 *   <li><b>Image Conversion:</b> Translates AWT {@link BufferedImage} pixels into JavaFX {@link WritableImage} formats for UI rendering.</li>
 * </ul>
 * </p>
 */
public class QRCodeGenerator {

    public static Image generateQRCodeImage(String text, int width, int height) throws Exception {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
        WritableImage writableImage = new WritableImage(width, height);
        PixelWriter pixelWriter = writableImage.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                pixelWriter.setArgb(x, y, bufferedImage.getRGB(x, y));
            }
        }
        return writableImage;
    }
}