package org.plain.utils.captcha;

import com.google.code.kaptcha.impl.DefaultNoise;
import com.google.code.kaptcha.impl.WaterRipple;
import com.google.code.kaptcha.util.Config;
import com.google.code.kaptcha.util.Configurable;
import org.testng.annotations.Test;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CaptchaUtilTest {

    @Test
    public void generatedCaptchaPreservesTextAndImageContract() throws Exception {
        for (int i = 0; i < 12; i++) {
            CaptchaUtil.Captcha captcha = CaptchaUtil.generateCaptcha();

            assert UUID.fromString(captcha.getCaptchaId()).toString().equals(captcha.getCaptchaId());
            assert captcha.getCaptchaText().matches("[ABCDEFGHJKLMNPQRSTUVWXYZ23456789]{5}");
            assert captcha.getCaptchaImage().getWidth() == 160;
            assert captcha.getCaptchaImage().getHeight() == 60;

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            boolean encoded = ImageIO.write(captcha.getCaptchaImage(), "png", output);
            assert encoded;
            BufferedImage decoded = ImageIO.read(new ByteArrayInputStream(output.toByteArray()));
            assert decoded != null;
            assert decoded.getWidth() == 160;
            assert decoded.getHeight() == 60;
        }
    }

    @Test
    public void generatedCaptchasHaveDistinctIds() {
        Set<String> ids = new HashSet<>();
        for (int i = 0; i < 16; i++) {
            boolean added = ids.add(CaptchaUtil.generateCaptcha().getCaptchaId());
            assert added;
        }
    }

    @Test
    public void lighterNoiseRetainsDistortionAndLeavesCharacterSpacing() throws Exception {
        Field field = CaptchaUtil.class.getDeclaredField("PRODUCER");
        field.setAccessible(true);
        Config config = ((Configurable) field.get(null)).getConfig();

        assert new Color(190, 190, 190).equals(config.getNoiseColor());
        assert Color.BLACK.equals(config.getTextProducerFontColor());
        assert config.getNoiseImpl() instanceof DefaultNoise;
        assert config.getObscurificatorImpl() instanceof WaterRipple;
        assert config.getTextProducerFontSize() == 26;
        assert config.getTextProducerCharSpace() == 2;
    }

    @Test
    public void widestConfiguredCharactersFitImageWidth() throws Exception {
        Field field = CaptchaUtil.class.getDeclaredField("PRODUCER");
        field.setAccessible(true);
        Config config = ((Configurable) field.get(null)).getConfig();
        FontRenderContext context = new FontRenderContext(null, true, false);
        int maximumGlyphWidth = 0;
        for (Font font : config.getTextProducerFonts(config.getTextProducerFontSize())) {
            for (char character : config.getTextProducerCharString()) {
                int width = (int) font.createGlyphVector(context, new char[]{character}).getVisualBounds().getWidth();
                maximumGlyphWidth = Math.max(maximumGlyphWidth, width);
            }
        }
        int length = config.getTextProducerCharLength();
        int textWidth = maximumGlyphWidth * length + config.getTextProducerCharSpace() * (length - 1);

        assert textWidth <= config.getWidth() - 8 : "宽字符需要留出边距，实际文字宽度：" + textWidth;
    }
}
