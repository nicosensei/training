/**
 * 
 */
package fr.nikokode.img;

import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * @author ngiraud
 *
 */
public class Transformations
{
 
 
	public static BufferedImage noise(
			BufferedImage img, 
			int quantity, 
			int threshold) {
		
		BufferedImage dest = new BufferedImage(
				img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
 
		//good values are
		//quantity = 10;
		//threshold = 50;
 
		Random r = new Random();
 
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int px = img.getRGB(x, y);
 
				int ran = r.nextInt(quantity);
				if (ran <= 1) {
 
					int amount = r.nextInt(threshold);
					int red = ((px >> 16) & 0xFF) + amount;
 
					amount = r.nextInt(threshold);
					int green = ((px >> 8) & 0xFF) + amount;	
 
					amount = r.nextInt(threshold);			
					int blue = (px & 0xFF) + amount;
 
					//Overflow fix
					if (red > 255) { red = 255; }
					if (green > 255) { green = 255; }
					if (blue > 255) { blue = 255; }
 
					px = (0xFF<<24) + (red<<16) + (green<<8) + blue;
				}
 
				dest.setRGB(x, y, px);
			}
		}
 
		return dest;
	}
 
 	public static BufferedImage pixelate(BufferedImage img, int size) {
		BufferedImage dest = new BufferedImage(
				img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
 
		for (int x = 0; x < img.getWidth(); x+=size) {
			for (int y = 0; y < img.getHeight(); y+=size) {
 
				int px = 0;
 
				for (int xi = 0; xi < size; xi++) {
					for (int yi = 0; yi < size; yi++) {
						px += img.getRGB(x, y);
						px = px / 2;
					}
				}
 
				for (int xi = 0; xi < size; xi++) {
					for (int yi = 0; yi < size; yi++) {
						dest.setRGB(x+xi, y+yi, px);
					}
				}
			}
		}
 
		return dest;
	}
 	
	public static BufferedImage histogramThreshold(BufferedImage img, int threshold) {
 
		BufferedImage dest = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
 
		int reds[] = new int[256];
		int greens[] = new int[256];
		int blues[] = new int[256];
 
 
		//Count the occurance of each pixel's red, green and blue
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int px = img.getRGB(x, y);
 
 
				int red = ((px >> 16) & 0xFF);
				reds[red]++;
 
				int green = ((px >> 8) & 0xFF);
				greens[green]++;
 
				int blue = (px & 0xFF);
				blues[blue]++;
 
				dest.setRGB(x, y, px);
			}
		}
 
		//Analyse the results
		int mostCommonRed = 0;
		int mostCommonBlue = 0;
		int mostCommonGreen = 0;
 
		for (int i = 0; i < 256; i++) {
			if (reds[i] > mostCommonRed) {
				mostCommonRed = i;
			}
 
			if (blues[i] > mostCommonBlue) {
				mostCommonBlue = i;
			}
 
			if (greens[i] > mostCommonGreen) {
				mostCommonGreen = i;
			}
		}
 
		//Set the destination to pixels that are in a range +/- threshold from mostCommon value
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int px = img.getRGB(x, y);
 
				int red = ((px >> 16) & 0xFF);
				int green = ((px >> 8) & 0xFF);				
				int blue = (px & 0xFF);
				int val = 0;
 
 
				if (((red - 20 < mostCommonRed) && (red + threshold > mostCommonRed)) || ((blue - threshold < mostCommonBlue) && (blue + threshold > mostCommonBlue)) || ((green - threshold < mostCommonGreen) && (green + threshold > mostCommonGreen))) {
					val = (0xFF<<24) + (red<<16) + (green<<8) + blue;
				} else {
					val = (0xFF<<24) + (0xFF<<16) + (0xFF<<8) + 0xFF;
				}
 
 
				dest.setRGB(x, y, val);
			}
		}
 
		return dest;
	}
 
	public static BufferedImage invert(BufferedImage img) {
		BufferedImage dest = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
 
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int px = img.getRGB(x, y);
 
				//Subtracting the channels value from 0xFF effectively inverts it
				int red = 0xFF - ((px >> 16) & 0xFF);
				int green = 0xFF - ((px >> 8) & 0xFF);
				int blue = 0xFF - (px & 0xFF);
 
				int inverted = (0xFF<<24) + (red<<16) + (green<<8) + blue;
				dest.setRGB(x, y, inverted);
			}
		}
 
		return dest;
	}
 
 
 
	public static BufferedImage greyScale(BufferedImage img) {
		BufferedImage dest = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
 
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
 
				int px = img.getRGB(x, y);
 
				int alpha = (px >> 24) & 0xFF;
				int red = (px >> 16) & 0xFF;
				int green = (px >> 8) & 0xFF;
				int blue = px & 0xFF;
 
				//average of RGB
				int avg = (red + blue + green) / 3;
 
				//set R, G & B with avg color
				int grey = (alpha<<24) + (avg<<16) + (avg<<8) + avg;
 
				dest.setRGB(x, y, grey);
			}
		}
 
		return dest;
	}
 
 
	public static BufferedImage burn(BufferedImage img) {
		BufferedImage dest = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
 
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int px = img.getRGB(x, y);
 
				int burn = px << 8; //this was a lucky guess. not sure why it works
 
				dest.setRGB(x, y, burn);
			}
		}
 
		return dest;
	}
 
 
	public static BufferedImage gaussianFilter(BufferedImage img) {
		int cuttoff = 2000;
		double magic = 1.442695;
		int xcenter = img.getWidth() / 2 - 1;
		int ycenter = img.getHeight() / 2 - 1;
 
		BufferedImage dest = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
 
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int px = img.getRGB(x, y);
 
				double distance = Math.sqrt(x*x+y*y);
				double value = px*255*Math.exp((-1*distance*distance)/(magic*cuttoff*cuttoff));
				dest.setRGB(x, y, (int) value);	
 
			}
		}
 
		return dest;
 
	}
 
 
	public static BufferedImage flipVerticalHorizontal(BufferedImage img) {
		BufferedImage dest = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
 
		//Flip vertical and horizontal
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int px = img.getRGB(x, y);
				int destX = img.getWidth() - x - 1;
				int destY = img.getHeight() - y - 1;
				dest.setRGB(destX, destY, px);			
			}
		}
 
		return dest;
	}
 
 
 
	public static BufferedImage flipVertical(BufferedImage img) {
		BufferedImage dest = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
 
		//Flip vertical and horizontal
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int px = img.getRGB(x, y);
				dest.setRGB(x, img.getHeight() - y - 1, px);			
			}
		}
 
		return dest;
	}
 
 
	public static BufferedImage flipHorizontal(BufferedImage img) {
		BufferedImage dest = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
 
		//Flip horizontal
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int px = img.getRGB(x, y);
				dest.setRGB(img.getWidth() - x - 1, y, px);			
			}
		}
 
		return dest;
	}
 
}
