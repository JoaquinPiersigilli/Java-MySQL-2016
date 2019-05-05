package GUI;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.border.EmptyBorder;

import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

@SuppressWarnings("serial")
public class Inicio extends JFrame {
	
	private JPanel contentPane;
	private JMenu acciones;
	private Administrador administrador;
	private Empleado empleado;
	private JDesktopPane jDesktopPane1;
	private JButton butEmpleado;
	private JButton butAdministrador;
	private Inicio inicio;

	
	/**
	 * Create the frame.
	 */
	public Inicio() {
		inicio=this;
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1000, 800);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		jDesktopPane1 = new JDesktopPane();
		getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
		LayoutManager layout = getContentPane().getLayout();
		getContentPane().setLayout(null);
		
		// JMenu management
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		acciones = new JMenu();
		menuBar.add(acciones);
		acciones.setText("Acciones");
		
		butAdministrador = new JButton("Ingresar como Administrador");
		butAdministrador.setBounds(250, 164, 300, 40);
		getContentPane().add(butAdministrador);
		butAdministrador.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				try {
					getContentPane().setLayout(layout);
					setContentPane(contentPane);
					getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
					
					administrador = new Administrador();					
					administrador.setLocation(0, -12);
					administrador.setVisible(false);
					jDesktopPane1.add(administrador);

					empleado = new Empleado(inicio);
					empleado.setLocation(0, -12);
					empleado.setVisible(false);
					jDesktopPane1.add(empleado);
					
					administrador.setMaximum(true);
				} catch (PropertyVetoException ex) {
				}
				administrador.setVisible(true);
				empleado.setVisible(false);
				butAdministrador.setVisible(false);
				butEmpleado.setVisible(false);
			}
		});
	
		
		butEmpleado = new JButton("Ingresar como Empleado");
		butEmpleado.setBounds(250, 300, 300, 40);
		getContentPane().add(butEmpleado);
		butEmpleado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				try {
					getContentPane().setLayout(layout);
					setContentPane(contentPane);
					getContentPane().add(jDesktopPane1, BorderLayout.CENTER);
					
					administrador = new Administrador();					
					administrador.setLocation(0, -12);
					administrador.setVisible(false);
					jDesktopPane1.add(administrador);

					empleado = new Empleado(inicio);
					empleado.setLocation(0, -12);
					empleado.setVisible(false);
					jDesktopPane1.add(empleado);
					
					empleado.setMaximum(true);
				} catch (PropertyVetoException ex) {
				}
				administrador.setVisible(false);
				empleado.setVisible(true);
				butAdministrador.setVisible(false);
				butEmpleado.setVisible(false);
			}
		});
		
		
		JMenuItem menuAdmin = new JMenuItem();
		acciones.add(menuAdmin);
		menuAdmin.setText("Administrador");
		menuAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					administrador.setMaximum(true);
				} catch (PropertyVetoException e) {
				}
				administrador.setVisible(true);
				empleado.setVisible(false);
			}
		});

		JMenuItem menuEmpleado = new JMenuItem();
		acciones.add(menuEmpleado);
		menuEmpleado.setText("Empleados");
		menuEmpleado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				try {
					empleado.setMaximum(true);
				} catch (PropertyVetoException e) {
				}
				administrador.setVisible(false);
				empleado.setVisible(true);
			}
		});

		JSeparator jSeparator1 = new JSeparator();
		acciones.add(jSeparator1);

		JMenuItem salir = new JMenuItem();
		acciones.add(salir);
		salir.setText("Salir");
		salir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				administrador.desconectarBD();
				empleado.desconectarBD();
				System.exit(0);
			}
		});
	}
		
	public void quitarhome(){
		butEmpleado.setVisible(false);
		butAdministrador.setVisible(false);
	}
	public void mostrarhome(){
		butEmpleado.setVisible(true);
		butAdministrador.setVisible(true);		
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Inicio frame = new Inicio();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}