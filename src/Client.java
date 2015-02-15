import java.net.*;
import java.io.*;

public class Client {
	public static void main(String[] args) 
			throws IOException {
		// IP: 173.250.181.224
		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);

		try (
			Socket echoSocket = new Socket(hostName, portNumber);
			PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(
				new InputStreamReader(echoSocket.getInputStream()));
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
		) {
			String input = "";
			while (!input.equals("quit")) {
//				String read = in.readLine();
				System.out.println(in.readLine());
				input = stdIn.readLine();
				out.println(input);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}