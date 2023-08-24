package algorithm.test;

/**
 * @version v1.0
 * @date 2022/8/13
 * @desc
 * @Since 2022/8/13 13:55
 **/
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

public class Main {
    static BufferedWriter out = new BufferedWriter(new OutputStreamWriter(System.out));
    public static void main(String args[]) throws IOException {


        Scanner sc = new Scanner(System.in);
        StringBuffer sb = new StringBuffer();
        while (sc.hasNext()){
            String tmp = sc.nextLine();
            if (tmp.endsWith(";")) {
                sb.append(tmp);
                System.out.printf(sb.toString().trim().replaceAll("\\s+", " "));
                sb = new StringBuffer();
            } else {
             sb.append(tmp);
            }

        }

    }
    public static boolean canFinish(int numCourses, int[][] prerequisites){

        LinkedList<Integer>[] graph=buildGraph(numCourses, prerequisites);

        //这里的路径不需要记录顺序，只要知道路径里有什么，并且可以每次减去索引路径就好。
        boolean[] onPath=new boolean[numCourses];
        //仍旧要使用visited数组防止重复遍历。
        boolean[] visited=new boolean[numCourses];

        for(int i=0; i<graph.length; i++){
            //if(!visited[i]){不需要，因为traverse一开头就判断是否visited
            boolean isHaveCycle=traverse(graph, i, onPath, visited);
            if(!isHaveCycle){
                return false;
            }
            //}
        }
        return true;

    }
    public static boolean traverse(LinkedList<Integer>[] graph, int index, boolean[] onPath, boolean[] visited) {

        if (onPath[index]) {
            return false;
        }
        if (visited[index]) {
            return true;
        }

        visited[index]=true;
        onPath[index]=true;

        for(int i=0; i<graph[index].size(); i++){
            int next=graph[index].get(i);
            boolean isHaveCycle=traverse(graph, next, onPath, visited);
            if (!isHaveCycle) {
                return false;
            }
        }
        onPath[index]=false;
        return true;
    }

    public static LinkedList<Integer>[] buildGraph(int n, int[][] pre){

        LinkedList<Integer>[] graph=new LinkedList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new LinkedList<>();
        }
        for(int i=0; i<pre.length; i++){
            int from=pre[i][1];
            int to=pre[i][0];
            graph[from].addLast(to);
        }

        return graph;
    }

}