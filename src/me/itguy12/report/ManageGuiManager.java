package me.itguy12.report;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ManageGuiManager implements Listener {

	private static Inventory mainGui;
	
	 public static void onEnable() {
		 mainGui = Bukkit.getServer().createInventory(null, 9, "Report Manager");
		 ItemStack seeReports = new ItemStack(Material.EMERALD_BLOCK);
		 ItemMeta see = seeReports.getItemMeta();
		 see.setDisplayName(ChatColor.GREEN + "See Reports");
		 seeReports.setItemMeta(see);
		 mainGui.setItem(3, seeReports);
		 
		 ItemStack clearReports = new ItemStack(Material.REDSTONE_BLOCK);
		 ItemMeta clea = seeReports.getItemMeta();
		 clea.setDisplayName(ChatColor.RED + "Clear Reports");
		 clearReports.setItemMeta(clea);
		 mainGui.setItem(5, clearReports);
	 }
	 
	 public static void show(Player p) {
		 p.openInventory(mainGui);
	 }
	 
	 @EventHandler
	 public void onPlayerClick(InventoryClickEvent e) {
		 if(!(e.getInventory().equals(mainGui))) {
			 return;
		 }
		 if(e.getCurrentItem().getType() == Material.EMERALD_BLOCK){
			 Player p = (Player) e.getWhoClicked();
			 e.setCancelled(true);
			 p.chat("/listreports");
			 p.closeInventory();
		 }else{
			 Player p = (Player) e.getWhoClicked();
			 e.setCancelled(true);
			 p.chat("/clearreports");
			 p.closeInventory();
		 }
	 }
	 
	 
	 
	 
	 
}
