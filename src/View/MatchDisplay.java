package View;

import Model.Match.Overall;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MatchDisplay {
    private final JTextPane area;

    public MatchDisplay() {
        area = new JTextPane();
        area.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);
        area.setFont(new Font("Arial", Font.PLAIN, 14));
        area.setContentType("text/html");
        area.setEditable(false);
        area.setBorder(new EmptyBorder(5,5,5,5));
    }

    public JTextPane getArea() {
        return area;
    }

    public void set(Overall overall) {
        StringBuilder resutl = new StringBuilder();
        resutl.append("<html>");
        for(int k = 0; k < overall.getMatch(0).size();k++) {
            resutl.append(("<b>Match #")).append(k + 1).append(":</b><br>");
            for(int j = 0; j <= overall.groupCount(); j++) {
                if(overall.getMatch(j).get(k).getDescription().equals("")) {
                    System.out.println(">" + overall.getMatch(j).get(k).getDescription());
                }
                resutl.append("#").append(j).append("   ").append(overall.getMatch(j).get(k).getDescription()).append("<br>");
            }
            resutl.append("<br>");
        }
        resutl.append("</html>");
        area.setText(resutl.toString());
    }
}
