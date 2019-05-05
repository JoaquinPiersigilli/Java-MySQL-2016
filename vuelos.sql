#Archivo batch (vuelos.sql) para la creación de la 
#Base de datos del proyecto2

# Creo de la Base de Datos
CREATE DATABASE vuelos;

# selecciono la base de datos sobre la cual voy a hacer modificaciones
USE vuelos;

#-------------------------------------------------------------------------
# Creación Tablas para las entidades

CREATE TABLE ubicaciones(
	pais VARCHAR(45) NOT NULL,
	estado VARCHAR(45) NOT NULL,
	ciudad VARCHAR(45) NOT NULL,
	huso INT(2) NOT NULL,

	CONSTRAINT pk_ubicaciones
	PRIMARY KEY(pais,estado,ciudad)
	
) ENGINE=InnoDB;

CREATE TABLE aeropuertos(
	codigo VARCHAR(3) NOT NULL,
	nombre VARCHAR(45) NOT NULL,
	telefono VARCHAR(45) NOT NULL,
	direccion VARCHAR(45) NOT NULL,
	pais VARCHAR(45) NOT NULL,
	estado VARCHAR(45) NOT NULL,
 	ciudad VARCHAR(45) NOT NULL,
	
	CONSTRAINT pk_aeropuertos
	PRIMARY KEY (codigo),
	
	CONSTRAINT fk_aeropuertos_pais
	FOREIGN KEY (pais,estado,ciudad) REFERENCES ubicaciones(pais,estado,ciudad)
		
) ENGINE=InnoDB;

CREATE TABLE vuelos_programados(
	numero VARCHAR (45) NOT NULL,
	aeropuerto_salida  CHAR (3) NOT NULL,
	aeropuerto_llegada CHAR (3) NOT NULL,
	
	CONSTRAINT pk_vuelos_programados
	PRIMARY KEY (numero),
	
	CONSTRAINT fk_vuelos_programados_salida
	FOREIGN KEY (aeropuerto_salida) REFERENCES aeropuertos(codigo),

	CONSTRAINT fk_vuelos_programados_llegada
	FOREIGN KEY(aeropuerto_llegada) REFERENCES aeropuertos(codigo)	
	
) ENGINE=InnoDB;

CREATE TABLE modelos_avion(
	modelo VARCHAR(45) NOT NULL,
	fabricante VARCHAR(45) NOT NULL,
	cabinas INT(4) UNSIGNED NOT NULL,
	cant_asientos INT(4) UNSIGNED NOT NULL,
	
	CONSTRAINT pk_modelos_avion
	PRIMARY KEY (modelo)
	
) ENGINE=InnoDB;


CREATE TABLE salidas(
	vuelo VARCHAR (45) NOT NULL,
	dia enum ('Do','Lu','Ma','Mi','Ju','Vi','Sa') NOT NULL,
	hora_sale TIME NOT NULL,
	hora_llega TIME NOT NULL,
	modelo_avion VARCHAR(45) NOT NULL,
	
	CONSTRAINT pk_salidas
	PRIMARY KEY (vuelo,dia),
	
	CONSTRAINT fk_salidas_vuelo
	FOREIGN KEY (vuelo) REFERENCES vuelos_programados(numero),
	
	CONSTRAINT fk_salidas_modelo
	FOREIGN KEY (modelo_avion) REFERENCES modelos_avion(modelo)

) ENGINE=InnoDB;

CREATE TABLE instancias_vuelo(
	vuelo VARCHAR(45) NOT NULL,
	fecha DATE NOT NULL,
	dia ENUM ('Do','Lu','Ma','Mi','Ju','Vi','Sa') NOT NULL,
	estado VARCHAR (45),
	
	CONSTRAINT pk_instancias_vuelo
	PRIMARY KEY (vuelo,fecha),
	
	CONSTRAINT fk_instancias_vuelo_vuelo_dia
	FOREIGN KEY (vuelo,dia) REFERENCES salidas(vuelo,dia)		
	
) ENGINE=InnoDB;

