package tk.cyriellecentori.brainbot.commands;

import java.util.Vector;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import tk.cyriellecentori.brainbot.Brainbot;

public abstract class BotCommand {
	public final String name;
	public final String help;
	public final Vector<Long> serveursExclu;
	public final Vector<Long> usersAuthorized;
	public final boolean slashCommand;
	public final boolean delay;
	public static final boolean beta = false;
	
	public BotCommand(String name, String help, Vector<Long> serveursExclu, Vector<Long> usersAuthorized) {
		this.name = name;
		this.help = help;
		this.serveursExclu = serveursExclu;
		this.usersAuthorized = usersAuthorized;
		slashCommand = false;
		delay = false;
	}
	
	public BotCommand(String name, String help) {
		this.name = name;
		this.help = help;
		this.serveursExclu = new Vector<Long>();
		this.usersAuthorized = new Vector<Long>();
		slashCommand = false;
		delay = false;
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
		slashCommand = false;
		delay = false;
		
	}
	
	public BotCommand(String name, String help, boolean delay, JDA jda, OptionData... options) {
		this.name = name;
		this.help = help;
		this.serveursExclu = new Vector<Long>();
		this.usersAuthorized = new Vector<Long>();
		slashCommand = true;
		this.delay = delay;
		if(!beta)
			jda.upsertCommand(name, help).addOptions(options).queue();
		else
			jda.getGuildById(372635468786827265L).upsertCommand(name, help).addOptions(options).queue();
	}
	
	public BotCommand(String name, String help, boolean delay, Vector<Guild> guilds, OptionData... options) {
		this.name = name;
		this.help = help;
		this.serveursExclu = new Vector<Long>();
		this.usersAuthorized = new Vector<Long>();
		slashCommand = true;
		this.delay = delay;
		for(Guild guild : guilds) {
			try {
				guild.upsertCommand(name, help).addOptions(options).queue();
				this.serveursExclu.add(guild.getIdLong());
			} catch(NullPointerException e) {}
			
		}
	}
	
	public abstract void execute(Brainbot bb, MessageReceivedEvent message);
	
	public void slashCommand(Brainbot bb, SlashCommandInteractionEvent event) {}
	
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

		@Override
		public void slashCommand(Brainbot bb, SlashCommandInteractionEvent event) {
			target.slashCommand(bb, event);
		}
		
		
		
	}
}
