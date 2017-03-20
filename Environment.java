import java.io.IOException;
import java.io.*;


public class Environment {

	public static Lexical create(){
		return extend(null, null, null);
		}
	
	/**
	 * LookUp
	 * 
	 */
	//finding the variable 
	//looking up a variable/value in the first two lists of an environment structure
	//If it is not there, we lookup the variable in the next two lists and so on
	
	public static Lexical lookup(String variable, Lexical env){
		//System.out.println("in lookup");
		//System.out.println(env.left + " env.left");
		Lexical a = null;
		try{
			//System.out.println("in try");
			//System.out.println("variable is " + variable);
			//System.out.println(env.left.left.left.right);
			//System.out.println(env.right);
			while(env != null){
				Lexical table = env.left;
				Lexical vars = table.left;
				Lexical vals = table.right;
				//System.out.println(table);
				//System.out.println(vars.left);
				//System.out.println(vars.right);
				//System.out.println(vals);
				//Lexical vars = env.right.left; //car
				//System.out.println(vars.left.lexeme);
				//System.out.println(variable);
				//Lexical vals = env.right.right; //cadr
				//System.out.println(vals.left.left.left);
				//Lexical outer = env.right.right; //cddr - outer environment
				//System.out.println(vars.right);
				//System.out.println(vals.right);
				while(vars != null){
					//System.out.println(vars.left.lexeme);
					if(variable.equals(vars.left.lexeme)){ //variable == var vars
						//go into lexeme to get the name
						//System.out.println(vars.left.nval);
						//System.out.println("in this if statement");
						//System.out.println(vals.left.left.left.nval);
						return vals.left;
						}
					vars = vars.right; //cdr vars
					vals = vals.right; //cdr vals
					}
					//parallel list
					//vars = vars.right; //cdr vars
					//vals = vals.right; //cdr vals
					env = env.right;
				}
				//set environment = outer environment
			}
		
		//error
		catch(Exception e){
			System.out.println("variable , " + variable +" , is undefined");
		}
		return null;
	}
	
	/**
	 * Update
	 * 
	 */
	//only setCar is used to set the appropriate car pointer of the values list
	public static Lexical update(String lookupvariable, Lexical update, Lexical env){
		//System.out.println("in update");
		//System.out.println(update.nval);
		//System.out.println(env);
		//System.out.println(env.left.left);
		//System.out.println(env.left.left.left.lexeme);
	
		try{
			//System.out.println("in try");
			while(env != null){
				//System.out.println("in while");
				Lexical table = env.left;
				Lexical vars = table.left;
				Lexical vals = table.right;
				//System.out.println(vars);
				//System.out.println(vals.left);
				//System.out.println("seeing if vars is null");
				while(vars != null){
				//	System.out.println("comparing the lookup variable to vars " + lookupvariable + " " + vars.left.lexeme);
					if(lookupvariable.equals(vars.left.lexeme)){ 
						//System.out.println("setting lookup variable" + update);
							vals.left = update;
							//System.out.println(vals);
							return update;
							
						}
					//parallel list
					//System.out.println("cdring through the parallel list");
					vars = vars.right; //cdr vars
					vals = vals.right; //cdr vals
						
					
					}
				//System.out.println("out of while of while");
				//set environment = outer environment 
				env = env.right;
				//return env;
				}
			}
		//error
		catch(Exception e){
			System.out.println("variable , " + lookupvariable +" , is undefined");
			}
		//S
		return null;
		}
	
