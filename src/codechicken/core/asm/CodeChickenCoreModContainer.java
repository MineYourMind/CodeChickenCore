package codechicken.core.asm;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import codechicken.core.CCUpdateChecker;
import codechicken.core.featurehack.LiquidTextures;
import codechicken.core.internal.CCCEventHandler;
import codechicken.core.launch.CodeChickenCorePlugin;
import codechicken.lib.config.ConfigFile;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.VersionParser;
import net.minecraftforge.fml.common.versioning.VersionRange;
import net.minecraftforge.common.MinecraftForge;

public class CodeChickenCoreModContainer extends DummyModContainer
{
    public static ConfigFile config;

    public static void loadConfig() {
        if(config == null)
            config = new ConfigFile(new File(CodeChickenCorePlugin.minecraftDir, "config/CodeChickenCore.cfg")).setComment("CodeChickenCore configuration file.");
    }

    public CodeChickenCoreModContainer() {
        super(MetadataCollection.from(MetadataCollection.class.getResourceAsStream("/cccmod.info"), "CodeChickenCore").getMetadataForId("CodeChickenCore", null));
    }

    @Override
    public List<ArtifactVersion> getDependants() {
        LinkedList<ArtifactVersion> deps = new LinkedList<ArtifactVersion>();
        if(!getVersion().contains("$")) {
            deps.add(VersionParser.parseVersionReference("NotEnoughItems@[1.0.5,)"));
            deps.add(VersionParser.parseVersionReference("EnderStorage@[1.4.6,)"));
            deps.add(VersionParser.parseVersionReference("ChickenChunks@[1.3.5,)"));
            deps.add(VersionParser.parseVersionReference("Translocator@[1.1.2,)"));
            deps.add(VersionParser.parseVersionReference("WR-CBE|Core@[1.4.2,)"));
        }
        return deps;
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    @Subscribe
    public void preInit(FMLPreInitializationEvent event) {
        if (event.getSide().isClient())
            LiquidTextures.init();
    }

    @Subscribe
    public void init(FMLInitializationEvent event) {
        if (event.getSide().isClient()) {
            if (config.getTag("checkUpdates").getBooleanValue(true))
                CCUpdateChecker.updateCheck(getModId());
            FMLCommonHandler.instance().bus().register(new CCCEventHandler());
            MinecraftForge.EVENT_BUS.register(new CCCEventHandler());
        }
    }

    @Override
    public VersionRange acceptableMinecraftVersionRange() {
        return VersionParser.parseRange(CodeChickenCorePlugin.mcVersion);
    }
}
