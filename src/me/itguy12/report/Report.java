package me.itguy12.report;

import org.bukkit.entity.Player;

public class Report {

	
	private Player reporter;
	private Player reported;
	private String reason;
	
	public Report(Player reporter, Player reported, String reason) {
		this.reason = reason;
		this.reported = reported;
		this.reporter = reporter;
	}
	
	public Player getReporter() {
		return reporter;
	}
	
	public String getReason() {
		return reason;
	}
	
	public Player getReported() {
		return reported;
	}
}
