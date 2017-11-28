package start;

import java.awt.image.BufferedImage;

import javax.swing.JLabel;

public class THPicture
{

	//protected ImageRectangle image;
	protected BufferedImage image;
	protected JLabel label;
	
	/*
	THPicture(ImageRectangle image, JLabel label) 
	{
		this.image = image;
		this.label = label;
	}
	*/
	
	THPicture(BufferedImage image, JLabel label)
	{
		this.image = image;
		this.label = label;
	}
	
	THPicture(THPicture picture) 
	{
		this.image = picture.image;
		this.label = picture.label;
	}
	
	//public ImageRectangle getImage() { return image; }
	
	public BufferedImage getImage() { return image; }
	public JLabel getLabel() { return label; }
	
	
	
}
