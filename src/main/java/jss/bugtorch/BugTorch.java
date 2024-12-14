package jss.bugtorch;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLConstructionEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import glowredman.txloader.Asset.Source;
import glowredman.txloader.TXLoaderCore;
import jss.bugtorch.config.BugTorchConfig;
import jss.bugtorch.listeners.ButtonManager;
import jss.bugtorch.listeners.ExplodingItemsRegistry;
import jss.bugtorch.mixinplugin.BugTorchEarlyMixins;
import jss.bugtorch.modsupport.ExtraUtilitiesSupport;
import jss.bugtorch.modsupport.PamsTemperatePlantsSupport;
import jss.bugtorch.modsupport.ThaumcraftSupport;
import jss.bugtorch.modsupport.TorchLeversSupport;
import jss.bugtorch.modsupport.VanillaSupport;
import jss.bugtorch.modsupport.VillageNamesSupport;
import jss.bugtorch.modsupport.WitcherySupport;
import jss.bugtorch.util.AssetLoader;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

import static jss.bugtorch.config.BugTorchConfig.Hchunpowderlist;

@Mod(
    modid = BugTorch.MODID,
    name = BugTorch.NAME,
    version = BugTorch.VERSION,
    acceptableRemoteVersions = "*",
    dependencies = "required-after:gtnhmixins@[2.0.0,);after:Thaumcraft;after:temperateplants;after:VillageNames;after:witchery;"
)
public class BugTorch {

    public static final String MODID = "bugtorch";
    public static final String NAME = "BugTorch";
    public static final String VERSION = Tags.VERSION;
    public static final Logger logger = LogManager.getLogger(NAME);
    // cached to boost looping, should be used pretty often due to how recipe lookup works
    public static final int[] dyeOreIds = new int[16];

    public BugTorch() {
        String configFolder = Loader.instance().getConfigDir().getAbsolutePath() + File.separator + MODID + File.separator;
        BugTorchConfig.loadBaseConfig(new File(configFolder + "base.cfg"));
        BugTorchConfig.loadModdedConfig(new File(configFolder + "modSupport.cfg"));
        if (BugTorchEarlyMixins.txLoaderPresent) {
            new Runnable() {
                @Override
                public void run() {
                    TXLoaderCore.getAssetBuilder("minecraft/sounds/mob/ghast/fireball4.ogg")
                    .setOverride("bugtorch/sounds/mob/ghast/fireball4.ogg")
                    .setSource(Source.ASSET)
                    .setVersion("1.19.2")
                    .add();
                }
            }.run();
        }
    }


    @Mod.EventHandler
    public void construct(FMLConstructionEvent event) {
        if (!FMLCommonHandler.instance().getSide().isClient()) return;

        try {
            Class.forName("glowredman.txloader.TXLoaderCore");
            BugTorchConfig.txLoaderPresent = true;
            AssetLoader.load();
        } catch (Exception ignored) {
            BugTorchConfig.txLoaderPresent = false;
        }
    }
    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

        String configFolder =  event.getModConfigurationDirectory().getAbsolutePath() + File.separator + MODID + File.separator;
        BugTorchConfig.loadBaseConfig(new File(configFolder + "base.cfg"));
        BugTorchConfig.loadModdedConfig(new File(configFolder + "modSupport.cfg"));

        VanillaSupport.enableSupport(event);

        if(event.getSide() == Side.CLIENT) {
            FMLCommonHandler.instance().bus().register(ButtonManager.INSTANCE);
            MinecraftForge.EVENT_BUS.register(ButtonManager.INSTANCE);
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        String[] dyes = {
            "dyeBlack",
            "dyeRed",
            "dyeGreen",
            "dyeBrown",
            "dyeBlue",
            "dyePurple",
            "dyeCyan",
            "dyeLightGray",
            "dyeGray",
            "dyePink",
            "dyeLime",
            "dyeYellow",
            "dyeLightBlue",
            "dyeMagenta",
            "dyeOrange",
            "dyeWhite"
        };
        for (int i = 0; i < dyes.length; i++) {
            dyeOreIds[i] = OreDictionary.getOreID(dyes[i]);
        }

        if(Loader.isModLoaded("ExtraUtilities")) {
            ExtraUtilitiesSupport.enableSupport();
        }

        if(Loader.isModLoaded("temperateplants")) {
            PamsTemperatePlantsSupport.enableSupport();
        }

        if(Loader.isModLoaded("Thaumcraft")) {
            ThaumcraftSupport.enableSupport();
        }

        if(Loader.isModLoaded("torchLevers")) {
            TorchLeversSupport.enableSupport();
        }

        if(Loader.isModLoaded("witchery")) {
            WitcherySupport.enableSupport();
        }

        if(Loader.isModLoaded("VillageNames")) {
            VillageNamesSupport.enableSupport();
        }
        for (String itemName : Hchunpowderlist) {
            String modId;
            String itemNameOnly;
            int metadata = 0;
            float explosionPower = 3f; // Default explosion power

            String[] parts = itemName.split(":");
            if (parts.length >= 2) {
                modId = parts[0];
                itemNameOnly = parts[1];
                if (parts.length >= 3) {
                    try {
                        metadata = Integer.parseInt(parts[2]);
                    } catch (NumberFormatException e) {
                        // If not an integer, assume it's a float for explosion power
                        try {
                            explosionPower = Float.parseFloat(parts[2]);
                        } catch (NumberFormatException ex) {
                            continue;
                        }
                    }
                }

                if (parts.length == 4) {
                    try {
                        explosionPower = Float.parseFloat(parts[3]);
                    } catch (NumberFormatException e) {
                        continue;
                    }
                }
                Item item = GameRegistry.findItem(modId, itemNameOnly);
                if (item != null) {
                    ExplodingItemsRegistry.ItemWithMeta explodingItem = new ExplodingItemsRegistry.ItemWithMeta(item, metadata, explosionPower);
                    ExplodingItemsRegistry.explodingItems.add(explodingItem);
                }
            }
        }
    }
}
