package ph.edu.dlsu.anilink.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import ph.edu.dlsu.anilink.model.Reservation;

import java.awt.image.BufferedImage;

public class QRService {

    public BufferedImage generateQRCode(Reservation reservation) {
        if (reservation == null) {
            return null;
        }

        return generateQRCodeImage(reservation.getQrPayload(), 200, 200);
    }

    public boolean verifyQRCode(String qrPayload) {
        return qrPayload != null && qrPayload.startsWith("ANILINK-RES-");
    }

    private BufferedImage generateQRCodeImage(String text, int width, int height) {
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
            return null;
        }
    }
}