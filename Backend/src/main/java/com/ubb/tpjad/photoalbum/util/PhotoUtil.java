package com.ubb.tpjad.photoalbum.util;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class PhotoUtil {
    public static InputStream simpleResizeImage(BufferedImage originalImage, int targetWidth, String format) throws IOException {
        BufferedImage resizedImage = Scalr.resize(originalImage, targetWidth);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resizedImage, format, baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }
}
