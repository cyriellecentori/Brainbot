package tk.cyriellecentori.brainbot.shop;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Vector;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import tk.cyriellecentori.brainbot.Brainbot;
import tk.cyriellecentori.brainbot.commands.MessageCommand;
import tk.cyriellecentori.brainbot.profiles.Achievement;
import tk.cyriellecentori.brainbot.profiles.Profile;

public class Shop extends MessageCommand {
	
	public static abstract class Item {
		public final int cost;
		public final String name;
		public final String description;
		
		public Item(int cost, String name, String description) {
			this.cost = cost;
			this.name = name;
			this.description = description;
		}
		public abstract boolean execute(Brainbot bb, Profile buyer, TextChannel chan);
	}
	
	public static class RoleItem extends Item {
		public final long role;
		
		public RoleItem(int cost, long role, String name, String description) {
			super(cost, name, description);
			this.role = role;
		}

		@Override
		public boolean execute(Brainbot bb, Profile buyer, TextChannel chan) {
			boolean addRole = Brainbot.jda.getRoleById(role).getGuild().getMemberById(buyer.id).getRoles().contains(Brainbot.jda.getRoleById(role));
			if(!addRole) {
				Brainbot.jda.getRoleById(role).getGuild().addRoleToMember(Brainbot.jda.getUserById(buyer.id), Brainbot.jda.getRoleById(role)).queue();
			} else {
				return false;
			}
			return true;
			
		}
	}
	
	protected LinkedHashMap<String, Vector<Item>> items;
	
	public Shop(String name, String help, LinkedHashMap<String, Vector<Item>> items, Vector<Long> servs) {
		super(name, help, "", servs, true);
		this.items = items;
	}
	
	public Shop(String name, String help, LinkedHashMap<String, Vector<Item>> items, boolean ephemeral, JDA jda) {
		super(name, help, "", true, ephemeral, jda, 
				new OptionData(OptionType.STRING, "sous-commande", "Sous-commande").addChoice("Liste des choix", "list").addChoice("Achat", "buy").setRequired(true),
				new OptionData(OptionType.STRING, "article", "Article").setRequired(false));
		this.items = items;
	}
	
	public Shop(String name, String help, LinkedHashMap<String, Vector<Item>> items, boolean ephemeral, Vector<Guild> guilds) {
		super(name, help, "", true, ephemeral, guilds, 
				new OptionData(OptionType.STRING, "sous-commande", "Sous-commande").addChoice("Liste des choix", "list").addChoice("Achat", "buy").setRequired(true),
				new OptionData(OptionType.STRING, "article", "Article").setRequired(false));
		this.items = items;
	}
	
	public void setList(String monnaie) {
		String ret = "Voilà ce que j'ai en stock :\n";
		for(Entry<String, Vector<Item>> cat : items.entrySet()) {
			ret = ret.concat("**" + cat.getKey() + "** :\n");
			for(Item i : cat.getValue()) {
				ret = ret.concat("__" + i.name + "__ (" + i.cost + " " + monnaie + ") — " + i.description + "\n");
			}
			ret = ret.concat("\n");
		}
		response = ret;
	}
	
	public void achat(TextChannel chan, User author, String itemName, Brainbot bb) {
		if(itemName.equals("")) {
			response = "Acheter, oui, mais… acheter quoi ?";
			return;
		}
		Item searched = null;
		for(Entry<String, Vector<Item>> cat : items.entrySet()) {
			for(Item i : cat.getValue()) {
				if(i.name.equals(itemName)) {
					searched = i;
				}
			}
		}
		if(searched == null) {
			response = "J'ai pas ce que tu cherches, là.";
		} else {
			Profile user = Brainbot.profiles.get(author.getIdLong());
			if(searched.cost > user.money) {
				response = "T'as pas assez de flouze.";
			} else {
				//response = searched.name + " acheté !";
				if(!searched.execute(bb, user, chan)) {
					if(searched instanceof RoleItem)
						response = "Tu peux pas acheter ça plusieurs fois.";
					else if(searched instanceof Aliment)
						response = "Tu n'as plus de place dans ton frigo.";
					else
						response = "Impossible d'acheter l'item.";
				} else {
					response = searched.name + " acheté !";
					user.money -= searched.cost;
					user.shopSpent += searched.cost;
					if(user.shopSpent >= 5000) {
						user.giveAchievement(new Achievement("Les courses du mois", "Dépensez plus de 5 000 dans les boutiques.", false, 0), chan);
					}
					if(user.shopSpent >= 15000)
						user.giveAchievement(new Achievement("Courses de Noël", "Dépensez plus de 15 000 dans les boutiques.", false, 0), chan);
					if(user.shopSpent >= 2500)
						user.giveAchievement(new Achievement("Bourgeois", "Dépensez plus de 25 000 dans les boutiques.", false, 0), chan);
					if(user.shopSpent >= 50000)
						user.giveAchievement(new Achievement("Cible du fisc", "Dépensez plus de 50 000 dans les boutiques.", false, 0), chan);
					if(user.shopSpent >= 100000)
						user.giveAchievement(new Achievement("Nolife", "Dépensez plus de 100 000 dans les boutiques. Vous pouvez désormais retourner à la civilsation.", true, 20000), chan);
				}
			}
		}
	
	
	}
	
	@Override
	public void slashCommand(Brainbot bb, SlashCommandInteractionEvent event) {
		if(event.getOption("sous-commande").getAsString().equals("list")) {
			setList(Brainbot.getCurrency(event.getGuild().getIdLong()));
		} else if(event.getOption("sous-commande").getAsString().equals("buy")) {
			achat(event.getTextChannel(), event.getUser(), event.getOption("article").getAsString(), bb);
		}
		super.slashCommand(bb, event);
	}
	
	@Override
	public void execute(Brainbot bb, MessageReceivedEvent message) {
		String[] split = message.getMessage().getContentRaw().split(" ");
		try {
			if(split[1].equals("list")) {
				setList(Brainbot.getCurrency(message.getGuild().getIdLong()));
			} else if(split[1].equals("buy")) {
				achat(message.getTextChannel(), message.getAuthor(), message.getMessage().getContentRaw().split(" ", 3)[2], bb);
			} else {
				response = "Commandes utilisables :\n"
						+ name + " list : Affiche tous les items disponibles.\n"
						+ name + " buy nom de l'item : Achète l'item demandé, le nom doit être exactement le même que dans la liste.\n"
						+ name + " help : Affche cette page d'aide.";
			}
		} catch(IndexOutOfBoundsException e) {
			response = "Commandes utilisables :\n"
					+ name + " list : Affiche tous les items disponibles.\n"
					+ name + " buy nom de l'item : Achète l'item demandé, le nom doit être exactement le même que dans la liste.\n"
					+ name + " help : Affche cette page d'aide.";
		}
		super.execute(bb, message);
		
	}
	
}
