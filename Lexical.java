public class Lexical {
	public String token; // Type of token
	public String lexeme; // The lexeme
	public int nval;
	public String sval;
	public Lexical left;
	public Lexical right;

	public Lexical(String token, String lexeme,int nval) {
		this.token = token;
		this.lexeme = lexeme;
		this.nval = nval;
	}
	
	public Lexical(String t, Lexical l, Lexical r){
		this.token = t;
		this.left = l;
		this.right = r;
	}

	
	// Returns the type of the token
	public String getTokenType() {
		return token;
	}

	// Returns the lexeme of the token
	public String getLexeme() {
		return lexeme;
	}

	// Returns a string representation of the token
	public String toString() {
		return token + "\t" + lexeme;
	}
}
