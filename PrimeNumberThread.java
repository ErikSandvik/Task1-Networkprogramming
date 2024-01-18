import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PrimeNumberThread implements Runnable {
    private final int start;
    private final int end;
    private List<Integer> primeNumbers;

    public PrimeNumberThread(int start, int end) {
        this.start = start;
        this.end = end;
        primeNumbers = new ArrayList<>();
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

    private int[] makePartitions(int originalInteger, int amountOfPartitions) {
        int[] partitions = new int[amountOfPartitions];

        int valueForPartition = originalInteger / amountOfPartitions;
        int remainder = originalInteger % amountOfPartitions;

        for (int i = 0; i < amountOfPartitions; i++) {
            partitions[i] = valueForPartition;
        }

        while (remainder > 0) {
            for (int i = 0; i < amountOfPartitions; i++) {
                partitions[i]++;
                remainder--;
                if (remainder < 0) break;
            }
        }

        return partitions;
    }
}

class RunThreads {
    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(2);
    }

}
