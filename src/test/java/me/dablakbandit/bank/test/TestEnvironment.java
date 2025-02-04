package me.dablakbandit.bank.test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import be.seeseemelk.mockbukkit.entity.PlayerMock;
import be.seeseemelk.mockbukkit.plugin.PluginManagerMock;
import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.save.loader.LoaderManager;
import me.dablakbandit.core.CorePlugin;

import static org.awaitility.Awaitility.await;

public class TestEnvironment {
    private final ServerMock server;
    private final CorePlugin corePlugin;
    private final BankPlugin bankPlugin;

    public TestEnvironment() {
        server = MockBukkit.mock();
        System.setProperty("core-test", "true");
        MockBukkit.ensureMocking();
        corePlugin = (CorePlugin) MockBukkit.getMock().getPluginManager().loadPlugin(CorePlugin.class, new Object[0]);
        bankPlugin = (BankPlugin) MockBukkit.getMock().getPluginManager().loadPlugin(BankPlugin.class, new Object[0]);
        MockBukkit.getMock().getPluginManager().enablePlugin(corePlugin);
        MockBukkit.getMock().getPluginManager().enablePlugin(bankPlugin);
        LoaderManager.getInstance().start();
    }

    public ServerMock getServer() {
        return server;
    }

    public CorePlugin getCorePlugin() {
        return corePlugin;
    }

    public BankPlugin getBankPlugin() {
        return bankPlugin;
    }

    public void tearDown() {
        PluginManagerMock pluginManager = MockBukkit.getMock().getPluginManager();
        for (PlayerMock onlinePlayer : server.getPlayerList().getOnlinePlayers()) {
            server.getPlayerList().disconnectPlayer(onlinePlayer);
        }
        await().until(() -> LoaderManager.getInstance().getLoaderThread().count() == 0);
        pluginManager.disablePlugin(bankPlugin);
        pluginManager.disablePlugin(corePlugin);
        MockBukkit.unmock();
    }
}
