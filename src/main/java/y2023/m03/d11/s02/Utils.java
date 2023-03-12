package y2023.m03.d11.s02;

public class Utils {

    public static Person getPerson(Long personId) {
        sleep(200L);
        return new Person(personId);
    }

    public static Asset getAssets(Person person) {
        sleep(200L);
        return new Asset(person);
    }

    public static Liability getLiabilities(Person person) {
        sleep(200L);
        return new Liability(person);
    }

    public static Person doSomethingElseImportant(Person person) {
        sleep(200L);
        return person;
    }

    public static Credit calculateCredit(Asset assets, Liability liabilities) {
        sleep(200L);
        return new Credit(assets, liabilities);
    }

    private static void sleep(Long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