CREATE TABLE clases(
	nombre VARCHAR(45) NOT NULL,
	porcentaje DECIMAL(2,2) UNSIGNED NOT NULL,
	
	CONSTRAINT pk_clases
	PRIMARY KEY (nombre)
	
) ENGINE=InnoDB;

CREATE TABLE comodidades(
	codigo INT(4) UNSIGNED NOT NULL,
	descripcion TEXT NOT NULL,
	
	CONSTRAINT pk_comodidades
	PRIMARY KEY (codigo)
	
) ENGINE=InnoDB;

CREATE TABLE pasajeros(
	doc_tipo VARCHAR(45) NOT NULL,
	doc_nro INT(9) UNSIGNED NOT NULL,
	apellido VARCHAR(45) NOT NULL,
	nombre VARCHAR(45) NOT NULL,
	direccion VARCHAR(45) NOT NULL,
	telefono VARCHAR(45) NOT NULL,
	nacionalidad VARCHAR(45) NOT NULL,
	
	CONSTRAINT pk_pasajeros
	PRIMARY KEY (doc_tipo, doc_nro)

) ENGINE=InnoDB;

CREATE TABLE empleados(
	legajo INT(9) UNSIGNED NOT NULL,
	password CHAR(32) NOT NULL,
	doc_tipo VARCHAR(45) NOT NULL,
	doc_nro INT(9) UNSIGNED NOT NULL,
	apellido VARCHAR(45) NOT NULL,
	nombre VARCHAR(45) NOT NULL,
	direccion VARCHAR(45) NOT NULL,
	telefono VARCHAR(45) NOT NULL,
	
	CONSTRAINT pk_empleados
	PRIMARY KEY (legajo)	
	

) ENGINE=InnoDB;

CREATE TABLE reservas(
	numero INT UNSIGNED NOT NULL AUTO_INCREMENT,
	fecha DATE NOT NULL,
	vencimiento DATE NOT NULL,
	estado VARCHAR(45) NOT NULL,
	doc_tipo VARCHAR(45) NOT NULL,
	doc_nro INT(9) UNSIGNED NOT NULL, 
	legajo INT(9) UNSIGNED NOT NULL,

	CONSTRAINT pk_reservas
	PRIMARY KEY (numero),
	
	CONSTRAINT fk_reservas_doctipo
	FOREIGN KEY (doc_tipo,doc_nro) REFERENCES pasajeros(doc_tipo,doc_nro),
		
	CONSTRAINT fk_reservas_legajo
	FOREIGN KEY (legajo) REFERENCES empleados(legajo)
	
) ENGINE=InnoDB;

CREATE TABLE brinda(
	vuelo VARCHAR (45) NOT NULL,
	dia enum ('Do','Lu','Ma','Mi','Ju','Vi','Sa') NOT NULL,
	clase VARCHAR(45) NOT NULL,
	precio DECIMAL(7,2) UNSIGNED NOT NULL,
	cant_asientos INT UNSIGNED NOT NULL,

	CONSTRAINT pk_brinda
	PRIMARY KEY (vuelo,dia,clase),
	
	CONSTRAINT fk_brinda_vuelo
	FOREIGN KEY (vuelo,dia) REFERENCES salidas(vuelo,dia),	
	
	CONSTRAINT fk_brinda_clase
	FOREIGN KEY (clase) REFERENCES clases(nombre)
	

) ENGINE=InnoDB;

CREATE TABLE posee(
	clase VARCHAR(45) NOT NULL,
	comodidad INT(4) UNSIGNED NOT NULL,
	
	CONSTRAINT pk_posee
	PRIMARY KEY (clase, comodidad),
	
	CONSTRAINT fk_posee_clase
	FOREIGN KEY (clase) REFERENCES clases(nombre),
	
	CONSTRAINT fk_posee_comodidad
	FOREIGN KEY (comodidad) REFERENCES comodidades(codigo)

) ENGINE=InnoDB;


