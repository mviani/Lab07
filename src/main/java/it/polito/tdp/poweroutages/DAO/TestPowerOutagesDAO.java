package it.polito.tdp.poweroutages.DAO;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.MonthDay;
import java.time.Period;
import java.time.*;
import java.util.LinkedList;
import java.util.List;


import it.polito.tdp.poweroutages.model.Nerc;

public class TestPowerOutagesDAO {

	public static void main(String[] args) {
		
		try {
			Connection connection = ConnectDB.getConnection();
			connection.close();
			System.out.println("Connection Test PASSED");
			
			PowerOutageDAO dao = new PowerOutageDAO() ;
			
			//System.out.println(dao.getNercList()) ;
			List<Nerc> n = new LinkedList<Nerc>();
			n = dao.getNercList();
			System.out.println(n) ;
			LocalDateTime d1 = dao.getPowerOutagesByNerc(n.get(1)).get(3).getDateEventBegan();
			LocalDateTime d2 = dao.getPowerOutagesByNerc(n.get(1)).get(3).getDateEventFinished();
			System.out.println("data 1:   "+d1+"     data 2:    "+d2);
			
			double ns = Duration.between(d1, d2).toMinutes();
			double hours = ns/60;
			System.out.println(hours);
			
			
		} catch (Exception e) {
			System.err.println("Test FAILED");
		}

	}

}
