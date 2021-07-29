package com.txu.vectorization;

import com.txu.scala.crux.SWildcard;
import scala.collection.immutable.Set;

public class RegexVectorTest {


    static void foo(int[] a, int[] b, int[] c) {
        for (int i = 0; i < a.length; i++) {
            c[i] = a[i] + b[i];
        }
    }

    static void foo1(String[] a , String[] c) {
        for (int i = 0; i < a.length; i++) {
            c[i] = a[i].substring(0,a[i].length()-1);
        }
    }

    static void foo2(String[] a, String b, String[] c) {
        for (int i = 0; i < a.length; i++) {
            c[i] = a[i].equals(b) ? a[i]:"";
        }
    }

    static void foo3(String[] a, String b, String[] c) {
        for (int i = 0; i < a.length; i++) {
            c[i] = a[i].substring(0,b.length()).equals(b) ? a[i]:"";
        }
    }
    static void foo4(String[] a, String b, String[] c) {
        Set<String> regex = SWildcard.splitWildcardToRegexSet(b);
        for (int i = 0; i < a.length; i++) {
            boolean f = SWildcard.matches(regex,a[i]);
            c[i] = f ? a[i]:"";
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int[] a = new int[] { 1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 6, 7, 8 };
        int[] c = new int[16];
//        for (int i = 0; i < 20_000; i++) {
//            foo(a, a, c);
//        }
        Thread.sleep(2000);

        String[] a1 = new String[]{ "sa", "sad", "sadd", "das", "saddd", "saddd", "sas", "sff" };
        String[] a2 = new String[]{ "sa", "asd", "dasd", "das", "saddd", "saddd", "sas", "sff" };
        String[] c1 = new String[8];
        String[] d1 = new String[8];
        String[] d2 = new String[8];
        String[] d3 = new String[8];
        String b = "sa*";
//        long t1 = System.currentTimeMillis();
//        b = b.substring(0,b.length()-1);
//        for (int i = 0; i < 20_0000; i++) {
//            foo1(a2, c1);
//            foo2(c1, b, d1);
//        }
//        long t2 = System.currentTimeMillis();
//        System.out.println(t2 - t1);
//        Thread.sleep(2000);
        long t3 = System.currentTimeMillis();
        String b1 = b.substring(0,b.length()-1);
        for (int i = 0; i < 20_000; i++) {
            foo3(a1, b1, d2);
        }
        long t4 = System.currentTimeMillis();
        System.out.println(t4 - t3);

        long t5 = System.currentTimeMillis();
        for (int i = 0; i < 20_0000; i++) {
            foo4(a1, b, d3);
        }
        long t6 = System.currentTimeMillis();
        System.out.println(t6 - t5);
        for (String s : a1) {
            System.out.print(s+ " ");
        }
        System.out.println(" ");
        for (String s : d1) {
            System.out.print(s+ " ");
        }
        System.out.println(" ");
        for (String s : d2) {
            System.out.print(s+ " ");
        }
        System.out.println(" ");
        for (String s : d3) {
            System.out.print(s+ " ");
        }
        System.out.println(" ");
    }

}
