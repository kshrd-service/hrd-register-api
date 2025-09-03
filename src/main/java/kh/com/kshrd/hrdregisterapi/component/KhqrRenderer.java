package kh.com.kshrd.hrdregisterapi.component;

import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

@Component
public class KhqrRenderer {

    private static final int SIZE = 500;

    private static final float BADGE_SCALE = 0.14f;
    private static final float ICON_SCALE  = 0.52f;

    private static final String ICON_PATH = "/static/images/Dollar-Sign.png";

    private static final Color BLACK = Color.BLACK;
    private static final Color WHITE = Color.WHITE;
    private static final Color FRAME = Color.decode("#295f98");

    public byte[] render(byte[] highResQrPng) throws Exception {
        BufferedImage canvas = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = canvas.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

            int qrBoxSize = Math.round(SIZE * 0.92f);
            int qrLeft    = (SIZE - qrBoxSize) / 2;
            int qrTop     = (SIZE - qrBoxSize) / 2;

            BufferedImage qrHi = ImageIO.read(new ByteArrayInputStream(highResQrPng));
            BufferedImage qrScaled = new BufferedImage(qrBoxSize, qrBoxSize, BufferedImage.TYPE_INT_ARGB);
            AffineTransform at = AffineTransform.getScaleInstance(
                    qrBoxSize / (double) qrHi.getWidth(),
                    qrBoxSize / (double) qrHi.getHeight()
            );
            AffineTransformOp op = new AffineTransformOp(at, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            op.filter(qrHi, qrScaled);
            g.drawImage(qrScaled, qrLeft, qrTop, null);

            drawCornerFrame(g, qrLeft, qrTop, qrBoxSize);

            int badgeD = Math.round(SIZE * BADGE_SCALE);
            int badgeX = SIZE / 2 - badgeD / 2;
            int badgeY = SIZE / 2 - badgeD / 2;

            g.setColor(WHITE);
            g.fillOval(badgeX, badgeY, badgeD, badgeD);

            g.setColor(BLACK);
            g.setStroke(new BasicStroke(Math.max(2f, SIZE * 0.012f)));
            g.drawOval(badgeX, badgeY, badgeD, badgeD);

            BufferedImage icon = loadIconOrNull();
            if (icon != null) {
                int iconSize = Math.round(badgeD * ICON_SCALE);
                int ix = SIZE / 2 - iconSize / 2;
                int iy = SIZE / 2 - iconSize / 2;

                Image scaledIcon = icon.getScaledInstance(iconSize, iconSize, Image.SCALE_SMOOTH);
                Graphics2D gIcon = (Graphics2D) g.create();
                gIcon.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                gIcon.drawImage(scaledIcon, ix, iy, null);
                gIcon.dispose();
            }

        } finally {
            g.dispose();
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(canvas, "PNG", baos);
            return baos.toByteArray();
        }
    }

    private void drawCornerFrame(Graphics2D g, int x, int y, int size) {
        int gap = Math.max(6, Math.round(SIZE * 0.02f));
        int len = Math.max(28, Math.round(size * 0.16f));
        float thick = Math.max(6f, SIZE * 0.028f);

        Stroke old = g.getStroke();
        g.setStroke(new BasicStroke(thick, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.setColor(KhqrRenderer.FRAME);

        int left   = x - gap;
        int right  = x + size + gap;
        int top    = y - gap;
        int bottom = y + size + gap;

        g.drawLine(left, top, left + len, top);
        g.drawLine(left, top, left, top + len);
        g.drawLine(right - len, top, right, top);
        g.drawLine(right, top, right, top + len);
        g.drawLine(left, bottom, left + len, bottom);
        g.drawLine(left, bottom - len, left, bottom);
        g.drawLine(right - len, bottom, right, bottom);
        g.drawLine(right, bottom - len, right, bottom);

        g.setStroke(old);
    }

    private BufferedImage loadIconOrNull() {
        try (InputStream is = getClass().getResourceAsStream(ICON_PATH)) {
            if (is == null) return null;
            return ImageIO.read(is);
        } catch (Exception e) {
            return null;
        }
    }
}
