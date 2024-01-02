package y2023.m03.d11.s02;

import java.util.concurrent.*;

import static y2023.m03.d11.s02.Utils.*;
import static y2023.m03.d11.s02.Utils.doSomethingElseImportant;

public class VirtualThreadTask {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        var executor = new ExecutorTask();
        ExecutorService service = newVirtualThreadPerTaskExecutor();
        Credit credit = executor.calculateCreditForPersonWithExecutors(service, 1L);
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start) + " => " + credit);
        System.exit(0);
    }

    public Credit calculateCreditForPersonWithExecutors(ExecutorService threadPoll, Long personId)
            throws ExecutionException, InterruptedException {
        var person = getPerson(personId);
        final var assetFuture = threadPoll.submit(() -> getAssets(person));
        final var liabilityFuture = threadPoll.submit(() -> getLiabilities(person));

        threadPoll.submit(() -> doSomethingElseImportant(person));
        return calculateCredit(assetFuture.get(), liabilityFuture.get());
    }

    /**
     * TODO：代码待验证修复，需要JDK19，但是lombok现在不支持19，以后再学习吧
     */
    public static ExecutorService newVirtualThreadPerTaskExecutor(){
//        ThreadFactory factory = Thread.ofVirtual().factory();
//        return newThreadPerTaskExecutor(factory);
        return null;
    }

}