CREATE TABLE reserva_vuelo_clase(
	numero INT UNSIGNED NOT NULL,
	vuelo VARCHAR(45) NOT NULL,
	fecha_vuelo DATE NOT NULL,
	clase VARCHAR(45) NOT NULL,
	
	CONSTRAINT pk_reserva_vuelo_clase
	PRIMARY KEY (numero, vuelo, fecha_vuelo),
	
	CONSTRAINT fk_reserva_vuelo_clase_numero
	FOREIGN KEY (numero) REFERENCES reservas(numero),
	
	CONSTRAINT fk_reserva_vuelo_clase_vuelo
	FOREIGN KEY (vuelo,fecha_vuelo) REFERENCES instancias_vuelo(vuelo,fecha),
	
	CONSTRAINT fk_reserva_vuelo_clase_clase
	FOREIGN KEY (clase) REFERENCES clases(nombre)
	
) ENGINE=InnoDB;


CREATE TABLE asientos_reservados(
	vuelo VARCHAR(45) NOT NULL,
	fecha DATE NOT NULL,
	clase VARCHAR(45) NOT NULL,
	cantidad INT(4) UNSIGNED NOT NULL,
	
	CONSTRAINT pk_asientos_reservados
	PRIMARY KEY (vuelo, fecha, clase),
	
	CONSTRAINT fk_asientos_reservados_vuelo
	FOREIGN KEY (vuelo,fecha) REFERENCES instancias_vuelo(vuelo,fecha),
	
	CONSTRAINT fk_asientos_reservados_clase
	FOREIGN KEY (clase) REFERENCES clases(nombre)
	

	
) ENGINE=InnoDB;

#-------------------------------------------------------------------------
# Creación de vista
#-------------------------------------------------------------------------
 
CREATE VIEW cantidad_reservas AS
SELECT b.vuelo, b.clase, ivv.fecha, count(rvc.vuelo and rvc.fecha_vuelo and rvc.clase) as reservas FROM (reserva_vuelo_clase as rvc right JOIN instancias_vuelo as iv ON rvc.fecha_vuelo=iv.fecha and rvc.vuelo=iv.vuelo) RIGHT JOIN (brinda as b JOIN instancias_vuelo as ivv ON b.vuelo=ivv.vuelo AND b.dia=ivv.dia) ON b.vuelo = iv.vuelo and b.dia=iv.dia and b.clase=rvc.clase and iv.fecha=ivv.fecha GROUP BY b.vuelo, b.clase, b.dia;

CREATE VIEW vuelos_disponibles AS
SELECT DISTINCT s.vuelo, modelo_avion, iv.fecha, iv.dia, hora_sale, hora_llega, timediff(hora_llega, hora_sale) AS DuracionVuelo, aeropuerto_salida, aeropuerto_llegada, asa.nombre as nombre_oringen, alle.nombre as nombre_destino, asa.estado as EstadoSalida, alle.estado as EstadoLlegada, asa.pais as PaisSalida, alle.pais as PaisLlegada, asa.ciudad as CiudadSalida, alle.ciudad as CiudadLlegada, b.clase as Clase, b.precio as Precio, (TRUNCATE(b.cant_asientos + (b.cant_asientos * c.porcentaje),0)) as cant_total_asientos,
(TRUNCATE(b.cant_asientos + (b.cant_asientos * c.porcentaje),0) - vr.reservas) AS Disponibles
FROM (((((vuelos_programados as vp JOIN aeropuertos as asa ON vp.aeropuerto_salida=asa.codigo) JOIN aeropuertos as alle ON vp.aeropuerto_llegada=alle.codigo) JOIN (salidas as s JOIN instancias_vuelo as iv ON s.vuelo = iv.vuelo AND s.dia = iv.dia) ON iv.vuelo = vp.numero) JOIN brinda as b ON b.vuelo = s.vuelo AND b.dia = s.dia) JOIN clases as c ON b.clase = c.nombre) LEFT JOIN cantidad_reservas as vr ON vr.vuelo = b.vuelo and vr.clase = b.clase and vr.fecha = iv.fecha;


#-------------------------------------------------------------------------
# Creación de usuarios y otorgamiento de privilegios
#-------------------------------------------------------------------------

#CREO EL ADMIN.

