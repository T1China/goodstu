package com.imooc.scala;


public class RegexTestJava {

    public static void main(String[] args) {

//        String arr[][] = {{"asd?","","*","asdsa|*dss","d*sd","a*a","dsa|dasd","dasd","das*","d**d","d?sd","f*s*a","fasf*","*fasf","fa*sf","*fasf*","*d*a*sd*fq"},
//                {"asd","dd","saqd","adss","dasd","adsa","dasd","dasd","dasda","dasd","dasd","fasfa","fasf","fasf","fasf","fasf","dasdfq"}
//        };
//        for (int i = 0; i < arr[0].length; i++) {
//            System.out.println(i+ ": " +JWildcard.matches(arr[0][i], arr[1][i]));
//            if(!JWildcard.matches(arr[0][i], arr[1][i])==com.yevdo.jwildcard.JWildcard.matches(arr[0][i], arr[1][i])) System.out.printf( "ERR");
//        }
//        System.out.print(Pattern.matches("^.*[0-9]$", "Grapha"));
//        System.out.print(SWildcard.matches("abd*|add?|a*d|a?d|*ad|?ad", "ahwerywbdc"));
//        System.out.print(com.yevdo.jwildcard.JWildcard.matches("abd*", "abdc"));
        String a = "asda";
        System.out.print( a.substring(0, a.length() - 1));
    }
}
