package tk.cyriellecentori.brainbot.commands;

import java.util.Vector;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import tk.cyriellecentori.brainbot.Brainbot;

public class MessageCommand extends BotCommand {
	
	protected String response;
	
	public MessageCommand(String name, String help, String response) {
		super(name, help);
		this.response = response; 
	}
	
	public MessageCommand(String name, String help, String response, Vector<Long> a, Vector<Long> b) {
		super(name, help, a, b);
		this.response = response; 
	}
	
	public MessageCommand(String name, String help, String response, Vector<Long> a, boolean b) {
		super(name, help, a, b);
		this.response = response; 
	}

	@Override
	public void execute(Brainbot bb, MessageReceivedEvent message) {
		String[] spli = response.split("\n");
		String send = "";
		for(String str : spli) {
			if(send.length() + str.length() + 1 > 2000) {
				message.getChannel().sendMessage(send).queue();
				send = "";
			}
			send += str + "\n";
		}
		message.getChannel().sendMessage(send).queue();
	}

}
