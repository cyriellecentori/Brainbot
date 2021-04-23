package tk.cyriellecentori.brainbot.commands;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import tk.cyriellecentori.brainbot.Brainbot;
import tk.cyriellecentori.brainbot.BotExceptions.MoneyException;
import tk.cyriellecentori.brainbot.BotExceptions.NumberException;

public class DrinkCommand extends BotCommand {
	
	String boisson;
	String emoji1;
	String emoji2;
	int cost;
	boolean masc;
	int boissID;

	public DrinkCommand(String name, String boisson, String emoji1, String emoji2, int cost, boolean masc, int boissID) {
		super(name, "");
		this.boisson = boisson;
		this.emoji1 = emoji1;
		this.emoji2 = emoji2;
		this.cost = cost;
		this.masc = masc;
		this.boissID = boissID;
	}

	@Override
	public void execute(Brainbot bb, MessageReceivedEvent message) {
		if(message.isFromGuild()) {
			MessageChannel channel = message.getChannel();
			User author = message.getAuthor();
			try {
				Brainbot.profiles.pay(author.getIdLong(), cost);
				if(message.getMessage().getMentionedMembers().get(0).getUser().getId().equals(message.getAuthor().getId())) {
					channel.sendMessage("Tu peux juste me dire que tu veux " + (masc ? "un" : "une") + " " + boisson + " avec " + Brainbot.prefix + name + ". "
							+ "Pas besoin de spécifier que c'est toi. Tiens, voilà " + (masc ? "ton" : "ta") + " " + boisson + ".").queue();
					Brainbot.profiles.get(author.getIdLong()).drinksBus[boissID]++;
				}else if(message.getMessage().getMentionedMembers().get(0).getUser().getId().equals(Brainbot.jda.getSelfUser().getId())) {
					channel.sendMessage("C'est pour moi ? Oh ! Merci !").queue();
					Brainbot.profiles.get(author.getIdLong()).drinksOfferts[boissID]++;
					Brainbot.profiles.get(message.getMessage().getMentionedMembers().get(0).getUser().getIdLong()).drinksBus[0]++;
				}else {
					channel.sendMessage((masc ? "Un" : "Une") + " " + boisson + " pour " + message.getMessage().getMentionedMembers().get(0).getAsMention()
							+ " de la part de " + author.getAsMention() + " ! " + emoji2).queue();
					Brainbot.profiles.get(author.getIdLong()).drinksOfferts[boissID]++;
					Brainbot.profiles.get(message.getMessage().getMentionedMembers().get(0).getUser().getIdLong()).drinksBus[0]++;
				}
			}catch(IndexOutOfBoundsException e) {
				channel.sendMessage((masc ? "Un" : "Une") + " " + boisson +  " pour " + author.getAsMention() + " ! " + emoji1).queue();
				Brainbot.profiles.get(author.getIdLong()).drinksBus[boissID]++;
			}catch(MoneyException e) {
				channel.sendMessage("T'as pas l'fric ! C'est " + cost + " "
						+ Brainbot.getCurrency(message.getGuild().getIdLong()) + " pourtant. " + (name == "yop" ? "Pas riche" : "Pauvre") + ", va.").queue();
			} catch (NumberException e) {
				//Vraiement, jamais ça arrive ça.
				e.printStackTrace();
			}
		}
	}

}
