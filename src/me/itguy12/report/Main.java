package me.itguy12.report;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	private ArrayList<Report> reports = new ArrayList<>();

	private int configVersion = 3;
	private boolean staffBypass;
	
	@Override
	public void onEnable() {
		if (!getDataFolder().exists())
			getDataFolder().mkdir();

		File file = new File(getDataFolder(), "config.yml");

		if (!file.exists()) {
			try (InputStream in = getResource("config.yml")) {
				Files.copy(in, file.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (!(getConfig().getInt("version") == configVersion)) {
			try (InputStream in = getResource("config.yml")) {
				Files.copy(in, file.toPath());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		staffBypass = getConfig().getBoolean("staff-bypass");

	}

	public boolean onCommand(CommandSender cs, Command cmd, String s, String[] args) {

		// /report <player> <reason> -> report.report
		if (cmd.getName().equalsIgnoreCase("report")) {
			if (!(cs instanceof Player)) {
				cs.sendMessage(ChatColor.RED + "You cannot submit a report.");
				return false;
			}

			Player p = (Player) cs;

			if (!p.hasPermission("report.report")) {
				p.sendMessage(ChatColor.RED + "You cannot submit a report.");
				return false;
			}

			if (args.length >= 2) {
				Player target = Bukkit.getPlayer(args[0]);
				if (target == null) {
					p.sendMessage(ChatColor.RED + "That is not an online player!");
					return true;
				}
				
				if(staffBypass && target.hasPermission("report.notify")) {
					p.sendMessage(ChatColor.RED + "That player bypassed that command.");
					return true;
				}
				
				List<String> a = Arrays.asList(args);

				String reason = "";

				for (String str : a) {
					if (!str.equals(p.getName()) && !str.equals(target.getName())) {
						reason = reason + str + " ";
					}
				}

				Report r = new Report(p, target, reason.trim());
				reports.add(r);

				// report.notify to see message
				String notification = getConfig().getString("messages.report-notification");
				notification = notification.replaceAll("%reporter%", r.getReporter().getName());
				notification = notification.replaceAll("%reported%", r.getReported().getName());
				notification = notification.replaceAll("%reason%", r.getReason());
				notification = ChatColor.translateAlternateColorCodes('&', notification);

				for (Player pl : Bukkit.getOnlinePlayers()) {
					if (pl.hasPermission("report.notify")) {
						pl.sendMessage(notification);
					}
				}

				String reportMessage = getConfig().getString("messages.report-message");
				reportMessage = reportMessage.replaceAll("%reporter%", r.getReporter().getName());
				reportMessage = reportMessage.replaceAll("%reported%", r.getReported().getName());
				reportMessage = reportMessage.replaceAll("%reason%", r.getReason());
				reportMessage = ChatColor.translateAlternateColorCodes('&', reportMessage);

				System.out.println(r.getReason());

				p.sendMessage(reportMessage);
				return true;
			} else {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&', 
						getConfig().getString("messages.report-syntax")));
				return false;
			}
			// /listreports -> report.reportlist
		} else if (cmd.getName().equalsIgnoreCase("listreports")) {
			if (!cs.hasPermission("report.reportlist")) {
				cs.sendMessage(ChatColor.RED + "No permission.");
				return true;
			}

			if (reports.isEmpty()) {
				cs.sendMessage(ChatColor.translateAlternateColorCodes('&',
						getConfig().getString("messages.no-reports")));
				return true;
			}

			for (Report r : reports) {
				String listMessage = getConfig().getString("messages.report-list");
				listMessage = listMessage.replaceAll("%reporter%", r.getReporter().getName());
				listMessage = listMessage.replaceAll("%reported%", r.getReported().getName());
				listMessage = listMessage.replaceAll("%reason%", r.getReason());
				listMessage = ChatColor.translateAlternateColorCodes('&', listMessage);

				cs.sendMessage(listMessage);
			}

			return true;

			// /clearreports -> report.reportsclear
		} else if (cmd.getName().equalsIgnoreCase("clearreports")) {
			if (!cs.hasPermission("report.reportsclear")) {
				cs.sendMessage(ChatColor.RED + "No permission.");
				return true;
			}
			reports.clear();
			cs.sendMessage(ChatColor.translateAlternateColorCodes('&',
					getConfig().getString("messages.report-clear-message")));
			return true;
		}
		return false;
	}

}
