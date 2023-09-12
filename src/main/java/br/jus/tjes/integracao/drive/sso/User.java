package br.jus.tjes.integracao.drive.sso;

import java.util.ArrayList;

public class User {
	public String id;
	public long createdTimestamp;
	public String username;
	public boolean enabled;
	public boolean totp;
	public boolean emailVerified;
	public String firstName;
	public String email;
	public Attributes attributes;
	public ArrayList<Object> disableableCredentialTypes;
	public ArrayList<Object> requiredActions;
	public int notBefore;
	public Access access;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public long getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(long createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isTotp() {
		return totp;
	}

	public void setTotp(boolean totp) {
		this.totp = totp;
	}

	public boolean isEmailVerified() {
		return emailVerified;
	}

	public void setEmailVerified(boolean emailVerified) {
		this.emailVerified = emailVerified;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Attributes getAttributes() {
		return attributes;
	}

	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	public ArrayList<Object> getDisableableCredentialTypes() {
		return disableableCredentialTypes;
	}

	public void setDisableableCredentialTypes(ArrayList<Object> disableableCredentialTypes) {
		this.disableableCredentialTypes = disableableCredentialTypes;
	}

	public ArrayList<Object> getRequiredActions() {
		return requiredActions;
	}

	public void setRequiredActions(ArrayList<Object> requiredActions) {
		this.requiredActions = requiredActions;
	}

	public int getNotBefore() {
		return notBefore;
	}

	public void setNotBefore(int notBefore) {
		this.notBefore = notBefore;
	}

	public Access getAccess() {
		return access;
	}

	public void setAccess(Access access) {
		this.access = access;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", createdTimestamp=" + createdTimestamp + ", username=" + username + ", enabled="
				+ enabled + ", totp=" + totp + ", emailVerified=" + emailVerified + ", firstName=" + firstName
				+ ", email=" + email + ", attributes=" + attributes + ", disableableCredentialTypes="
				+ disableableCredentialTypes + ", requiredActions=" + requiredActions + ", notBefore=" + notBefore
				+ ", access=" + access + "]";
	}
}
