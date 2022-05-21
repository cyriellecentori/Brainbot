package tk.cyriellecentori.brainbot.commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import tk.cyriellecentori.brainbot.Brainbot;
import tk.cyriellecentori.brainbot.profiles.Profile;
import tk.cyriellecentori.brainbot.BotExceptions.MoneyException;
import tk.cyriellecentori.brainbot.BotExceptions.NumberException;

public class DrinkCommand extends MessageCommand {
	
	String boisson;
	String emoji1;
	String emoji2;
	int cost;
	boolean masc;
	int boissID;

	public DrinkCommand(String name, String boisson, String emoji1, String emoji2, int cost, boolean masc, int boissID, JDA jda) {
		super(name, "À boire !", "", false, false, jda, new OptionData(OptionType.USER, "utilisateur", "Utilisateur à qui offrir une boisson.").setRequired(false));
		this.boisson = boisson;
		this.emoji1 = emoji1;
		this.emoji2 = emoji2;
		this.cost = cost;
		this.masc = masc;
		this.boissID = boissID;
	}
	
	public void command(User author, boolean offer, User other) {
		try {
			Brainbot.profiles.pay(author.getIdLong(), cost);
			if(offer) {
				if(other.getIdLong() == author.getIdLong()) {
					response = "Tu peux juste me dire que tu veux " + (masc ? "un" : "une") + " " + boisson + " avec " + Brainbot.prefix + name + ". "
							+ "Pas besoin de spécifier que c'est toi. Tiens, voilà " + (masc ? "ton" : "ta") + " " + boisson + ".";
					Brainbot.profiles.get(author.getIdLong()).drinksBus[boissID]++;
				} else if(other.getIdLong() == Brainbot.jda.getSelfUser().getIdLong()) {
					response = "C'est pour moi ? Oh ! Merci !";
					Brainbot.profiles.get(author.getIdLong()).drinksOfferts[boissID]++;
					Brainbot.profiles.get(other.getIdLong()).drinksBus[0]++;
				} else {
					response = (masc ? "Un" : "Une") + " " + boisson + " pour " + other.getAsMention()
							+ " de la part de " + author.getAsMention() + " ! " + emoji2;
					Brainbot.profiles.get(author.getIdLong()).drinksOfferts[boissID]++;
					Brainbot.profiles.get(other.getIdLong()).drinksBus[0]++;
				}
			} else {
				response = (masc ? "Un" : "Une") + " " + boisson +  " pour " + author.getAsMention() + " ! " + emoji1;
				Brainbot.profiles.get(author.getIdLong()).drinksBus[boissID]++;
			} 
		} catch(MoneyException e) {
			response = "T'as pas l'fric ! C'est que " + cost + " balles pourtant. " + (name == "yop" ? "Pas riche" : "Pauvre") + ", va.";
		} catch (NumberException e) {
			// TODO Auto-generated catch block
			// Ça n’arrive jamais ça
			e.printStackTrace();
			response = "Attends heu… wtf j’ai merdé là.";
		}
	}

	@Override
	public void execute(Brainbot bb, MessageReceivedEvent message) {
		if(message.isFromGuild()) {
			try {
				command(message.getAuthor(), true, message.getMessage().getMentions().getUsers().get(0));
			} catch(IndexOutOfBoundsException e) {
				command(message.getAuthor(), false, null);
			}
			super.execute(bb, message);
		}
		
	}

	@Override
	public void slashCommand(Brainbot bb, SlashCommandInteractionEvent event) {
		if(event.isFromGuild()) {
			OptionMapping user = event.getOption("utilisateur");
			if(user == null) {
				command(event.getUser(), false, null);
			} else {
				command(event.getUser(), true, user.getAsUser());
			}
		} else {
			response = "Cette commande n’est disponible que dans un serveur.";
		}
		super.slashCommand(bb, event);
		
	}

}
