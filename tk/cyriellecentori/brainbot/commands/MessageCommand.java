package tk.cyriellecentori.brainbot.commands;

import java.util.Vector;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import tk.cyriellecentori.brainbot.Brainbot;

public class MessageCommand extends BotCommand {
	
	protected String response;
	public final boolean ephemeral;
	
	public MessageCommand(String name, String help, String response) {
		super(name, help);
		this.response = response; 
		ephemeral = false;
	}
	
	public MessageCommand(String name, String help, String response, boolean delay, boolean ephemeral, JDA jda, OptionData... options) {
		super(name, help, delay, jda, options);
		this.response = response; 
		this.ephemeral = ephemeral;
	}
	
	public MessageCommand(String name, String help, String response, boolean delay,  boolean ephemeral, Vector<Guild> guilds, OptionData... options) {
		super(name, help, delay, guilds, options);
		this.response = response;
		this.ephemeral = ephemeral;
	}
	
	public MessageCommand(String name, String help, String response, Vector<Long> a, Vector<Long> b) {
		super(name, help, a, b);
		this.response = response;
		ephemeral = false;
	}
	
	public MessageCommand(String name, String help, String response, Vector<Long> a, boolean b) {
		super(name, help, a, b);
		this.response = response;
		ephemeral = false;
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

	@Override
	public void slashCommand(Brainbot bb, SlashCommandInteractionEvent event) {
		if(response.length() < 2000) {
			if(delay)
				event.getHook().sendMessage(response).queue();
			else
				event.reply(response).setEphemeral(ephemeral).queue();
		} else {
			if(!delay) {
				event.deferReply().queue();
			}
			String[] spli = response.split("\n");
			Vector<String> message = new Vector<String>();
			String send = "";
			for(String str : spli) {
				if(send.length() + str.length() + 1 > 2000) {
					message.add(send);
					send = "";
				}
				send += str + "\n";
			}
			message.add(send);
			if(message.size() == 1) {
				event.getHook().sendMessage(response).setEphemeral(ephemeral).queue();
				System.err.println("Attention MessageCommand.");
			} else {
				String id = new String("mm").concat(Long.toString(System.currentTimeMillis()));
				bb.multimessages.put(id, message);
				bb.mmposition.put(id, 0);
				event.getHook().sendMessage(message.get(0)).addActionRow(
						Button.secondary(id.concat("-p"), "Précédent").asDisabled(), 
						Button.secondary(id.concat("-n"), "Suivant")).setEphemeral(ephemeral).queue();
			}
		}
	}

}
