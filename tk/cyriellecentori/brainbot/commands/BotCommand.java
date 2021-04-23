package tk.cyriellecentori.brainbot.commands;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import tk.cyriellecentori.brainbot.Brainbot;

public abstract class BotCommand {
	public final String name;
	public final String help;
	
	public BotCommand(String name, String help) {
		this.name = name;
		this.help = help;
	}
	
	public abstract void execute(Brainbot bb, MessageReceivedEvent message);
}