	public static Lexical insert(Lexical env, Lexical variable, Lexical value){
		//System.out.println("in insert");
		//System.out.println("variable: " + variable);
		//System.out.println("value: "+ value);
		Lexical table = env.left;
		Lexical cartable = table.left;
		Lexical cdrtable = table.right;
		Lexical newcartable = new Lexical("VARIABLES", variable, cartable);
		Lexical newcdrtable = new Lexical("VALUES", value, cdrtable);
		env.left = new Lexical("TABLE",newcartable,newcdrtable);
		//class notes
		//System.out.println(env.right.left);
		//Lexical vars = env.left; //car env
		//Lexical vals = env.right.left; //cadr env
		//Lexical lEnv = new Lexical(variable.Lexical, vars,null);
		//env.left = lEnv;
		//JOIN used but not sure how it transfers over to java
		//Lexical RLEnv = new Lexical(value.Lexical, vals,null);
		//env.right.left = RLEnv;
		
		return value; 	
		}
	
	
	public static Lexical extend(Lexical variables, Lexical values, Lexical env){
		//variables = formal parameter
		//values = function call , ex:argList
		//env = defining environment 
		//System.out.println("in extend param: " + variables + " lst: " + values);
		Lexical cons = null;
		if(variables != null){
			Lexical param = variables.left;

			Lexical lst = values;
			cons = new Lexical("ENV", makeTable(param, lst), env);
			
			}
		else{ cons = new Lexical("ENV", makeTable(variables, values), env);
			}
		return cons;
	}	
	
	public static Lexical makeTable(Lexical var, Lexical val){
		return new Lexical("TABLE", var, val);
		}
	