GRANT ALL PRIVILEGES ON vuelos.* TO 'admin'@'localhost'
IDENTIFIED BY 'admin' WITH GRANT OPTION;

#CREO EL USER EMPLEADO.

CREATE USER 'empleado'@'%' IDENTIFIED BY 'empleado';

#LE OTORGO LOS PRIVILEGIOS A EMPLEADO.

GRANT SELECT ON vuelos.ubicaciones TO 'empleado'@'%';
GRANT SELECT ON vuelos.aeropuertos TO 'empleado'@'%';
GRANT SELECT ON vuelos.vuelos_programados TO 'empleado'@'%';
GRANT SELECT ON vuelos.modelos_avion TO 'empleado'@'%';
GRANT SELECT ON vuelos.salidas TO 'empleado'@'%';
GRANT SELECT ON vuelos.instancias_vuelo TO 'empleado'@'%';
GRANT SELECT ON vuelos.clases TO 'empleado'@'%';
GRANT SELECT ON vuelos.comodidades TO 'empleado'@'%';
GRANT SELECT ON vuelos.empleados TO 'empleado'@'%';
GRANT SELECT ON vuelos.brinda TO 'empleado'@'%';
GRANT SELECT ON vuelos.posee TO 'empleado'@'%';
GRANT SELECT ON vuelos.vuelos_disponibles TO 'empleado'@'%';
GRANT SELECT ON vuelos.cantidad_reservas TO 'empleado'@'%';

GRANT SELECT, INSERT, UPDATE, DELETE ON vuelos.reservas TO 'empleado'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON vuelos.pasajeros TO 'empleado'@'%';
GRANT SELECT, INSERT, UPDATE, DELETE ON vuelos.reserva_vuelo_clase TO 'empleado'@'%';

CREATE USER 'cliente'@'%' IDENTIFIED BY 'cliente';

GRANT SELECT ON vuelos.vuelos_disponibles TO 'cliente'@'%';
GRANT SELECT ON vuelos.cantidad_reservas TO 'cliente'@'%';


#-------------------------------------------------------------------------
#Stored Procedures
#-------------------------------------------------------------------------


 delimiter !
CREATE PROCEDURE reservar_ida(IN numvuelo VARCHAR(45), IN fechaV DATE, IN claseV VARCHAR(45), IN tipodoc VARCHAR(45), IN numdoc INT(9), IN legajoE INT(9))
BEGIN
#Transaccion para reservar un vuelo de ida.
	DECLARE asientos_disponibles INT;
	DECLARE fecha_vencimiento DATE;
	DECLARE estado_reserva VARCHAR(45);
	DECLARE cantidad_asientos INT;
	DECLARE nro_reserva INT;
	DECLARE cant_asientos_reservados INT(4);
	DECLARE porcent DECIMAL(2,2);

	START TRANSACTION;
	
		IF  EXISTS (SELECT * FROM empleados WHERE legajo = legajoE) 
		 AND EXISTS (SELECT * FROM pasajeros WHERE doc_tipo=tipodoc && doc_nro=numdoc) 
		 AND EXISTS (SELECT * FROM brinda as b JOIN instancias_vuelo as iv ON b.vuelo=iv.vuelo AND b.dia=iv.dia WHERE clase=claseV && b.vuelo=numVuelo && fecha=fechaV)
	
		THEN 
	
			SELECT cantidad INTO cant_asientos_reservados FROM asientos_reservados WHERE vuelo=numvuelo && clase=claseV && fecha=fechaV FOR UPDATE;
			SELECT cant_asientos INTO cantidad_asientos FROM brinda AS b JOIN  instancias_vuelo AS iv ON b.vuelo=iv.vuelo AND iv.dia=b.dia WHERE fecha=fechaV && b.vuelo=numvuelo && clase=claseV;
			SELECT porcentaje INTO porcent FROM clases WHERE nombre=claseV;		
			SET asientos_disponibles = (TRUNCATE(cantidad_asientos + (cantidad_asientos * porcent),0) - cant_asientos_reservados);
			SET fecha_vencimiento = fechaV - INTERVAL 15 DAY;
		
			IF (asientos_disponibles > 0)
			THEN 
				IF((fecha_vencimiento-CURDATE())>=0)
				THEN
					#Actualizo cantidad de asientos reservados y tablas.	
					IF cant_asientos_reservados<cantidad_asientos 
					THEN 
						SET estado_reserva='Confirmada';
					ELSE 
						SET estado_reserva='En espera';
					END IF;
				
					#Actualizo tabla reservas
					INSERT INTO reservas(fecha, vencimiento, estado, doc_tipo, doc_nro, legajo) VALUES(CURDATE(),fecha_vencimiento,estado_reserva,tipodoc,numdoc,legajoE);
					SET nro_reserva = LAST_INSERT_ID();
			
					#Actualizo tabla reserva_vuelo_clase
					INSERT INTO reserva_vuelo_clase(numero,vuelo,fecha_vuelo,clase) VALUES(nro_reserva,numvuelo,fechaV,claseV);
			
					#Actualizo la cantidad de asientos reservados para el vuelo numvuelo y la clase claseV en la fecha fechaV.
					UPDATE asientos_reservados SET cantidad = cantidad +1 WHERE vuelo=numvuelo && clase=claseV && fecha=fechaV;
			
					SELECT 'La reserva se realizo con exito' AS resultado;
				ELSE
					SELECT 'No se puede reservar. Faltan menos de 15 dias para la fecha del Vuelo' AS resultado;
				END IF;
			ELSE 
				SELECT 'No hay lugares disponibles para realizar la reserva' AS resultado;
			END IF;
		ELSE
			SELECT 'No se puede realizar la reserva ya que el emplado, el pasajero o el vuelo no existe' AS resultado;
		END IF;
	COMMIT;

	
