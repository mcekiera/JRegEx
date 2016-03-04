package Syntax;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public class SyntaxLibrary {
    private final static SyntaxLibrary instance = new SyntaxLibrary();
    private final static Map<String,Element> library = createLibrary();

    private SyntaxLibrary(){}

    private static Element getElement(String representation) {
        return library.get(representation);
    }

    private static Map<String,Element> createLibrary() {
        Map<String, Element> content = new HashMap<String, Element>();
        File file = new File("Syntax/regex.txt");
        try {
            for(String line : Files.readAllLines(file.toPath())) {
                String[] parts = line.split("\\t");
                Element element = new Element(parts[0],parts[0],parts[2]);
                System.out.println(parts[0] + parts[0] + parts[2]);
                content.put(element.getMark(),element);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return content;
    }




}
