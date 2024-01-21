import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PrimeNumberThread implements Runnable {
    private final int start;
    private final int end;
    private List<Integer> primeNumbers;

    public PrimeNumberThread(int start, int end, List<Integer> primeNumbers) {
        this.start = start;
        this.end = end;
        this.primeNumbers = primeNumbers;
    }

    @Override
    public void run() {
        for (int i = start; i <= end; i++) {
            if (isPrime(i)) {
                synchronized (primeNumbers) {
                    primeNumbers.add(i);
                }
            }
        }
    }

    private boolean isPrime(int num)
    {
        if(num <= 1) {
            return false;
        }
        for(int i = 2; i <= num / 2; i++) {
            if((num % i) == 0)
                return  false;
        }
        return true;
    }


}

class PrimeFinder {
    public static List<Integer> findPrimes(int start, int end, int numOfThreads) {
        List<Integer> primeNumbers = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);

        int partStart;
        int partEnd;
        int rangeSize = (end - start + 1) / numOfThreads;

        for (int i = 0; i < numOfThreads; i++) {
            partStart = start + i * rangeSize;
            if (i == numOfThreads - 1) {
                partEnd = end;
            } else {
                partEnd = partStart + rangeSize - 1;
            }

            executorService.submit(new PrimeNumberThread(partStart, partEnd, primeNumbers));
        }


        executorService.shutdown();

        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        primeNumbers.sort(Comparator.naturalOrder());

        return primeNumbers;

    }


}

class RunThreads {
    public static void main(String[] args) {
        List<Integer> primes = PrimeFinder.findPrimes(0, 100, 3);
        System.out.println(primes);
    }

}
