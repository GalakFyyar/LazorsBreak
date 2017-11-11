import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

class Drawer{
	private static int gridWidth;
	private static int gridHeight;
	private static int imageWidth;
	private static int imageHeight;
	
	private static BufferedImage bufferedImage;
	private static Graphics2D graphics;
	
	private static BufferedImage plbl;
	private static BufferedImage lazd0;
	private static BufferedImage lazd1;
	private static BufferedImage lazd2;
	private static BufferedImage lazd3;
	private static BufferedImage hold0;
	private static BufferedImage hold1;
	private static BufferedImage hold2;
	private static BufferedImage hold3;
	private static BufferedImage lzer0;
	private static BufferedImage lzer1;
	private static BufferedImage lzer2;
	private static BufferedImage lzer3;
	private static BufferedImage rflc;
	private static BufferedImage rfum;
	private static BufferedImage absb;
	private static BufferedImage abum;
	
	private static int imagesWritten = 0;
	
	static void init(int aGridWidth, int aGridHeight){
		gridWidth = aGridWidth;
		gridHeight = aGridHeight;
		imageWidth = gridWidth * 50;
		imageHeight = gridHeight * 50;
		//TYPE_INT_ARGB: 4 byte RGBA pixels
		bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
		graphics = bufferedImage.createGraphics();
		
		graphics.setBackground(new Color(0, 50, 80));
		
		try{
			plbl  = ImageIO.read(new File("res/plbl.png"));
			lazd0 = ImageIO.read(new File("res/lazd0.png"));
			lazd1 = ImageIO.read(new File("res/lazd1.png"));
			lazd2 = ImageIO.read(new File("res/lazd2.png"));
			lazd3 = ImageIO.read(new File("res/lazd3.png"));
			hold0 = ImageIO.read(new File("res/hold0.png"));
			hold1 = ImageIO.read(new File("res/hold1.png"));
			hold2 = ImageIO.read(new File("res/hold2.png"));
			hold3 = ImageIO.read(new File("res/hold3.png"));
			lzer0 = ImageIO.read(new File("res/lzer0.png"));
			lzer1 = ImageIO.read(new File("res/lzer1.png"));
			lzer2 = ImageIO.read(new File("res/lzer2.png"));
			lzer3 = ImageIO.read(new File("res/lzer3.png"));
			rflc  = ImageIO.read(new File("res/rflc.png"));
			rfum  = ImageIO.read(new File("res/rfum.png"));
			absb  = ImageIO.read(new File("res/absb.png"));
			abum  = ImageIO.read(new File("res/abum.png"));
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	static void draw(Cell[][] grid){
		graphics.clearRect(0, 0, imageWidth, imageHeight);
		for(int i = 0; i < gridWidth; i++){
			for(int j = 0; j < gridHeight; j++){
				int xi = i * 50;
				int yi = j * 50;
				
				Cell c = grid[i][j];
				if(c.plbl){
					graphics.drawImage(plbl, null, xi, yi);
				}
				
				if(c.lazd){
					if(c.dpos[0])
						graphics.drawImage(lazd0, null, xi, yi - 8);
					if(c.dpos[1])
						graphics.drawImage(lazd1, null, xi - 8, yi);
					if(c.dpos[2])
						graphics.drawImage(lazd2, null, xi, yi);
					if(c.dpos[3])
						graphics.drawImage(lazd3, null, xi, yi);
				}
				
				if(c.hole){
					if(c.hpos[0])
						graphics.drawImage(hold0, null, xi, yi);
					if(c.hpos[1])
						graphics.drawImage(hold1, null, xi, yi);
					if(c.hpos[2])
						graphics.drawImage(hold2, null, xi, yi);
					if(c.hpos[3])
						graphics.drawImage(hold3, null, xi, yi);
				}
				
				if(c.zero)
					graphics.drawImage(lzer0, null, xi, yi);
				if(c.frst)
					graphics.drawImage(lzer1, null, xi, yi);
				if(c.scnd)
					graphics.drawImage(lzer2, null, xi, yi);
				if(c.thrd)
					graphics.drawImage(lzer3, null, xi, yi);
				
				if(c.occu){
					if(c.rflc){
						if(c.mvbl)
							graphics.drawImage(rflc, null, xi, yi);
						else
							graphics.drawImage(rfum, null, xi, yi);
					}else{
						if(c.mvbl)
							graphics.drawImage(absb, null, xi, yi);
						else
							graphics.drawImage(abum, null, xi, yi);
					}
				}
			}
		}
		try{
			ImageIO.write(bufferedImage, "PNG", new File("sol/solution" + imagesWritten + ".png"));
		}catch(IOException e){
			e.printStackTrace();
		}
		imagesWritten++;
	}
}
