package tk.cyriellecentori.brainbot.profiles;

import java.util.LinkedHashMap;
import java.util.Vector;
import java.util.Map.Entry;

import net.dv8tion.jda.api.entities.Guild;
import tk.cyriellecentori.brainbot.Brainbot;
import tk.cyriellecentori.brainbot.shop.Aliment;

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
	public Vector<Achievement> achievements = new Vector<Achievement>();
	private Vector<Aliment> frigo = new Vector<Aliment>();

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
		if(frigo == null) {
			frigo = new Vector<Aliment>();
		}
		if(achievements == null) {
			achievements = new Vector<Achievement>();
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
		String successList = "";
		for(Achievement a : achievements) {
			successList += a.toString() + "\n";
		}
		return "Profil de " + Brainbot.jda.getUserById(id).getName() + " (" + guild.getMemberById(id).getEffectiveName() + ")\n"
				+ ((id != Brainbot.cyrielleID) ? ("Argent : " + money + "\n") : ("Argent : Infini\n"))
				+ "Nombre d'utilisations du — b!daily : " + dailies + " — b!work : " + worked + " — bm!play : " + played + "\n"
				+ "Boissons bues — Bière : " + drinksBus[0] + " — Yops : " + drinksBus[1] + " — Cafés : " + drinksBus[2] + " — Thé : " + drinksBus[3] + " — Vin : " + drinksBus[4] + " — Whisky : " + drinksBus[5] + " — Sake : " + drinksBus[6] + "\n"
				+ "Boissons offertes — Bière : " + drinksOfferts[0] + " — Yops : " + drinksOfferts[1] + " — Cafés : " + drinksOfferts[2] + " — Thé : " + drinksOfferts[3] + " — Vin : " + drinksOfferts[4] + " — Whisky : " + drinksOfferts[5] + " — Sake : " + drinksOfferts[6] + "\n"
				+ "Travaux différents effectués : " + getSumWorks() + "\n"
				+ ((achievements.size() == 0) ? "" : ("Succès : \n"
				+ successList));
	}
	
	public boolean addAliment(Aliment aliment) {
		int placeDispo = 100;
		for(Aliment a : frigo) {
			placeDispo -= a.place;
		}
		if(placeDispo < aliment.place) {
			return false;
		} else
			frigo.add(aliment);
		return true;
	}
	
	public Aliment removeAliment(int index) {
		if(index >= frigo.size())
			return null;
		Aliment a = frigo.elementAt(index);
		frigo.remove(index);
		return a;
	}
	
	public int getFrigoScore() {
		int tot = (frigo.size() < 10) ? 10 : frigo.size();
		int note = 0;
		LinkedHashMap<Aliment, Integer> dejaVu = new LinkedHashMap<Aliment, Integer>();
		for(Aliment a : frigo) {
			double modifier = 1;
			if(dejaVu.containsKey(a)) {
				modifier = Math.exp(-dejaVu.get(a) / 3d);
				dejaVu.put(a, dejaVu.get(a) + 1);
			} else {
				dejaVu.put(a, 1);
			}
			note += modifier * a.score;
		}
		if(frigo.size() < 10)
			note += (10 - frigo.size()) * 2;
		return Math.round(note / tot);
	}
	
	public String getFrigo() {
		String list = "";
		for(int i = 0; i < frigo.size(); i++) {
			list += i + " — " + frigo.elementAt(i).name + " — Place : " + frigo.elementAt(i).place + "\n";
		}
		return list;
	}
	
	public boolean isFrigoVide() {
		return frigo.size() == 0;
	}
}