import java.util.*;

public class ProgramHandler {
    private static ProgramHandler single_instance = null;

    private ProgramHandler() {
    }

    public static class Variable {
        String name;

        Variable(String n) {
            name = n;
        }
    }

    public static ProgramHandler getInstance() {
        if (single_instance == null)
            single_instance = new ProgramHandler();

        return single_instance;
    }

    /**
     * Pure Virtual interface in order to create a set of programs that all have the void run(ProgramState) method
     */
    interface Runnable {
        public void run(ProgramState state);
    }


    /**
     * All statements must be runnable, this is a wrapper class to determine what is a statement and what is just runnable
     */
    static class Statement implements Runnable {
        @Override
        public void run(ProgramState state) {

        }
    }

    /**
     * Wrapper for a hash map, containing variables names and values
     */
    static class ProgramState {
        public HashMap<String, Integer> varLookup = new HashMap<>();
    }

    /**
     * A program stores a list of statements which all impliment the function run(ProgramState) meaning that it can run
     * all statements in its program
     */
    static class Program implements Runnable {
        public List<Statement> stmts = new ArrayList<Statement>();

        /**
         * implimentation of the abstract Runnable's void run(ProgramState)
         * @param state holds the varLookup
         */
        @Override
        public void run(ProgramState state) {
            // loops through all statements and runs them each
            for (Statement statement : stmts) {
                statement.run(state);
            }
        }
    }

    /**
     * "Clear" statement implementation
     */
    static class ClearStatement extends Statement {
        // the variable to be worked on when ran
        public Variable v;

        /**
         * @param state the varLookup
         */
        @Override
        public void run(ProgramState state) {
            // checks if the variable exists
            if (!state.varLookup.containsKey(v.name)) {
                // if no, create it
                state.varLookup.put(v.name, 0);
            } else {
                // if yes, set it to 0
                state.varLookup.replace(v.name, 0);
            }
        }
    }

    /**
     * "Incr" statement implementation
     */
    static class IncrStatement extends Statement {
        // the variable to be worked on when ran
        public Variable v;

        /**
         * @param state the varLookup container
         */
        @Override
        public void run(ProgramState state) {
            if (state.varLookup.containsKey(v.name)) {
                // if v exists in varLookup, increase value by 1
                int curr = state.varLookup.get(v.name);
                state.varLookup.replace(v.name, curr + 1);
            }
        }
    }

    /**
     * "Decr" statement implementation
     */
    static class DecrStatement extends Statement {
        // the variable to be worked on when ran
        public Variable v;

        /**
         * @param state varLookup container
         */
        @Override
        public void run(ProgramState state) {
            if (state.varLookup.containsKey(v.name)) {
                // if v in varLookup, decrease value by 1
                int curr = state.varLookup.get(v.name);
                state.varLookup.replace(v.name, curr - 1);
            }
        }
    }

    /**
     * Implementation of while loop
     */
    static class WhileLoop extends Statement {
        // a program of its content to recursively run
        public Program p;
        // an expression that if it returns a boolValue, will determine when the loop runs or continues
        public Expression.Expr expr;

        /**
         * @param state varLookup container
         */
        @Override
        public void run(ProgramState state) {
            // gets the value from the expression at this instance - note value is an abstract class and val is a sub class
            // to Value
            Expression.Value val = expr.run(state);
            Expression.BoolValue boolValue = val instanceof Expression.BoolValue ? ((Expression.BoolValue) val) : null;
            // Checks that val is a boolValue, and creates a new boolValue
            if (boolValue != null) {
                // runs if the bool is true
                if (boolValue.b) {
                    // runs the recursive program
                    p.run(state);
                    // reruns "void run(ProgramState)" to check expr again and continue until the bool is false
                    run(state);
                }
            } else {
                // if the expression is not of boolType, the user has made a mistake and not properly defined their
                // Expression
                System.err.println("Wrong Expression Type");
            }
        }

    }
}

