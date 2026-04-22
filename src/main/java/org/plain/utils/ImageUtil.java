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
    private ImageUtil() {
        /* This utility class should not be instantiated */
    }


    /**
     * Convert a buffered image to a byte array.
     *
     * @param image  the image to convert
     * @param format target image format
     * @return image bytes
     * @throws IOException when image writing fails
     */
    public static byte[] toBytes(BufferedImage image, ImageFormat format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format.getFormatName(), baos);
        return baos.toByteArray();
    }

    /**
     * Convert a buffered image to a Base64 encoded string.
     *
     * @param image  the image to convert
     * @param format target image format
     * @return Base64 string representation of the image
     * @throws IOException when image writing fails
     */
    public static String toBase64(BufferedImage image, ImageFormat format) throws IOException {
        byte[] bytes = toBytes(image, format);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
