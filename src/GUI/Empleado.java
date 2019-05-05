package GUI;

import java.awt.BorderLayout;
import javax.swing.JComboBox;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Date;

import javax.swing.JInternalFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import quick.dbtable.DBTable;

@SuppressWarnings("serial")
public class Empleado extends JInternalFrame{

	private JTextField textField;
	private JPasswordField passwordField;
	private JTextField docTipo;
	private JTextField docNum;
	private JLabel UserName;
	private JLabel password;
	private JLabel ciuSalida;
	private JLabel ciuLlegada;
	private JLabel vuelosIda;
	private JLabel vuelosVuelta;
	private JLabel consultar;
	private JLabel asientos,asientos2;
	private JLabel fechasIda;
	private JLabel fechasVuelta;	
	private JLabel docTipoLabel;
	private JLabel docNumLabel;
	private DBTable table, tablaIda, tablaVuelta,tablaAsientosIda,tablaAsientosVuelta;
	private DBTable tablaFechasIda, tablaFechasVuelta;
	private String usuario = "";
	private String clave = "";
	private String driver;
	private String servidor;
	private String baseDatos;
	private String uriConexion;
	private String ciudad1 = null;
	private String ciudad2 = null;
	private String fechaIda = null;
	private String fechaVuelta = null;
	private String ida;
	private String vuelta;
	private String result;
	private String datoVuelo1;
	private String datoFecha1;
	private String datoClase1;
	private String datoVuelo2;
	private String datoFecha2;
	private String datoClase2;
	private String datoTipoDoc;
	private String datoNumDoc;
	private JButton btnIngresar;
	private JButton aceptar;
	private JButton aceptarIdaVuelta;
	private JButton aceptarFechaIda;
	private JButton aceptarFechaVuelta;
	private JButton mostrarAsientosIda;
	private JButton mostrarAsientosVuelta;
	private JButton reservarIda;
	private JButton reservarIdaYvuelta;
	private JButton volver;
	private JButton volver2;
	private JButton home;
	private JButton confirmar;
	private JButton confirmar2;
	protected Connection conexionBD = null;
	private JComboBox listaCiudades;
	private JComboBox listaCiudadesLlegada;
	private JComboBox<String> idaVuelta;	
	
	public Empleado(Inicio inicio){
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);

		btnIngresar = new JButton("Ingresar");
		btnIngresar.setBounds(300, 164, 89, 23);
		getContentPane().add(btnIngresar);
		btnIngresar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				inicio.quitarhome();
				usuario = "empleado";
				clave = "empleado";

