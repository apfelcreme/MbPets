package io.github.apfelcreme.MbPets;

import io.github.apfelcreme.MbPets.Interfaces.DroppedItem;
import io.github.apfelcreme.MbPets.Interfaces.FallingBlock;
import me.libraryaddict.disguise.disguisetypes.AnimalColor;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.RabbitType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Ocelot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Collection of all config-related stuff
 * @author Jan
 *
 */
public class MbPetsConfig {

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
		createRabbitStyleNodes();
		createDroppedItemNodes();
		createFallingBlockNodes();
		createSlimeSizesNodes();
		createPetPriceNodes();
		createPetDamageNodes();
		MbPets.getInstance().saveConfig();
	}
	
	/**
	 * adds all AnimalColors to the config.yml
	 */
	private static void createAnimalColorNodes() {
		MbPets.getInstance().getConfig().set("animalColors.BLACK.displaytext", "Schwarz");
		MbPets.getInstance()
				.getConfig()
				.set("animalColors.BLACK.aliases",
						Arrays.asList("BLACK", "SCHWARZ", "DUNKEL"));
		MbPets.getInstance().getConfig().set("animalColors.BLUE.displaytext", "Blau");
		MbPets.getInstance().getConfig()
				.set("animalColors.BLUE.aliases", Arrays.asList("BLUE", "BLAU"));
		MbPets.getInstance().getConfig().set("animalColors.BROWN.displaytext", "Braun");
		MbPets.getInstance().getConfig()
				.set("animalColors.BROWN.aliases", Arrays.asList("BROWN", "BRAUN"));
		MbPets.getInstance().getConfig().set("animalColors.CYAN.displaytext", "Cyan");
		MbPets.getInstance().getConfig()
				.set("animalColors.CYAN.aliases", Arrays.asList("CYAN", "TÜRKIS"));
		MbPets.getInstance().getConfig().set("animalColors.GRAY.displaytext", "Grau");
		MbPets.getInstance().getConfig()
				.set("animalColors.GRAY.aliases", Arrays.asList("GRAY", "GRAU"));
		MbPets.getInstance().getConfig().set("animalColors.GREEN.displaytext", "Grün");
		MbPets.getInstance()
				.getConfig()
				.set("animalColors.GREEN.aliases",
						Arrays.asList("GREEN", "GRUEN", "GRÜN", "GRAS"));
		MbPets.getInstance().getConfig().set("animalColors.LIGHT_BLUE.displaytext", "Hellblau");
		MbPets.getInstance()
				.getConfig()
				.set("animalColors.LIGHT_BLUE.aliases",
						Arrays.asList("LIGHT_BLUE", "LIGHT-BLUE", "LIGHTBLUE", "HELLBLAU",
								"HIMMELBLAU", "HIMMEL"));
		MbPets.getInstance().getConfig().set("animalColors.LIME.displaytext", "Limette");
		MbPets.getInstance().getConfig()
				.set("animalColors.LIME.aliases", Arrays.asList("LIME", "LIMETTE"));
		MbPets.getInstance().getConfig().set("animalColors.MAGENTA.displaytext", "Magenta");
		MbPets.getInstance().getConfig()
				.set("animalColors.MAGENTA.aliases", Arrays.asList("MAGENTA"));
		MbPets.getInstance().getConfig().set("animalColors.ORANGE.displaytext", "Orange");
		MbPets.getInstance().getConfig()
				.set("animalColors.ORANGE.aliases", Arrays.asList("ORANGE"));
		MbPets.getInstance().getConfig().set("animalColors.PINK.displaytext", "Pink");
		MbPets.getInstance().getConfig()
				.set("animalColors.PINK.aliases", Arrays.asList("PINK", "ROSA"));
		MbPets.getInstance().getConfig().set("animalColors.PURPLE.displaytext", "Lila");
		MbPets.getInstance()
				.getConfig()
				.set("animalColors.PURPLE.aliases",
						Arrays.asList("PURPLE", "VIOLETT", "LILA"));
		MbPets.getInstance().getConfig().set("animalColors.RED.displaytext", "Rot");
		MbPets.getInstance().getConfig()
				.set("animalColors.RED.aliases", Arrays.asList("RED", "ROT"));
		MbPets.getInstance().getConfig().set("animalColors.SILVER.displaytext", "Silber");
		MbPets.getInstance().getConfig()
				.set("animalColors.SILVER.aliases", Arrays.asList("SILVER", "SILBER"));
		MbPets.getInstance().getConfig().set("animalColors.WHITE.displaytext", "Weiß");
		MbPets.getInstance()
				.getConfig()
				.set("animalColors.WHITE.aliases",
						Arrays.asList("WHITE", "WEIß", "WEISS"));
		MbPets.getInstance().getConfig().set("animalColors.YELLOW.displaytext", "Gelb");
		MbPets.getInstance().getConfig()
				.set("animalColors.YELLOW.aliases", Arrays.asList("YELLOW", "GELB"));
	}

	/**
	 * adds predefined prices to the config.yml
	 */
	private static void createDisguiseTypesNodes() {
		MbPets.getInstance().getConfig().set("disguiseTypes.COW.displaytext", "Kuh");
		MbPets.getInstance().getConfig()
				.set("disguiseTypes.COW.aliases", Arrays.asList("KUH", "COW"));
		MbPets.getInstance().getConfig().set("disguiseTypes.HORSE.displaytext", "Pferd");
		MbPets.getInstance().getConfig()
				.set("disguiseTypes.HORSE.aliases", Arrays.asList("HORSE", "PFERD"));
		MbPets.getInstance().getConfig().set("disguiseTypes.UNDEAD_HORSE.displaytext", "Zombiepferd");
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.UNDEAD_HORSE.aliases",
						Arrays.asList("ZOMBIEPFERD", "ZOMBIEHORSE",
								"UNDEAD_HORSE"));
		MbPets.getInstance().getConfig().set("disguiseTypes.SKELETON_HORSE.displaytext", "Skelettpferd");
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.SKELETON_HORSE.aliases",
						Arrays.asList("SKELETTPFERD", "SKELETTHORSE",
								"SEKELTON_HORSE"));
		MbPets.getInstance().getConfig().set("disguiseTypes.WOLF.displaytext", "Hund");
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.WOLF.aliases", Arrays.asList("WOLF", "DOG", "HUND"));
		MbPets.getInstance().getConfig().set("disguiseTypes.OCELOT.displaytext", "Katze");
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.OCELOT.aliases",
						Arrays.asList("OCELOT", "CAT", "KATZE"));
		MbPets.getInstance().getConfig().set("disguiseTypes.PIG.displaytext", "Schwein");
		MbPets.getInstance().getConfig()
				.set("disguiseTypes.PIG.aliases", Arrays.asList("PIG", "SCHWEIN"));
		MbPets.getInstance().getConfig().set("disguiseTypes.IRON_GOLEM.displaytext", "Eisengolem");
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.IRON_GOLEM.aliases",
						Arrays.asList("IRON_GOLEM", "EISENGOLEM", "IRONGOLEM",
								"GOLEM"));
		MbPets.getInstance().getConfig().set("disguiseTypes.CHICKEN.displaytext", "Huhn");
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.CHICKEN.aliases",
						Arrays.asList("CHICKEN", "CHICK", "HUHN", "KÜKEN",
								"HÜHNCHEN"));
		MbPets.getInstance().getConfig().set("disguiseTypes.MUSHROOM_COW.displaytext", "Pilzkuh");
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.MUSHROOM_COW.aliases",
						Arrays.asList("MUSHROOMCOW", "MUSHROOM-COW", "MUSHROOM_COW",
								"MOOSHROOM", "PILZKUH"));
		MbPets.getInstance().getConfig().set("disguiseTypes.SHEEP.displaytext", "Schaf");
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.SHEEP.aliases",
						Arrays.asList("SHEEP", "SCHAF", "PULLOVERSCHWEIN"));
		MbPets.getInstance().getConfig().set("disguiseTypes.RABBIT.displaytext", "Hase");
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.RABBIT.aliases",
						Arrays.asList("RABBIT", "HASE", "HÄSCHEN", "KANINCHEN",
								"KLOPFER", "KARNICKEL", "BUNNY"));
		MbPets.getInstance().getConfig().set("disguiseTypes.ENDERMAN.displaytext", "Enderman");
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.ENDERMAN.aliases",
						Arrays.asList("ENDERMAN", "ENDERMEN", "ENDER",
								"SLENDERMAN"));
		MbPets.getInstance().getConfig().set("disguiseTypes.SLIME.displaytext", "Slime");
		MbPets.getInstance().getConfig()
				.set("disguiseTypes.SLIME.aliases", Arrays.asList("SLIME", "SCHLEIM"));
		MbPets.getInstance().getConfig().set("disguiseTypes.MAGMA_CUBE.displaytext", "Magmacube");
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.MAGMA_CUBE.aliases",
						Arrays.asList("MAGMA_CUBE", "MAGMA-CUBE", "MAGMACUBE", "MAGMA",
								"LAVACUBE"));
		MbPets.getInstance().getConfig().set("disguiseTypes.PRIMED_TNT.displaytext", "Explodierendes TNT");
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.PRIMED_TNT.aliases",
						Arrays.asList("PRIMED_TNT", "PRIMEDTNT", "TNT", "SPRENGSTOFF",
								"EXPLOSION", "EXPLODIERENDES_TNT", "EXPLODIERENDESTNT", "EXPLODIERENDES-TNT"));
		//these 2 are just for from-db-reconstruction
		MbPets.getInstance().getConfig().set("disguiseTypes.DROPPED_ITEM.displaytext", "Item");
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.DROPPED_ITEM.aliases",
						Arrays.asList("DROPPED_ITEM", "DROPPEDITEM"));
		MbPets.getInstance().getConfig().set("disguiseTypes.FALLING_BLOCK.displaytext", "Block");
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.FALLING_BLOCK.aliases",
						Arrays.asList("FALLING_BLOCK", "FALLINGBLOCK"));
	}

	/**
	 * adds predefined {@link DroppedItem} aliases to the config.yml
	 */
	public static void createDroppedItemNodes() {
		MbPets.getInstance().getConfig().set("disguiseTypes.DROPPED_ITEM.DIAMOND.displaytext", "Diamant");
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.DROPPED_ITEM.DIAMOND.aliases",
						Arrays.asList("DIAMOND", "DIAMANT", "DIA"));
		MbPets.getInstance().getConfig().set("disguiseTypes.DROPPED_ITEM.TNT.displaytext", "TnT");
