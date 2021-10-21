import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Expression {
    /**
     * @param <T> pure virtual interface of template T in order to have an abstracted consistent way of retrieving data
     *           , made safe by isinstance checks before any grab using getValue().
     */
    public interface Value<T> {
        public T getValue();
    }

    /**
     * A Value that holds an integer
     */
    static class IntVal implements Value<Integer> {
        Integer v;

        public IntVal(Integer v) {
            this.v = v;
        }
        public Integer getValue() {
            return v;
        }
    }

    /**
     * A Value that holds an bool
     */
    static class BoolValue implements Value<Boolean> {
        boolean b;

        public BoolValue(boolean b) {
            this.b = b;
        }

        public Boolean getValue() {
            return b;
        }
    }

    /**
     * Abstracted expression, all expressions must be evaluable, the template of value doesn't matter as its abstracted
     */
    interface Expr {
        Value run(ProgramHandler.ProgramState s);
    }

    /**
     * a constant expression, value template type doesnt matter because any value can be constant
     */
    public static class LiteralExpr implements Expr {
        Value x;

        @Override
        public Value run(ProgramHandler.ProgramState s) {
            return x;
        }

        public LiteralExpr(Value x) {
            this.x = x;
        }
    }


    /**
     * a Variable expression, value template type matters because variables are lock into being Integers
     */
     static class VarExpr implements Expr {
        String name;

        @Override
        public Value<Integer> run(ProgramHandler.ProgramState s) {
            return new IntVal(s.varLookup.get(name));
        }

        public VarExpr(String n) {
            name = n;
        }
    }

    /**
     * A not comparison expression, the expr return types are not templated as this is irelevant, however the output
     * value will always ben boolean
     */
    static class NotExpr implements Expr {
        Expr lhs, rhs;

        public NotExpr(Expr lhs, Expr rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }
        public Value<Boolean> run(ProgramHandler.ProgramState s) {
            Value lhsV = lhs.run(s);
            Value rhsV = rhs.run(s);
            if (lhsV.getClass() == rhsV.getClass()) {
                return new BoolValue(lhsV.getValue() != rhsV.getValue());
            }
            return new BoolValue(true);
        }
    }
}
