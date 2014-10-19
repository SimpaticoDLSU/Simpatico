package preprocess;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;

import javax.swing.SwingConstants;
import javax.swing.JToolBar;
import javax.swing.JLayeredPane;

import java.awt.SystemColor;

import javax.swing.JPanel;
import javax.swing.border.Border;

import java.awt.Font;
import java.awt.TextArea;
import java.awt.Button;
import java.awt.Color;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JButton;
import javax.swing.border.MatteBorder;
import java.awt.ComponentOrientation;

public class GUI_Start {

	private JFrame baseFrame;
	private JLabel lblSimpatico;
	public static final int SCROLLBARS_VERTICAL_ONLY = 1;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI_Start window = new GUI_Start();
					window.baseFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI_Start() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		baseFrame = new JFrame();
		baseFrame.setName("baseFrame");
		baseFrame.setBounds(100, 100, 761, 388);
		baseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//custom 	
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setOpaque(true);
		layeredPane.setForeground(SystemColor.controlHighlight);
		layeredPane.setBackground(new Color(0, 153, 102));
		baseFrame.getContentPane().add(layeredPane, BorderLayout.CENTER);	
		//custom
		/*
		baseFrame.setUndecorated(true);
		baseFrame.setShape(new RoundRectangle2D.Double(10, 10, 3000, 4000, 50, 50));
		baseFrame.setSize(300, 200);
		baseFrame.setVisible(true);*/
		
		lblSimpatico = new JLabel("Simpatico");
		lblSimpatico.setHorizontalAlignment(SwingConstants.CENTER);
		lblSimpatico.setBounds(0, 6, 761, 95);
		layeredPane.add(lblSimpatico);
		lblSimpatico.setFont(new Font("Carrington", Font.PLAIN, 60));
		layeredPane.setLayer(lblSimpatico, 5);
		lblSimpatico.setForeground(SystemColor.text);
		
		JLayeredPane layeredPane_1 = new JLayeredPane();
		layeredPane_1.setOpaque(true);
		layeredPane_1.setBackground(new Color(204, 204, 204));
		layeredPane_1.setBounds(0, 110, 761, 256);
		layeredPane.add(layeredPane_1);
		
		TextArea tArea_Original = new TextArea();
		tArea_Original.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		tArea_Original.setBounds(10, 10, 741, 205);
		layeredPane_1.add(tArea_Original);
		tArea_Original.setFont(new Font("Segan", Font.PLAIN, 14));
		tArea_Original.setForeground(SystemColor.text);
		tArea_Original.setText("Paste your text that you want to simplify here.");
		layeredPane.setLayer(tArea_Original, 30);
		tArea_Original.setBackground(new Color(204, 204, 204));
		
		JButton btnSubmit = new JButton("Simplify Text");
		btnSubmit.setBounds(320, 221, 117, 29);
		layeredPane_1.add(btnSubmit);
		
		
		//Additional Edits
		
		
	}
	
	
}
