package UI;

import java.awt.Button;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class ToolFrame extends JFrame{

	public ToolFrame() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		setSize(800,600);
		setLocationByPlatform(true);
		setTitle("TagTool");
		MainComponent component=new MainComponent(this.getSize());
		add(component);
	}
}