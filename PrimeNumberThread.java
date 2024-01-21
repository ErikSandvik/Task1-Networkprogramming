import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

//A class that represents a thread that looks for prime numbers between a certain range
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
                //Synchronized to avoid concurrency issues
                synchronized (primeNumbers) {
                    primeNumbers.add(i);
                }
            }
        }
    }

    //Checks if number is prime
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

//Class that finds primes between a range using a given number of threads
class PrimeFinder {
    public static List<Integer> findPrimes(int start, int end, int numOfThreads) {
        List<Integer> primeNumbers = new ArrayList<>();

        //Makes a fixedThreadPool with a number of threads equal to the integer "numOfThreads"
        ExecutorService executorService = Executors.newFixedThreadPool(numOfThreads);

        //Divides the work into numOfThreads-parts
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

            //Makes the thread
            executorService.submit(new PrimeNumberThread(partStart, partEnd, primeNumbers));
        }

        //Shutdown the ExecutorService
        executorService.shutdown();

        //Waits for all threads to finish
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //Sort the prime numbers in ascending order
        primeNumbers.sort(Comparator.naturalOrder());

        return primeNumbers;
    }
}


class RunThreads {
    public static void main(String[] args) {
        //Finds all primes between 0 and 100 using 3 threads
        List<Integer> primes = PrimeFinder.findPrimes(0, 100, 3);

        System.out.println(primes);
    }

}
