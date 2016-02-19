package net.sourceforge.marathon.javaagent.components;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import javax.swing.JComboBox;
import javax.swing.JTextField;

import net.sourceforge.marathon.javaagent.AbstractJavaElement;
import net.sourceforge.marathon.javaagent.EventQueueWait;
import net.sourceforge.marathon.javaagent.IJavaElement;
import net.sourceforge.marathon.javaagent.JavaAgent;
import net.sourceforge.marathon.javaagent.JavaTargetLocator.JWindow;

import org.json.JSONArray;

public class JComboBoxJavaElement extends AbstractJavaElement {

    public JComboBoxJavaElement(Component component, JavaAgent driver, JWindow window) {
        super(component, driver, window);
    }

    private interface Predicate {
        public boolean isValid(JComboBoxOptionJavaElement e);
    }

    public String getContent() {
        return new JSONArray(getContent((JComboBox) component)).toString();
    }

    public static String[][] getContent(JComboBox component) {
        int nOptions = ((JComboBox) component).getModel().getSize();
        String[][] content = new String[1][nOptions];
        for (int i = 0; i < nOptions; i++) {
            content[0][i] = JComboBoxOptionJavaElement.getText(component, i, true);
        }
        return content;
    }

    @Override public List<IJavaElement> getByPseudoElement(String selector, Object[] params) {
        if (selector.equals("nth-option")) {
            return Arrays.asList((IJavaElement) new JComboBoxOptionJavaElement(this, ((Integer) params[0]).intValue() - 1));
        } else if (selector.equals("all-options") || selector.equals("all-cells")) {
            return collectItems(new ArrayList<IJavaElement>(), new Predicate() {
                @Override public boolean isValid(JComboBoxOptionJavaElement e) {
                    return true;
                }
            });
        }
        return super.getByPseudoElement(selector, params);
    }

    private List<IJavaElement> collectItems(List<IJavaElement> r, Predicate p) {
        int nitems = getCount();
        for (int i = 0; i < nitems; i++) {
            JComboBoxOptionJavaElement l = new JComboBoxOptionJavaElement(this, i);
            if (p.isValid(l))
                r.add(l);
        }
        return r;
    }

    private int getCount() {
        try {
            return EventQueueWait.exec(new Callable<Integer>() {
                @Override public Integer call() {
                    return ((JComboBox) getComponent()).getModel().getSize();
                }
            });
        } catch (Exception e) {
            throw new InternalError("Call to getSize() failed for JList#model");
        }
    }

    @Override public String _getText() {
        return getSelectedItemText((JComboBox) component);
    }

    public static String getSelectedItemText(JComboBox combo) {
        int selectedIndex = combo.getSelectedIndex();
        if (selectedIndex == -1)
            return "";
        return JComboBoxOptionJavaElement.getText(combo, selectedIndex, true);
    }

    @Override public boolean marathon_select(final String value) {
        final String text = JComboBoxOptionJavaElement.stripHTMLTags(value);
        int selectedItem = findMatch(value, new Predicate() {
            @Override public boolean isValid(JComboBoxOptionJavaElement e) {
                if (!text.equals(e.getAttribute("text")))
                    return false;
                return true;
            }
        });
        if (selectedItem == -1) {
            if (((JComboBox) getComponent()).isEditable()) {
                ((JTextField) ((JComboBox) getComponent()).getEditor().getEditorComponent()).setText(value);
                return true;
            }
            return false;
        }
        ((JComboBox) getComponent()).setSelectedIndex(selectedItem);
        return true;
    }

    private int findMatch(String text, Predicate p) {
        int count = getCount();
        for (int item = 0; item < count; item++)
            if (p.isValid(new JComboBoxOptionJavaElement(this, item)))
                return item;
        return -1;
    }
}