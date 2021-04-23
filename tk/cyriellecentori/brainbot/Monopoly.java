package tk.cyriellecentori.brainbot;

import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.Vector;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.MessageChannel;
import tk.cyriellecentori.brainbot.profiles.Profile;

public class Monopoly {
	
	private long guild;
	
	public Guild getGuild() {
		return Brainbot.jda.getGuildById(guild);
	}
	
	public static abstract class Case {
		protected String name;
		protected long partie;
		
		public Case(String name) {
			this.name = name;
		}
		
		public abstract void onIt(Profile profile, MessageChannel chan);
		
		public String toString() {
			return "Informations sur la case actuelle :\n"
					+ "Nom : " + name + "\n"
					+ "Partie : " + getPartie().getGuild().getName() + "\n";
		}
		
		public String quickString() {
			return name + "\n";
		}
		
		public String getName() {
			return name;
		}
		
		public void setPartie(long partie) {
			this.partie = partie;
		}
		
		protected Monopoly getPartie() {
			return Brainbot.monopolyParties.get(partie);
		}
	}
	
	public static abstract class Possession extends Case {
		protected long proprio = 0L;
		protected int prix;
		
		public Possession(String name, int prix) {
			super(name);
			this.prix = prix;
		}
		
		public String toString() {
			return super.toString()
					+ "Type : Possession\n"
					+ "Propriétaire : " + ((proprio == 0L) ? "Personne" : getPartie().getGuild().getMemberById(proprio).getEffectiveName()) + "\n"
					+ "Prix d'achat : " + prix + "\n";
		}
		
		public String quickString() {
			return super.quickString()
					+ "Propriétaire : " + ((proprio == 0L) ? "Personne" : getPartie().getGuild().getMemberById(proprio).getEffectiveName()) + "\n"
					+ "Achat : " + prix + "\n";
		}
		
		public void onIt(Profile profile, MessageChannel chan) {
			if(profile.id != proprio && proprio != 0L) {
				pay(profile, chan);
			} else if (proprio == 0L) {
				chan.sendMessage("Vous tombez sur la case \"" + name 
						+ "\", qui n'appartient à personne. Vous pouvez l'acheter pour la modique somme de " + prix 
						+ " " + Brainbot.getCurrency(getPartie().getGuild().getIdLong()) + " ou la laisser.").queue();
			} else {
				chan.sendMessage("Vous tombez sur votre propriété, \"" + name + "\" ! ").queue();
			}
		}
		
		public abstract void pay(Profile profile, MessageChannel chan);
		
		public int getPrix() {
			return prix;
		}
		
		public long getProprio() {
			return proprio;
		}
		
		public void setProprio(Profile player) {
			proprio = player.id;
		}
	}
	
	public static class Propriete extends Possession {
		protected int niveau;
		protected int[] loyers;
		protected int prixNiveau;
		
		public Propriete(String name, int prix, int[] loyers, int prixNiveau) {
			super(name, prix);
			this.prix = prix;
			this.loyers = loyers;
			this.prixNiveau = prixNiveau;
		}

		@Override
		public void pay(Profile profile, MessageChannel chan) {
				Profile proprioProfile = Brainbot.profiles.get(proprio);
				profile.money -= loyers[niveau];
				proprioProfile.money += loyers[niveau];
				chan.sendMessage("Vous êtes tombé sur la case \"" + name + "\", qui est la propriété de " 
						+ getPartie().getGuild().getMemberById(proprio).getEffectiveName()
						+ " ! Vous payez un loyer de " + loyers[niveau] + " " 
						+ Brainbot.getCurrency(getPartie().getGuild().getIdLong()) + " à cause du niveau " + niveau + " de la propriété.").queue();
			
		}
		
		public void upgrade(Profile profile, MessageChannel chan) {
			if(profile.id == proprio) {
				if(niveau < loyers.length - 1) {
					if(profile.money >= prixNiveau) {
						niveau++;
						profile.money -= prixNiveau;
						chan.sendMessage("La propriété \"" + name + "\" a été améliorée au niveau " + niveau).queue();
					} else {
						chan.sendMessage("Vous n'avez pas assez d'argent !").queue();
					}
				} else {
					chan.sendMessage("Le niveau de cette propriété est déjà maximum !").queue();
				}
			} else {
				chan.sendMessage("Vous n'êtes pas le propriétaire de cette propriété.").queue();
			}
			
		}
		
