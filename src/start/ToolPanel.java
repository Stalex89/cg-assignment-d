package start;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ToolPanel extends JPanel 
{



	
	ToolPanel()
	{
		super();
		
		// Set background of tool panel
        setBackground   (Color.BLACK);
        setPreferredSize(new Dimension(140,400));
        setLayout       (new BorderLayout(8,8));

	}
}
