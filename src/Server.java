import java.io.*;
import java.net.*;

public class Server {
	
	public static void main(String arg[]) throws Exception{

		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(4321);
		} catch (IOException e) {
			System.out.println("Error: " + e);
			System.exit(0);
		}
		while(true){
			GameLogic a = new GameLogic();
			a.clientSocket=serverSocket.accept();
			Thread t = new Thread(a);
			t.start();
		}
	}
}