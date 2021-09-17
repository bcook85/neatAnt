package display;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Spritesheet {
	
	private ArrayList<BufferedImage> images;
	private BufferedImage sheet;
	private int frameWidth;
	private int frameHeight;
	private int frameColumns;
	private int frameRows;

	public Spritesheet(String filePath, int frameWidth, int frameHeight) {
		try {
			sheet = ImageIO.read(getClass().getClassLoader().getResourceAsStream(filePath));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		images = new ArrayList<BufferedImage>();
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		frameColumns = (int)Math.floor(sheet.getWidth() / frameWidth);
		frameRows = (int)Math.floor(sheet.getHeight() / frameHeight);
		for (int x = 0; x < frameColumns; x++) {
			for (int y = 0; y < frameRows; y++) {
				images.add(sheet.getSubimage(
					x * this.frameWidth
					,y * this.frameHeight
					,this.frameWidth
					,this.frameHeight
				));
			}
		}
	}
	
	public BufferedImage getImage(int index) {
		if (index >= 0 && index < images.size()) {
			return images.get(index);
		}
		return null;
	}
	
	public int getFrameWidth() {
		return frameWidth;
	}
	
	public int getFrameHeight() {
		return frameHeight;
	}
}
