package net.sourceforge.barcodetickets.api;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class UrlEncoderGui {

	JFrame frame = null;
	JTextArea input = null;
	JTextArea output = null;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new UrlEncoderGui().run();
	}

	public void run() {
		// build the objects
		frame = new JFrame("URL Encode Tool");
		input = new JTextArea("Text to be encoded", 5, 60);
		output = new JTextArea("", 5, 60);
		Font consolas = new Font("Consolas", Font.PLAIN, 12);
		try {
			output.setText(URLEncoder.encode(input.getText(), "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		// appearances
		input.setFont(consolas);
		output.setFont(consolas);
		output.setEditable(false);
		output.setBackground(new Color(0xECECEC));

		// base functionality
		input.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				try {
					output.setText(URLEncoder.encode(input.getText(), "UTF-8"));
				} catch (UnsupportedEncodingException e1) {
					e1.printStackTrace();
				}
			}
		});

		LayoutManager g = new GridLayout(2, 1);
		JPanel contentPane = new JPanel(g);
		frame.setContentPane(contentPane);
		frame.getContentPane().add(input);
		frame.getContentPane().add(output);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

}