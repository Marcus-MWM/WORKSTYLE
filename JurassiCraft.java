//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.ilexiconn.jurassicraft;

import com.google.gson.Gson;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import net.ilexiconn.jurassicraft.common.CommonProxy;
import net.ilexiconn.jurassicraft.common.block.JCBlockRegistry;
import net.ilexiconn.jurassicraft.common.command.CommandSpawnDino;
import net.ilexiconn.jurassicraft.common.crafting.JCRecipeRegistry;
import net.ilexiconn.jurassicraft.common.creativetab.JCCreativeTabRegistry;
import net.ilexiconn.jurassicraft.common.data.CapeContainer;
import net.ilexiconn.jurassicraft.common.entity.JCEntityRegistry;
import net.ilexiconn.jurassicraft.common.events.JurassiCraftInteractEvent;
import net.ilexiconn.jurassicraft.common.events.JurassiCraftLivingEvent;
import net.ilexiconn.jurassicraft.common.handler.GuiHandler;
import net.ilexiconn.jurassicraft.common.handler.JsonEntityHandler;
import net.ilexiconn.jurassicraft.common.item.JCItemRegistry;
import net.ilexiconn.jurassicraft.common.message.MessageAnimation;
import net.ilexiconn.jurassicraft.common.message.MessageFence;
import net.ilexiconn.jurassicraft.common.tileentity.JCTileEntityRegistry;
import net.ilexiconn.jurassicraft.common.world.WorldGenAmberOre;
import net.ilexiconn.jurassicraft.common.world.WorldGenFossilOre;
import net.ilexiconn.jurassicraft.common.world.WorldGenGypsum;
import net.ilexiconn.llibrary.server.util.WebUtils;
import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = "jurassicraft",
        name = "JurassiCraft",
        version = "1.5.0",
        dependencies = "required-after:llibrary@[1.4.0,)"
)
public class JurassiCraft {
    @SidedProxy(
            clientSide = "net.ilexiconn.jurassicraft.client.ClientProxy",
            serverSide = "net.ilexiconn.jurassicraft.common.CommonProxy"
    )
    public static CommonProxy proxy;
    @Instance("jurassicraft")
    public static JurassiCraft instance;
    public static boolean enableDebugging;
    public static JsonEntityHandler entityParser;
    public static SimpleNetworkWrapper network;
    public static int entityIndex = 0;
    public static CapeContainer capeContainer;
    public Logger logger;

    public JurassiCraft() {
    }

    public static String getModId() {
        return "jurassicraft:";
    }

    @EventHandler
    public void init(FMLPreInitializationEvent event) throws Exception {
        this.logger = event.getModLog();
        entityParser = new JsonEntityHandler();
        entityParser.parseServerEntities();
        (new JCCreativeTabRegistry()).init();
        (new JCEntityRegistry()).init();
        (new JCBlockRegistry()).init();
        (new JCItemRegistry()).init();
        (new JCRecipeRegistry()).init();
        (new JCTileEntityRegistry()).init();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());
        network = NetworkRegistry.INSTANCE.newSimpleChannel("jcWrapper");
        network.registerMessage(MessageAnimation.class, MessageAnimation.class, 0, Side.CLIENT);
        network.registerMessage(MessageFence.class, MessageFence.class, 1, Side.SERVER);
        GameRegistry.registerWorldGenerator(new WorldGenAmberOre(), 1);
        GameRegistry.registerWorldGenerator(new WorldGenFossilOre(), 1);
        GameRegistry.registerWorldGenerator(new WorldGenGypsum(), 1);
        proxy.init();
        MinecraftForge.EVENT_BUS.register(new JurassiCraftLivingEvent());
        MinecraftForge.EVENT_BUS.register(new JurassiCraftInteractEvent());
        capeContainer = (CapeContainer)(new Gson()).fromJson(WebUtils.readPastebin("qhA18Kcq"), CapeContainer.class);
        // capeContainer = new CapeContainer();
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        MinecraftServer server = MinecraftServer.func_71276_C();
        ICommandManager command = server.func_71187_D();
        ServerCommandManager manager = (ServerCommandManager)command;
        manager.func_71560_a(new CommandSpawnDino());
    }
}
