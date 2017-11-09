package UI;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

public class LogPanel extends JPanel{

	private JTextArea logs=new JTextArea();
	public LogPanel() {
		JScrollPane pane=new JScrollPane(logs);
		pane.setSize(this.getSize());
		logs.setTabSize(4);
		logs.setLineWrap(true);
		logs.setWrapStyleWord(true);
		add(pane);
	}

}
