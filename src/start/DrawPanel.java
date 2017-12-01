package start;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class DrawPanel extends JPanel {
	
	static public final int  SENS_DIST = 10;
	
	
	ArrayList<ImageRectangle> images; 
	ImageRectangle picture_caught = null;
	//Array to store all shapes
	ArrayList<Shape> shapes = new ArrayList<Shape>();
	
	//Array to store colors of shapes
	ArrayList<Color> colors = new ArrayList<Color>();
	
	AffineTransform transform, inverse;
	ImageBuffer buffer;
	Color currentColor;
	Color backgroundColor;
	
	int shape_caught = -1;
	ShapePoint point_caught = ShapePoint.POINT_NONE;
	
	// Coordinates of the point
	double xp,yp;
	
	// End points coordinates
	int x1, y1, x2, y2;
	
	int m_press_x = -100; 
	int m_press_y = -100;
	
	DrawPanel()
	{
		super();
        setBackground   (Color.WHITE);
        setPreferredSize (new Dimension(800,600));
        setLayout(null);
        
        images = new ArrayList<ImageRectangle>();
        
        // Here we use common transformation matrix; in case
        // of the image consisting of many elements you need to store matrices
        // associated with individual shapes
        transform = new AffineTransform( 1, 0, /* 0 */
                                         0, 1, /* 0 */
                                         0, 0 /* 1 */);
        try
        {
           inverse = transform.createInverse();
        }
        catch (Exception x)
        {
        }
        
	    // Set the initial color
	    //currentColor = getCurrentColor();
 
        
	}
	
	public void paintComponent(Graphics g)
	{
		System.out.println("PAINT COMPINENT CALLED");
		Graphics2D g2d = (Graphics2D) g;
	    super.paintComponent(g);   
	    
        // Draw all component shapes 
	    
	    
	    for(ImageRectangle img : images)
	    {
	    	//System.out.println("img x,y = (" + img.x_rect_pos + "," + img.y_rect_pos + ")");
	    	//System.out.println("img rect x,y = (" + img.x + "," + img.y + ")");
	        img.drawWhole( g2d ); 
	    }
	    
		//g2d.setColor( Color.black );       
		g2d.setXORMode( getBackground() ); 
		
		// Redraw all shapes in the array
		for(int i = 0; i < shapes.size(); ++i)
		{
			g2d.setColor(colors.get(i));
			g2d.draw(shapes.get(i));
		}
		//g2d.setColor(currentColor);
		g2d.setPaintMode();
	    
   
	}
	
	

	
	
	
	public void addImage(ImageRectangle img) throws Exception
	{
		//System.out.println("new ImgRect (x,y) = (" + img.x_rect_pos + ", " + img.y_rect_pos + ")");
		try 
		{
			images.add(img);
			System.out.println("image added successfully. Number of images is = " + images.size());
		}
		catch(Exception ex)
		{
			System.out.println("Failed to add image");
		}
		
		//System.out.println("images size = " + images.size());
		repaint();
	}
	
	
	public void setImageBuffer(ImageBuffer buffer)
	{
		this.buffer = buffer;
	}
	
	// Point catch detector (end points, center)
	boolean CatchShapePoint( double  x, double y )
	{
		// Search through the array to detect which shape was caught
		for(int i = 0; i < shapes.size(); i++)
		{

			// If the shape type is the line
			if(shapes.get(i) instanceof Line2D.Double)			
			{
				// Get the reference to the line in array 
				Line2D.Double line = (Line2D.Double)shapes.get(i);

				// Catch x1 y1 point 
				if((Math.abs( line.x1 - x) < 10) && (Math.abs( line.y1 - y) < 10))
				{
					//System.out.println("Line x1 point was caught");
					shape_caught = i;
					point_caught = ShapePoint.POINT_LINE_X1Y1;
					return true;
				}
				
				// Catch x2 y2 point 
				if((Math.abs(  line.x2 - x) < 10) && (Math.abs( line.y2 - y) < 10))
				{
					//System.out.println("Line x2 point was caught");
					shape_caught = i;
					point_caught = ShapePoint.POINT_LINE_X2Y2;
					return true;
				}
	
		    	// Catch center point
				double xc = 0.5 * (line.x1 + line.x2);
		    	double yc = 0.5 * (line.y1 + line.y2);    	
		    	
		    	if ( (Math.abs( xc - x) < 10) && (Math.abs( yc - y) < 10) )
		    	{
		    		//System.out.println("Line center point was caught");
		    		shape_caught = i;
		    		point_caught = ShapePoint.POINT_LINE_CENTER;
		    		return true;
		    	}   
			}
			
			// If the shape type is rectangle
			else if(shapes.get(i) instanceof Rectangle2D.Double)
			{
				// Get the reference to the line in array 
				Rectangle2D.Double rectangle = (Rectangle2D.Double)shapes.get(i);

				// Catch upper left point
				if( (Math.abs(rectangle.x - x) < 10)  && (Math.abs(rectangle.y - y) < 10))
				{
					System.out.println("Rectangle upper left  point was caught");
					shape_caught = i;
					point_caught = ShapePoint.POINT_RECTANGLE_UPPER_LEFT;
					return true;
				}
				
				// Catch upper right point
				if( (Math.abs(rectangle.x+ rectangle.width - x) < 10)  && (Math.abs(rectangle.y - y) < 10))
				{
					System.out.println("Rectangle upper right  point was caught");
					shape_caught = i;
					point_caught = ShapePoint.POINT_RECTANGLE_UPPER_RIGHT;
					return true;
				}
				
				// Catch lower left point
				if( (Math.abs(rectangle.x - x) < 10)  && (Math.abs(rectangle.y + rectangle.height - y) < 10))
				{
					System.out.println("Rectangle lower left  point was caught");
					shape_caught = i;
					point_caught = ShapePoint.POINT_RECTANGLE_LOWER_LEFT;
					return true;
				}
				
				
				// Catch lower right point
				if( (Math.abs(rectangle.x + rectangle.width - x) < 10)  && (Math.abs(rectangle.y + rectangle.height - y) < 10))
				{
					System.out.println("Rectangle lower right  point was caught");
					shape_caught = i;
					point_caught = ShapePoint.POINT_RECTANLGE_LOWER_RIGHT;
					return true;
				}
				
				
		    	// Catch center point
				double xc = rectangle.x + rectangle.width/2;
		    	double yc = rectangle.y + rectangle.height/2;  

		    	if ( (Math.abs( xc - x) < 10) && (Math.abs( yc - y) < 10) )
		    	{
		    		//System.out.println("Rectangle center point was caught");
		    		shape_caught = i;
		    		point_caught = ShapePoint.POINT_RECTANLGE_CENTER;
		    		return true;
		    	} 
			}
			
			// If the shape type is circle
			else if(shapes.get(i) instanceof Ellipse2D.Double)
			{
				// Get the reference to the line in array 
				Ellipse2D.Double circle = (Ellipse2D.Double)shapes.get(i);

		    	// Catch center point
				double xc = circle.x + circle.width/2;
		    	double yc = circle.y + circle.height/2;  

				double d;
				
				//Calculate distance to the image center
				d = Math.sqrt((y-yc)*(y-yc) + (x-xc)*(x-xc));
				
				//catch any point on the radius with Euclidian distance
				if( (Math.abs( d - circle.width/2) < 10) && (Math.abs( d - circle.height/2) < 10) )
				{
					//System.out.println("Cicle radius  point was caught");
		    		shape_caught = i;
		    		point_caught = ShapePoint.POINT_CIRCLE_RADIUS;
		    		return true;
				}
		    	
		    	if ( (Math.abs( xc - x) < 10) && (Math.abs( yc - y) < 10) )
		    	{
		    		//System.out.println("Circle center point was caught");
		    		shape_caught = i;
		    		point_caught = ShapePoint.POINT_CIRCLE_CENTER;
		    		return true;
		    	} 
		    	
		    	
		    	
			}
		}
		if(shape_caught != -1 || point_caught != ShapePoint.POINT_NONE) 
		{
    		shape_caught = -1;
    		point_caught = ShapePoint.POINT_NONE;
		}
		return false;
	}
	

	// Move line function
	void UpdateShapePosition( int x, int y )
	{
		
		// Check if we caught some shape
		if ( shape_caught >= 0 )
		{	   
			Graphics g = getGraphics();
			Graphics2D  g2d = (Graphics2D)g;  
			g2d.setXORMode(getBackground());
			

			// If we caught the line
			if(shapes.get(shape_caught) instanceof Line2D.Double)
			{
				// Get the caught line 
				Line2D.Double line = (Line2D.Double)shapes.get(shape_caught);
				
				g2d.setColor(colors.get(shape_caught));
				
				// Delete at previous position          
				g2d.draw(line); 
				
				// Update line end position
				if ( point_caught == ShapePoint.POINT_LINE_X1Y1 )
				{

					line.x1 = x;
					line.y1 = y;
					xp = x;
					yp = y;
				}
				
				else
				if ( point_caught == ShapePoint.POINT_LINE_X2Y2 )        	  
				{

					line.x2 = x;
					line.y2 = y;
					xp = x;
					yp = y;
				}
				
	            else
                if ( point_caught == ShapePoint.POINT_LINE_CENTER )        	  
                {
          	       double xc = x - 0.5 * (line.x1 + line.x2);
          	       double yc = y - 0.5 * (line.y1 + line.y2);   

           	       line.x1 += (int)xc;
           	       line.y1 += (int)yc;
           	       line.x2 += (int)xc;
           	       line.y2 += (int)yc;   
           	       xp = 0.5 * (line.x1 + line.x2);
           	       yp = 0.5 * (line.y1 + line.y2);
                }
				
				// Draw line at new position  
				g2d.draw(line);
			}   
			
			// If we caught the rectangle
			else if(shapes.get(shape_caught) instanceof Rectangle2D.Double)
			{
				// Get the caught line 
				Rectangle2D.Double rectangle = (Rectangle2D.Double)shapes.get(shape_caught);
				
				g2d.setColor(colors.get(shape_caught));
				
				// Delete at previous position          
				g2d.draw(rectangle); 
				
				
				// Update from upper left point
				if ( point_caught == ShapePoint.POINT_RECTANGLE_UPPER_LEFT )
				{
					double x_prev = rectangle.x;
					double y_prev = rectangle.y;
					rectangle.x = x;
					rectangle.y = y;
					rectangle.width = rectangle.width+(x_prev - rectangle.x);
					rectangle.height = rectangle.height+(y_prev - rectangle.y);
					xp = x;
					yp = y;
				}
				
				// Update from upper right point
				if ( point_caught == ShapePoint.POINT_RECTANGLE_UPPER_RIGHT )
				{
					double y_prev = rectangle.y;
					
					rectangle.y = y;
					rectangle.width = x - rectangle.x;
					rectangle.height = rectangle.height+(y_prev - rectangle.y);
					xp = x;
					yp = y;
				}
				
				// Update from lower left point
				if ( point_caught == ShapePoint.POINT_RECTANGLE_LOWER_LEFT )
				{
					double x_prev = rectangle.x;
					
					rectangle.x = x;
					rectangle.width = rectangle.width+(x_prev - rectangle.x);
					rectangle.height = y - rectangle.y;
					xp = x;
					yp = y;
				}
				
				// Update from lower right point
				if ( point_caught == ShapePoint.POINT_RECTANLGE_LOWER_RIGHT )
				{

					rectangle.width = x - rectangle.x;
					rectangle.height = y - rectangle.y;
					xp = x;
					yp = y;
				}
				
				
				
				// Update center
				if ( point_caught == ShapePoint.POINT_RECTANLGE_CENTER )
				{
          	       double xc = x - (rectangle.x + rectangle.width/2);
          	       double yc = y - (rectangle.y + rectangle.height/2);   

           	       rectangle.x += (int)xc;
           	       rectangle.y += (int)yc;

           	       xp = xc;
           	       yp = yc;
                }
				
				// Draw line at new position  
				g2d.draw(rectangle);
				
			}
			
			// If we caught the circle
			else if(shapes.get(shape_caught) instanceof Ellipse2D.Double)
			{
				// Get the caught line 
				Ellipse2D.Double circle = (Ellipse2D.Double)shapes.get(shape_caught);
				
				g2d.setColor(colors.get(shape_caught));
				
				// Delete at previous position          
				g2d.draw(circle); 

				// Update line end position
				if ( point_caught == ShapePoint.POINT_CIRCLE_RADIUS)
				{

	          	    System.out.println("c.x previous = " + circle.x);
	          	    System.out.println("c.y previous = " + circle.y);
	          	    System.out.println("c.width previous = " + circle.width);
	          	    System.out.println("c.height previous = " + circle.height);
	          	    
	          	    double xc = (circle.x + circle.width/2);
	          	    double yc = (circle.y + circle.height/2); 
	          	    
	          	    System.out.println("x.center = " + xc + ",y.center = " + yc);
	          	    
	          	    
					double d_prev = Math.sqrt((y-yc)*(y-yc) + (x-xc)*(x-xc));
	          	    System.out.println("d = " + d_prev);
					
						circle.x = (int)xc - (int)d_prev;
		          	    System.out.println("c.x = " + circle.x);
						circle.y = (int)yc - (int)d_prev;
		          	    System.out.println("c.y = " + circle.y);
						circle.width = 2*(int)d_prev;
		          	    System.out.println("c.width = " + circle.width);
						circle.height = 2*(int)d_prev;
		          	    System.out.println("c.height = " + circle.height);
                }
				
				
				
				// Update line end position
				if ( point_caught == ShapePoint.POINT_CIRCLE_CENTER )
				{
          	       double xc = x - (circle.x + circle.width/2);
          	       double yc = y - (circle.y + circle.height/2);   

          	       circle.x += (int)xc;
          	       circle.y += (int)yc;

           	       xp = xc;
           	       yp = yc;
                }
				
				// Draw line at new position  
				g2d.draw(circle);
				
			}
			g2d.setPaintMode(); 
		}   
		   
	}
	
	private void drawImage(Graphics graphic) {

	    Graphics2D g2d = (Graphics2D) graphic;
	    g2d.setBackground(backgroundColor);
	    g2d.clearRect(0,0,this.getWidth(),this.getHeight());
		g2d.setXORMode(getBackground());
	    
		// Redraw all shapes in the array
		for(int i = 0; i < shapes.size(); ++i)
		{
			g2d.setColor(colors.get(i));
			g2d.draw(shapes.get(i));
		}
		g2d.setColor(currentColor);
		g2d.setPaintMode();

	}
	
	void saveImage() 
	{
		BufferedImage image = new BufferedImage (this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
		
		try {
	         Graphics2D graphic = image.createGraphics();
	         File output = new File("output.jpeg");
	         drawImage(graphic);  // actual drawing on your image
	 			for(ImageRectangle picture : images)
	 			{
				picture.drawWhole(graphic);
	 			}
	         ImageIO.write(image, "jpeg", output);
	         System.out.println("Image has been created");
	    } catch(IOException log) {
	    		System.out.println("Imagae creation unsuccessful");
	         System.out.println(log);
	    }
	}
	
	void pushToTop() 
	{
		ImageRectangle buffer = picture_caught;
		images.remove(picture_caught);
		images.add(buffer);
	}
	
	void pushToBottom() 
	{
		ArrayList<ImageRectangle> bufferArray = new ArrayList<ImageRectangle>();
		for(ImageRectangle img : images)
		{
			bufferArray.add(img);
		}
		ImageRectangle buffer = picture_caught;
		bufferArray.remove(picture_caught);
		images.clear();
		images.add(buffer);
		for(int idx = bufferArray.size()-1; idx >=0; --idx)
		{
			System.out.println("idx = " + idx);
			images.add(bufferArray.get(idx));
		}
		
	}
}
