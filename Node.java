import java.util.ArrayList;

class Node {
    int[] keys;
    String[] data;
    int t;
    Node[] child;
    int num;
    boolean isLeaf;

    public Node(int t, boolean isLeaf) {
        this.t = t;
        this.isLeaf = isLeaf;
        this.keys = new int[2 * this.t - 1];
        this.data = new String[2 * this.t - 1];
        this.child = new Node[2 * this.t];
        this.num = 0;
    }

    public int findKey(int key) {
        int n = 0;
        while (n < num && keys[n] < key)
            ++n;
        return n;
    }


    public void insertNew(int key, String newData) {
        int i = num - 1;

        if (isLeaf) {
            while (i >= 0 && keys[i] > key) {//find pos if is leaf
                keys[i + 1] = keys[i];
                data[i + 1] = data[i];
                i--;
            }
            keys[i + 1] = key;
            data[i + 1] = newData;
            num++;
        } else {
            while (i >= 0 && keys[i] > key)//find pos if is not leaf
                i--;
            if (child[i + 1].num == 2 * t - 1) {//child full - split
                split(i + 1, child[i + 1]);
                if (keys[i + 1] < key)
                    i++;
            }
            child[i + 1].insertNew(key, newData);
        }
    }

    public void split(int i, Node y) {

        Node z = new Node(y.t, y.isLeaf);
        z.num = t - 1;

        for (int j = 0; j < t - 1; j++) {
            z.keys[j] = y.keys[j + t];
            z.data[j] = y.data[j + t];
        }
        if (!y.isLeaf) {
            if (t >= 0) System.arraycopy(y.child, t, z.child, 0, t);
        }
        y.num = t - 1;

        if (num + 1 - (i + 1) >= 0) System.arraycopy(child, i + 1, child, i + 1 + 1, num + 1 - (i + 1));
        child[i + 1] = z;

        for (int j = num - 1; j >= i; j--) {
            keys[j + 1] = keys[j];
            data[j + 1] = data[j];
        }

        keys[i] = y.keys[t - 1];
        data[i] = y.data[t - 1];

        num++;
    }

    public void traverse(ArrayList<Integer> keys, ArrayList<String> datas) {
        int i;
        for (i = 0; i < num; i++) {
            if (!isLeaf)
                child[i].traverse(keys, datas);
            keys.add(this.keys[i]);
            datas.add(data[i]);
        }

        if (!isLeaf) {
            child[i].traverse(keys, datas);
        }
    }

    public Node search(int key, int[] comparisons) {
        int high = num;
        int low = 0;
        int medium = (high + low) / 2;
        while (low <= high) {
            comparisons[0]++;
            medium = (high + low) / 2;
            if(keys[medium] == key) {
                comparisons[0] += 1;
                return this;
            } else if (keys[medium] < key) {
                comparisons[0] += 2;
                low = medium + 1;
            } else {
                high = medium - 1;
            }
        }

        if (isLeaf)
            return null;
        return child[medium].search(key, comparisons);
    }

    public void remove(int key) {
        int n = findKey(key);
        if (n < num && keys[n] == key) {
            if (isLeaf)
                removeFromLeaf(n);//if is leaf
            else
                removeFromNonLeaf(n);//if not
        } else {
            if (isLeaf) {//if is leaf
                System.out.print("no such key");
                return;
            }

            boolean flag = n == num;
            if (child[n].num < t)
                fill(n);
            if (flag && n > num)
                child[n - 1].remove(key);
            else
                child[n].remove(key);
        }
    }

    public void removeFromLeaf(int n) {
        for (int i = n + 1; i < num; ++i) {
            keys[i - 1] = keys[i];
            data[i - 1] = data[i];
        }
        num--;
    }

    public void removeFromNonLeaf(int n) {
        int key = keys[n];

        if (child[n].num >= t) {
            int predInt = getPredInt(n);
            String predStr = getPredStr(n);
            data[n] = predStr;
            keys[n] = predInt;
            child[n].remove(predInt);
        } else if (child[n + 1].num >= t) {
            int succ = getSuccessor(n);
            keys[n] = succ;
            child[n + 1].remove(succ);
        } else {
            merge(n);
            child[n].remove(key);
        }
    }

