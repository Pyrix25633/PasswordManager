package net.segmentation_four.password_manager.resource;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

/**
 * Class for handling resource loading
 * @author Segmentation Four
 */
public class ResourceLoader {
    // Constants

    private static final HashMap<String, BufferedImage> IMAGES = new HashMap<>();

    // Public static methods

    /**
     * Loads an image file
     * @param file The file path
     * @return The BufferedImage
     * @throws IOException IOException
     */
    public static BufferedImage loadImage(String file, boolean fromResources) throws IOException {
        BufferedImage image = IMAGES.get(file);
        if(image == null) {
            image = fromResources ?
                    ImageIO.read(Objects.requireNonNull(ResourceLoader.class.getClassLoader().getResource(file))) :
                    ImageIO.read(new File(file));
            IMAGES.put(file, image);
        }
        return image;
    }

    /**
     * Load an image file and scales it
     * @param file The file path
     * @param scale The scale
     * @return The scaled BufferedImage
     * @throws IOException IOException
     */
    public static @NotNull BufferedImage loadImage(String file, boolean fromResources, int scale) throws IOException {
        BufferedImage image = loadImage(file, fromResources);
        BufferedImage scaledImage = new BufferedImage(image.getWidth() * scale, image.getHeight() * scale,
                BufferedImage.TYPE_INT_RGB);
        for(int x = 0; x < image.getWidth(); x++) {
            for(int y = 0; y < image.getHeight(); y++) {
                int color = image.getRGB(x, y);
                for(int sx = 0; sx < scale; sx++)
                    for(int sy = 0; sy < scale; sy++)
                        scaledImage.setRGB(x * scale + sx, y * scale + sy, color);
            }
        }
        return scaledImage;
    }
}