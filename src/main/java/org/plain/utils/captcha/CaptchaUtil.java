package org.plain.utils.captcha;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.util.Config;

import java.awt.image.BufferedImage;
import java.util.Properties;

/**
 * 验证码工具类
 * @author Jayden.Liang
 */
public class CaptchaUtil {

    private static final Producer PRODUCER;

    private static final String CHAR_LENGTH_KEY = "kaptcha.textproducer.char.length";
    private static final String CHAR_LENGTH = "5";

    private static final String CHAR_STRING_KEY = "kaptcha.textproducer.char.string";
    private static final String CHAR_STRING = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";

    private static final String IMAGE_WIDTH_KEY = "kaptcha.image.width";
    private static final String IMAGE_WIDTH = "160";

    private static final String IMAGE_HEIGHT_KEY = "kaptcha.image.height";
    private static final String IMAGE_HEIGHT = "60";
    private static final String NOISE_IMPL_KEY = "kaptcha.noise.impl";
    private static final String NOISE_IMPL = "com.google.code.kaptcha.impl.DefaultNoise";



    static {
        // 静态初始化验证码配置
        Properties props = new Properties();
        props.setProperty(CHAR_LENGTH_KEY, CHAR_LENGTH);
        props.setProperty(CHAR_STRING_KEY, CHAR_STRING);
        props.setProperty(IMAGE_WIDTH_KEY, IMAGE_WIDTH);
        props.setProperty(IMAGE_HEIGHT_KEY, IMAGE_HEIGHT);
        props.setProperty(NOISE_IMPL_KEY, NOISE_IMPL);
        Config config = new Config(props);
        PRODUCER = config.getProducerImpl();
    }

    private CaptchaUtil() {
        throw new AssertionError("No instances");
    }

    /**
     * 生成验证码
     * @return 验证码字符串
     */
    public static Captcha generateCaptcha() {
        String captchaId = java.util.UUID.randomUUID().toString();
        String text = PRODUCER.createText();
        BufferedImage image = PRODUCER.createImage(text);
        return new Captcha(captchaId,text, image);
    }


    public static class Captcha {
        private final String captchaId;
        private final String captchaText;
        private final BufferedImage captchaImage;

        public Captcha(String captchaId, String captchaText, BufferedImage captchaImage) {
            this.captchaId = captchaId;
            this.captchaText = captchaText;
            this.captchaImage = captchaImage;
        }

        public String getCaptchaId() {
            return captchaId;
        }

        public String getCaptchaText() {
            return captchaText;
        }

        public BufferedImage getCaptchaImage() {
            return captchaImage;
        }
    }
}