		public void downgrade(Profile profile, MessageChannel chan) {
			if(profile.id == proprio) {
				if(niveau > 0) {
					niveau--;
					profile.money += prixNiveau / 2;
					chan.sendMessage("La propriété \"" + name + "\" a été descendue au niveau " + niveau +
							"\nCela vous a rapporté " + prixNiveau / 2 + Brainbot.getCurrency(getPartie().getGuild().getIdLong())).queue();
				} else {
					chan.sendMessage("Le niveau de cette propriété est déjà minimum !").queue();
				}
			} else {
				chan.sendMessage("Vous n'êtes pas le propriétaire de cette propriété.").queue();
			}
			
		}
		
		public String toString() {
			String tloyers = "";
			for(int i = 0; i < loyers.length; i++) {
				tloyers += "Niveau " + i + " : " + loyers[i] + "\n"; 
			}
			return super.toString()
					+ "Sous-type : Propriété\n"
					+ "Loyer actuel : " + loyers[niveau] + "\n"
					+ "Niveau : " + niveau + "\n"
					+ "Coût d'amélioration : " + prixNiveau + "\n"
					+ "Table des loyers : \n"
					+ tloyers;
		}
		
		public String quickString() {
			String tloyers = "";
			for(int i = 0; i < loyers.length; i++) {
				if(i == niveau)
					tloyers += "__";
				tloyers += loyers[i];
				if(i == niveau)
					tloyers += "__";
				if(i != loyers.length - 1)
					tloyers += " - ";
			}
			return super.quickString()
					+ "Loyers : " + tloyers + "\n"
					+ "Amélioration : " + prixNiveau + "\n";
		}
	}
	
	public static class Gare extends Possession {
		static int prixDeBase = 250;
		
		public Gare(String name) {
			super(name, prixDeBase * 4);
		}

		@Override
		public void pay(Profile profile, MessageChannel chan) {
				Profile proprioProfile = Brainbot.profiles.get(proprio);
				int nbGares = getPartie().getNbGares(proprio);
				profile.money -= nbGares * nbGares * prixDeBase;
				proprioProfile.money += nbGares * nbGares * prixDeBase;
				chan.sendMessage("Vous êtes tombé sur la case \"" + name + "\", qui est la propriété de " 
						+ getPartie().getGuild().getMemberById(proprio).getEffectiveName()
						+ " ! Vous payez " + (nbGares * prixDeBase) +  " " + Brainbot.getCurrency(getPartie().getGuild().getIdLong()) 
						+ " à cause des " + nbGares + " possédées par le joueur.").queue();
			
		}
		
		public String toString() {
			int nbGares = getPartie().getNbGares(proprio);
			return super.toString()
					+ "Sous-type : Gare\n"
					+ ((nbGares == 0) ? "" : ("Coût actuel : " + nbGares * prixDeBase + "\n"
					+ "Nombre de gares du propriétaire : " + nbGares));
		}
		
	}
	
	public static class Compagnie extends Possession {
		
		static float taux = 0.1f;
		
		public Compagnie(String name) {
			super(name, 1000);
		}

		@Override
		public void pay(Profile profile, MessageChannel chan) {
				Profile proprioProfile = Brainbot.profiles.get(proprio);
				int moneyLess = Math.round(taux * (float) profile.money);
				profile.money -= moneyLess;
				proprioProfile.money += moneyLess;
				chan.sendMessage("Vous êtes tombé sur la case \"" + name + "\", qui est la propriété de " 
						+ getPartie().getGuild().getMemberById(proprio).getEffectiveName()
						+ " ! Vous payez " + moneyLess +  " " + Brainbot.getCurrency(getPartie().getGuild().getIdLong()) 
						+ ".").queue();
		}
		
		public String toString() {
			return super.toString()
					+ "Sous-type : Compagnie\n"
					+ "Taux : " + 100*taux + "%\n";
		}
		
	}
	
	public static abstract class Chance extends Case {
		public Chance() {
			super("Chance");
		}
		
		public abstract void onIt(Profile profile, MessageChannel chan);
		
		public String toString() {
			return super.toString()
					+ "Type : Chance";
		}
		
	}
	
