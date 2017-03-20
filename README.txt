I’m coding in Java to make a Python based language. There are 3 main files and a lexeme file: scanner.java, Recognizer.java, Parser.java, Environment.java and Token.java(the lexemes). The scanner.java file is where the scanner of the code come in. Also my Recognizer.java recognizes if the code is in the right format and build. My Parser.java is the builds the parse tree and return a prettyprint for the python code. And example of how to use these files are here:

Either in eclipse or in your terminal, In Eclipse you go to run configurations and set your .txt file to the read in file as arg[0]. Then you put all of the java files in the same package and then you can run the all individually. An example in eclipse would be. 

.txt file contents:  The .txt file must have an empty space(Space Bar) after the last character 

def name(a){
	a = a + 1
	}
or

def name(a){
	if a = 2{
		print a
		}
}

or

if a < 4{
	a = 3
	}
Both of those function definitions would return “LEGAL” in the Recognizer. The scanner would just return each token. 

In a terminal you have to run the java file by running these commands on a Linex System:
1) Go to the directory
2) type in javac (Recognizer.java, Scanner.java, or PrettyPrinter.java) to compile either
3) type in java (Whatever file you choose) and then the .txt file for the argument

Example in your terminal:
javac Recognizer.java
java Recognizer input.txt

Another example of how I’m using my version of python is a while loop:
My python version doesn’t take in for loops, so you you have to do a loop its a while loop.
.txt file contents

while i <= 4{
	if i = 2{
		i = i + 3
		}
	else{
		i = i + 1
		}
	}  

If there is a token out of place or if there isn’t a space after the last character in the .txt file, then the Recognizer will return “ILLEGAL” and will say what it is the wrong token and what expected token it is expecting. 


In the Environment.java File it will build the parse tree need to be evaluated by the environment.java file this is where the actual code will run and return the desired output that is asked.
