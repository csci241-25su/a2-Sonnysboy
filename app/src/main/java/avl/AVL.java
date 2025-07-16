package avl;

public class AVL {

    public Node root;

    private int size;

    public int getSize() {
        return size;
    }

    /**
     * find w in the tree. return the node containing w or
     * null if not found
     */
    public Node search(String w) {
        return search(root, w);
    }

    private Node search(Node n, String w) {
        if (n == null) {
            return null;
        }
        if (w.equals(n.word)) {
            return n;
        } else if (w.compareTo(n.word) < 0) {
            return search(n.left, w);
        } else {
            return search(n.right, w);
        }
    }
    private void bstInsert(Node n, String w) {
        if (n.left == null && n.right == null) {
            if (lt(w, n.word)) {
                n.left = new Node(w, n);
            } else {
                n.right = new Node(w, n);
            }
        } else if (n.left == null) {
            if (lt(w, n.word)) {
                n.left = new Node(w, n);
            } else {
                bstInsert(n.right, w);
            }
        } else {
            if (lt(w, n.word)) {
                bstInsert(n.left, w);
            } else {
                n.right = new Node(w, n);
            }
        }
        n.height = 1 + Math.max(h(n.left), h(n.right));
    }
    /**
     * insert w into the tree as a standard BST, ignoring balance
     */
    public void bstInsert(String w) {
        if (search(w) != null) return;
        if (root == null) {
            root = new Node(w);
            size = 1;
            return;
        }
        ++size;
        bstInsert(root, w);
    }

//    private void bstInsert(Node n, String w) {
//        if (lt(w, n.word)) {
//            if (n.left == null) {
//                n.left = new Node(w, n);
//            } else {
//                bstInsert(n.left, w);
//            }
//        } else if (gt(w, n.word)) {
//            if (n.right == null) {
//                n.right = new Node(w, n);
//            } else {
//                bstInsert(n.right, w);
//            }
//        }
//        n.height = h(n);
//    }


    private int h(Node n) {
        if (n == null) return -1;
        return 1 + Math.max(h(n.left), h(n.right));
    }

    // for my sanity
    private <T extends Comparable<T>> boolean lt(T a, T b) {
        return a.compareTo(b) < 0;
    }

    private <T extends Comparable<T>> boolean gt(T a, T b) {
        return a.compareTo(b) > 0;
    }

    /**
     * insert w into the tree, maintaining AVL balance
     * precondition: the tree is AVL balanced and any prior insertions have been
     * performed by this method.
     */
    public void avlInsert(String w) {
        avlInsert(root, w);
        ++size;
    }

    /* insert w into the tree, maintaining AVL balance
     *  precondition: the tree is AVL balanced and n is not null */
    private void avlInsert(Node n, String w) {
        if (n == null) {
            root = new Node(w);
            return;
        }
        if (lt(w, n.word)) {
            if (n.left != null) avlInsert(n.left, w);
            else n.left = new Node(w, n);
        } else if (gt(w, n.word)){
            if (n.right != null) avlInsert(n.right, w);
            else n.right = new Node(w, n);
        }
        rebalance(n);
    }

    /**
     * do a left rotation: rotate on the edge from x to its right child.
     * precondition: x has a non-null right child
     */
    public void leftRotate(Node x) {
        if (x == null || x.right == null) return;
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) {
            y.left.parent = x;
        }
        y.parent = x.parent;
        if (x.parent == null) {
            root = y;
        } else if (x.parent.left == x) {
            x.parent.left = y;
        } else {
            x.parent.right = y;
        }
        y.left = x;
        x.parent = y;

        y.height = h(y);
        x.height = h(x);
    }

    /**
     * do a right rotation: rotate on the edge from x to its left child.
     * precondition: y has a non-null left child
     */
    public void rightRotate(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        x.right = y;
        y.left = T2;

        if (T2 != null) {
            T2.parent = y;
        }
        x.parent = y.parent;
        if (y.parent == null) {
            this.root = x;
        } else if (y == y.parent.right) {
            y.parent.right = x;
        } else {
            y.parent.left = x;
        }

        y.parent = x;

        y.height = h(y);
        x.height = h(x);
    }

    /**
     * rebalance a node N after a potentially AVL-violoting insertion.
     * precondition: none of n's descendants violates the AVL property
     */
    public void rebalance(Node node) {
        node.height = h(node);
        int b = bf(node);
        if (b > 1 && bf(node.left) >= 0) {
            rightRotate(node);
        } else if (b > 1 && bf(node.left) < 0) {
            leftRotate(node.left);
            rightRotate(node);
        } else if (b < -1 && bf(node.right) <= 0) {
            leftRotate(node);
        } else if (b < -1 && bf(node.right) > 0) {
            rightRotate(node.right);
            leftRotate(node);
        }
    }

    private int bf(Node n) {
        return n == null ? 0 : h(n.left) - h(n.right);
    }

    /**
     * remove the word w from the tree
     */
    public void remove(String w) {
        remove(root, w);
    }

    /* remove w from thve tree rooted at n */
    private void remove(Node n, String w) {
        return; // (enhancement TODO - do the base assignment first)
    }

    /**
     * print a sideways representation of the tree - root at left,
     * right is up, left is down.
     */
    public void printTree() {
        printSubtree(root, 0);
    }

    private void printSubtree(Node n, int level) {
        if (n == null) {
            return;
        }
        printSubtree(n.right, level + 1);
        for (int i = 0; i < level; i++) {
            System.out.print("        ");
        }
        System.out.println(n);
        printSubtree(n.left, level + 1);
    }

    /**
     * inner class representing a node in the tree.
     */
    public class Node {
        public String word;
        public Node parent;
        public Node left;
        public Node right;
        public int height;

        public String toString() {
            return word + "(" + height + ")";
        }

        /**
         * constructor: gives default values to all fields
         */
        public Node() {
        }

        /**
         * constructor: sets only word
         */
        public Node(String w) {
            word = w;
            height = 0;
        }

        /**
         * constructor: sets word and parent fields
         */
        public Node(String w, Node p) {
            word = w;
            parent = p;
            height = 0;
        }

        /**
         * constructor: sets all fields
         */
        public Node(String w, Node p, Node l, Node r) {
            word = w;
            parent = p;
            left = l;
            right = r;
            height = Math.max(h(l), h(r));
        }
    }

    public static String preOrder(AVL.Node n) {
        if (n == null) {
            return "";
        }
        String result = n.word
                + " " + preOrder(n.left)
                + " " + preOrder(n.right);

        return result.trim().replaceAll(" +", " ");
    }

}
