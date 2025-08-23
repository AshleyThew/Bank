package me.dablakbandit.bank.implementations.cheque;

import me.dablakbandit.bank.BankPlugin;
import me.dablakbandit.bank.config.BankItemConfiguration;
import me.dablakbandit.bank.config.BankLanguageConfiguration;
import me.dablakbandit.bank.config.BankPluginConfiguration;
import me.dablakbandit.bank.database.BankDatabaseManager;
import me.dablakbandit.bank.database.base.IChequeDatabase;
import me.dablakbandit.bank.implementations.BankImplementation;
import me.dablakbandit.bank.player.info.BankMoneyInfo;
import me.dablakbandit.bank.utils.format.Format;
import me.dablakbandit.core.players.CorePlayers;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class ChequeImplementation extends BankImplementation {

	private static final ChequeImplementation instance = new ChequeImplementation();
	private static final NamespacedKey CHEQUE_ID_KEY = new NamespacedKey(BankPlugin.getInstance(), "bank_cheque_id");
	private static final NamespacedKey CHEQUE_BOOK_KEY = new NamespacedKey(BankPlugin.getInstance(), "bank_cheque_book");
	private static final NamespacedKey CHEQUE_BOOK_USES_KEY = new NamespacedKey(BankPlugin.getInstance(), "bank_cheque_book_uses");

	public static ChequeImplementation getInstance() {
		return instance;
	}

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

	private ChequeImplementation() {
	}

	public String generateChequeId() {
		// Generate a unique cheque ID using UUID for better security
		return UUID.randomUUID().toString();
	}

	/**
	 * Get the remaining uses from a cheque book item
	 *
	 * @param chequeBook the cheque book item
	 * @return remaining uses, or -1 if unlimited uses or invalid item
	 */
	public int getChequeBookRemainingUses(ItemStack chequeBook) {
		if (!isChequeBook(chequeBook)) {
			return -1;
		}

		// If uses are not enabled, return -1 to indicate unlimited
		if (!BankPluginConfiguration.BANK_CHEQUES_BOOK_USES_ENABLED.get()) {
			return -1;
		}

		ItemMeta meta = chequeBook.getItemMeta();
		if (meta == null) {
			return -1;
		}

		PersistentDataContainer dataContainer = meta.getPersistentDataContainer();

		// Check if uses data exists, if not, initialize with max uses
		if (!dataContainer.has(CHEQUE_BOOK_USES_KEY, PersistentDataType.INTEGER)) {
			int maxUses = BankPluginConfiguration.BANK_CHEQUES_BOOK_USES_AMOUNT.get();
			dataContainer.set(CHEQUE_BOOK_USES_KEY, PersistentDataType.INTEGER, maxUses);
			chequeBook.setItemMeta(meta);
			return maxUses;
		}

		return dataContainer.get(CHEQUE_BOOK_USES_KEY, PersistentDataType.INTEGER);
	}

	/**
	 * Consume one use from a cheque book and update the item
	 *
	 * @param chequeBook the cheque book item
	 * @return true if a use was consumed, false if no uses left or unlimited uses
	 */
	public boolean consumeChequeBookUse(ItemStack chequeBook) {
		if (!isChequeBook(chequeBook)) {
			return false;
		}

		// If uses are not enabled, always allow
		if (!BankPluginConfiguration.BANK_CHEQUES_BOOK_USES_ENABLED.get()) {
			return true;
		}

		int remainingUses = getChequeBookRemainingUses(chequeBook);
		if (remainingUses <= 0) {
			return false; // No uses left
		}

		// Consume one use
		ItemMeta meta = chequeBook.getItemMeta();
		PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
		int newUses = remainingUses - 1;
		dataContainer.set(CHEQUE_BOOK_USES_KEY, PersistentDataType.INTEGER, newUses);

		// Update the display to show new uses count
		updateChequeBookDisplay(chequeBook, meta, newUses);

		return true;
	}

	/**
	 * Update the cheque book display to show current uses
	 */
	private void updateChequeBookDisplay(ItemStack chequeBook, ItemMeta meta, int remainingUses) {
		boolean usesEnabled = BankPluginConfiguration.BANK_CHEQUES_BOOK_USES_ENABLED.get();
		int maxUses = BankPluginConfiguration.BANK_CHEQUES_BOOK_USES_AMOUNT.get();

		String configuredName;
		List<String> configuredLore;

		if (usesEnabled) {
			configuredName = BankItemConfiguration.BANK_CHEQUE_BOOK_ITEM.getName();
			configuredLore = new ArrayList<>(BankItemConfiguration.BANK_CHEQUE_BOOK_ITEM.getLore());
		} else {
			configuredName = BankItemConfiguration.BANK_CHEQUE_BOOK_ITEM_UNLIMITED.getName();
			configuredLore = new ArrayList<>(BankItemConfiguration.BANK_CHEQUE_BOOK_ITEM_UNLIMITED.getLore());
		}

		// Replace placeholders in name and lore
		String finalName = configuredName.replace("<uses>", String.valueOf(remainingUses)).replace("<max-uses>", String.valueOf(maxUses));

		List<String> finalLore = configuredLore.stream().map(line -> line.replace("<uses>", String.valueOf(remainingUses)).replace("<max-uses>", String.valueOf(maxUses))).collect(Collectors.toList());

		meta.setDisplayName(finalName);
		meta.setLore(finalLore);
		chequeBook.setItemMeta(meta);
	}

	/**
	 * Find a cheque book in player's inventory that can be used
	 *
	 * @param player the player
	 * @return the cheque book item or null if none found
	 */
	public ItemStack findUsableChequeBook(CorePlayers player) {
		for (ItemStack item : player.getPlayer().getInventory().getContents()) {
			if (item != null && isChequeBook(item)) {
				int remainingUses = getChequeBookRemainingUses(item);
				// If unlimited uses (-1) or has uses remaining
				if (remainingUses == -1 || remainingUses > 0) {
					return item;
				}
			}
		}
		return null;
	}

	public boolean createCheque(CorePlayers issuer, double amount, UUID recipientUuid) {
		return createCheque(issuer, amount, recipientUuid, false);
	}

	public boolean createCheque(CorePlayers issuer, double amount, UUID recipientUuid, boolean fromChequeBook) {
		if (!BankPluginConfiguration.BANK_CHEQUES_ENABLED.get()) {
			BankLanguageConfiguration.sendFormattedMessage(issuer, BankLanguageConfiguration.MESSAGE_CHEQUE_DISABLED.get());
			return false;
		}

		// Check if issuer has enough money
		BankMoneyInfo moneyInfo = issuer.getInfo(BankMoneyInfo.class);
		double createCost = BankPluginConfiguration.BANK_CHEQUES_CREATE_COST.get();
		double totalCost = amount + createCost;

		if (moneyInfo.getMoney() < totalCost) {
			BankLanguageConfiguration.sendFormattedMessage(issuer, BankLanguageConfiguration.MESSAGE_CHEQUE_INSUFFICIENT_FUNDS.get());
			return false;
		}

		// Check minimum/maximum amounts
		if (amount < BankPluginConfiguration.BANK_CHEQUES_MINIMUM_AMOUNT.get()) {
			String message = BankLanguageConfiguration.MESSAGE_CHEQUE_AMOUNT_TOO_LOW_WITH_MIN.get().replace("{amount}", Format.formatMoney(BankPluginConfiguration.BANK_CHEQUES_MINIMUM_AMOUNT.get()));
			BankLanguageConfiguration.sendFormattedMessage(issuer, message);
			return false;
		}

		if (amount > BankPluginConfiguration.BANK_CHEQUES_MAXIMUM_AMOUNT.get()) {
			String message = BankLanguageConfiguration.MESSAGE_CHEQUE_AMOUNT_TOO_HIGH_WITH_MAX.get().replace("{amount}", Format.formatMoney(BankPluginConfiguration.BANK_CHEQUES_MAXIMUM_AMOUNT.get()));
			BankLanguageConfiguration.sendFormattedMessage(issuer, message);
			return false;
		}

		// Generate cheque
		String chequeId = generateChequeId();
		long issueTime = System.currentTimeMillis();

		Cheque cheque = new Cheque(chequeId, UUID.fromString(issuer.getUUIDString()), recipientUuid, amount, issueTime);

		if (moneyInfo.getMoney() < totalCost) {
			return false;
		}

		// Check if book enabled and uses are enabled, then validate cheque book
		// Only check if NOT from cheque book or if explicitly required
		if (BankPluginConfiguration.BANK_CHEQUES_BOOK_ENABLED.get() && BankPluginConfiguration.BANK_CHEQUES_BOOK_USES_ENABLED.get() && fromChequeBook) {

			ItemStack chequeBook = findUsableChequeBook(issuer);
			if (chequeBook == null) {
				BankLanguageConfiguration.sendFormattedMessage(issuer, BankLanguageConfiguration.MESSAGE_CHEQUE_BOOK_REQUIRED.get());
				return false;
			}

			// Try to consume a use from the cheque book
			if (!consumeChequeBookUse(chequeBook)) {
				BankLanguageConfiguration.sendFormattedMessage(issuer, BankLanguageConfiguration.MESSAGE_CHEQUE_BOOK_NO_USES.get());
				return false;
			}

			// If cheque book now has 0 uses, remove it from inventory
			int remainingUses = getChequeBookRemainingUses(chequeBook);
			if (remainingUses <= 0) {
				issuer.getPlayer().getInventory().removeItem(chequeBook);
				BankLanguageConfiguration.sendFormattedMessage(issuer, BankLanguageConfiguration.MESSAGE_CHEQUE_BOOK_CONSUMED.get());
			}
		}

		moneyInfo.setMoney(moneyInfo.getMoney() - totalCost);

		// Store cheque in database asynchronously
		IChequeDatabase chequeDatabase = BankDatabaseManager.getInstance().getBankDatabase().getInfoDatabase().getChequeDatabase();
		chequeDatabase.storeCheque(cheque).thenAccept(success -> {
			Bukkit.getScheduler().runTask(me.dablakbandit.bank.BankPlugin.getInstance(), () -> {
				if (success) {
					// Only give cheque item after successful database storage
					ItemStack chequeItem = createChequeItem(cheque);
					issuer.getPlayer().getInventory().addItem(chequeItem);

					String recipientName = recipientUuid != null ? (Bukkit.getPlayer(recipientUuid) != null ? Bukkit.getPlayer(recipientUuid).getName() : "Unknown Player") : "Bearer";

					String message = BankLanguageConfiguration.MESSAGE_CHEQUE_CREATED_SUCCESS.get().replace("{amount}", Format.formatMoney(amount)).replace("{recipient}", recipientName);
					BankLanguageConfiguration.sendFormattedMessage(issuer, message);
				} else {
					// If database storage fails, refund the total cost (amount + creation cost)
					moneyInfo.addMoney(totalCost);
					BankLanguageConfiguration.sendFormattedMessage(issuer, BankLanguageConfiguration.MESSAGE_CHEQUE_CREATION_FAILED_REFUND.get());
				}
			});
		});

		return true;
	}

	public void redeemChequeItem(CorePlayers redeemer, ItemStack chequeItem) {
		if (!BankPluginConfiguration.BANK_CHEQUES_ENABLED.get()) {
			BankLanguageConfiguration.sendFormattedMessage(redeemer, BankLanguageConfiguration.MESSAGE_CHEQUE_DISABLED.get());
			return;
		}

		if (!isChequeItem(chequeItem)) {
			BankLanguageConfiguration.sendFormattedMessage(redeemer, BankLanguageConfiguration.MESSAGE_CHEQUE_INVALID.get());
			return;
		}

		String chequeId = getChequeIdFromItem(chequeItem);
		if (chequeId == null) {
			BankLanguageConfiguration.sendFormattedMessage(redeemer, BankLanguageConfiguration.MESSAGE_CHEQUE_INVALID_DATA.get());
			return;
		}

		IChequeDatabase chequeDatabase = BankDatabaseManager.getInstance().getBankDatabase().getInfoDatabase().getChequeDatabase();

		// Remove the cheque item from inventory
		redeemer.getPlayer().getInventory().removeItem(chequeItem);

		// Check database first (async)
		chequeDatabase.getCheque(chequeId).thenAccept(dbCheque -> {
			Bukkit.getScheduler().runTask(me.dablakbandit.bank.BankPlugin.getInstance(), () -> {
				if (dbCheque == null) {
					BankLanguageConfiguration.sendFormattedMessage(redeemer, BankLanguageConfiguration.MESSAGE_CHEQUE_NOT_EXISTS.get());
					return;
				}

				if (!dbCheque.canBeRedeemedBy(UUID.fromString(redeemer.getUUIDString()))) {
					if (dbCheque.isRedeemed()) {
						BankLanguageConfiguration.sendFormattedMessage(redeemer, BankLanguageConfiguration.MESSAGE_CHEQUE_ALREADY_REDEEMED.get());
					} else {
						redeemer.getPlayer().getInventory().addItem(chequeItem);
						BankLanguageConfiguration.sendFormattedMessage(redeemer, BankLanguageConfiguration.MESSAGE_CHEQUE_NOT_FOR_YOU.get());
					}
					return;
				}

				// Mark as redeemed in database (async)
				chequeDatabase.markChequeRedeemed(chequeId, redeemer.getUUIDString()).thenAccept(success -> {
					Bukkit.getScheduler().runTask(me.dablakbandit.bank.BankPlugin.getInstance(), () -> {
						if (success) {
							// Give money to redeemer
							BankMoneyInfo moneyInfo = redeemer.getInfo(BankMoneyInfo.class);
							moneyInfo.addMoney(dbCheque.getAmount());

							String message = BankLanguageConfiguration.MESSAGE_CHEQUE_REDEEMED_SUCCESS.get().replace("{amount}", Format.formatMoney(dbCheque.getAmount()));
							BankLanguageConfiguration.sendFormattedMessage(redeemer, message);
						} else {
							BankLanguageConfiguration.sendFormattedMessage(redeemer, BankLanguageConfiguration.MESSAGE_CHEQUE_ALREADY_REDEEMED_OR_INVALID.get());
						}
					});
				});
			});
		});
	}

	public ItemStack createChequeItem(Cheque cheque) {
		// Get base item from configuration
		ItemStack item = BankItemConfiguration.BANK_CHEQUE_ITEM.get().clone();
		ItemMeta meta = item.getItemMeta();

		// Get the configured name and lore
		String configuredName = BankItemConfiguration.BANK_CHEQUE_ITEM.getName();
		List<String> configuredLore = BankItemConfiguration.BANK_CHEQUE_ITEM.getLore();

		// Store cheque ID securely in PersistentDataContainer (not visible to users)
		PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
		dataContainer.set(CHEQUE_ID_KEY, PersistentDataType.STRING, cheque.getChequeId());

		String issuerName = Bukkit.getPlayer(cheque.getIssuer()) != null ? Bukkit.getPlayer(cheque.getIssuer()).getName() : "Unknown Player";
		String recipientName = cheque.getRecipient() != null ? (Bukkit.getPlayer(cheque.getRecipient()) != null ? Bukkit.getPlayer(cheque.getRecipient()).getName() : "Unknown Player") : "Bearer";
		String issueDate = dateFormat.format(new Date(cheque.getIssueTime()));

		// Replace placeholders in name
		String finalName = configuredName.replace("<amount>", Format.formatMoney(cheque.getAmount())).replace("<issuer>", issuerName).replace("<recipient>", recipientName).replace("<issue-date>", issueDate).replace("<cheque-id>", cheque.getChequeId());

		meta.setDisplayName(finalName);

		// Replace placeholders in lore
		List<String> finalLore = configuredLore.stream().map(line -> line.replace("<amount>", Format.formatMoney(cheque.getAmount())).replace("<issuer>", issuerName).replace("<recipient>", recipientName).replace("<issue-date>", issueDate).replace("<cheque-id>", cheque.getChequeId())).collect(Collectors.toList());

		meta.setLore(finalLore);

		item.setItemMeta(meta);
		return item;
	}

	public ItemStack createChequeBook() {
		boolean usesEnabled = BankPluginConfiguration.BANK_CHEQUES_BOOK_USES_ENABLED.get();
		int maxUses = BankPluginConfiguration.BANK_CHEQUES_BOOK_USES_AMOUNT.get();

		// Get base item from configuration based on uses enabled
		ItemStack item;
		String configuredName;
		List<String> configuredLore;

		if (usesEnabled) {
			item = BankItemConfiguration.BANK_CHEQUE_BOOK_ITEM.get().clone();
			configuredName = BankItemConfiguration.BANK_CHEQUE_BOOK_ITEM.getName();
			configuredLore = new ArrayList<>(BankItemConfiguration.BANK_CHEQUE_BOOK_ITEM.getLore());
		} else {
			item = BankItemConfiguration.BANK_CHEQUE_BOOK_ITEM_UNLIMITED.get().clone();
			configuredName = BankItemConfiguration.BANK_CHEQUE_BOOK_ITEM_UNLIMITED.getName();
			configuredLore = new ArrayList<>(BankItemConfiguration.BANK_CHEQUE_BOOK_ITEM_UNLIMITED.getLore());
		}

		ItemMeta meta = item.getItemMeta();

		// Add secure identifier to cheque book
		PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
		dataContainer.set(CHEQUE_BOOK_KEY, PersistentDataType.STRING, "bank_cheque_book");

		// Initialize uses if enabled
		if (usesEnabled) {
			dataContainer.set(CHEQUE_BOOK_USES_KEY, PersistentDataType.INTEGER, maxUses);

			// Replace placeholders in name and lore
			String finalName = configuredName.replace("<uses>", String.valueOf(maxUses)).replace("<max-uses>", String.valueOf(maxUses));

			List<String> finalLore = configuredLore.stream().map(line -> line.replace("<uses>", String.valueOf(maxUses)).replace("<max-uses>", String.valueOf(maxUses))).collect(Collectors.toList());

			meta.setDisplayName(finalName);
			meta.setLore(finalLore);
		} else {
			// For unlimited uses, just use the configured name and lore as-is
			meta.setDisplayName(configuredName);
			meta.setLore(configuredLore);
		}

		item.setItemMeta(meta);
		return item;
	}

	public boolean isChequeItem(ItemStack item) {
		if (item == null || !item.hasItemMeta()) {
			return false;
		}

		ItemMeta meta = item.getItemMeta();
		if (meta == null) {
			return false;
		}

		// Check for secure cheque ID first
		PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
		if (dataContainer.has(CHEQUE_ID_KEY, PersistentDataType.STRING)) {
			String chequeId = dataContainer.get(CHEQUE_ID_KEY, PersistentDataType.STRING);
			return chequeId != null && !chequeId.isEmpty();
		}
		return false;
	}

	public boolean isChequeBook(ItemStack item) {
		if (item == null || !item.hasItemMeta()) {
			return false;
		}

		ItemMeta meta = item.getItemMeta();
		if (meta == null) {
			return false;
		}

		// Check for secure identifier first
		PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
		if (dataContainer.has(CHEQUE_BOOK_KEY, PersistentDataType.STRING)) {
			String identifier = dataContainer.get(CHEQUE_BOOK_KEY, PersistentDataType.STRING);
			return "bank_cheque_book".equals(identifier);
		}
		return false;
	}

	public String getChequeIdFromItem(ItemStack item) {
		if (!isChequeItem(item)) {
			return null;
		}

		ItemMeta meta = item.getItemMeta();
		if (meta == null) {
			return null;
		}

		// Read cheque ID from secure PersistentDataContainer
		PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
		if (dataContainer.has(CHEQUE_ID_KEY, PersistentDataType.STRING)) {
			return dataContainer.get(CHEQUE_ID_KEY, PersistentDataType.STRING);
		}

		return null;
	}

	@Override
	public void load() {
	}

	@Override
	public void enable() {
		Bukkit.getPluginManager().registerEvents(ChequeItemListener.getInstance(), BankPlugin.getInstance());
	}

	@Override
	public void disable() {
		HandlerList.unregisterAll(ChequeItemListener.getInstance());
	}
}
