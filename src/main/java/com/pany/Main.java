package com.pany;

import com.pany.server.TelnetServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static void main(String[] args) throws IOException {
		var context = new AnnotationConfigApplicationContext("com.pany");
		var server = context.getBean(TelnetServer.class);
		server.start();
		System.out.printf("Server started on port %d%n", server.getPort());
		System.out.println("Press enter to stop the server.");
		var bufferRead = new BufferedReader(new InputStreamReader(System.in));
		bufferRead.readLine();
		server.shutdown();
		bufferRead.close();
	}

}
