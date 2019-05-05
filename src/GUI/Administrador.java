package GUI;

import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import java.awt.Color;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import quick.dbtable.DBTable;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Administrador extends JInternalFrame{
	
	private JTextField textField;
	private JPasswordField passwordField;
	private DBTable table;
	private JTextArea textArea;
	private JList list1, list2;
	private JScrollPane scrollPane1, scrollPane2;
	private JButton btnClear;
	private JButton btnEnter;
	private JButton btnIngresar;
	private JLabel lblPassword;
	private JLabel lblUserName;
	private String usuario = "";
	private String clave = "";
	private String driver;
	private String servidor;
	private String baseDatos;
	private String uriConexion;
	protected Connection conexionBD = null;

	/**
	 * Create the frame.
	 */
	
	public Administrador() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);

		textField = new JTextField();
		textField.setBounds(400, 74, 109, 20);
		getContentPane().add(textField);
		textField.setColumns(10);

		lblUserName = new JLabel("User Name");
		lblUserName.setBounds(300, 74, 86, 20);
		getContentPane().add(lblUserName);

		lblPassword = new JLabel("Password");
		lblPassword.setBounds(300, 126, 86, 14);
		getContentPane().add(lblPassword);

		passwordField = new JPasswordField();
		passwordField.setBounds(400, 123, 109, 20);
		getContentPane().add(passwordField);

		textArea = new JTextArea();
		textArea.setBounds(23, 14, 400, 100);
		getContentPane().add(textArea);
		textArea.setVisible(false);
		Border border = BorderFactory.createLineBorder(Color.BLACK);
		textArea.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(10, 10, 10, 10)));

		btnEnter = new JButton("Enter");
		btnEnter.setBounds(450, 31, 89, 23);
		btnEnter.setVisible(false);
		getContentPane().add(btnEnter);
		btnEnter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				refrescarTabla();
			}
		});

		btnClear = new JButton("Clear");
		btnClear.setBounds(450, 74, 89, 23);
		getContentPane().add(btnClear);
		btnClear.setVisible(false);
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.setText("");
				textArea.requestFocus();
				table.setVisible(false);
			}
		});

		btnIngresar = new JButton("Ingresar");
		btnIngresar.setBounds(300, 164, 89, 23);
		getContentPane().add(btnIngresar);

		table = new DBTable();
		table.setBounds(23, 300, 720, 250);
		table.setEditable(false);
		table.setVisible(false);
		getContentPane().add(table);

		btnIngresar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				usuario = textField.getText();
				char[] arr = passwordField.getPassword();
				clave = "";
				for (int i = 0; i < arr.length; i++) {
					clave = clave + arr[i];
				}
				if (usuario.equals("admin") && clave.equals("admin")) {
					conectarBD();
					if (conexionBD != null) {
						crearFrame();
					}
				} else {
					JOptionPane.showMessageDialog(null, "Datos invalidos. Intente nuevamente.");
				}
			}
		});
	}
	private void crearFrame() {
		this.textArea.setVisible(true);
		btnClear.setVisible(true);
		btnEnter.setVisible(true);
		btnIngresar.setVisible(false);
		textArea.setVisible(true);
		textField.setVisible(false);
		passwordField.setVisible(false);
		lblPassword.setVisible(false);
		lblUserName.setVisible(false);

		try {
			Statement stm = conexionBD.createStatement();
			ResultSet rs = stm.executeQuery("show tables from vuelos");

			ArrayList<String> ar = new ArrayList<String>();

			while (rs.next()) {
				String nombreTabla = rs.getString("Tables_in_vuelos");
				ar.add(nombreTabla);
			}

			scrollPane1 = new JScrollPane();
			scrollPane1.setBounds(23, 126, 160, 160);
			list1 = new JList(ar.toArray());
			scrollPane1.setBorder(new LineBorder(new Color(0, 0, 0), 2));
			list1.setBounds(23, 126, 160, 160);
			list1.setVisible(true);
			scrollPane1.setVisible(true);
			scrollPane1.setViewportView(list1);
			getContentPane().add(scrollPane1);
			list1.addListSelectionListener(new ListListener());

			scrollPane2 = new JScrollPane();
			scrollPane2.setBounds(264, 126, 160, 160);
			scrollPane2.setBorder(new LineBorder(new Color(0, 0, 0), 2));
			list2 = new JList();
			list2.setBounds(264, 126, 160, 160);
			list2.setVisible(false);
			scrollPane2.setVisible(false);
			scrollPane2.setViewportView(list2);
			getContentPane().add(scrollPane2);

			stm.close();
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void conectarBD() {
		try {
			driver = "com.mysql.jdbc.Driver";
			servidor = "localhost:3306";
			baseDatos = "vuelos";
			uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos;

			// establece una conexion con la B.D. "vuelos" usando directamante una tabla DBTable
			table.connectDatabase(driver, uriConexion, usuario, clave);
			conexionBD = java.sql.DriverManager.getConnection(uriConexion, usuario, clave);
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this,
					"Se produjo un error al intentar conectarse a la base de datos.\n" + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void desconectarBD() {
		try {
			if (table != null)
				table.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	private void refrescarTabla() {
		try {
			Statement stmt = this.conexionBD.createStatement();
			stmt.execute(textArea.getText().trim());
			ResultSet rs = stmt.getResultSet();

			if (rs != null) {
				// actualizo el contenido de la tabla.
				table.setVisible(true);
				table.refresh(rs);

				int columnas = table.getColumnCount();
				for (int i = 0; i < columnas; i++) {
					table.getColumn(i).setMinWidth(80);
				}
				rs.close();
			} else {
				table.setVisible(false);
				JOptionPane.showMessageDialog(null, "Consulta exitosa.");
			}
			stmt.close();

		} catch (SQLException ex) {
			// en caso de error, se muestra la causa en la consola
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(this), ex.getMessage() + "\n",
					"Error al ejecutar la consulta.", JOptionPane.ERROR_MESSAGE);

		}
	}

	private class ListListener implements ListSelectionListener {

		public void valueChanged(ListSelectionEvent arg0) {
			// Evito eventos duplicados
			if (!arg0.getValueIsAdjusting()) {

				String tablaSeleccionada = (String) list1.getSelectedValue();
				try {
					Statement stm2 = conexionBD.createStatement();
					ResultSet rs2 = stm2.executeQuery("show fields from " + tablaSeleccionada);

					ArrayList<String> ar2 = new ArrayList<String>();

					while (rs2.next()) {
						ar2.add(rs2.getString("Field"));
					}

					DefaultListModel model = new DefaultListModel();
					for (int i = 0; i < ar2.size(); i++) {
						model.addElement(ar2.get(i));
					}
					list2.setModel(model);
					list2.setVisible(true);
					scrollPane2.setVisible(true);

					rs2.close();
					stm2.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

	}
}