import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The lexers job is to take a string from a file and convert it into a list of tokens and a list of variables referenced
 * l : String -> [token]
 */
public class Lexer {
    /**
     * @param file the file to be processed
     * @return a Pair holding a list of the tokens and a list of the variable names
     */
    public static Pair<List<Token>, List<String>> lexer(String file) {
        List<Token> tokens = new ArrayList<>();
        List<String> vars = new ArrayList<>();

        // Looks through the file, identifying and isolating all tokens and all variables and constants, such that
        // the order of vars and numbers in the tokens matches with the correct names/values in the vars list
        int previousChar = 0;
        for (int currentChar = 0; currentChar < file.length(); currentChar++) {
            if (file.charAt(currentChar) == ' ' || file.charAt(currentChar) == ';') {
                String x = "";
                if (previousChar == currentChar) {
                    x = "" + file.charAt(previousChar);
                }
                else {
                    x = file.substring(previousChar, currentChar).replaceAll(" ", "");
                }
                switch (x) {
                    case ("clear") -> {
                        tokens.add(Token.Clear);
                    }
                    case ("incr") -> {
                        tokens.add(Token.Incr);
                    }
                    case ("decr") -> {
                        tokens.add(Token.Decr);
                    }
                    case ("while") -> {
                        tokens.add(Token.While);
                    }
                    case ("do") -> {
                        tokens.add(Token.Do);
                    }
                    case ("not") -> {
                        tokens.add(Token.Not);
                    }
                    case (";") -> {
                        tokens.add(Token.SemiColon);
                    }
                    case ("end") -> {
                        tokens.add(Token.End);
                    }
                    default -> {
                        if (x.matches("[0-9]+")) {
                            tokens.add(Token.Number);
                            vars.add(x);
                        }
                        else if (x.matches("[a-zA-Z]+")) {
                            tokens.add(Token.Var);
                            vars.add(x);
                        }
                    }
                }
                previousChar = currentChar;
            }
        }
        return new Pair<>(tokens, vars);
    }
}
