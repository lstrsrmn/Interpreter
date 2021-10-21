import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/** the main class, runs all the sub-routine */
public class Interpreter {
  public static void main(String[] args) {
    // spaces define new arguments, and as the program only takes 1, the file name, we can safely
    // concatenate them
    // into 1 string, adding spaces where the command line has split it
    StringBuilder fileString = new StringBuilder();
    for (String s : args) {
      fileString.append(s);
      fileString.append(" ");
    }
    try {
      // reading the file
      File filePath = new File(fileString.toString());
      Scanner reader = new Scanner(filePath);
      StringBuilder data = new StringBuilder();
      // moves the file into a string
      while (reader.hasNextLine()) {
        data.append(reader.nextLine());
        data.append(" ");
      }
      reader.close();
      // Creating a container from a static lexer method
      Pair<List<Token>, List<String>> container =
          Lexer.lexer(data.toString().trim().replaceAll(" +", " "));
      // creates a new state
      ProgramHandler.ProgramState state = new ProgramHandler.ProgramState();
      // creates a new main program from the parser
      ProgramHandler.Program program = Parser.parse(container);
      // starts the programs running
      program.run(state);
      // prints all variables and values
      for (Map.Entry<String, Integer> entry : state.varLookup.entrySet()) {
        System.out.println("Variable: " + entry.getKey() + ", Value: " + entry.getValue());
      }
    } catch (FileNotFoundException e) {
      System.err.println("File can't be found.");
      // e.printStackTrace();
      System.exit(1);
    }
  }
}