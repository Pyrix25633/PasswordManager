package net.segmentation_four.password_manager.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;

public class ResourceLoader {
    private static final HashMap<String, BufferedImage> images = new HashMap<>();

    /**
     * Static method to load an image file
     * @param file The file path
     * @return The BufferedImage
     */
    public static BufferedImage loadImage(String file) {
        BufferedImage image = images.get(file);
        if(image == null)
            try {
                image = ImageIO.read(new File(file));
                images.put(file, image);
            } catch(Exception ignored) {}
        return image;
    }
}