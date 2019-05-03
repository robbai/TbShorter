import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class Main {
	
//	private static final String readDir = "res/TB-Example-Data.pgn", writeDir = (readDir.replace(".pgn", "-Shortened.pgn"));
	private static final String dotPgn = ".pgn", writeSuffix = "-Shortened.pgn";

	public static void main(String[] args){
		// Handle the arguments
//		System.out.println(Arrays.toString(args));
		String readDir = "";
		try {
			readDir = args[0];
		}catch(Exception e){
			System.err.println("No PGN provided!");
			System.exit(1);
		}
		
		File file = new File(readDir);

		try(BufferedReader br = new BufferedReader(new FileReader(file))){
			ArrayList<Game> games = new ArrayList<Game>(); 
			
			String[] currentGame = new String[PgnFormat.TOTAL_LINES];
			
			// Read the file
			int lineIndex = 0;
			while(true){
				if(lineIndex != 0 && lineIndex % PgnFormat.TOTAL_LINES == 0){
					games.add(new Game(currentGame));
					currentGame = new String[PgnFormat.TOTAL_LINES];
				}
				String line = br.readLine();
				if(line == null){
					games.add(new Game(currentGame));
					break;
				}
				currentGame[lineIndex % PgnFormat.TOTAL_LINES] = line;				
				lineIndex ++;
			}
			
			String[] result = new String[games.size()];
			for(int i = 0; i < games.size(); i++){
				Game game = games.get(i);
				
				int pieceCount = game.getPieceCount();
				game.data[PgnFormat.MOVES] = attemptToShortenMoves(game, pieceCount);
				
				String truePgn = game.asTruePGN();
				System.out.println(truePgn);		
				result[i] = truePgn;
//				break;
			}
			
			writeResult(result, readDir.substring(0, readDir.length() - dotPgn.length()) + writeSuffix);
			
			br.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	public static int countChar(String str, char c){
		return (int)str.chars().filter(ch -> ch == c).count();
	}
	
	private static String attemptToShortenMoves(Game game, int pieceCount){
		String[] moves = game.data[PgnFormat.MOVES].split(" ");
			
		String result = moves[moves.length - 1];
		
		String newMoves = "";
		int captures = 0;
		for(int i = 0; i < (moves.length - 1); i++){
			String move = moves[i];
			newMoves += move + " ";
			if(move.contains("x")){
				captures ++;
				if(captures >= pieceCount) break;
			}
		}
		newMoves += result;
		
		return newMoves;
	}
	
	private static void writeResult(String[] lines, String writeDir){
		File fout = new File(writeDir);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(fout);
			
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

			for(String s : lines){
				bw.write(s);
				bw.newLine();
			}

			bw.close();
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

}
