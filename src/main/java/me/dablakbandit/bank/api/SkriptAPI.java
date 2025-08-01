package me.dablakbandit.bank.api;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.ExpressionType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

public class SkriptAPI {

	private static final SkriptAPI api = new SkriptAPI();

	public static SkriptAPI getInstance() {
		return api;
	}

	private SkriptAPI() {
	}

	public void init() {
		Skript.registerExpression(ExpressionPlayerExp.class, Double.class, ExpressionType.PROPERTY, "exp of %player% bank");
		Skript.registerExpression(ExpressionPlayerExp.class, Double.class, ExpressionType.PROPERTY, "money of %player% bank");
	}

	static class ExpressionPlayerExp extends SimplePropertyExpression<Player, Double> {
		@Override
		protected String getPropertyName() {
			return "bankexp";
		}

		public Class<? extends Double> getReturnType() {
			return Double.class;
		}

		@Override
		public Double convert(Player arg0) {
			return BankAPI.getInstance().getExp(arg0);
		}

		@Override
		public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
			Double d = (Double) delta[0];
			Player p = getExpr().getSingle(e);
			if (p == null || d == null) {
				return;
			}
			BankAPI.getInstance().setExp(p, d);
		}

		@Override
		public Class<?>[] acceptChange(Changer.ChangeMode mode) {
			if (mode == Changer.ChangeMode.SET) {
				return new Class[]{Double.class};
			}
			return null;
		}
	}

	static class ExpressionPlayerMoney extends SimplePropertyExpression<Player, Double> {
		@Override
		protected String getPropertyName() {
			return "bankmoney";
		}

		public Class<? extends Double> getReturnType() {
			return Double.class;
		}

		@Override
		public Double convert(Player arg0) {
			return BankAPI.getInstance().getMoney(arg0);
		}

		@Override
		public void change(Event e, Object[] delta, Changer.ChangeMode mode) {
			Double d = (Double) delta[0];
			Player p = getExpr().getSingle(e);
			if (p == null || d == null) {
				return;
			}
			BankAPI.getInstance().setMoney(p, d);
		}

		@Override
		public Class<?>[] acceptChange(Changer.ChangeMode mode) {
			if (mode == Changer.ChangeMode.SET) {
				return new Class[]{Double.class};
			}
			return null;
		}
	}
}
