package org.plain.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

/**
 * 图片工具类
 * @author Hugh
 */
public class ImageUtil {

    // BufferedImage -> byte[]
    public static byte[] toBytes(BufferedImage image, ImageFormat format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format.getFormatName(), baos);
        return baos.toByteArray();
    }

    // BufferedImage -> Base64 String
    public static String toBase64(BufferedImage image, ImageFormat format) throws IOException {
        byte[] bytes = toBytes(image, format);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
