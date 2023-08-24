package com.txu.job;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import scala.collection.mutable.ArrayBuffer;
import scala.util.matching.Regex;


import java.util.Scanner;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * @version v1.0
 * @date 2022/2/18
 * @desc
 * @Since 2022/2/18 16:04
 **/
public class FourComputer {
    public static void main(String args[]) {
        while (true) {
            Scanner cin = new Scanner(System.in);
            String  str = cin.nextLine().trim();
//            StringBuffer[] buffer = new StringBuffer[str.length()];
//            StringBuffer tmp = new StringBuffer();
//            int curIndex = 0;
//            int maxIndex = 0;
//            String tmpStr = "";
//            for (char i : str.toCharArray()) {
//                tmpStr = String.valueOf(i);
//                if (buffer[curIndex]==null) {
//                    buffer[curIndex]=new StringBuffer();
//                }
//                StringBuffer tmpb = buffer[curIndex];
//                switch (tmpStr) {
//                    case "-" :
//                    case "+" :
//                    case "*" :
//                        if (tmp.length()>0){
//                            tmpb.append(tmp + ","+ tmpStr);
//                            tmp = new StringBuffer();
//                        }
//
//                        break;
//                    case "(" :
//                        tmpb.append(","+ tmp );
//                        tmp = new StringBuffer();
//                        curIndex++;
//                        maxIndex++;
//                        break;
//                    case ")" :
//                        tmpb.append(","+ tmp );
//                        tmp = new StringBuffer();
//                        curIndex--;
//                        break;
//                    default  :
//                        tmp.append(tmpStr);
//                        break;
//                }
//
//            }

            System.out.println(StringEscapeUtils.escapeJava(str));

        }
    }
}
