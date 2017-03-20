import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.*;

public class Parser{

	private static BufferedReader reader; // Reader
	private static char curr; // The current character being scanned
	private static Lexical t;
	public static Parser lexer;
	private static final char EOF = (char) (-1);


	public Parser(File f) {
		try {
			reader = new BufferedReader(new FileReader(f));
			} 
		catch (Exception e) {
			e.printStackTrace();
			}
		// Read the first character
		curr = read();

		}

	private static char read() {
		try {
			return (char) (reader.read());
			} 
		catch (IOException e) {
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

	public static Lexical advance() {
		Lexical a = t;
		//Token b = program();
		t = Parser.nextLexical();
		//if (t.token == "EOF") {
		
	//System.out.println("LEGAL");
		//}
		//System.out.println(a.token);
		return a;
	}

	public static Lexical match(String type) {
		if (!check(type)) {
			System.out.println("ERROR ILLEGAL Lexical!" + " " + t + ", Looking For " + type);
			System.exit(1);
		}
		return advance();
	}

	public static Lexical program() {
		Lexical a = null;
		Lexical b = null;
		//System.out.println("in program");
		if (definitionpending()) {
		//	System.out.println("in definition");
			a = definition();
		}
		else{
		//	System.out.println("in statement");
			a = statement();			
			}
		if(programpending()){
		//	System.out.println("in program2");
			b = program();	
			}
		Lexical prog = new Lexical("PROGRAM", a, b);
		//System.out.println(a.token);
		return prog;
	}

	public static Lexical definition() {
		Lexical a = null;
		Lexical b = null;
		if (funcDefpending()) {
			a = funcDef();
			}
		else{
			a = varDecl();
			}
			Lexical definition = new Lexical("DEFINITION", a, b);
			return definition;
			
				
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
			return (check("KEYWORD"))||(check("LAMBDA"));
		}

	public static Boolean varDeclpending() {
			return (check("VAR"));
		}

	public static Lexical varDecl() {
			Lexical a = null;
			Lexical b = null;
			match("VAR");
			a = match("ID");
			operator();
			b = expression();
			Lexical varDeclaration = new Lexical("VARDECL", a, b);
			return varDeclaration;
				
		}

	public static Lexical funcDef() {
		
		Lexical a = null;
		Lexical b = null;
		if (check("KEYWORD")){
		//	System.out.println("checked keyword");
			match("KEYWORD");
			a = match("ID");
			//System.out.println("matched Id");
			match("OPAREN");
			b = optParamList();
			b.right = block();
				
			//System.out.println(b.left.lexeme);
			}
		else{
		//	System.out.println("in else of funcdef");
			a = lambda();		
			}
		Lexical funcdefinition = new Lexical("FUNCDEF", a, b);
		return funcdefinition;		
		}

	public static Lexical lambda(){
		Lexical a = null;
		Lexical b = null;
		match("LAMBDA");
		match("OPAREN");
		a = optParamList();
		b = block();
		Lexical lambdafunc = new Lexical("LAMBDAFUNC", a, b);
		return lambdafunc;		
		}

	public static Lexical optParamList() {
		//System.out.println("in optParamList");
		Lexical a = null;
		Lexical b = null;
		if (check("CPAREN")) {
			match("CPAREN");
			}
		else{
			a = paramList();
			}
			Lexical optparamlist = new Lexical("OPTPARAMLIST", a, b);
			return optparamlist;
		}

	public static Boolean paramListPending() {
		return check("ID");
		}

	public static Boolean optParamListpending() {
		return (paramListPending());
		}

	public static Lexical paramList() {
		Lexical a = null;
		Lexical b = null;
		a = match("ID");
		if (check("COMMA")) {
			match("COMMA");
			b = paramList();
			}
		else{
			match("CPAREN");
			}
		Lexical parameters = new Lexical("PARAMLIST", a, b);
		return parameters;
		}

	public static Lexical list() {
		//System.out.println("in list");
		Lexical a = null;
		Lexical b = null;
		a = expression();
		if (check("COMMA")) {
			match("COMMA");
			b = list();
			}
		else {
			match("CPAREN");
			}
		Lexical lst = new Lexical("LIST", a, b);
		return lst;	
		}


	public static Boolean listpending() {
		return (expressionpending());
		}

	public static Lexical expression(){
		//	System.out.println("in expression");
			Lexical a = null;
			Lexical b = null;
			//Lexical c = null;
		//	System.out.println("in expression");
			a = unary();
			if (operatorPending()) {
		//		System.out.println("in operatorpending");
				b = operator();
				//c = a;
				//b.left = c;
				b.right = expression();
				} 
			Lexical express = new Lexical("EXPRESSION", a, b);
			return express;
		}

	public static Lexical operator(){
		Lexical a = null;
		if (check("PLUS")) {
			a = match("PLUS");
			} 
		else if (check("TIMES")){
			a = match("TIMES");
			}
		else if (check("MINUS")){
			a = match("MINUS");
			} 
		else if (check("DIVIDE")){
			a = match("DIVIDE");
			} 
		else if (check("AND")){
			a = match("AND");
			} 
		else if (check("OR")){
			a = match("OR");
			} 
		else if (check("LT")){
			a = match("LT");
			} 
		else if (check("GT")){
			a = match("GT");
			}
		else if (check("LTE")){
			a = match("LTE");
			} 
		else if (check("GTE")){
			a = match("GTE");
			} 
		else if (check("ASSIGN")){
			a = match("ASSIGN");
			} 
		else if (check("EQUALS")){
			a = match("EQUALS");
			} 
		else {
			a = match("NEQUALS");
			}
		Lexical op = new Lexical("OPERATOR", a, null);
		//System.out.println(a.token);
		return op;	
		}

	public static Lexical unary() {
		//System.out.println("in unary");
		Lexical a = null;
		Lexical b = null;
		if (check("INTEGER")) {
			a = match("INTEGER");
			}
		else if(varExpressionpending()){
			a = varExpression();
			}
		else if(check("STRING")){
		//	System.out.println("checked string");
			a = match("STRING");		
			}
		else if(check("PRINT")){
		//	System.out.println("checked print");
			match("PRINT");
			match("OPAREN");
			a = list();	
			}
		else {
			match("OPAREN");
			a = expression();
			match("CPAREN");
			if(check("OPAREN")){
				match("OPAREN");
				b = optList();
				}
			}
		Lexical una = new Lexical("UNARY", a, b);
		return una;
		}

	public static Lexical varExpression() {
		Lexical a = null;
		Lexical b = null;
		//System.out.println("in varexpression");
		a = match("ID");
		if (check("OPAREN")) {
			//System.out.println("in function call");
			match("OPAREN");
			b = optList();
			//match("CPAREN");
			}
		Lexical varexpress = new Lexical("VAREXPRESSION", a, b);
		return varexpress;			
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
	public static Lexical optList() {
		Lexical a = null;
		Lexical b = null;
		if (check("CPAREN")) {
			match("CPAREN");
			}
		else{
			a = list();
			}
		Lexical oplist = new Lexical("OPTLIST", a, b);
		return oplist;
		}
	public static Boolean operatorPending() {
		return check("PLUS") || check("TIMES") || check("MINUS") || check("DIVIDE")
				|| check("AND") || check("OR") || check("LT") || check("GT")
				|| check("LTE") || check("GTE") || check("EQUALS")
				|| check("NEQUALS")|| check("ASSIGN");
	}

	public static Lexical block() {
		Lexical a = null;
		match("OBRACE");
		a = statementList();
		match("CBRACE");
		Lexical block = new Lexical("BLOCK", a, null);
		return block;
	}

	public static Lexical statementList() {
		//System.out.println("in statementlist");
		Lexical a = null;
		Lexical b = null;
		a = statement();
		if (statementPending()) {
			b = statementList();
		}
		Lexical statements = new Lexical("STATEMENTLIST", a, b);
		return statements;
	}

	public static Boolean statementPending() {
		return (expressionpending()) || (definitionpending()) || (ifStatementpending()) 
				|| (whileStatementpending());
	}

	public static Lexical statement() {
		Lexical a = null;
		Lexical b = null;
		if (varDeclpending()) {
			a = varDecl();
			} 
		else if (ifStatementpending()) {
			a = ifStatement();
			} 
		else if (whileStatementpending()) {
			a = whileStatement();
			} 
		else if(expressionpending()) {
			a = expression();
			}
		Lexical statement = new Lexical("STATEMENT", a, b);
		return statement;
	}

	public static Boolean ifStatementpending() {
		return (check("IF"));
	}

	public static Boolean whileStatementpending() {
		return (check("WHILE"));
	}

	public static Lexical ifStatement() {
		Lexical a = null;
		Lexical b = null;
		match("IF");
		a = expression();
		b = block();
		b.right = optElse();
		Lexical ifstate = new Lexical("IFSTATEMENT", a, b);
		return ifstate;
	}

	public static Lexical optElse() {
		Lexical a = null;
		Lexical b = null;
		if (check("ELSE")) {
			match("ELSE");
			a = block();
				
			
		}
		Lexical opElse = new Lexical("OPTELSE", a, b);
		return opElse;
	}

	//public void optInit() {
	//	if (check("ASSIGN")) {
	//		match("ASSIGN");
	//		expression();
	//	}
	//}
	public static Lexical whileStatement() {
		Lexical a = null;
		Lexical b = null;
		match("WHILE");
		a = expression();
		b = block();
		Lexical whilestate = new Lexical("WHILESTATEMENT", a, b);
		return whilestate;
		}

	public static void PrettyPrint(Lexical t) {
		//System.out.println("in PrettyPrint");
		//System.out.println(t.token + "hey");
		int count = 0;
		switch (t.token)
            {
		case "PROGRAM":
			//System.out.print(t.left.token);
			//System.out.print(t.right.token); 
			
			PrettyPrint(t.left);
			if(t.right != null){
				PrettyPrint(t.right);					
			}
			break;
		case "DEFINITION":
			PrettyPrint(t.left);
			break;
		case "FUNCDEF":
			//System.out.println();
			//System.out.println();
			//System.out.println(t.left.token);
			//System.out.println("hello");
			//System.out.println(t.right.left.token);
			//System.out.println(t.right.right.token);
			//System.out.println(t.right.right.left.token);
			//System.out.println(t.right.right.right.token);
			//System.out.println();
			if(t.left.token.equals("LAMBDAFUNC")){
				//System.out.println("in start of lambdafunc");
				System.out.print("lambda");
				System.out.print("(");
				//System.out.println(t.left);
				PrettyPrint(t.left);
				//System.out.print(" ");
				//PrettyPrint(t.right);
				//System.out.println();
				//System.out.print("	");
				}
			else{
				System.out.print("def ");
				System.out.print(t.left.lexeme);
				System.out.print("(");
				if(t.right.left != null){
					//System.out.print("yo");
					//System.out.println(t.right.left.lexeme);
					PrettyPrint(t.right);
					//System.out.print("hey");
					System.out.print(")");
					System.out.println("{");
					System.out.print("	");
					PrettyPrint(t.right.right);
					//System.out.print(t.right.right.right.token);
					System.out.println("	}");
					System.out.println();
					}
				else{
					System.out.print(")");
					System.out.println("{");
					System.out.print("	");
					//System.out.println(t.right.token);
					PrettyPrint(t.right.right);
					System.out.println("	}");
					System.out.println();					
					}
				}
			break; 
		case "LAMBDAFUNC":
			//System.out.println(t.right.left);
			if(t.left.left != null){
					//System.out.print("yo");
					//System.out.println(t.right.left.lexeme);
					PrettyPrint(t.left);
					//System.out.print("hey");
					System.out.print(")");
					System.out.println("{");
					System.out.print("	");
					PrettyPrint(t.right.left);
					//System.out.print(t.right.right.right.token);
					System.out.println("	}");
					System.out.println();
					}
				else{
					System.out.print(")");
					System.out.println("{");
					System.out.print("	");
					//System.out.println(t.right.token);
					PrettyPrint(t.right);
					System.out.println("	}");
					System.out.println();					
					}
				break;
		
		case "OPERATOR":
			//System.out.println("in operator");
			
			if(t.left.token.equals("PLUS")){
				System.out.print(" + ");
				} 
			else if (t.left.token.equals("TIMES")) {
				System.out.print(" * ");
				} 
			else if (t.left.token.equals("MINUS")) {
				System.out.print(" - ");
				}
			else if (t.left.token.equals("DIVIDE")) {
				System.out.print(" / ");
				} 
			else if (t.left.token.equals("AND")) {
				System.out.print(" && ");
				} 
			else if (t.left.token.equals("OR")) {
				System.out.print(" || ");
				} 
			else if (t.left.token.equals("LT")) {
				System.out.print(" < ");
				} 
			else if (t.left.token.equals("GT")) {
				System.out.print(" > ");
				} 
			else if (t.left.token.equals("LTE")) {
				System.out.print(" <= ");
				} 
			else if (t.left.token.equals("GTE")) {
				System.out.print(" >= ");
				} 
			else if (t.left.token.equals("ASSIGN")) {
				System.out.print(" = ");
				} 
			else if (t.left.token.equals("EQUALS")) {
				System.out.print(" == ");
				} 
			else {
				System.out.print(" != ");
				}
			//System.out.println(t.right.token);
			//PrettyPrint(t.right);
			break;
		case "OPTPARAMLIST":
		//	System.out.println(t.left);
			PrettyPrint(t.left);
			break;	
		case "PARAMLIST":
			//System.out.println(t.left.lexeme);
			if(t.left != null){
				//System.out.print("in if");
				System.out.print(t.left.lexeme);
				if(t.right != null){
					//System.out.print("in if");
					System.out.print(", ");
					PrettyPrint(t.right);
				}
			}
			break;
		case "LIST":
			//System.out.println("in list");
			//System.out.println(t.left.token);
			//System.out.println(t.right.token);
			//System.out.println(t.right.left.token);
			//System.out.println(t.right.left.left.token);
			if(t.left != null){
				//System.out.println("in if statement of list");
 				PrettyPrint(t.left);
			//System.out.println(t.right.token);
				if(t.right != null){
				//System.out.println(t.right.left.left.token);
					System.out.print(", ");
					PrettyPrint(t.right);
				}
			}
			else{
				System.out.print("");
			}
			break;
		case "EXPRESSION":
			//PrettyPrint(t.left);
			//System.out.println(t.left);
			//System.out.print("	");
			if(t.right != null){
				//System.out.println("outside pretty");
				//PrettyPrint(t.left);
				//System.out.println("after pretty");
				//PrettyPrint(t.right.right);
				//System.out.print("	");
				PrettyPrint(t.left);
				System.out.print(" ");
				PrettyPrint(t.right);
				System.out.print(" ");
				PrettyPrint(t.right.right);
				System.out.println();
				}
			else{
				//System.out.println("in else statement of expression");
				//System.out.println(t.left.token);
				PrettyPrint(t.left);
				}			
			break;
		case "UNARY":
			//System.out.println();
			//System.out.println(t.left);
			if(t.left.token.equals("INTEGER")){
				System.out.print(t.left.nval);			
				}
			else if(t.left.token.equals("STRING")){
				System.out.print(t.left.lexeme);			
				}
			else if(t.left.token.equals("VAREXPRESSION")){
				PrettyPrint(t.left);		
				}
			else if(t.right != null){
				//System.out.println();
				
				System.out.print(" (");
				PrettyPrint(t.left);
				System.out.print(")");
				System.out.print(" (");
				PrettyPrint(t.right);
				System.out.print(") ");
						
				}
			else{
				System.out.print("print(");
				//System.out.print(t.left.token);
				PrettyPrint(t.left);
				System.out.println(")");
				}
			//System.out.println(t.left.lexeme);
			break;

		case "VARDECL":
			//System.out.println("");
			System.out.print("var ");
			System.out.print(t.left.lexeme);
			System.out.print(" = ");
			PrettyPrint(t.right);
			System.out.println();
			break;
		case "VAREXPRESSION":
			System.out.print(t.left.lexeme);
			//System.out.println(t.right.token);
			if(t.right != null){
				System.out.print("(");
				PrettyPrint(t.right);
				System.out.println(")");
				}
			break;
		case "OPTLIST":
			//PrettyPrint(t.left);
			if(t.left != null){
				PrettyPrint(t.left);			
				}
			else{
				System.out.print("");
				}
			break;
		case "BLOCK":
			PrettyPrint(t.left);
			break;
		case "STATEMENTLIST":
			//System.out.println(t.right.left.token);
			//System.out.println(t.right.token);
			//System.out.println("in statemenlist");
			//System.out.println(t.right.left.token + "dddddddddddddddddddddddd");
			//count += 1;
			//System.out.print("");
			//System.out.println();
			//System.out.print("");
			//System.out.println(t.left.token);	
			PrettyPrint(t.left);
			//System.out.print("	h");
			if(t.right != null){
				//PrettyPrint(t.left);
				System.out.println();
				System.out.print("	");
				PrettyPrint(t.right);			
			}
			break;
		case "STATEMENT":
			PrettyPrint(t.left);		
			break;
		case "IFSTATEMENT":
			//System.out.println(t.left);
			//System.out.println(t.right.left.left.left.token);
			//System.out.println(t.right.token);
			//System.out.println();
			System.out.print("if ");
			PrettyPrint(t.left);
			System.out.print('{');
			System.out.println();
			System.out.print("		");
			PrettyPrint(t.right);
			System.out.println();
			System.out.print('}');
			PrettyPrint(t.right.right);
			break;

		case "OPTELSE":
			//System.out.println(t.left.right.left.right);
			//System.out.println(t.right.token);
			System.out.println();
			if(t.left != null){
				System.out.print("	else");
				System.out.println('{');
				System.out.print("		");
				PrettyPrint(t.left);
				System.out.print('}');
				}
			break;
			
		case "WHILESTATEMENT":
			System.out.println();
			System.out.print("	while ");
			PrettyPrint(t.left);
			System.out.print('{');
			System.out.println();
			System.out.print("		");
			PrettyPrint(t.right);
			System.out.print('}');
	      		break;
            }
	}	

	public static Lexical Parse(File f) {
		//System.out.println("in parse function");
		Lexical tree = null;
		while ((t = Parser.nextLexical()) != null) {
			//System.out.println(t);
			tree = StartingLexical();
			}
		//System.out.println("out of whilestatement");
		PrettyPrint(tree);
		return tree;
		}


	public static Lexical StartingLexical() {
		//System.out.println("in starting lexical");
		return program();
		}

	public static void main(String[] args) throws NullPointerException{

		String inFile = args[0];
		String outFile = "/dev/stdout";
		File f = new File(inFile);
		new Parser(f);
		Parse(f);

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(outFile));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
