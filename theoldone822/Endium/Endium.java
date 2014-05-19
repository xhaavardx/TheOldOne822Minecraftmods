package theoldone822.Endium;

import java.util.Arrays;

import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.EnumToolMaterial;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.EnumHelper;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@NetworkMod(clientSideRequired = true, serverSideRequired = false)
@Mod(modid = "endium", name = "Endium", version = "1.1", dependencies = "required-after:simpleores; required-after:simpleoresfusion; after:netherrocksfusion")
public class Endium {
	public static EnumToolMaterial toolEndium;
	public static EnumToolMaterial toolTelos;
	public static EnumToolMaterial toolSunteleia;

	public static EnumArmorMaterial armorEndium;
	public static EnumArmorMaterial armorTelos;
	public static EnumArmorMaterial armorSunteleia;

	public static int rendererEndium;
	public static int rendererTelos;
	public static int rendererSunteleia;

	@Instance("Endium")
	public static Endium instance = new Endium();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new EventHelper());
		ModMetadata metadata = event.getModMetadata();
		metadata.autogenerated = false;

		metadata.authorList = Arrays.asList("theoldone822");
		metadata.description = "Simpleores End metal.";

		TickRegistry.registerTickHandler(new TickHandler(), Side.SERVER);

		IDs.doConfig(event);
		Settings.doSettings(event);
		
		toolEndium = EnumHelper.addToolMaterial("ENDIUM", Settings.EndiumMiningLevel, Settings.EndiumUsesNum, Settings.EndiumMiningSpeed, Settings.EndiumDamageVsEntity, Settings.EndiumEnchantability);
		toolTelos = EnumHelper.addToolMaterial("TELOS", Settings.TelosMiningLevel, Settings.TelosUsesNum, Settings.TelosMiningSpeed, Settings.TelosDamageVsEntity, Settings.TelosEnchantability);
		toolSunteleia = EnumHelper.addToolMaterial("SUNTELEIA", Settings.SunteleiaMiningLevel, Settings.SunteleiaUsesNum, Settings.SunteleiaMiningSpeed, Settings.SunteleiaDamageVsEntity, Settings.SunteleiaEnchantability);

		armorEndium = EnumHelper.addArmorMaterial("ENDIUM", Settings.EndiumArmorDurability, Settings.EndiumArmorDamageReduction, Settings.EndiumArmorEnchantability);
		armorTelos = EnumHelper.addArmorMaterial("TELOS", Settings.TelosArmorDurability, Settings.TelosArmorDamageReduction, Settings.TelosArmorEnchantability);
		armorSunteleia = EnumHelper.addArmorMaterial("SUNTELEIA", Settings.SunteleiaArmorDurability, Settings.SunteleiaArmorDamageReduction, Settings.SunteleiaArmorEnchantability);

		Content.doArmor();
		Content.doBlocks();
		Content.doItems();
		Content.doTools();
		Recipes.doRecipes();
	}

	@EventHandler
    public void load(FMLInitializationEvent event)
    {
    	GameRegistry.registerWorldGenerator(new EndiumGenerator());
		if (Loader.isModLoaded("TreeCapitator")) {
			NBTTagCompound c = new NBTTagCompound();
			c.setString("modID", "endium");
			c.setString("axeIDList", String.valueOf(Content.EndiumAxe.itemID) + "; " + String.valueOf(Content.TelosAxe.itemID) + "; " + String.valueOf(Content.SunteleiaAxe.itemID));
			FMLInterModComms.sendMessage("TreeCapitator", "ThirdPartyModConfig", c);
		}
		
		toolEndium.customCraftingMaterial = Content.EndiumIngot;
		toolTelos.customCraftingMaterial = Content.TelosIngot;
		toolSunteleia.customCraftingMaterial = Content.SunteleiaIngot;
		armorEndium.customCraftingMaterial = Content.EndiumIngot;
		armorTelos.customCraftingMaterial = Content.TelosIngot;
		armorSunteleia.customCraftingMaterial = Content.SunteleiaIngot;
    }
}
