package start;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;



public class MouseTransferListener implements MouseListener, MouseMotionListener, 
				ActionListener//extends MouseAdapter
{
	// Placeholder for displaying thumbnail image
	private JLabel tLabel = new JLabel()	
	{
	    @Override 
	    public boolean contains(int x, int y) 
	    {
	      return false;
	    }
	};
		
	// Referrences to other panels and buffer
	DrawPanel dPanel;
	MiniaturePanel mPanel;
	ShapePanel sPanel;
	ToolPanel tPanel;
	ButtonPanel bPanel;
	ImageBuffer buffer;
	
	ImageRectangle picture_caught = null;
	boolean isAtPoint = false;
	
	Tool currTool = Tool.TOOL_NONE;
	ToolOperationMode op_mode = ToolOperationMode.OP_NONE;
	
	public MouseTransferListener(DrawPanel dPanel, MiniaturePanel mPanel, 
			ShapePanel sPanel, ToolPanel tPanel, ButtonPanel bPanel, ImageBuffer buffer) 
	{
		this.dPanel = dPanel;
		this.mPanel = mPanel;
		this.sPanel = sPanel;
		this.tPanel = tPanel;
		this.bPanel = bPanel;
		this.buffer = buffer; 
	}	

	// Window for image thumbnail while dragging
	private final JWindow tWindow = new JWindow();	
	
	// Component for checking where mouse released
	Component lastEntered;
	
	@Override
	public void mouseDragged(MouseEvent e) {
		
		//System.out.println("MouseTransferAdapter - mouse dragged at x=" + e.getX() + ", y=" +e.getY());
		
		Object p = e.getSource();
		//System.out.println("MouseTransferAdapter - mouse pressed on " + p.getClass());
		
		// Check if we pressed on MiniaturePanel
		if(p instanceof MiniaturePanel)
		{
			if( currTool == Tool.TOOL_SELECT )
			{
				tWindow.setLocation(e.getLocationOnScreen());
			
			}
		}
		else if(p instanceof DrawPanel)
		{ 
			if( currTool == Tool.TOOL_SELECT )
			{
				// If we catched some shape
				if ( dPanel.shape_caught != -1 )
				{
					dPanel.repaint();
					
					// Change position of the existing shape
					dPanel.UpdateShapePosition( e.getX(), e.getY() );	
				}
			}
			
			else 
			{
				Graphics g = dPanel.getGraphics();
				Graphics2D  g2d = (Graphics2D)g;   	
	
				// Set XOR mode to delete old shape
				g2d.setXORMode( dPanel.getBackground() );
				dPanel.currentColor = bPanel.getCurrentColor();
				g2d.setColor(dPanel.currentColor);
				
	
			    if( currTool == Tool.TOOL_LINE )
			    {
					//delete the existing line
					g2d.draw(new Line2D.Double(dPanel.x1,dPanel.y1,dPanel.x2,dPanel.y2));
					
					//find new end coordinates
					dPanel.x2 = e.getX();
					dPanel.y2 = e.getY();
					
					//draw new line
					g2d.draw(new Line2D.Double(dPanel.x1,dPanel.y1,dPanel.x2,dPanel.y2));
					System.out.println("Line redrawn");
			    }
			    if( currTool == Tool.TOOL_RECTANGLE )
			    {
			    	//delete the existing rectangle
					g2d.draw(new Rectangle2D.Double(Math.min(dPanel.x1,dPanel.x2),
							Math.min(dPanel.y1,dPanel.y2),
							Math.abs(dPanel.x2-dPanel.x1),Math.abs(dPanel.y2-dPanel.y1)));
			        
					//find new end coordinates
					dPanel.x2 = e.getX();
					dPanel.y2 = e.getY();
			        
			        //draw new rectangle	        
			        g2d.draw(new Rectangle2D.Double(Math.min(dPanel.x1,dPanel.x2),
			        		Math.min(dPanel.y1,dPanel.y2),Math.abs(dPanel.x2-dPanel.x1),
			        		Math.abs(dPanel.y2-dPanel.y1)));
			        System.out.println("Rectangle redrawn");
			    }
			    if( currTool == Tool.TOOL_CIRCLE )
			    {
			    	//delete the existing circle
					//g2d.draw(new Ellipse2D.Double(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x2-x1),Math.abs(y2-y1)));
			        g2d.draw(new Ellipse2D.Double (dPanel.x1-Math.max(Math.abs(dPanel.x1-dPanel.x2),
			        		Math.abs(dPanel.y1-dPanel.y2)), dPanel.y1-Math.max(Math.abs(dPanel.x1-dPanel.x2),
			        				Math.abs(dPanel.y1-dPanel.y2)), 
			        		2*Math.max(Math.abs(dPanel.x1-dPanel.x2),Math.abs(dPanel.y1-dPanel.y2)),
			        		2*Math.max(Math.abs(dPanel.x1-dPanel.x2),Math.abs(dPanel.y1-dPanel.y2))));
	
			    	
					//find new end coordinates
			        dPanel.x2 = e.getX();
			        dPanel.y2 = e.getY();
			    	
			        //draw new circle
			        //g2d.draw(new Ellipse2D.Double(Math.min(x1,x2),Math.min(y1,y2),Math.abs(x2-x1),Math.abs(y2-y1)));
			        g2d.draw(new Ellipse2D.Double (dPanel.x1-Math.max(Math.abs(dPanel.x1-dPanel.x2),
			        		Math.abs(dPanel.y1-dPanel.y2)), dPanel.y1-Math.max(Math.abs(dPanel.x1-dPanel.x2),
			        				Math.abs(dPanel.y1-dPanel.y2)), 
			        		2*Math.max(Math.abs(dPanel.x1-dPanel.x2),Math.abs(dPanel.y1-dPanel.y2)),
			        		2*Math.max(Math.abs(dPanel.x1-dPanel.x2),Math.abs(dPanel.y1-dPanel.y2))));
			        
			        System.out.println("Circle redrawn");
			    }
		
		        g2d.setPaintMode(); 
			}
		}
		
		//System.out.println( "mouseDragged at " + arg0.getX() + "  " + arg0.getY() );
	}
	
	

	@Override
	public void mouseMoved(MouseEvent e) {
		
		//System.out.println("MouseTransferAdapter - mouse moved at x=" + e.getX() + ", y=" + e.getY());
		
		// If we selected none 
				if(currTool == Tool.TOOL_SELECT)
				{
					Graphics g = dPanel.getGraphics();
					Graphics2D  g2d = (Graphics2D)g;   	 
					g2d.setXORMode(dPanel.getBackground());
					
					// If we caught a point and we track it first time
					if(dPanel.CatchShapePoint(  e.getX(), e.getY() ) && !isAtPoint) 
					{
						// If tracked shape is line
						if(dPanel.shapes.get(dPanel.shape_caught) instanceof Line2D.Double)
						{
							//get the caught line 
							Line2D.Double line = (Line2D.Double)dPanel.shapes.get(dPanel.shape_caught);
							
							// Update line end position
							if ( dPanel.point_caught == ShapePoint.POINT_LINE_X1Y1 )
							{
								dPanel.xp = line.x1;
								dPanel.yp = line.y1;
								//System.out.println("Mouse is over point x1");
							}
							
							else
							if ( dPanel.point_caught == ShapePoint.POINT_LINE_X2Y2 )        	  
							{
								dPanel.xp = line.x2;
								dPanel.yp = line.y2;
								//System.out.println("Mouse is over point x2");
							}
							
				            else
			                if ( dPanel.point_caught == ShapePoint.POINT_LINE_CENTER )        	  
			                {
			                	double xc =  0.5 * (line.x1 + line.x2);
			                	double yc =  0.5 * (line.y1 + line.y2);   
			                	dPanel.xp = xc;
			                	dPanel.yp = yc;
								//System.out.println("Mouse is over point x2");
			
			                }
					        
						}
						
						// If tracked shape is rectangle
						else if(dPanel.shapes.get(dPanel.shape_caught) instanceof Rectangle2D.Double)
						{
							//get the caught line 
							Rectangle2D.Double rectangle = (Rectangle2D.Double)dPanel.shapes.get(dPanel.shape_caught);

							// Update line end position
							if ( dPanel.point_caught == ShapePoint.POINT_RECTANLGE_CENTER )
							{
								dPanel.xp = rectangle.x + rectangle.width/2;
						    	dPanel.yp = rectangle.y + rectangle.height/2; 
								//System.out.println("Mouse is over center point");
							}
						}
						
						// If tracked shape is circle
						else if(dPanel.shapes.get(dPanel.shape_caught) instanceof Ellipse2D.Double)
						{
							//get the caught line 
							Ellipse2D.Double circle = (Ellipse2D.Double)dPanel.shapes.get(dPanel.shape_caught);

							// Update line end position
							if ( dPanel.point_caught == ShapePoint.POINT_CIRCLE_CENTER )
							{
								dPanel.xp = circle.x + circle.width/2;
						    	dPanel.yp = circle.y + circle.height/2; 
								//System.out.println("Mouse is over center point");
							}
						}
						
						g2d.draw(new Rectangle2D.Double (dPanel.xp-5,dPanel.yp-5,10,10));
						isAtPoint = true;
					}
					
					// If we left the range of tracking point 
					else if((Math.abs(e.getX()-dPanel.xp) > 10 || Math.abs(e.getY())-dPanel.yp > 10) && isAtPoint) 
					{
						g2d.draw(new Rectangle2D.Double (dPanel.xp-5,dPanel.yp-5,10,10));
						isAtPoint = false; 
					}
					
					g2d.setPaintMode(); 	
				}

		
		
	}



	@Override
	public void mouseEntered(MouseEvent e) {
		
		//System.out.println("MouseTransferAdapter - mouse entered at x=" + e.getX() + ", y=" + e.getY());
		
		//Object p = e.getSource();
		
		lastEntered = e.getComponent();
		
		//System.out.println("MouseTransferAdapter - mouse entered on " + p.getClass());
		

	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		//System.out.println("MouseTransferAdapter - mouse exited at x=" + e.getX() + ", y=" + e.getY());
		
		//Object p = e.getSource();
		
		//System.out.println("MouseTransferAdapter - mouse exited on " + p.getClass());
		
		// Release line if currently being dragged
		dPanel.shape_caught = -1;
		dPanel.point_caught = ShapePoint.POINT_NONE;
		isAtPoint = false;
	}


	@Override
	public void mousePressed(MouseEvent e) 
	{
		//System.out.println("MouseTransferAdapter - mouse pressed at (" + e.getX() + "," + e.getY() + ")");
		
		// First check on which panel mouse was pressed
		
		
		
		Object p = e.getSource();
		//System.out.println("MouseTransferAdapter - mouse pressed on " + p.getClass());
		
		// Check if we pressed on MiniaturePanel
		if(p instanceof MiniaturePanel)
		{
			// Then we want to DnD Picture
			
			if( currTool == Tool.TOOL_SELECT)
			{
			
				// Get the component at mouse cordinates 
		        Component c = SwingUtilities.getDeepestComponentAt(mPanel, e.getX(), e.getY());
		        
		        // If we got a JLabel picture thumbnail
		        if(c!=null && c instanceof JLabel) 
		        {
					System.out.println("MouseTransferAdapter - mouse pressed - got component " + c.getClass());
		        	
					// Set up drag image preview window
					tWindow.add(tLabel);
					tWindow.setAlwaysOnTop(true);
					tWindow.setBackground(new Color(0,true));
					
					int key;
	
					BufferedImage image;
		        	THPicture THpcture;
		        	
		        	try 
		        	{
		        		key = mPanel.getIndexOfLabel((JLabel)c);
		        		//System.out.println("key = " + key);
		        	
		        		image = mPanel.getImageByKey(key);
		        		//System.out.println("image = " + image);
		        	
		        		THpcture = new THPicture(image, (JLabel)c);
		        	
		        		buffer.setBufferImage(THpcture);
		        	}
		        	catch(Exception ex) 
		        	{
		        		
		        	}
		        
	        	
	        	JLabel cl = (JLabel)c;
	        	
	        	tLabel.setIcon(cl.getIcon());
	    		tWindow.pack();
	    		tWindow.setLocation(e.getLocationOnScreen());
	    		tWindow.setVisible(true);
		        
		        }
			}
		}
		
		// Check if mouse was pressed on DrawPanel
		else if(p instanceof DrawPanel)
		{
			int m_press_x =  e.getX();
		    int m_press_y =  e.getY();
			
			System.out.println("MouseTransferAdapter - mouse pressed on DrawPanel");
			
			Graphics g = dPanel.getGraphics();
			Graphics2D  g2d = (Graphics2D)g;   	  
			g2d.setXORMode(dPanel.getBackground()); 
			
			
			
			if ( e.getButton() == MouseEvent.BUTTON1)
			{		

				if( currTool == Tool.TOOL_SELECT )
				{
					// If we pressed left mouse button over point of the shape
					if ( dPanel.CatchShapePoint(  e.getX(), e.getY()) ) 
					{
						// Hide pivot point
						isAtPoint = false;
						g2d.draw(new Rectangle2D.Double (dPanel.xp-5,dPanel.yp-5,10,10));
					}
					
				}			
				else 
				{
					// Get initial coordinates of the figure
			        dPanel.x1 = (int)e.getX();
			        dPanel.y1 = (int)e.getY();
			        dPanel.x2 = (int)e.getX();
			        dPanel.y2 = (int)e.getY();
				}
				

			}
			
			// If right button pressed
			else
			{
				if( currTool == Tool.TOOL_SELECT )
				{
					// If we catch some figure point
					if ( dPanel.CatchShapePoint(  e.getX(), e.getY() ) ) 
					{
		
						// If point is the center
						if(dPanel.point_caught == ShapePoint.POINT_LINE_CENTER ||
								dPanel.point_caught == ShapePoint.POINT_RECTANLGE_CENTER ||
								dPanel.point_caught == ShapePoint.POINT_CIRCLE_CENTER)
						{
		
							
							
							// If tracked shape is line
							if(dPanel.shapes.get(dPanel.shape_caught) instanceof Line2D.Double)
							{
								
								//get the caught line 
								Line2D.Double line = (Line2D.Double)dPanel.shapes.get(dPanel.shape_caught);
								
								g2d.setColor(dPanel.colors.get(dPanel.shape_caught));
								
								//remove the line from content pane
								g2d.draw(line);
							}
							
							// If tracked shape is rectangle
							else if(dPanel.shapes.get(dPanel.shape_caught) instanceof Rectangle2D.Double)
							{
								
								//get the caught rectangle
								Rectangle2D.Double rectangle = (Rectangle2D.Double)dPanel.shapes.get(dPanel.shape_caught);
								
								g2d.setColor(dPanel.colors.get(dPanel.shape_caught));
								
								//remove the rectangle from content pane
								g2d.draw(rectangle);
							}
							
							// If tracked shape is circle
							else if(dPanel.shapes.get(dPanel.shape_caught) instanceof Ellipse2D.Double)
							{
								
								//get the caught circle
								Ellipse2D.Double circle = (Ellipse2D.Double)dPanel.shapes.get(dPanel.shape_caught);
								
								g2d.setColor(dPanel.colors.get(dPanel.shape_caught));
								
								//remove the circle from content pane
								g2d.draw(circle);
							}
							
							// Remove shape from array
							dPanel.shapes.remove(dPanel.shape_caught);
							dPanel.colors.remove(dPanel.shape_caught);
							dPanel.shape_caught = -1;
							dPanel.point_caught = ShapePoint.POINT_NONE;
							
							// Remove pivot point
							isAtPoint = false;
							g2d.setColor(Color.BLACK);
							g2d.draw(new Rectangle2D.Double (dPanel.xp-5,dPanel.yp-5,10,10));
							g2d.setColor(dPanel.currentColor);
						}
							
							
					}
				}
			}
			g2d.setPaintMode(); 
			
			// Check for every image in DrawPanel image array
			for(ImageRectangle image : dPanel.images)
			{
				// Check is mouse position near the vertex of the rectangle
				// and set appropriate operation mode (scale or rotate)
				if (image.catchRectangle(m_press_x, m_press_y))
				{
					// If we pressed with left mouse button
					if (e.getButton() == MouseEvent.BUTTON1)
					{
						System.out.println("Left mouse button was pressed at image, rotation initiated");
						op_mode = ToolOperationMode.OP_IMAGE_ROTATION;
					}
					else
					{
						System.out.println("Left mouse button was pressed at image, scaling initiated");
						op_mode = ToolOperationMode.OP_IMAGE_SCALING;
					}
				}
			}
		}
		

	}

	@Override
	public void mouseReleased(MouseEvent e) 
	{
	
		System.out.println("MouseTransferAdapter - mouse released at x=" + e.getX() + ", y=" + e.getY());
		
		Object p = e.getSource();
		//System.out.println("MouseTransferAdapter - mouse pressed on " + p.getClass());
		
		// Check if we pressed on MiniaturePanel
		if(p instanceof MiniaturePanel)
		{
			if( currTool == Tool.TOOL_SELECT)
			{
				if(lastEntered instanceof DrawPanel)
				{
					THPicture pic;
					ImageRectangle img;
					
	
				
					try
					{
						pic = buffer.getBufferImage();
						img = new ImageRectangle(pic.getImage(), e.getX()-tPanel.getWidth(), e.getY());
						dPanel.addImage(img);
						
					}
					catch(Exception ex) 
					{
						System.out.println("Failed to add image");
					}
					

				}

			}

		}
		buffer.clearBufferImage();
		tWindow.setVisible(false);
		
		if(p instanceof DrawPanel)
		{
			// If left mouse button pressed
			if ( e.getButton() == MouseEvent.BUTTON1)
			{		
				Graphics g = dPanel.getGraphics();
				Graphics2D  g2d = (Graphics2D)g;   	 
				g2d.setXORMode(dPanel.getBackground());
				 
				if( currTool == Tool.TOOL_SELECT)
				{
					// REmove the pivot point
					if ( dPanel.CatchShapePoint(  e.getX(), e.getY() ) )
					{
						isAtPoint = true;
						g2d.draw(new Rectangle2D.Double (dPanel.xp-5,dPanel.yp-5,10,10));
					}
					
					// Untrack the shape (need to be tracked again)
					dPanel.shape_caught = -1;
		
					g2d.setPaintMode(); 
				}
			
				else 
				{
					//get the end coordinates
					dPanel.x2 = e.getX();
					dPanel.y2 = e.getY();
					
					//template for the future shape
					Shape figure = null;
					
					//if the end point is not the start point
					if(dPanel.x1 != dPanel.x2 || dPanel.y1 != dPanel.y2)
					{
						dPanel.currentColor = bPanel.getCurrentColor();
						g2d.setColor(dPanel.currentColor);
						
						if( currTool == Tool.TOOL_LINE )
						{
							//create new line and add it to our array
							figure = new Line2D.Double(dPanel.x1,dPanel.y1,dPanel.x2,dPanel.y2);
							dPanel.shapes.add((Line2D.Double)(figure));
							System.out.println("New line created");
						}
						
						else if( currTool == Tool.TOOL_RECTANGLE )
						{
							//create new rectangle and add it to our array
							figure = new Rectangle2D.Double(Math.min(dPanel.x1,dPanel.x2),
									Math.min(dPanel.y1,dPanel.y2),Math.abs(dPanel.x2-dPanel.x1),
									Math.abs(dPanel.y2-dPanel.y1));
							dPanel.shapes.add((Rectangle2D.Double)(figure));
							System.out.println("New rectangle created");
						}
						else if( currTool == Tool.TOOL_CIRCLE )
						{
							//create new circle and add it to our array
							figure = new Ellipse2D.Double (dPanel.x1-Math.max(Math.abs(dPanel.x1-dPanel.x2),
									Math.abs(dPanel.y1-dPanel.y2)), dPanel.y1-Math.max(Math.abs(dPanel.x1-dPanel.x2),
											Math.abs(dPanel.y1-dPanel.y2)), 
					        		2*Math.max(Math.abs(dPanel.x1-dPanel.x2),Math.abs(dPanel.y1-dPanel.y2)), 
					        		2*Math.max(Math.abs(dPanel.x1-dPanel.x2),Math.abs(dPanel.y1-dPanel.y2)));
							dPanel.shapes.add((Ellipse2D.Double)(figure));
							System.out.println("New circle created");
						}
						dPanel.colors.add(dPanel.currentColor);
					}
					
					//if we have a new figure added
					if(figure != null)
					{
						//repaint the content pane
						dPanel.repaint();
					}
				}
			}
		}
			
	}



	@Override
	public void mouseClicked(MouseEvent e) 
	{

		
	}



	@Override
	public void actionPerformed(ActionEvent e) 
	{
		// Acquire reference to the object being affected the event
		Object source = e.getSource();
	  
		// Distinguish which button has been clicked
		if ( source == sPanel.button1 ) {
			System.out.println("Select selected");
			currTool = Tool.TOOL_SELECT;
		}
		else if ( source == sPanel.button2 ) {
			System.out.println("Text selected");
			currTool = Tool.TOOL_TEXT;
		}
	    else if (source == sPanel.button3){
	    	System.out.println("Rectangle selected");
	    	currTool = Tool.TOOL_RECTANGLE;
	    }
	    else if (source == sPanel.button4){
	    	System.out.println("Circle selected");
	    	currTool = Tool.TOOL_CIRCLE;
	    }

	    else 
	    {
	    	System.out.println("None selected");
	    	currTool = Tool.TOOL_NONE;
	    }
		
	}
	
	
	
}
