package org.plain.utils;

import lombok.Getter;

/**
 * 图片格式枚举类，定义常见的图片格式。
 * 用于类型安全地指定图片的读写格式。
 * @author Jayden.Liang
 */
@Getter
public enum ImageFormat {
    /** PNG 格式 */
    PNG("png"),
    /** JPG 格式 */
    JPG("jpg"),
    /** JPEG 格式 */
    JPEG("jpeg"),
    /** BMP 格式 */
    BMP("bmp"),
    /** GIF 格式 */
    GIF("gif");

    /** 格式名称（用于 ImageIO 等 API）
     * -- GETTER --
     *  获取格式名称字符串。
     */
    private final String formatName;

    /**
     * 构造方法。
     * @param formatName 格式名称
     */
    ImageFormat(String formatName) {
        this.formatName = formatName;
    }

    /**
     * 获取格式名称
     *
     * @return format name
     */
    public String getFormatName() {
        return formatName;
    }

}
