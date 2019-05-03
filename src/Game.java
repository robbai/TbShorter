import java.util.Arrays;

public class Game {
	
	/**
	 * Excluding the king
	 */
	public static final char[] pieceChars = new char[] {'p', 'n', 'b', 'r', 'q'};
	
	public String[] data;

	public Game(String[] data){
		super();
		this.data = data;
	}

	@Override
	public String toString() {
		return Arrays.toString(this.data);
	}
	
	public int getPieceCount(){
		// Manipulate the FEN
		String fenMain = this.data[PgnFormat.FEN];
		fenMain = fenMain.substring(fenMain.indexOf(" ") + 2); // Skip the word "FEN"
		fenMain = fenMain.substring(0, fenMain.indexOf(" ")).toLowerCase();
		
		int count = 0;
		for(char c : pieceChars) count += Main.countChar(fenMain, c);
		
		return count;
	}
	
	public int getMoveCount(){
		String moves = this.data[PgnFormat.MOVES].replace(".", "");
		
		int count = 0;
		for(String word : moves.split(" ")){
			try{
				int val = Integer.parseInt(word);
				count = Math.max(count, val);
			}catch(Exception e){
				// Do nothing
			}
		}
		
		return count;
	}
	
	public String asTruePGN(){
		String str = "";
		for(int i = 0; i < (PgnFormat.TOTAL_LINES - 1); i++) str += this.data[i] + "\n";
		return str;
	}

}
