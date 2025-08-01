package me.dablakbandit.bank;

import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.database.BankDatabaseManager;
import me.dablakbandit.bank.test.TestEnvironment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockbukkit.mockbukkit.entity.PlayerMock;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BankPluginTest {

    private TestEnvironment testEnvironment;

    @BeforeEach
    public void setUp() {
        testEnvironment = new TestEnvironment();
    }

    @Test
    void pluginLoadEnabled() {
        assertTrue(true);
        await().until(() -> BankDatabaseManager.getInstance().getBankDatabase().isConnected());
        BankPluginConfiguration.BANK_EXP_INTEREST_ENABLED.set(true);
        assertTrue(BankPluginConfiguration.BANK_EXP_INTEREST_ENABLED.get());
    }

    @Test
    void pluginLoadEnabled2() {
        assertTrue(true);
        await().until(() -> BankDatabaseManager.getInstance().getBankDatabase().isConnected());
        BankPluginConfiguration.BANK_EXP_INTEREST_ENABLED.set(true);
        assertTrue(BankPluginConfiguration.BANK_EXP_INTEREST_ENABLED.get());
        PlayerMock playerMock = testEnvironment.getServer().addPlayer();
        await().until(() -> CorePlayerManager.getInstance().getPlayer(playerMock) != null);
        CorePlayers corePlayer = CorePlayerManager.getInstance().getPlayer(playerMock);
        await().until(() -> !corePlayer.getInfo(BankInfo.class).isLocked(false));
    }

    @AfterEach
    public void tearDown() {
        testEnvironment.tearDown();
    }
}
