package tk.cyriellecentori.brainbot.profiles;

import java.util.LinkedHashMap;
import java.util.Vector;
import java.util.Map.Entry;

import net.dv8tion.jda.api.entities.Guild;
import tk.cyriellecentori.brainbot.Brainbot;

public class Profile{
	public long money = 0;
	public long lastdaily = 0;
	public long dailies = 0;
	public long lasttv = 0;
	public long lastwork = 0;
	public long worked = 0;
	public long lastplay = 0;
	public long played = 0;
	public LinkedHashMap<Long,Long> lastPlays = new LinkedHashMap<Long,Long>();
	public long id;
	public LinkedHashMap<Long, Vector<Integer>> worksCollec = new LinkedHashMap<Long, Vector<Integer>>();
	public int[] drinksBus = {0,0,0,0,0,0,0};
	public int[] drinksOfferts = {0,0,0,0,0,0,0};

	public Profile(long id){
		this.id = id;
	}

	public void check() {
		if(drinksBus == null) {
			drinksBus = new int[] {0,0,0,0,0,0,0};
		}
		if(drinksOfferts == null) {
			drinksOfferts = new int[] {0,0,0,0,0,0,0};
		}
		if(worksCollec == null) {
			worksCollec = new LinkedHashMap<Long, Vector<Integer>>();
		}
		if(lastPlays == null) {
			lastPlays = new LinkedHashMap<Long,Long>();
		}
	}

	public int getSumWorks() {
		int res = 0;
		for(Entry<Long, Vector<Integer>> vec : worksCollec.entrySet()) {
			res+= vec.getValue().size();
		}
		return res;
	}

	public String toString(Guild guild) {
		return "Profil de " + Brainbot.jda.getUserById(id).getName() + " (" + guild.getMemberById(id).getEffectiveName() + ")\n"
				+ ((id != Brainbot.cyrielleID) ? ("Argent : " + money + "\n") : ("Argent : Infini\n"))
				+ "Nombre d'utilisations du — b!daily : " + dailies + " — b!work : " + worked + " — bm!play : " + played + "\n"
				+ "Boissons bues — Bière : " + drinksBus[0] + " — Yops : " + drinksBus[1] + " — Cafés : " + drinksBus[2] + " — Thé : " + drinksBus[3] + " — Vin : " + drinksBus[4] + " — Whisky : " + drinksBus[5] + " — Sake : " + drinksBus[6] + "\n"
				+ "Boissons offertes — Bière : " + drinksOfferts[0] + " — Yops : " + drinksOfferts[1] + " — Cafés : " + drinksOfferts[2] + " — Thé : " + drinksOfferts[3] + " — Vin : " + drinksOfferts[4] + " — Whisky : " + drinksOfferts[5] + " — Sake : " + drinksOfferts[6] + "\n"
				+ "Travaux différents effectués : " + getSumWorks();
	}
}