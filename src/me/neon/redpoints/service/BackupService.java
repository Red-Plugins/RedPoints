package me.neon.redpoints.service;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import me.neon.redpoints.RedPoints;
import me.neon.redpoints.configuration.FileConfiguration;
import me.neon.redpoints.controllers.ConfigurationController;
import me.neon.redpoints.utils.Cash;
import me.neon.redpoints.utils.IModule;

public class BackupService implements IModule {

	public List<Cash> getAccountsForBackupMysql() {
		List<Cash> accounts = new ArrayList<>();
		PreparedStatement st = null;
		try {
			st = RedPoints.getInstance().getServiceConnectionMysql().prepareStatement("SELECT * FROM `" + RedPoints.getInstance().getServiceTableMysql() + "`");
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Cash cash = new Cash(rs.getString("user"), RedPoints.getInstance().translateNameToUUID(rs.getString("user")), rs.getDouble("amount"));
				accounts.add(cash);
			}
		} catch (SQLException e) { }
		return accounts;
	}
	
	public List<Cash> getAccountsForBackupYaml() {
		ConfigurationController cc = RedPoints.getInstance().getModuleForClass(ConfigurationController.class);
		List<Cash> accounts = new ArrayList<>();
		for (String account : cc.getStorageConfig().getConfig().getConfigurationSection("Accounts").getKeys(false)) {
			String name = cc.getStorageConfig().getString("Accounts." + account + ".Name");
			double amount = cc.getStorageConfig().getConfig().getDouble("Accounts." + account + ".Amount");
			Cash cash = new Cash(name, RedPoints.getInstance().translateNameToUUID(name), amount);
			accounts.add(cash);
		}
		return accounts;
	}
	
	public void saveBackupFile(List<Cash> accounts) {
		Instant instant = Instant.now();
		ZoneId z = ZoneId.of("America/Sao_Paulo");
		ZonedDateTime zdt = instant.atZone(z);
		String time = String.valueOf(zdt).replace("[America/Sao_Paulo]", "");
		File locale = new File(RedPoints.getInstance().getDataFolder() + "/backup");
		String fileName = "backup-" + time + ".yml";
		if (!locale.exists()) {
			locale.mkdir();
		}
		FileConfiguration personalizatedConfig = new FileConfiguration(locale, fileName);
		for (Cash account : accounts) {
			personalizatedConfig.getConfig().set("BackupAccount." + account.getuuid() + ".Name", account.getName());
			personalizatedConfig.getConfig().set("BackupAccount." + account.getuuid() + ".Amount", account.getAmount());
			personalizatedConfig.saveConfiguration();
		}
	}
	
	@Override
	public void starting() { }

	@Override
	public void closing() { }
}
