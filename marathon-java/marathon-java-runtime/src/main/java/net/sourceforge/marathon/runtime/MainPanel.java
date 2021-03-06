/*******************************************************************************
 * Copyright 2016 Jalian Systems Pvt. Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package net.sourceforge.marathon.runtime;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Properties;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import net.sourceforge.marathon.runtime.api.Constants;
import net.sourceforge.marathon.runtime.api.FileSelectionListener;
import net.sourceforge.marathon.runtime.api.IFileSelectedAction;
import net.sourceforge.marathon.runtime.api.IPropertiesPanel;
import net.sourceforge.marathon.runtime.api.ISubPropertiesPanel;
import net.sourceforge.marathon.runtime.api.UIUtils;
import net.sourceforge.marathon.runtime.api.ValidationUtil;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public class MainPanel implements IPropertiesPanel, IFileSelectedAction, ISubPropertiesPanel {
    public static final Icon icon = new ImageIcon(
            MainPanel.class.getClassLoader().getResource("net/sourceforge/marathon/mpf/images/main_obj.gif"));
    private JTextField mainClassField;
    private JTextArea programArgsField;
    private JTextArea vmArgsField;
    private JTextField workingDirField;
    private JButton browseVM;
    private JTextField vmCommandField;
    private Component parent;
    private JButton browse;
    private JPanel panel;

    public MainPanel(Component parent) {
        this.parent = parent;
    }

    public JPanel createPanel() {
        initComponents();
        PanelBuilder builder = new PanelBuilder(new FormLayout("left:pref, 3dlu, fill:pref:grow, 3dlu, fill:pref",
                "pref, 3dlu, pref, 3dlu, fill:pref:grow, 3dlu, fill:pref:grow, 3dlu, pref, 3dlu, pref, 3dlu, pref"));
        builder.border(Borders.DIALOG);
        CellConstraints labelConstraints = new CellConstraints();
        CellConstraints compConstraints = new CellConstraints();
        builder.addLabel("Class &Name: ", labelConstraints.xy(1, 1), mainClassField, compConstraints.xywh(3, 1, 3, 1));
        JScrollPane scrollPane = new JScrollPane(programArgsField, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        JLabel label = builder.addLabel("Pro&gram Arguments: ",
                labelConstraints.xy(1, 5, CellConstraints.LEFT, CellConstraints.TOP), scrollPane, compConstraints.xywh(3, 5, 3, 1));
        label.setLabelFor(programArgsField);
        scrollPane = new JScrollPane(vmArgsField, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        label = builder.addLabel("&VM Arguments: ", labelConstraints.xy(1, 7, CellConstraints.LEFT, CellConstraints.TOP),
                scrollPane, compConstraints.xywh(3, 7, 3, 1));
        label.setLabelFor(vmArgsField);
        builder.addLabel("&Working Directory: ", labelConstraints.xy(1, 9), workingDirField, compConstraints.xy(3, 9));
        builder.add(browse, labelConstraints.xy(5, 9));
        browse.setMnemonic(KeyEvent.VK_B);
        builder.addLabel("&Java Home: ", labelConstraints.xy(1, 11), vmCommandField, compConstraints.xy(3, 11));
        builder.add(browseVM, labelConstraints.xy(5, 11));
        return builder.getPanel();
    }

    private void initComponents() {
        mainClassField = new JTextField(20);
        programArgsField = new JTextArea(4, 30);
        programArgsField.setLineWrap(true);
        programArgsField.setWrapStyleWord(true);
        vmArgsField = new JTextArea(4, 30);
        vmArgsField.setLineWrap(true);
        vmArgsField.setWrapStyleWord(true);
        vmCommandField = new JTextField(20);
        browseVM = UIUtils.createBrowseButton();
        browseVM.setMnemonic('o');
        FileSelectionListener fileSelectionListenerVM = new FileSelectionListener(this, null, parent, vmCommandField,
                "Select Java Home Folder");
        fileSelectionListenerVM.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        browseVM.addActionListener(fileSelectionListenerVM);
        workingDirField = new JTextField(20);
        browse = UIUtils.createBrowseButton();
        browse.setMnemonic('r');
        FileSelectionListener fileSelectionListener = new FileSelectionListener(this, null, parent, workingDirField,
                "Select Working Directory");
        fileSelectionListener.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        browse.addActionListener(fileSelectionListener);
    }

    public String getName() {
        return "Main";
    }

    public Icon getIcon() {
        return icon;
    }

    public void getProperties(Properties props) {
        props.setProperty(Constants.PROP_APPLICATION_MAINCLASS, mainClassField.getText());
        props.setProperty(Constants.PROP_APPLICATION_ARGUMENTS, programArgsField.getText().trim());
        props.setProperty(Constants.PROP_APPLICATION_VM_ARGUMENTS, vmArgsField.getText().trim());
        props.setProperty(Constants.PROP_APPLICATION_JAVA_HOME, vmCommandField.getText());
        props.setProperty(Constants.PROP_APPLICATION_WORKING_DIR, workingDirField.getText());
    }

    public void setProperties(Properties props) {
        mainClassField.setText(props.getProperty(Constants.PROP_APPLICATION_MAINCLASS, ""));
        mainClassField.setCaretPosition(0);
        programArgsField.setText(props.getProperty(Constants.PROP_APPLICATION_ARGUMENTS, ""));
        programArgsField.setCaretPosition(0);
        vmArgsField.setText(props.getProperty(Constants.PROP_APPLICATION_VM_ARGUMENTS, ""));
        vmArgsField.setCaretPosition(0);
        vmCommandField.setText(props.getProperty(Constants.PROP_APPLICATION_JAVA_HOME, ""));
        vmCommandField.setCaretPosition(0);
        workingDirField.setText(props.getProperty(Constants.PROP_APPLICATION_WORKING_DIR, ""));
        workingDirField.setCaretPosition(0);
    }

    public boolean isValidInput() {
        if (mainClassField.getText() == null || mainClassField.getText().equals("")) {
            JOptionPane.showMessageDialog(parent, "Main class can't be empty", "Main Class", JOptionPane.ERROR_MESSAGE);
            mainClassField.requestFocus();
            return false;
        }
        if (!ValidationUtil.isValidClassName(mainClassField.getText())) {
            JOptionPane.showMessageDialog(parent, "Invalid class name given for main class", "Main Class",
                    JOptionPane.ERROR_MESSAGE);
            mainClassField.requestFocus();
            return false;
        }
        if (mainClassField.getText().indexOf('.') == -1) {
            int r = JOptionPane.showConfirmDialog(parent,
                    "There is no package given for the main class. You need to give fully qualified class name. Do you want to continue?",
                    "Main Class", JOptionPane.YES_NO_OPTION);
            if (r != JOptionPane.OK_OPTION) {
                mainClassField.requestFocus();
                return false;
            }
        }
        if (programArgsField.getText().indexOf('\n') != -1 || programArgsField.getText().indexOf('\r') != -1) {
            JOptionPane.showMessageDialog(parent, "Can not have new lines in Program Arguments", "Program Arguments",
                    JOptionPane.ERROR_MESSAGE);
            programArgsField.requestFocus();
            return false;
        }
        if (vmArgsField.getText().indexOf('\n') != -1 || vmArgsField.getText().indexOf('\r') != -1) {
            JOptionPane.showMessageDialog(parent, "Can not have new lines in VM Arguments", "VM Arguments",
                    JOptionPane.ERROR_MESSAGE);
            vmArgsField.requestFocus();
            return false;
        }
        return true;
    }

    public void filesSelected(File[] files, Object cookie) {
        ((JTextField) cookie).setText(files[0].getAbsolutePath());
    }

    public JPanel getPanel() {
        if (panel == null)
            panel = createPanel();
        return panel;
    }

    public int getMnemonic() {
        return KeyEvent.VK_M;
    }

}
