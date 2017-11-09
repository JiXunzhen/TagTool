package UI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.print.attribute.Size2DSyntax;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import Controller.TagInitController;

public class MainComponent extends JComponent {
	private JTextPane logs = new JTextPane();
	private JProgressBar bar=new JProgressBar();
	private Dimension size;
	JLabel progresslabel=new JLabel();
	
	private SimpleAttributeSet normalset = new SimpleAttributeSet();
	private SimpleAttributeSet errorset = new SimpleAttributeSet();

	private String inputpath;
	private String outputpath;
	
	private int project_file_count;
	private int current_count;

	public MainComponent(Dimension s) {
		size=s;
		setSize(size);
		inputpath = "";
		outputpath = TagInitController.DEFAULT_OUTPUT_CONFIG;
		StyleConstants.setForeground(normalset, Color.BLACK);
		StyleConstants.setFontSize(normalset, 12);
		StyleConstants.setForeground(errorset, Color.RED);
		StyleConstants.setFontSize(errorset, 12);

		logs.setEditable(false);
		TagInitController.getInstance().register(this);
		setBackground(Color.LIGHT_GRAY);
		project_file_count=0;
		current_count=0;
		
		bar.setOrientation(JProgressBar.HORIZONTAL);
		bar.setMinimum(0);
		bar.setMaximum(100);
		bar.setSize(size.width*3/4, 20);
		bar.setLocation(size.width / 8, 170);
		bar.setBorderPainted(true);
		
		progresslabel.setText("Progress:"+current_count+"/"+project_file_count);
		progresslabel.setSize(size.width*3/4, 20);
		progresslabel.setLocation(size.width/8, 150);
	}
	
	public void setFileCount(int count){
		project_file_count=count;
	}

	public void addLog(String log) {
		int length = logs.getDocument().getLength();
		try {
			logs.getDocument().insertString(length, log + "\n", normalset);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		current_count++;
		repaint();
	}

	public void addError(String error) {
		int length = logs.getDocument().getLength();
		try {
			logs.getDocument().insertString(length, error + "\n", errorset);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		repaint();
	}

	@Override
	protected void paintComponent(Graphics arg0) {
		super.paintComponent(arg0);
		removeAll();
		paintButton();
		paintLogPanel();

	}

	private void paintButton() {
		// input configure button
		JLabel inputlabel = new JLabel(inputpath);
		inputlabel.setBackground(Color.WHITE);
		inputlabel.setBorder(BorderFactory.createLoweredBevelBorder());
		inputlabel.setSize(size.width * 5 / 8, 30);
		inputlabel.setLocation(size.width / 8, 60);
		add(inputlabel);

		JButton inputbtn = new JButton("Input");
		inputbtn.setSize(size.width / 8, 30);
		inputbtn.setLocation(size.width * 6 / 8, 60);

		inputbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("."));
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); // select
																				// directory

				chooser.showOpenDialog(null);
				File file = chooser.getSelectedFile();
				inputpath = file.getPath();
				if (file.exists()) {
					//clear content
					try {
						logs.getDocument().remove(0, logs.getDocument().getLength());
						project_file_count=0;
						current_count=0;
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
					
					new Thread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							TagInitController.getInstance().initProject(file);
						}
					}).start();
				}
				repaint();
			}
		});
		add(inputbtn);

		// output configure button
		JLabel outputlabel = new JLabel(outputpath);
		outputlabel.setBackground(Color.WHITE);
		outputlabel.setBorder(BorderFactory.createLoweredBevelBorder());
		outputlabel.setSize(size.width * 5 / 8, 30);
		outputlabel.setLocation(size.width / 8, 110);
		add(outputlabel);

		JButton outputbtn = new JButton("Output");
		outputbtn.setSize(size.width / 8, 30);
		outputbtn.setLocation(size.width * 6 / 8, 110);

		outputbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File("c://"));
				chooser.setFileSelectionMode(1); // select directory

				chooser.showOpenDialog(null);
				File file = chooser.getSelectedFile();
				outputpath = file.getPath();
				if (file.exists()) {
					TagInitController.getInstance().initOutputDir(file);
					repaint();
				}
			}
		});
		add(outputbtn);
	}

	private void paintLogPanel() {
		if(project_file_count==0){
			bar.setValue(0);	
		}
		else {
			bar.setValue(current_count*100/project_file_count);
			progresslabel.setText("Progress:"+current_count+"/"+project_file_count);
		}
		add(bar);
		add(progresslabel);
		
		JScrollPane pane = new JScrollPane(logs);
		DefaultCaret caret = (DefaultCaret) logs.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		pane.setSize(size.width * 3 / 4, 350);
		pane.setLocation(size.width / 8, 200);
		add(pane);
	}
}