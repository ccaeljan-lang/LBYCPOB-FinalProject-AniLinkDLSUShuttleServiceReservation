package ph.edu.dlsu.anilink.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import ph.edu.dlsu.anilink.model.Reservation;

import java.awt.image.BufferedImage;
import java.util.UUID;

public class QRService {

    public BufferedImage generateQRCode(Reservation reservation) {
        String payload = reservation != null ? "ANILINK-RES-" + reservation.toString() : UUID.randomUUID().toString();
        return generateQRCodeImage(payload, 200, 200);
    }

    public boolean verifyQRCode(String qrPayload) {
        return qrPayload != null && qrPayload.startsWith("ANILINK-RES-");
    }

}