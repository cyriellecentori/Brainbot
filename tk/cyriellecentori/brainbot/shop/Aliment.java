package tk.cyriellecentori.brainbot.shop;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import tk.cyriellecentori.brainbot.Brainbot;
import tk.cyriellecentori.brainbot.profiles.Profile;
import tk.cyriellecentori.brainbot.shop.Shop.Item;

public class Aliment extends Item {
	public final int score;
	public final int place;
	
	public Aliment(String nom, int score, int place, int cost, String description) {
		super(cost, nom, description + " — Place : " + place);
		this.score = score;
		this.place = place;
	}

	@Override
	public boolean execute(Brainbot bb, Profile buyer, TextChannel chan) {
		boolean ret = buyer.addAliment(this);
		buyer.checkFrigoAchievements(chan);
		return ret;
	}
	
	public static Aliment searchAliment(String name) {
		Aliment search = null;
		for(Aliment a : aliments) {
			if(a.name.equals(name)) {
				search = a;
				break;
			}
		}
		if(search == null)
			System.out.println("Aliment non trouvé : " + name);
		return search;
	}
	
	public static Aliment[] aliments = {
		new Aliment("Beurre salé", 13, 1, 350, "Le légendaire beurre salé."),
		new Aliment("Beurre doux", 0, 1, 350, "Le terrible beurre doux."),
		new Aliment("Salade", 6, 2, 150, "Une salade fraichement récoltée du jardin de Mamie Molette"),
		new Aliment("Sirop d'Orgeat", 9, 2, 200, "Un succulent sirop d'orgeat, assez pour devenir addict."),
		new Aliment("Chorizo", 11, 1, 150, "À prononcer avec l'accent."),
		new Aliment("Pastèque", 7, 5, 150, "Une pastèque bien rouge et bien juteuse."),
		new Aliment("Botte de carrottes", 3, 3, 100, "Un petit groupe de carottes un peu trop prétentieux."),
		new Aliment("Botte de radis", 3, 2, 100, "Un petit groupe de radis, on dirait qu'ils se sont perdus."),
		new Aliment("Thé glacé", 6, 2, 200, "Une bouteille de thé glacé, on dit que cela attire les roux."),
		new Aliment("Coca Bolma", 4, 2, 350, "Une bouteille d'une boisson étrange, vous ne savez pas ce qu'il y a dedans, à part du sucre."),
		new Aliment("Eau", 6, 2, 20, "Une simple bouteille d'eau, parce que l'eau du robinet est bien trop incertaine selon vous."),
		new Aliment("Boblenna", 7, 2, 250, "Une boisson gazeuse goût orange inventée pour gagner de l'argent sur le dos des nazis."),
		new Aliment("Gambetta", 8, 2, 250, "Une boisson de provence. Vous entendez le bruit des cigales… et de la musique expérimentale."),
		new Aliment("Diabolo fraise", 11, 2, 150, "Un diabolo fraise qui vous donne l'impression d'avoir 5 ans en le buvant."),
		new Aliment("Chocolat blanc", 2, 1, 200, "Une étrange mixture qui n'est certainement pas du chocolat."),
		new Aliment("Chocolat au lait", 8, 1, 200, "Fraichement sorti des vaches marron."),
		new Aliment("Chocolat noir", 10, 1, 200, "Le seul, l'unique, l'inimitable."),
		new Aliment("Croissant aux amandes", 13, 1, 300, "On dit que cette viennoiserie attire les profs de mathématiques aussi bien que leurs élèves."),
		new Aliment("Pâtisserie du Saint-Laurent", 11, 1, 400, "Tout droit téléportée de la meilleure patisserie de Clermont-Ferrand."),
		new Aliment("Melon", 4, 3, 200, "Une pastèque, mais plus petite et orange."),
		new Aliment("Pizza au chorizo", 10, 2, 350, "Une bonne pizza des familles fumée au feu de bois."),
		new Aliment("Pizza à l'ananas", 1, 2, 350, "Une bonne pizza de- Attendez, quoi ?"),
		new Aliment("Crème Chantilly", 11, 2, 350, "Une merveilleuse crème pouvant accompagner glaces, desserts, et famille de Montmorency."),
		new Aliment("Tomates", 2, 2, 150, "On ne sait toujours pas si c'est des fruits ou des légumes."),
		new Aliment("Moutarde", 6, 1, 250, "Une sauce brûlant le palais telle les bourguignons brûlant Jeanne d'Arc."),
		new Aliment("Mayonnaise", 6, 1, 250, "Un mélange suspect de plusieurs aliments qui ne vont pas ensemble."),
		new Aliment("Sauce Barbecue", 10, 1, 250, "Du ketchup, mais en bon."),
		new Aliment("Ketchup", 5, 1, 200, "Du ketchup, mais en ketchup."),
		new Aliment("Roblochon", 11, 2, 300, "Ou Robloch, pour les intimes. A un caratère aussi fort que son odeur."),
		new Aliment("Saint-Nectaire", 13, 2, 350, "Un fromage Auvergnat qui mérite bien sa canonisation."),
		new Aliment("Emmental", 10, 2, 200, "Il reste quelques poils de souris dedans."),
		new Aliment("Fromage de chèvre", 3, 1, 300, "Une idée farfelue dont les corses sont particulièrement friands."),
		new Aliment("Lardons", 8, 1, 200, "Le porc passe-partout."),
		new Aliment("Poulet", 9, 2, 350, "Il reste quelques plumes."),
		new Aliment("Bœuf", 10, 2, 450, "À découper soi-même."),
		new Aliment("Côtes de Porc", 7, 2, 400, "Une source hilarante de blagues originales."),
		new Aliment("Sauce Carbonara", 12, 1, 250, "Une meute d'italiens la surveille."),
		new Aliment("Truffade", 13, 3, 450, "Deuxième plat le plus mortel de France."),
		new Aliment("Aligot", 12, 3, 400, "Une purée au fromage de la densité calorifique d'une étoile à neutrons."),
		new Aliment("Raclette", 11, 2, 350, "J'espère que vous avez apporté la machine."),
		new Aliment("Tartiflette", 12, 3, 400, "La meilleure amie du Robloch."),
		new Aliment("Concombre", 6, 2, 200, "Le meilleur ami de… oubliez."),
		new Aliment("Navet", 13, 2, 500, "Ne spéculez pas sur son prix et laissez-le jouer à Smash Bros. Oui, même s'il main Bowser."),
		new Aliment("Vinaigrette", 3, 1, 200, "Un liquide étrange souvent utilisé par les profs de chimie pour expliquer le terme « non-miscible »."),
		new Aliment("Œufs", 9, 1, 200, "Faut pas manger ce qui sort du cul des poules c'est caca."),
		new Aliment("Ananas", 1, 4, 150, "Qui a eu l'idée d'en mettre sur les pizzas ?"),
		new Aliment("Curry", 13, 1, 450, "Meilleur que la poudre blanche."),
		new Aliment("Herbes de provence", 7, 2, 200, "A le même effet que l'herbe à chat sur certaines compositrices."),
		new Aliment("Taupinambour", 3, 1, 250, "Une tubercule au nom fort amusant."),
		new Aliment("Pomme de terre", 10, 3, 150, "Rien à voir avec les pommes. Aime tuer des irlandais."),
		new Aliment("Pâtes", 8, 2, 50, "Quand vous n'avez plus d'idées."),
		new Aliment("Riz", 8, 1, 50, "Quand vous n'avez plus d'idées mais que vous voulez changer un peu."),
		new Aliment("Poivron", 1, 1, 150, "Parasite les pizzas."),
		new Aliment("Kouign-Amann", 13, 3, 450, "Dessert le plus dangereux au monde."),
		new Aliment("Far breton", 13, 3, 350, "Comme un flan, mais pas un flan."),
		new Aliment("BN", 9, 1, 250, "Tout droit sorti d'une Biscuiterie Nantaise."),
		new Aliment("LU", 7, 1, 250, "Un biscuit ma foi fort utile."),
		new Aliment("Saucisson", 10, 1, 350, "Une merveille du monde."),
		new Aliment("Saucisson au Saint-Nectaire", 13, 1, 450, "Deux merveilles du monde mises ensemble."),
		new Aliment("Cornichons", 3, 1, 150, "Une source de blagues poétiques."),
		new Aliment("Pommes", 8, 3, 200, "Un fruit qui réveille les physiciens de leur sieste."),
		new Aliment("Poires", 7, 3, 200, "Des pommes bizarres."),
		new Aliment("Abricots", 6, 2, 200, "Le meilleur ami du concombre."),
		new Aliment("Cerises", 6, 1, 150, "Employées à Groupama."),
		new Aliment("Olives", 2, 1, 150, "Pousse dans les pubs Carglass."),
		new Aliment("Brocoli", 8, 2, 250, "On dirait une forêt miniature !"),
		new Aliment("Haricots", 8, 2, 200, "Sans liaison."),
		new Aliment("Petits-pois", 6, 2, 150, "Réveille les plus narcoleptiques des princesses."),
		new Aliment("Baguette Tradition", 11, 2, 100, "La seule véritable baguette."),
		new Aliment("Baguette moulée", 2, 2, 90, "Une horreur indescriptible."),
		new Aliment("Citron", 5, 1, 150, "Vous permet de devenir invisible."),
		new Aliment("Orange", 6, 2, 150, "Qui du fruit ou de la couleur ?"),
		new Aliment("Dessert à la pistache", 11, 1, 250, "Dans des petits pots en verre, parce que c'est plus chiant à jeter."),
		new Aliment("Yaourt", 7, 1, 100, "Remerciez les centaines de bactéries l'ayant durement construit."),
		new Aliment("Tarte à la mirabelle", 6, 1, 250, "Existe aussi en version tartelette pour les enfants !"),
		new Aliment("Épinards", 1, 2, 100, "Power up !"),
		new Aliment("Fraises", 10, 1, 250, "Ne les cueillez pas trop vite, vous pourriez les faire fuir."),
		new Aliment("Clémentine", 8, 1, 100, "L'esprit de Noël quand on a pas d'argent."),
		new Aliment("Magret de canard", 12, 2, 350, "L'esprit de Noël quand on a assez d'argent."),
		new Aliment("Fois gras", 13, 2, 600, "L'esprit de Noël quand on a trop d'argent."),
		new Aliment("Huitres", 1, 3, 550, "L'esprit de… Non."),
		new Aliment("Crosets", 9, 2, 150, "En bons italiens, les savoyards aussi ont leur genre de pâtes."),
		new Aliment("Glace à la vanille", 9, 2, 100, "Spécialité des vendeurs de meubles suédois."),
		new Aliment("Oignons", 7, 1, 150, "Fait plus chialer que la fin d'Assassination Classroom."),
		new Aliment("Ail", 6, 1, 200, "Ça fait mal."),
		new Aliment("Brandade de Morue", 5, 2, 250, "Le sel marin n'est pas en option."),
		new Aliment("Lasagnes", 8, 3, 300, "Attention au chat orange."),
		new Aliment("Crème anglaise", 12, 1, 350, "Beaucoup trop bon pour que ça vienne d'eux."),
		new Aliment("Nouilles", 8, 1, 150, "Nommés d'après une célèbre agente."),
		new Aliment("Champigons", 7, 1, 250, "Protégés par Mario."),
		new Aliment("Saumon", 12, 2, 450, "Ambassadeur de Norvège."),
		new Aliment("Truite", 11, 2, 350, "Élevées avec amour par un prof de physique."),
		new Aliment("Thon", 9, 2, 250, "Meilleur ami de la brandade de morue."),
		new Aliment("Cabillaud", 7, 2, 200, "On recherche toujours son goût."),
		new Aliment("Mangue", 5, 2, 250, "Peut se découper selon un repère cartésien orthonormé."),
		new Aliment("Bananes", 8, 2, 200, "Les singes savent mieux les ouvrir que vous."),
		new Aliment("Raisin", 7, 2, 250, "Vous donne l'air d'un empeureur romain."),
		new Aliment("Fruits de mer", 4, 3, 400, "Ramassés lors de vos pêches à pied les plus endiablées avec vos grands-parents."),
		new Aliment("Noisettes", 7, 1, 150, "Perdues par un écureuil lors de la dernière grande glaciation."),
		new Aliment("Noix", 5, 1, 200, "Brain in a nutshell."),
		new Aliment("Marrons", 5, 1, 200, "Qui de la couleur ou du fru… leg… heu… truc ?"),
		new Aliment("Pruneaux", 4, 2, 250, "Pourrit le far breton depuis la fin du XIXe siècle."),
		new Aliment("Paté", 7, 2, 250, "Mieux vaut ne pas savoir de quoi c'est fait."),
		new Aliment("Miel", 9, 2, 350, "Ce qui sort du cul des abeilles."),
		new Aliment("Pomme d'or", 8, 3, 1500, "Le seul moyen de manger de l'or sans être malade."),
		new Aliment("Pomme de Notch", 12, 5, 15000, "Remplace des haltères."),
		new Aliment("Maïs", 7, 1, 150, "Pratique pour faire des labyrinthes."),
		new Aliment("Cannelle", 13, 1, 350, "L'un des meilleurs goûts de cette terre et des autres."),
		new Aliment("Tarte Caramel-Cannelle", 12, 3, 450, "Cuisinée avec amour par une maman chèvre."),
		new Aliment("Glamburger", 8, 2, 500, "Un hamburger avec un petit drapeau à l'éffigie d'un robot androgyne."),
		new Aliment("Tem flak", 0, 0, 2, "tEm fLAk goOd fur ur helth"),
		new Aliment("Aubergine", 3, 2, 150, "🍆.")
	};
}
