package Constructs;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {

    public static void main(String[] args) {
        Matcher matcher = Pattern.compile("a").matcher("");
        if(matcher.reset("aaa").find()) {
            System.out.println(matcher.group());
        } else if(matcher.reset("b").find()) {
            System.out.println(matcher.group());
        }



    }
}


//todo inny podzia³: pointmaching, charactermaching, conceptmatching, simple and composed
