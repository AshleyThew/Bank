package me.dablakbandit.bank;

import me.dablakbandit.bank.database.BankDatabaseManager;
import me.dablakbandit.bank.test.TestEnvironment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BankWikiExtract {

    private TestEnvironment testEnvironment;

    @BeforeEach
    public void setUp() {
        testEnvironment = new TestEnvironment();
    }

    @Test
    void pluginLoadEnabled() {
        assertTrue(true);
        await().until(() -> BankDatabaseManager.getInstance().getBankDatabase().isConnected());
        File dataFolder = BankPlugin.getInstance().getDataFolder();
        assertTrue(dataFolder.exists());
        File storage = new File(".", "wiki/");

        if (!storage.exists()) {
            storage.mkdirs();
        }

        // list .yml in dataFolder
        File[] baseFiles = dataFolder.listFiles(pathname -> pathname.getName().endsWith(".yml"));

        List<File> files = new ArrayList<>(Arrays.asList(baseFiles));

        // Add files from dataFolder/conf/ to files
        File advFolder = new File(dataFolder, "conf");
        if (advFolder.exists()) {
            File[] advFiles = advFolder.listFiles(pathname -> pathname.getName().endsWith(".yml"));
            files.addAll(Arrays.asList(advFiles));
        }

        // Sort by file name
        files.sort((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()));

        for (File yaml : files) {
            File wikiFile = new File(storage, "config-" + yaml.getName() + ".md");
            System.out.println("Writing to " + wikiFile.getName());
            // real yaml file and output to wikiFile

            try (FileReader fileReader = new FileReader(yaml);
                    FileWriter fileWriter = new FileWriter(wikiFile)) {

                // Write opening triple backticks
                fileWriter.write(
                        "Generated with bank v"
                                + BankPlugin.getInstance().getDescription().getVersion() + "\n");
                fileWriter.write("```\n");

                // Copy YAML file content as plain text
                int c;
                while ((c = fileReader.read()) != -1) {
                    fileWriter.write(c);
                }

                // Write opening triple backticks
                fileWriter.write("\n```");

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        File sideBar = new File(storage, "_Sidebar.md");
        try {
            List<String> lines = new ArrayList<>();
            boolean configSectionFound = false;
            boolean configSectionFinished = false;

            // Read the current _Sidebar.md file
            try (BufferedReader reader = new BufferedReader(new FileReader(sideBar))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(
                            line + " " + configSectionFound + " " + configSectionFinished + " " + line.endsWith(":"));
                    if (!configSectionFound && line.startsWith("Configs:")) {
                        lines.add(line);
                        configSectionFound = true;
                        for (File file : files) {
                            lines.add("* [" + file.getName() + "](config-" + file.getName() + ")");
                        }
                    } else if (configSectionFound && !configSectionFinished) {
                        if (line.endsWith(":")) {
                            lines.add("");
                            lines.add(line);
                            configSectionFinished = true;
                        }
                    } else {
                        lines.add(line);
                    }
                }
            }

            // If the config section was not found, add it
            if (!configSectionFound) {
                lines.add("Config:");
                for (File file : files) {
                    lines.add("* [" + file.getName() + "](config-" + file.getName() + ")");
                }
            }

            // Write the updated content back to _Sidebar.md
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(sideBar))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    public void tearDown() {
        testEnvironment.tearDown();
    }
}
