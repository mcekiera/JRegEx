package Model;

import java.util.*;

public class RegexInputInterpreter {

    public Map<ElementType,Indices> getElements(String pattern) {
        Map<ElementType,Indices> elements = new LinkedHashMap<>();

        return elements;
    }



    public List<Indices> getUnbalancedBrackets(String patter) {
        java.util.List<Integer> temp = new ArrayList<Integer>();
        temp.addAll(getUnbalancedStructure(patter, '(', ')'));
        temp.addAll(getUnbalancedStructure(patter, '[', ']'));
        List<Indices> result = new ArrayList<Indices>();
        for(Integer i : temp) {
            result.add(new Indices(i,i+1));
        }
        return result;
    }

    private java.util.List<Integer> getUnbalancedStructure(String patter, char open, char close) {
        Stack<Integer> start = new Stack<Integer>();
        Stack<Integer> end = new Stack<Integer>();
        for (int i = 0; i < patter.length(); i++) {
            char ch = patter.charAt(i);
            if(ch == '\\') {
                i++;
                continue;
            }
            if(ch == open) {
                start.push(i);
            }else if(ch == close) {
                if(start.size()!=0) {
                    start.pop();
                }else {
                    end.push(i);
                }
            }
        }
        java.util.List<Integer> result = new ArrayList<>(start);
        result.addAll(end);
        return result;
    }
}
