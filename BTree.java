import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

class BTree {
    Node root;
    int t;

    public BTree(int t) {
        this.root = null;
        this.t = t;
    }

    public void insert(int key, String data) {
        if (root == null) {//create root
            root = new Node(t, true);
            root.keys[0] = key;
            root.data[0] = data;
            root.num = 1;
        } else {
            if (root.num == 2 * t - 1) {//root full - new el. is its child
                Node s = new Node(t, false);
                s.child[0] = root;
                s.split(0, root);//new root has 2 childs
                int i = 0;
                if (s.keys[0] < key)
                    i++;
                s.child[i].insertNew(key, data);

                root = s;
            } else//insert to already existing tree
                root.insertNew(key, data);
        }
    }

    public void delete(int key) {
        if (root == null) {
            System.out.println("empty tree");
            return;
        }
        root.remove(key);
        if (root.num == 0) {
            if (root.isLeaf)
                root = null;
            else
                root = root.child[0];
        }
    }

    public String search(int key) {
        String res;
        int[] comparisons = new int[1];
        Node node;
        if (root == null) node = null;
        else node = root.search(key, comparisons);
        if (node == null) {
            res = "error";
        } else {
            System.out.println("comparisons to find " + key + ": " + comparisons[0]);
            res = node.data[node.findKey(key)];
        }

        return res;
    }

    private ArrayList<String> getFromFile(String pathname) {
        ArrayList<String> strings = new ArrayList<>();
        try (DataInputStream dis = new DataInputStream(new FileInputStream(pathname))) {
            strings.addAll(Arrays.asList(dis.readUTF().split("\n")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return strings;
    }

    public void restore() {
        ArrayList<String> intArr = getFromFile("keys.bin");
        ArrayList<String> strArr = getFromFile("datas.bin");
        this.t = Integer.parseInt(intArr.get(0));
        for (int i = 1; i < strArr.size(); i++) {
            insert(Integer.parseInt(intArr.get(i)), strArr.get(i));
        }
    }

    public void save() throws IOException {
        ArrayList<Integer> intArr = new ArrayList<>();
        ArrayList<String> strArr = new ArrayList<>();
        root.traverse(intArr, strArr);
        StringBuilder builderInt = new StringBuilder();
        StringBuilder builderStr = new StringBuilder();
        builderInt.append(this.t).append("\n");
        for (int i = 0; i < intArr.size(); i++) {
            int intEl = intArr.get(i);
            String strEl = strArr.get(i);
            builderInt.append(intEl).append("\n");
            builderStr.append(strEl).append("\n");
        }
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream("keys.bin"))) {
            dos.writeUTF(builderInt.toString());
        }
        try (DataOutputStream dos = new DataOutputStream(new FileOutputStream("datas.bin"))) {
            dos.writeUTF(builderStr.toString());
        }

    }

    public void traverse() {
        if (root != null) {
            ArrayList<Integer> keys = new ArrayList<>();
            ArrayList<String> valuesArray = new ArrayList<>();
            root.traverse(keys, valuesArray);
            for (int i = 0; i < keys.size(); i++) {
                int key = keys.get(i);
                String data = valuesArray.get(i);
                System.out.printf("%d : %s |", key, data);
            }
        }
        System.out.println();
    }
}