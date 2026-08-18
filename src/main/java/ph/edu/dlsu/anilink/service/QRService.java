package ph.edu.dlsu.anilink.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;
import ph.edu.dlsu.anilink.model.Reservation;

import java.awt.image.BufferedImage;

/**
 * Service component responsible for QR code generation and payload verification in the AniLink system.
 *
 * <p>This service leverages ZXing library components to render optical barcodes for boarding verification.
 * It encapsulates the following key capabilities:
 * <ul>
 *   <li><b>Spring Service Bean:</b> Managed as a singleton service component via {@link Service} for application injection.</li>
 *   <li><b>Reservation QR Generation:</b> Converts a {@link Reservation}'s unique payload string into a renderable 2D QR image.</li>
 *   <li><b>Payload Verification:</b> Validates incoming payload format standards (e.g., matching {@code ANILINK-RES-} prefixes or valid UUID/key lengths) during boarding scans.</li>
 *   <li><b>ZXing BitMatrix Rendering:</b> Encodes arbitrary raw text strings directly into 2D raster {@link BufferedImage} graphics with customizable dimensional boundaries.</li>
 * </ul>
 * </p>
 */
@Service
public class QRService {

    public BufferedImage generateQRCode(Reservation reservation) {
        if (reservation == null || reservation.getQrPayload() == null || reservation.getQrPayload().isBlank()) {
            return null;
        }
        return generateQRCodeImage(reservation.getQrPayload(), 250, 250);
    }

    public boolean verifyQRCode(String qrPayload) {
        if (qrPayload == null || qrPayload.isBlank()) {
            return false;
        }

        // Accepts formatted reservation strings, UUIDs, or raw database keys
        return qrPayload.startsWith("ANILINK-RES-") || qrPayload.trim().length() >= 8;
    }

    public BufferedImage generateQRCodeImage(String text, int width, int height) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
                }
            }
            return image;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}