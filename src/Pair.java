// Simple templated pair class because this was too hard for the java developers to do :)
public class Pair<T, K> {
    T first;
    K second;

    public Pair(T t, K k) {
        first = t;
        second = k;
    }
}
