import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;

public class Client {
final static String host="localhost";
final static int port=4321;
static BufferedReader localread;
static BufferedReader FromServerIn;
static PrintWriter toServerOut;

	public static void main(String arg[]) {
		Socket clientsocket;
		
		try{
			clientsocket =new Socket(InetAddress.getByName(host),port);
			
			FromServerIn=new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));

			toServerOut = new PrintWriter(clientsocket.getOutputStream(),true);
			
			localread=new BufferedReader(new InputStreamReader(System.in));
			
			String tmp=FromServerIn.readLine();
			
			while(tmp!=null){
				if(tmp.equals("!@#$")){
					toServerOut.println(localread.readLine());
					tmp=FromServerIn.readLine();
				}
				else if(tmp.equals("!@#$")){
					//Syste.out.print();
				}
				else{
					System.out.println(tmp);	
					tmp=FromServerIn.readLine();
				}
			}
			
			FromServerIn.close();
			clientsocket.close();
		}
		catch(Exception ex){
			
		}
		finally{

		}
	}
}