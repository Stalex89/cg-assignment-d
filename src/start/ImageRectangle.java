package start;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;


class ImageRectangle extends Rectangle2D.Double
{
	
	static final int DEF_RECT_SIZE_X = 60;
	static final int DEF_RECT_SIZE_Y = 90;

	int rect_width, rect_height;
	
	int rect_x, rect_y;
	
	enum RectVertices  
	{ 
		UPPER_LEFT, 
		UPPER_RIGHT, 
		LOWER_LEFT, 
		LOWER_RIGHT, 
		CENTER, 
		NONE_VERT 
	};
	
	/*
	enum OpModes  
	{ 
		OP_ROTATION, 
		OP_SCALING, 
		OP_TRANSLATION, 
		OP_NONE 
	};
	*/
	
	boolean      rect_caught;
	
	RectVertices vert_caught = RectVertices.NONE_VERT;
	//OpModes op_mode = OpModes.OP_NONE;
	
	BufferedImage img;
	public AffineTransform transform, inverse;

	
	//ImageRectangle( ImageRectangle image, int x_pos, int y_pos )
	ImageRectangle( BufferedImage image, int x_pos, int y_pos )
	{     
		// Create placeholder rectangle 
		super( x_pos, y_pos, DEF_RECT_SIZE_X, DEF_RECT_SIZE_Y );
	    
		img = image;
		
		rect_x = x_pos;
		rect_y = y_pos;
		
		//System.out.println("new ImgRect (x,y) = (" + x_rect_pos + ", " + y_rect_pos + ")");
		
		
	    transform = new AffineTransform( 1, 0, /* 0 */
	    								 0, 1, /* 0 */
	    								 0, 0  /* 1 */);

	    // Scale it to fit the fixed rectangle
	    rect_height = img.getHeight(); 
	    rect_width = img.getWidth();  
	    
		//System.out.println("new ImgRect size = (" + rect_size_x + ", " + rect_size_y + ")");
	    
	    // Reset rectangle size and location to fit to the size image
	    setRect( x_pos, y_pos, rect_width, rect_height );
	      
	    // CAUSES BUG With IMAGE POSITIONING
	    double  init_scale = DEF_RECT_SIZE_Y / (double)rect_height;
	    transform.setToScale( init_scale, init_scale );
	      
	    try 
	    {
	    	inverse = transform.createInverse();
	    }
	    catch (Exception x)
	    { 
	    	inverse = null; 
	    }
	}
	
	   // This method draws the whole shape elements 
	   void drawWhole(Graphics2D g2d)
	   {      
	      // Assign the shape transformation to the drawing context 
	      //g2d.transform( transform );        
	      
	      g2d.draw( this );
	      if ( img != null )
	    	  g2d.drawImage( img, rect_x, rect_y, null );
	      
	      
	      // CAUSES BUG WITH IMAGE POSITIONING!!!!!
	      // Restore identity transform to avoid incorrect drawings of controls
	      //g2d.setTransform(new AffineTransform());      
	      
	   }
	   
	   // This method tests if passed point in panel coordinates is close to one of rectangle vertices
	   boolean  catchRectangle( int x, int y )
	   {
	      // Calculate the position of the point in local shape coordinates
	      Point2D.Double p1 = new Point2D.Double( x, y );
	      
	      Point2D.Double p = new Point2D.Double(0, 0);
	      inverse.transform(p1, p);
	      
	      // Find the sensitivity area in the local shape coordinates
	      double sc_x = transform.getScaleX();
	      double tolerance = DrawPanel.SENS_DIST / sc_x;
	      
	      if (( Math.abs( p.x ) < tolerance ) && ( Math.abs( p.y ) < tolerance ))
	      {
	    	 System.out.println("Upper Left Vertex caught");
	         rect_caught = true;
	         vert_caught = RectVertices.UPPER_LEFT;
	         return true;
	      }
	      if (( Math.abs( p.x - rect_width ) < tolerance ) && ( Math.abs( p.y ) < tolerance ))
	      {
	    	  System.out.println("Upper Right Vertex caught");
	         rect_caught = true;
	         vert_caught = RectVertices.UPPER_RIGHT;
	         return true;
	      }
	      if (( Math.abs( p.x - rect_width ) < tolerance ) && ( Math.abs( p.y - rect_height ) < tolerance ))
	      {
	    	 System.out.println("Lower Right Vertex caught");
	         rect_caught = true;
	         vert_caught = RectVertices.LOWER_RIGHT;
	         return true;
	      }
	      if (( Math.abs( p.x ) < tolerance ) && ( Math.abs( p.y - rect_height ) < tolerance ))
	      {
	    	 System.out.println("Lower Left Vertex caught");
	         rect_caught = true;
	         vert_caught = RectVertices.LOWER_LEFT;
	         return true;
	      }      
	      
	      
	      // Catch the center
	      Point2D.Double rect_center =  new Point2D.Double((p.x-rect_width)/2,(p.y-rect_height)/2);
	      
	      if (( Math.abs(rect_center.x) < tolerance ) && ( Math.abs( rect_center.y) < tolerance ))
	      {
	    	 System.out.println("Center caught");
	         rect_caught = true;
	         vert_caught = RectVertices.CENTER;
	         return true;
	      }  

	      // If we are there then no rectangle vertex is close enough
	      rect_caught = false;
	      vert_caught = RectVertices.NONE_VERT;
	      
	      return false;
	   }

