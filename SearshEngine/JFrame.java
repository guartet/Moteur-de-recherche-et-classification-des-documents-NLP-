package SearshEngine;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JTable;

public class JFrame extends javax.swing.JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JFrame frame = new JFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public JFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 651, 398);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(102, 204, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.setIcon(new ImageIcon("C:\\Users\\Elbouadi\\eclipse-workspace\\Moteur1\\imgs\\hiclipart.com-id_dytwu.png"));
		btnNewButton.setBounds(564, 11, 61, 54);
		contentPane.add(btnNewButton);
		
		JLabel lblImport = new JLabel("Import");
		lblImport.setForeground(SystemColor.textHighlight);
		lblImport.setBackground(SystemColor.textHighlight);
		lblImport.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblImport.setBounds(574, 76, 46, 14);
		contentPane.add(lblImport);
		
		JLabel lblMoteurDeRecherche = new JLabel("Moteur de Recherche");
		lblMoteurDeRecherche.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblMoteurDeRecherche.setForeground(SystemColor.textHighlight);
		lblMoteurDeRecherche.setBounds(202, 23, 208, 54);
		contentPane.add(lblMoteurDeRecherche);
		
		textField = new JTextField();
		textField.setBounds(153, 73, 295, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		table = new JTable();
		table.setBackground(SystemColor.textHighlightText);
		table.setBounds(34, 123, 566, 225);
		contentPane.add(table);
	}
}
