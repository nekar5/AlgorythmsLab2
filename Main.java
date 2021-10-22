package main;

import java.io.IOException;

class Main {
    public static void main(String[] args) throws IOException {
        int value;
        Tree testTree = new Tree();

        /*
        testTree.restore();
        testTree.print();
        */


        testTree.insert(104, "one");
        testTree.insert(142, "two");
        testTree.insert(555, "three");
        testTree.insert(546, "four");
        testTree.insert(999, "five");
        testTree.insert(123, "six");
        testTree.insert(1, "seven");
        testTree.insert(456, "eight");
        testTree.insert(99, "nine");
        testTree.insert(111, "ten");
        testTree.print();
        testTree.searchAndCount(456).printNode();
        System.out.println();
        /*
        testTree.insert(80, "example");
        testTree.print();
        */

        //test
        /*
        testTree.insert(999, "insert duplicate test");
        testTree.insert(80, "insert test");
        testTree.delete(555);
        testTree.print();
        */
        /*
        testTree.searchAndCount(104);
        testTree.searchAndCount(142);
        testTree.searchAndCount(555);
        testTree.searchAndCount(546);
        testTree.searchAndCount(999);
        testTree.searchAndCount(123);
        testTree.searchAndCount(1);
        testTree.searchAndCount(456);
        testTree.searchAndCount(99);
        testTree.searchAndCount(111);
        testTree.searchAndCount(1111);//test

         */

        testTree.printTraverse(testTree.root);
        //testTree.save();
    }
}