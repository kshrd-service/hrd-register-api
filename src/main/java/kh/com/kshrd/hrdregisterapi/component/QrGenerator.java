package kh.com.kshrd.hrdregisterapi.component;

import org.springframework.stereotype.Component;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Map;

@Component
public class QrGenerator {

    public byte[] toHighResPng(String data) throws Exception {
        int matrixSize = 2000;
        int margin = 1;
        ErrorCorrectionLevel ec = ErrorCorrectionLevel.H;

        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());
        hints.put(EncodeHintType.ERROR_CORRECTION, ec);
        hints.put(EncodeHintType.MARGIN, margin);

        BitMatrix matrix = new QRCodeWriter()
                .encode(data, BarcodeFormat.QR_CODE, matrixSize, matrixSize, hints);

        BufferedImage img = MatrixToImageWriter.toBufferedImage(
                matrix,
                new MatrixToImageConfig(MatrixToImageConfig.BLACK, MatrixToImageConfig.WHITE)
        );

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(img, "PNG", baos);
            return baos.toByteArray();
        }
    }
}
