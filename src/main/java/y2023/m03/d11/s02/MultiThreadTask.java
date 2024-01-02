package y2023.m03.d11.s02;

import java.util.concurrent.atomic.AtomicReference;

import static y2023.m03.d11.s02.Utils.*;

public class MultiThreadTask {
    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        var multi = new MultiThreadTask();
        Credit credit = multi.calculateCreditForPersonClassically(1L);
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start) + " => " + credit);
    }

    public Credit calculateCreditForPersonClassically(Long personId) throws InterruptedException {
        var person = getPerson(personId);
        var assetsRef = new AtomicReference<Asset>();

        var t1 = new Thread(() -> assetsRef.set(getAssets(person)));

        var liabilityRef = new AtomicReference<Liability>();
        var t2 = new Thread(() -> liabilityRef.set(getLiabilities(person)));
        var t3 = new Thread(() -> doSomethingElseImportant(person));

        t1.start();
        t2.start();
        t3.start();

        t2.join();
        t2.join();

        final var credit = calculateCredit(assetsRef.get(), liabilityRef.get());
        t3.join();

        return credit;
    }

}
