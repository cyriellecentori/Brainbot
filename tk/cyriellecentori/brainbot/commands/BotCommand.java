package tk.cyriellecentori.brainbot.commands;

import java.util.Vector;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import tk.cyriellecentori.brainbot.Brainbot;

public abstract class BotCommand {
	public final String name;
	public final String help;
	public final Vector<Long> serveursExclu;
	public final Vector<Long> usersAuthorized;
	
	public BotCommand(String name, String help, Vector<Long> serveursExclu, Vector<Long> usersAuthorized) {
		this.name = name;
		this.help = help;
		this.serveursExclu = serveursExclu;
		this.usersAuthorized = usersAuthorized;
	}
	
	public BotCommand(String name, String help) {
		this.name = name;
		this.help = help;
		this.serveursExclu = new Vector<Long>();
		this.usersAuthorized = new Vector<Long>();
	}
	
	public BotCommand(String name, String help, Vector<Long> servsOrUsers, boolean servers) {
		this.name = name;
		this.help = help;
		if(servers) {
			this.serveursExclu = servsOrUsers;
			this.usersAuthorized = new Vector<Long>();
		} else {
			this.serveursExclu = new Vector<Long>();
			this.usersAuthorized = servsOrUsers;
		}
		
	}
	
	public abstract void execute(Brainbot bb, MessageReceivedEvent message);
	
	public boolean isOkForThisServer(long servID) {
		return (serveursExclu.size() == 0) ? true : serveursExclu.contains(servID);
	}
	
	public boolean isOkForThisUser(long userID) {
		return (usersAuthorized.size() == 0) ? true : usersAuthorized.contains(userID);
	}
	
	public static class Alias extends BotCommand {

		BotCommand target;
		
		public Alias(String name, BotCommand target) {
			super(name, "", target.serveursExclu, target.usersAuthorized);
			this.target = target;
		}

		@Override
		public void execute(Brainbot bb, MessageReceivedEvent message) {
			target.execute(bb, message);
		}
		
		
		
	}
}
