import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;

public class Recognizer{

	private static BufferedReader reader; // Reader
	private static char curr; // The current character being scanned
	private static Lexical t;
	public static Recognizer lexer;
	private static final char EOF = (char) (-1);


	public Recognizer(File f) {
		try {
			reader = new BufferedReader(new FileReader(f));
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Read the first character
		curr = read();

	}

	private static char read() {
		try {
			return (char) (reader.read());
		} catch (IOException e) {
			e.printStackTrace();
			return EOF;
		}
	}

	// Checks if a character is a digit
	private static boolean isNumeric(char c) {
		if (c >= '0' && c <= '9')
			return true;

		return false;
	}

	public static boolean isAlpha(char c) {
		if (c >= 'a' && c <= 'z')
			return true;
		if (c >= 'A' && c <= 'Z')
			return true;

		return false;

	}

	public static Lexical nextLexical() {
		if( reader == null) return null;
		int state = 1; // Initial state
		int numBuffer = 0; // A buffer for number literals
		String alphaBuffer = "";
		int decBuffer = 0;
		while (true) {
			if (curr == EOF) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				reader = null;
				return new Lexical("EOF","EOF",0);
			}
			switch (state) {
			// Controller for each state
			case 1:
				switch (curr) {
				case ' ': // Whitespaces
				case '\n':
				case '\b':
				case '\f':
				case '\r':
				case '\t':
					curr = read();
					continue;

				case ';':
					curr = read();
					return new Lexical("SEMICOLON", ";",0);
				case '.':
					curr = read();
					return new Lexical("DOT", ".",0);
				case '*':
					curr = read();
					return new Lexical("TIMES", "*",0);
				case '/':
					curr = read();
					state = 14;
					continue;
				case ',':
					curr = read();
					return new Lexical("COMMA", ",",0);
				case ':':
					curr = read();
					return new Lexical("COLON", ":",0);
				case '(':
					curr = read();
					return new Lexical("OPAREN", "(",0);
				case ')':
					curr = read();
					return new Lexical("CPAREN", ")",0);
				case '{':
					curr = read();
					return new Lexical("OBRACE", "{",0);
				case '}':
					curr = read();
					return new Lexical("CBRACE", "}",0);
				case '%':
					curr = read();
					return new Lexical("MOD", "%",0);
				case '=':
					curr = read();
					state = 8;
					continue;
				case '>':
					curr = read();
					state = 81;
					continue;
				case '<':
					curr = read();
					state = 82;
					continue;
				case '+':
					curr = read();
					state = 80;
					continue;
				case '-':
					curr = read();
					state = 83;
					continue;

				case '!':
					curr = read();
					state = 9;
					continue;
				case '&':
					curr = read();
					state = 10;
					continue;
				case '|':
					curr = read();
					state = 11;
					continue;
				case '"':
					curr = read();
					state = 13;
					alphaBuffer = "";
					continue;

				default:
					state = 2; // Check the next possibility
					continue;
				}


				// Integer - Start
			case 2:
				if (isNumeric(curr)) {
					numBuffer = 0; // Reset the buffer.
					numBuffer += (curr - '0');

					state = 3;

					curr = read();

				} else {
					state = 5; // does not start with number or symbol go to
					// case 5
				}
				continue;

				// Integer - Body
			case 3:
				if (isNumeric(curr)) {
					numBuffer *= 10;
					numBuffer += (curr - '0');

					curr = read();

				} else if (curr == '.') {

					curr = read();

					state = 4; // has decimal point go to case 4

				} else {
					return new Lexical("INTEGER", "" + numBuffer, numBuffer);
				}

				continue;

			case 4:
				if (isNumeric(curr)) {
					decBuffer = 0;
					decBuffer += (curr - '0');
					state = 7;
					curr = read();

				} else {
					return new Lexical("ERROR", "Invalid input: " + numBuffer
							+ ".",0);
				}
				continue;
			case 7:
				if (isNumeric(curr)) {
					decBuffer *= 10;
					decBuffer += (curr - '0');

					curr = read();
				} else {
					return new Lexical("INTEGER", "" + numBuffer + "."
							+ decBuffer,0);
				}
				continue;

			case 5:
				if (isAlpha(curr) || curr == '_') {
					alphaBuffer = "";
					alphaBuffer += curr;
					state = 6;
					curr = read();
				} else {
					alphaBuffer = "";
					alphaBuffer += curr;
					curr = read();
					return new Lexical("ERROR", "Invalid input:" + alphaBuffer,0);
				}
				continue;

			case 6:
				if ((isAlpha(curr) || isNumeric(curr) || curr == '_')) {

					alphaBuffer += curr;
					curr = read();

					} 
				else {

					if (alphaBuffer.equals("class")
							|| alphaBuffer.equals("def")
							|| alphaBuffer.equals("assert")
							|| alphaBuffer.equals("and")
							|| alphaBuffer.equals("exec")
							|| alphaBuffer.equals("global")
							|| alphaBuffer.equals("import")
							|| alphaBuffer.equals("not")
							|| alphaBuffer.equals("pass")
							|| alphaBuffer.equals("and")
							|| alphaBuffer.equals("break")
							|| alphaBuffer.equals("return")
							|| alphaBuffer.equals("continue")
							|| alphaBuffer.equals("in")
							|| alphaBuffer.equals("try")
							|| alphaBuffer.equals("yield")
							|| alphaBuffer.equals("raise")
							|| alphaBuffer.equals("is")
							|| alphaBuffer.equals("as")
							|| alphaBuffer.equals("del")
							|| alphaBuffer.equals("elif")
							|| alphaBuffer.equals("except")
							|| alphaBuffer.equals("finally")
							|| alphaBuffer.equals("from")
							|| alphaBuffer.equals("or")
							//|| alphaBuffer.equals("print")
							|| alphaBuffer.equals("with")
							|| alphaBuffer.equals("return")
							) {
						return new Lexical("KEYWORD", "" + alphaBuffer,0);

						} 
					else if (alphaBuffer.equals("true")
							|| alphaBuffer.equals("false")) {
						return new Lexical("BOOLEAN", "" + alphaBuffer,0);
						}
					else if (alphaBuffer.equals("lambda")){ 
						return new Lexical("LAMBDA", "" + alphaBuffer,0);}
					else if (alphaBuffer.equals("var")){ 
						return new Lexical("VAR", "" + alphaBuffer,0);}
					else if (alphaBuffer.equals("while")){
						return new Lexical("WHILE", "" + alphaBuffer,0);
						}
					else if (alphaBuffer.equals("if")){
						return new Lexical("IF", "" + alphaBuffer,0);
						}
					else if(alphaBuffer.equals("elseif")){
						return new Lexical("ELSEIF",""+alphaBuffer,0);}
					else if (alphaBuffer.equals("else")){
						return new Lexical("ELSE", "" + alphaBuffer,0);
						}
					else if(alphaBuffer.equals("print")){
						return new Lexical("PRINT",""+alphaBuffer,0);
						}
					else{
						return new Lexical("ID", "" + alphaBuffer,0);
						}
					}
				continue;

				// if ==
			case 8:
				if (curr == '=') {
					curr = read();
					return new Lexical("EQUALS", "==",0);
					} 
				else {
					return new Lexical("ASSIGN", "=",0);
					}
			case 82:
				if (curr == '=') {
					curr = read();
					return new Lexical("LTE", "<=",0);
					} 
				else {

					return new Lexical("LT", "<",0);
					}
			case 81:
				if (curr == '=') {
					curr = read();
					return new Lexical("GTE", ">=",0);
					}
				else {
					return new Lexical("GT", ">",0);
					}
				// if ++
			case 80:
				if (curr == '+') {
					curr = read();
					return new Lexical("PLUSPLUS", "++",0);
					} 
				else {
					return new Lexical("PLUS", "+", 0);
					}
			case 83:
				if (curr == '-') {
					curr = read();
					return new Lexical("MINUSMINUS", "--",0);
					} 
				else {

					return new Lexical("MINUS", "-",0);
					}
				// if !=
			case 9:
				if (curr == '=') {
					curr = read();
					return new Lexical("NEQUALS", "!=",0);
					} 
				else {
					return new Lexical("NOT", "!",0);
					}

				// if &&
			case 10:
				if (curr == '&') {
					curr = read();
					return new Lexical("AND", "&&", 0);
				} else {
					return new Lexical("ERROR", "Invalid input: &",0);
				}
				// if ||
			case 11:
				if (curr == '|') {
					curr = read();
					return new Lexical("OR", "||",0);
				} else {
					return new Lexical("ERROR", "Invalid input: |",0);
				}

			case 13:
				if (curr == '"') {
					curr = read();
					return new Lexical("STRING", "\"" + alphaBuffer + "\"", 0);
				} else if (curr == '\n' || curr == EOF) {
					curr = read();
					return new Lexical("ERROR", "Invalid string literal", 0);
				} else {
					alphaBuffer += curr;
					curr = read();
				}
				continue;
			case 14:
				if (curr == '/') {
					state = 15;
					curr = read();
				} else if (curr == '*') {
					state = 16;
					curr = read();
				} else {
					return new Lexical("DIVIDE", "/", 0);
				}
				continue;
			case 15:
				if (curr == '\n') {

					state = 1;
				}
				curr = read();
				continue;
			case 16:
				if (curr == '*') {
					state = 17;

				}
				curr = read();
				continue;
			case 17:
				if (curr == '/') {
					curr = read();
					state = 1;
				} else {
					curr = read();
					state = 16;
				}
				continue;
			}
		}
	}

