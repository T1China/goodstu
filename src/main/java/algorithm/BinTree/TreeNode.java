package algorithm.BinTree;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @version v1.0
 * @date 2023/8/17
 * @desc
 * @Since 2023/8/17 11:21
 **/
public class TreeNode {
    int value;
    TreeNode left;
    TreeNode right;
    TreeNode(int a) {
        this.value = a;
    }

    TreeNode(int a, TreeNode left, TreeNode right) {
        this.value = a;
        this.left = left;
        this.right = right;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }


    public static TreeNode createBinTree(int[] arr) {
        if (arr.length == 0) return  null;
        ArrayList<TreeNode> list = new ArrayList<TreeNode>();
        for (int i: arr) {
            list.add(new TreeNode(i));
        }
        TreeNode root = list.get(0);
        int arrLen = arr.length;
        for (int i = 0; i < arrLen/2 - 1; i++) {
            if (i*2+1 < arrLen) {
                list.get(i).setLeft(list.get(i*2+1));
            }
            if (i*2+2 < arrLen) {
                list.get(i).setRight(list.get(i*2+2));
            }
        }
        TreeNode nod4 = root.left.left;
        int lastParentIndex = arr.length/2-1;
        list.get(lastParentIndex).setLeft(list.get(arrLen-1));
        return root;
    }

    public static void preOrder(TreeNode node, ArrayList<Integer> list) {
        if (node != null){
            list.add(node.value);
            if (node.left != null) preOrder(node.left, list);
            if (node.right != null)  preOrder(node.right, list);
        }
    }


    public static void preOrder2(TreeNode node, ArrayList<Integer> list) {
        LinkedList<TreeNode> stack = new LinkedList<TreeNode>();
        TreeNode tmpNode = node;
        while (tmpNode != null || !stack.isEmpty()){
            if (tmpNode != null) {
                list.add(tmpNode.value);
                stack.push(tmpNode);
                tmpNode = tmpNode.left;
            } else {
                TreeNode nod = stack.pop();
                tmpNode = nod.right;
            }
        }
    }


    public int getMaxIdx(LinkedList<Integer> list, int idx, int newValue) {
        if (idx < 0) {
            int tmpMaxIdx = 0;
            int tmpMax = list.get(0);
            for (int i = 1; i < list.size(); i++) {
             if (tmpMax < list.get(i)) {
                 tmpMax = list.get(i);
                 tmpMaxIdx = i;
             }
            }
        } else {
            if (idx == 0) {

            }
        }

        return value;
    }

    public static void inOrder2(TreeNode node, ArrayList<Integer> list) {
        LinkedList<TreeNode> stack = new LinkedList<TreeNode>();
        TreeNode tmpNode = node;
        while (tmpNode != null || !stack.isEmpty()){
            if (tmpNode != null) {
                stack.push(tmpNode);
                tmpNode = tmpNode.left;
            } else {
                TreeNode nod = stack.pop();
                list.add(nod.value);
                tmpNode = nod.right;
            }
        }
    }



    public static void postOrder2(TreeNode node, ArrayList<Integer> list) {
        LinkedList<TreeNode> stack = new LinkedList<TreeNode>();
        TreeNode tmpNode = node;
        while (tmpNode != null || !stack.isEmpty()){
            if (tmpNode != null) {
                stack.push(tmpNode);
                tmpNode = tmpNode.left;
            } else {
                TreeNode nod = stack.pop();
                list.add(nod.value);
                tmpNode = nod.right;
            }
        }
    }

    public static void inOrder(TreeNode node, ArrayList<Integer> list) {
        if (node != null){
            if (node.left != null) inOrder(node.left, list);
            list.add(node.value);
            if (node.right != null)  inOrder(node.right, list);
        }
    }

    public static void postOrder(TreeNode node, ArrayList<Integer> list) {
        if (node != null){
            if (node.left != null) postOrder(node.left, list);
            if (node.right != null)  postOrder(node.right, list);
            list.add(node.value);
        }
    }

    public static void order(TreeNode node, ArrayList<Integer> list) {
        LinkedList<TreeNode> queue = new LinkedList<TreeNode>();
        queue.add(node);
        TreeNode tmpNode;
        while (!queue.isEmpty()){
            tmpNode = queue.poll();
            list.add(tmpNode.value);
            if (tmpNode.left != null) queue.add(tmpNode.left);
            if (tmpNode.right != null)  queue.add(tmpNode.right);
        }
    }

    public static void main(String[] args) {
        int[] arr = {1,2,3,4,5,6,7,8};
        TreeNode nod = TreeNode.createBinTree(arr);
        ArrayList<Integer> preList= new ArrayList<Integer>();
        TreeNode.preOrder(nod, preList);
        System.out.println(preList.toString());
        ArrayList<Integer> preList2= new ArrayList<Integer>();
        TreeNode.preOrder2(nod, preList2);
        System.out.println(preList2.toString());

        ArrayList<Integer> inList= new ArrayList<Integer>();
        TreeNode.inOrder(nod, inList);
        System.out.println(inList.toString());

        ArrayList<Integer> inList2= new ArrayList<Integer>();
        TreeNode.inOrder2(nod, inList2);
        System.out.println(inList.toString());


        ArrayList<Integer> postList= new ArrayList<Integer>();
        TreeNode.postOrder(nod, postList);
        System.out.println(postList.toString());

        ArrayList<Integer> postList2 = new ArrayList<Integer>();
        TreeNode.postOrder2(nod, postList2);
        System.out.println(postList2.toString());

        ArrayList<Integer> list = new ArrayList<Integer>();
        TreeNode.order(nod, list);
        System.out.println(list.toString());
    }
}