package y2023.m03.d11.s02;

import java.util.concurrent.*;

import static y2023.m03.d11.s02.Utils.*;

public class ComposabilityTask {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        long start = System.currentTimeMillis();
        var compose = new ComposabilityTask();
        ScheduledExecutorService service = Executors.newScheduledThreadPool(5);
        Credit credit = compose.calculateCreditForPersonWithComposability(service, 1L);
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start) + " => " + credit);
    }

    public Credit calculateCreditForPersonWithComposability(ExecutorService threadPoll, Long personId)
            throws ExecutionException, InterruptedException {
        return CompletableFuture.supplyAsync(() -> getPerson(personId))
                .thenComposeAsync(person -> {
                    // final CompletableFuture<Asset> assetsFuture
                    final var assetsFuture = CompletableFuture.supplyAsync(() -> getAssets(person));
                    // final CompletableFuture<Liability> assetsFuture
                    final var liabilityFuture = CompletableFuture.supplyAsync(() -> getLiabilities(person));
                    // final CompletableFuture<Person> importantWorkFuture
                    final var importantWorkFuture = CompletableFuture.supplyAsync(() -> doSomethingElseImportant(person));

                    return importantWorkFuture.thenCompose((v) ->
                            assetsFuture.thenCombineAsync(liabilityFuture,
                                    (Utils::calculateCredit)));
                }).get();
    }
}
