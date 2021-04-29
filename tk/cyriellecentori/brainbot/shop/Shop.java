package tk.cyriellecentori.brainbot.shop;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Vector;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.managers.GuildManager;
import tk.cyriellecentori.brainbot.Brainbot;
import tk.cyriellecentori.brainbot.commands.BotCommand;
import tk.cyriellecentori.brainbot.commands.MessageCommand;
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
		public abstract boolean execute(Brainbot bb, Profile buyer, MessageReceivedEvent command);
	}
	
	public static class RoleItem extends Item {
		public final long role;
		
		public RoleItem(int cost, long role, String name, String description) {
			super(cost, name, description);
			this.role = role;
		}

		@Override
		public boolean execute(Brainbot bb, Profile buyer, MessageReceivedEvent command) {
			boolean addRole = Brainbot.jda.getRoleById(role).getGuild().getMemberById(buyer.id).getRoles().contains(Brainbot.jda.getRoleById(role));
			if(!addRole) {
				Brainbot.jda.getRoleById(role).getGuild().addRoleToMember(buyer.id, Brainbot.jda.getRoleById(role)).queue();
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

	@Override
	public void execute(Brainbot bb, MessageReceivedEvent message) {
		String[] split = message.getMessage().getContentRaw().split(" ");
		try {
			if(split[1].equals("list")) {
				String ret = "Voilà ce que j'ai en stock :\n";
				for(Entry<String, Vector<Item>> cat : items.entrySet()) {
					ret = ret.concat("**" + cat.getKey() + "** :\n");
					for(Item i : cat.getValue()) {
						ret = ret.concat("__" + i.name + "__ (" + i.cost + " " + Brainbot.getCurrency(message.getGuild().getIdLong()) + ") — " + i.description + "\n");
					}
					ret = ret.concat("\n");
				}
				response = ret;
			} else if(split[1].equals("buy")) {
				String itemName = message.getMessage().getContentRaw().split(" ", 3)[2];
				System.out.println(itemName);
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
					Profile user = Brainbot.profiles.get(message.getAuthor().getIdLong());
					if(searched.cost > user.money) {
						response = "T'as pas assez de flouze.";
					} else {
						//response = searched.name + " acheté !";
						if(!searched.execute(bb, user, message)) {
							if(searched instanceof RoleItem)
								response = "Tu peux pas acheter ça plusieurs fois.";
							else if(searched instanceof Aliment)
								response = "Tu n'as plus de place dans ton frigo.";
							else
								response = "Impossible d'acheter l'item.";
						} else {
							response = searched.name + " acheté !";
							user.money -= searched.cost;
						}
					}
				}
			
			
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
