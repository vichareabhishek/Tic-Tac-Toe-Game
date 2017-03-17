import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameLogic implements Runnable{
	BufferedReader readlocal=new BufferedReader(new InputStreamReader(System.in));

	char ServerSymbol='X';
	char ClientSymbol='O';
	int CurrentPlayer=0;
	
	Socket clientSocket=null;
	
	BufferedReader in=null;
	PrintWriter out=null;
	
	String board[][]={
			{" "," "," "},
			{" "," "," "},
			{" "," "," "}
			};
	
	
	@Override
	public void run() {
		try{
			out = new PrintWriter(clientSocket.getOutputStream(),true);
			in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			playGame();
			clientSocket.close();
		}
		catch(Exception ex){
			ex.printStackTrace();
		}
	}

	public GameLogic() {
	}

	void toClient(String s){
		out.print(s);
	}
	
	void toClientln(String s){
		out.println(s);
	}
	
	boolean insertSymbol(int x,int y, char symbol){
		if(board[x][y]==""){
			board[x][y]=String.valueOf(symbol);
			return true;
		}
		else
			return false;
	}

	String fromClient() throws IOException{
		return in.readLine();
	}
	
	int[][] CheckWinStatus(String pattern){
		
		if((board[0][0]+board[0][1]+board[0][2]).equals(pattern))
			return new int [][]{{0,0},{0,1},{0,2}};
		
		else if((board[1][0]+board[1][1]+board[1][2]).equals(pattern))
			return new int [][]{{1,0},{1,1},{1,2}};
			
		else if((board[2][0]+board[2][1]+board[2][2]).equals(pattern))
			return new int [][]{{2,0},{2,1},{2,2}};
		
		else if((board[0][0]+board[1][0]+board[2][0]).equals(pattern))
			return new int [][]{{0,0},{1,0},{2,0}};
			
		else if((board[0][1]+board[1][1]+board[2][1]).equals(pattern))
			return new int [][]{{0,1},{1,1},{2,1}};
			
		else if((board[0][2]+board[1][2]+board[2][2]).equals(pattern))
			return new int [][]{{0,2},{1,2},{2,2}};
		
		else if((board[0][0]+board[1][1]+board[2][2]).equals(pattern))
			return new int [][]{{0,0},{1,1},{2,2}};
			
		else if((board[0][2]+board[1][1]+board[2][0]).equals(pattern))
			return new int [][]{{0,2},{1,1},{2,0}};
		else
			return null;
	}
	void DisplayBoard(){
		toClientln("--------");
		for(int i=0;i<3;i++){
			toClient("|");
			for(int j=0;j<3;j++){
				toClient(board[i][j]+" ");
			}
			toClient("|");
			toClientln("");
		}
		toClientln("--------");
	}
	
	void playGame(){
		for(int i=1;i<=9;i++){
			if (i%2==0){
					machineMove();
				if(i>=5 && CheckWinStatus(String.valueOf(ServerSymbol)+String.valueOf(ServerSymbol)+String.valueOf(ServerSymbol))!=null){
					toClientln("Machine won..!");
				DisplayBoard();
				break;
				}
			}
			else{
				manMove();
				if(i>=5 && CheckWinStatus(String.valueOf(ClientSymbol)+String.valueOf(ClientSymbol)+String.valueOf(ClientSymbol))!=null){
					toClient("Client won..!");
				DisplayBoard();
				break;
				
				}
			}
			if(i==9)
				toClientln("It's a tie..!");
			DisplayBoard();
			
		}
	}
	
	void manMove(){
		int x,y;
		toClientln("Please Enter Place to Enter Symbol");
		
		try {
			//int x=Integer.parseInt(readlocal.readLine())-1;
			//int y=Integer.parseInt(readlocal.readLine())-1;
			
			//if((!board[x][y].equals("X")) || (!board[x][y].equals("Y")))
			//insertSymbol(Integer.parseInt(readlocal.readLine())-1, Integer.parseInt(readlocal.readLine())-1, ClientSymbol);
			//else
			toClientln("!@#$");
			x=Integer.parseInt(fromClient())-1;
			toClientln("!@#$");
			y=Integer.parseInt(fromClient())-1;
			
				
				while((board[x][y].equals("X")) || (board[x][y].equals("O"))){
				toClientln("Please Select Other Place to Enter Symbol");
				toClientln("!@#$");
				x=Integer.parseInt(fromClient())-1;
				toClientln("!@#$");
				y=Integer.parseInt(fromClient())-1;
			}
				board[x][y]=String.valueOf(ClientSymbol);
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	void machineMove(){
		int location[]=getBestLocations(getPossibleLocations());
		if(location==null){
			toClientln("It's a tie..!");
			System.exit(0);
		}
		else
			board[location[0]][location[1]]=String.valueOf(ServerSymbol);
	}
	
	List getPossibleLocations(){
		List tmpList=new ArrayList();

		if((board[0][0]+board[0][1]+board[0][2]).indexOf(ClientSymbol)<0){
			//toClient("{0,0},{0,1},{0,2}");
			tmpList.add(new int [][]{{0,0},{0,1},{0,2}});	
		}

		if((board[1][0]+board[1][1]+board[1][2]).indexOf(ClientSymbol)<0){
			tmpList.add(new int [][]{{1,0},{1,1},{1,2}});
			//toClient("{1,0},{1,1},{1,2}");
		}
	
		if((board[2][0]+board[2][1]+board[2][2]).indexOf(ClientSymbol)<0){
			tmpList.add(new int [][]{{2,0},{2,1},{2,2}});
			//toClient("{2,0},{2,1},{2,2}");
		}
			
		if((board[0][0]+board[1][0]+board[2][0]).indexOf(ClientSymbol)<0){
			//toClient("{0,0},{1,0},{2,0}");
			tmpList.add(new int [][]{{0,0},{1,0},{2,0}});	
		}
	
		if((board[0][1]+board[1][1]+board[2][1]).indexOf(ClientSymbol)<0){
			tmpList.add(new int [][]{{0,1},{1,1},{2,1}});
			//toClient("{0,1},{1,1},{2,1}");
		}
			
		if((board[0][2]+board[1][2]+board[2][2]).indexOf(ClientSymbol)<0){
			tmpList.add(new int [][]{{0,2},{1,2},{2,2}});
			//toClient("{0,2},{1,2},{2,2}");
		}
			
		if((board[0][0]+board[1][1]+board[2][2]).indexOf(ClientSymbol)<0){
			tmpList.add(new int [][]{{0,0},{1,1},{2,2}});
			//toClient("{0,0},{1,1},{2,2}");
		}
			
		if((board[0][2]+board[1][1]+board[2][0]).indexOf(ClientSymbol)<0){
			tmpList.add(new int [][]{{0,2},{1,1},{2,0}});
			//toClient("{0,2},{1,1},{2,0}");
		}
			
	
		if (tmpList.isEmpty())
			return null;
		else
			return tmpList;

	}
	
	int[] getBestLocations(List locationList){
		int location[][]=null;
		String row="";
		
		if(locationList!=null){
		//Check for 2 complete Symbol
		for(int x=0;x<=locationList.size()-1;x++){
			
			location=(int[][])locationList.get(x);
			row="";
			for(int i=0; i<=2;i++){
				row+=(board[location[i][0]][location[i][1]]);
			}

			if(getCharacterCount(row, ServerSymbol)==2){
					System.out.println(row);
					if(row.charAt(0)!=ServerSymbol)
						return new int[]{location[0][0],location[0][1]};
					else if(row.charAt(1)!=ServerSymbol)
						return new int[]{location[1][0],location[1][1]};
					else
						return new int[]{location[2][0],location[2][1]};
				}
			}
		
		//Check for Counter attack
		if(((int)board[0][0].toCharArray()[0]+(int)board[0][1].toCharArray()[0]+(int)board[0][2].toCharArray()[0])==190){
			//toClient("Best: {0,0},{0,1},{0,2}");
			return getEmptyLocation(new int[][]{{0,0},{0,1},{0,2}});	
		}
		
		else if(((int)board[1][0].toCharArray()[0]+(int)board[1][1].toCharArray()[0]+(int)board[1][2].toCharArray()[0])==190){
			//toClient("Best: {0,0},{0,1},{0,2}");
			return getEmptyLocation(new int[][]{{1,0},{1,1},{1,2}});
		}
		
		else if(((int)board[2][0].toCharArray()[0]+(int)board[2][1].toCharArray()[0]+(int)board[2][2].toCharArray()[0])==190){
			//toClient("Best: {2,0},{2,1},{2,2}");
			return getEmptyLocation(new int[][]{{2,0},{2,1},{2,2}});	
		}

		else if(((int)board[0][0].toCharArray()[0]+(int)board[1][0].toCharArray()[0]+(int)board[2][0].toCharArray()[0])==190){
			//toClient("Best: {0,0},{1,0},{2,0}");
			return getEmptyLocation(new int[][]{{0,0},{1,0},{2,0}});	
		}

		else if(((int)board[0][1].toCharArray()[0]+(int)board[1][1].toCharArray()[0]+(int)board[2][1].toCharArray()[0])==190){
			//toClient("Best: {0,1},{1,1},{2,1}");
			return getEmptyLocation(new int[][]{{0,1},{1,1},{2,1}});	
		}

		else if(((int)board[0][2].toCharArray()[0]+(int)board[1][2].toCharArray()[0]+(int)board[2][2].toCharArray()[0])==190){
			//toClient("Best: {0,2},{1,2},{2,2}");
			return getEmptyLocation(new int[][]{{0,2},{1,2},{2,2}});	
		}

		else if(((int)board[0][0].toCharArray()[0]+(int)board[1][1].toCharArray()[0]+(int)board[2][2].toCharArray()[0])==190){
			//toClient("Best: {0,0},{1,1},{2,2}");
			return getEmptyLocation(new int[][]{{0,0},{1,1},{2,2}});	
		}

		else if(((int)board[0][2].toCharArray()[0]+(int)board[1][1].toCharArray()[0]+(int)board[2][0].toCharArray()[0])==190){
			//toClient("Best: {0,2},{1,1},{2,0}");
			return getEmptyLocation(new int[][]{{0,2},{1,1},{2,0}});	
		}

		//Check for 1 complete Symbol
		for(int x=0;x<=locationList.size()-1;x++){
			
			location=(int[][])locationList.get(x);
			row="";
			for(int i=0; i<=2;i++){
				row+=(board[location[i][0]][location[i][1]]);
			}

			if(getCharacterCount(row, ServerSymbol)==1){
				//toClient(row);
					if(row.charAt(0)!=ServerSymbol)
						return new int[]{location[0][0],location[0][1]};
					else if(row.charAt(1)!=ServerSymbol)
						return new int[]{location[1][0],location[1][1]};
					else
						return new int[]{location[2][0],location[2][1]};
				}
			}
		
		//Check for 0 complete Symbol
		for(int x=0;x<=locationList.size()-1;x++){
			
			location=(int[][])locationList.get(x);
			row="";
			for(int i=0; i<=2;i++){
				row+=(board[location[i][0]][location[i][1]]);
			}

			if(getCharacterCount(row, ServerSymbol)==0){
				//toClient(row);
					if(row.charAt(0)!=ServerSymbol)
						return new int[]{location[0][0],location[0][1]};
					else if(row.charAt(1)!=ServerSymbol)
						return new int[]{location[1][0],location[1][1]};
					else
						return new int[]{location[2][0],location[2][1]};
				}
			}
		
		}
		else
			return null;	
		return null;
	}
	
	int[] getEmptyLocation(int[][] locations){
		if (board[locations[0][0]][locations[0][1]].equals(" "))
			return new int[]{locations[0][0],locations[0][1]};
		else if (board[locations[1][0]][locations[1][1]].equals(" "))
			return new int[]{locations[1][0],locations[1][1]};
		else if (board[locations[2][0]][locations[2][1]].equals(" "))
			return new int[]{locations[2][0],locations[2][1]};
		else
			return null;
	}
	
	int getCharacterCount(String txt,char searchchar){
		int count=0,i=-1;
		
		while((i=txt.indexOf(searchchar,i+1))>=0)
			count++;	
		return count;
	}

}