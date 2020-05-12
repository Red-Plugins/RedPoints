package me.neon.redcash.service;

import java.sql.PreparedStatement; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

import me.neon.redcash.RedCash;
import me.neon.redcash.controllers.ManagerController;
import me.neon.redcash.utils.Cash;
import me.neon.redcash.utils.Heads;

public class ManagerMysqlService implements IModule, ManagerService {
	
	private Heads heads;
	
	@Override
	public void getTopList() {
		PreparedStatement st = null;
		ManagerController mc = RedCash.getInstance().getModuleForClass(ManagerController.class);
		try {
			st = RedCash.getInstance().getServiceConnectionMysql().prepareStatement("SELECT * FROM `" + RedCash.getInstance().getServiceTableMysql() + "` ORDER BY `amount` DESC");
			ResultSet rs = st.executeQuery();
			int i = 0;
			heads = new Heads();
			mc.topList.clear();
			while (rs.next()) {
				i++;
				Cash cash = new Cash(rs.getString("user"), RedCash.getInstance().translateNameToUUID(rs.getString("user")), rs.getInt("amount"));
				ItemStack head = heads.getHead(cash, i, null);
				mc.topList.add(head);
			}
		} catch (SQLException e) { }
	}
	
	@Override
	public boolean hasAccount(UUID uuid) {
		PreparedStatement st = null;
		try {
			st = RedCash.getInstance().getServiceConnectionMysql().prepareStatement("SELECT * FROM `" + RedCash.getInstance().getServiceTableMysql() + "` WHERE `uuid` = ?");
			st.setString(1, uuid.toString());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			return false;
		}
	}
	
	@Override
	public void createAccount(Cash cash) {
		PreparedStatement st = null;
		try {
			st = RedCash.getInstance().getServiceConnectionMysql().prepareStatement("INSERT INTO `" + RedCash.getInstance().getServiceTableMysql() + "`(user, uuid, amount) VALUES (?, ?, ?)");
			st.setString(1, cash.getName().toLowerCase());
			st.setString(2, cash.getuuid().toString());
			st.setInt(3, cash.getAmount());
			st.executeUpdate();
		} catch (SQLException e) { }
	}
	
	@Override
	public void setAmount(Cash cash) {
		if (hasAccount(cash.getuuid())) {
			PreparedStatement st = null;
			try {
				st = RedCash.getInstance().getServiceConnectionMysql().prepareStatement("UPDATE `" + RedCash.getInstance().getServiceTableMysql() + "` SET `amount` = ? WHERE `uuid` = ?");
				st.setInt(1, cash.getAmount());
				st.setString(2, cash.getuuid().toString());
				st.executeUpdate();
			} catch (SQLException e) { }
		}
	}
	
	@Override
	public void removeAmount(Cash cash) {
		if (!hasAccount(cash.getuuid())) return;
		cash.setAmount(getAmount(cash.getuuid()) - cash.getAmount());
		setAmount(cash);
	}
	
	@Override
	public void addAmount(Cash cash) {
		if (!hasAccount(cash.getuuid())) return;
		cash.setAmount(getAmount(cash.getuuid()) + cash.getAmount());
		setAmount(cash);
	}
	
	@Override
	public int getAmount(UUID uuid) {
		if (hasAccount(uuid)) {
			PreparedStatement st = null;
			try {
				st = RedCash.getInstance().getServiceConnectionMysql().prepareStatement("SELECT * FROM `" + RedCash.getInstance().getServiceTableMysql() + "` WHERE `uuid` = ?");
				st.setString(1, uuid.toString());
				ResultSet rs = st.executeQuery();
				while (rs.next()) {
					return rs.getInt("amount");
				}
				return 0;
			} catch (SQLException e) {
				return 0;
			}
		} else {
			return 0;
		}
	}
	
	@Override
	public void deleteAccount(UUID uuid) {
		if (hasAccount(uuid)) {
			PreparedStatement st = null;
			try {
				st = RedCash.getInstance().getServiceConnectionMysql().prepareStatement("DELETE FROM `" + RedCash.getInstance().getServiceTableMysql() + "` WHERE `uuid` = ?");
				st.setString(1, uuid.toString());
				st.executeUpdate();
			} catch (SQLException e) { }
		}
	}

	@Override
	public void starting() {
	}

	@Override
	public void closing() {
	}
}
