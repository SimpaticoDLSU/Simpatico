package preprocess;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.SwingConstants;
import javax.swing.JToolBar;

public class GUI_Start {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI_Start window = new GUI_Start();
					window.frame.setVisible(true);
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
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblSimpatico = new JLabel("Simpatico");
		frame.getContentPane().add(lblSimpatico, BorderLayout.WEST);
		
		JLabel lblHeroesOfThe = new JLabel("Heroes of the Simpatico");
		lblHeroesOfThe.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(lblHeroesOfThe, BorderLayout.NORTH);
	}

}