	public static Boolean check(String type) {
		return t.token == type;
	}

	public static void advance() {
		t = Recognizer.nextLexical();
		if (t.token == "EOF") {
			System.out.println("LEGAL");
		}
	}

	public static void match(String type) {
		if (!check(type)) {
			System.out.println("ERROR ILLEGAL Lexical!" + " " + t + ", Looking For " + type);
			System.exit(1);
		}
		advance();
	}

	public static void program() {
		//System.out.println("in program");
		if (definitionpending()) {
			//System.out.println("in definition");
			definition();
		}
		else{
			//System.out.println("in statement");
			statement();			
			}
		if(programpending()){
			program();	
			}
	}

	public static void definition() {
		if (funcDefpending()) {
			funcDef();
			}
		else{
			varDecl();
			}
				
		}
	public static Boolean programpending(){
			return (definitionpending()) || (statementPending());
			}	
	
	public static Boolean definitionpending() {
			return (funcDefpending()) || (varDeclpending());
		}
	
	public static Boolean varExpressionpending(){
			return (check("ID"));
		}

	public static Boolean expressionpending() {
			return (unarypending());
		}
	
	public static Boolean unarypending(){
			return (check("INTEGER")) || (check("ID")) || (check("STRING")) || (definitionpending()) || (check("PRINT")) || (check("OPAREN"));
			}

