package preprocess;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import java.awt.BorderLayout;
import java.awt.SystemColor;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.SwingConstants;

import java.awt.Font;

import javax.swing.JTextPane;
import javax.swing.JTextArea;

import java.awt.Panel;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.TextArea;
import java.awt.Cursor;
import java.awt.Window.Type;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import net.miginfocom.swing.MigLayout;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.EmptyBorder;
import javax.swing.UIManager;

public class GUI_Splash {

	private JFrame frmSimpaticoAlpha;
	private String[] args;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI_Splash window = new GUI_Splash();
					window.frmSimpaticoAlpha.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
	}

	/**
	 * Create the application.
	 */
	public GUI_Splash() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSimpaticoAlpha = new JFrame();
		frmSimpaticoAlpha.setFocusTraversalPolicyProvider(true);
		frmSimpaticoAlpha.setVisible(true);
		frmSimpaticoAlpha.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		frmSimpaticoAlpha.setResizable(false);
		frmSimpaticoAlpha.getContentPane().setBackground(new Color(0, 153, 102));
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBackground(new Color(0, 153, 102));
		frmSimpaticoAlpha.getContentPane().add(layeredPane, BorderLayout.CENTER);
		
		JLayeredPane layeredPane_1 = new JLayeredPane();
		layeredPane.setLayer(layeredPane_1, 10);
		layeredPane_1.setBackground(new Color(0, 153, 102));
		layeredPane_1.setBounds(0, 0, 350, 458);
		layeredPane.add(layeredPane_1);
		
		JLabel lblSimpatico = new JLabel("Simpatico");
		lblSimpatico.setBorder(null);
		lblSimpatico.setFont(new Font("Carrington", Font.PLAIN, 52));
		lblSimpatico.setForeground(new Color(255, 255, 255));
		lblSimpatico.setHorizontalAlignment(SwingConstants.CENTER);
		lblSimpatico.setBounds(6, 16, 338, 64);
		layeredPane_1.add(lblSimpatico);
		
		JLabel lblVer = new JLabel("Ver. 0.1.0 ");
		lblVer.setForeground(new Color(255, 255, 255));
		lblVer.setFont(new Font("Segoe UI", Font.PLAIN, 28));
		lblVer.setBackground(new Color(255, 255, 255));
		lblVer.setHorizontalAlignment(SwingConstants.CENTER);
		lblVer.setBounds(6, 104, 338, 50);
		layeredPane_1.add(lblVer);
		
		JTextPane txt_Message = new JTextPane();
		txt_Message.setEditable(false);
		txt_Message.setText("Thank you for installing Simpatico (0.1.0) in your computer. Before proceeding, please do note that this copy is for evaluation purposes only. The output should not be used in real world scenarios. Do not copy the software without permission.  \n");
		txt_Message.setForeground(Color.WHITE);
		txt_Message.setOpaque(false);
		txt_Message.setBounds(16, 163, 317, 157);
		layeredPane_1.add(txt_Message);
		
		JLayeredPane layeredPane_2 = new JLayeredPane();
		layeredPane_2.setOpaque(true);
		layeredPane_2.setBackground(new Color(51, 51, 51));
		layeredPane_2.setBorder(null);
		layeredPane_1.setLayer(layeredPane_2, 20);
		layeredPane_2.setBounds(0, 360, 350, 98);
		layeredPane_1.add(layeredPane_2);
		
		JButton btn_Proceed = new JButton("Proceed");
		btn_Proceed.setBorder(UIManager.getBorder("Button.border"));
		btn_Proceed.setBackground(new Color(204, 204, 204));
		btn_Proceed.setBounds(67, 28, 220, 43);
		btn_Proceed.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				txt_Message.setText("Loading....");
				GUI_Start gs = new GUI_Start();
				frmSimpaticoAlpha.dispose();
				frmSimpaticoAlpha.setVisible(false);
				gs.main(args);
			}
		});
		layeredPane_2.setLayout(null);
		layeredPane_2.add(btn_Proceed);
		layeredPane_2.setLayer(btn_Proceed, 21);
		//Added
		StyledDocument doc = txt_Message.getStyledDocument();
		SimpleAttributeSet center = new SimpleAttributeSet();
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		doc.setParagraphAttributes(0, doc.getLength(), center, false);
		frmSimpaticoAlpha.setBackground(new Color(0, 0, 0));
		frmSimpaticoAlpha.setTitle("Simpatico Alpha 0.1");
		frmSimpaticoAlpha.setBounds(100, 100, 350, 480);
		frmSimpaticoAlpha.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
