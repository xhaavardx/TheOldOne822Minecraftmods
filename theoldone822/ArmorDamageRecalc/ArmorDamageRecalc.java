package theoldone822.ArmorDamageRecalc;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraft.item.ItemStack;
//import net.minecraft.util.RegistryNamespaced;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = "ArmorDamageRecalc", name = "Armor Damage Recalc", version = "0.9d")
public class ArmorDamageRecalc {

	Configuration settings;
	Configuration helm;
	Configuration chest;
	Configuration legs;
	Configuration boots;

	Configuration cloak;
	Configuration shoulder;
	Configuration vambrace;

	public static int[] helmAmountI;
	public static String[] helmListI;

	public static int[] chestAmountI;
	public static String[] chestListI;

	public static int[] legsAmountI;
	public static String[] legsListI;

	public static int[] bootsAmountI;
	public static String[] bootsListI;

	private static boolean firstRun = true;

	@Instance("ArmorDamageRecalc")
	public static ArmorDamageRecalc instance = new ArmorDamageRecalc();

	public static int maxArmor = 25;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new EventHandlers());
		ModMetadata metadata = event.getModMetadata();
		metadata.autogenerated = false;

		metadata.authorList = Arrays.asList("theoldone822");
		metadata.description = ".";
		File installDir = event.getModConfigurationDirectory();
		File configDir = new File(installDir, "ArmorDamageRecalc");
		settings = new Configuration(new File(configDir, "ArmorDamageRecalc.cfg"));

		File f = new File(configDir, "HelmBonusList.cfg");
		if (f.exists()) {
			firstRun = false;
		}

		helm = new Configuration(new File(configDir, "HelmBonusList.cfg"));
		chest = new Configuration(new File(configDir, "ChestplateBonusList.cfg"));
		legs = new Configuration(new File(configDir, "LeggingsBonusList.cfg"));
		boots = new Configuration(new File(configDir, "BootsBonusList.cfg"));

		settings.load();
		settings.save();
	}

	@EventHandler
	public void PostInitiateArmor(FMLPostInitializationEvent initEvent) {

/*		if (firstRun) {
			for (int i = 0; i < 32000; i++) {
				if (Item.getItemById(i) != null) {
					if (Item.getItemById(i) instanceof ItemArmor) {
						ItemArmor item = (ItemArmor) Item.getItemById(i);
						int n = item.damageReduceAmount;
						int c = item.armorType;
						float factor = maxArmor / 25;
						switch (c) {
						case 0:
							helmAmountI = ArrayUtils.add(helmAmountI, ((int) Math.floor((n * factor)) - n));
							helmListI = ArrayUtils.add(helmListI, Item.itemRegistry.getNameForObject(item));
							break;
						case 1:
							chestAmountI = ArrayUtils.add(chestAmountI, ((int) Math.floor((n * factor)) - n));
							chestListI = ArrayUtils.add(chestListI, Item.itemRegistry.getNameForObject(item));
							break;
						case 2:
							legsAmountI = ArrayUtils.add(legsAmountI, ((int) Math.floor((n * factor)) - n));
							legsListI = ArrayUtils.add(legsListI, Item.itemRegistry.getNameForObject(item));
							break;
						case 3:
							bootsAmountI = ArrayUtils.add(bootsAmountI, ((int) Math.floor((n * factor)) - n));
							bootsListI = ArrayUtils.add(bootsListI, Item.itemRegistry.getNameForObject(item));
							break;
						}
					}
				}
			}
			helm.load();
			ExtendedHandler.helmList = helm.get("armor bonus", "ListOfHelms", helmListI,
					"Must have same number of entries and in same order as HelmBonusList").getStringList();
			ExtendedHandler.helmAmount = helm
					.get("armor bonus", "HelmBonusList", helmAmountI,
							"The amount to add to the armor normally provided. Must have same number of entries and in same order as ListOfHelms")
					.getIntList();
			helm.save();

			chest.load();
			ExtendedHandler.chestList = chest
					.get("armor bonus", "ListOfChestplates", chestListI,
							"Must have same number of entries and in same order as ChestplateBonusList")
					.getStringList();
			ExtendedHandler.chestAmount = chest
					.get("armor bonus", "ChestplateBonusList", chestAmountI,
							"The amount to add to the armor normally provided. Must have same number of entries and in same order as ListOfChestplates")
					.getIntList();
			chest.save();

			legs.load();
			ExtendedHandler.legsList = legs.get("armor bonus", "ListOfLeggings", legsListI,
					"Must have same number of entries and in same order as LeggingBonusList").getStringList();
			ExtendedHandler.legsAmount = legs
					.get("armor bonus", "LeggingBonusList", legsAmountI,
							"The amount to add to the armor normally provided. Must have same number of entries and in same order as ListOfLeggings")
					.getIntList();
			legs.save();

			boots.load();
			ExtendedHandler.bootsList = boots.get("armor bonus", "ListOfBoots", bootsListI,
					"Must have same number of entries and in same order as BootBonusList").getStringList();
			ExtendedHandler.bootsAmount = boots
					.get("armor bonus", "BootBonusList", bootsAmountI,
							"The amount to add to the armor normally provided. Must have same number of entries and in same order as ListOfBoots")
					.getIntList();
			boots.save();

		} else {

			helm.load();
			ExtendedHandler.helmList = helm.get("armor bonus", "ListOfHelms", new String[] { "" },
					"Must have same number of entries and in same order as HelmBonusList").getStringList();
			ExtendedHandler.helmAmount = helm
					.get("armor bonus", "HelmBonusList", new int[] { 0 },
							"The amount to add to the armor normally provided. Must have same number of entries and in same order as ListOfHelms")
					.getIntList();
			helm.save();

			chest.load();
			ExtendedHandler.chestList = chest
					.get("armor bonus", "ListOfChestplates", new String[] { "" },
							"Must have same number of entries and in same order as ChestplateBonusList")
					.getStringList();
			ExtendedHandler.chestAmount = chest
					.get("armor bonus", "ChestplateBonusList", new int[] {},
							"The amount to add to the armor normally provided. Must have same number of entries and in same order as ListOfChestplates")
					.getIntList();
			chest.save();

			legs.load();
			ExtendedHandler.legsList = legs.get("armor bonus", "ListOfLeggings", new String[] { "" },
					"Must have same number of entries and in same order as LeggingBonusList").getStringList();
			ExtendedHandler.legsAmount = legs
					.get("armor bonus", "LeggingBonusList", new int[] {},
							"The amount to add to the armor normally provided. Must have same number of entries and in same order as ListOfLeggings")
					.getIntList();
			legs.save();

			boots.load();
			ExtendedHandler.bootsList = boots.get("armor bonus", "ListOfBoots", new String[] { "" },
					"Must have same number of entries and in same order as BootBonusList").getStringList();
			ExtendedHandler.bootsAmount = boots
					.get("armor bonus", "BootBonusList", new int[] {},
							"The amount to add to the armor normally provided. Must have same number of entries and in same order as ListOfBoots")
					.getIntList();
			boots.save();
		}
*/
	}
}