	public static Boolean funcDefpending() {
			return (check("KEYWORD"));
		}

	public static Boolean varDeclpending() {
			return (check("VAR"));
		}

	public static void varDecl() {
			match("VAR");
			match("ID");
			operator();
			expression();
				
		}

	public static void funcDef() {
		if (check("KEYWORD")){
			match("KEYWORD");
			match("ID");
			match("OPAREN");
			optParamList();
			block();
			}
		else{
			lambda();		
			}		
		}

	public static void lambda(){
		match("LAMBDA");
		match("OPAREN");
		optParamList();
		block();
		}

	public static void optParamList() {
		if (check("CPAREN")) {
			match("CPAREN");
			}
		else{
			paramList();
			}
		}

	public static Boolean paramListPending() {
		return check("ID");
		}

	public static Boolean optParamListpending() {
		return (paramListPending());
		}

	public static void paramList() {
		match("ID");
		if (check("COMMA")) {
			match("COMMA");
			paramList();
			}
		else{
			match("CPAREN");
			}
		}
	//public static void varExpression(){
		//if(check("ID")){
		//	match("ID");
		//	if(check("OPAREN")){
		//		match("OPAREN");
		//		list();
		//		}	
		//	}
		//else{
		//	match("OPAREN");
		//	expression();
		//	expression();
		//	match("CPAREN");
		//	}
		//}
	//public void numeric() {
	//	if (check("INTEGER")) {
	//		match("INTEGER");
	//	} else {
	//		match("ID");
	//	}
	//
	public static void list() {
		expression();
		if (check("COMMA")) {
			match("COMMA");
			list();
			}
		else {
			match("CPAREN");
			}
			
		}

	//public static Boolean expressionpending() {
	//	return ((primarypending() || functioncallpending()));
	//}

	//public static Boolean functioncallpending() {
	//	return (check("ID")) || (check("KEYWORD"));
	//	}

	/*public static void functioncall() {
		if (check("ID")) {
			match("ID");
			match("OPAREN");
			optionalList();
			match("CPAREN");
		}
		else{
			if(check("KEYWORD")){
				match("KEYWORD");
				match("STRING");
			}
		}
	}*/

	public static Boolean listpending() {
		return (expressionpending());
		}

	public static void expression(){
		//	System.out.println("in expression");
			unary();
			if (operatorPending()) {
		//		System.out.println("in operatorpending");
				operator();
				expression();
				} 
		}

	//public static Boolean unarypending() {
		//return check("INTEGER") || definitionpending() || check("OPAREN") 
		//		|| check("ID");
		//}

