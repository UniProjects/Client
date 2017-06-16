package org.popov.belezirev.client;

public class Credentials<USERNAME, PASSWORD> {
	private final USERNAME username;
	private final PASSWORD password;

	public Credentials(USERNAME username, PASSWORD password) {
		this.username = username;
		this.password = password;
	}

	public USERNAME getUsername() {
		return username;
	}

	public PASSWORD getPassword() {
		return password;
	}
}
