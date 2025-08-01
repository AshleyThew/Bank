package me.dablakbandit.bank.database.sql;

import me.dablakbandit.bank.database.base.IChequeDatabase;
import me.dablakbandit.bank.implementations.cheque.Cheque;
import me.dablakbandit.bank.log.BankLog;
import me.dablakbandit.core.database.listener.SQLListener;

import java.sql.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public abstract class SQLChequeDatabase extends SQLListener implements IChequeDatabase {

	private static final String TABLE_NAME = "bank_cheques";

	private PreparedStatement insertCheque;
	private PreparedStatement selectCheque;
	private PreparedStatement updateRedeemed;
	private PreparedStatement checkExists;
	private PreparedStatement checkRedeemed;
	private PreparedStatement selectAllCheques;

	@Override
	public void setup(Connection connection) {
		try {
			String createTableSql = getCreateTableSql();

			Statement statement = connection.createStatement();
			statement.execute(createTableSql);
			statement.close();

			insertCheque = connection.prepareStatement("INSERT INTO " + TABLE_NAME + " " + "(cheque_id, issuer, recipient, amount, issue_time) " + "VALUES (?, ?, ?, ?, ?)");

			selectCheque = connection.prepareStatement("SELECT * FROM " + TABLE_NAME + " WHERE cheque_id = ?");

			updateRedeemed = connection.prepareStatement("UPDATE " + TABLE_NAME + " SET redeemed = TRUE, redeemed_by = ?, redeemed_time = ? " + "WHERE cheque_id = ? AND redeemed = FALSE");

			checkExists = connection.prepareStatement("SELECT 1 FROM " + TABLE_NAME + " WHERE cheque_id = ?");

			checkRedeemed = connection.prepareStatement("SELECT redeemed FROM " + TABLE_NAME + " WHERE cheque_id = ?");

			selectAllCheques = connection.prepareStatement("SELECT * FROM " + TABLE_NAME);

		} catch (SQLException e) {
			BankLog.error("Error creating cheque table: " + e.getMessage());
			e.printStackTrace();
		}
	}

	protected abstract String getCreateTableSql();

	@Override
	public void close(Connection connection) {
		closeStatements();
	}

	@Override
	public CompletableFuture<Boolean> storeCheque(Cheque cheque) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				synchronized (insertCheque) {
					insertCheque.setString(1, cheque.getChequeId());
					insertCheque.setString(2, cheque.getIssuer().toString());
					insertCheque.setString(3, cheque.getRecipient() != null ? cheque.getRecipient().toString() : null);
					insertCheque.setDouble(4, cheque.getAmount());
					insertCheque.setLong(5, cheque.getIssueTime());

					return insertCheque.executeUpdate() > 0;
				}
			} catch (SQLException e) {
				BankLog.error("Error storing cheque: " + e.getMessage());
				e.printStackTrace();
				return false;
			}
		});
	}

	@Override
	public CompletableFuture<Cheque> getCheque(String chequeId) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				synchronized (selectCheque) {
					selectCheque.setString(1, chequeId);

					try (ResultSet resultSet = selectCheque.executeQuery()) {
						if (resultSet.next()) {
							UUID issuer = UUID.fromString(resultSet.getString("issuer"));
							String recipientStr = resultSet.getString("recipient");
							UUID recipient = recipientStr != null ? UUID.fromString(recipientStr) : null;
							double amount = resultSet.getDouble("amount");
							long issueTime = resultSet.getLong("issue_time");
							boolean redeemed = resultSet.getBoolean("redeemed");
							String redeemedByStr = resultSet.getString("redeemed_by");

							Cheque cheque = new Cheque(chequeId, issuer, recipient, amount, issueTime);

							if (redeemed) {
								UUID redeemedBy = redeemedByStr != null ? UUID.fromString(redeemedByStr) : null;
								cheque.redeem(redeemedBy);
							}

							return cheque;
						}
					}
				}

				return null;

			} catch (SQLException e) {
				BankLog.error("Error getting cheque: " + e.getMessage());
				e.printStackTrace();
				return null;
			}
		});
	}

	@Override
	public CompletableFuture<Boolean> markChequeRedeemed(String chequeId, String redeemedBy) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				synchronized (updateRedeemed) {
					updateRedeemed.setString(1, redeemedBy);
					updateRedeemed.setLong(2, System.currentTimeMillis());
					updateRedeemed.setString(3, chequeId);

					return updateRedeemed.executeUpdate() > 0;
				}
			} catch (SQLException e) {
				BankLog.error("Error marking cheque as redeemed: " + e.getMessage());
				e.printStackTrace();
				return false;
			}
		});
	}

	@Override
	public CompletableFuture<Boolean> chequeExists(String chequeId) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				synchronized (checkExists) {
					checkExists.setString(1, chequeId);

					try (ResultSet resultSet = checkExists.executeQuery()) {
						return resultSet.next();
					}
				}
			} catch (SQLException e) {
				BankLog.error("Error checking if cheque exists: " + e.getMessage());
				e.printStackTrace();
				return false;
			}
		});
	}

	@Override
	public CompletableFuture<Boolean> isChequeRedeemed(String chequeId) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				synchronized (checkRedeemed) {
					checkRedeemed.setString(1, chequeId);

					try (ResultSet resultSet = checkRedeemed.executeQuery()) {
						if (resultSet.next()) {
							return resultSet.getBoolean("redeemed");
						}
						return false; // Cheque doesn't exist
					}
				}
			} catch (SQLException e) {
				BankLog.error("Error checking if cheque is redeemed: " + e.getMessage());
				e.printStackTrace();
				return false;
			}
		});
	}

	@Override
	public CompletableFuture<java.util.List<Cheque>> getAllCheques() {
		return CompletableFuture.supplyAsync(() -> {
			java.util.List<Cheque> cheques = new java.util.ArrayList<>();
			try {
				synchronized (selectAllCheques) {
					try (ResultSet resultSet = selectAllCheques.executeQuery()) {
						while (resultSet.next()) {
							String chequeId = resultSet.getString("cheque_id");
							UUID issuer = UUID.fromString(resultSet.getString("issuer"));
							String recipientStr = resultSet.getString("recipient");
							UUID recipient = recipientStr != null ? UUID.fromString(recipientStr) : null;
							double amount = resultSet.getDouble("amount");
							long issueTime = resultSet.getLong("issue_time");
							boolean redeemed = resultSet.getBoolean("redeemed");
							String redeemedByStr = resultSet.getString("redeemed_by");

							Cheque cheque = new Cheque(chequeId, issuer, recipient, amount, issueTime);

							if (redeemed) {
								UUID redeemedBy = redeemedByStr != null ? UUID.fromString(redeemedByStr) : null;
								cheque.redeem(redeemedBy);
							}

							cheques.add(cheque);
						}
					}
				}
			} catch (SQLException e) {
				BankLog.error("Error getting all cheques: " + e.getMessage());
				e.printStackTrace();
			}
			return cheques;
		});
	}

	@Override
	public void close() {
		closeStatements();
	}
}
