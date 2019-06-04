package image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageTest {

	public static void main(String[] args) throws Exception  {
		BufferedImage simg = initBufferedImage("e:/a.png");
		
//		BufferedImage gimg = grayProcess(simg);
//		BufferedImage alimg = alphaProcess(simg);
//		BufferedImage bimg = binaryImage(simg); 
		
		BufferedImage rimg = getRed(simg); 

		
		
		writeBufferedImage(rimg, "e:/a3.png");
	}

	private static BufferedImage initBufferedImage(String imagePath) {
		File file = new File(imagePath);
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	private static BufferedImage getRed(BufferedImage sourceImage) {
		int width = sourceImage.getWidth();
		int height = sourceImage.getHeight();
		BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = sourceImage.getRGB(i, j);
//				int r = (0xff & rgb);
//				int g = (0xff & (rgb >> 8));
//				int b = (0xff & (rgb >> 16));
				rgb = rgb & 0xff0000;
				grayImage.setRGB(i, j, rgb);
			}
		}
		return grayImage;
	}
	
	private static BufferedImage grayProcess(BufferedImage sourceImage) {
		int width = sourceImage.getWidth();
		int height = sourceImage.getHeight();
		BufferedImage grayImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);// BufferedImage.TYPE_BYTE_GRAY指定了这是一个灰度图片
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = sourceImage.getRGB(i, j);
				grayImage.setRGB(i, j, rgb);
			}
		}
		return grayImage;
	}

	private static void writeBufferedImage(BufferedImage img, String filePath) {
		String format = filePath.substring(filePath.indexOf('.') + 1);
		// 获取图片格式
		System.out.println(format);
		try {
			ImageIO.write(img, format, new File(filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static BufferedImage alphaProcess(BufferedImage bufferedImage) {
		// 获取源图像的宽高
		int width = bufferedImage.getWidth();
		int height = bufferedImage.getHeight();
		System.out.println(width + " " + height);
		// 实例化一个同样大小的图片，并将type设为 BufferedImage.TYPE_4BYTE_ABGR，支持alpha通道的rgb图像
		BufferedImage resImage = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);

		double grayMean = 0;
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = bufferedImage.getRGB(i, j);
				int r = (0xff & rgb);
				int g = (0xff & (rgb >> 8));
				int b = (0xff & (rgb >> 16));
				// 这是灰度值的计算公式
				grayMean += (r * 0.299 + g * 0.587 + b * 0.114);
			}
		}
		// 计算平均灰度
		grayMean = grayMean / (width * height);

		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = bufferedImage.getRGB(i, j);
				// 一个int是32位,java中按abgr的顺序存储，即前8位是alpha，最后8位是r，所以可以通过下面的方式获取到rgb的值
				int r = (0xff & rgb);
				int g = (0xff & (rgb >> 8));
				int b = (0xff & (rgb >> 16));
				double gray = (r * 0.299 + g * 0.587 + b * 0.114);
				// 如果灰度值大于之前求的平均灰度值，则将其alpha设为0，下面准确写应该是rgb = r + (g << 8) + (b << 16) ＋ （0 <<
				// 24）;
				if (gray > grayMean) {
					rgb = r + (g << 8) + (b << 16);
				}
				resImage.setRGB(i, j, rgb);
			}
		}
		// ok，返回的就是将浅色背景设为透明的BufferedImage了，可以用灰度化里提到的方式写成文件
		return resImage;
	}

	public static BufferedImage binaryImage(BufferedImage image) throws Exception {
		int w = image.getWidth();
		int h = image.getHeight();
		float[] rgb = new float[3];
		double[][] zuobiao = new double[w][h];
		int black = new Color(0, 0, 0).getRGB();
		int white = new Color(255, 255, 255).getRGB();
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
		;
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				int pixel = image.getRGB(x, y);
				rgb[0] = (pixel & 0xff0000) >> 16;
				rgb[1] = (pixel & 0xff00) >> 8;
				rgb[2] = (pixel & 0xff);
				float avg = (rgb[0] + rgb[1] + rgb[2]) / 3;
				zuobiao[x][y] = avg;

			}
		}
		double SW = 192;
		for (int x = 0; x < w; x++) {
			for (int y = 0; y < h; y++) {
				if (zuobiao[x][y] < SW) {
					bi.setRGB(x, y, black);
				} else {
					bi.setRGB(x, y, white);
				}
			}
		}

		return bi;
	}

}
