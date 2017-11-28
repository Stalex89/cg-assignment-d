package start;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class ShapePanel extends JPanel {
	
	JRadioButton button1;
	JRadioButton button2;
	JRadioButton button3;  	
	JRadioButton button4;  	
	

	
	ShapePanel() 
	{
		super();
		
		// Set background of tool panel
        setBackground   (Color.GREEN);
        setPreferredSize(new Dimension(30,200));
        //setLayout       (new BorderLayout(8,8));
        setLayout(new GridLayout(4,1));
        
	    button1 = new JRadioButton( "Select" ); 
	    button2 = new JRadioButton( "Text" );
	    button3 = new JRadioButton( "Rectangle" );
	    button4 = new JRadioButton( "Circle" ); 
	    
	    ButtonGroup bg = new ButtonGroup();
	    bg.add( button1 );
	    bg.add( button2 ); 
	    bg.add( button3 );
	    bg.add( button4 );

	    //JPanel toolPanel =  new JPanel();
        //toolPanel.setLayout(new GridLayout(4,1));
        //toolPanel.setBackground(Color.darkGray);
        //toolPanel.setPreferredSize(new Dimension(40,20));
	    
        //toolPanel.add( button1 );
        //toolPanel.add( button2 ); 
        //toolPanel.add( button3 );
        //toolPanel.add( button4 );

        add( button1 );
        add( button2 ); 
        add( button3 );
        add( button4 );
	    
	    //add(toolPanel, BorderLayout.CENTER);

	}
}
