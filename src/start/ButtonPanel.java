package start;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;

public class ButtonPanel extends JPanel //implements ActionListener
{

	JButton button1;
	JButton button2;
	JButton button3;  	
	JButton button4;  	
	JButton button5;  	
	JButton button6;  	
	
	// Three text field for RGB values
	JTextField rField;
	JTextField gField;
	JTextField bField;
	
	JButton saveTextButton;
	JButton loadTextButton;
	JButton saveImageButton;
	
	ButtonPanel()
	{
		super();
		setLayout(new GridLayout(2,6));
		
		
		button1 = new JButton( "Translate left" );
		button2 = new JButton( "Translate right" );
		button3 = new JButton( "Translate up" ); 
		button4 = new JButton( "Translate down" ); 
		button5 = new JButton( "Rotate left" ); 
		button6 = new JButton( "Rotate right" );  
	       
	    add( button1 );
	    add( button2 ); 
	    add( button3 );
	    add( button4 );
	    add( button5 ); 
	    add( button6 );
	    
		rField = new JTextField( "0" );
		gField = new JTextField( "0" );
		bField = new JTextField( "0" );
		
		saveTextButton = new JButton("save as txt");
		loadTextButton = new JButton("load as text");
		saveImageButton = new JButton("save as image");
		
		// Color input validation
		InputVerifier colorInputVerifier = new InputVerifier() 
		{
			// Color input validation function
			public boolean verify(JComponent comp)
			{
				JTextField field = (JTextField)comp;
		        boolean passed = false;
		        try {
		            int n = Integer.parseInt(field.getText());
		            passed = ( 0 <= n && n <= 255 );
		        } catch (NumberFormatException e) { }
		        if ( !passed ) {
		        	comp.getToolkit().beep();
		            field.selectAll();
		        }
		        return passed;
			}
			
		};
		
		// Add verifier to RGB input fields
		rField.setInputVerifier(colorInputVerifier);
		gField.setInputVerifier(colorInputVerifier);
		bField.setInputVerifier(colorInputVerifier);
		
	    add( rField ); 
	    add( gField ); 
	    add( bField ); 
	    
	    add( saveTextButton );
	    add( loadTextButton );
	    add( saveImageButton );
		
	}
	
	Color getCurrentColor () 
	{
		int r = Integer.parseInt(rField.getText());
		int g = Integer.parseInt(gField.getText());
		int b = Integer.parseInt(bField.getText());
		return new Color(r,g,b); 
	}
}