	public static class BolmaChance extends Chance {
		
		public BolmaChance() {
			super();
		}

		@Override
		public void onIt(Profile profile, MessageChannel chan) {
			chan.sendMessage("Case chance !").queue();
			String message = "";
			int ran = Brainbot.random.nextInt(13);
			switch(ran) {
			case 1:
				message = "Vous trébuchez et la physique du monde plante ! Vous êtes téléporté(e) à une case aléatoire.";
				chan.sendMessage(message).queue();
				getPartie().toCase(profile, chan, Brainbot.random.nextInt(getPartie().getNbCases()));
				break;
			case 2:
				message = "Vous trouvez la montre à gousset d'un phasme tête en l'air, vous la revendez pour 500 roubles bolmaciennes.";
				chan.sendMessage(message).queue();
				profile.money += 500;
				break;
			case 3:
				message = "Vous croisez une limace et glissez sur sa base trois cases plus loin.";
				chan.sendMessage(message).queue();
				getPartie().upCase(profile, chan, 3);
				break;
			case 4:
				message = "Tournée générale ! Vous offrez 100 roubles à tous les joueurs.";
				chan.sendMessage(message).queue();
				for(Profile player : getPartie().listPlayers()) {
					player.money += 50;
				}
				
				profile.money -= 100 * getPartie().listPlayers().size();
				break;
			case 5:
				message = "Vous vous frottez à la mafia papillonesque, on vous fait retourner à la case départ.";
				chan.sendMessage(message).queue();
				getPartie().toCase(profile, chan, 0);
				break;
			case 6:
				message = "Votre alcoolisme vous fait passer toute une journée au bar. Impossible de jouer pendant 24h.";
				chan.sendMessage(message).queue();
				profile.lastPlays.replace(getPartie().getGuild().getIdLong(), System.currentTimeMillis() + 3600*18*1000);
				break;
				
			case 7:
				message = "Vous êtres pris en flagrant délit d'hérésie envers Ciel. Heureusement, le témoin est corruptible. Vous payez 500 roubles.";
				chan.sendMessage(message).queue();
				profile.money -= 500;
				break;
				
			case 8:
				message = "Vous espérez gagner de l'argent en tirant une carte chance. Pas de bol, vous perdez 200 roubles.";
				chan.sendMessage(message).queue();
				profile.money -= 200;
				break;
				
			case 9:
				message = "Vous croisez Ciel dans la rue. Elle vous fait part de sa grâce céleste en vous donnant le droit de rejouer un coup.";
				chan.sendMessage(message).queue();
				profile.lastPlays.replace(getPartie().getGuild().getIdLong(), 0L);
				break;
				
			case 10:
				message = "Vous jouez à la roulette russe avec des copains. ";
				chan.sendMessage(message).queue();
				Vector<Profile> prs = getPartie().listPlayers();
				int r = Brainbot.random.nextInt(prs.size());
				message += getPartie().getGuild().getMemberById(prs.get(r).id).getAsMention() + "perd. Cette personne perd 350 roubles en frais d'hospitalisation.";
				prs.get(r).money -= 350;
				break;
				
			case 11:
				message = "Vous laissez tomber un billet de 400 roubles par terre. ";
				chan.sendMessage(message).queue();
				Vector<Profile> prs1 = getPartie().listPlayers();
				int r1 = Brainbot.random.nextInt(prs1.size());
				message += getPartie().getGuild().getMemberById(prs1.get(r1).id).getAsMention() + " le ramasse.";
				prs1.get(r1).money += 400;
				profile.money -= 400;
				break;
				
			case 12:
				int sum = getPartie().getLevelSum(profile.id);
				if(sum == 0) {
					message = "Vous bénéficiez d'allocations ! Vous gagnez 800 roubles.";
					profile.money += 800;
				} else {
					message = "Taxe d'habitation ! Vous payez 150 roubles par niveau de vos propriétés, soit un total de " + sum * 150 + " roubles.";
					profile.money -= sum * 150;
				}
				chan.sendMessage(message).queue();
				break;
				
			default:
				message = "Vous piochez une carte chance, et elle diparait dans votre main. Rien ne se passe.";
				chan.sendMessage(message).queue();
			}
			
		}
		
	}
	
public static class TarbouChance extends Chance {
		
