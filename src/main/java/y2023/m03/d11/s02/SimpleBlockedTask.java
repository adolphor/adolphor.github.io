package y2023.m03.d11.s02;

import static y2023.m03.d11.s02.Utils.*;

public class SimpleBlockedTask {
    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        SimpleBlockedTask simple = new SimpleBlockedTask();
        Credit credit = simple.calculateCreditForPerson(1L);
        long end = System.currentTimeMillis();
        System.out.println("耗时：" + (end - start) + " => " + credit);
    }

    public Credit calculateCreditForPerson(Long personId) throws Exception {
        var person = getPerson(personId);
        var assets = getAssets(person);
        var liabilities = getLiabilities(person);
        doSomethingElseImportant(person);
        return calculateCredit(assets, liabilities);
    }

}
