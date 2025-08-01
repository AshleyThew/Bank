package me.dablakbandit.bank.database.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import me.dablakbandit.bank.database.base.IChequeDatabase;
import me.dablakbandit.bank.implementations.cheque.Cheque;
import me.dablakbandit.bank.log.BankLog;
import org.bson.Document;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ChequeMongoDatabase implements IChequeDatabase {

	private final MongoCollection<Document> collection;

	public ChequeMongoDatabase(MongoCollection<Document> collection) {
		this.collection = collection;
		collection.createIndex(new Document("cheque_id", 1));
		collection.createIndex(new Document("redeemed", 1));
		collection.createIndex(new Document("expiry_time", 1));
	}

	@Override
	public CompletableFuture<Boolean> storeCheque(Cheque cheque) {
		return CompletableFuture.supplyAsync(() -> {
			try {
				Document doc = new Document().append("cheque_id", cheque.getChequeId()).append("issuer", cheque.getIssuer().toString()).append("recipient", cheque.getRecipient() != null ? cheque.getRecipient().toString() : null).append("amount", cheque.getAmount()).append("issue_time", cheque.getIssueTime()).append("redeemed", false).append("redeemed_by", null).append("redeemed_time", 0L);

				collection.insertOne(doc);
				return true;

			} catch (Exception e) {
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
				Document doc = collection.find(Filters.eq("cheque_id", chequeId)).first();

				if (doc != null) {
					UUID issuer = UUID.fromString(doc.getString("issuer"));
					String recipientStr = doc.getString("recipient");
					UUID recipient = recipientStr != null ? UUID.fromString(recipientStr) : null;
					double amount = doc.getDouble("amount");
					long issueTime = doc.getLong("issue_time");
					boolean redeemed = doc.getBoolean("redeemed", false);
					String redeemedByStr = doc.getString("redeemed_by");

					Cheque cheque = new Cheque(chequeId, issuer, recipient, amount, issueTime);

					if (redeemed) {
						UUID redeemedBy = redeemedByStr != null ? UUID.fromString(redeemedByStr) : null;
						cheque.redeem(redeemedBy);
					}

					return cheque;
				}

				return null;

			} catch (Exception e) {
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
				long now = System.currentTimeMillis();
				return collection.updateOne(Filters.and(Filters.eq("cheque_id", chequeId), Filters.eq("redeemed", false)), Updates.combine(Updates.set("redeemed", true), Updates.set("redeemed_by", redeemedBy), Updates.set("redeemed_time", now))).getModifiedCount() > 0;

			} catch (Exception e) {
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
				return collection.countDocuments(Filters.eq("cheque_id", chequeId)) > 0;

			} catch (Exception e) {
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
				Document doc = collection.find(Filters.eq("cheque_id", chequeId)).projection(new Document("redeemed", 1)).first();

				if (doc != null) {
					return doc.getBoolean("redeemed", false);
				}
				return false; // Cheque doesn't exist

			} catch (Exception e) {
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
				for (Document doc : collection.find()) {
					String chequeId = doc.getString("cheque_id");
					UUID issuer = UUID.fromString(doc.getString("issuer"));
					String recipientStr = doc.getString("recipient");
					UUID recipient = recipientStr != null ? UUID.fromString(recipientStr) : null;
					double amount = doc.getDouble("amount");
					long issueTime = doc.getLong("issue_time");
					boolean redeemed = doc.getBoolean("redeemed", false);
					String redeemedByStr = doc.getString("redeemed_by");

					Cheque cheque = new Cheque(chequeId, issuer, recipient, amount, issueTime);

					if (redeemed) {
						UUID redeemedBy = redeemedByStr != null ? UUID.fromString(redeemedByStr) : null;
						cheque.redeem(redeemedBy);
					}

					cheques.add(cheque);
				}
			} catch (Exception e) {
				BankLog.error("Error getting all cheques: " + e.getMessage());
				e.printStackTrace();
			}
			return cheques;
		});
	}

	@Override
	public void close() {
		// MongoDB client connection is managed by the core database system
	}
}
