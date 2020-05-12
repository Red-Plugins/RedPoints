package me.neon.redcash.inventory;

import java.util.ArrayList; 
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ScrollerInventory {
	
	public static HashMap<Player, ScrollerInventory> users = new HashMap<Player, ScrollerInventory>();
	public ArrayList<Inventory> pages = new ArrayList<Inventory>();
	public static final String previousPageName = "§cPágina anterior";
	public static final String nextPageName = "§cPróxima página";
	public int currpage = 0;
	private Player player;
	
	public ScrollerInventory(ArrayList<ItemStack> itens, String name, Player player) {
		this.player = player;
		Inventory page = getBlank(name, player, itens);
		for (int i = 0; i < itens.size(); i++) {
			if (page.firstEmpty() == 46) {
				pages.add(page);
				page = getBlank(name, player, itens);
				page.addItem(itens.get(i));
			} else {
				page.addItem(itens.get(i));
			}
		}
		pages.add(page);
		player.openInventory(pages.get(currpage));
		users.put(player, this);
	}
	
	private Inventory getBlank(String name, Player player, ArrayList<ItemStack> itens) {
		Inventory page = Bukkit.createInventory(null, 54, name);
		
		ItemStack nextPage = new ItemStack(Material.ARROW);
		ItemMeta nextMeta = nextPage.getItemMeta();
		nextMeta.setDisplayName(nextPageName);
		nextPage.setItemMeta(nextMeta);
		
		ItemStack prevPage = new ItemStack(Material.ARROW);
		ItemMeta prevMeta = prevPage.getItemMeta();
		prevMeta.setDisplayName(previousPageName);
		prevPage.setItemMeta(prevMeta);
		
		ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 14);
		
		for (ItemStack item : itens) {
			if (item.getItemMeta().getDisplayName().contains(player.getName().toLowerCase())) {
				ItemStack newItem = new ItemStack(item);
				ItemMeta meta = newItem.getItemMeta();
				meta.setDisplayName("§cSuas informações:");
				newItem.setItemMeta(meta);
				page.setItem(49, newItem);
			}
		}
		
		page.setItem(46, glass);
		page.setItem(47, glass);
		page.setItem(48, glass);
		page.setItem(50, glass);
		page.setItem(51, glass);
		page.setItem(52, glass);
		page.setItem(53, nextPage);
		page.setItem(45, prevPage);
		return page;
	}

	public Player getPlayer() {
		return player;
	}
}	