	public static Lexical eval(Lexical t, Lexical env){
		//System.out.println("in eval");
		switch (t.token)
            {
		case "INT": return t;

            	case "STRING": return t;

            	case "ID": return lookup(t.lexeme,env);
			
		case "PROGRAM":
			//System.out.print(t.left.token);
			//System.out.print(t.right.left.token); 
			
//			eval(t.left, env);
			if(t.right != null){
				eval(t.left, env);
				return eval(t.right, env);					
				}
			else{
				return eval(t.left, env);
				}
			//break;
		case "DEFINITION":
			return eval(t.left, env);
			//break;
		case "FUNCDEF":
			return evalFuncDef(t, env);
			//break; 
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
				return evalSimpleOp(t, env);
				//PrettyPrint(t.left);
				//System.out.print(" ");
				//PrettyPrint(t.right);
				//System.out.print(" ");
				//PrettyPrint(t.right.right);
				}
			else{
				//System.out.println("in else statement of expression");
				//System.out.println(t.left.token);
				return eval(t.left, env);
				}			
			//break;
		case "UNARY":
			//System.out.println();
			//System.out.println(t.left.lexeme);
			if(t.left.token.equals("INTEGER")){
				return eval(t.left, env);			
				}
			else if(t.left.token.equals("STRING")){
				return eval(t.left, env);			
				}
			else if(t.left.token.equals("VAREXPRESSION")){
				//System.out.println(t.left.left);
				return eval(t.left, env);		
				}
			//else if(t.right != null){
				//System.out.println();
				
				//System.out.print(" (");
				//PrettyPrint(t.left);
				//System.out.print(")");
				//System.out.print(" (");
				//PrettyPrint(t.right);
				//System.out.print(") ");
						
				//}
			else{
				//System.out.println(t.left);
				return evalPrint(t.left, env);
				/*System.out.print("print(");
				//System.out.print(t.left.token);
				PrettyPrint(t.left);
				System.out.print(")");*/
				}
			//System.out.println(t.left.lexeme);
			//break;

		case "VARDECL":
			return evalVarDecl(t, env);
		case "VAREXPRESSION":
			//System.out.print(t.left.lexeme);
			//System.out.println(t.right.left);
			//System.out.println(t.left);
			if(t.right != null){
				
				return evalFuncCall(t, env);
				//System.out.print("(");
				//PrettyPrint(t.right);
				//System.out.println(")");
				}
			else{
				return eval(t.left, env);
				}
			//break;
		/*case "OPTLIST":
			//PrettyPrint(t.left);
			if(t.left != null){
				PrettyPrint(t.left);			
				}
			else{
				System.out.print("");
				}
			break;*/
		case "BLOCK":
			return evalBlock(t, env);
			//break;
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
			//return eval(t.left, env);
			//System.out.print("	h");
			if(t.right != null){
				//PrettyPrint(t.left);
				//System.out.println();
				//System.out.print("	");
				eval(t.left, env);
				return eval(t.right, env);			
				}
			else{
				//System.out.println("in else");
				return eval(t.left, env);
				}
			//break;
		case "STATEMENT":
			return eval(t.left,env);		
			//break;
		case "IFSTATEMENT":
			return evalIfStatement(t, env);
			/*//System.out.println(t.left);
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
			//if(t.right.left != null){
			//	PrettyPrint(t.right.left);
			//}*/
			//break;

		case "OPTELSE":
			return evalElse(t, env);
			/*System.out.println(t.left.right.left.right);
			//System.out.println(t.right.token);
			System.out.println();
			System.out.print("	else");
			System.out.println('{');
			System.out.print("		");
			PrettyPrint(t.left);
			System.out.print('}');*/
			//break;
			
		case "WHILESTATEMENT":
			return evalWhile(t,env);
			/*System.out.println();
			System.out.print("	while ");
			PrettyPrint(t.left);
			System.out.print('{');
			System.out.println();
			System.out.print("		");
			PrettyPrint(t.right);
			System.out.print('}');*/
	      		//break;
            }
		return t;
		}

	public static Lexical evalFuncDef(Lexical t,Lexical env){
		//System.out.println("in funcdef");
		Lexical closure = new Lexical("CLOSURE",env,t);
		return insert(env,t.left,closure);
		}

	public static Lexical envInsert(Lexical env, Lexical t, Lexical close){
		Lexical oldEnv = env.right;
		Lexical newEnv = new Lexical("NEWENVIRONMENT", t, close);
		Lexical join = new Lexical("JOIN",newEnv,oldEnv);
		env.right = join;
		return env;
		}

	public static Lexical evalFuncCall(Lexical t, Lexical env){
		//System.out.println("in evalFuncCall");
		//System.out.println(t.left.token);
		Lexical closure = eval(t.left,env);
		//System.out.println("closure");
        	Lexical args = t.right;
		//System.out.println(args);
		Lexical params = closure.right.right;
		//System.out.println(params.left.left);
		Lexical body = closure.right.right.right;	
        	Lexical senv = closure.left;
		//Parser.PrettyPrint(args);
        	Lexical eargs = evalArgs(args.left,env);
		//System.out.println(eargs);
		//Parser.PrettyPrint(eargs);
        	Lexical xenv = extend(params,eargs,senv);
        	//System.out.println(body);
        	return eval(body,xenv);
		
        	}
	
	public static Lexical getFuncCallName(Lexical t, Lexical env){
		//System.out.println("in functioncall");
		//System.out.println(env.right.left.right);
		Lexical a = null;
		if(env.right.left.left.lexeme.equals(t.left.lexeme)){
			a = env.right.left.right;
			}
		else{
			getFuncCallName(t, env.right.right);
			}
		//System.out.println(a);
		return a;
		}

	public static Lexical evalArgs(Lexical t, Lexical env){
		//System.out.println("t.token is" + t.token);
		if(t != null){
			Lexical a = eval(t.left, env);
			//System.out.println("evaluated argslst " + a);
			Lexical b = null;
			Lexical lst = new Lexical("LIST",a,b);
			Lexical moreArgs = evalArgs(t.right, env);
			lst.right = moreArgs;
			return lst;
				
			
				
			//System.out.println(t.right.left.left.Lexical);
			//System.out.print(",");
			//PrettyPrint(t.right);
			}
		else{
			//System.out.println("In else of evalArgs");
			return t;
			}
		//return t;
		}
	
	public static Lexical evalVarDecl(Lexical t, Lexical env){
		//System.out.println(t.left.token);
		//System.out.println(t.right);
		//System.out.println("in evalVarDecl");
		return insert(env, t.left, t.right);
		}

	public static Lexical evalBlock(Lexical t, Lexical env){
		//System.out.println("in evalBlock");
		return eval(t.left, env);
		}	

	public static Lexical evalPrint(Lexical t, Lexical env){
		//System.out.println("in evalPrint");
		//System.out.println(t.left.left.left);
		Lexical a = null;		
		if(t.left.left.left.token.equals("STRING")){
			String s = (t.left.left.left.lexeme);
			s = s.replaceAll("\"", "");
			System.out.println(s);
			}
		else if(t.left.left.left.token.equals("VAREXPRESSION")){
			//System.out.println("in evalprint else if 1");
			a = lookup(t.left.left.left.left.lexeme, env);
			//System.out.println(a);
			Lexical b = eval(a,env);
			//System.out.println(a);
			//System.out.println(a.token);
			if(a.token.equals("INTEGER")){
				//System.out.println("a.token is integer");
				System.out.println(b.nval);
				}
			else{
			//	System.out.println("a.token is not an integer");
				System.out.println(b.lexeme);
				}			
			//System.out.println(t.left.left.left.left.lexeme);
			//System.out.println(lookup(t.left.left.left.lexeme, env));
			}
		else if(t.left.left.left.token.equals("INTEGER")){
			System.out.println(t.left.left.left.nval);
			}
		//System.out.println(t.left.left.left.lexeme);
		return t;
		}

	public static Lexical evalVarExpression(Lexical t, Lexical env){
		//System.out.println("in evalVarExpression");
		return t;
		}

	public static Lexical evalIfStatement(Lexical t, Lexical env){
		//System.out.println("in evalIfStatement");
		//System.out.println();
		Lexical test = eval(t.left, env);
		//System.out.println("evaluated test");
		if(isTrue(test)){
		//	System.out.println("in if of evalIfStatement");
			return eval(t.right, env);
			}
		else{
			return eval(t.right.right, env);
			}
		//return t;
		}

	public static Lexical evalElse(Lexical t, Lexical env){
			if(t.left != null){
				return eval(t.left,env);
				}
			else{
				return null;
				}
		}

	public static Lexical evalSimpleOp(Lexical t, Lexical env){
		String s = t.right.left.token;		
		if(s.equals("PLUS")){
			return evalPlus(t, env);			
			}
		else if(s.equals("MINUS")){
			return evalMinus(t, env);
			}
		else if(s.equals("TIMES")){
			return evalTimes(t, env);
			}
		else if(s.equals("DIVIDE")){
			return evalDivide(t, env);
			}
		else if(s.equals("GT")){
			return evalGt(t, env);
			}
		else if(s.equals("GTE")){
			return evalGte(t, env);
			}
		else if(s.equals("LT")){
			return evalLt(t, env);
			}
		else if(s.equals("LTE")){
			return evalLte(t, env);
			}
		else if(s.equals("EQUALS")){
			return evalEquals(t, env);
			}
		else if(s.equals("ASSIGN")){
			return evalAssign(t, env);
			}
		else{
			return evalNequals(t, env);
			}

		}

	public static Lexical evalPlus(Lexical t, Lexical env){
		Lexical left = eval(t.left, env);
		Lexical right = eval(t.right.right, env);
		if(left.token.equals("INTEGER") && right.token.equals("INTEGER")){
			return newIntegernval(left.nval + right.nval);
			}
		else{
			return newStringlexeme(left.lexeme + right.lexeme);
			}
		}

	public static Lexical newIntegernval(int n){
		Lexical integer = new Lexical("INTEGER","",n);
		return integer;
		}

	public static Lexical newStringlexeme(String s){
		Lexical string = new Lexical("STRING",s,0);
		return string;
		}

	public static Lexical evalMinus(Lexical t, Lexical env){
		//System.out.println("in evalMinus");
		Lexical left = eval(t.left, env);
		//System.out.println("evaluated t.left " + left);
		Lexical right = eval(t.right.right, env);
		//System.out.println(t.right.right.left.left);
		//System.out.println("evaluated t.right.right " + right.nval);
		return newIntegernval(left.nval - right.nval);		
		}

	public static Lexical evalTimes(Lexical t, Lexical env){
		Lexical left = eval(t.left, env);
		Lexical right = eval(t.right.right, env);
		//System.out.println(left.nval * right.nval);
		return newIntegernval(left.nval * right.nval);
		}

	public static Lexical evalDivide(Lexical t, Lexical env){
		Lexical left = eval(t.left, env);
		Lexical right = eval(t.right.right, env);
		return newIntegernval(left.nval / right.nval);
			
		}

	public static Lexical evalGt(Lexical t, Lexical env){
		Lexical left = eval(t.left, env);
		//System.out.println(left);
		Lexical right = eval(t.right.right, env);
		//System.out.println("evaluated t.right.right " + right);
		return toBoolean(left.nval > right.nval);
			
		}

	public static Lexical evalGte(Lexical t, Lexical env){
		Lexical left = eval(t.left, env);
		//System.out.println(left);
		Lexical right = eval(t.right.right, env);
		//System.out.println("evaluated t.right.right " + right);
		return toBoolean(left.nval >= right.nval);
			
		}

	public static Lexical evalLt(Lexical t, Lexical env){
		Lexical left = eval(t.left, env);
		//System.out.println(left);
		Lexical right = eval(t.right.right, env);
		//System.out.println("evaluated t.right.right " + right);
		return toBoolean(left.nval < right.nval);
			
		}

	public static Lexical evalLte(Lexical t, Lexical env){
		Lexical left = eval(t.left, env);
		//System.out.println(left);
		Lexical right = eval(t.right.right, env);
		//System.out.println("evaluated t.right.right " + right);
		return toBoolean(left.nval <= right.nval);
			
		}

	public static Lexical evalAssign(Lexical t, Lexical env){
		//System.out.println("in evalAssign");
		//System.out.println(env.right);
		//System.out.println(t.left.left.left);
		//Lexical lookup = eval(t.left,env);
		Lexical value = eval(t.right.right, env);
		//System.out.println(value.nval);
		update(t.left.left.left.lexeme,value,env);		
		return value;
		}

	public static Lexical evalEquals(Lexical t, Lexical env){
		Lexical left = eval(t.left, env);
		//System.out.println(left);
		Lexical right = eval(t.right.right, env);
		//System.out.println("evaluated t.right.right " + right);
		return toBoolean(left.nval == right.nval);
			
		}

	public static Lexical evalNequals(Lexical t, Lexical env){
		Lexical left = eval(t.left, env);
		//System.out.println(left);
		Lexical right = eval(t.right.right, env);
		//System.out.println("evaluated t.right.right " + right);
		return toBoolean(left.nval != right.nval);
			
		}

	public static Lexical evalWhile(Lexical t, Lexical env){
		Lexical test = eval(t.left,env);
		while(isTrue(test)){
			eval(t.right,env);
			test = eval(t.left,env);	
		}
		return test;
		}

	
		
	public static boolean isTrue(Lexical exp){
		if(exp.token.equals("TRUE") ){
			return true;
		}
		else{
			return false;		
		}
	}	
	//public static Lexical toBoolean(boolean expr){}	
	public static Lexical toBoolean(boolean expr){
			//System.out.println("in toBoolean");
			if(expr){
				//System.out.println("in if state of toBoolean");
				return new Lexical("TRUE","true",0);			
			}
			else{
				return new Lexical("FALSE","false",0);			
			}	
		}	
	
	

	
	
public static void main(String[] args) {
		//System.out.println("before eval function");
		String inFile = args[0];
		String outFile = "/dev/stdout";
		File f = new File(inFile);
		Lexical env = create();
		Parser parseTree = new Parser(f);
		Lexical t = parseTree.Parse(f);
		//System.out.println("before eval function");
		eval(t,env);
		//System.out.println(environment.right.left);		
		//insert(null,null,environment)		
	}	
		
}
