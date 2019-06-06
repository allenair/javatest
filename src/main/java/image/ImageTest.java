package image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageTest {

	public static void main(String[] args) throws Exception {
		BufferedImage simg = initBufferedImage("d:/a.png");

//		BufferedImage gimg = grayProcess(simg);
//		BufferedImage alimg = alphaProcess(simg);
//		BufferedImage bimg = binaryImage(simg); 
//		BufferedImage rimg = getRed(simg);
		
		BufferedImage rimg = img_color_contrast(simg,100);
		
//		BufferedImage bimg = binaryImage(rimg); 

//		writeBufferedImage(rimg, "d:/abc222.png");
		
		System.out.println("a.png==="+checkTable("d:/a.png"));
		System.out.println("a01.png==="+checkTable("d:/a01.png"));
		System.out.println("b.jpg==="+checkTable("d:/b.jpg"));
		System.out.println("cc.png==="+checkTable("d:/cc.png"));
		System.out.println("c5.png==="+checkTable("d:/c5.png"));
		System.out.println("c6.png==="+checkTable("d:/c6.png"));
		System.out.println("d3.png==="+checkTable("d:/d3.png"));
		System.out.println("b3.png==="+checkTable("d:/b3.png"));
		System.out.println("zx02.png==="+checkTable("d:/zx02.png"));
		
	}

	public static boolean checkTable(String fileName) throws Exception  {
		BufferedImage simg = initBufferedImage(fileName);
		int width = simg.getWidth();
		int height = simg.getHeight();
		
		int maxLength = 0;
		for (int i = 0; i < width - 50; i+=10) {
			BufferedImage tmp = simg.getSubimage(i, 0, 50, height);
			BufferedImage rimg = img_color_contrast(tmp, 100);
			BufferedImage bimg = binaryImage(rimg);

			int count = 0;
			for (int h = 0; h < height; h++) {
				boolean flag = false;
				for (int r = 0; r < 50; r++) {
					Color color = new Color(bimg.getRGB(r, h));
					if (color.getRed() + color.getGreen() + color.getBlue() < 100) {
						count++;
						flag = true;
						break;
					}
				}

				maxLength = maxLength < count ? count : maxLength;
				if (maxLength > height / 4) {
					return true;
				}

				if (!flag) {
					count = 0;
				}
			}
		}
		
		return false;
	}
	
	
	public static BufferedImage img_color_contrast(BufferedImage imgsrc, int contrast) {
		try {
			int contrast_average = 128;
			// 创建一个不带透明度的图片
			BufferedImage back = new BufferedImage(imgsrc.getWidth(), imgsrc.getHeight(), BufferedImage.TYPE_INT_RGB);
			int width = imgsrc.getWidth();
			int height = imgsrc.getHeight();
			int pix;
			for (int i = 0; i < height; i++) {
				for (int j = 0; j < width; j++) {
					int pixel = imgsrc.getRGB(j, i);
					Color color = new Color(pixel);

					if (color.getRed() < contrast_average) {
						pix = color.getRed() - Math.abs(contrast);
						if (pix < 0)
							pix = 0;
					} else {
						pix = color.getRed() + Math.abs(contrast);
						if (pix > 255)
							pix = 255;
					}
					int red = pix;
					if (color.getGreen() < contrast_average) {
						pix = color.getGreen() - Math.abs(contrast);
						if (pix < 0)
							pix = 0;
					} else {
						pix = color.getGreen() + Math.abs(contrast);
						if (pix > 255)
							pix = 255;
					}
					int green = pix;
					if (color.getBlue() < contrast_average) {
						pix = color.getBlue() - Math.abs(contrast);
						if (pix < 0)
							pix = 0;
					} else {
						pix = color.getBlue() + Math.abs(contrast);
						if (pix > 255)
							pix = 255;
					}
					int blue = pix;

					color = new Color(red, green, blue);
					int x = color.getRGB();
					back.setRGB(j, i, x);
				}
			}
			return back;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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
