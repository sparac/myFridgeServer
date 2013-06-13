package hr.fer.ztel.myFridge.security;

import java.text.ParseException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;

public class BasicAuthorizationHeader {
	private static final String SUPPORTED_METHOD = "BASIC";

	private String username;
	private String password;

	public BasicAuthorizationHeader() {

	}

	public BasicAuthorizationHeader(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "BasicAuthorizationHeader [username=" + username + ", password=" + password + "]";
	}

	public static BasicAuthorizationHeader newInstance(String header) throws ParseException {

		if (header == null || header.isEmpty()) {
			throw new ParseException("Header empty", 0);
		}

		String[] headerParts = header.split(" ", -1);
		if (headerParts.length < 2) {
			throw new ParseException("Invalid header format", 0);
		}

		if (!SUPPORTED_METHOD.equalsIgnoreCase(headerParts[0])) {
			throw new ParseException("Invalid authentication method", 0);
		}

		String credentials = StringUtils.newStringUtf8(Base64.decodeBase64(headerParts[1]));
		if (credentials.indexOf(':') < 0) {
			throw new ParseException("Invalid credentials format", 0);
		}

		String username = credentials.substring(0, credentials.indexOf(':'));
		String password = credentials.substring(credentials.indexOf(':') + 1);

		return new BasicAuthorizationHeader(username, password);
	}
}
