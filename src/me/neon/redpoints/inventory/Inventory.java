package me.neon.redpoints.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

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
		if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ScrollerInventory.nextPageName)) {
			event.setCancelled(true);
			if (inventory.currpage >= inventory.pages.size() - 1) { 
				return;
			} else {
				inventory.currpage += 1;
				player.openInventory(inventory.pages.get(inventory.currpage));
			}
		} else if (event.getCurrentItem().getItemMeta().getDisplayName().equals(ScrollerInventory.previousPageName)) {
			event.setCancelled(true);
			if (inventory.currpage > 0) {
				inventory.currpage -= 1;
				player.openInventory(inventory.pages.get(inventory.currpage));
			}
		}
	}
}
