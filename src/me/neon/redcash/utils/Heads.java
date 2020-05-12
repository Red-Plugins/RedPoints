package me.neon.redcash.utils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class Heads {
	
	public String[] getInformations(Cash cash, int position) {
		String[] informations = {"", "§c- §fCash: §7" + cash.getAmount(), "§c- §fPosição: §7" + position, ""};
		return informations;
	}
	
	public ItemStack getHead(Cash cash, int position, String url) {
		ItemStack itemStack = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
		if (url == null || url.isEmpty()) { 
			SkullMeta itemMeta = (SkullMeta) itemStack.getItemMeta();
			itemMeta.setDisplayName("§c- " + cash.getName());
			itemMeta.setLore(Arrays.asList(getInformations(cash, position)));
			itemMeta.setOwner(cash.getName());
			itemStack.setItemMeta(itemMeta);
			return itemStack;
		}
		
		SkullMeta itemMetaProfile = (SkullMeta) itemStack.getItemMeta();
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
		profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
		itemMetaProfile.setLore(Arrays.asList(getInformations(cash, position)));
		
        Field profileField;
        try {
            profileField = itemMetaProfile.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
            return itemStack;
        }
        
        profileField.setAccessible(true);
        try {
            profileField.set(itemMetaProfile, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
            return itemStack;
        }

        itemStack.setItemMeta(itemMetaProfile);
		return itemStack;
	}
}