		public TarbouChance() {
			super();
		}

		@Override
		public void onIt(Profile profile, MessageChannel chan) {
			chan.sendMessage("Case chance !").queue();
			String message = "";
			int ran = Brainbot.random.nextInt(12);
			switch(ran) {
				
			default:
				message = "Vous piochez une carte chance, et elle diparait dans votre main. Rien ne se passe.";
			}
			chan.sendMessage(message).queue();
		}
		
		public String toString() {
			return super.toString()
					+ "Type : Chance";
		}
		
	}
	
	public static class Personne extends Case {
		public long id;
		
		public Personne(long id) {
			super(Brainbot.jda.getUserById(id).getName());
			this.id = id;
		}

		@Override
		public void onIt(Profile profile, MessageChannel chan) {
			chan.sendMessage("Vous tombez sur la case " + Brainbot.jda.getUserById(id).getAsMention() + " ! Cette personne décidera de votre sort...").queue();
		}
		
		public String toString() {
			return super.toString()
					+ "Type : Membre"
					+ "Membre : " + getPartie().getGuild().getMemberById(id).getEffectiveName();
		}
		
		public String quickString() {
			return super.quickString()
					+ "Membre : " + getPartie().getGuild().getMemberById(id).getEffectiveName();
		}
	}
	
	
	public static class Depart extends Case {
		public Depart() {
			super("Départ");
		}

		@Override
		public void onIt(Profile profile, MessageChannel chan) {
			profile.money += 400;
			chan.sendMessage("Vous arrivez à la case départ ! Vous recevez 400 " + Brainbot.getCurrency(getPartie().getGuild().getIdLong()) + " pour avoir pris le temps de vous y arrêter.").queue();
		}
		
		public String toString() {
			return super.toString()
					+ "Type : Départ";
		}
	}
	
	public static class Detente extends Case {
		
		String texte;
		
		public Detente(String name, String texte) {
			super(name);
			this.texte = texte;
		}

		@Override
		public void onIt(Profile profile, MessageChannel chan) {
			chan.sendMessage(texte).queue();
		}
		
	}
	
	public static class Taxe extends Detente {
		int prix;
		
		public Taxe(String name, String texte, int prix) {
			super(name, texte);
			this.prix = prix;
		}
		
		public void onIt(Profile profile, MessageChannel chan) {
			super.onIt(profile, chan);
			profile.money -= prix;
		}
		
		public String toString() {
			return super.toString()
					+ "Type : Taxe";
		}
	}
	
	
	
	/* Monopoly */
	
	protected Case[] cases;
	protected LinkedHashMap<Long, Integer> playerPos = new LinkedHashMap<Long, Integer>();
	
	public Monopoly(Case[] set, Guild guild) {
		this.guild = guild.getIdLong();
		cases = set;
		for(Case c : cases) {
			c.setPartie(this.guild);
		}
	}
	
	public int getNbGares(long playerID) {
		if(playerID == 0L)
			return 0;
		int nb = 0;
		for(Case c : cases) {
			if(c instanceof Gare) {
				if(((Gare) c).proprio == playerID) {
					nb++;
				}
			}
		}
		return nb;
	}
	
	public int getLevelSum(long playerID) {
		if(playerID == 0L)
			return 0;
		int nb = 0;
		for(Case c : cases) {
			if(c instanceof Propriete) {
				if(((Propriete) c).proprio == playerID) {
					nb += ((Propriete) c).niveau;
				}
			}
		}
		return nb;
	}
	
	public boolean lancer(Profile player, MessageChannel chan) {
		if(!playerPos.containsKey(player.id)) {
			playerPos.put(player.id, 0);
		}
		player.money += 20;
		if(player.money > 0) {
			player.played++;
			int currentCase = playerPos.get(player.id);
			int casesPlus = (Brainbot.random.nextInt(6) + 1) + (Brainbot.random.nextInt(6) + 1);
			currentCase = (currentCase + casesPlus) % cases.length;
			chan.sendMessage("Vous gagnez 20 " + Brainbot.getCurrency(guild) + " de participation !\nLancer du dé !\nVous avancez de " + casesPlus + " case" + (casesPlus > 1 ? "s" : "") + " !").queue();
			playerPos.put(player.id, currentCase);
			cases[playerPos.get(player.id)].onIt(player, chan);
			return true;
		}else {
			chan.sendMessage("Vous êtes à sec ! Gagnez un peu d'argent avant de jouer ! Vous gagnez tout de même 20 " + Brainbot.getCurrency(guild) + " de participation !").queue();
			return false;
		}
	}
	
