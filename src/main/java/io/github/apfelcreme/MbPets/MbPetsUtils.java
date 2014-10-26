package io.github.apfelcreme.MbPets;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import me.libraryaddict.disguise.disguisetypes.AnimalColor;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Ocelot;
import org.bukkit.entity.Player;

public class MbPetsUtils {

	/**
	 * all allowed functions. e.g /pet delete
	 * 
	 * @author Jan
	 * 
	 */
	public enum Functions {
		BABY, CALL, CANCEL, COLOR, CONFIRM, CONVERT, DELETE, FLUSH, HELP, INFO, LIST, MODIFY, MONITOR, NAME, NUMBER, STATUS, STYLE, TYPE, UNCALL, RELOAD, REGENERATE;

		public static boolean contains(String o) {
			for (Functions f : values()) {
				if (f.name().equalsIgnoreCase(o)) {
					return true;
				}
			}
			return false;
		}
	}

	/**
	 * all allowed keys for a command.
	 * 
	 * @author Jan
	 * 
	 */
	public enum Keys {
		BABY, COLOR, COMMAND, NAME, NUMBER, PLAYER, STYLE, TYPE;

		public static Keys getKey(String s) {
			for (Keys e : values()) {
				if (e.name().equalsIgnoreCase(s)) {
					return e;
				}
			}
			return null;
		}
	}

	/**
	 * adds all AnimalColors to the config.yml
	 */
	private static void createAnimalColorNodes() {
		MbPets.getInstance()
				.getConfig()
				.set("animalColors.BLACK",
						Arrays.asList("BLACK", "SCHWARZ", "DUNKEL"));
		MbPets.getInstance().getConfig()
				.set("animalColors.BLUE", Arrays.asList("BLUE", "BLAU"));
		MbPets.getInstance().getConfig()
				.set("animalColors.BROWN", Arrays.asList("BROWN", "BRAUN"));
		MbPets.getInstance().getConfig()
				.set("animalColors.CYAN", Arrays.asList("CYAN", "TÜRKIS"));
		MbPets.getInstance().getConfig()
				.set("animalColors.GRAY", Arrays.asList("GRAY", "GRAU"));
		MbPets.getInstance()
				.getConfig()
				.set("animalColors.GREEN",
						Arrays.asList("GREEN", "GRUEN", "GRÜN", "GRAS"));
		MbPets.getInstance()
				.getConfig()
				.set("animalColors.LIGHT_BLUE",
						Arrays.asList("LIGHT_BLUE", "LIGHTBLUE", "HELLBLAU",
								"HIMMELBLAU", "HIMMEL"));
		MbPets.getInstance().getConfig()
				.set("animalColors.LIME", Arrays.asList("LIME", "LIMETTE"));
		MbPets.getInstance().getConfig()
				.set("animalColors.MAGENTA", Arrays.asList("MAGENTA"));
		MbPets.getInstance().getConfig()
				.set("animalColors.ORANGE", Arrays.asList("ORANGE"));
		MbPets.getInstance().getConfig()
				.set("animalColors.PINK", Arrays.asList("PINK", "ROSA"));
		MbPets.getInstance()
				.getConfig()
				.set("animalColors.PURPLE",
						Arrays.asList("PURPLE", "VIOLETT", "LILA"));
		MbPets.getInstance().getConfig()
				.set("animalColors.RED", Arrays.asList("RED", "ROT"));
		MbPets.getInstance().getConfig()
				.set("animalColors.SILVER", Arrays.asList("SILVER", "SILBER"));
		MbPets.getInstance()
				.getConfig()
				.set("animalColors.WHITE",
						Arrays.asList("WHITE", "WEIß", "WEISS"));
		MbPets.getInstance().getConfig()
				.set("animalColors.YELLOW", Arrays.asList("YELLOW", "GELB"));
	}

