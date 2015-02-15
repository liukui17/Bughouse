import java.net.*;
import java.io.*;

public class Client {
	public static void main(String[] args) 
			throws IOException {
		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);

		try (
			Socket echoSocket = new Socket(hostName, portNumber);
			PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(
				new InputStreamReader(echoSocket.getInputStream()));
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		) {
			String input;
			while ((input = stdIn.readLine()) != null) {
				out.println(input);
				System.out.println(in.readLine());
				if (input == quit) {
					return;
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}