//		MbPets.getInstance().getConfig()
//				.set("disguiseTypes.DROPPED_ITEM.TNT.aliases", Arrays.asList("TNT", "SPRENGSTOFF"));
//		MbPets.getInstance().getConfig().set("disguiseTypes.DROPPED_ITEM.FIRE.displaytext", "Feuer");
//		MbPets.getInstance().getConfig()
//				.set("disguiseTypes.DROPPED_ITEM.FIRE.aliases", Arrays.asList("FIRE", "FEUER"));
		MbPets.getInstance().getConfig().set("disguiseTypes.DROPPED_ITEM.OBSIDIAN.displaytext", "Feuerteufel");
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.DROPPED_ITEM.OBSIDIAN.aliases",
						Arrays.asList("OBSIDIAN", "TEUFEL", "TEUFEL",
								"FEUERTEUFEL"));
		MbPets.getInstance().getConfig().set("disguiseTypes.DROPPED_ITEM.SNOW_BLOCK.displaytext", "Eisengel");
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.DROPPED_ITEM.SNOW_BLOCK.aliases",
						Arrays.asList("SNOW_BLOCK", "SNOW-BLOCK", "SNOW", "ENGEL", "ANGEL",
								"EISENGEL"));

	}


	/**
	 * adds predefined {@link FallingBlock} aliases to the config.yml
	 */
	public static void createFallingBlockNodes() {
		MbPets.getInstance().getConfig().set("disguiseTypes.FALLING_BLOCK.DIAMOND_BLOCK.displaytext", "Diamantblock");
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.FALLING_BLOCK.DIAMOND_BLOCK.aliases",
						Arrays.asList("DIAMOND_BLOCK", "DIAMOND-BLOCK", "DIAMANTBLOCK",
								"DIAMONDBLOCK", "DIABLOCK"));
		MbPets.getInstance().getConfig().set("disguiseTypes.FALLING_BLOCK.GOLD_BLOCK.displaytext", "Goldblock");
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.FALLING_BLOCK.GOLD_BLOCK.aliases",
						Arrays.asList("GOLD_BLOCK", "GOLD-BLOCK", "GOLDBLOCK"));
		MbPets.getInstance().getConfig().set("disguiseTypes.FALLING_BLOCK.IRON_BLOCK.displaytext", "Eisenblock");
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.FALLING_BLOCK.IRON_BLOCK.aliases",
						Arrays.asList("IRON_BLOCK", "IRON-BLOCK", "IRONBLOCK", "EISENBLOCK"));
		MbPets.getInstance().getConfig().set("disguiseTypes.FALLING_BLOCK.LAPIS_BLOCK.displaytext", "Lapislazuliblock");
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.FALLING_BLOCK.LAPIS_BLOCK.aliases",
						Arrays.asList("LAPIS_BLOCK", "LAPIS-BLOCK", "LAPISBLOCK",
								"LAPISLAZULIBLOCK", "LAPISLAZULI_BLOCK"));
		MbPets.getInstance().getConfig().set("disguiseTypes.FALLING_BLOCK.GRASS.displaytext", "Grasblock");
		MbPets.getInstance().getConfig()
				.set("disguiseTypes.FALLING_BLOCK.GRASS.aliases", Arrays.asList("GRASS", "GRAß", "GRAS", "GRASBLOCK", "GRASSBLOCK", "GRAßBLOCK"));
		MbPets.getInstance().getConfig().set("disguiseTypes.FALLING_BLOCK.CAKE_BLOCK.displaytext", "Kuchen");
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.FALLING_BLOCK.CAKE_BLOCK.aliases",
						Arrays.asList("CAKE_BLOCK", "CAKE-BLOCK", "CAKEBLOCK", "KUCHEN",
								"KUCHENBLOCK"));
		MbPets.getInstance().getConfig().set("disguiseTypes.FALLING_BLOCK.WORKBENCH.displaytext", "Werkbank");
		MbPets.getInstance()
				.getConfig()
				.set("disguiseTypes.FALLING_BLOCK.WORKBENCH.aliases",
						Arrays.asList("WORKBENCH", "MINEBENCH",
								"CRAFTING_TABLE", "CRAFTINGTABLE"));
	}

	/**
	 * adds all HorseColors to the config.yml
	 * 
	 */
	public static void createHorseColorNodes() {
		MbPets.getInstance().getConfig().set("horseColors.BLACK.displaytext", "Schwarz");
		MbPets.getInstance()
				.getConfig()
				.set("horseColors.BLACK.aliases",
						Arrays.asList("BLACK", "SCHWARZ", "DUNKEL"));
		MbPets.getInstance().getConfig().set("horseColors.BROWN.displaytext", "Braun");
		MbPets.getInstance().getConfig()
				.set("horseColors.BROWN.aliases", Arrays.asList("BROWN", "BRAUN"));
		MbPets.getInstance().getConfig().set("horseColors.CHESTNUT.displaytext", "Kastanie");
		MbPets.getInstance()
				.getConfig()
				.set("horseColors.CHESTNUT.aliases",
						Arrays.asList("CHESTNUT", "KASTANIE", "NUSS"));
		MbPets.getInstance().getConfig().set("horseColors.CREAMY.displaytext", "Cremig");
		MbPets.getInstance()
				.getConfig()
				.set("horseColors.CREAMY.aliases",
						Arrays.asList("CREAMY", "CREMIG", "KREMIG"));
		MbPets.getInstance().getConfig().set("horseColors.DARK_BROWN.displaytext", "Dunkelbraun");
		MbPets.getInstance()
				.getConfig()
				.set("horseColors.DARK_BROWN.aliases",
						Arrays.asList("DARK_BROWN", "DARK-BROWN", "DARKBROWN", "DUNKELBRAUN"));
		MbPets.getInstance().getConfig().set("horseColors.GRAY.displaytext", "Grau");
		MbPets.getInstance().getConfig()
				.set("horseColors.GRAY.aliases", Arrays.asList("GRAY", "GRAU"));
		MbPets.getInstance().getConfig().set("horseColors.WHITE.displaytext", "Weiß");
		MbPets.getInstance()
				.getConfig()
				.set("horseColors.WHITE.aliases",
						Arrays.asList("WHITE", "WEIß", "WEISS"));
	}

	/**
	 * adds all HorseStyles to the config.yml
	 */
	public static void createHorseStyleNodes() {
		MbPets.getInstance().getConfig().set("horseStyles.BLACK_DOTS.displaytext", "Schwarzfleckig");
		MbPets.getInstance()
				.getConfig()
				.set("horseStyles.BLACK_DOTS.aliases",
						Arrays.asList("BLACK_DOTS", "BLACK-DOTS", "BLACK", "SCHWARZ",
								"SCHWARZFLECKIG", "BLACKDOTTED"));
		MbPets.getInstance().getConfig().set("horseStyles.NONE.displaytext", "Keiner");
		MbPets.getInstance()
				.getConfig()
				.set("horseStyles.NONE.aliases",
						Arrays.asList("NONE", "KEINS", "KEINE", "KEIN", "KEINER"));
		MbPets.getInstance().getConfig().set("horseStyles.WHITE.displaytext", "Weiß");
		MbPets.getInstance()
				.getConfig()
				.set("horseStyles.WHITE.aliases",
						Arrays.asList("WHITE", "WEIß", "WEISS"));
		MbPets.getInstance().getConfig().set("horseStyles.WHITE_DOTS.displaytext", "Weißfleckig");
		MbPets.getInstance()
				.getConfig()
				.set("horseStyles.WHITE_DOTS.aliases",
						Arrays.asList("WHITE_DOTS", "WHITE-DOTS", "WEIßFLECKIG",
								"WEISSFLECKIG", "WHITEDOTTED"));
		MbPets.getInstance().getConfig().set("horseStyles.WHITEFIELD.displaytext", "Milchig");
		MbPets.getInstance()
				.getConfig()
				.set("horseStyles.WHITEFIELD.aliases",
						Arrays.asList("WHITEFIELD", "WEIßFELD", "WEISSFELD",
								"MILCHIG", "VERSCHWOMMEN"));
	}

	/**
	 * adds all OcelotStyles to the config.yml
	 */
	public static void createOcelotStyleNodes() {
		MbPets.getInstance().getConfig().set("ocelotStyles.WILD_OCELOT.displaytext", "Wild");
		MbPets.getInstance()
				.getConfig()
				.set("ocelotStyles.WILD_OCELOT.aliases",
						Arrays.asList("WILD_OCELOT", "WILD-OCELOT", "WILDOCELOT", "WILD"));
		MbPets.getInstance().getConfig().set("ocelotStyles.BLACK_CAT.displaytext", "Schwarz");
		MbPets.getInstance()
				.getConfig()
				.set("ocelotStyles.BLACK_CAT.aliases",
						Arrays.asList("BLACK_CAT", "BLACK-CAT", "BLACKCAT", "BLACK",
								"SCHWARZ", "DUNKEL"));
		MbPets.getInstance().getConfig().set("ocelotStyles.RED_CAT.displaytext", "Rot");
		MbPets.getInstance()
				.getConfig()
				.set("ocelotStyles.RED_CAT.aliases",
						Arrays.asList("RED_CAT", "RED-CAT", "REDCAT", "RED", "ROT"));
		MbPets.getInstance().getConfig().set("ocelotStyles.SIAMESE_CAT.displaytext", "Siamesisch");
		MbPets.getInstance()
				.getConfig()
				.set("ocelotStyles.SIAMESE_CAT.aliases",
						Arrays.asList("SIAMESE_CAT", "SIAMESE-CAT", "SIAMESE", "SIAMESISCH",
								"SIAM"));
	}

	/**
	 * adds predefined Rabbit rypes to the config.yml
	 */
	private static void createRabbitStyleNodes() {
		MbPets.getInstance().getConfig().set("rabbitTypes.BLACK.displaytext", "Schwarz");
		MbPets.getInstance()
				.getConfig()
				.set("rabbitTypes.BLACK.aliases",
						Arrays.asList("BLACK", "SCHWARZ", "DUNKEL"));
		MbPets.getInstance().getConfig().set("rabbitTypes.BROWN.displaytext", "Braun");
		MbPets.getInstance()
				.getConfig()
				.set("rabbitTypes.BROWN.aliases",
						Arrays.asList("BROWN", "BRAUN", "NORMAL"));
		MbPets.getInstance().getConfig().set("rabbitTypes.GOLD.displaytext", "Gold");
		MbPets.getInstance().getConfig()
				.set("rabbitTypes.GOLD.aliases", Arrays.asList("GOLD", "GELB", "HELL"));
		MbPets.getInstance().getConfig().set("rabbitTypes.PATCHES.displaytext", "Fleckig");
		MbPets.getInstance()
				.getConfig()
				.set("rabbitTypes.PATCHES.aliases",
						Arrays.asList("PATCHES", "FLECKEN", "FLECKIG",
								"SCHWARZ-WEIß", "SCHWARZWEIß", "SCHWARZ-WEISS",
								"SCHWARZWEISS"));
		MbPets.getInstance().getConfig().set("rabbitTypes.PEPPER.displaytext", "Pfeffer");
		MbPets.getInstance()
				.getConfig()
				.set("rabbitTypes.PEPPER.aliases",
						Arrays.asList("PEPPER", "SALT_AND_PEPPER", "PFEFFER", "SALZPFEFFER",
								"PFEFFERSALZ"));
		MbPets.getInstance().getConfig().set("rabbitTypes.WHITE.displaytext", "Weiß");
		MbPets.getInstance()
				.getConfig()
				.set("rabbitTypes.WHITE.aliases",
						Arrays.asList("WHITE", "WEIß", "WEISS"));
		MbPets.getInstance().getConfig().set("rabbitTypes.KILLER_BUNNY.displaytext", "Killerbunny");
		MbPets.getInstance()
				.getConfig()
				.set("rabbitTypes.KILLER_BUNNY.aliases",
						Arrays.asList("KILLER_BUNNY", "KILLER-BUNNY", "KILLER", "KILLERBUNNY",
								"KILLERHASE", "KILLERKANINCHEN", "BÖSE"));
	}

	/**
	 * adds predefined size values for slimes and magmacubes
	 */
	private static void createSlimeSizesNodes() {
		MbPets.getInstance().getConfig().set("slimeSizes.1.displaytext", "Klein");
		MbPets.getInstance()
				.getConfig()
				.set("slimeSizes.1.aliases",
						Arrays.asList("1", "EINS", "KLEIN", "ONE", "SHORT"));
		MbPets.getInstance().getConfig().set("slimeSizes.2.displaytext", "Mittel");
		MbPets.getInstance()
				.getConfig()
				.set("slimeSizes.2.aliases",
						Arrays.asList("2", "ZWEI", "MITTEL", "TWO", "MEDIUM"));
		MbPets.getInstance().getConfig().set("slimeSizes.3.displaytext", "Groß");
		MbPets.getInstance()
				.getConfig()
				.set("slimeSizes.3.aliases",
						Arrays.asList("3", "DREI", "GROß", "GROSS", "THREE",
								"LARGE"));
		MbPets.getInstance().getConfig().set("slimeSizes.4.displaytext", "Riesig");
		MbPets.getInstance()
				.getConfig()
				.set("slimeSizes.4.aliases",
						Arrays.asList("4", "VIER", "RIESIG", "HUGE", "FOUR"));
		
	}

	/**
	 * adds predefined pet prices to the config.yml
	 */
	private static void createPetPriceNodes() {
		MbPets.getInstance().getConfig().set("prices.WOLF", 10000);
		MbPets.getInstance().getConfig().set("prices.OCELOT", 10000);
		MbPets.getInstance().getConfig().set("prices.SHEEP", 15000);
		MbPets.getInstance().getConfig().set("prices.RABBIT", 15000);
		MbPets.getInstance().getConfig().set("prices.HORSE", 20000);
		MbPets.getInstance().getConfig().set("prices.COW", 15000);
		MbPets.getInstance().getConfig().set("prices.MUSHROOM_COW", 20000);
		MbPets.getInstance().getConfig().set("prices.CHICKEN", 15000);
		MbPets.getInstance().getConfig().set("prices.PIG", 15000);
		MbPets.getInstance().getConfig().set("prices.DROPPED_ITEM", 25000);
		MbPets.getInstance().getConfig().set("prices.UNDEAD_HORSE", 30000);
		MbPets.getInstance().getConfig().set("prices.SKELETON_HORSE", 30000);
		MbPets.getInstance().getConfig().set("prices.IRON_GOLEM", 30000);
		MbPets.getInstance().getConfig().set("prices.ENDERMAN", 35000);
		MbPets.getInstance().getConfig().set("prices.SLIME", 20000);
		MbPets.getInstance().getConfig().set("prices.MAGMA_CUBE", 30000);
		MbPets.getInstance().getConfig().set("prices.FALLING_BLOCK", 25000);
		MbPets.getInstance().getConfig().set("prices.PRIMED_TNT", 25000);
		// price for modifying
		MbPets.getInstance().getConfig().set("prices.MODIFY", 1000);
		// percentage for selling
		MbPets.getInstance().getConfig().set("prices.SELL", 0.75);
		MbPets.getInstance().saveConfig();
	}

	/**
	 * adds predefined pet damage values to the config.yml
	 */
	private static void createPetDamageNodes() {
		MbPets.getInstance().getConfig().set("damage.WOLF", 4);
		MbPets.getInstance().getConfig().set("damage.OCELOT", 4);
		MbPets.getInstance().getConfig().set("damage.SHEEP", 3);
		MbPets.getInstance().getConfig().set("damage.RABBIT", 3);
		MbPets.getInstance().getConfig().set("damage.HORSE", 4);
		MbPets.getInstance().getConfig().set("damage.COW", 3);
		MbPets.getInstance().getConfig().set("damage.MUSHROOM_COW", 4);
		MbPets.getInstance().getConfig().set("damage.CHICKEN", 3);
		MbPets.getInstance().getConfig().set("damage.PIG", 3);
		MbPets.getInstance().getConfig().set("damage.DROPPED_ITEM", 6);
		MbPets.getInstance().getConfig().set("damage.UNDEAD_HORSE", 5);
		MbPets.getInstance().getConfig().set("damage.SKELETON_HORSE", 5);
		MbPets.getInstance().getConfig().set("damage.IRON_GOLEM", 5);
		MbPets.getInstance().getConfig().set("damage.ENDERMAN", 6);
		MbPets.getInstance().getConfig().set("damage.SLIME", 4);
		MbPets.getInstance().getConfig().set("damage.MAGMA_CUBE", 5);
		MbPets.getInstance().getConfig().set("damage.FALLING_BLOCK", 5);
		MbPets.getInstance().getConfig().set("damage.PRIMED_TNT", 5);
		// price for modifying
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
				.set("texts.info.petRightclickEnd",
						"{DARK_GREEN}Die Zeit ist abgelaufen!");
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
				.set("texts.info.types",
						"{DARK_GREEN}Folgende Pet-Typen stehen dir zur Verfügung: ");
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
				.set("texts.info.petSold",
						"{DARK_GREEN}Du hast {GREEN}{0}{DARK_GREEN} für {GREEN}{1}{DARK_GREEN} Benches verkauft!");
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
				.set("texts.info.monitorVersion",
						"{DARK_GREEN}Derzeitige Plugin-Version: {GREEN}{0}");
		MbPets.getInstance()
				.getConfig()
				.set("texts.info.despawnCount",
						"{DARK_GREEN}Es wurden {GREEN}{0}{DARK_GREEN} Pets entfernt");
		MbPets.getInstance()
				.getConfig()
				.set("texts.info.despawnCountRange",
						"{DARK_GREEN}Es wurden {GREEN}{0}{DARK_GREEN} Pets in einer Entfernung von {GREEN}{1}{DARK_GREEN} entfernt");
		MbPets.getInstance()
				.getConfig()
				.set("texts.info.configRegenerated",
						"{DARK_GREEN}Die Config wurde neu generiert! Eine Kopie der alten Config wurde angelegt.");
		MbPets.getInstance()
				.getConfig()
				.set("texts.info.configReloaded",
						"{DARK_GREEN}Die Config wurde neu geladen!");
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
								+ "{GREEN}/pet uncall{WHITE} - {DARK_GREEN}Schickt das derzeit gerufene Pet weg.\n \n"
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
	 * returns the field specified by the given key Colors in the pattern
	 * {COLOR} are converted to the ChatColor equivalent. e.g.: {RED} is
	 * replaced by ChatColor.RED
	 * 
	 * @param key
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
	 * returs an AnimalColor from the chat input given. e.g. /pet ... color blau
	 * will return AnimalColor.BLUE
	 * 
	 * @param color
	 * @return
	 */
	public static AnimalColor parseColor(String color) {
		if (color == null)
			return null;
		for (AnimalColor a : AnimalColor.values()) {
			if (MbPets.getInstance().getConfig()
					.getStringList("animalColors." + a.name() + ".aliases")
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
		if (color == null)
			return null;
		for (Horse.Color a : Horse.Color.values()) {
			if (MbPets.getInstance().getConfig()
					.getStringList("horseColors." + a.name() + ".aliases")
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
		if (style == null)
			return null;
		for (Horse.Style a : Horse.Style.values()) {
			if (MbPets.getInstance().getConfig()
					.getStringList("horseStyles." + a.name() + ".aliases")
					.contains(style.toUpperCase())) {
				return a;
			}
		}
		return null;
	}

	/**
	 * returns an Material from the chat input given. e.g. /pet ... style ...
	 * 
	 * @param itemStack
	 * @return
	 */
	public static Material parseMaterial(String itemStack) {
		if (itemStack == null)
			return null;
		for (Material a : Material.values()) {
			if (MbPets.getInstance().getConfig()
					.getStringList("disguiseTypes.DROPPED_ITEM." + a.name() + ".aliases")
					.contains(itemStack.toUpperCase())) {
				return a;
			}
		}
		return null;
	}

	/**
	 * returns an Material from the chat input given
	 * @param block
	 * @return
	 */
	public static Material parseBlock(String block) {
		if (block == null)
			return null;
		for (Material a : Material.values()) {
			if (MbPets.getInstance().getConfig()
					.getStringList("disguiseTypes.FALLING_BLOCK." + a.name() + ".aliases")
					.contains(block.toUpperCase())) {
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
		if (type == null)
			return null;
		for (Ocelot.Type a : Ocelot.Type.values()) {
			if (MbPets.getInstance().getConfig()
					.getStringList("ocelotStyles." + a.name() + ".aliases")
					.contains(type.toUpperCase())) {
				return a;
			}
		}
		return null;
	}

	/**
	 * returns a RabbitType from the chat input given. e.g. /pet ... style gold
	 * will return RabbitType.GOLD
	 * 
	 * @param type
	 * @return
	 */
	public static RabbitType parseRabbitType(String type) {
		if (type == null)
			return null;
		for (RabbitType a : RabbitType.values()) {
			if (MbPets.getInstance().getConfig()
					.getStringList("rabbitTypes." + a.name() + ".aliases")
					.contains(type.toUpperCase())) {
				return a;
			}
		}
		return null;
	}

	/**
	 * returns an Integer (1-3) for a slime's size. input would be e.g. "Groß" or "klein" 
	 * @param size
	 * @return
	 */
	public static Integer parseSlimeSize(String size) {
		if (size == null)
			return null;
		for (int i = 1; i < 5; i++) {
			if (MbPets.getInstance().getConfig()
					.getStringList("slimeSizes." + i + ".aliases")
					.contains(size.toUpperCase())) {
				return i;
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
		if (type == null)
			return null;
		for (DisguiseType a : DisguiseType.values()) {
			if (MbPets.getInstance().getConfig()
					.getStringList("disguiseTypes." + a.name() + ".aliases")
					.contains(type.toUpperCase())) {
				return a;
			}
		}
		return null;
	}

	/**
	 * returns a list of Strings that contains the names of all available pet-types
	 * 
	 * @return
	 */
	public static List<String> getAvailableTypes() {
		List<String> strings = new ArrayList<String>();
		for (DisguiseType type : DisguiseType.values()) {
			String typ = MbPets.getInstance().getConfig()
					.getString("disguiseTypes." + type.name() + ".displaytext"); 
			if (typ != null && !typ.equalsIgnoreCase("ITEM") && !typ.contains("BLOCK")) {
				strings.add(typ);
			}			
		}
		return strings;
	}

	/**
	 * returns a list of Strings that contains the names of all available animalColors
	 * 
	 * @return
	 */
	public static List<String> getAvailableColors() {
		List<String> strings = new ArrayList<String>();
		for (AnimalColor color : AnimalColor.values()) {
			String typ = MbPets.getInstance().getConfig()
				.getString("animalColors." + color.name() + ".displaytext"); 
			if (typ != null) {
				strings.add(typ);
			}
		}
		return strings;
	}

	/**
	 * returns a list of Strings that contains the names of all available HorseColors
	 * 
	 * @return
	 */
	public static List<String> getAvailableHorseColors() {
		List<String> strings = new ArrayList<String>();
		for (Horse.Color color : Horse.Color.values()) {
			String typ = MbPets.getInstance().getConfig()
				.getString("horseColors." + color.name() + ".displaytext"); 
			if (typ != null) {
				strings.add(typ);
			}
		}
		return strings;
	}

	/**
	 * returns a list of Strings that contains the names of all available HorseStyles
	 * 
	 * @return
	 */
	public static List<String> getAvailableHorseStyles() {
		List<String> strings = new ArrayList<String>();
		for (Horse.Style style : Horse.Style.values()) {
			String typ = MbPets.getInstance().getConfig()
				.getString("horseStyles." + style.name() + ".displaytext"); 
			if (typ != null) {
				strings.add(typ);
			}
		}
		return strings;
	}

	/**
	 * returns a list of Strings that contains the names of all available OcelotStyles
	 * 
	 * @return
	 */
	public static List<String> getAvailableOcelotStyles() {
		List<String> strings = new ArrayList<String>();
		for (Ocelot.Type style : Ocelot.Type.values()) {
			String typ = MbPets.getInstance().getConfig()
				.getString("ocelotStyles." + style.name() + ".displaytext"); 
			if (typ != null) {
				strings.add(typ);
			}
		}
		return strings;
	}

	/**
	 * returns a list of Strings that contains the names of all available RabbitStyles
	 * 
	 * @return
	 */
	public static List<String> getAvailableRabbitStyles() {
		List<String> strings = new ArrayList<String>();
		for (RabbitType style : RabbitType.values()) {
			String typ = MbPets.getInstance().getConfig()
				.getString("rabbitTypes." + style.name() + ".displaytext");
			if (typ != null) {
				strings.add(typ);
			}
		}
		return strings;
	}

	/**
	 * returns a list of Strings that contains the names of all available DroppedItems
	 * 
	 * @return
	 */
	public static List<String> getAvailableDroppedItems() {
		List<String> strings = new ArrayList<String>();
		for (Material material : Material.values()) {
			String typ = MbPets.getInstance().getConfig()
				.getString("disguiseTypes.DROPPED_ITEM." + material.name() + ".displaytext"); 
			if (typ != null) {
				strings.add(typ);
			}
		}
		return strings;
	}

	/**
	 * returns a list of Strings that contains the names of all available DroppedItems
	 * 
	 * @return
	 */
	public static List<String> getAvailableFallingBlocks() {
		List<String> strings = new ArrayList<String>();
		for (Material block : Material.values()) {
			String typ = MbPets.getInstance().getConfig()
				.getString("disguiseTypes.FALLING_BLOCK." + block.name() + ".displaytext"); 
			if (typ != null) {
				strings.add(typ);
			}
		}
		return strings;
	}


	/**
	 * returns a list of Strings that contains the names of all available DroppedItems
	 * 
	 * @return
	 */
	public static List<String> getAvailableSlimeSizes() {
		List<String> strings = new ArrayList<String>();
		for (int i = 1; i < 5; i++) {
			String text = MbPets.getInstance().getConfig()
				.getString("slimeSizes." + i + ".displaytext"); 
			if (text != null) {
				strings.add(text);
			}
		}
		return strings;
	}
	
	/**
	 * returns the type-specific price
	 * 
	 * @param type
	 * @return
	 */
	public static Double getPetPrice(DisguiseType type) {
		if (type == null) {
			return 0.0;
		}
		return MbPets.getInstance().getConfig()
				.getDouble("prices." + type.name(), 0);
	}

	/**
	 * returns the type-specific attack strength
	 * @param type
	 * @return
	 */
	public static Double getPetAttackStrength(DisguiseType type) {
		if (type == null) {
			return 4.0; // Basic attack stregth of a wolf
		}
		return MbPets.getInstance().getConfig()
				.getDouble("damage." + type.name(), 4.0);
	}
}
