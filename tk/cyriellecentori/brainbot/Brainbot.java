
package tk.cyriellecentori.brainbot;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Vector;

import javax.security.auth.login.LoginException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDA.Status;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.MentionType;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.DisconnectEvent;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.ReconnectedEvent;
import net.dv8tion.jda.api.events.ResumedEvent;
import net.dv8tion.jda.api.events.guild.GuildBanEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.internal.managers.ChannelManagerImpl;
import tk.cyriellecentori.brainbot.BotExceptions.MoneyException;
import tk.cyriellecentori.brainbot.Monopoly.*;
import tk.cyriellecentori.brainbot.commands.BotCommand;
import tk.cyriellecentori.brainbot.commands.DrinkCommand;
import tk.cyriellecentori.brainbot.commands.MessageCommand;
import tk.cyriellecentori.brainbot.profiles.Achievement;
import tk.cyriellecentori.brainbot.profiles.AchievementFood;
import tk.cyriellecentori.brainbot.profiles.Profile;
import tk.cyriellecentori.brainbot.profiles.ProfilesHandler;
import tk.cyriellecentori.brainbot.profiles.AchievementFood.Contains;
import tk.cyriellecentori.brainbot.shop.Aliment;
import tk.cyriellecentori.brainbot.shop.Shop;
import tk.cyriellecentori.brainbot.BotExceptions.*;



public class Brainbot implements EventListener {

	public static String token;

	public static BufferedWriter messageLog;

	public static JDA jda;
	public static JDABuilder builder;

	public static Gson gson = new Gson();
	public static Vector<Long> serversNoReact = new Vector<Long>();
	public static ProfilesHandler profiles = new ProfilesHandler();

	public static long cyrielleID = 340877529973784586L;

	public static long bolmabarID = 482100088630542346L;
	public static long bolmabarComptoirID = 482102037752840202L;
	
	public static String prefix = "b!";
	public static String monoPrefix = "bm!";

	public static int messagesCounter = 0;

	public static String ipaddress = getCurrentIp();

	public static long cyriellePrivate = 356861332106117120L;

	public static long tarbouchID = 329046715619344385L;
	public static String version = "2.7";
	public static String patch = "1";
	public static String changelog = "```diff\n"
				+ "Brainbot version " + version + ".0 – Les Succès\n"
				+ "+ Ajout d'un système de Succès\n"
				+ "+ Ajout de succès en rapport avec le Frigo\n"
				+ "+ Ajout de succès cachés.\n"
				+ "+ Ajout de b!achievements.\n"
				+ "+ Affichage des succès eus sur le profil.\n"
				+ "- Correction de la lisibilité des aliments.\n"
				+ "\nPatch " + version + ".1\n"
				+ "+ Ajout de quelques succès.\n"
				+ "```";

	public static Random random = new Random();

	public static long secondComptoirID = 737937116830498876L;
	public static long sallePrincipaleID = 482101013474574336L;
	public static long archivesBarID = 686320659135201407L;

	public static LinkedHashMap<Long, String[]> works = new LinkedHashMap<Long, String[]>();

	public static String[] tvs = new String[] {};
	
	
	static public class InterfaceAdapter<T>
    implements JsonSerializer<T>, JsonDeserializer<T> {

    @Override
    public final JsonElement serialize(final T object, final Type interfaceType, final JsonSerializationContext context) 
    {
        final JsonObject member = new JsonObject();
        member.addProperty("type", object.getClass().getName());
        member.add("data", context.serialize(object));

        return member;
    }

    @Override
    public final T deserialize(final JsonElement elem, final Type interfaceType, final JsonDeserializationContext context) 
            throws JsonParseException 
    {
        final JsonObject member = (JsonObject) elem;
        final JsonElement typeString = get(member, "type");
        final JsonElement data = get(member, "data");
        final Type actualType = typeForName(typeString);

        return context.deserialize(data, actualType);
    }

    private Type typeForName(final JsonElement typeElem) 
    {
        try 
        {
            return Class.forName(typeElem.getAsString());
        } 
        catch (ClassNotFoundException e) 
        {
            throw new JsonParseException(e);
        }
    }

    private JsonElement get(final JsonObject wrapper, final String memberName) 
    {
        final JsonElement elem = wrapper.get(memberName);

        if (elem == null) 
        {
            throw new JsonParseException(
                "no '" + memberName + "' member found in json file.");
        }
        return elem;
    }

}


