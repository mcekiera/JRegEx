package Constructs;

import Constructs.Types.*;
import Expression.Expression;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConstructsFactory {
    private Pattern backslash = Pattern.compile("^((?:\\\\[dDsSwWtnrfaebBAGZzphHvVRP](?:\\{\\p{L}+})?)|(?:\\\\.)).*");
    private Pattern charClass = Pattern.compile("^(\\[[^\\[]*&&\\[[^\\]]+\\]\\]|\\[[^\\]]+\\]).*");
    private Pattern group;
    private Pattern metachar = Pattern.compile("^([.^$|]).+");
    private Pattern quantifier = Pattern.compile("^([?+*][?+]?|\\{\\d+(?:,(?:\\d)?)?\\}).*");
    private Pattern quotation = Pattern.compile("^(\\\\Q((?!\\\\E).)+\\\\E).*");
    private Pattern mode = Pattern.compile("^(\\(?:\\?[ixmsud]\\)).*");
    private Pattern specialBackslash = Pattern.compile("^(\\\\(?:0[0-7]{1,3}|c\\w|x(?:\\{[^}]+}|[0-9a-fA-F]{2})|u[0-9a-fA-F]{4})).*");
    private Pattern logical = Pattern.compile("^(\\||&&).*");
    private static final ConstructsFactory instance = new ConstructsFactory();

    private ConstructsFactory() {}

    public Construct create(Expression expression, int patternIndex) {
        Matcher matcher;
        if((matcher = specialBackslash.matcher(expression.getPattern().toString().substring(patternIndex))).find()) {
            return new SpecialBackslash(matcher.group(1));
        } else if((matcher = quotation.matcher(expression.getPattern().toString().substring(patternIndex))).find()) {
            return new Quotation(matcher.group(1));
        } else if((matcher = backslash.matcher(expression.getPattern().toString().substring(patternIndex))).find()) {
            return new Backslash(matcher.group(1));
        } else if((matcher = charClass.matcher(expression.getPattern().toString().substring(patternIndex))).find()) {
            return new CharClass(matcher.group(1));
        } else if((matcher = metachar.matcher(expression.getPattern().toString().substring(patternIndex))).find()) {
            return new Metacharacter(matcher.group(1));
        } else if((matcher = quantifier.matcher(expression.getPattern().toString().substring(patternIndex))).find()) {
            return new Quantifier(matcher.group(1));
        } else if((matcher = mode.matcher(expression.getPattern().toString().substring(patternIndex))).find()) {
            return new Mode(matcher.group(1));
        } else if((matcher = logical.matcher(expression.getPattern().toString().substring(patternIndex))).find()) {
            return new Logical(matcher.group(1));
        } else {
            return new SimpleChar(expression.getPattern().toString().substring(patternIndex,patternIndex+1));
        }
    }

    public static ConstructsFactory getInstance() {
        return instance;
    }
}
