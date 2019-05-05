 DELIMITER !
 
 CREATE FUNCTION diaIngles(fecha CHAR(2)) RETURNS INT
 BEGIN
    CASE fecha
		WHEN 'Do' THEN RETURN 1;
		WHEN 'Lu' THEN RETURN 2;
		WHEN 'Ma' THEN RETURN 3;
		WHEN 'Mi' THEN RETURN 4;
		WHEN 'Ju' THEN RETURN 5;
		WHEN 'Vi' THEN RETURN 6;
		WHEN 'Sa' THEN RETURN 7;
	END CASE; 	
 END; !
 
 DELIMITER ;
 
delimiter !
CREATE TRIGGER cargar_instanciasDeVuelo
AFTER INSERT ON salidas
FOR EACH ROW
BEGIN
	DECLARE fecha_proxima DATE;
	DECLARE fecha_limite DATE;
	DECLARE dia_ CHAR(2);
	DECLARE indexDia INT;
	DECLARE indexCURDATE INT;
	DECLARE fin BOOLEAN DEFAULT false;
	DECLARE C cursor for SELECT clase FROM brinda WHERE vuelo=NEW.vuelo && dia=NEW.dia;
	
	SET fecha_proxima = CURDATE(); 
	SET fecha_limite = DATE_ADD(CURDATE(), interval 1 year); 
	SET dia_ = NEW.dia; 
	SET indexDia = diaIngles(dia_); 
	SET indexCURDATE = DAYOFWEEK(CURDATE()); 
	
	WHILE((indexDia)<>(indexCURDATE)) DO
		SET fecha_proxima = DATE_ADD(fecha_proxima,interval 1 day);
		SET indexCURDATE = DAYOFWEEK(fecha_proxima);
	END WHILE;
	
	#Resto 1 semana del primer dia que cumple
	SET fecha_proxima = DATE_SUB(fecha_proxima,interval 7 day); 
	
	WHILE(fecha_proxima <> fecha_limite) DO
	
		INSERT INTO instancias_vuelo(vuelo, fecha, dia, estado)
		VALUES (NEW.vuelo, DATE_ADD(fecha_proxima,interval 7 day), dia_, 'a tiempo');	
		
		SET fecha_proxima = DATE_ADD(fecha_proxima,interval 7 day);
		
	END WHILE;
	
END; !
delimiter ;
 