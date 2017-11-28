package start;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MiniaturePanel extends JPanel
{
	// File representing the folder that you select
	static final File dir = new File("C:/Users/aleks/eclipse-workspace/Assignment_D/images");

	    
	// Array of supported extensions (use a List if you prefer)
	static final String[] EXTENSIONS = new String[] { "gif", "png", "bmp", "jpg" };
	    
	// Filter to identify images based on their extensions
	static final FilenameFilter IMAGE_FILTER = new FilenameFilter() 
	{
		@Override
	    public boolean accept(final File dir, final String name) 
		{
			for (final String ext : EXTENSIONS) 
			{
				if (name.endsWith("." + ext)) 
				{
					return (true);
	            }
	        }
	        
			return (false);
	    }
	 };
	 
	    
	    
	 // File list sorted with filter
	 File[] list = dir.listFiles(IMAGE_FILTER);
	
	 //ArrayList<ImageRectangle> images;
	 //ArrayList<JLabel> labels;
	 
	 Map<Integer, JLabel> labels = new HashMap<Integer, JLabel>();
	 //Map<Integer, ImageRectangle> images = new HashMap<Integer, ImageRectangle>();
	 Map<Integer, BufferedImage> images = new HashMap<Integer, BufferedImage>();
	
	 ImageBuffer buffer;
	 
	MiniaturePanel()
	{
		setLayout(new GridLayout(0,1,8,8));
		setBackground(Color.GRAY);
		
        //images = new ArrayList<ImageRectangle>();
        //labels = new ArrayList<JLabel>();
        

        // Upload images from folder
        uploadImages();

        /*
        for(JLabel label : labels)
        {
        	add(label);
        }
        */
        
        for(Map.Entry<Integer, JLabel> entry : labels.entrySet())
        {
        	add(entry.getValue());
        }
        		
	}
	
	private void uploadImages () 
	{
		
		if (dir.isDirectory()) // make sure it's a directory
		{ 
			int counter = 0;
			
            for (final File f : list) 
            {
				BufferedImage img = null;
				
				try
				{
					//img = ImageIO.read(new File("C:/Users/aleks/eclipse-workspace/Test_Image_Miniatures/src/images/sunw01.jpg"));
					img = ImageIO.read(f);
					System.out.println("image " + f.getName() + " loaded successfully");
				}
				catch (IOException e)
				{
					System.out.println("The image cannot be loaded");
					System.exit(-1);
				}
				
				//ImageRectangle image = new ImageRectangle(img, 0, 0);
				
				//images.add(image);
				
				//images.put(counter, image);
				images.put(counter, img);
				
				ImageIcon icon = new ImageIcon(getScaledImage(img,120,80));
				
				JLabel label = new JLabel();
				
				label.setIcon(icon);
				
				//labels.add(label);
				
				labels.put(counter, label);
				
				++counter;
            }
		}
	} 
	
    private Image getScaledImage(Image srcImg, int w, int h)
    {
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();
        return resizedImg;
    }
    
    public int getIndexOfLabel(JLabel label) throws Exception
    {
    	for (Map.Entry<Integer, JLabel> entry : labels.entrySet()) 
    	{
            if (entry.getValue().equals(label)) 
            {
            	System.out.println("found key" + entry.getKey());
                return entry.getKey();
            }
        }
    	throw new Exception("No label match found in labels");
    }
    
    /*
    public ImageRectangle getImageByKey (int key) throws Exception
    {
    	for(Map.Entry<Integer, ImageRectangle> entry : images.entrySet())
    	{
    		if(entry.getKey().equals(key))
    		{
    			System.out.println("found image");
    			return entry.getValue();
    		}
    	}
    	throw new Exception("No image found by this key");
    }
    */
    public BufferedImage getImageByKey (int key) throws Exception
    {
    	for(Map.Entry<Integer, BufferedImage> entry : images.entrySet())
    	{
    		if(entry.getKey().equals(key))
    		{
    			//System.out.println("found image");
    			return entry.getValue();
    		}
    	}
    	throw new Exception("No image found by this key");
    }
    
    public JLabel getLabelByKey(int key) throws Exception
    {
    	for(Map.Entry<Integer, JLabel> entry : labels.entrySet())
    	{
    		if(entry.getKey().equals(key))
    		{
    			//System.out.println("found lable");
    			return entry.getValue();
    		}
    	}
    	throw new Exception("No lable found by this key");
    }
    
   
	public void setImageBuffer(ImageBuffer buffer)
	{
		this.buffer = buffer;
	}
    
}