END; !
delimiter ;

delimiter !
CREATE PROCEDURE reservar_ida_y_vuelta(IN vuelo1 VARCHAR(45), IN fecha1 DATE, IN clase1 VARCHAR(45), IN vuelo2 VARCHAR(45), IN fecha2 DATE, IN clase2 VARCHAR(45), IN tipodoc VARCHAR(45), IN numdoc INT(9), IN legajoE INT(9)) 
BEGIN
	
	DECLARE cant_reservas_vuelo1 INT(4);
	DECLARE cant_reservas_vuelo2 INT(4);
	DECLARE	cantidad_asientos_vuelo1 INT;
	DECLARE cantidad_asientos_vuelo2 INT;
	DECLARE porcent_vuelo1 DECIMAL(2,2);
	DECLARE porcent_vuelo2 DECIMAL(2,2);
	DECLARE asientos_disponibles_vuelo1 INT;
	DECLARE asientos_disponibles_vuelo2 INT;
	DECLARE fecha_vencimiento DATE;
	DECLARE estado_reserva VARCHAR(45);
	DECLARE nro_reserva INT;

	START TRANSACTION;
	
		IF  EXISTS (SELECT * FROM empleados WHERE legajo = legajoE) 
		 AND EXISTS (SELECT * FROM pasajeros WHERE doc_tipo=tipodoc && doc_nro=numdoc) 
		 AND EXISTS (SELECT * FROM brinda as b JOIN instancias_vuelo as iv ON b.vuelo=iv.vuelo AND b.dia=iv.dia WHERE clase=clase1 && b.vuelo=vuelo1 && fecha=fecha1)
		 AND EXISTS (SELECT * FROM brinda as b JOIN instancias_vuelo as iv ON b.vuelo=iv.vuelo AND b.dia=iv.dia WHERE clase=clase2 && b.vuelo=vuelo2 && fecha=fecha2)
		
		THEN
		 	 SELECT cantidad INTO cant_reservas_vuelo1 FROM asientos_reservados WHERE vuelo=vuelo1 && clase=clase1 && fecha=fecha1 FOR UPDATE;
		 	 SELECT cantidad INTO cant_reservas_vuelo2 FROM asientos_reservados WHERE vuelo=vuelo2 && clase=clase2 && fecha=fecha2 FOR UPDATE;
			 SELECT cant_asientos INTO cantidad_asientos_vuelo1 FROM brinda AS b JOIN  instancias_vuelo AS iv ON b.vuelo=iv.vuelo AND iv.dia=b.dia WHERE fecha=fecha1 && b.vuelo=vuelo1 && clase=clase1;
			 SELECT cant_asientos INTO cantidad_asientos_vuelo2 FROM brinda AS b JOIN  instancias_vuelo AS iv ON b.vuelo=iv.vuelo AND iv.dia=b.dia WHERE fecha=fecha2 && b.vuelo=vuelo2 && clase=clase2;
			 SELECT porcentaje INTO porcent_vuelo1 FROM clases WHERE nombre=clase1;	
			 SELECT porcentaje INTO porcent_vuelo2 FROM clases WHERE nombre=clase2;	
			 SET asientos_disponibles_vuelo1 = (TRUNCATE(cantidad_asientos_vuelo1 + (cantidad_asientos_vuelo1 * porcent_vuelo1),0) - cant_reservas_vuelo1);
			 SET asientos_disponibles_vuelo2 = (TRUNCATE(cantidad_asientos_vuelo2 + (cantidad_asientos_vuelo2 * porcent_vuelo2),0) - cant_reservas_vuelo2);
			 SET fecha_vencimiento = fecha1 - INTERVAL 15 DAY;
			
			 IF asientos_disponibles_vuelo1 > 0 
			 THEN IF asientos_disponibles_vuelo2 > 0
				 THEN
					 IF((fecha_vencimiento-CURDATE())>=0)
					 THEN
						 IF (cant_reservas_vuelo1 >= cantidad_asientos_vuelo1) || (cant_reservas_vuelo2 >= cantidad_asientos_vuelo2)
						 THEN SET estado_reserva = 'En espera';
						 ELSE SET estado_reserva = 'Confirmada';
						 END IF;
					 
						 #Actualizo tabla reservas
						 INSERT INTO reservas(fecha, vencimiento, estado, doc_tipo, doc_nro, legajo) VALUES(CURDATE(),fecha_vencimiento,estado_reserva,tipodoc,numdoc,legajoE);
						 SET nro_reserva = LAST_INSERT_ID();
			
						 #Actualizo tabla reserva_vuelo_clase
						 INSERT INTO reserva_vuelo_clase(numero,vuelo,fecha_vuelo,clase) VALUES(nro_reserva,vuelo1,fecha1,clase1);
						 INSERT INTO reserva_vuelo_clase(numero,vuelo,fecha_vuelo,clase) VALUES(nro_reserva,vuelo2,fecha2,clase2);
			
						 #Actualizo la cantidad de asientos reservados para el vuelo numvuelo y la clase claseV en la fecha fechaR.
						 UPDATE asientos_reservados SET cantidad = cantidad +1 WHERE vuelo=vuelo1 && clase=clase1 && fecha=fecha1;
						 UPDATE asientos_reservados SET cantidad = cantidad +1 WHERE vuelo=vuelo2 && clase=clase2 && fecha=fecha2;
			
						 SELECT 'La reserva se realizo con exito' AS resultado;
					 ELSE
						 SELECT 'No se puede reservar. Faltan menos de 15 dias para la fecha del Vuelo de Ida' AS resultado;
					 END IF;
				 ELSE
					 SELECT 'No se puede realizar la reserva porque no hay lugares disponibles en el vuelo2' AS resultado;
				END IF;
			
			 ELSE
				 SELECT 'No se puede realizar la reserva porque no hay lugares disponibles en el vuelo 1' AS resultado;
			 END IF;
	    ELSE
			 SELECT 'No se puede realizar la reserva ya que el emplado, el pasajero o alguno de los vuelos no existe' AS resultado;			
		END IF;

	COMMIT;
END; !
delimiter ;


GRANT EXECUTE ON procedure vuelos.reservar_ida TO 'empleado'@'%';
GRANT EXECUTE ON procedure vuelos.reservar_ida_y_vuelta TO 'empleado'@'%';