	/**
	 * adds predefined prices to the config.yml
	 */
	private static void createDisguiseTypesNodes() {
		MbPets.getInstance().getConfig()
				.set("disguiseTypes.COW", Arrays.asList("KUH", "COW"));
		MbPets.getInstance().getConfig()
				.set("disguiseTypes.HORSE", Arrays.asList("HORSE", "PFERD"));
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.UNDEAD_HORSE",
						Arrays.asList("ZOMBIEPFERD", "ZOMBIEHORSE",
								"UNDEAD_HORSE"));
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.SKELETON_HORSE",
						Arrays.asList("SKELETTPFERD", "SKELETTHORSE",
								"SEKELTON_HORSE"));
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.WOLF", Arrays.asList("WOLF", "DOG", "HUND"));
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.OCELOT",
						Arrays.asList("OCELOT", "CAT", "KATZE"));
		MbPets.getInstance().getConfig()
				.set("disguiseTypes.PIG", Arrays.asList("PIG", "SCHWEIN"));
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.IRON_GOLEM",
						Arrays.asList("IRON_GOLEM", "EISENGOLEM", "IRONGOLEM",
								"GOLEM"));
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.CHICKEN",
						Arrays.asList("CHICKEN", "CHICK", "HUHN", "KÜKEN",
								"HÜHNCHEN"));
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.MUSHROOM_COW",
						Arrays.asList("MUSHROOMCOW", "MUSHROOM_COW",
								"MOOSHROOM", "PILZKUH"));
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.SHEEP",
						Arrays.asList("SHEEP", "SCHAF", "PULLOVERSCHWEIN"));
		// MbPets.getInstance()
		// .getConfig()
		// .set("disguiseTypes.DROPPED_ITEM",
		// Arrays.asList("FEUER", "FIRE", "TNT", "SPRENGSTOFF",
		// "DIA", "DIAMANT", "DIAMOND"));
	}

	/**
	 * adds predefined Dropped Items to the config.yml
	 */
	public static void createDroppedItemNodes() {
		MbPets.getInstance()
				.getConfig()
				.set("droppedItems.DIAMOND",
						Arrays.asList("DIAMOND", "DIAMANT", "DIA"));
		MbPets.getInstance().getConfig()
				.set("droppedItems.TNT", Arrays.asList("TNT", "SPRENGSTOFF"));
		MbPets.getInstance().getConfig()
				.set("droppedItems.FIRE", Arrays.asList("FIRE", "FEUER"));

	}

	/**
	 * adds all HorseColors to the config.yml
	 * 
	 */
	public static void createHorseColorNodes() {
		MbPets.getInstance()
				.getConfig()
				.set("horseColors.BLACK",
						Arrays.asList("BLACK", "SCHWARZ", "DUNKEL"));
		MbPets.getInstance().getConfig()
				.set("horseColors.BROWN", Arrays.asList("BROWN", "BRAUN"));
		MbPets.getInstance()
				.getConfig()
				.set("horseColors.CHESTNUT",
						Arrays.asList("CHESTNUT", "KASTANIE", "NUSS"));
		MbPets.getInstance()
				.getConfig()
				.set("horseColors.CREAMY",
						Arrays.asList("CREAMY", "CREMIG", "KREMIG"));
		MbPets.getInstance()
				.getConfig()
				.set("horseColors.DARK_BROWN",
						Arrays.asList("DARK_BROWN", "DARKBROWN", "DUNKELBRAUN"));
		MbPets.getInstance().getConfig()
				.set("horseColors.GRAY", Arrays.asList("GRAY", "GRAU"));
		MbPets.getInstance()
				.getConfig()
				.set("horseColors.WHITE",
						Arrays.asList("WHITE", "WEIß", "WEISS"));
	}

	/**
	 * adds all HorseStyles to the config.yml
	 */
	public static void createHorseStyleNodes() {
		MbPets.getInstance()
				.getConfig()
				.set("horseStyles.BLACK_DOTS",
						Arrays.asList("BLACK_DOTS", "BLACK", "SCHWARZ",
								"SCHWARZFLECKIG", "BLACKDOTTED"));
		MbPets.getInstance()
				.getConfig()
				.set("horseStyles.NONE",
						Arrays.asList("NONE", "KEINS", "KEINE", "KEIN"));
		MbPets.getInstance()
				.getConfig()
				.set("horseStyles.WHITE",
						Arrays.asList("WHITE", "WEIß", "WEISS"));
		MbPets.getInstance()
				.getConfig()
				.set("horseStyles.WHITE_DOTS",
						Arrays.asList("WHITE_DOTS", "WEIßFLECKIG",
								"WEISSFLECKIG", "WHITEDOTTED"));
		MbPets.getInstance()
				.getConfig()
				.set("horseStyles.WHITEFIELD",
						Arrays.asList("WHITEFIELD", "WEIßFELD", "WEISSFELD",
								"MILCHIG", "VERSCHWOMMEN"));
	}

	/**
	 * adds all OcelotStyles to the config.yml
	 */
	public static void createOcelotStyleNodes() {
		MbPets.getInstance()
				.getConfig()
				.set("ocelotStyles.WILD_OCELOT",
						Arrays.asList("WILD_OCELOT", "WILDOCELOT", "WILD"));
		MbPets.getInstance()
				.getConfig()
				.set("ocelotStyles.BLACK_CAT",
						Arrays.asList("BLACK_CAT", "BLACKCAT", "BLACK",
								"SCHWARZ", "DUNKEL"));
		MbPets.getInstance()
				.getConfig()
				.set("ocelotStyles.RED_CAT",
						Arrays.asList("RED_CAT", "REDCAT", "RED", "ROT"));
		MbPets.getInstance()
				.getConfig()
				.set("ocelotStyles.SIAMESE_CAT",
						Arrays.asList("SIAMESE_CAT", "SIAMESE", "SIAMESISCH",
								"SIAM"));
	}

	/**
	 * adds predefined pet prices to the config.yml
	 */
	private static void createPetPriceNodes() {
		MbPets.getInstance().getConfig().set("prices.WOLF", 10000);
		MbPets.getInstance().getConfig().set("prices.OCELOT", 10000);
		MbPets.getInstance().getConfig().set("prices.SHEEP", 15000);
		MbPets.getInstance().getConfig().set("prices.HORSE", 20000);
		MbPets.getInstance().getConfig().set("prices.COW", 15000);
		MbPets.getInstance().getConfig().set("prices.MOOSHROOM", 20000);
		MbPets.getInstance().getConfig().set("prices.CHICKEN", 15000);
		MbPets.getInstance().getConfig().set("prices.PIG", 15000);
		MbPets.getInstance().getConfig().set("prices.DROPPED_ITEM", 25000);
		MbPets.getInstance().getConfig().set("prices.UNDEAD_HORSE", 30000);
		MbPets.getInstance().getConfig().set("prices.SKELETON_HORSE", 30000);
		MbPets.getInstance().getConfig().set("prices.IRON_GOLEM", 30000);
		MbPets.getInstance().getConfig().set("prices.DIAMOND", 35000);
		MbPets.getInstance().getConfig().set("prices.FIRE", 35000);
		MbPets.getInstance().getConfig().set("prices.TNT", 35000);
		// price for modifying
		MbPets.getInstance().getConfig().set("prices.MODIFY", 1000);
		MbPets.getInstance().getConfig().set("prices.COLOREDNAMETAG", 1000);
		MbPets.getInstance().saveConfig();
	}

	/**
	 * creates and saves the default-texts to config.yml
	 */
	private static void createTextNodes() {
		// infos
		MbPets.getInstance()
				.getConfig()
				.set("texts.info.StatusHeader",
						"{DARK_GREEN}Dein Pet ({GREEN}{0}{DARK_GREEN}) sieht bis jetzt so aus:");
		MbPets.getInstance()
				.getConfig()
				.set("texts.info.Confirm",
						"{DARK_GREEN}Um den Kauf abzuschließen, gib {GREEN}/pet confirm{DARK_GREEN} ein!\n\n");
		MbPets.getInstance().getConfig()
				.set("texts.info.Element", "{DARK_GREEN}{0}: {GREEN}{1}");
		MbPets.getInstance()
				.getConfig()
				.set("texts.info.petConfirmed",
						"{GREEN}Du hast erfolgreich ein Pet gekauft!");
		MbPets.getInstance()
				.getConfig()
				.set("texts.info.petRightclick",
						"{DARK_GREEN}Rechtsklicke innerhalb der nächsten {GREEN}10 Sekunden{DARK_GREEN} das Tier, welches du zum Pet machen willst!");
		MbPets.getInstance()
				.getConfig()
				.set("texts.info.petDeleted",
						"{GREEN}Das Pet wurde erfolgreich gelöscht!");
		MbPets.getInstance()
				.getConfig()
				.set("texts.info.petsFlushed",
						"{GREEN}Die Pets dieses Spielers wurden gelöscht!");
		MbPets.getInstance()
				.getConfig()
				.set("texts.info.preparedPetCanceled",
						"{GREEN}Deine Auswahl wurde zurückgesetzt!");
		MbPets.getInstance()
				.getConfig()
				.set("texts.info.listHead",
						"{DARK_GREEN}{0} besitzt folgende Pets:");
		MbPets.getInstance()
				.getConfig()
				.set("texts.info.listElement",
						"{DARK_GREEN}{0}{GREEN}. {1} {DARK_GREEN}{2}");
		MbPets.getInstance()
				.getConfig()
				.set("texts.info.petUncalled",
						"{GREEN}Du hast dein Pet weggeschickt!");
		MbPets.getInstance()
				.getConfig()
				.set("texts.info.petReadyToModify",
						"{GREEN}Dein Pet kann nun modifiziert werden!");
		MbPets.getInstance()
				.getConfig()
				.set("texts.info.monitorDB",
						"{DARK_GREEN}Die Datenbank ist derzeit {0}");
		MbPets.getInstance()
				.getConfig()
				.set("texts.info.monitorSpawnedPets",
						"{DARK_GREEN}Derzeit aktive Pets: {GREEN}{0}");
		MbPets.getInstance()
				.getConfig()
				.set("texts.info.monitorPreparedPets",
						"{DARK_GREEN}Derzeit bearbeitete Pets: {GREEN}{0}");
		MbPets.getInstance()
				.getConfig()
				.set("texts.info.configReloaded",
						"{GREEN}Die Config wurde neu generiert! Eine Kopie der alten Config wurde angelegt.");

		// help
		MbPets.getInstance()
				.getConfig()
				.set("texts.help.Options",
						"{DARK_GREEN}Folgende Optionen stehen dir zur Verfügung:\n"
								+ "{GREEN}/pet type/color/style/name/baby{WHITE} - {DARK_GREEN}Legt die jeweilige Eigenschaft des zu erstellenden Pets fest.\n"
								+ "{GREEN}/pet confirm{WHITE} - {DARK_GREEN}Schließt die Pet-Konfiguration ab und kauft das Pet.\n"
								+ "{GREEN}/pet call [#]{WHITE} - {DARK_GREEN}Ruft das letzte gekaufte Pet bzw jenes mit der angegeben Nummer.\n"
								+ "{GREEN}/pet cancel{WHITE} - {DARK_GREEN}Bricht deine derzeitige Pet-Konfiguration ab.\n"
								+ "{GREEN}/pet info [#]{WHITE} - {DARK_GREEN}Zeigt die Eigenschaften des Pets mit der angegebenen Nummer.\n"
								+ "{GREEN}/pet list [player <name>]{WHITE} - {DARK_GREEN}Zeigt deine Pets bzw die Pets eines Mitspielers an.\n"
								+ "{GREEN}/pet status{WHITE} - {DARK_GREEN}Zeigt deine derzeitige Pet-Konfiguration an.\n"
								+ "{GREEN}/pet uncall{WHITE} - {DARK_GREEN}Schickt das derzeit gerufene Pet weg.\n"
								+ "{DARK_GREEN}Bei Fragen zu den Pet-Eigenschaften gib {GREEN}/pet help type <Typ>{DARK_GREEN} ein!");
		MbPets.getInstance()
				.getConfig()
				.set("texts.help.Types",
						"{DARK_GREEN}Lass dir mit {GREEN}/pet help type <type>"
								+ "{DARK_GREEN} die Hilfe für verschiedene Pet-Typen anzeigen!");
		MbPets.getInstance()
				.getConfig()
				.set("texts.help.Head",
						"{DARK_GREEN}Folgende Optionen kannst du für diese "
								+ "Pets treffen:");
		MbPets.getInstance().getConfig()
				.set("texts.help.BABY", "{DARK_GRAY}true/false");
		MbPets.getInstance()
				.getConfig()
				.set("texts.help.COLOR",
						"{DARK_GRAY}Schwarz, Blau, Braun, Cyan, Grau, Grün, Hellblau, Limette, Magenta, Orange, "
								+ "Pink, Lila, Rot, Silber, Weiss, Gelb");
		MbPets.getInstance()
				.getConfig()
				.set("texts.help.HORSECOLOR",
						"{DARK_GRAY}Schwarz, Kastanie, Cremig, Dunkelbraun, Braun, Weiss");
		MbPets.getInstance()
				.getConfig()
				.set("texts.help.HORSESTYLE",
						"{DARK_GRAY}Schwarzfleckig, Kein, Weiß, Weißfleckig, Whitefield");
		MbPets.getInstance()
				.getConfig()
				.set("texts.help.OCELOTSTYLE",
						"{DARK_GRAY}Wild, Schwarz, Rot, Siamesisch");
		MbPets.getInstance()
				.getConfig()
				.set("texts.help.TYPE",
						"{DARK_GREEN}Folgende Typen stehen dir zur Verfügung: {GREEN}Pferd, Zombiepferd, "
								+ "Skelettpferd, Hund, Katze, Schwein, Eisengolem, Kuh, "
								+ "Huhn, Pilzkuh, Sheep, Diamant, Feuer, TnT");
		MbPets.getInstance()
				.getConfig()
				.set("texts.help.DROPPEDITEMTYPE",
						"{DARK_GRAY}Diamant, TnT, Feuer");
		MbPets.getInstance()
				.getConfig()
				.set("texts.help.NAME",
						"{DARK_GRAY}Jeder Name, der kein Leerzeichen enthält");

		// alerts
		MbPets.getInstance()
				.getConfig()
				.set("texts.error.noPermission",
						"{RED}Du hast keine ausreichende Berechtigung!");
		MbPets.getInstance()
				.getConfig()
				.set("texts.error.missingValue",
						"{RED}Für die Option {DARK_RED}{0}{RED} fehlt der Wert!");
		MbPets.getInstance()
				.getConfig()
				.set("texts.error.noDbConnection",
						"{RED}Die Verbindung zur Datenbank wurde unterbrochen!");
		MbPets.getInstance()
				.getConfig()
				.set("texts.error.wrongFunction",
						"{DARK_RED}{0}{RED} ist keine gültige Option!");
		MbPets.getInstance().getConfig()
				.set("texts.error.noPetToCall", "{RED}{0} besitzt keine Pets!");
		MbPets.getInstance()
				.getConfig()
				.set("texts.error.noPreparedPets",
						"{RED}Du erstellst gerade kein Pet!");
		MbPets.getInstance()
				.getConfig()
				.set("texts.error.missingType",
						"{RED}Du kannst keine Eigenschaften festlegen, bevor du nicht den Typ des Pets gewählt hast! Gib {DARK_RED}/pet help{RED} für Hilfe ein!");
		MbPets.getInstance()
				.getConfig()
				.set("texts.error.noActivePet",
						"{RED}Du hast derzeit kein Pet gerufen!");
		MbPets.getInstance()
				.getConfig()
				.set("texts.error.notThatManyPets",
						"{RED}Du hast nicht so viele Pets!");
		MbPets.getInstance()
				.getConfig()
				.set("texts.error.notThatMuchMoney",
						"{RED}Du hast nicht genügend Benches!");
		MbPets.getInstance()
				.getConfig()
				.set("texts.error.playerOffline",
						"{RED}Der Spieler ist offline!");
		MbPets.getInstance()
				.getConfig()
				.set("texts.error.playerUnknown",
						"{RED}Der Spieler ist unbekannt!");
		MbPets.getInstance()
				.getConfig()
				.set("texts.error.entityIsAlreadyAPet",
						"{RED}Das gewählte Tier ist bereits ein Pet!");
		MbPets.getInstance()
				.getConfig()
				.set("texts.error.notYourPet",
						"{RED}Das gewählte Tier ist bereits auf jemand anderen gesichert!");
		MbPets.getInstance()
				.getConfig()
				.set("texts.error.petNotFinished",
						"{RED}Du hast noch nicht alle Eigenschaften angegeben!\n Gib sie mit {DARK_RED}/pet <Eigenschaft> <Wert>{RED} an!");
		MbPets.getInstance()
				.getConfig()
				.set("texts.error.noVault",
						"{RED}Dieser Server unterstützt keine Economy!\n Wechsle mit {DARK_RED}/server{RED} auf einen anderen Server, um ein Pet zu kaufen!");
		MbPets.getInstance().saveConfig();

	}

	/**
	 * returns the number the given player has registered
	 * 
	 * @param targetPlayer
	 * @return
	 */
	public static int getNumberOfPets(OfflinePlayer targetPlayer) {

		PreparedStatement statement;
		int ret = 0;

		try {
			statement = DatabaseConnectionManager
					.getInstance()
					.getConnection()
					.prepareStatement(
							"Select count(playerid) as number from MbPets_Pet where playerid = (SELECT playerid from MbPets_Player where uuid = ?)");
			statement.setString(1, targetPlayer.getUniqueId().toString());
			ResultSet res = statement.executeQuery();
			ret = (res.first() ? res.getInt("number") : 0);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}

	/**
	 * gets the block the player is looking at.
	 * 
	 * @param player
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static Location getTargetBlockLocation(Player player) {
		Location loc = player.getTargetBlock(null, 0).getLocation();
		// gets stuck in earth sometimes.
		// Pet is invulnerable anyways, so it can be dropped from the sky :)
		loc.setY(loc.getY() + 1.5);
		return loc;
	}

	/**
	 * returns the field specified by the given key Colors in the pattern
	 * {COLOR} are converted to the ChatColor equivalent. e.g.: {RED} is
	 * replaced by ChatColor.RED
	 * 
	 * @param string
	 * @return
	 */
	public static String getTextNode(String key) {
		String ret = (String) MbPets.getInstance().getConfig()
				.get("texts." + key);
		if (ret != null && !ret.isEmpty()) {
			for (int i = 0; i < ChatColor.values().length; i++) {
				// the enum contains no element called "ChatColor.OBFUSCATED",
				// but ChatColor.values()[] does !?
				if (!ChatColor.values()[i].name()
						.equalsIgnoreCase("obfuscated")) {
					String replace = "{"
							+ ChatColor.values()[i].name().toUpperCase() + "}";
					ret = ret.replace(
							replace,
							ChatColor.valueOf(
									ChatColor.values()[i].name().toUpperCase())
									.toString());
				}
			}
			return ret;
		} else {
			return "Missing text node: " + key;
		}
	}

	/**
	 * init
	 */
	public static void init() {
		createTextNodes();
		createDisguiseTypesNodes();
		createAnimalColorNodes();
		createHorseColorNodes();
		createHorseStyleNodes();
		createOcelotStyleNodes();
		createDroppedItemNodes();
		createPetPriceNodes();
		MbPets.getInstance().saveConfig();
	}

	/**
	 * returs an AnimalColor from the chat input given. e.g. /pet ... color blau
	 * will return AnimalColor.BLUE
	 * 
	 * @param color
	 * @return
	 */
	public static AnimalColor parseColor(String color) {
		if (color == null) return null;
		for (AnimalColor a : AnimalColor.values()) {
			if (MbPets.getInstance().getConfig()
					.getStringList("animalColors." + a.name())
					.contains(color.toUpperCase())) {
				return a;
			}
		}
		return null;
	}

	/**
	 * returs an Horse.Color from the chat input given. e.g. /pet ... color
	 * Kastanie will return Horse.Color.CHESTNUT
	 * 
	 * @param color
	 * @return
	 */
	public static Horse.Color parseHorseColor(String color) {
		if (color == null) return null;
		for (Horse.Color a : Horse.Color.values()) {
			if (MbPets.getInstance().getConfig()
					.getStringList("horseColors." + a.name())
					.contains(color.toUpperCase())) {
				return a;
			}
		}
		return null;
	}

	/**
	 * returns an Horse.Style from the chat input given. e.g. /pet ... style ...
	 * schwarzfleckig will return Horse.Style.BLACK_DOTS
	 * 
	 * @param style
	 * @return
	 */
	public static Horse.Style parseHorseStyle(String style) {
		if (style == null) return null;
		for (Horse.Style a : Horse.Style.values()) {
			if (MbPets.getInstance().getConfig()
					.getStringList("horseStyles." + a.name())
					.contains(style.toUpperCase())) {
				return a;
			}
		}
		return null;
	}

	/**
	 * returns an Material from the chat input given. e.g. /pet ... style ...
	 * 
	 * @param type
	 * @return
	 */
	public static Material parseItemStack(String itemStack) {
		if (itemStack == null) return null;
		for (Material a : Material.values()) {
			if (MbPets.getInstance().getConfig()
					.getStringList("droppedItems." + a.name())
					.contains(itemStack.toUpperCase())) {
				return a;
			}
		}
		return null;
	}

	/**
	 * returns an Ocelot.Style from the chat input given. e.g. /pet ... style
	 * ...
	 * 
	 * @param type
	 * @return
	 */
	public static Ocelot.Type parseOcelotType(String type) {
		if (type == null) return null;
		for (Ocelot.Type a : Ocelot.Type.values()) {
			if (MbPets.getInstance().getConfig()
					.getStringList("ocelotStyles." + a.name())
					.contains(type.toUpperCase())) {
				return a;
			}
		}
		return null;
	}

	/**
	 * returs a DisguiseType from the chat input given. e.g. /pet ... type katze
	 * will return DisguiseType.CAT
	 * 
	 * @param type
	 * @return
	 */
	public static DisguiseType parseType(String type) {
		if (type == null) return null;
		for (DisguiseType a : DisguiseType.values()) {
			if (MbPets.getInstance().getConfig()
					.getStringList("disguiseTypes." + a.name())
					.contains(type.toUpperCase())) {
				return a;
			}
		}
		return null;
	}

	/**
	 * looks in the config for potential aliases for tabComplete
	 * 
	 * @param configurationPath
	 * @param input
	 * @return
	 */
	public static String parseConfigurationSectionForTabComplete(
			String configurationPath, String input) {
		if (input.isEmpty()) {
			return null;
		}
		List<String> list = MbPets.getInstance().getConfig()
				.getStringList(configurationPath);
		for (String s : list) {
			if (s.toUpperCase().startsWith(input.toUpperCase())) {
				return s;
			}
		}
		return null;
	}

}
