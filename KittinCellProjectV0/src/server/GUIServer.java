package server;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import com.sun.istack.internal.FinalArrayList;

public class GUIServer extends JPanel implements ActionListener{
	GridBagConstraints c;
	JScrollPane scrollPane;
	JTextPane textPane;
	ImageIcon background; //background
    Style serverStyle;
    JButton exit;
    JButton pause;
    int caretPos = 0;
    boolean paused = false;
    ArrayList<String> messages;
	
	public GUIServer(){
		messages = new ArrayList<String>();
		background = new ImageIcon("images/server4.jpg");
		setLayout(new GridBagLayout());
		exit = new JButton("Exit");
		exit.addActionListener(this);
		pause = new JButton("Pause Output");
		pause.addActionListener(this);
		c = new GridBagConstraints();
		c.gridx=0;
		c.gridy=0;
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1;
		c.weighty = 1;
		c.insets = new Insets(80, 40, 0, 40);
		setPreferredSize(new Dimension(800, 600));
		setMaximumSize(getPreferredSize());
		setMinimumSize(getPreferredSize());
		
		textPane = new JTextPane();
//		DefaultCaret caret = (DefaultCaret)textPane.getCaret();
//		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		textPane.setFont(new Font("Verdana", Font.PLAIN, 12));		
		serverStyle = textPane.addStyle("style", null);
		StyleConstants.setForeground(serverStyle, Color.black);
		scrollPane = new JScrollPane(textPane);
//		textPane.setOpaque(false);
//		scrollPane.setOpaque(false);
		textPane.setEditable(false);
		textPane.setBackground(new Color(0, 0, 255, 00));
		scrollPane.setBackground(new Color(100, 100, 255, 0));
		textPane.setBorder(null);
		scrollPane.setBorder(null);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER );
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS );
		
		c.gridwidth=2;		
		add(scrollPane,c);
		c.gridwidth=1;
		c.gridy=1;
		c.fill = GridBagConstraints.NONE;
		c.insets = new Insets(0, 10, 10, 10);
		c.weightx = .2;
		c.weighty = .2;
		c.anchor = GridBagConstraints.LINE_END;
		add(exit, c);
		c.gridx=1;
		c.anchor = GridBagConstraints.LINE_START;
		add(pause, c);
		setVisible(true);
	}
	
	public void updateTextPane(final String text) {
	  SwingUtilities.invokeLater(new Runnable() {
	    public void run() {
	      Document doc = textPane.getDocument();
	  	if(doc.getLength()>30000){
			try {
				doc.remove(0, 1000);
				caretPos -= 1000;
			} catch (BadLocationException e) {
				System.err.println("YOLO");
			}
		}
	      try {
	        doc.insertString(doc.getLength(), text, null);
	      } catch (BadLocationException e) {
	        throw new RuntimeException(e);
	      }
	      if(!paused){
				caretPos = doc.getLength()-1;
			}
			textPane.setCaretPosition(caretPos);
	    }
	  });
	}
	
	public void paintComponent(Graphics g){
		background.paintIcon(this, g, 0, 0);
		textPane.revalidate();
		//scrollPane.revalidate();
		//textPane.repaint();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(exit)){
        	System.exit(1);
        }
		else if(e.getSource().equals(pause)){
			if(pause.getText().equals("Pause Output")){
				paused = true;
				pause.setText("Unpause Output");				
			}
			else {
				paused = false;
				pause.setText("Pause Output");				
			}
		}
	}
}