	   double rotateShapeByPoints(
	      Point2D.Double p_p_panel, // mouse press point - start position for rotation
	      Point2D.Double r_p_panel  // mouse release point - end position for rotation 
	   )
	   {
	      AffineTransform inverse;
	      try {
	         inverse = transform.createInverse();
	      }
	      catch (Exception x) { return 0; }

	      // First find the position of the press point in local rectanglecoords
	      Point2D.Double p_p = new java.awt.geom.Point2D.Double(0, 0);
	      inverse.transform( p_p_panel, p_p);
	          
	      // Find the coords of the release point in local coords
	      Point2D.Double p_r = new java.awt.geom.Point2D.Double(0, 0);
	      inverse.transform( r_p_panel, p_r);
	      
	      // Find the shape center - for your own shapes you nned to owerwrite this method
	      Rectangle2D bb = getBounds2D();
	      double shape_center_x = bb.getCenterX();     
	      double shape_center_y = bb.getCenterY();
	      
	      // Find corresponding vectors
	      Point2D.Double v_p = new java.awt.geom.Point2D.Double( 
	                              p_p.x - shape_center_x, 
	                              p_p.y - shape_center_y );
	      Point2D.Double v_r = new java.awt.geom.Point2D.Double( 
	            p_r.x - shape_center_x, 
	            p_r.y - shape_center_y );
	      
	      // Compute corresponding angles
	      double a_p = Math.atan2( v_p.y, v_p.x);
	      if ( a_p < 0 )
	         a_p = 2*Math.PI + a_p;
	      // Now a_p is in the range <0 2*pi)
	      
	      double a_r = Math.atan2( v_r.y, v_r.x);
	      if ( a_p < 0 )
	         a_p = 2*Math.PI + a_p;
	      // Now a_r is in the range <0 2*pi)
	      
	      double  a_rotation = a_r - a_p;
	      
	      // Now we are ready to rotate
	      // =======================================================
	      // set transform for rotation by a_rotation
	      // Find center
	      Point2D.Double pc = new java.awt.geom.Point2D.Double(shape_center_x, shape_center_y);
	      Point2D.Double p2 = new java.awt.geom.Point2D.Double(0, 0);
	      transform.transform(pc, p2);
	      AffineTransform new_transform = new AffineTransform(  1, 0, /* 0 */
	                                                            0, 1, /* 0 */
	                                                            0, 0 /* 1 */);
	      new_transform.rotate( a_rotation, p2.x, p2.y);
	      new_transform.concatenate(transform);
	      
	      transform.setTransform( new_transform );

	      try {
	         this.inverse = transform.createInverse();
	      }
	      catch (Exception x) { return 0; }
	      
	      return a_rotation;
	   }
	            
