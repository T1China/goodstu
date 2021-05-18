package com.imooc.synchronized_;

/**
 * @version v1.0
 * @date 2021/5/12
 * @desc
 * @Since 2021/5/12 10:16
 **/
public class Test1 {
    public static void main(String[] args) {
        synchronized ( Test1.class){
            System.out.print(1);
        }
    }
}
