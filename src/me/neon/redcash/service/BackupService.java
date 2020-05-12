package me.neon.redcash.service;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import me.neon.redcash.RedCash;
import me.neon.redcash.configuration.FileConfiguration;
import me.neon.redcash.utils.Cash;

public class BackupService implements IModule {

	public List<Cash> getAccountsForBackup() {
		List<Cash> accounts = new ArrayList<>();
		PreparedStatement st = null;
		try {
			st = RedCash.getInstance().getServiceConnectionMysql().prepareStatement("SELECT * FROM `" + RedCash.getInstance().getServiceTableMysql() + "`");
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				Cash cash = new Cash(rs.getString("user"), RedCash.getInstance().translateNameToUUID(rs.getString("user")), rs.getInt("amount"));
				accounts.add(cash);
			}
		} catch (SQLException e) { }
		return accounts;
	}
	
	public void saveBackupFile(List<Cash> accounts) {
		Instant instant = Instant.now();
		ZoneId z = ZoneId.of("America/Sao_Paulo");
		ZonedDateTime zdt = instant.atZone(z);
		String time = String.valueOf(zdt).replace("[America/Sao_Paulo]", "");
		File locale = new File(RedCash.getInstance().getDataFolder() + "/backup");
		String fileName = "backup-" + time + ".yml";
		FileConfiguration personalizatedConfig = new FileConfiguration(locale, fileName);
		if (!locale.exists()) {
			locale.mkdir();
		}
		for (Cash account : accounts) {
			personalizatedConfig.getConfig().set("BackupAccount." + account.getuuid() + ".Name", account.getName());
			personalizatedConfig.getConfig().set("BackupAccount." + account.getuuid() + ".Amount", account.getAmount());
			personalizatedConfig.saveConfiguration();
		}
	}
	
	public void forcedBackup() {
		
	}
	
	@Override
	public void starting() { }

	@Override
	public void closing() { }
}
