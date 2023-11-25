package jss.bugtorch;

import java.io.File;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import jss.bugtorch.listeners.BroadcastSettingsDisable;
import jss.bugtorch.listeners.BroadcastSettingsRemover;
import jss.bugtorch.listeners.LANButtonDisable;
import jss.bugtorch.listeners.LANButtonRemover;
import jss.bugtorch.listeners.SuperSecretSettingsDisable;
import jss.bugtorch.listeners.SuperSecretSettingsRemover;
import jss.bugtorch.modsupport.ExtraUtilitiesSupport;
import jss.bugtorch.modsupport.PamsTemperatePlantsSupport;
import jss.bugtorch.modsupport.ThaumcraftSupport;
import jss.bugtorch.modsupport.TorchLeversSupport;
import jss.bugtorch.modsupport.VanillaSupport;
import jss.bugtorch.modsupport.VillageNamesSupport;
import jss.bugtorch.modsupport.WitcherySupport;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.item.Item;
import cpw.mods.fml.common.registry.*;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import jss.bugtorch.config.BugTorchConfig;

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
    public static final String VERSION = "GRADLETOKEN_VERSION";
    public static final Logger logger = LogManager.getLogger(NAME);
    // cached to boost looping, should be used pretty often due to how recipe lookup works
    public static final int[] dyeOreIds = new int[16];

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        String configFolder =  event.getModConfigurationDirectory().getAbsolutePath() + File.separator + MODID + File.separator;
        BugTorchConfig.loadBaseConfig(new File(configFolder + "base.cfg"));
        BugTorchConfig.loadModdedConfig(new File(configFolder + "modSupport.cfg"));

        VanillaSupport.enableSupport();

        if(event.getSide() == Side.CLIENT) {
            if(BugTorchConfig.removeBroadcastSettingsButton) {
                FMLCommonHandler.instance().bus().register(BroadcastSettingsRemover.INSTANCE);
                MinecraftForge.EVENT_BUS.register(BroadcastSettingsRemover.INSTANCE);
            }
            if(BugTorchConfig.removeLANButton) {
                FMLCommonHandler.instance().bus().register(LANButtonRemover.INSTANCE);
                MinecraftForge.EVENT_BUS.register(LANButtonRemover.INSTANCE);
            }
            if(BugTorchConfig.removeSuperSecretSettingsButton) {
                FMLCommonHandler.instance().bus().register(SuperSecretSettingsRemover.INSTANCE);
                MinecraftForge.EVENT_BUS.register(SuperSecretSettingsRemover.INSTANCE);
            }
            if(BugTorchConfig.DisableBroadcastSettings) {
                FMLCommonHandler.instance().bus().register(BroadcastSettingsDisable.INSTANCE);
                MinecraftForge.EVENT_BUS.register(BroadcastSettingsDisable.INSTANCE);
            }
            if(BugTorchConfig.DisableLANButton) {
                FMLCommonHandler.instance().bus().register(LANButtonDisable.INSTANCE);
                MinecraftForge.EVENT_BUS.register(LANButtonDisable.INSTANCE);
            }
            if(BugTorchConfig.DisableSuperSecretSettings) {

                FMLCommonHandler.instance().bus().register(SuperSecretSettingsDisable.INSTANCE);
                MinecraftForge.EVENT_BUS.register(SuperSecretSettingsDisable.INSTANCE);
            }
        }
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        // 👽
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
    }

        if (Loader.isModLoaded("VillageNames")) {
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
