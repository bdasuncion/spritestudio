package fileRW;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ExportToImage
{	
	public static void toPNG(BufferedImage img, File out) {
		try {
			ImageIO.write(img, "png", out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void toBMP(BufferedImage img, File out) {
		try {
			ImageIO.write(img, "bmp", out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void toJPG(BufferedImage img, File out) {
		try {
			ImageIO.write(img, "jpg", out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}