	public static void operator(){
		if (check("PLUS")) {
			match("PLUS");
			} 
		else if (check("TIMES")){
			match("TIMES");
			}
		else if (check("MINUS")){
			match("MINUS");
			} 
		else if (check("DIVIDE")){
			match("DIVIDE");
			} 
		else if (check("AND")){
			match("AND");
			} 
		else if (check("OR")){
			match("OR");
			} 
		else if (check("LT")){
			match("LT");
			} 
		else if (check("GT")){
			match("GT");
			}
		else if (check("LTE")){
			match("LTE");
			} 
		else if (check("GTE")){
			match("GTE");
			} 
		else if (check("ASSIGN")){
			match("ASSIGN");
			} 
		else if (check("EQUALS")){
			match("EQUALS");
			} 
		else {
			match("NEQUALS");
			}
		}

	public static void unary() {
		//System.out.println("in unary");
		if (check("INTEGER")) {
			match("INTEGER");
			}
		else if(varExpressionpending()){
			varExpression();
			}
		else if(check("STRING")){
			match("STRING");		
			}
		else if(check("PRINT")){
			match("PRINT");
			match("OPAREN");
			list();	
			}
		else if (definitionpending()) {
			definition();
			} 
		else {
			match("OPAREN");
			expression();
			match("CPAREN");
			if(check("OPAREN")){
				match("OPAREN");
				optList();
				}
			}
		}

	public static void varExpression() {
		//System.out.println("in varexpression");
		match("ID");
		if (check("OPAREN")) {
			//System.out.println("in function call");
			match("OPAREN");
			optList();
			//match("CPAREN");
			}
			
		}

	// static Boolean varExpressionPending() {
	//	return check("ID");
	//}

	//public static void optExpressionList() {
	//	primary();
	//	if (operatorPending()) {
	//		operator();
	//		expression();
	//	}
	//}
	public static void optList() {
		if (check("CPAREN")) {
			match("CPAREN");
			}
		else{
			list();
			}
		}
	public static Boolean operatorPending() {
		return check("PLUS") || check("TIMES") || check("MINUS") || check("DIVIDE")
				|| check("AND") || check("OR") || check("LT") || check("GT")
				|| check("LTE") || check("GTE") || check("EQUALS")
				|| check("NEQUALS")|| check("ASSIGN");
	}

	public static void block() {
		match("OBRACE");
		statementList();
		match("CBRACE");
	}

	public static void statementList() {
		statement();
		if (statementPending()) {
			statementList();
		}
	}

	public static Boolean statementPending() {
		return (expressionpending()) || (definitionpending()) || (ifStatementpending()) 
				|| (whileStatementpending());
	}

	public static void statement() {
		if (expressionpending()) {
			expression();
		} else if (ifStatementpending()) {
			ifStatement();
		} else if (whileStatementpending()) {
			whileStatement();
		} else {
			definition();
		}
	}

	public static Boolean ifStatementpending() {
		return (check("IF"));
	}

	public static Boolean whileStatementpending() {
		return (check("WHILE"));
	}

	public static void ifStatement() {
		match("IF");
		expression();
		block();
		optElse();
	}

	public static void optElse() {
		if (check("ELSE")) {
			match("ELSE");
			block();
			if (ifStatementpending()){
				ifStatement();						
				}
		}
	}

	//public void optInit() {
	//	if (check("ASSIGN")) {
	//		match("ASSIGN");
	//		expression();
	//	}
	//}

	public static void Recognize(File f) {
		while ((t = Recognizer.nextLexical()) != null) {
			//System.out.println(t);
			StartingLexical();
		}
	}

	public static void whileStatement() {
		match("WHILE");
		expression();
		block();
	}

	public static void StartingLexical() {
		//if (t.token.equals("ID")) {
		//	varDecl();
		//	} 
		//else if (t.token.equals("WHILE")) {
		//	expression();
		//} else if (t.token.equals("IF")) {
		//	ifStatement();
		//} else if (t.token.equals("FUNCDEF")) {
		//	funcDef();
		//} else if (t.token.equals("FUNCTIONCALL")) {
		//	varExpression();			
			//functioncall();
		//} 
		//else
		//System.out.println(t.token); 
		if (programpending()) {
			//System.out.println("in if statement");
			program();
			} 
		
		else {
			System.out.println("ILLEGAL" + " " + t.token + " " + t.lexeme);
			System.exit(1);
			}
		}

	public static void main(String[] args) throws NullPointerException{

		String inFile = args[0];
		String outFile = "/dev/stdout";
		File f = new File(inFile);
		new Recognizer(f);
		Recognize(f);

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
