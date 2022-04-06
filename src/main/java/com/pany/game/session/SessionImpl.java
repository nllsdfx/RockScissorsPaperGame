package com.pany.game.session;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class SessionImpl implements Session {

	private final Socket socket;
	private final OutputStreamWriter writer;
	private final BufferedReader reader;
	private String name;

	public SessionImpl(Socket socket) throws IOException {
		this.socket = socket;
		this.writer = new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8);
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	@Override
	public void sendMessage(String message) {
		try {
			writer.write(message);
			writer.flush();
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public String readMessage() {
		try {
			return reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void disconnect() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof SessionImpl session)) {
			return false;
		}
		return Objects.equals(getName(), session.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getName());
	}
}
