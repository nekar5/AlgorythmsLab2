import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException {
        BTree testTree = new BTree(10);
        //testTree.restore();
        Random rand = new Random();

        String[] testData = new String[] {"one", "two", "three", "four", "five"
                , "six", "seven", "eight", "nine", "ten"
                , "eleven", "twelve", "thirteen", "fourteen", "fifteen"
                , "sixteen", "seventeen", "eighteen", "nineteen", "twenty"};
        for (String s : testData) {
            testTree.insert(rand.nextInt(10000), s);
        }

        testTree.insert(5, "test1");
        testTree.insert(15, "test2");
        testTree.insert(55, "test3");
        testTree.insert(1005, "test4");
        testTree.insert(1505, "test5");
        testTree.insert(2005, "test6");
        testTree.insert(2505, "test7");
        testTree.insert(3005, "test8");
        testTree.insert(3505, "test9");
        testTree.insert(4005, "test10");
        testTree.insert(4505, "test11");
        testTree.insert(5005, "test12");
        testTree.insert(5505, "test13");
        testTree.insert(6005, "test14");
        testTree.insert(6505, "test15");

        testTree.traverse();

        testTree.search(5);
        testTree.search(15);
        testTree.search(55);
        testTree.search(1005);
        testTree.search(1505);
        testTree.search(2005);
        testTree.search(2505);
        testTree.search(3005);
        testTree.search(3505);
        testTree.search(4005);
        testTree.search(4505);
        testTree.search(5005);
        testTree.search(5505);
        testTree.search(6005);
        testTree.search(6505);


        testTree.delete(5);
        testTree.delete(1505);
        testTree.delete(2505);
        testTree.traverse();


        testTree.save();
    }
}
