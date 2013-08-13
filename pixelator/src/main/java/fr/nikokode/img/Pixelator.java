/**
 * 
 */
package fr.nikokode.img;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import fr.nikokode.commons.cmd.CommandOption;
import fr.nikokode.commons.cmd.CommandOptions;

/**
 * @author ngiraud
 *
 */
public class Pixelator {
	
	public static final CommandOption INPUT = 
			new CommandOption("i", true, true, "Input file");
	public static final CommandOption PIXEL_BLOCK_SIZE = 
			new CommandOption("p", true, false, "Pixelation block size");

	public static final void main(String[] args) throws IOException {
		
		CommandOptions opts = new CommandOptions();
		opts.addOption(INPUT);
		opts.addOption(PIXEL_BLOCK_SIZE);
		
		CommandLine cmdLine = null; 
		try {
			cmdLine = opts.parseParameters(args);
		} catch (ParseException e) {
			System.out.println(opts.usage(Pixelator.class.getCanonicalName()));
			System.exit(0);
		}
		
		File sourcePath = new File(cmdLine.getOptionValue(INPUT.getOpt()).trim());
		BufferedImage source = ImageIO.read(sourcePath);
		System.out.println("Loaded source image " + sourcePath.getName() 
				+ " " + source.getWidth() + "x" + source.getHeight());
		
		int blockSize = Integer.parseInt(
				cmdLine.getOptionValue(PIXEL_BLOCK_SIZE.getOpt(), "8").trim());		
		BufferedImage pixelated = Transformations.pixelate(source, blockSize);		
		System.out.println("Pixelated with block size of " + blockSize);
		
		// Save as PNG
        String outPath = sourcePath.getAbsolutePath().substring(
        		0, sourcePath.getAbsolutePath().lastIndexOf(".")) 
        		+ "px" + blockSize + ".png";
        File out = new File(outPath);
        ImageIO.write(pixelated, "png", out);
		
        System.out.println("Wrote " + out.getAbsolutePath());
        
        System.exit(0);
	}

}
