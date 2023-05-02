package it.polito.tdp.meteo.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.meteo.model.Citta;
import it.polito.tdp.meteo.model.Rilevamento;

public class MeteoDAO {
	
	public List<Rilevamento> getAllRilevamenti() {

		final String sql = "SELECT Localita, Data, Umidita FROM situazione ORDER BY data ASC";

		List<Rilevamento> rilevamenti = new ArrayList<Rilevamento>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {

				Rilevamento r = new Rilevamento(rs.getString("Localita"), rs.getDate("Data").toLocalDate(), rs.getInt("Umidita"));
				rilevamenti.add(r);
			}

			conn.close();
			return rilevamenti;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public List<Rilevamento> getAllRilevamentiLocalitaMese(int mese, String localita) {
		
		String sql="SELECT DATA,Umidita "
				+ "FROM situazione "
				+ "WHERE localita=? AND month(data)=?";
		
		List<Rilevamento> listaRilevamenti=new ArrayList<Rilevamento>();
		try {
			Connection conn=ConnectDB.getConnection();
			PreparedStatement st=conn.prepareStatement(sql);
			st.setString(1, localita);
			st.setInt(2, mese);
			
			ResultSet rs=st.executeQuery();
			
			while(rs.next()) {
				LocalDate data=rs.getDate(1).toLocalDate();
				int umidita=rs.getInt(2);
				Rilevamento riv=new Rilevamento(localita,data,umidita);	
				listaRilevamenti.add(riv);
			}
			conn.close();
			return listaRilevamenti;
		}catch(SQLException e) {
			System.out.println("eccezione lettura database");
		}
		
		return null;
	}
	
	public Map<String,Double> getMEdiaUmidita(int mese) {
		String sql="SELECT localita, avg(Umidita) "
				+ "FROM situazione "
				+ "WHERE MONTH(DATA)=? "
				+ "GROUP BY localita ";
		
		Map<String,Double> mappaUmiditaPerCitta=new HashMap<String,Double>();
		try {
			Connection conn=ConnectDB.getConnection();
			PreparedStatement st=conn.prepareStatement(sql);
			st.setInt(1, mese);
			
			ResultSet rs=st.executeQuery();
			
			while(rs.next()) {
				String citta=rs.getString(1);
				double umidita=rs.getDouble(2);
				mappaUmiditaPerCitta.put(citta, umidita);	
			}
			conn.close();
			return mappaUmiditaPerCitta;
		}catch(SQLException e) {
			System.out.println("eccezione lettura database");
		}
		
		return null;
	}
	
	public List<Citta> getAllCities(int mese){
		final String sql = "SELECT localita "
				+ "FROM situazione "
				+ "GROUP BY localita";

		List<Citta> cities = new ArrayList<Citta>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				String city=rs.getString(1);
				List<Rilevamento> listaRilevamenti=this.getAllRilevamentiLocalitaMese(mese,city);
				Citta citta=new Citta(city,listaRilevamenti);

				cities.add(citta);
			}

			conn.close();
			return cities;

		} catch (SQLException e) {

			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
	}


}
