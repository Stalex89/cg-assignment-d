package start;

// Import all neccessary libraries and classes
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.Scanner;


public class PosterApplicationFrame extends JFrame
{
	MouseTransferListener mouseListener;

	ImageBuffer buffer;
	
	
	public DrawPanel dPanel;
	protected ButtonPanel bPanel;
	protected ToolPanel tPanel;
	protected MiniaturePanel mPanel;
	protected ShapePanel sPanel;
	protected ImagePanel iPanel;
	
	public PosterApplicationFrame()
    { 
		// Set size and border layout of application
		this.setSize(800, 600);
		setLayout(new BorderLayout());
		setTitle("Poster Application Creator v 0.1");
        


		
		
        // Set up panels 
		dPanel = new DrawPanel();
		bPanel = new ButtonPanel();
		tPanel = new ToolPanel();
		buffer = new ImageBuffer();
		
        // Init all subpanels
        mPanel = new MiniaturePanel();
        sPanel = new ShapePanel();
        iPanel = new ImagePanel();

		buffer.setDrawPanel(dPanel);
		buffer.setMiniaturePanel(mPanel);
		dPanel.setImageBuffer(buffer);
		mPanel.setImageBuffer(buffer);
		
        // Set up mouse listener and transfer handler
		mouseListener = new MouseTransferListener(dPanel, mPanel, sPanel, tPanel, bPanel, buffer);
		
		dPanel.addMouseListener(mouseListener);
        tPanel.addMouseListener(mouseListener);
        bPanel.addMouseListener(mouseListener);
		mPanel.addMouseListener(mouseListener);
        iPanel.addMouseListener(mouseListener);
        sPanel.addMouseListener(mouseListener);
        
		dPanel.addMouseMotionListener(mouseListener);
        tPanel.addMouseMotionListener(mouseListener);
        bPanel.addMouseMotionListener(mouseListener);
        mPanel.addMouseMotionListener(mouseListener);
        iPanel.addMouseMotionListener(mouseListener);
        sPanel.addMouseMotionListener(mouseListener);
        
	    sPanel.button1.addActionListener(mouseListener);
	    sPanel.button2.addActionListener(mouseListener);
	    sPanel.button3.addActionListener(mouseListener);
	    sPanel.button4.addActionListener(mouseListener);
	    
		bPanel.button1.addActionListener(mouseListener);
		bPanel.button2.addActionListener(mouseListener);
		bPanel.button3.addActionListener(mouseListener);
		bPanel.button4.addActionListener(mouseListener);
		bPanel.button5.addActionListener(mouseListener);
		bPanel.button6.addActionListener(mouseListener);
		bPanel.button7.addActionListener(mouseListener);
		bPanel.button8.addActionListener(mouseListener);
		bPanel.saveImageButton.addActionListener(mouseListener);
		
	    iPanel.add(new JScrollPane(mPanel));
	    
	    tPanel.add(iPanel, BorderLayout.NORTH);
	    tPanel.add(sPanel, BorderLayout.SOUTH);
		
		// Add panels and poster area to the frame
		add(new JScrollPane(dPanel),  BorderLayout.CENTER);
		add(bPanel, BorderLayout.SOUTH);
		add(tPanel, BorderLayout.WEST);
		
		// Set close operation nad visibility of the program
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
	
	public void paintComponents(Graphics g)
	{
		super.paintComponents(g);
		System.out.println("FRAME PAINT COMPINENT CALLED");

   
	}
	
}
