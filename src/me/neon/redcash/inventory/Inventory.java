package me.neon.redcash.inventory;

import org.bukkit.entity.Player; 
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import me.neon.redcash.event.RedCashChangeEvent;

public class Inventory implements Listener {
	
	@EventHandler(ignoreCancelled = true)
	public void onClick(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		if (!(ScrollerInventory.users.containsKey(player))) return;
		ScrollerInventory inventory = ScrollerInventory.users.get(player);
		if (event.getCurrentItem() == null) return;
		if (event.getCurrentItem().getItemMeta() == null) return;
		if (event.getCurrentItem().getItemMeta().getDisplayName() == null) return;
		event.setCancelled(true);
		if (event.getCurrentItem().getItemMeta().getDisplayName() == ScrollerInventory.nextPageName) {
			event.setCancelled(true);
			if (inventory.currpage >= inventory.pages.size() - 1) return;
			else {
				inventory.currpage += 1;
				player.openInventory(inventory.pages.get(inventory.currpage));
			}
		} else if (event.getCurrentItem().getItemMeta().getDisplayName() == ScrollerInventory.previousPageName) {
			event.setCancelled(true);
			if (inventory.currpage > 0) {
				inventory.currpage -= 1;
				player.openInventory(inventory.pages.get(inventory.currpage));
			}
		}
	}
	
	@EventHandler
	public void test(RedCashChangeEvent event) {
		event.getPlayer().sendMessage("eae, um dos seus eventos funcionaram, parabens e nem deu erro :)");
	}
}
