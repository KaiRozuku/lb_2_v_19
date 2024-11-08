import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CopyOnWriteArraySet;

class SquareCalculator implements Callable<Set<Double>> {
    private final Double[] numbers;

    public SquareCalculator(Double[] numbers) {
        this.numbers = numbers;
    }

    @Override
    public Set<Double> call() {
        Set<Double> result = new CopyOnWriteArraySet<>();
        for (double num : numbers) {
            result.add(Math.pow(num, 2));
        }
        return result;
    }
}