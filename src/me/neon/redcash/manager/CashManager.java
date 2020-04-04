package me.neon.redcash.manager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.neon.redcash.Cash;
import me.neon.redcash.database.Mysql;

public class CashManager {
	
	private String tableName = Cash.getInstance().config.getString("Database.Table");
	private static CashManager instance = new CashManager();
	public static CashManager getInstance() { return instance; }
	
	public boolean containsAccount(String player) {
		PreparedStatement st = null;
		try {
			st = Mysql.getInstance().getConnection().prepareStatement("SELECT * FROM `" + tableName + "` WHERE `user` = ?");
			st.setString(1, player.toLowerCase());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public int getAccounts(ArrayList<String> top) {
		PreparedStatement st = null;
		try {
			st = Mysql.getInstance().getConnection().prepareStatement("SELECT * FROM `" + tableName + "` ORDER BY `amount` DESC");
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				top.add(rs.getString("user"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return top.size();
	}
	
	public int getAccounts() {
		return Tasks.top.size();
	}
	
	public void createAccount(String player) {
		PreparedStatement st = null;
		try {
			st = Mysql.getInstance().getConnection().prepareStatement("INSERT INTO `" + tableName + "`(`user`, `amount`) VALUES (?,?)");
			st.setString(1, player.toLowerCase());
			st.setDouble(2, 0);
			st.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void setAmount(String player, int amount) {
		if (containsAccount(player)) {
			PreparedStatement st = null;
			try {
				st = Mysql.getInstance().getConnection().prepareStatement("UPDATE `" + tableName + "` SET `amount` = ? WHERE `user` = ?");
				st.setInt(1, amount);
				st.setString(2, player.toLowerCase());
				st.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public int getAmount(String player) {
		if (containsAccount(player)) {
			PreparedStatement st = null;
			try {
				st = Mysql.getInstance().getConnection().prepareStatement("SELECT * FROM `" + tableName + "` WHERE `user` = ?");
				st.setString(1, player.toLowerCase());
				ResultSet rs = st.executeQuery();
				while (rs.next()) {
					return rs.getInt("amount");
				}
				return 0;
			} catch (SQLException e) {
				return 0;
			}
		} else {
			createAccount(player);
			return 0;
		}
	}
	
	public void addAmount(String player, int amount) {
		if (containsAccount(player)) {
			setAmount(player, getAmount(player) + amount);
		} else {
			createAccount(player);
		}
	}
	
	public void removeAmount(String player, int amount) {
		if (containsAccount(player)) {
			setAmount(player, getAmount(player) - amount);
		} else {
			createAccount(player);
		}
	}
	
	public void deleteAccount(String player) {
		if (containsAccount(player)) {
			PreparedStatement st = null;
			try {
				st = Mysql.getInstance().getConnection().prepareStatement("DELETE FROM `" + tableName + "` WHERE `user` = ?");
				st.setString(1, player.toLowerCase());
				st.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public List<String> updateTop(ArrayList<String> topList) {
		PreparedStatement st = null;
		try {
			st = Mysql.getInstance().getConnection().prepareStatement("SELECT * FROM `" + tableName + "` ORDER BY `amount` DESC");
			ResultSet rs = st.executeQuery();
			int i = 0;
			while (rs.next()) {
				if (i <= 10) {
					i++;
					topList.add("§a" + i + "º §f" + rs.getString("user") + ":§a " + rs.getInt("amount"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return topList;
	}
	
	public List<String> getTop() {
		return Tasks.top;
	}
}
