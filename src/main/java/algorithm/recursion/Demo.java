package algorithm.recursion;

/**
 * @version v1.0
 * @date 2022/1/24
 * @desc 递归模板
 * @Since 2022/1/24 19:12
 **/
public class Demo {
    int MAX_LEVEL = 10;
    public void recur(int lv, int param) {

        //terminator
        if (lv > MAX_LEVEL) {
            // process result
            return;
        }

        //process current logic
        int newParam = process(lv, param);

        //drill down
        recur(lv + 1, newParam);

        //restore current status
        if (newParam <0) {

        }
    }

    private int process(int lv, int param) {
        return lv;
    }
}