    public int getPredInt(int n) { // шукаємо крайній правий вузол із лівого піддерева
        Node current = child[n];
        while (!current.isLeaf)
            current = current.child[current.num];
        return current.keys[current.num - 1];
    }

    public String getPredStr(int n) {
        Node current = child[n];
        while (!current.isLeaf)
            current = current.child[current.num];
        return current.data[current.num - 1];
    }

    public int getSuccessor(int n) {
        Node cur = child[n + 1];
        while (!cur.isLeaf)
            cur = cur.child[0];
        return cur.keys[0];
    }


    public void fill(int n) {

        if (n != 0 && child[n - 1].num >= t)
            takeFromPrev(n);

        else if (n != num && child[n + 1].num >= t)
            takeFromNext(n);
        else {
            if (n != num)
                merge(n);
            else
                merge(n - 1);
        }
    }

    public void takeFromPrev(int n) {
        Node child = this.child[n];
        Node sibling = this.child[n - 1];

        for (int i = child.num - 1; i >= 0; --i) {
            child.keys[i + 1] = child.keys[i];
            child.data[i + 1] = child.data[i];
        }


        if (!child.isLeaf) {
            if (child.num + 1 >= 0) System.arraycopy(child.child, 0, child.child, 1, child.num + 1);

        }

        child.keys[0] = keys[n - 1];
        child.data[0] = data[n - 1];
        if (!child.isLeaf)
            child.child[0] = sibling.child[sibling.num];

        keys[n - 1] = sibling.keys[sibling.num - 1];
        data[n - 1] = sibling.data[sibling.num - 1];
        child.num += 1;
        sibling.num -= 1;
    }

    public void takeFromNext(int n) {
        Node child = this.child[n];
        Node sibling = this.child[n + 1];

        child.keys[child.num] = keys[n];
        child.data[child.num] = data[n];

        if (!child.isLeaf)
            child.child[child.num + 1] = sibling.child[0];

        keys[n] = sibling.keys[0];
        data[n] = sibling.data[0];

        for (int i = 1; i < sibling.num; ++i) {
            sibling.keys[i - 1] = sibling.keys[i];
            sibling.data[i - 1] = sibling.data[i];
        }


        if (!sibling.isLeaf) {
            if (sibling.num >= 0) System.arraycopy(sibling.child, 1, sibling.child, 0, sibling.num);
        }
        child.num += 1;
        sibling.num -= 1;
    }

    public void merge(int n) {
        Node child = this.child[n];
        Node sibling = this.child[n + 1];

        child.keys[t - 1] = keys[n];
        child.data[t - 1] = data[n];

        for (int i = 0; i < sibling.num; ++i) {
            child.keys[i + t] = sibling.keys[i];
            child.data[i + t] = sibling.data[i];
        }


        if (!child.isLeaf) {
            if (sibling.num + 1 >= 0) System.arraycopy(sibling.child, 0, child.child, 0 + t, sibling.num + 1);
        }

        for (int i = n + 1; i < num; ++i) {
            keys[i - 1] = keys[i];
            data[i - 1] = data[i];
        }

        if (num + 1 - (n + 2) >= 0) System.arraycopy(this.child, n + 2, this.child, n + 2 - 1, num + 1 - (n + 2));

        child.num += sibling.num + 1;
        num--;
    }
}




































            /*
            if (medium == 0 && keys[medium] > key) {
                break;
            } else if (medium == num - 1 && keys[medium] < key) {
                medium++;
                break;
            } else if (keys[medium] < key && keys[medium + 1] > key) {
                medium++;
                break;
            } else if (keys[medium] > key && keys[medium - 1] < key) {
                break;
            } else if (keys[medium] > key) {
                high = medium - 1;
            } else if (keys[medium] < key) {
                low = medium + 1;
            } else {
                return this;
            }*/