	public void toCase(Profile player, MessageChannel chan, int nCase) {
		if(!playerPos.containsKey(player.id)) {
			playerPos.put(player.id, 0);
		}
		if(nCase >= cases.length)
			throw new ArrayIndexOutOfBoundsException();
		playerPos.put(player.id, nCase);
		cases[playerPos.get(player.id)].onIt(player, chan);
	}
	
	public void upCase(Profile player, MessageChannel chan, int plusCase) {
		if(!playerPos.containsKey(player.id)) {
			playerPos.put(player.id, 0);
		}
		toCase(player, chan, (playerPos.get(player.id) + plusCase) % cases.length);
	}
	
	public int getNbCases() {
		return cases.length;
	}
	
	public Vector<Profile> listPlayers() {
		Vector<Profile> ret = new Vector<Profile>();
		for(Long id : playerPos.keySet()) {
			ret.add(Brainbot.profiles.get(id));
		}
		return ret;
	}
	
	public void acheter(Profile player, MessageChannel chan) {
		if(!playerPos.containsKey(player.id)) {
			chan.sendMessage("Vous ne jouez pas encore au Monopoly ! Lancez le dé pour lancer votre partie !").queue();
			return;
		}
		Case currentCase = cases[playerPos.get(player.id)];
		if(currentCase instanceof Possession) {
			Possession currentPossession = (Possession) currentCase;
			if(player.money >= currentPossession.getPrix() && currentPossession.getProprio() == 0L) {
				player.money -= currentPossession.getPrix();
				currentPossession.setProprio(player);
				chan.sendMessage("Vous avez bien acheté la propriété \"" + currentCase.getName() + "\" !").queue();
			}else if(currentPossession.getProprio() != 0L){
				chan.sendMessage("Cette propriété est déjà en la possession de " + getGuild().getMemberById(currentPossession.getProprio()).getEffectiveName() + " !").queue();
			} else {
				chan.sendMessage("Vous n'avez pas assez d'argent !").queue();
			}
		}else {
			chan.sendMessage("Vous ne pouvez pas acheter cette case.").queue();
		}
	}
	
	public void setProprio(Profile player, int prop) {
		Possession currentCase = (Possession) cases[prop];
		currentCase.setProprio(player);
	}
	
	public Vector<Possession> getProp(Profile player) {
		Vector<Possession> output = new Vector<Possession>();
		for(Case c : cases) {
			if(c instanceof Possession)
				if(((Possession) c).proprio == player.id)
					output.add((Possession) c);
		}
		return output;
	}
	
	public void printInfoCase(Profile player, MessageChannel chan) {
		if(!playerPos.containsKey(player.id)) {
			chan.sendMessage("Vous ne jouez pas encore au Monopoly ! Lancez le dé pour lancer votre partie !").queue();
			return;
		}
		chan.sendMessage(cases[playerPos.get(player.id)].toString()).queue();
	}
	
	public boolean isPlaying(Profile player) {
		return playerPos.containsKey(player.id);
	}
	
	public String getPlayersDesc() {
		String ret = "Liste des joueurs : \n";
		for(Entry<Long, Integer> player : playerPos.entrySet()) {
			ret += getGuild().getMemberById(player.getKey()).getEffectiveName() + " - Case " + player.getValue() + " : " + cases[player.getValue()].getName() + "\n";
			ret += "Propriétés : \n";
			Vector<Possession> props = getProp(Brainbot.profiles.get(player.getKey()));
			if(props.size() == 0) {
				ret += "Aucune.\n";
			}
			for(Possession poss : props) {
				ret += poss.getName() + "\n";
			}
			ret += "———————————————————\n";
		}
		return ret;
	}
	
	@Override
	public String toString() {
		String ret = "Plateau du jeu : \n";
		for(int i = 0; i < cases.length; i++) {
			ret += i + " — " + cases[i].quickString() + "\n——————————————————\n";
		}
		return ret;
	}
}