	public static void main(String[] args) throws IOException, InterruptedException {

		if(args.length < 1) {
			System.err.println("Merci d'indiquer le token du bot en paramètre.");
			return;
		}
		token = args[0];



		try {
			messageLog = new BufferedWriter(new FileWriter("/home/cyrielle/messages.log", true));
			System.setOut(new PrintStream("/home/cyrielle/botlog.txt"));
			System.setErr(new PrintStream("/home/cyrielle/boterr.txt"));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}


		System.out.println("──────────────────────────────────────NEW BOT INSTANCE──────────────────────────────────────");
		messageLog.write("──────────────────────────────────────NEW BOT INSTANCE──────────────────────────────────────");
		messageLog.newLine();
		messageLog.flush();



		try {

			builder = JDABuilder.createDefault(token)
					.enableIntents(GatewayIntent.GUILD_MEMBERS)
					.addEventListeners(new Brainbot())
					.setMemberCachePolicy(MemberCachePolicy.ALL);

			jda = builder.build();
		} catch(LoginException | IllegalArgumentException e) {
			e.printStackTrace();
			return;
		}
		jda.setAutoReconnect(true);
		
		 GsonBuilder gsonBilder = new GsonBuilder();
		 gsonBilder.registerTypeAdapter(Case.class, new InterfaceAdapter<Case>());
		 gsonBilder.setPrettyPrinting();

		 gson =gsonBilder.create();



		String noReactData = "";
		String profilesData = "";

		BufferedReader noReactFile = new BufferedReader(new FileReader("noreact.json"));
		while(true) {
			String str = noReactFile.readLine();
			if(str == null) break;
			else noReactData = noReactData + "\n" + str;
		}

		noReactFile.close();

		BufferedReader profilesFile = new BufferedReader(new FileReader("profiles.json"));
		while(true) {
			String str = profilesFile.readLine();
			if(str == null) break;
			else profilesData = profilesData + "\n" + str;
		}

		profilesFile.close();


		serversNoReact = gson.fromJson(noReactData, new TypeToken<Vector<Long>>() {}.getType());
		profiles = gson.fromJson(profilesData, profiles.getClass());
		profiles.checkAll();
		if(profiles == null)
			profiles = new ProfilesHandler();


		try {
			String worksData = "";
			BufferedReader worksFile = new BufferedReader(new FileReader("works.json"));
			while(true) {
				String str = worksFile.readLine();
				if(str == null) break;
				else worksData = worksData + "\n" + str;
			}
			works = gson.fromJson(worksData, new TypeToken<LinkedHashMap<Long, String[]>>() {}.getType());
			worksFile.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(serversNoReact == null)
			serversNoReact = new Vector<Long>();
		initCommands();
		try {
			String tvsData = "";
			BufferedReader tvsFile = new BufferedReader(new FileReader("tvs.json"));
			while(true) {
				String str = tvsFile.readLine();
				if(str == null) break;
				else tvsData = tvsData + "\n" + str;
			}
			tvs = gson.fromJson(tvsData, new TypeToken<String[]>() {}.getType());
			tvsFile.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		try {
			String monoData = "";
			BufferedReader monoFile = new BufferedReader(new FileReader("monopoly.json"));
			while(true) {
				String str = monoFile.readLine();
				if(str == null) break;
				else monoData = monoData + "\n" + str;
			}
			monopolyParties = gson.fromJson(monoData, new TypeToken<LinkedHashMap<Long, Monopoly>>() {}.getType());
			monoFile.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		if(monopolyParties == null)
			monopolyParties = new LinkedHashMap<Long, Monopoly>();

	}
	
	


	public static void save() throws IOException {
		BufferedWriter noReactFile = new BufferedWriter(new FileWriter("noreact.json"));
		BufferedWriter profilesFile = new BufferedWriter(new FileWriter("profiles.json"));
		BufferedWriter monopolyFile = new BufferedWriter(new FileWriter("monopoly.json"));
		noReactFile.write(gson.toJson(serversNoReact));
		profilesFile.write(gson.toJson(profiles));
		monopolyFile.write(gson.toJson(monopolyParties));
		noReactFile.close();
		profilesFile.close();
		monopolyFile.close();
	}



	public static String getCurrentIp() {
		// Find public IP address 
		String systemipaddress = ""; 
		try
		{ 
			URL url_name = new URL("http://bot.whatismyipaddress.com"); 

			BufferedReader sc = 
					new BufferedReader(new InputStreamReader(url_name.openStream())); 

			// reads system IPAddress 
			systemipaddress = sc.readLine().trim(); 
		} 
		catch (Exception e) 
		{ 
			systemipaddress = "Cannot Execute Properly";
			e.printStackTrace();
		} 
		return (systemipaddress); 
	}

	public static String getCurrency(long server) {
		if(server == 329046715619344385L) {
			return "FezDollars";
		}else if(server == 482100088630542346L){
			return "roubles bolmaciennes";
		}else if(server == 564515181766377511L) { 
			return "grammes de chocolat";
		}else {
			return "roubles";
		}
	}
	
	
	public static Vector<Case[]> monopolySets = new Vector<Case[]>();
	public static LinkedHashMap<Long, Monopoly> monopolyParties = new LinkedHashMap<Long, Monopoly>();
	
	public static void initMonopolySets() {
		Case[] bolmaSet = new Case[40];
		bolmaSet[0] = new Depart();
		bolmaSet[1] = new Propriete("Ruines du palais présidentiel", 240, new int[] {8,40,120,270,640,1000}, 200);
		bolmaSet[2] = new BolmaChance();
		bolmaSet[3] = new Propriete("Bombe nucléaire dans un jardin", 240, new int[] {16,80,240,540,1280,2000}, 200);
		bolmaSet[4] = new Taxe("Racket de phasmes", "Vous tombez sur la case \"Racket de phasmes\" ! Ils vous volent 200 roubles !", 200);
		bolmaSet[5] = new Gare("Miniplein");
		bolmaSet[6] = new Propriete("Usine de Yop au Jambon", 400, new int[] {24, 120, 360, 810, 1600, 2200}, 200);
		bolmaSet[7] = new Personne(cyrielleID);
		bolmaSet[8] = new Propriete("Roue de Hamster géante", 400, new int[] {24, 120, 360, 810, 1600, 2200}, 200);
		bolmaSet[9] = new Propriete("L'égoûterie RAT-P", 480, new int[] {32, 160, 400, 1200, 1800, 2400}, 200);
		bolmaSet[10] = new Detente("Parc radioactif", "Vous vous promenez à travers les ruines d'un vieux parc à la gloire d'une limace. Il est désormais rempli de déchets radioactifs,");
		bolmaSet[11] = new Propriete("Salle de concert Kipic", 560, new int[] {40, 200, 600, 1800, 2500, 3000}, 400);
		bolmaSet[12] = new Compagnie("Service d'incinération des morts");
		bolmaSet[13] = new Propriete("Observatoire de la Rose", 560, new int[] {40, 200, 600, 1800, 2500, 3000}, 400);
		bolmaSet[14] = new Propriete("Bar E-Sports SonicZone", 640, new int[] {48, 240, 720, 2000, 2800, 3600}, 400);
		bolmaSet[15] = new Gare("Miniamour");
		bolmaSet[16] = new Propriete("Magasin de cagoules de Chenlin", 720, new int[] {56, 280, 800, 2200, 2800, 3600}, 400);
		bolmaSet[17] = new BolmaChance();
		bolmaSet[18] = new Propriete("Studio de BolmaciaFM-TV", 720, new int[] {56, 280, 800, 2200, 2800, 3600}, 400);
		bolmaSet[19] = new Propriete("Musée d'Histoire Naturelle", 800, new int[] {64, 320, 880, 2400, 3200, 4000}, 400);
		bolmaSet[20] = new Detente("Rideau de Fer", "Vous vous promenez le long du grand mur séparant les papillons des limaces.");
		bolmaSet[21] = new Propriete("Porcherie nationalisée en goulag", 880, new int[] {72, 360, 1000, 2800, 3800, 4200}, 600);
		bolmaSet[22] = new BolmaChance();
		bolmaSet[23] = new Propriete("Forêt sauvage de Bavéliande", 880, new int[] {72, 360, 1000, 2800, 3800, 4200}, 600);
		bolmaSet[24] = new Propriete("Guilde des Guides de Sibérie", 960, new int[] {80, 400, 1200, 3000, 3700, 4400}, 600);
		bolmaSet[25] = new Gare("Minipax");
		bolmaSet[26] = new Propriete("Zoo privatisé en bureaux", 1040, new int[] {88, 440, 1320, 3200, 3900, 4600}, 600);
		bolmaSet[27] = new Propriete("Cabinet Médical de Padsécu", 1040, new int[] {88, 440, 1320, 3200, 3900, 4600}, 600);
		bolmaSet[28] = new Compagnie("Service d'envoi des morts dans l'espace");
		bolmaSet[29] = new Propriete("Université privée de la Sordonne-et-je-prend", 1120, new int[] {96, 480, 1440, 3400, 4100, 4800}, 600);
		bolmaSet[30] = new Detente("Exécution publique", "Tu assistes à une exécution publique pour te détendre. Ça fait du bien.");
		bolmaSet[31] = new Propriete("Ambassade Tarboucharde", 1200, new int[] {104, 520, 1560, 3600, 4400, 5100}, 800);
		bolmaSet[32] = new Propriete("Commissariat de la police de la pensée", 1200, new int[] {104, 520, 1560, 3600, 4400, 5100}, 800);
		bolmaSet[33] = new Personne(cyrielleID);
		bolmaSet[34] = new Propriete("Laboratoires sous-terrains", 1280, new int[] {112, 600, 1800, 4000, 4800, 5600}, 800);
		bolmaSet[35] = new Gare("Miniver");
		bolmaSet[36] = new BolmaChance();
		bolmaSet[37] = new Propriete("Bar Bolmacien", 1400, new int[] {140, 700, 2000, 4400, 5200, 6000}, 800);
		bolmaSet[38] = new Taxe("Don à l'Église", "Vous faites ~~de votre plein gré~~ un don de 500 roubles à l'Église.", 500);
		bolmaSet[39] = new Propriete("Temple de Ciel", 1600, new int[] {200, 800, 2400, 5600, 6800, 8000}, 1000);
		
		monopolySets.add(bolmaSet);
		
		
		Case[] tarbouSet = new Case[40];
		tarbouSet[0] = new Depart();
		tarbouSet[1] = new Propriete("Camp de réfugiés", 240, new int[] {8,40,120,270,640,1000}, 200);
		tarbouSet[2] = new TarbouChance();
		tarbouSet[3] = new Propriete("Friches industrielles", 240, new int[] {16,80,240,540,1280,2000}, 200);
		tarbouSet[4] = new Taxe("Enfants des rues", "Vous tombez sur la case \"Enfants des rues\" ! Ils vous volent 200 roubles ! Faut bien qu'ils mangent…", 200);
		tarbouSet[5] = new Gare("Port commercial");
		tarbouSet[6] = new Propriete("Élevage de chameaux à kebab", 400, new int[] {24, 120, 360, 810, 1600, 2200}, 300);
		tarbouSet[7] = new TarbouChance();
		tarbouSet[8] = new Propriete("Élevage de poules à dents", 400, new int[] {24, 120, 360, 810, 1600, 2200}, 300);
		tarbouSet[9] = new Propriete("Désert des serpents radioactifs", 480, new int[] {32, 160, 400, 1200, 1800, 2400}, 300);
		tarbouSet[10] = new Detente("Avenue des Parades Militaires", "");
		tarbouSet[11] = new Propriete("Souk des tailleurs de fez", 560, new int[] {40, 200, 600, 1800, 2500, 3000}, 400);
		tarbouSet[12] = new Compagnie("Manufacture nationale des blindés");
		tarbouSet[13] = new Propriete("Ruelle des zbeuglatines", 560, new int[] {40, 200, 600, 1800, 2500, 3000}, 400);
		tarbouSet[14] = new Propriete("Souk cybernétique", 640, new int[] {48, 240, 720, 2000, 2800, 3600}, 400);
		tarbouSet[15] = new Gare("Port de guerre");
		tarbouSet[16] = new Propriete("Caserne de la Garde Dictatoriale", 720, new int[] {56, 280, 800, 2200, 2800, 3600}, 500);
		tarbouSet[17] = new TarbouChance();
		tarbouSet[18] = new Propriete("Secteur libre de Bled City", 720, new int[] {56, 280, 800, 2200, 2800, 3600}, 500);
		tarbouSet[19] = new Propriete("Ambassade bolmacienne", 800, new int[] {64, 320, 880, 2400, 3200, 4000}, 500);
		tarbouSet[20] = new Detente("Mémorial des Grandes Guerres Nucléaires", "");
		tarbouSet[21] = new Propriete("Tours de PFM-TV et WC News", 880, new int[] {72, 360, 1000, 2800, 3800, 4200}, 600);
		tarbouSet[22] = new TarbouChance();
		tarbouSet[23] = new Propriete("Rédaction du P'tit Tarbouchard", 880, new int[] {72, 360, 1000, 2800, 3800, 4200}, 600);
		tarbouSet[24] = new Propriete("Ministère de la Censure et de la Propagande d'État", 960, new int[] {80, 400, 1200, 3000, 3700, 4400}, 600);
		tarbouSet[25] = new Gare("Chemin de fer du Hedjaz");
		tarbouSet[26] = new Propriete("Orphelinat de Souk Tarbouch", 1040, new int[] {88, 440, 1320, 3200, 3900, 4600}, 700);
		tarbouSet[27] = new Propriete("Grande Roue de Dar-el-Fez", 1040, new int[] {88, 440, 1320, 3200, 3900, 4600}, 700);
		tarbouSet[28] = new Compagnie("Centre de tests nucléaires");
		tarbouSet[29] = new Propriete("Stadium de Bled City", 1120, new int[] {96, 480, 1440, 3400, 4100, 4800}, 700);
		tarbouSet[30] = new Detente("", "");
		tarbouSet[31] = new Propriete("Autel du Feu Sacré", 1200, new int[] {104, 520, 1560, 3600, 4400, 5100}, 800);
		tarbouSet[32] = new Propriete("Piscine du Cap'tain Abyss", 1200, new int[] {104, 520, 1560, 3600, 4400, 5100}, 800);
		tarbouSet[33] = new TarbouChance();
		tarbouSet[34] = new Propriete("Statue de Cap'tain, guidant les Tarbouchards vers la prospérité", 1200, new int[] {104, 520, 1560, 3600, 4400, 5100}, 800);
		tarbouSet[35] = new Gare("Aéroport International de Tarbouchie");
		tarbouSet[36] = new TarbouChance();
		tarbouSet[37] = new Propriete("Conseil Tribal de Bled City", 1400, new int[] {140, 700, 2000, 4400, 5200, 6000}, 1000);
		tarbouSet[38] = new Taxe("Campagne d'imposition des b!work", "", 500);
		tarbouSet[39] = new Propriete("Palais d'Abyssia Island", 1600, new int[] {200, 800, 2400, 5600, 6800, 8000}, 1000);
	}
	
	public static boolean initMonopoly(int set, Guild guild, boolean overwrite) {
		if(monopolyParties.containsKey(guild.getIdLong()) && !overwrite) {
			return false;
		}
		monopolyParties.put(guild.getIdLong(), new Monopoly(monopolySets.get(set), guild));
		return true;
	}

	public static LinkedHashMap<String, BotCommand> bcommands = new LinkedHashMap<String, BotCommand>();
	public static LinkedHashMap<String, BotCommand> bmcommands = new LinkedHashMap<String, BotCommand>();
	
	public static String formatTime(long millis) {
		long seconds = millis / 1000;
		millis = millis % 1000;
		long minutes = seconds / 60;
		seconds = seconds % 60;
		long hours = minutes / 60;
		minutes %= 60;
		String ret = "";
		if(hours != 0) {
			ret = ret.concat(hours + " heures");
		}
		
		if(hours != 0 && minutes == 0) {
			ret = ret.concat(".");
		} else if(hours != 0) {
			ret = ret.concat(" et ");
		}
		
		if(minutes != 0) {
			ret = ret.concat(minutes + " minutes.");
		} else if(minutes == 0 && hours == 0 && seconds > 0) {
			ret = ret.concat("moins d'une minute.");
		}
		
		
		
		return ret;
	}
	
	public static void initCommands() {
		
		Aliment[] tem = new Aliment[100];
		Aliment temF = Aliment.searchAliment("Tem flak");
		for(int i = 0; i < 100; i++) {
			tem[i] = temF;
		}
		
		AchievementFood.frigoAchievements  = new AchievementFood[]{
				new AchievementFood.Contains("Un frigo comme Cyrielle", "Ayez le même frigo que Cyrielle", false, 500, new Aliment[]{
						Aliment.searchAliment("Tarte Caramel-Cannelle"),
						Aliment.searchAliment("Fraises"),
						Aliment.searchAliment("Beurre salé"),
						Aliment.searchAliment("Pastèque"),
						Aliment.searchAliment("Crème Chantilly"),
						Aliment.searchAliment("Noisettes"),
						Aliment.searchAliment("Truffade"),
						Aliment.searchAliment("Boblenna"),
						Aliment.searchAliment("Pomme de terre"),
						Aliment.searchAliment("Kouign-Amann"),
						Aliment.searchAliment("Brocoli")
					}, true),
				new AchievementFood.Contains("i cAN GO TO COOLEG!", "Achetez assez de Temmie Flakes pour leur permettre d'obtenir une éducation.", true, 200, tem, false),
				new AchievementFood.Note("Le Frigo Parfait", "Ayez un score de 10/10 ou plus sur votre Frigo.", false, 7500, 10, false),
				new AchievementFood.Note("Le Frigo Plus-Que-Parfait", "Ayez le score maximal sur votre Frigo.", false, 10000, 13, true),
				new AchievementFood.Contains("Purgatoire", "Ayez un frigo contenant à la fois du beurre doux et du beurre salé.", false, 500, new Aliment[] {
						Aliment.searchAliment("Beurre salé"),
						Aliment.searchAliment("Beurre doux")
				}, false),
				new AchievementFood.Contains("Paradis", "Faites de votre Frigo un paradis.", true, 2500, new Aliment[] {
						Aliment.searchAliment("Beurre salé"),
						Aliment.searchAliment("Beurre salé"),
						Aliment.searchAliment("Beurre salé"),
						Aliment.searchAliment("Beurre salé"),
						Aliment.searchAliment("Beurre salé")
				}, false),
				new AchievementFood.Contains("Enfer", "Faites de votre Frigo un enfer.", true, 1, new Aliment[] {
						Aliment.searchAliment("Beurre doux"),
						Aliment.searchAliment("Beurre doux"),
						Aliment.searchAliment("Beurre doux"),
						Aliment.searchAliment("Beurre doux"),
						Aliment.searchAliment("Beurre doux")
				}, false),
				new AchievementFood.Note("Vomi du barman", "Faites vomir le patron.", true, 1, 0, true),
				new AchievementFood.Note("Perfectly balanced", "As all things should be.", true, 500, 5, true),
				new AchievementFood.Contains("Frigo dangereux", "Placez le danger dans votre frigo.", true, 5000, new Aliment[] {
						Aliment.searchAliment("Truffade"),
						Aliment.searchAliment("Kouign-Amann"),
						Aliment.searchAliment("Sirop d'Orgeat"),
						Aliment.searchAliment("Roblochon"),
						Aliment.searchAliment("Brandade de Morue"),
						Aliment.searchAliment("Paté"),
						Aliment.searchAliment("Tem flak")
				}, false),
				new AchievementFood.Contains("Notch", "Achetez sa pomme.", false, 0, new Aliment[] {
						Aliment.searchAliment("Pomme de Notch")
				}, false),
				new AchievementFood.Contains("Ça ne marche pas comme ça…", "Tentez de l'alchimie à base de pommes d'or.", true, 15000, new Aliment[] {
						Aliment.searchAliment("Pomme d'or"),
						Aliment.searchAliment("Pomme d'or"),
						Aliment.searchAliment("Pomme d'or"),
						Aliment.searchAliment("Pomme d'or"),
						Aliment.searchAliment("Pomme d'or"),
						Aliment.searchAliment("Pomme d'or"),
						Aliment.searchAliment("Pomme d'or"),
						Aliment.searchAliment("Pomme d'or"),
						Aliment.searchAliment("Pomme d'or"),
						Aliment.searchAliment("Pomme d'or")
				}, false),
				new AchievementFood.Contains("Une odeur de provence", "Faites entrer la provence dans votre frigo.", true, 5000, new Aliment[] {
						Aliment.searchAliment("Herbes de provence"),
						Aliment.searchAliment("Gambetta"),
						Aliment.searchAliment("Pomme de terre"),
						Aliment.searchAliment("Olives"),
						Aliment.searchAliment("Glace à la vanille")
				}, false),
				new AchievementFood.Contains("Cheese Master", "Achetez tout le fromage du monde.", false, 2000, new Aliment[] {
						Aliment.searchAliment("Roblochon"),
						Aliment.searchAliment("Saint-Nectaire"),
						Aliment.searchAliment("Emmental"),
						Aliment.searchAliment("Fromage de chèvre"),
						Aliment.searchAliment("Truffade"),
						Aliment.searchAliment("Aligot"),
						Aliment.searchAliment("Raclette"),
						Aliment.searchAliment("Tartiflette"),
						Aliment.searchAliment("Saucisson au Saint-Nectaire")
				}, false),
				new AchievementFood.Contains("Un volcan dans le frigo", "Faites pousser un volcan dans votre frigo.", true, 5000, new Aliment[] {
						Aliment.searchAliment("Croissant aux amandes"),
						Aliment.searchAliment("Pâtisserie du Saint-Laurent"),
						Aliment.searchAliment("Saint-Nectaire"),
						Aliment.searchAliment("Truffade"),
						Aliment.searchAliment("Aligot"),
						Aliment.searchAliment("Pomme de terre"),
						Aliment.searchAliment("Saucisson"),
						Aliment.searchAliment("Saucisson au Saint-Nectaire")
				}, false),
				new AchievementFood.Contains("Qu'est-ce que c'est que cette odeur", "Sérieusement, beurk.", true, 10000, new Aliment[] {
						Aliment.searchAliment("Roblochon"),
						Aliment.searchAliment("Roblochon"),
						Aliment.searchAliment("Roblochon"),
						Aliment.searchAliment("Roblochon"),
						Aliment.searchAliment("Roblochon"),
						Aliment.searchAliment("Roblochon"),
						Aliment.searchAliment("Roblochon"),
						Aliment.searchAliment("Roblochon"),
						Aliment.searchAliment("Roblochon"),
						Aliment.searchAliment("Roblochon")
				}, false),
				new AchievementFood.Contains("Sponsorisé par MTT", "Faites sponsoriser votre frigo.", true, 5000, new Aliment[] {
						Aliment.searchAliment("Tarte Caramel-Cannelle"),
						Aliment.searchAliment("Glamburger"),
						Aliment.searchAliment("Glamburger"),
						Aliment.searchAliment("Glamburger"),
						Aliment.searchAliment("Tem flak"),
						Aliment.searchAliment("Cannelle")
				}, false)
			};
		
		Vector<Long> vecyr = new Vector<Long>();
		vecyr.add(cyrielleID);
		
		Vector<Long> bolmaVec = new Vector<Long>();
		bolmaVec.add(bolmabarID);
		
		Vector<Long> tarVec = new Vector<Long>();
		tarVec.add(tarbouchID);
		bcommands.put("carte", new MessageCommand("carte", "Affiche la liste des boissons. Alias : b!drinks", "Vous avez le choix parmi une multitude de boissons : \n"
							+ "De la bière :beer: (" + prefix + "beer) \n"
							+ "Du café :coffee: (" + prefix + "coffee) \n"
							+ "Du thé :tea: (" + prefix + "tea)\n"
							+ "Du vin de qualité :wine_glass: (" + prefix + "wine) \n"
							+ "Du whisky :whisky: (" + prefix + "whisky)\n"
							+ "Du sake :sake: (" + prefix + "sake)\n"
							+ "Du yop au jambon (" + prefix + "yop)\n"
							+ "J'ai de l'eau aussi mais c'est nul (" + prefix + "water)\n"
							+ "Les prix ? Bof, c'est pas important.\nAlors, vous prenez quoi ?"));
		bcommands.put("drinks", new BotCommand.Alias("drinks", bcommands.get("carte")));
		bcommands.put("mrboom", new MessageCommand("mrboom", "Toujours de sa faute.", "La fin du monde ? "
						+ "Une chèvre qui mange tes parents ? "
						+ "Ta·ton petit·e ami·e qui est en fait Cyriéphile ? "
						+ "C'est la faute de MrBoom !"));
		bcommands.put("invite", new MessageCommand("invite", "Invitez le bot sur votre serveur !", "V'la l'invit : " + jda.getInviteUrl(Permission.values())));
		bcommands.put("info", new MessageCommand("info", "Des informations moi.", "Bot par Cyrielle#3528, code source disponible sur demande. Version " + version + "." + patch));
		bcommands.put("changelog", new MessageCommand("changelog", "Les dernières modifications du code.", changelog));
		bcommands.put("ip", new MessageCommand("ip", "Seule une seule personne peut voir mon ip.", "", vecyr, false) {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				this.response = getCurrentIp();
				super.execute(bb, message);
			}
		});
		bcommands.put("admingive", new BotCommand("admingive", "Pour donner de l'argent sans en donner.", vecyr, false) {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				int toGive = Integer.parseInt(message.getMessage().getContentRaw().split(" ")[1]);
				User user = message.getMessage().getMentionedUsers().get(0);
				Profile pf = profiles.get(user.getIdLong());
				pf.money += toGive;

				message.getChannel().sendMessage("Cyrielle a généreusement offert " + toGive + " balles à " + user.getAsMention() + " !").queue();
			}
		});
		bcommands.put("mesays", new BotCommand("mesays", "Je dirai ce qu'on me dira de dire !", vecyr, false) {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				String toSay = message.getMessage().getContentRaw().split(" ", 2)[1];
				message.getChannel().sendMessage(toSay).queue();
				message.getMessage().delete().queue();
			}
		});
		bcommands.put("megaban", new BotCommand("megaban", "Pour les ADMINS qui veulent MEGABAN des gens, même s'ils sont pas sur le serv.") {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				if(message.isFromGuild())
					if(message.getMember().hasPermission(Permission.ADMINISTRATOR)) {
						try {
							String id = message.getMessage().getContentRaw().split(" ")[1];
							message.getChannel().sendMessage("BANNED " + message.getGuild().getMemberById(id).getAsMention()).queue();
							message.getGuild().ban(id, 0).queue();
						}catch(ArrayIndexOutOfBoundsException e) {
							message.getChannel().sendMessage("Il manque l'ID de la personne à ban.").queue();
						}
					}
			}
		});
		bcommands.put("toggle", new BotCommand("toggle", "Permet de changer quelques paramètres du bot.") {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				if(message.isFromGuild())
					if(message.getMember().hasPermission(Permission.ADMINISTRATOR)) {
						try {
							String func = message.getMessage().getContentRaw().split(" ")[1];
							if(func.equals("réactions")) {
								if(serversNoReact.contains(message.getGuild().getIdLong())) {
									serversNoReact.remove(message.getGuild().getIdLong());
									message.getChannel().sendMessage("Fonctionnalité de réactions activée.").queue();
								}else {
									serversNoReact.add(message.getGuild().getIdLong());
									message.getChannel().sendMessage("Fonctionnalité de réactions désactivée.").queue();
								}
							}
						}catch(ArrayIndexOutOfBoundsException e) {
							message.getChannel().sendMessage("Il manque la fonctionnalité à désactiver.").queue();
						}
					}
			}
		});
		bcommands.put("money", new BotCommand("money", "Affiche votre argent actuel ou celui des autres en ajoutant une mention utilisateur.") {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				if(message.isFromGuild()) {
					if(message.getMessage().getMentionedUsers().isEmpty()) {
						if(message.getAuthor().getIdLong() == cyrielleID) {
							message.getChannel().sendMessage("Vous avez une quantité infinie d'argent.").queue();
						} else if(profiles.get(message.getAuthor().getIdLong()).money == 0) {
							message.getChannel().sendMessage("Vous êtes à sec !").queue();
						}else {
							message.getChannel().sendMessage("Vous avez " + profiles.get(message.getAuthor().getIdLong()).money
									+ " " + getCurrency(message.getGuild().getIdLong()) + ".").queue();
						}
					}else if(message.getMessage().getMentionedUsers().get(0).getIdLong() == cyrielleID) {
						message.getChannel().sendMessage("Cyrielle a une quantité infinie d'argent, voyons !").queue();
					} else {
						User toKnow = message.getMessage().getMentionedUsers().get(0);
						if(profiles.get(toKnow.getIdLong()).money == 0) {
							message.getChannel().sendMessage(message.getGuild().getMember(toKnow).getEffectiveName() + " est a sec !").queue();
						}else {
							message.getChannel().sendMessage(message.getGuild().getMember(toKnow).getEffectiveName()
									+ " a " + profiles.get(toKnow.getIdLong()).money + " "
									+ getCurrency(message.getGuild().getIdLong()) + ".").queue();
						}
					}
				}
			}
		});
		bcommands.put("works", new BotCommand("works", "Vous envoie en MP la liste de vos b!work découverts jusqu'à présent.") {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				Profile pro = profiles.get(message.getAuthor().getIdLong());
				PrivateChannel pv = message.getAuthor().openPrivateChannel().complete();
				String list = "Liste des b!work trouvés jusqu'à présent : \n";
				for(Entry<Long, Vector<Integer>> entry : pro.worksCollec.entrySet()) {
					String total = entry.getValue().size() + "/" + works.get(entry.getKey()).length;
					list += jda.getGuildById(Math.abs(entry.getKey())).getName() + (entry.getKey() > 0 ? "(Positifs)" : "(Négatifs)") + " — " + total + " :\n";
					for(int w : entry.getValue()) {
						list += w + " — " + works.get(entry.getKey())[w] + "\n";
					}
					list += "\n";
				}
				if(list.length() >= 2000) {
					String[] spli = list.split("\n");
					String send = "";
					for(String str : spli) {
						if(send.length() + str.length() + 1 > 2000) {
							pv.sendMessage(send).queue();
							send = "";
						}
						send += str + "\n";
					}
					pv.sendMessage(send).queue();
				} else {
					pv.sendMessage(list).queue();
				}
				message.getChannel().sendMessage("Envoyé en privé !").queue();
			}
		});
		bcommands.put("work", new BotCommand("work", "Travaillez pour gagner de l'argent !") {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				User author = message.getAuthor();
				MessageChannel channel = message.getChannel();
				if(message.isFromGuild()) {
					if(System.currentTimeMillis() - profiles.get(author.getIdLong()).lastwork >= 3600000L) {
						Profile pro = profiles.get(author.getIdLong());
						pro.worked++;
						int modifier = (random.nextInt(5) == 0) ? -1 : 1;
						int win = (modifier == 1) ? random.nextInt(20) + 30 : -35 - random.nextInt(10);
						pro.money += win;
						pro.lastwork = System.currentTimeMillis();
						String[] worksTab = works.get(message.getGuild().getIdLong() * modifier);
						

						if(worksTab != null){
							if(!pro.worksCollec.containsKey(message.getGuild().getIdLong() * modifier)) {
								pro.worksCollec.put(message.getGuild().getIdLong() * modifier, new Vector<Integer>());
							}
							int randomChoice = random.nextInt(worksTab.length);
							String toSend = worksTab[randomChoice]
									+ "\nTu " + ((modifier == 1) ? "gagnes " : "perds ") + Math.abs(win) + " "
									+ getCurrency(message.getGuild().getIdLong()) + ".";
							if(!pro.worksCollec.get(message.getGuild().getIdLong() * modifier).contains(randomChoice)) {
								toSend += "\nNouveau b!work ajouté à la collection !";
								pro.worksCollec.get(message.getGuild().getIdLong() * modifier).add(randomChoice);
								if(pro.worksCollec.get(message.getGuild().getIdLong() * modifier).size() == works.get(message.getGuild().getIdLong() * modifier).length) {
									Achievement a = new Achievement("Travailleur acharné — " + message.getGuild().getName() 
											+ " — " + ((modifier == 1) ? "Positifs" : "Négatifs"), "A collecté tous les travaux " + 
									((modifier == 1) ? "positifs" : "négatifs") + " de ce serveur.", false, 0);
									pro.giveAchievement(a, message);
									toSend += "\nSuccès obtenu !\n" + a.toString();
								}
							}
							channel.sendMessage(toSend).queue();
						} else {
							channel.sendMessage("Déso, mais les trucs humour du " + prefix + "work ne sont pas encore dispo pour ce serveur. "
									+ "Demandez à <40877529973784586> de rajouter ça ! En attendant, tu gagnes "
									+ win + " " + getCurrency(message.getGuild().getIdLong()) + ".").queue();
						}
					}else {
						channel.sendMessage("T'essayerais pas d'membobiner par hasard ? T'as bossé y'a moins d'une heure "
								+ "! Aller, ouste ! Encore "
								+ formatTime(3600000L - (System.currentTimeMillis() - profiles.get(author.getIdLong()).lastwork))).queue();
					}
				}
			}
		});
		bcommands.put("tv", new BotCommand("tv", "Regardez la TV officielle de Tarbouchie !", tarVec, true) {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				User author = message.getAuthor();
				MessageChannel channel = message.getChannel();
				if(System.currentTimeMillis() - profiles.get(author.getIdLong()).lasttv >= 6L * 3600000L) {
					channel.sendMessage(tvs[random.nextInt(tvs.length)]
							+ "\nTa fidélité te rapporte 15 FezDollars !").queue();
					profiles.get(author.getIdLong()).money += 15;
					profiles.get(author.getIdLong()).lasttv = System.currentTimeMillis();
				} else {
					channel.sendMessage("Assez de TV pour l'instant, il faut être productif !").queue();
				}
			}
		});
		bcommands.put("give", new BotCommand("give", "Donnez quelque chose à quelqu'un. N'importe quoi.") {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				User author = message.getAuthor();
				MessageChannel channel = message.getChannel();
				if(message.isFromGuild())
					try {
						int toGive = Integer.parseInt(message.getMessage().getContentRaw().split(" ")[1]);
						User user = message.getMessage().getMentionedUsers().get(0);
						profiles.pay(author.getIdLong(), toGive);
						profiles.get(user.getIdLong()).money += toGive;

						channel.sendMessage(author.getAsMention() + " à offert " + toGive + " balles à " + user.getAsMention()).queue();
					}catch(NumberFormatException | IndexOutOfBoundsException e) {
						channel.sendMessage("C'est pas compliqué ! " + prefix + "give argent @Utilisateur ! T'in t'es une merde :poop: !").queue();
					}catch(NumberException e) {
						channel.sendMessage("C'est quoi ce nombre de merde là ?").queue();
					}catch(MoneyException e) {
						channel.sendMessage("Depuis quand on peut donner de l'argent qu'on a pas ? Hors de ma vue, connard.").queue();
					}
			}
		});
		bcommands.put("daily", new BotCommand("daily", "Récupéréz votre revenu universel quotidien.") {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				User author = message.getAuthor();
				MessageChannel channel = message.getChannel();
				if(message.isFromGuild())
					if(System.currentTimeMillis() - profiles.get(author.getIdLong()).lastdaily >= 24 * 3600000L) {
						profiles.get(author.getIdLong()).dailies++;
						profiles.get(author.getIdLong()).money += 10;
						profiles.get(author.getIdLong()).lastdaily = System.currentTimeMillis();
						channel.sendMessage("Vl'a tes 10 balles! Utilise les bien.").queue();
					}else {
						channel.sendMessage("T'essayerais pas d'membobiner par hasard ? Je te les ai déjà filé ! Aller, ouste ! "
								+ "Encore " + formatTime(24 * 3600000L - (System.currentTimeMillis() - profiles.get(author.getIdLong()).lastdaily))).queue();
					}
			}
		});
		bcommands.put("top", new BotCommand("top", "Observez les grands gagnants du capitalisme sur ce serveur.") {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				User author = message.getAuthor();
				MessageChannel channel = message.getChannel();
				if(message.isFromGuild()) {
					Vector<Profile> top = new Vector<Profile>();
					Profile higher = new Profile(author.getIdLong());
					for(int i = 0; i < profiles.size(); i++) {
						higher = new Profile(author.getIdLong());
						for(Member member : message.getGuild().getMembers()) {
							Profile pf = profiles.getNC(member.getUser().getIdLong());
							if(pf != null && member.getUser().getIdLong() != cyrielleID) {
								if(pf.money >= higher.money && !top.contains(pf)) {
									boolean containingAlready = false;
									for(Profile pf2 : top) {
										if(pf2 == pf || pf2.equals(pf)) {
											containingAlready = true;
										}
									}
									if(!containingAlready) {
										higher = pf;
									}
								}
							}
						}
						top.add(higher);
					}
					String str = "Classement des plus riches du serveur :\n";
					for(int i = 0, j = i+1;i < top.size(); i++) {
						j = i + 1;
						str = str.concat(j + (j == 1 ? "er/ère" : "ème") + " - " + message.getGuild().getMemberById(top.get(i).id).getEffectiveName() + " : " + top.get(i).money + "\n");
						if(j == 10) {
							break;
						}
					}
					for(int i = 0, j = i+1; i < top.size(); i++, j = i+1) {
						if(top.get(i).id == author.getIdLong()) {
							str = str.concat("Votre classement : " + j + (j == 1 ? "er/ère" : "ème"));
							break;
						}
					}
					channel.sendMessage(str).queue();
				}
			}
		});
		bcommands.put("profile", new BotCommand("profile", "Regardez votre profil ou celui des autres.") {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				Profile pro;
				try {
					pro = profiles.get(message.getMessage().getMentionedMembers().get(0).getUser().getIdLong());
				} catch(IndexOutOfBoundsException e) {
					pro = profiles.get(message.getAuthor().getIdLong());
				}
				message.getChannel().sendMessage(pro.toString(message.getGuild())).queue();
			}
		});
		bcommands.put("beer", new DrinkCommand("beer", "bière", ":beer:", ":beers:", 4, false, 0));
		bcommands.put("yop", new DrinkCommand("yop", "Yop au Jambon", "", "", 30, true, 1));
		bcommands.put("coffee", new DrinkCommand("coffee", "café", ":coffee:", ":coffee:", 3, true, 2));
		bcommands.put("tea", new DrinkCommand("tea", "thé", ":tea:", ":tea:", 2, true, 3));
		bcommands.put("wine", new DrinkCommand("wine", "vin", ":wine_glass:", ":champagne_glass:", 8, true, 4));
		bcommands.put("whisky", new DrinkCommand("whisky", "whisky", ":whisky:", ":whisky:", 5, true, 5));
		bcommands.put("sake", new DrinkCommand("sake", "sake", ":sake:", ":sake:", 3, true, 6));
		bcommands.put("water", new BotCommand("water", "") {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				MessageChannel channel = message.getChannel();
				if(message.isFromGuild())
					try {
						if(message.getMessage().getMentionedMembers().get(0).getUser().getId().equals(message.getAuthor().getId())) {
							channel.sendMessage("T'as pas besoin de te mentionner mais bon... "
									+ "Voila la fontaine : :potable_water: ").queue();
						}else if(message.getMessage().getMentionedMembers().get(0).getUser().getId().equals(jda.getSelfUser().getId())) {
							channel.sendMessage("J'vais t'le balancer à la gueule ton verre d'eau, j'te jure.").queue();
						}else {
							channel.sendMessage("MAIS TU VAS PAS OFFRIR DE L'EAU !!! T'ES CON OU QUOI ???").queue();
						}
					}catch(IndexOutOfBoundsException e) {
						channel.sendMessage("Sers toi : :potable_water: ").queue();
					}
			}
		});
		bcommands.put("help", new MessageCommand("help", "Cette page d'aide.", "Voici ainsi tout ce que je peux faire :\n") {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				response = "Liste des commandes :\n";
				for(Entry<String, BotCommand> bc : bcommands.entrySet()) {
					BotCommand com = bc.getValue();
					if(com.help.length() > 0) {
						if(com.isOkForThisServer(message.getGuild().getIdLong()) && com.isOkForThisUser(message.getAuthor().getIdLong())) {
							response = response.concat(prefix + com.name + " : " + com.help + "\n");
						}
					}
				}
				//response = response.concat(monoPrefix + "help : L'aide du Monopoly.");
				super.execute(bb, message);
			}
		});
		bcommands.put("purge", new BotCommand("purge", "Pour ceux qui peuvent supprimer les messages, je le fais pour eux.") {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				String msg = message.getMessage().getContentRaw();
				MessageChannel channel = message.getChannel();
				if(message.isFromGuild()) {
					if(message.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
						try {
							int toDelete = Integer.parseInt(msg.split(" ")[1]);

							List<Message> mh = channel.getHistoryBefore(message.getMessage(), toDelete).complete().getRetrievedHistory();
							Vector<AuditableRestAction<Void>> toExecute = new Vector<AuditableRestAction<Void>>();
							for(Message msgToDel : mh) {
								toExecute.addElement(msgToDel.delete());
							}


							message.getMessage().delete().queue();
							Thread execute = new Thread() {
								@Override
								public void run() {
									for(AuditableRestAction<Void> todo : toExecute) {
										todo.queue();
									}
								}

							};

							execute.start();

						} catch(NumberFormatException | IndexOutOfBoundsException e) {
							channel.sendMessage("Mettez un nombre svp.").queue();
							e.printStackTrace();
						}
					} else {
						channel.sendMessage("Permission denied.").queue();
					}
				}
			}
		});
		
		bcommands.put("save", new BotCommand("save", "Sauvegarde les données du bot, au cas où vous viendriez de faire la commande de votre vie.") {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				try {
					save();
					message.getChannel().sendMessage("Sauvegarde réussie !").queue();
				} catch (IOException e) {
					message.getChannel().sendMessage("Échec de la sauvegarde.").queue();
					e.printStackTrace();
				}
			}
		});
		bcommands.put("say", new BotCommand("say", "Je dis simplement ce que vous m'avez dit de dire, mais avec votre pseudo devant.") {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				String toSay = message.getMessage().getContentRaw().split(" ", 2)[1];
				message.getChannel().sendMessage(message.getAuthor().getName() + " " + toSay).queue();
				message.getMessage().delete().queue();
			}
		});
		bcommands.put("d", new BotCommand("d", "Je lance un dé pour vous.") {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				MessageChannel channel = message.getChannel();
				try {
					String toSay = message.getMessage().getContentRaw().split(" ", 2)[1];
					long nb = Long.parseLong(toSay);
					if(nb < 2) throw new NegativeNumberException();
					else if(nb > 10000000) throw new NullNumberException();
					channel.sendMessage("Resultat : " + ((Math.abs(random.nextLong() % nb)) + 1)).queue();
				}catch(NumberFormatException | ArrayIndexOutOfBoundsException e) {
					channel.sendMessage("Heu... il est bizarre ton dé, là. J'y touche pas moi.").queue();
				}catch(NegativeNumberException e) {
					channel.sendMessage("Félicitations, tu n'as __pas__ cassé le système.").queue();
				}catch(NullNumberException e) {
					channel.sendMessage("Wooooo doucement on se calme là ! "
							+ "C'est beaucoup trop gros là, j'ai pas de grue pour lancer ça.").queue();
				}
			}
		});
		bcommands.put("stop", new BotCommand("stop", "Me tue pas stp ;-;", vecyr, false) {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				message.getChannel().sendMessage("AAAAAH ! *meurt*").queue();
				try {
					save();
					messageLog.close();
					System.out.println("Extinction du bot...");
					jda.shutdown();
				} catch (IOException e) {
					e.printStackTrace();
					message.getChannel().sendMessage("ATTENTION ! Échec de la sauvegarde ;-;\nDu coup je m'éteins pas, on sait jamais.").queue();
				}
			}
		});
		bcommands.put("judge", new BotCommand("judge", "Donnez-moi quelque chose à juger, je le ferai avec plaisir…") {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				Message msg = message.getMessage();
				MessageChannel channel = message.getChannel();
				int note = 0;
				boolean special =  false;
				String toJudge = "";
				if(!msg.getMentions(MentionType.USER).isEmpty()) {
					note = (int) (msg.getMentionedUsers().get(0).getIdLong() % 101);
					toJudge = msg.getMentionedUsers().get(0).getAsMention();
				}else if(msg.getContentRaw().contains("@everyone")) {
					channel.sendMessage("Truc à juger : UNE MENTION EVERYONE! Jugement : 0 sur 100").queue();
					channel.sendMessage("STOP EVERYONE ! C'EST CHIANT PUTAIN ! :middle_finger: ").queue();
					special = true;
				}else if(msg.getContentRaw().contains("@here")) {
					channel.sendMessage("Truc à juger : UNE MENTION HERE ! Judgement : 1 sur 100").queue();
					channel.sendMessage("STOP ! C'EST CHIANT ! :middle_finger: ").queue();
					special = true;
				}else if(!msg.getMentions(MentionType.ROLE).isEmpty()) {
					note = (int) (msg.getMentionedRoles().get(0).getIdLong() % 101);
					toJudge = "Le rôle " + msg.getMentionedRoles().get(0).getName() + ".";
				}else if(!msg.getMentions(MentionType.CHANNEL).isEmpty()) {
					note = (int) (msg.getMentionedChannels().get(0).getIdLong() % 101);
					toJudge = "Le salon " + msg.getMentionedChannels().get(0).getAsMention() + ".";
				}else {
					toJudge = msg.getContentRaw().split(" ", 2)[1];
					note = Math.abs(toJudge.toLowerCase().hashCode() % 101);

				}

				if(!special) {

					channel.sendMessage("Truc à juger : " + toJudge + "\nC'est parti. Result : " + note + " sur 100.").queue();
					if(note == 0) {
						channel.sendMessage("Objectivement le pire truc dans l'univers.").queue();
					}else if(note < 20) {
						channel.sendMessage("Je deteste cette merde.").queue();
					}else if(note >= 20 && note < 40) {
						channel.sendMessage("Et j'suis cool, ça mérite moins.").queue();
					}else if(note >= 40 && note < 60) {
						channel.sendMessage("J'peux rien te dire sur ce truc. C'est juste... normal.").queue();
					}else if(note >= 60 && note < 80) {
						channel.sendMessage("J'laime bien, mais pas plus que ça.").queue();
					}else if(note >= 80 && note < 90) {
						channel.sendMessage("C'est sympa. Vraiment. Pas parfait, mais cool.").queue();
					}else if(note >= 90 && note < 95) {
						channel.sendMessage("Ce truc est putain de génial.").queue();
					}else if(note >= 95 && note < 100) {
						channel.sendMessage("Ce truc est presque parfait.").queue();
					}else if(note == 100) {
						channel.sendMessage("Perfection.").queue();
					}

				}
			}
		});
		
		LinkedHashMap<String, Vector<Shop.Item>> bolmitems = new LinkedHashMap<String, Vector<Shop.Item>>();
		Vector<Shop.Item> bolmaroles = new Vector<Shop.Item>();
		bolmaroles.add(new Shop.RoleItem(50000, 836953014094397452L, "Riche", "Montrez votre richesse !"));
		bolmaroles.add(new Shop.RoleItem(100000, 836953010500272129L, "Richissime", "Montrez votre richesse, mais encore plus !"));
		bolmaroles.add(new Shop.RoleItem(9999, 836971648199557160L, "tEm armorr", "tU va dèff tTS lé énmi (Vous rend invulnérable à toutes les insultes)"));
		bolmitems.put("Rôles", bolmaroles);
		
		bcommands.put("bolmashop", new Shop("bolmashop", "La boutique officielle du bar !", bolmitems, bolmaVec));
		
		bcommands.put("frigo", new MessageCommand("frigo", "Je fais la liste de ce que contient votre frigo !", "", bolmaVec, true) {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				if(Brainbot.profiles.get(message.getAuthor().getIdLong()).isFrigoVide())
					response = "Le frigo est vide ;-;";
				else {
					response = "Contenu de votre frigo :\n";
					response += Brainbot.profiles.get(message.getAuthor().getIdLong()).getFrigo();
				}
				Brainbot.profiles.get(message.getAuthor().getIdLong()).checkFrigoAchievements(message);
				super.execute(bb, message);
			}
		});
		
		bcommands.put("frigo_note", new MessageCommand("frigo_note", "Je note votre frigo.", "", bolmaVec, true) {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				if(Brainbot.profiles.get(message.getAuthor().getIdLong()).isFrigoVide())
					response = "Le frigo est vide ;-;";
				else {
					response = "Note de ton frigo : ";
					int note = Brainbot.profiles.get(message.getAuthor().getIdLong()).getFrigoScore();
					response += note + "/10 — ";
					if(note > 10) {
						response += "INCROYABLE ! Nan franchement je pourrais pas rêver meilleur frigo.";
					} else if(note == 10) {
						response += "Le frigo parfait.";
					} else if(note >= 8) {
						response += "Pas mal, pas mal.";
					} else if(note >= 6) {
						response += "Bien, mais peut mieux faire.";
					} else if(note >= 4) {
						response += "Peux mieux faire.";
					} else if(note >= 2) {
						response += "Pas ouf, franchement, hein…";
					} else if(note == 1) {
						response += "Qu'est-ce que c'est que cette merde.";
					} else if(note == 0) {
						response += "Ok je sens que je vais vomir referme moi ça.";
					} else {
						response += "WHAT THE FUCK qu'est-ce que ce PUTAIN de frigo !";
					}
				}
				Brainbot.profiles.get(message.getAuthor().getIdLong()).checkFrigoAchievements(message);
				super.execute(bb, message);
			}
		});
		
		LinkedHashMap<String, Vector<Shop.Item>> aliments = new LinkedHashMap<String, Vector<Shop.Item>>();
		Vector<Shop.Item> alimentsVec = new Vector<Shop.Item>();
		for(Aliment a : Aliment.aliments) {
			alimentsVec.add(a);
		}
		aliments.put("Aliments", alimentsVec);
		
		bcommands.put("foodshop", new Shop("foodshop", "Le magasin de nourriture du bar !", aliments, bolmaVec));
		
		bcommands.put("frigo_eat", new MessageCommand("frigo_eat", "Manger quelque chose de votre frigo ! Utilisation : frigo_del ID, ID étant l'identifiant de la position de l'aliment à supprimer.", "", bolmaVec, true) {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				if(Brainbot.profiles.get(message.getAuthor().getIdLong()).isFrigoVide())
					response = "Le frigo est vide ;-;";
				else
					try {
						int toDel = Integer.parseInt(message.getMessage().getContentRaw().split(" ")[1]);
						Profile pro = Brainbot.profiles.get(message.getAuthor().getIdLong());
						Aliment r = pro.removeAliment(toDel);
						if(r == null) {
							response = "Cet aliment n'existe pas.";
						} else {
							response = "Tu manges l'aliment « " + r.name + " ».";
						}
					} catch(NumberFormatException e) {
						response = "C'est pas un nombre entier, ça.";
					} catch(ArrayIndexOutOfBoundsException e) {
						response = "Tu peux pas tout manger d'un coup.";
					}
				Brainbot.profiles.get(message.getAuthor().getIdLong()).checkFrigoAchievements(message);
				super.execute(bb, message);
			}
		});
		
		bcommands.put("give_achievement", new BotCommand("give_achievement", "Donne un tout nouvel achievement à quelqu'un !", vecyr, false) {

			@Override
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				try {
					String[] args = message.getMessage().getContentRaw().split(" ", 5);
					Brainbot.profiles.get(Long.decode(args[1])).giveAchievement(new Achievement(args[2], args[4], true, Integer.parseInt(args[3])), message);
				} catch(ArrayIndexOutOfBoundsException | NumberFormatException e) {
					message.getChannel().sendMessage("Ordre des paramètres : ID_utilisateur nom récompense description.");
				}
				
			}
			
		});
		
		bcommands.put("achievements", new MessageCommand("achievements", "Liste certains succès (mais pas tous !).", "", bolmaVec, true) {
			@Override
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				response = "Liste non-exhaustive des succès :\n";
				for(AchievementFood af : AchievementFood.frigoAchievements) {
					response += (af.secret ? "*" : "") + af.name + (af.secret ? "*" : "") + " : ";
					response += (af.secret ? "Secret." : af.desc);
					response += ((af.reward > 0) ? " — Récompense : " + af.reward : "") + "\n";
				}
				super.execute(bb, message);
				
			}
		});
		
		bcommands.put("delach", new BotCommand("delach", "", vecyr, true) {

			@Override
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				try {
					String[] args = message.getMessage().getContentRaw().split(" ", 3);
					Brainbot.profiles.get(Long.decode(args[1])).removeAchievement(Integer.parseInt(args[2]));
				} catch(ArrayIndexOutOfBoundsException | NumberFormatException e) {
					message.getChannel().sendMessage("Ordre des paramètres : ID_utilisateur ID_ach");
				}
				
			}
			
		});
		
		bmcommands.put("admin_init", new BotCommand("admin_init", "Initialisation de la partie de monopoly pour ce serveur.", vecyr, false) {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				int set = Integer.parseInt(message.getMessage().getContentRaw().split(" ")[1]);
				initMonopolySets();
				if(initMonopoly(set, message.getGuild(), false)) {
					message.getChannel().sendMessage("Création de la partie de monopolie réussie !").queue();
				} else {
					message.getChannel().sendMessage("Il existe déjà une partie de monopoly sur ce serveur.").queue();
				}
			}
		});
		
		bmcommands.put("admin_delete", new BotCommand("admin_delete", "Supprime la partie de monopoly du serveur.", vecyr, false) {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				if(monopolyParties.containsKey(message.getGuild().getIdLong())) {
					monopolyParties.remove(message.getGuild().getIdLong());
					message.getChannel().sendMessage("Partie de monopoly supprimée du serveur.").queue();
				} else {
					message.getChannel().sendMessage("Il n'y a pas de partie de monopoly sur ce serveur.").queue();
				}
			}
		});
		
		bmcommands.put("admin_move", new BotCommand("admin_move", "Déplace un joueur sur la case désirée.", vecyr, false) {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				int sq = Integer.parseInt(message.getMessage().getContentRaw().split(" ")[1]);
				long id = Long.parseLong(message.getMessage().getContentRaw().split(" ")[2]);
				monopolyParties.get(message.getGuild().getIdLong()).toCase(profiles.get(id), message.getChannel(), sq);
				message.getChannel().sendMessage("Ce joueur a été déplacé case " + sq).queue();
			}
		});
		
		bmcommands.put("admin_reset_cd", new BotCommand("admin_reset_cd", "Réinitialise le cooldown d'un joueur.", vecyr, false) {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				long id = Long.parseLong(message.getMessage().getContentRaw().split(" ")[1]);
				profiles.get(id).lastPlays.put(message.getGuild().getIdLong(), 0L);
				message.getChannel().sendMessage("Le countdown de ce joueur a été annulé.").queue();
			}
		});
		
		bmcommands.put("admin_set_proprio", new BotCommand("admin_set_proprio", "Donne une propriété à un joueur.", vecyr, false) {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				int sq = Integer.parseInt(message.getMessage().getContentRaw().split(" ")[1]);
				long id = Long.parseLong(message.getMessage().getContentRaw().split(" ")[2]);
				try {
					monopolyParties.get(message.getGuild().getIdLong()).setProprio(profiles.get(id), sq);
					message.getChannel().sendMessage("Cette personne aquiert la propriété ").queue();
				} catch (ArrayIndexOutOfBoundsException e) {
					message.getChannel().sendMessage("Array index out of bounds.").queue();
				} catch (ClassCastException e) {
					message.getChannel().sendMessage("La case pointée n'est pas une propriété.").queue();
				}
			}
		});
		
		bmcommands.put("admin_plateau", new BotCommand("admin_plateau", "Affiche le plateau du jeu.", vecyr, false) {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				String plat = monopolyParties.get(message.getGuild().getIdLong()).toString();
				String[] spli = plat.split("\n");
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
		});
		
		bmcommands.put("admin_players", new BotCommand("admin_players", "Affiche le détail des joueurs.", vecyr, false) {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				String plat = monopolyParties.get(message.getGuild().getIdLong()).getPlayersDesc();
				String[] spli = plat.split("\n");
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
		});
		
		bmcommands.put("play", new BotCommand("play", "Lance le dé du Monopoly.") {
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				MessageChannel channel = message.getChannel();
				User author = message.getAuthor();
				if(profiles.get(author.getIdLong()).lastPlays == null) {
					profiles.get(author.getIdLong()).lastPlays = new LinkedHashMap<Long, Long>();
				}
				if(!profiles.get(author.getIdLong()).lastPlays.containsKey(message.getGuild().getIdLong())) {
					profiles.get(author.getIdLong()).lastPlays.put(message.getGuild().getIdLong(),profiles.get(author.getIdLong()).lastplay);
				}
				if(System.currentTimeMillis() - profiles.get(author.getIdLong()).lastPlays.get(message.getGuild().getIdLong()) >= 6 * 3600000L) {
					profiles.get(author.getIdLong()).lastPlays.replace(message.getGuild().getIdLong(),System.currentTimeMillis());
					boolean res = monopolyParties.get(message.getGuild().getIdLong()).lancer(profiles.get(author.getIdLong()), channel);
					if(!res)
						profiles.get(author.getIdLong()).lastPlays.replace(message.getGuild().getIdLong(),0L);
				} else {
					channel.sendMessage("Vous ne pourrez lancer le dé que dans " + formatTime(6 * 3600000L - (System.currentTimeMillis() - profiles.get(author.getIdLong()).lastPlays.get(message.getGuild().getIdLong())))).queue();
				}
			}
		});
		
		bmcommands.put("buy", new BotCommand("buy", "Achète la propriété sur laquelle se trouve un joueur, si cela est possible.") {
			@Override
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				monopolyParties.get(message.getGuild().getIdLong()).acheter(profiles.get(message.getAuthor().getIdLong()), message.getChannel());
			}
		});
		
		bmcommands.put("info", new BotCommand("info", "Affiche des informations sur la case actuelle.") {
			@Override
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				monopolyParties.get(message.getGuild().getIdLong()).printInfoCase(profiles.get(message.getAuthor().getIdLong()), message.getChannel());
			}
		});
		
		bmcommands.put("help", new MessageCommand("help", "Affiche cette page d'aide.", "Liste des commandes du Monopoly :\n") {
			@Override
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				for(Entry<String, BotCommand> bc : bmcommands.entrySet()) {
					BotCommand com = bc.getValue();
					if(com.help.length() > 0) {
						response = response.concat(monoPrefix + com.name + " : " + com.help + "\n");
					}
				}
				super.execute(bb, message);
			}
		});
		
		bmcommands.put("possessions", new BotCommand("possessions", "Affiche la liste de vos possessions.") {

			@Override
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				List<User> mention = message.getMessage().getMentionedUsers();
				Profile pro = (mention.size() == 0) ? profiles.get(message.getAuthor().getIdLong()) : profiles.get(mention.get(0).getIdLong());
				Vector<Possession> poss = monopolyParties.get(message.getGuild().getIdLong()).getProp(pro);
				String msgRet = "Liste des propriétés :\n";
				for(int i = 0; i < poss.size(); i++) {
					msgRet += "" + i + " : " + poss.get(i).getName() + "\n";
				}
				if(poss.size() == 0) {
					msgRet = "Vous ne possédez rien !";
				}
				message.getChannel().sendMessage(msgRet).queue();
			}
			
		});
		
		bmcommands.put("upgrade", new BotCommand("upgrade", "Permet d'améliorer une de vos propriétés.") {
			@Override
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				try {
					int id = Integer.parseInt(message.getMessage().getContentRaw().split(" ")[1]);
					((Propriete) monopolyParties.get(message.getGuild().getIdLong()).getProp(profiles.get(message.getAuthor().getIdLong())).get(id)).upgrade(profiles.get(message.getAuthor().getIdLong()), message.getChannel());
				} catch(Exception e) {
					message.getChannel().sendMessage("Utilisation incorrecte de la commande ou la possession indiquée n'est pas un terrain.").queue();
				}
			}
		});
		
		bmcommands.put("downgrade", new BotCommand("downgrade", "Permet de rétrograder l'une de vos propriétés contre de l'argent.") {
			@Override
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				try {
					int id = Integer.parseInt(message.getMessage().getContentRaw().split(" ")[1]);
					((Propriete) monopolyParties.get(message.getGuild().getIdLong()).getProp(profiles.get(message.getAuthor().getIdLong())).get(id)).downgrade(profiles.get(message.getAuthor().getIdLong()), message.getChannel());
				} catch(Exception e) {
					message.getChannel().sendMessage("Utilisation incorrecte de la commande ou la possession indiquée n'est pas un terrain.").queue();
				}
			}
		});
		
		bmcommands.put("infoprop", new BotCommand("infoprop", "Affiche des informations sur une propriété.") {
			@Override
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				try {
					int id = Integer.parseInt(message.getMessage().getContentRaw().split(" ")[1]);
					message.getChannel().sendMessage(monopolyParties.get(message.getGuild().getIdLong()).getProp(profiles.get(message.getAuthor().getIdLong())).get(id).toString()).queue();
				} catch(Exception e) {
					message.getChannel().sendMessage("Utilisation incorrecte de la commande.").queue();
				}
			}
		});
		
		bmcommands.put("give", new BotCommand("give", "Donne l'une de vos propriétés.") {

			@Override
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				try {
					List<User> mention = message.getMessage().getMentionedUsers();
					Profile pro = profiles.get(mention.get(0).getIdLong());
					int id = Integer.parseInt(message.getMessage().getContentRaw().split(" ")[1]);
					if(monopolyParties.get(message.getGuild().getIdLong()).isPlaying(pro)) {
						monopolyParties.get(message.getGuild().getIdLong()).getProp(profiles.get(message.getAuthor().getIdLong())).get(id).setProprio(pro);
						message.getChannel().sendMessage("La propriété \"" + monopolyParties.get(message.getGuild().getIdLong()).getProp(profiles.get(message.getAuthor().getIdLong())).get(id)
								+ "\" a bien été donnée à " + mention.get(0).getAsMention()).queue();
					} else {
						message.getChannel().sendMessage("La personne mentionnée ne joue pas encore au monopoly.").queue();
					}
					
				} catch (Exception e) {
					message.getChannel().sendMessage("Utilisation incorrecte de la commande.").queue();
				}
			}
			
		});
		
		bcommands.put("devtest", new BotCommand("devtest", "", vecyr, false) {
			
			@Override
			public void execute(Brainbot bb, MessageReceivedEvent message) {
				
			}
			
		});
		
		
	}

	@Override
	public void onEvent(GenericEvent event) {
		try {
			if(event instanceof ReadyEvent) {

				System.out.println("Bot prêt!");
				System.out.println("Nom : " + jda.getSelfUser().getName());
				jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
				jda.getPresence().setActivity(Activity.watching("et surveille. Préfixe \"" + prefix + "\""));
				for(Guild guild : jda.getGuilds()) {
					guild.loadMembers();
				}

			}else if(event instanceof MessageReceivedEvent) {

				try {
					messageReceived((MessageReceivedEvent) event);
				} catch (IOException e) {
					e.printStackTrace();
				}

			}else if(event instanceof GuildMemberJoinEvent) {

				GuildMemberJoinEvent gevent = (GuildMemberJoinEvent) event;
				if(gevent.getGuild().getIdLong() == bolmabarID) {
					gevent.getGuild().getTextChannelById(bolmabarComptoirID)
					.sendMessage("Hé ! Bienvenue dans l'bar "  + gevent.getUser().getAsMention() + " !").queue();
				}

			}else if(event instanceof GuildBanEvent) {

				GuildBanEvent gevent = (GuildBanEvent) event;
				if(gevent.getGuild().getIdLong() == bolmabarID) {
					gevent.getGuild().getTextChannelById(bolmabarComptoirID)
					.sendMessage("Hé " + gevent.getUser().getName()
							+ " ! Tu te **CASSES** de mon bar ! Et reviens plus ! *coup de pied au cul*").queue();
				}

			}else if(event instanceof GuildMemberRemoveEvent) {

				GuildMemberRemoveEvent gevent = (GuildMemberRemoveEvent) event;
				if(gevent.getGuild().getIdLong() == bolmabarID) {
					gevent.getGuild().getTextChannelById(bolmabarComptoirID)
					.sendMessage("Hé " + gevent.getUser().getName() + " ! Merde, l'est parti.").queue();
				}

			}else if(event instanceof DisconnectEvent) {

				System.out.println("Bot déconnecté.");

			}else if(event instanceof ReconnectedEvent || event instanceof ResumedEvent) {

				System.out.println("Bot reconnecté.");

			}
		}catch(Exception e){

			System.out.println("Réinitialisation de la JDA.");
			System.err.println("Exception non-attrapée");
			e.printStackTrace();
			jda.shutdown();
			try {
				jda = builder.build();
				jda.awaitReady();
			} catch(LoginException | InterruptedException e1) {
				System.err.println("Bon là t'es niquée, ça veut vraiment pas. J'me casse.");
				e1.printStackTrace();
				System.exit(1);
			}

		}
	}

	public void messageReceived(MessageReceivedEvent event) throws IOException {

		long servID = event.isFromGuild() ? event.getGuild().getIdLong() : 0L;
		User author = event.getAuthor();
		Message message = event.getMessage();
		String msg = message.getContentDisplay();
		String authorPseudo = author.getAsMention();
		MessageChannel channel = event.getChannel();


		messageLog.write("Message : " + message.getId() + " : " + msg);
		messageLog.newLine();
		messageLog.write("Time : " + message.getTimeCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
		messageLog.newLine();
		messageLog.write("Pseudo : " + author.getName() + " | Id : " + author.getId());
		messageLog.newLine();
		messageLog.write("Channel : " + message.getChannel().getName()
				+ " | ChannelId : " + message.getChannel().getId());
		messageLog.newLine();
		if(event.isFromGuild())
			messageLog.write("Guild : " + message.getGuild().getName() + " | GuildId : " + message.getGuild().getId());
		messageLog.newLine();
		messageLog.write("__________________________________________________________");
		messageLog.newLine();
		messageLog.flush();


		if(messagesCounter >= 10) {
			try {
				save();
			} catch(IOException e) {
				e.printStackTrace();
			}

			//if(!ipaddress.equals(getCurrentIp())) {
				//jda.getPrivateChannelById(cyriellePrivate).sendMessage("Changement d'IP : " + getCurrentIp()).queue();
				//ipaddress = getCurrentIp();
			//}

			messagesCounter = 0;
		}
		messagesCounter++;

		if(author.isBot() || author.isFake()) return;
		try {
			
			if(!serversNoReact.contains(message.getGuild().getIdLong())) {

				if(msg.contains("¯\\_(ツ)_/¯")) {
					message.addReaction("U+1F918").queue();
				}
				if(msg.contains("😂")) {
					message.addReaction("U+1F923").queue();
					message.addReaction("U+1F44C").queue();
					message.addReaction("U+1F4AF").queue();
				}

			}
			
			if(msg.startsWith("Patron, ouvre-moi le second comptoir !")
					&& message.getGuild().getIdLong() == bolmabarID) {

				ChannelManagerImpl cm =
						new ChannelManagerImpl(message.getGuild().getGuildChannelById(secondComptoirID));
				cm.setParent(message.getGuild().getCategoryById(sallePrincipaleID)).submit().get();
				cm.sync(message.getGuild().getCategoryById(sallePrincipaleID)).submit().get();
				message.getGuild().getTextChannelById(secondComptoirID).sendMessage("Et voilà "
						+ author.getAsMention() + ", vous me direz \"Merci patron, on a fini !\""
						+ " quand vous aurez fini !").complete(); 

			}
			if(msg.startsWith("Merci patron, on a fini !")
					&& channel.getIdLong() == secondComptoirID) {

				message.getGuild().getTextChannelById(secondComptoirID).sendMessage("Niquel "
						+ author.getAsMention() + ", je le referme !").queue();
				ChannelManagerImpl cm =
						new ChannelManagerImpl(message.getGuild().getGuildChannelById(secondComptoirID));
				cm.setParent(message.getGuild().getCategoryById(archivesBarID)).submit().get();
				cm.sync(message.getGuild().getCategoryById(archivesBarID)).submit().get();

			}
			
			if(message.getMentionedUsers().contains(jda.getUserById(356861332106117120L))
					&& message.getGuild().getIdLong() == bolmabarID) {
				message.addReaction(message.getGuild().getEmoteById(623901460752629760L)).queue();
			}
			
			if(msg.contains("(╯°□°）╯︵ ┻━┻")) {

				if(!msg.contains("┬─┬ ノ( ゜-゜ノ)")) {
					if(author.getIdLong() == 363003065575407616L) {
						channel.sendMessage("Bon, normalement je devrais être en colère mais… BON RETOUR PARMI NOUS PUTAIN !").queue();
					}else if(author.getIdLong() == cyrielleID) {
						channel.sendMessage("N'oublie pas de bien la remettre après stp!").queue();
					}else {
						channel.sendMessage("┬─┬ ノ( ゜-゜ノ) \nMet pas le bordel!").queue();
					}
				}else {
					channel.sendMessage("Merci de m'aider à ranger.").queue();
				}

			}
			
			String[] split = msg.split(" ");
			if(split.length == 0) {
				return;
			}
			String[] prefixUsed = split[0].split("!");
			if(prefixUsed.length != 2) {
				return;
			}
			try {
				if(prefix.equals(prefixUsed[0] + "!")) {
					BotCommand com = bcommands.get(prefixUsed[1]);
					if(com.isOkForThisServer(message.getGuild().getIdLong()) && com.isOkForThisUser(message.getAuthor().getIdLong()))
						com.execute(this, event);
				}else if(monoPrefix.equals(prefixUsed[0] + "!")) {
					BotCommand com = bmcommands.get(prefixUsed[1]);
					if(com.isOkForThisServer(message.getGuild().getIdLong()) && com.isOkForThisUser(message.getAuthor().getIdLong()))
						com.execute(this, event);
				}
			} catch(NullPointerException e) {
				channel.sendMessage("Commande inconnue.").queue();
			}
		}catch(Exception e) {
			e.printStackTrace();
			channel.sendMessage("Déso " + authorPseudo + ", mais j'ai eu une erreur. Voilà pour Cyrielle : "
					+ e.getClass().getName() + ". Désolé.").queue();

		}
	}


}

