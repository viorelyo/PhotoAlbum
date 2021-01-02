package com.ubb.tpjad.photoalbum.util;

import org.imgscalr.Scalr;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ImagingOpException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class PhotoUtil {
    public static byte[] toByteArray(BufferedImage bi, String format) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        return baos.toByteArray();
    }

    public static BufferedImage simpleResizeImage(BufferedImage originalImage, int targetWidth) throws IllegalArgumentException, ImagingOpException {
        return Scalr.resize(originalImage, targetWidth);
    }
}
