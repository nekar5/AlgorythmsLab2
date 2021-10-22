package main;

public class Node {
    public int key;
    public String data;
    public Node leftChild;
    public Node rightChild;

    public void printNode()
    {
        System.out.print('{');
        System.out.print(key);
        System.out.print(", ");
        System.out.print(data);
        System.out.print("} ");
    }
}
