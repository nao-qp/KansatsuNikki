package plant.spring.application.util;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.springframework.web.multipart.MultipartFile;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;

// 画像加工メソッドクラス
public class ImageUtils {
	/**
     * アップロード画像を正しい向きに補正し、
     * 正方形にトリミングして600x600にリサイズし、
     * 高画質でJPEG保存する。
     * @param file アップロードされたファイル
     * @param destinationFile 保存先ファイル
     * @throws IOException
     */
    public static void saveResizedImage(MultipartFile file, File destinationFile) throws IOException {
        BufferedImage originalImage = ImageIO.read(file.getInputStream());

        // 1. Exifの回転補正（スマホ写真の向きを正す）
        originalImage = applyExifOrientationCorrection(file, originalImage);

        // 2. 正方形にトリミング（中央部分）
        BufferedImage cropped = cropToCenterSquare(originalImage);

        // 3. 1080x1080にリサイズ
        BufferedImage resized = resizeImage(cropped, 1080, 1080);

        // 4. 高画質JPEGで保存（95%品質）
        writeJpegWithQuality(resized, destinationFile, 0.8f);
    }

    /**
     * 画像の中心を基準に、正方形にトリミングする
     */
    private static BufferedImage cropToCenterSquare(BufferedImage original) {
        int width = original.getWidth();
        int height = original.getHeight();
        int squareSize = Math.min(width, height);
        int x = (width - squareSize) / 2;
        int y = (height - squareSize) / 2;
        return original.getSubimage(x, y, squareSize, squareSize);
    }

    /**
     * 指定サイズにリサイズ（高品質設定）
     */
    private static BufferedImage resizeImage(BufferedImage inputImage, int width, int height) {
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(inputImage, 0, 0, width, height, null);
        g2d.dispose();
        return resized;
    }

    /**
     * JPEGを画質指定で保存（0.0〜1.0）
     */
    private static void writeJpegWithQuality(BufferedImage image, File file, float quality) throws IOException {
        ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam param = writer.getDefaultWriteParam();
        param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        param.setCompressionQuality(quality);

        try (ImageOutputStream ios = ImageIO.createImageOutputStream(file)) {
            writer.setOutput(ios);
            writer.write(null, new IIOImage(image, null, null), param);
        } finally {
            writer.dispose();
        }
    }

    /**
     * Exifの回転情報に従って画像を正しい向きに補正
     */
    private static BufferedImage applyExifOrientationCorrection(MultipartFile file, BufferedImage image) throws IOException {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(file.getInputStream());
            Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);

            if (directory != null && directory.containsTag(ExifIFD0Directory.TAG_ORIENTATION)) {
                int orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);

                int width = image.getWidth();
                int height = image.getHeight();

                AffineTransform transform = new AffineTransform();
                int newWidth = width;
                int newHeight = height;

                switch (orientation) {
                    case 6: // 回転90度
                        transform.translate(height, 0);
                        transform.rotate(Math.toRadians(90));
                        newWidth = height;
                        newHeight = width;
                        break;
                    case 3: // 回転180度
                        transform.translate(width, height);
                        transform.rotate(Math.toRadians(180));
                        break;
                    case 8: // 回転270度
                        transform.translate(0, width);
                        transform.rotate(Math.toRadians(270));
                        newWidth = height;
                        newHeight = width;
                        break;
                    default:
                        return image; // 補正不要
                }

                BufferedImage rotatedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2d = rotatedImage.createGraphics();
                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2d.drawImage(image, transform, null);
                g2d.dispose();

                return rotatedImage;
            }
        } catch (Exception e) {
            System.err.println("Exifの読み取りに失敗しました: " + e.getMessage());
        }

        return image;
    }
}