				conectarBD();
				if (conexionBD != null) {
					try {
						usuario = textField.getText().trim();
						char[] arr = passwordField.getPassword();
						clave = "";
						for (int i = 0; i < arr.length; i++) {
							clave = clave + arr[i];
						}
						
						Statement stm = conexionBD.createStatement();

						boolean es = false;

						ResultSet rs = stm.executeQuery("select md5('" + clave + "') as pass");
						String str = "";
						while (rs.next()) {
							str = rs.getString("pass");
						}
						rs = stm.executeQuery("SELECT legajo, password FROM empleados WHERE legajo LIKE " + usuario);
					
						while (rs.next()) {
							es = rs.getString("legajo").equals(usuario) && rs.getString("password").equals(str);
							
						}

						if (!es)
							JOptionPane.showMessageDialog(null, "Datos incorrectos. Intente nuevamente.");
						else {
							textField.setVisible(false);
					     	passwordField.setVisible(false);
							btnIngresar.setVisible(false);
							UserName.setVisible(false);
							password.setVisible(false);
							home.setVisible(true);
							ciuSalida.setVisible(true);
							ciuLlegada.setVisible(true);
							aceptar.setVisible(true);
							listaCiudades.setVisible(true);
							listaCiudadesLlegada.setVisible(true);
							
							rs = stm.executeQuery("SELECT DISTINCT a.ciudad FROM aeropuertos as a JOIN vuelos_programados as vp ON vp.aeropuerto_salida=a.codigo");
							listaCiudades.removeAllItems();
							while (rs.next()) {
								listaCiudades.addItem(rs.getString("ciudad"));


							}
							listaCiudades.setSelectedIndex(-1);
							listaCiudades.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									JComboBox<String> cb = (JComboBox) e.getSource();
									ciudad1 = (String) cb.getSelectedItem();
									try{
										
									
									Statement stm = conexionBD.createStatement();
									
									
									ResultSet rs = stm.executeQuery("SELECT w.ciudad FROM (SELECT DISTINCT vp.aeropuerto_llegada FROM aeropuertos as a JOIN vuelos_programados as vp ON vp.aeropuerto_salida=a.codigo where a.ciudad='"+ciudad1+"') as x JOIN aeropuertos as w ON x.aeropuerto_llegada=w.codigo");
									listaCiudadesLlegada.removeAllItems();
									while (rs.next()) {
										listaCiudadesLlegada.addItem(rs.getString("w.ciudad"));
									}
									
									rs.close();
									stm.close();
									
									}catch	(SQLException e1) {
										e1.printStackTrace();
									}
								}
							});
							
							
							listaCiudadesLlegada.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent e) {
									JComboBox<String> cb = (JComboBox) e.getSource();
									ciudad2 = (String) cb.getSelectedItem();
								}
							});
						}
						
						rs.close();
						stm.close();

					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		listaCiudadesLlegada = new JComboBox();
		listaCiudadesLlegada.setBounds(70, 160, 120, 20);
		listaCiudadesLlegada.setVisible(false);
		getContentPane().add(listaCiudadesLlegada);
		
		listaCiudades = new JComboBox();
		listaCiudades.setBounds(70, 70, 120, 20);
		listaCiudades.setVisible(false);
		getContentPane().add(listaCiudades);
		
		idaVuelta = new JComboBox();
		idaVuelta.setBounds(70, 70, 150, 20);
		idaVuelta.setVisible(false);
		getContentPane().add(idaVuelta);
		
		ida = "SOLO IDA";
		vuelta = "IDA y VUELTA";
		
		idaVuelta.addItem(ida);
		idaVuelta.addItem(vuelta);	
		idaVuelta.setSelectedIndex(-1);
		
		volver = new JButton("Regresar");
		volver.setBounds(70,650,100,30);
		getContentPane().add(volver);
		volver.setVisible(false);
		volver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				home.setVisible(true);
				textField.setVisible(false);
		     	passwordField.setVisible(false);
				btnIngresar.setVisible(false);
				ciuSalida.setVisible(true);
				ciuLlegada.setVisible(true);
				aceptar.setVisible(true);
				listaCiudades.setVisible(true);
				listaCiudadesLlegada.setVisible(true);
				aceptarIdaVuelta.setVisible(false);
				UserName.setVisible(false);
				password.setVisible(false);
				idaVuelta.setVisible(false);
				volver.setVisible(false);
				consultar.setVisible(false);
				asientos.setVisible(false);
				asientos2.setVisible(false);
				mostrarAsientosIda.setVisible(false);
				mostrarAsientosVuelta.setVisible(false);
				tablaIda.setVisible(false);
				tablaVuelta.setVisible(false);
				tablaAsientosIda.setVisible(false);
				tablaAsientosVuelta.setVisible(false);
				fechasIda.setVisible(false);
				fechasVuelta.setVisible(false);
				
				if(vuelosIda!=null)
					vuelosIda.setVisible(false);
				if(vuelosVuelta!=null)
					vuelosVuelta.setVisible(false);
				
				tablaFechasIda.setVisible(false);
				tablaFechasVuelta.setVisible(false);
				
				aceptarFechaIda.setVisible(false);
				aceptarFechaVuelta.setVisible(false);
				
			}
		});
		
		home = new JButton("Regresar");
		home.setBounds(70,650,100,30);
		getContentPane().add(home);
		home.setVisible(false);
		home.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){		
				
				textField.setVisible(false);
		     	passwordField.setVisible(false);
				btnIngresar.setVisible(false);
				UserName.setVisible(false);
				password.setVisible(false);
				home.setVisible(false);
				ciuSalida.setVisible(false);
				ciuLlegada.setVisible(false);
				aceptar.setVisible(false);
				listaCiudades.setVisible(false);
				listaCiudadesLlegada.setVisible(false);	
				
				inicio.mostrarhome();
				
			}
		});
		
		volver2 = new JButton("Regresar");
		volver2.setBounds(70,650,100,30);
		getContentPane().add(volver2);
		volver2.setVisible(false);
		volver2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				volver2.setVisible(false);
				consultar.setVisible(true);
				volver.setVisible(true);						
				aceptarIdaVuelta.setVisible(true);					
				idaVuelta.setVisible(true);
				asientos.setVisible(false);
				asientos2.setVisible(false);
				mostrarAsientosIda.setVisible(false);
				mostrarAsientosVuelta.setVisible(false);
				tablaIda.setVisible(false);
				tablaVuelta.setVisible(false);
				tablaAsientosIda.setVisible(false);
				tablaAsientosVuelta.setVisible(false);
				fechasIda.setVisible(false);
				fechasVuelta.setVisible(false);
				reservarIda.setVisible(false);
				reservarIdaYvuelta.setVisible(false);
				docTipo.setVisible(false);
				docNum.setVisible(false);
				docTipoLabel.setVisible(false);
				docNumLabel.setVisible(false);
				confirmar.setVisible(false);
				confirmar2.setVisible(false);
				
				if(vuelosIda!=null)
					vuelosIda.setVisible(false);
				if(vuelosVuelta!=null)
					vuelosVuelta.setVisible(false);
				
				tablaFechasIda.setVisible(false);
				tablaFechasVuelta.setVisible(false);
				
				aceptarFechaIda.setVisible(false);
				aceptarFechaVuelta.setVisible(false);
				
			}
		});
		
		aceptar = new JButton("Aceptar");
		aceptar.setBounds(650, 650, 100, 30);
		getContentPane().add(aceptar);
		aceptar.setVisible(false);
		aceptar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(ciudad1==null || ciudad2==null){
					JOptionPane.showMessageDialog(null, "Asegurese de seleccionar ambas ciudades!.");
				}else{
				
				listaCiudades.setVisible(false);
				listaCiudadesLlegada.setVisible(false);
				ciuSalida.setVisible(false);
				ciuLlegada.setVisible(false);
				aceptar.setVisible(false);			
				consultar.setVisible(true);
				volver.setVisible(true);
				home.setVisible(false);
												

				idaVuelta.setVisible(true);
				
				aceptarIdaVuelta.setVisible(true);	
			}
			}
		});
		
		
		aceptarFechaIda = new JButton("Ver Vuelos");
		aceptarFechaIda.setBounds(650, 650, 100, 30);
		getContentPane().add(aceptarFechaIda);
		aceptarFechaIda.setVisible(false);
		aceptarFechaIda.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aceptarFechaIda.setVisible(false);
				
				if (tablaFechasIda.getRowCount() > 0) {
					fechaIda = Fechas.convertirDateAStringDB((Date)tablaFechasIda.getValueAt(tablaFechasIda.getSelectedRow(),0));
				}
									
				if(fechaIda==null){
					JOptionPane.showMessageDialog(null, "Asegurese de seleccionar una fecha!.");
				}else{
				
				fechasIda.setVisible(false);
				tablaFechasIda.setVisible(false);
				volver.setVisible(false);
				volver2.setVisible(true);
				
				
				vuelosIda = new JLabel("Vuelos de ida: "+ciudad1+"-"+ciudad2+ "-"+fechaIda);
				vuelosIda.setBounds(70, 10, 350, 100);
				getContentPane().add(vuelosIda);
				vuelosIda.setVisible(true);
			
				mostrarTablaIda();
				
			}
			}
		});
		
		aceptarFechaVuelta = new JButton("Ver Vuelos");
		aceptarFechaVuelta.setBounds(650, 650, 100, 30);
		getContentPane().add(aceptarFechaVuelta);
		aceptarFechaVuelta.setVisible(false);
		aceptarFechaVuelta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aceptarFechaVuelta.setVisible(false);
				
							
				if (tablaFechasIda.getRowCount() > 0) {
					fechaIda = Fechas.convertirDateAStringDB((Date)tablaFechasIda.getValueAt(tablaFechasIda.getSelectedRow(),0));
				}
				if(tablaFechasVuelta.getRowCount() > 0){
					fechaVuelta = Fechas.convertirDateAStringDB((Date)tablaFechasVuelta.getValueAt(tablaFechasVuelta.getSelectedRow(),0));
				}
				
				
				if(fechaIda==null || fechaVuelta==null){
					JOptionPane.showMessageDialog(null, "Asegurese de seleccionar una fecha!.");
				}else{
				
					fechasIda.setVisible(false);
					fechasVuelta.setVisible(false);					
					tablaFechasIda.setVisible(false);
					tablaFechasVuelta.setVisible(false);
					volver.setVisible(false);
					volver2.setVisible(true);
					
					vuelosIda = new JLabel("Vuelos de ida: "+ciudad1+"-"+ciudad2+ "-"+fechaIda);
					vuelosIda.setBounds(70, 10, 350, 100);
					getContentPane().add(vuelosIda);
					vuelosIda.setVisible(true);
				
					vuelosVuelta = new JLabel("Vuelos de vuelta: "+ciudad2+"-"+ciudad1+ "-"+fechaVuelta);
					vuelosVuelta.setBounds(70,300,350,100);
					getContentPane().add(vuelosVuelta);
					vuelosVuelta.setVisible(true);
					
					mostrarTablaIda();
					mostrarTablaVuelta();
			}
			}
		});
		
		aceptarIdaVuelta = new JButton("Aceptar");
		aceptarIdaVuelta.setBounds(650, 650, 100, 30);
		getContentPane().add(aceptarIdaVuelta);
		aceptarIdaVuelta.setVisible(false);		
		aceptarIdaVuelta.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					volver.setVisible(false);
					volver2.setVisible(true);
					consultar.setVisible(false);
					aceptarIdaVuelta.setVisible(false);
					idaVuelta.setVisible(false);
					
					result = (String) idaVuelta.getSelectedItem();
					if(result==ida){
						fechasIda.setVisible(true);;
						mostrarFechasIda();
						aceptarFechaIda.setVisible(true);				
											
						
					}else if (result==vuelta){	
						fechasIda.setVisible(true);
						fechasVuelta.setVisible(true);
						mostrarFechasIda();
						mostrarFechasVuelta();
						aceptarFechaVuelta.setVisible(true);
										
						
					}else{
						JOptionPane.showMessageDialog(null, "Seleccione alguna opción!.");
						consultar.setVisible(true);
						aceptarIdaVuelta.setVisible(true);
						idaVuelta.setVisible(true);
					}
				}
		});		
		
		textField = new JTextField();
		textField.setBounds(400, 74, 109, 20);
		getContentPane().add(textField);
		textField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(400, 123, 109, 20);
		getContentPane().add(passwordField);

		UserName = new JLabel("Legajo");
		UserName.setBounds(300, 74, 86, 20);
		getContentPane().add(UserName);

		ciuSalida = new JLabel("Seleccione la ciudad de Salida");
		ciuSalida.setBounds(70, 10, 300, 100);
		getContentPane().add(ciuSalida);
		ciuSalida.setVisible(false);		
		
		ciuLlegada = new JLabel("Seleccione la ciudad de Llegada");
		ciuLlegada.setBounds(70, 100, 300, 100);
		getContentPane().add(ciuLlegada);
		ciuLlegada.setVisible(false);
		
		consultar = new JLabel("Desea consultar por vuelos de SOLO IDA o IDA y VUELTA?");
		consultar.setBounds(70, 10, 350, 100);
		getContentPane().add(consultar);
		consultar.setVisible(false);	
		
		asientos = new JLabel("Disponibilidad de Asientos");
		asientos.setBounds(520, 10, 350, 100);
		getContentPane().add(asientos);
		asientos.setVisible(false);	
		
		asientos2 = new JLabel("Disponibilidad de Asientos");
		asientos2.setBounds(520, 300, 350, 100);
		getContentPane().add(asientos2);
		asientos2.setVisible(false);	
				
		password = new JLabel("Password");
		password.setBounds(300, 126, 86, 14);
		getContentPane().add(password);
		
		fechasIda = new JLabel("Estas son las fechas disponibles para la Ida, seleccione una");
		fechasIda.setBounds(70,10,400,100);
		getContentPane().add(fechasIda);
		fechasIda.setVisible(false);
		
		fechasVuelta = new JLabel("Estas son las fechas disponibles para la Vuelta, seleccione una");
		fechasVuelta.setBounds(70,300,400,100);
		getContentPane().add(fechasVuelta);
		fechasVuelta.setVisible(false);		

		table = new DBTable();
		table.setBounds(36, 11, 213, 248);
		table.setVisible(false);
		table.setEditable(false);
		getContentPane().add(table, BorderLayout.CENTER);

		tablaIda = new DBTable();
		tablaIda.setBounds(10, 90, 450, 200);
		tablaIda.setVisible(false);
		tablaIda.setEditable(false);
		getContentPane().add(tablaIda, BorderLayout.CENTER);	
		
		tablaVuelta = new DBTable();
		tablaVuelta.setBounds(10, 400, 450, 200);
		tablaVuelta.setVisible(false);
		tablaVuelta.setEditable(false);
		getContentPane().add(tablaVuelta, BorderLayout.CENTER);
		
		tablaAsientosIda = new DBTable();
		tablaAsientosIda.setBounds(500, 90, 200, 200);
		tablaAsientosIda.setVisible(false);
		tablaAsientosIda.setEditable(false);
		getContentPane().add(tablaAsientosIda, BorderLayout.CENTER);
		
		tablaAsientosVuelta = new DBTable();
		tablaAsientosVuelta.setBounds(500, 400, 200, 200);
		tablaAsientosVuelta.setVisible(false);
		tablaAsientosVuelta.setEditable(false);
		getContentPane().add(tablaAsientosVuelta, BorderLayout.CENTER);
		
		tablaFechasIda = new DBTable();
		tablaFechasIda.setBounds(70, 90, 130, 200);
		tablaFechasIda.setVisible(false);
		tablaFechasIda.setEditable(false);
		getContentPane().add(tablaFechasIda, BorderLayout.CENTER);
		
		tablaFechasVuelta = new DBTable();
		tablaFechasVuelta.setBounds(70, 380, 130, 200);
		tablaFechasVuelta.setVisible(false);
		tablaFechasVuelta.setEditable(false);
		getContentPane().add(tablaFechasVuelta, BorderLayout.CENTER);
		
		docTipo = new JTextField();
		docTipo.setBounds(430, 74, 109, 20);
		getContentPane().add(docTipo);
		docTipo.setColumns(10);
		docTipo.setVisible(false);
		
		docNum = new JTextField();
		docNum.setBounds(430, 123, 109, 20);
		getContentPane().add(docNum);
		docNum.setColumns(10);
		docNum.setVisible(false);
		
		docTipoLabel = new JLabel("Tipo de Documento del pasajero: ");
		docTipoLabel.setBounds(200, 74, 200, 20);
		getContentPane().add(docTipoLabel);
		docTipoLabel.setVisible(false);
		
		docNumLabel = new JLabel("Numero de Documento del pasajero:");
		docNumLabel.setBounds(200, 126, 220, 14);
		getContentPane().add(docNumLabel);
		docNumLabel.setVisible(false);
		
		mostrarAsientosIda = new JButton("Mostrar Disponibilidad");
		mostrarAsientosIda.setBounds(750, 90, 200, 30);
		getContentPane().add(mostrarAsientosIda);
		mostrarAsientosIda.setVisible(false);		
		mostrarAsientosIda.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try{
					if (tablaIda.getRowCount() > 0) {
						String consulta="SELECT Clase, Disponibles as 'Asientos Disponibles', Precio FROM vuelos_disponibles WHERE vuelo='"+tablaIda.getValueAt(tablaIda.getSelectedRow(), 0)+"' && fecha='"+fechaIda+"'";
						tablaAsientosIda.setVisible(true);
						tablaAsientosIda.setSelectSql(consulta);
						tablaAsientosIda.refresh();
						int columnas = tablaAsientosIda.getColumnCount();
						for (int i = 0; i < columnas; i++) {
							tablaAsientosIda.getColumn(i).setMinWidth(130);
							
						if(result == vuelta){
								if(tablaAsientosVuelta.isVisible()){
									reservarIdaYvuelta.setVisible(true);
								}
							}
							else reservarIda.setVisible(true);
						}
						
					
					}
					
					
					}catch (SQLException e1) {
						e1.printStackTrace();
					}
				}		
		});
		
		mostrarAsientosVuelta = new JButton("Mostrar Disponibilidad");
		mostrarAsientosVuelta.setBounds(750, 400, 200, 30);
		getContentPane().add(mostrarAsientosVuelta);
		mostrarAsientosVuelta.setVisible(false);		
		mostrarAsientosVuelta.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try{
						
					if (tablaVuelta.getRowCount() > 0) {
						String consulta="SELECT Clase, Disponibles as 'Asientos Disponibles', Precio FROM vuelos_disponibles WHERE vuelo='"+tablaVuelta.getValueAt(tablaVuelta.getSelectedRow(), 0)+"' && fecha='"+fechaVuelta+"'";
						tablaAsientosVuelta.setVisible(true);
						tablaAsientosVuelta.setSelectSql(consulta);
						tablaAsientosVuelta.refresh();
						int columnas = tablaAsientosVuelta.getColumnCount();
						for (int i = 0; i < columnas; i++) {
							tablaAsientosVuelta.getColumn(i).setMinWidth(130);
						}
						
						if(tablaAsientosIda.isVisible()){
							reservarIdaYvuelta.setVisible(true);
						}
						
					
					}
					
					
					}catch (SQLException e1) {
						e1.printStackTrace();
					}
				}		
		});
		
		
		reservarIda = new JButton("Reservar vuelo");
		reservarIda.setBounds(750, 650, 200, 30);
		getContentPane().add(reservarIda);
		reservarIda.setVisible(false);		
		reservarIda.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					datoVuelo1 = (String)tablaIda.getValueAt(tablaIda.getSelectedRow(), 0);
					datoFecha1 = Fechas.convertirDateAStringDB((Date)tablaIda.getValueAt(tablaIda.getSelectedRow(), 1));
					datoClase1 = (String)tablaAsientosIda.getValueAt(tablaAsientosIda.getSelectedRow(), 0);
					int asientosDisp = ((BigDecimal) tablaAsientosIda.getValueAt(tablaAsientosIda.getSelectedRow(), 1)).intValue();
					
				
					
					if(asientosDisp>0){
					
					reservarIda.setVisible(false);
					tablaIda.setVisible(false);
					tablaAsientosIda.setVisible(false);
					vuelosIda.setVisible(false);
					mostrarAsientosIda.setVisible(false);
					asientos.setVisible(false);
					
					docTipo.setVisible(true);
					docNum.setVisible(true);
					docTipoLabel.setVisible(true);
					docNumLabel.setVisible(true);					
					confirmar.setVisible(true);
					}
					else{
						JOptionPane.showMessageDialog(null, "No hay asientos disponibles para el vuelo "+datoVuelo1+" en la clase "+datoClase1+" !");						
					}
					
					
				}
		});
		
	    confirmar = new JButton("Confirmar Operacion");
	    confirmar.setBounds(200, 170, 200, 30);
		getContentPane().add(confirmar);
		confirmar.setVisible(false);		
		confirmar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					datoTipoDoc = docTipo.getText().trim();
					datoNumDoc  = docNum.getText().trim();

					try {
						Statement stm = conexionBD.createStatement();
						boolean es = false;
						
						ResultSet rs = stm.executeQuery("SELECT doc_tipo, doc_nro FROM pasajeros WHERE doc_tipo='"+datoTipoDoc+"' && doc_nro='"+datoNumDoc+"'");                                 
						
						while (rs.next()) {
							es = rs.getString("doc_tipo").equals(datoTipoDoc) && rs.getString("doc_nro").equals(datoNumDoc);	
						}

						if (!es)
							JOptionPane.showMessageDialog(null, "Ese pasajero no existe en la base de datos. Intente nuevamente.");
						else {
							stm.execute("call reservar_ida("+ "'"+datoVuelo1 +"'"+"," + "'"+datoFecha1 +"'"+"," +"'"+ datoClase1 +"'"+ "," + "'"+datoTipoDoc + "'"+"," + datoNumDoc + "," + usuario + ")");
							rs = stm.getResultSet();
							rs.next();
							JOptionPane.showMessageDialog(null, rs.getString("resultado"));

							stm.close();
							rs.close();
						}
						
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					
					
				}
		});
		
		
		reservarIdaYvuelta = new JButton("Reservar vuelos");
		reservarIdaYvuelta.setBounds(750, 650, 200, 30);
		getContentPane().add(reservarIdaYvuelta);
		reservarIdaYvuelta.setVisible(false);		
		reservarIdaYvuelta.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {				
					
					datoVuelo1 = (String)tablaIda.getValueAt(tablaIda.getSelectedRow(), 0);
					datoFecha1 = Fechas.convertirDateAStringDB((Date)tablaIda.getValueAt(tablaIda.getSelectedRow(), 1));
					datoClase1 = (String)tablaAsientosIda.getValueAt(tablaAsientosIda.getSelectedRow(), 0);
					int asientosDisp = ((BigDecimal) tablaAsientosIda.getValueAt(tablaAsientosIda.getSelectedRow(), 1)).intValue();
					
					datoVuelo2 = (String)tablaVuelta.getValueAt(tablaVuelta.getSelectedRow(), 0);
					datoFecha2 = Fechas.convertirDateAStringDB((Date)tablaVuelta.getValueAt(tablaVuelta.getSelectedRow(), 1));
					datoClase2 = (String)tablaAsientosVuelta.getValueAt(tablaAsientosVuelta.getSelectedRow(), 0);
					int asientosDisp2 = ((BigDecimal) tablaAsientosVuelta.getValueAt(tablaAsientosVuelta.getSelectedRow(), 1)).intValue();
					
					
				
					if(!(asientosDisp>0) && !(asientosDisp2>0)){
						JOptionPane.showMessageDialog(null, "No hay asientos disponibles para los vuelos seleccionados en las clases seleccionadas!");						
					}else{
						if(asientosDisp>0){
							if(asientosDisp2>0){
					
								reservarIda.setVisible(false);
								tablaIda.setVisible(false);
								tablaAsientosIda.setVisible(false);
								vuelosIda.setVisible(false);
								mostrarAsientosIda.setVisible(false);
								asientos.setVisible(false);
								
								reservarIdaYvuelta.setVisible(false);
								tablaVuelta.setVisible(false);
								tablaAsientosVuelta.setVisible(false);
								vuelosVuelta.setVisible(false);
								mostrarAsientosVuelta.setVisible(false);
								asientos2.setVisible(false);
								
								docTipo.setVisible(true);
								docNum.setVisible(true);
								docTipoLabel.setVisible(true);
								docNumLabel.setVisible(true);					
								confirmar2.setVisible(true);
							}
							else{
								JOptionPane.showMessageDialog(null, "No hay asientos disponibles para el vuelo "+datoVuelo2+" en la clase "+datoClase2+" !");						
							}
						}
						else{
							JOptionPane.showMessageDialog(null, "No hay asientos disponibles para el vuelo "+datoVuelo1+" en la clase "+datoClase1+" !");						
						}
					}
					
				}
		});
		
		confirmar2 = new JButton("Confirmar Operacion");
	    confirmar2.setBounds(200, 170, 200, 30);
		getContentPane().add(confirmar2);
		confirmar2.setVisible(false);		
		confirmar2.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					datoTipoDoc = docTipo.getText().trim();
					datoNumDoc  = docNum.getText().trim();

					try {
						Statement stm = conexionBD.createStatement();
						boolean es = false;
						
						ResultSet rs = stm.executeQuery("SELECT doc_tipo, doc_nro FROM pasajeros WHERE doc_tipo='"+datoTipoDoc+"' && doc_nro='"+datoNumDoc+"'");                                 
						
						while (rs.next()) {
							es = rs.getString("doc_tipo").equals(datoTipoDoc) && rs.getString("doc_nro").equals(datoNumDoc);	
						}

						if (!es)
							JOptionPane.showMessageDialog(null, "Ese pasajero no existe en la base de datos. Intente nuevamente.");
						else {
							stm.execute("call reservar_ida_y_vuelta("+ "'"+datoVuelo1 +"'"+"," + "'"+datoFecha1 +"'"+"," +"'"+ datoClase1 +"'"+ "," + "'"+datoVuelo2+"'"+","+"'"+datoFecha2+"'"+","+"'"+datoClase2+"'"+","+"'"+datoTipoDoc + "'"+"," + datoNumDoc + "," + usuario + ")");
							rs = stm.getResultSet();
							rs.next();
							JOptionPane.showMessageDialog(null, rs.getString("resultado"));

							stm.close();
							rs.close();
						}
						
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					
					
				}
		});
		
		
		
	}
	
	private void mostrarFechasIda(){
		try {
			
			Statement stm = conexionBD.createStatement();
			
			ResultSet rs = stm.executeQuery("SELECT codigo FROM aeropuertos WHERE ciudad='" + ciudad1+ "'");
			rs.next();
			String codigo1 = rs.getString("codigo");
			
			rs = stm.executeQuery("SELECT codigo FROM aeropuertos WHERE ciudad='" + ciudad2+ "'");
			rs.next();
			String codigo2 = rs.getString("codigo");

			rs = stm.executeQuery("SELECT numero FROM vuelos_programados WHERE aeropuerto_salida='" + codigo1+ "' and aeropuerto_llegada='"+codigo2+"'");
			rs.next();
			String numeroVuelo=rs.getString("numero");
			
			String consulta;		
			
			tablaFechasIda.setVisible(true);
			
			consulta = "SELECT fecha FROM instancias_vuelo WHERE vuelo='"+numeroVuelo+"'";
			
			tablaFechasIda.setSelectSql(consulta);
			tablaFechasIda.createColumnModelFromQuery();

				
		    	  for (int i = 0; i < tablaFechasIda.getColumnCount(); i++)
		    	  { // para que muestre correctamente los valores de tipo TIME (hora)  		   		  
		    		 if	 (tablaFechasIda.getColumn(i).getType()==Types.TIME)  
		    		 {    		 
		    		  tablaFechasIda.getColumn(i).setType(Types.CHAR);  
		  	       	 }
		    		 // cambiar el formato en que se muestran los valores de tipo DATE
		    		 if	 (tablaFechasIda.getColumn(i).getType()==Types.DATE)
		    		 {
		    		    tablaFechasIda.getColumn(i).setDateFormat("dd/MM/YYYY");
		    		 }
		          }
				
				tablaFechasIda.refresh();
				int columnas = tablaFechasIda.getColumnCount();
				for (int i = 0; i < columnas; i++) {
					tablaFechasIda.getColumn(i).setMinWidth(130);
				}
				
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	private void mostrarFechasVuelta(){
try {
			
			Statement stm = conexionBD.createStatement();
			
			ResultSet rs = stm.executeQuery("SELECT codigo FROM aeropuertos WHERE ciudad='" + ciudad1+ "'");
			rs.next();
			String codigo1 = rs.getString("codigo");
			
			rs = stm.executeQuery("SELECT codigo FROM aeropuertos WHERE ciudad='" + ciudad2+ "'");
			rs.next();
			String codigo2 = rs.getString("codigo");

			rs = stm.executeQuery("SELECT numero FROM vuelos_programados WHERE aeropuerto_salida='" + codigo2+ "' and aeropuerto_llegada='"+codigo1+"'");
			rs.next();
			String numeroVuelo=rs.getString("numero");
			
			String consulta;		
			
			tablaFechasVuelta.setVisible(true);
			
			consulta = "SELECT fecha FROM instancias_vuelo WHERE vuelo='"+numeroVuelo+"'";
			
			tablaFechasVuelta.setSelectSql(consulta);
			tablaFechasVuelta.createColumnModelFromQuery();

				
		    	  for (int i = 0; i < tablaFechasVuelta.getColumnCount(); i++)
		    	  { // para que muestre correctamente los valores de tipo TIME (hora)  		   		  
		    		 if	 (tablaFechasVuelta.getColumn(i).getType()==Types.TIME)  
		    		 {    		 
		    		  tablaFechasVuelta.getColumn(i).setType(Types.CHAR);  
		  	       	 }
		    		 // cambiar el formato en que se muestran los valores de tipo DATE
		    		 if	 (tablaFechasVuelta.getColumn(i).getType()==Types.DATE)
		    		 {
		    		    tablaFechasVuelta.getColumn(i).setDateFormat("dd/MM/YYYY");
		    		 }
		          }
				
				tablaFechasVuelta.refresh();
				int columnas = tablaFechasVuelta.getColumnCount();
				for (int i = 0; i < columnas; i++) {
					tablaFechasVuelta.getColumn(i).setMinWidth(130);
				}
			
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
		
	}
	
	private void mostrarTablaIda(){
		try {
			
			Statement stm = conexionBD.createStatement();
			
			ResultSet rs = stm.executeQuery("SELECT codigo FROM aeropuertos WHERE ciudad='" + ciudad1+ "'");
			rs.next();
			String codigo1 = rs.getString("codigo");
			
			rs = stm.executeQuery("SELECT codigo FROM aeropuertos WHERE ciudad='" + ciudad2+ "'");
			rs.next();
			String codigo2 = rs.getString("codigo");

			String consulta;						
			
			tablaIda.setVisible(true);
										
			consulta = "SELECT s.vuelo as 'Numero de vuelo', fecha as Fecha, s.dia as Dia, hora_sale as 'Hora salida', hora_llega as 'Hora llegada', timediff(hora_llega,hora_sale) as 'Tiempo estimado', modelo_avion as 'Modelo de avion', aeropuerto_salida as 'Aeropuerto de salida', aeropuerto_llegada as 'Aeropuerto de llegada' FROM (salidas as s JOIN vuelos_programados as vp ON s.vuelo=vp.numero) JOIN instancias_vuelo as iv ON s.vuelo=iv.vuelo AND s.dia=iv.dia WHERE fecha='"+fechaIda+"' && aeropuerto_salida='" + codigo1
			+ "' && aeropuerto_llegada='" + codigo2 + "'";
											
			tablaIda.setSelectSql(consulta);
			tablaIda.createColumnModelFromQuery();  
				
		    	  for (int i = 0; i < tablaIda.getColumnCount(); i++)
		    	  { // para que muestre correctamente los valores de tipo TIME (hora)  		   		  
		    		 if	 (tablaIda.getColumn(i).getType()==Types.TIME)  
		    		 {    		 
		    		  tablaIda.getColumn(i).setType(Types.CHAR);  
		  	       	 }
		    		 // cambiar el formato en que se muestran los valores de tipo DATE
		    		 if	 (tablaIda.getColumn(i).getType()==Types.DATE)
		    		 {
		    		    tablaIda.getColumn(i).setDateFormat("YYYY/MM/dd");
		    		 }
		          }
				
				tablaIda.refresh();
				int columnas = tablaIda.getColumnCount();
				for (int i = 0; i < columnas; i++) {
					tablaIda.getColumn(i).setMinWidth(130);
				}
				
				mostrarAsientosIda.setVisible(true);
				asientos.setVisible(true);
				
				
			} catch (SQLException ex) {
				ex.printStackTrace();
			}		
	}
	
	
	private void mostrarTablaVuelta(){
		
		try{
		Statement stm = conexionBD.createStatement();
		
		ResultSet rs = stm.executeQuery("SELECT codigo FROM aeropuertos WHERE ciudad='" + ciudad1+ "'");
		rs.next();
		String codigo1 = rs.getString("codigo");
		
		rs = stm.executeQuery("SELECT codigo FROM aeropuertos WHERE ciudad='" + ciudad2+ "'");
		rs.next();
		String codigo2 = rs.getString("codigo");

		String consulta;						
		
		tablaVuelta.setVisible(true);
									
		consulta = "SELECT s.vuelo as 'Numero de vuelo', fecha as Fecha, s.dia as Dia, hora_sale as 'Hora salida', hora_llega as 'Hora llegada', timediff(hora_llega,hora_sale) as 'Tiempo estimado', modelo_avion as 'Modelo de avion', aeropuerto_salida as 'Aeropuerto de salida', aeropuerto_llegada as 'Aeropuerto de llegada' FROM (salidas as s JOIN vuelos_programados as vp ON s.vuelo=vp.numero) JOIN instancias_vuelo as iv ON s.vuelo=iv.vuelo AND s.dia=iv.dia WHERE fecha='"+fechaVuelta+"' && aeropuerto_salida='" + codigo2
				+ "' && aeropuerto_llegada='" + codigo1 + "'";
										
		tablaVuelta.setSelectSql(consulta);
		tablaVuelta.createColumnModelFromQuery();  
			
	    	  for (int i = 0; i < tablaVuelta.getColumnCount(); i++)
	    	  { // para que muestre correctamente los valores de tipo TIME (hora)  		   		  
	    		 if	 (tablaVuelta.getColumn(i).getType()==Types.TIME)  
	    		 {    		 
	    		  tablaVuelta.getColumn(i).setType(Types.CHAR);  
	  	       	 }
	    		 // cambiar el formato en que se muestran los valores de tipo DATE
	    		 if	 (tablaVuelta.getColumn(i).getType()==Types.DATE)
	    		 {
	    		    tablaVuelta.getColumn(i).setDateFormat("YYYY/MM/dd");
	    		 }
	          }
			
			tablaVuelta.refresh();
			int columnas = tablaVuelta.getColumnCount();
			for (int i = 0; i < columnas; i++) {
				tablaVuelta.getColumn(i).setMinWidth(130);
			}
			
			mostrarAsientosIda.setVisible(true);
			mostrarAsientosVuelta.setVisible(true);
			asientos.setVisible(true);
			asientos2.setVisible(true);
			
		
		} catch (SQLException ex) {
			ex.printStackTrace();
		}		
		
	}
	
	private void conectarBD() {
		try {
			driver = "com.mysql.jdbc.Driver";
			servidor = "localhost:3306";
			baseDatos = "vuelos";
			uriConexion = "jdbc:mysql://" + servidor + "/" + baseDatos;

			// establece conexiones con la B.D. "vuelos" usando directamante tablas DBTable
			table.connectDatabase(driver, uriConexion, usuario, clave);
			tablaIda.connectDatabase(driver,uriConexion,usuario,clave);
			tablaVuelta.connectDatabase(driver,uriConexion,usuario,clave);
			tablaAsientosIda.connectDatabase(driver,uriConexion,usuario,clave);
			tablaAsientosVuelta.connectDatabase(driver,uriConexion,usuario,clave);
			tablaFechasIda.connectDatabase(driver,uriConexion,usuario,clave);
			tablaFechasVuelta.connectDatabase(driver,uriConexion,usuario,clave);
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
			if (tablaIda != null)
				tablaIda.close();
			if (tablaVuelta != null)
				tablaVuelta.close();
			if (tablaAsientosIda != null)
				tablaAsientosIda.close();
			if (tablaAsientosVuelta != null)
				tablaAsientosVuelta.close();
			if(tablaFechasIda != null)
				tablaFechasIda.close();
			if(tablaFechasVuelta != null)
				tablaFechasVuelta.close();
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
}
