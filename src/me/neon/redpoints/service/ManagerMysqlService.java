package me.neon.redpoints.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import me.neon.redpoints.RedPoints;
import me.neon.redpoints.controllers.ManagerController;
import me.neon.redpoints.utils.Cash;
import me.neon.redpoints.utils.Heads;
import me.neon.redpoints.utils.IModule;
import me.neon.redpoints.utils.Methods;

public class ManagerMysqlService implements IModule {
	
	private Heads heads;
	
	public void getLeaderBoardList() {
		PreparedStatement st = null;
		ManagerController mc = RedPoints.getInstance().getModuleForClass(ManagerController.class);
		try {
			st = RedPoints.getInstance().getServiceConnectionMysql().prepareStatement("SELECT * FROM `" + RedPoints.getInstance().getServiceTableMysql() + "` ORDER BY `amount` DESC");
			ResultSet rs = st.executeQuery();
			int i = 0;
			heads = new Heads();
			mc.leaderBoardList.clear();
			while (rs.next()) {
				i++;
				Cash cash = new Cash(rs.getString("user"), RedPoints.getInstance().translateNameToUUID(rs.getString("user")), rs.getDouble("amount"));
				ItemStack head = heads.getHead(cash, i, null);
				mc.leaderBoardList.add(head);
			}
			Methods.cleanUp(rs, st);
		} catch (SQLException e) { }
	}
	
	public boolean hasAccount(UUID uuid) {
		PreparedStatement st = null;
		try {
			st = RedPoints.getInstance().getServiceConnectionMysql().prepareStatement("SELECT * FROM `" + RedPoints.getInstance().getServiceTableMysql() + "` WHERE `uuid` = ?");
			st.setString(1, uuid.toString());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Methods.cleanUp(rs, st);
				return true;
			}
			Methods.cleanUp(rs, st);
			return false;
		} catch (SQLException e) {
			return false;
		}
	}
	
	public void createAccount(Cash cash) {
		PreparedStatement st = null;
		try {
			st = RedPoints.getInstance().getServiceConnectionMysql().prepareStatement("INSERT INTO `" + RedPoints.getInstance().getServiceTableMysql() + "`(user, uuid, amount) VALUES (?, ?, ?)");
			st.setString(1, cash.getName().toLowerCase());
			st.setString(2, cash.getuuid().toString());
			st.setDouble(3, cash.getAmount());
			st.executeUpdate();
			Methods.cleanUp(st);
		} catch (SQLException e) { }
	}
	
	public void setAmount(Cash cash) {
		if (hasAccount(cash.getuuid())) {
			PreparedStatement st = null;
			try {
				st = RedPoints.getInstance().getServiceConnectionMysql().prepareStatement("UPDATE `" + RedPoints.getInstance().getServiceTableMysql() + "` SET `amount` = ? WHERE `uuid` = ?");
				st.setDouble(1, cash.getAmount());
				st.setString(2, cash.getuuid().toString());
				st.executeUpdate();
				Methods.cleanUp(st);
			} catch (SQLException e) { }
		}
	}
	
	public void removeAmount(Cash cash) {
		if (!hasAccount(cash.getuuid())) return;
		cash.setAmount(getAmount(cash.getuuid()) - cash.getAmount());
		setAmount(cash);
	}
	
	public void addAmount(Cash cash) {
		if (!hasAccount(cash.getuuid())) return;
		cash.setAmount(getAmount(cash.getuuid()) + cash.getAmount());
		setAmount(cash);
	}
	
	public double getAmount(UUID uuid) {
		if (hasAccount(uuid)) {
			PreparedStatement st = null;
			try {
				st = RedPoints.getInstance().getServiceConnectionMysql().prepareStatement("SELECT * FROM `" + RedPoints.getInstance().getServiceTableMysql() + "` WHERE `uuid` = ?");
				st.setString(1, uuid.toString());
				ResultSet rs = st.executeQuery();
				while (rs.next()) {
					return rs.getDouble("amount");
				}
				return 0.0;
			} catch (SQLException e) {
				return 0.0;
			}
		} else {
			return 0.0;
		}
	}
	
	public void deleteAccount(UUID uuid) {
		if (hasAccount(uuid)) {
			PreparedStatement st = null;
			try {
				st = RedPoints.getInstance().getServiceConnectionMysql().prepareStatement("DELETE FROM `" + RedPoints.getInstance().getServiceTableMysql() + "` WHERE `uuid` = ?");
				st.setString(1, uuid.toString());
				st.executeUpdate();
				Methods.cleanUp(st);
			} catch (SQLException e) { }
		}
	}

	@Override
	public void starting() { }

	@Override
	public void closing() { }
}