	   double scaleShapeByPoints(
	      Point2D.Double p_p_panel, // mouse press point - start position for rotation
	      Point2D.Double r_p_panel  // mouse release point - end position for rotation 
	   )     
	   {
	      AffineTransform inverse;
	      try {
	         inverse = transform.createInverse();
	      }
	      catch (Exception x) { return 0; }

	      // First find the position of the press point in local rectanglecoords
	      Point2D.Double p_p = new java.awt.geom.Point2D.Double(0, 0);
	      inverse.transform( p_p_panel, p_p);
	          
	      // Find the coords of the release point in local coords
	      Point2D.Double p_r = new java.awt.geom.Point2D.Double(0, 0);
	      inverse.transform( r_p_panel, p_r);
	      
	      // Find the shape center - for your own shapes you need to owerwrite this method
	      Rectangle2D bb = getBounds2D();
	      double shape_center_x = bb.getCenterX();     
	      double shape_center_y = bb.getCenterY();
	      
	      // Find corresponding vectors
	      Point2D.Double v_p = new java.awt.geom.Point2D.Double( 
	                              p_p.x - shape_center_x, 
	                              p_p.y - shape_center_y );
	      Point2D.Double v_r = new java.awt.geom.Point2D.Double( 
	            p_r.x - shape_center_x, 
	            p_r.y - shape_center_y );
	      
	      double v_p_len = Math.sqrt( v_p.x*v_p.x + v_p.y*v_p.y );
	      double v_r_len = Math.sqrt( v_r.x*v_r.x + v_r.y*v_r.y );
	      
	      double scale = v_r_len / v_p_len;   
	                                
	      // Now we are ready to scale
	      // =======================================================
	      // set transform for scaling by scale
	      // Find center  
	                
	      Point2D.Double pc = new java.awt.geom.Point2D.Double(shape_center_x, shape_center_y);
	      Point2D.Double p2 = new java.awt.geom.Point2D.Double(0, 0);
	      transform.transform(pc, p2);
	      AffineTransform new_transform = new AffineTransform(  1, 0, /* 0 */
	                                                            0, 1, /* 0 */
	                                                            0, 0 /* 1 */);
	      new_transform.translate( p2.x, p2.y);
	      new_transform.scale( scale, scale);
	      new_transform.translate( -p2.x, -p2.y);
	      new_transform.concatenate(transform);        
	         
	      transform.setTransform( new_transform );

	      try {
	         this.inverse = transform.createInverse();
	      }
	      catch (Exception x) { return 0; }
	      
	      return scale;
	   }   
	   
	   void translate( int x_t, int y_t )
	   {
	      AffineTransform new_transform = new AffineTransform(1, 0, /* 0 */
	                                                          0, 1, /* 0 */
	                                                          0, 0 /* 1 */);
	      new_transform.translate( x_t, y_t);
	      new_transform.concatenate(transform);
	      transform = new_transform;
	      // transform.concatenate( new_transform);
	      try {
	         inverse = transform.createInverse();
	      }
	      catch (Exception x) {}
	   }
	   
	  
	   void rotateByDegs( double angle )
	   {
	      Point2D.Double p1 = new java.awt.geom.Point2D.Double( rect_width/2, rect_height/2 );
	      Point2D.Double p2 = new java.awt.geom.Point2D.Double(0, 0);
	      transform.transform(p1, p2);
	      AffineTransform new_transform = new AffineTransform(  1, 0, /* 0 */
	                                                            0, 1, /* 0 */
	                                                            0, 0 /* 1 */);
	      new_transform.rotate( angle * 2 * Math.PI / 360, p2.x, p2.y);
	      new_transform.concatenate(transform);
	      
	      transform = new_transform;
	      try
	      {
	         inverse = transform.createInverse();
	      }
	      catch (Exception x)
	      {
	      }
	   }
	   
	   void scale( double s_xy )
	   {
	      Point2D.Double p1 = new java.awt.geom.Point2D.Double( rect_width/2, rect_height/2 );  
	      Point2D.Double p2 = new java.awt.geom.Point2D.Double(0, 0);
	      transform.transform(p1, p2);
	      
	      AffineTransform new_transform = new AffineTransform(  1, 0, /* 0 */
	            0, 1, /* 0 */
	            0, 0 /* 1 */);
	     new_transform.translate( p2.x, p2.y);
	     new_transform.scale( s_xy, s_xy );
	     new_transform.translate( -p2.x, -p2.y);
	     new_transform.concatenate(transform);         
	     
	     transform = new_transform;
	             
	      // reduce size
	      // transform.scale(0.5, 0.5);
	      try
	      {
	         inverse = transform.createInverse();
	      }
	      catch (Exception x)
	      {
	      }
	   }
	   
	   void reset()
	   {
	      double  init_scale = DEF_RECT_SIZE_Y / (double)rect_height;
	      transform.setToScale( init_scale, init_scale );
	      
	      try {
	         inverse = transform.createInverse();
	      }
	      catch (Exception x){ inverse = null; }
	   }
	}
	   