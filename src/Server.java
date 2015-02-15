import java.net.*;
import java.io.*;

public class Server {
	public static void main(String[] args) {
		int port = Integer.parseInt(args[0]);

		try (
			ServerSocket server = new ServerSocket(port);
			Socket clientSocket = server.accept();
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(
				new InputStreamReader(clientSocket.getInputStream()));
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		) {
			String input;
			while ((input = stdIn.readLine()) != null) {
				out.println(input);
				System.out.println(in.readLine());
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}