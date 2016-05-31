# JRegEx

Java regular expression tester with diversified visualizations of matching properties of given pattern. It is based on https://regex101.com/, however it lack its many features.

<i><b>JRegEx</b></i> tests given regular pattern with Java regex flavour against provided testing text. In results it shows:
<ul>
  <li>general matching of pattern, </li>
  <li>structure of regular expression with description of particular constructs,</li>
  <li>detail of general matching,</li>
  <li>detailed matching for particular separted constructs.</li>
</ul>

# How it works?

The main window consins five mail elements:
<ol>
  <li>User input field</li>
  <li>Test text matching area</li>
  <li>Descritpion area</li>
  <li>Results description area</li>
  <li>Detailed match area</li>
</ol>

![alt tag](https://github.com/mcekiera/JRegEx/blob/master/src/DESC.png)

<h5>User Input Field</h5>
It is field for user regular expression input. It could be multiline, <u>however it useful feature only with use of particular modifiers (like <i>(?x)</i>)</u>. Field interpret input as regular expression, recognizes and highlight identified regular expression concepts, divided into several categories: 
<ul>
  <li>character class (orange, with dark orange highlight for internal special constructs)</li>
  <li>modifiers (purple)</li>
  <li>quotation and comments (light gray)</li>
  <li>standard metacharacters (like quantifiers, predifined classes, etc., blue)</li>
  <li>grouping concepts (capturing and non-capturing groups, atomic, groups, look behind, look ahead - color different for every separate construct)</li>
  <li>errors (red)</li>
</ul>
Every action, adding or removing character, within this field will cause <b>attempt to match against provided field 2.</b> test text. Positioning the mouse cursor over particular construct, would <b>display a tooltip with short description</b>. Clicking on separate construct will couse highlighting it with light blue color, what make it easier to distinguish separeted and grouped constructs.

<h5>Test text matching area</h5>
Area for user input of text for matching by regular expression. After typing into <i>User Input Field</i> any regular expression, matched fragments would be highlighted in color <b>corresponding to group colors</b> of <i>User Input Field</i>. Positioning the mouse cursor over matched fragment, a, would <b>display a tooltip with short description</b>. Clicking on separate fragment will couse highlighting it with light blue color, what make it easier to distinguish matched fragment.
Whats more, clicking on matched fagment would <b>display detailed match analysis for given example</b> in Detailed match area.

<h5>Descritpion area</h5>
Description field display a structure of regular pattern in form of tree, with whole expression as root, with grouping constructs as branches, and separate constructs as leafs. Every node has short explanation of given construct. The color of nodes is corresponding to Input field, with exception of grouping constructs, which in this field are highlighted in light green color. Clicking on chosen node would cause highlighting particular construct in Input Field.

<h5>Results description area</h5>
It displays short description of matched fragments, with information about capturing group and start and end indieces.

<h5>Detailed match area</h5>
It displays detailed matching analysis, which shows haw particular constructs within given regular expression matched on chosen example. <b>Particular construct is highlighted with same color as text matched by this construct</b>. Construct which does not match directly any fragments (like anchoring constructs, quantifiers, brackets or construct with does not match because it is an unused alternative, or are within quantifier which allow zero occurences) are highlighted as grey.

<i>Most complicated among my projects, it is actually extended version of RegExtractor, however it does not have some of its features, but it correctly match in detailed maching analysis, which was the bigges problem for me in RegExtractor. Also I think about adding additinal options in future.</i>

