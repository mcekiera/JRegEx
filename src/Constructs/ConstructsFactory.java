package Constructs;

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
    private static final ConstructsFactory instance = new ConstructsFactory();

    private ConstructsFactory() {}

    public Construct create(Expression expression, int patternIndex) {
        Matcher matcher;
        if(specialBackslash.matcher(expression.getPattern().toString().substring(patternIndex)).find()) {
            matcher = specialBackslash.matcher(expression.getPattern().toString().substring(patternIndex));
            matcher.find();
            return new SpecialBackslash(matcher.group(1));
        } else if(quotation.matcher(expression.getPattern().toString().substring(patternIndex)).find()) {
            matcher = quotation.matcher(expression.getPattern().toString().substring(patternIndex));
            matcher.find();
            return new Quotation(matcher.group(1));
        } else if(backslash.matcher(expression.getPattern().toString().substring(patternIndex)).find()) {
            matcher = backslash.matcher(expression.getPattern().toString().substring(patternIndex));
            matcher.find();
            return new Backslash(matcher.group(1));
        } else if(charClass.matcher(expression.getPattern().toString().substring(patternIndex)).find()) {
            matcher = charClass.matcher(expression.getPattern().toString().substring(patternIndex));
            matcher.find();
            return new CharClass(matcher.group(1));
        } else if(metachar.matcher(expression.getPattern().toString().substring(patternIndex)).find()) {
            matcher = metachar.matcher(expression.getPattern().toString().substring(patternIndex));
            matcher.find();
            return new Metacharacter(matcher.group(1));
        } else if(quantifier.matcher(expression.getPattern().toString().substring(patternIndex)).find()) {
            matcher = quantifier.matcher(expression.getPattern().toString().substring(patternIndex));
            matcher.find();
            return new Quantifier(matcher.group(1));
        } else if(mode.matcher(expression.getPattern().toString().substring(patternIndex)).find()) {
            matcher = mode.matcher(expression.getPattern().toString().substring(patternIndex));
            matcher.find();
            return new Mode(matcher.group(1));
        } else {
            return new SimpleChar(expression.getPattern().toString().substring(patternIndex,patternIndex+1));
        }
    }

    public static ConstructsFactory getInstance() {
        return instance;
    }
}
