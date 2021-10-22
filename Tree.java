package main;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Tree {
    Node root;

    public Tree() {
        root = null;
    }

    public Node searchAndCount(int key) {
        int counter = 1;
        Node current = root;
        while (current.key != key) {
            if (key < current.key) {// <-
                current = current.leftChild;
                counter++;
            } else {// ->
                current = current.rightChild;
                counter++;
            }
            if (current == null) {
                System.out.println("NOT FOUND! (" + counter + " steps)");
                return null;
            }
        }
        System.out.println("FOUND! (" + counter + " steps)");
        return current;
    }

    public Node search(int key) {//no counter
        Node current = root;
        while (current.key != key) {
            if (key < current.key)// <-
                current = current.leftChild;
            else// ->
                current = current.rightChild;
            if (current == null)
                return null;
        }
        return current;
    }


    public void insert(int key, String data) {
        Node newNode = new Node();
        newNode.key = key;
        newNode.data = data;
        if (root == null)
            root = newNode;
        else {
            if (search(key) == null) {
                Node current = root;
                Node parent;
                while (true) {
                    parent = current;
                    if (key < current.key) {// <-
                        current = current.leftChild;
                        if (current == null) {
                            parent.leftChild = newNode;
                            return;
                        }
                    } else {// ->
                        current = current.rightChild;
                        if (current == null) {
                            parent.rightChild = newNode;
                            return;
                        }
                    }
                }
            } else {
                System.out.println("Cant add duplicate keys! ( " + key + " )");
            }
        }
    }


    public boolean delete(int key) {
        if (root != null) {
            Node current = root;
            Node parent = root;
            boolean isLeftChild = true;

            while (current.key != key) {
                parent = current;
                if (key < current.key) {// <-
                    isLeftChild = true;
                    current = current.leftChild;
                } else {// ->
                    isLeftChild = false;
                    current = current.rightChild;
                }
                if (current == null)
                    return false;
            }

            //found
            if (current.leftChild == null && current.rightChild == null) {// - -
                if (current == root)
                    root = null;
                else if (isLeftChild)
                    parent.leftChild = null;
                else
                    parent.rightChild = null;
            } else if (current.rightChild == null)// + -
                if (current == root)
                    root = current.leftChild;
                else if (isLeftChild)
                    parent.leftChild = current.leftChild;
                else
                    parent.rightChild = current.leftChild;

            else if (current.leftChild == null)// - +
                if (current == root)
                    root = current.rightChild;
                else if (isLeftChild)
                    parent.leftChild = current.rightChild;
                else
                    parent.rightChild = current.rightChild;

            else {// + +
                Node successor = getSuccessor(current);
                if (current == root)
                    root = successor;
                else if (isLeftChild)
                    parent.leftChild = successor;
                else
                    parent.rightChild = successor;

                successor.leftChild = current.leftChild;
            }
            return true;
        }
        return false;
    }

    private Node getSuccessor(Node delNode) {
        Node successorParent = delNode;
        Node successor = delNode;
        Node current = delNode.rightChild;
        while (current != null) {//направо пока есть левые
            successorParent = successor;
            successor = current;
            current = current.leftChild;
        }

        if (successor != delNode.rightChild) {//усли не правый соединяем
            successorParent.leftChild = successor.rightChild;
            successor.rightChild = delNode.rightChild;
        }
        return successor;
    }

    public void printTraverse(Node localRoot) {
        if (localRoot != null) {
            System.out.print("{" + localRoot.key + ":" + localRoot.data + "} ");
            printTraverse(localRoot.leftChild);
            printTraverse(localRoot.rightChild);
        }
    }

    StringBuilder tempKeys = new StringBuilder();
    StringBuilder tempDatas = new StringBuilder();

    public void getEverything(Node node) {
        if (node != null) {
            tempKeys.append(node.key).append("\n");
            ;
            tempDatas.append(node.data).append("\n");
            ;
            getter(node.leftChild);
            getter(node.rightChild);
        }
    }

    public void getter(Node node) {
        if (node != null) {
            getEverything(node);
        }
    }

    public void save() throws IOException {
        tempKeys = new StringBuilder();
        tempDatas = new StringBuilder();
        getEverything(root);

        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream("keys.bin"))) {
            dos.writeUTF(tempKeys.toString());
        }
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream("datas.bin"))) {
            dos.writeUTF(tempDatas.toString());
        }
        System.out.println("\nSAVED TO FILE!");
    }


    private ArrayList<String> getFileContent(String pathname) {
        ArrayList<String> strings = new ArrayList<>();
        try (DataInputStream dis = new DataInputStream(new FileInputStream(pathname))) {
            strings.addAll(Arrays.asList(dis.readUTF().split("\n")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strings;
    }

    public void restore() {
        ArrayList<String> keys = getFileContent("keys.bin");
        ArrayList<String> datas = getFileContent("datas.bin");

        for (int i = 1; i < datas.size(); i++) {
            insert(Integer.parseInt(keys.get(i)), datas.get(i));
        }
        System.out.println("RESTORED FROM FILE!");
    }


    public void print() {
        Stack globalStack = new Stack();
        globalStack.push(root);
        int nBlanks = 32;
        boolean isRowEmpty = false;

        while (isRowEmpty == false) {
            Stack localStack = new Stack();
            isRowEmpty = true;

            for (int j = 0; j < nBlanks; j++)
                System.out.print(' ');

            while (globalStack.isEmpty() == false) {
                Node temp = (Node) globalStack.pop();
                if (temp != null) {
                    System.out.print(temp.key);
                    localStack.push(temp.leftChild);
                    localStack.push(temp.rightChild);

                    if (temp.leftChild != null ||
                            temp.rightChild != null)
                        isRowEmpty = false;
                } else {
                    System.out.print("--");
                    localStack.push(null);
                    localStack.push(null);
                }
                for (int j = 0; j < nBlanks * 2 - 2; j++)
                    System.out.print(' ');
            }
            System.out.println();
            nBlanks /= 2;
            while (localStack.isEmpty() == false)
                globalStack.push(localStack.pop());
        }
    }
}