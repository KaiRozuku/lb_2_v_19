import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        System.out.print("Введіть мінімальне значення діапазону: ");
        double minRange = sc.nextDouble();

        System.out.print("Введіть максимальне значення діапазону: ");
        double maxRange = sc.nextDouble();

        int arraySize = 40 + random.nextInt(21);
        Double[] array = new Double[arraySize];

        for (int i = 0; i < array.length; i++) {
            array[i] = minRange + (maxRange - minRange) * random.nextDouble();
        }

        System.out.println("Початковий масив: " + formattedArray(Arrays.stream(array).collect(Collectors.toList())));

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        List<Future<Set<Double>>> futures = new ArrayList<>();
        int chunkSize = 10;

        long startTime = System.nanoTime();

        for (int i = 0; i < array.length; i += chunkSize) {
            int end = Math.min(array.length, i + chunkSize);
            Double[] chunk = new Double[end - i];
            System.arraycopy(array, i, chunk, 0, end - i);

            Future<Set<Double>> future = executorService.submit(new SquareCalculator(chunk));
            futures.add(future);
        }

        futures.forEach(future -> {
            try {
                if (!future.isCancelled()) {
                    Set<Double> squares = future.get();
                    System.out.println("Результат обробки частини масиву: " + formattedArray(squares));
                }
                if (future.isDone())
                    System.out.println("Завдання виконано.");

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        long endTime = System.nanoTime();
        System.out.println("Час виконання програми: " + (endTime - startTime) / 1_000_000 + " мс");
        executorService.shutdown();
        sc.close();
    }

    private static String formattedArray(Collection<Double> array) {
        return array.stream()
                .map(num -> String.format("%.3f", num))
                .collect(Collectors.joining(", ", "[", "]"));
    }
}
