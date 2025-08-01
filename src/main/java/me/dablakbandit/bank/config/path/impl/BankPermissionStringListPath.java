package me.dablakbandit.bank.config.path.impl;

import me.dablakbandit.core.config.RawConfiguration;
import me.dablakbandit.core.config.path.ListPath;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class BankPermissionStringListPath extends ListPath<String> {


	private final PathPermissible permissible = new PathPermissible();

	public BankPermissionStringListPath(List<String> def) {
		super(def);
	}

	public BankPermissionStringListPath(String... def) {
		super(new ArrayList<>(Arrays.asList(def)));
	}

	protected List<String> get(RawConfiguration config, String path) {
		List<String> list = config.getStringList(path);
		permissible.setAttachmentInfo(list);
		return list;
	}

	protected Object setAs(List<String> list) {
		permissible.setAttachmentInfo(list);
		return list;
	}

	public PathPermissible getPermissible() {
		return permissible;
	}

	public class PathPermissible implements Permissible {

		private Set<PermissionAttachmentInfo> attachmentInfo;

		public void setAttachmentInfo(List<String> permissions) {
			this.attachmentInfo = permissions.stream().map(permission -> {
				return new PermissionAttachmentInfo(this, permission, null, true);
			}).collect(Collectors.toSet());
		}

		@Override
		public boolean isPermissionSet(String s) {
			return false;
		}

		@Override
		public boolean isPermissionSet(Permission permission) {
			return false;
		}

		@Override
		public boolean hasPermission(String permission) {
			return attachmentInfo.stream().anyMatch(attachmentInfo -> attachmentInfo.getValue() && attachmentInfo.getPermission().equalsIgnoreCase(permission));
		}

		@Override
		public boolean hasPermission(Permission permission) {
			return hasPermission(permission.getName());
		}

		@Override
		public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b) {
			return null;
		}

		@Override
		public PermissionAttachment addAttachment(Plugin plugin) {
			return null;
		}

		@Override
		public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i) {
			return null;
		}

		@Override
		public PermissionAttachment addAttachment(Plugin plugin, int i) {
			return null;
		}

		@Override
		public void removeAttachment(PermissionAttachment permissionAttachment) {

		}

		@Override
		public void recalculatePermissions() {

		}

		@Override
		public Set<PermissionAttachmentInfo> getEffectivePermissions() {
			return attachmentInfo;
		}

		@Override
		public boolean isOp() {
			return false;
		}

		@Override
		public void setOp(boolean b) {

		}
	}
}
