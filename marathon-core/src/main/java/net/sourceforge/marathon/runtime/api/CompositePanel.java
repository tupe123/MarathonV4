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
package net.sourceforge.marathon.runtime.api;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Properties;

import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.Borders;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

public abstract class CompositePanel implements IPropertiesPanel {

    public static final String NODIALOGBORDER = "no-dialog-border";

    protected final JDialog parent;

    private JComboBox<PlugInModelInfo> launcherField;
    private JTabbedPane launchInfo;

    private ModelInfo launcherModels;

    private ISubPropertiesPanel[] launcherPanels;

    private JPanel panel;

    private boolean needDialogBorder = true;

    public CompositePanel(JDialog parent) {
        this.parent = parent;
        launcherModels = new ModelInfo(getResourceName(), parent);
        initComponents();
    }

    abstract protected String getResourceName();

    public CompositePanel(JDialog parent, String nodialogborder) {
        this(parent);
        this.needDialogBorder = false;
    }

    public JPanel getPanel() {
        if (panel == null) {
            PanelBuilder builder = new PanelBuilder(
                    new FormLayout("left:pref, 3dlu, pref:grow, 3dlu, fill:pref", "3dlu, pref, 3dlu, fill:pref:grow"));
            if (needDialogBorder)
                builder.border(Borders.DIALOG);
            CellConstraints labelConstraints = new CellConstraints();
            CellConstraints compConstraints = new CellConstraints();
            builder.addLabel(getOptionFieldName(), labelConstraints.xy(1, 2), launcherField, compConstraints.xywh(3, 2, 3, 1));
            builder.add(launchInfo, compConstraints.xyw(1, 4, 5));
            panel = builder.getPanel();
        }
        return panel;
    }

    abstract protected String getOptionFieldName();

    @SuppressWarnings({ "unchecked", "rawtypes" }) private void initComponents() {
        launcherField = new JComboBox<PlugInModelInfo>(launcherModels) {
            private static final long serialVersionUID = 1L;

            @Override public void setSelectedIndex(int anIndex) {
                try {
                    if (anIndex != -1)
                        getLauncherModel(((PlugInModelInfo) launcherField.getItemAt(anIndex)).className);
                    super.setSelectedIndex(anIndex);
                } catch (Exception e) {
                }
            }
        };
        final ListCellRenderer oldRenderer = launcherField.getRenderer();
        launcherField.setRenderer(new BasicComboBoxRenderer() {
            private static final long serialVersionUID = 1L;

            @Override public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                Component c = oldRenderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                try {
                    getLauncherModel(((PlugInModelInfo) value).className);
                    c.setEnabled(true);
                } catch (Throwable t) {
                    c.setEnabled(false);
                }
                return c;
            }
        });
        launcherField.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    updateLauncher(getClassName());
                }
            }
        });
        launchInfo = new JTabbedPane();
    }

    public void updateLauncher(String launcher) {
        launchInfo.removeAll();
        launcherPanels = getLauncherPanels();
        for (int i = 0; i < launcherPanels.length; i++) {
            ISubPropertiesPanel p = launcherPanels[i];
            launchInfo.addTab(p.getName(), p.getIcon(), p.getPanel());
            launchInfo.setMnemonicAt(i, p.getMnemonic());
        }
        parent.invalidate();
    }

    public ISubPropertiesPanel[] getLauncherPanels() {
        String selectedLauncher = getClassName();
        if (selectedLauncher == null)
            return new ISubPropertiesPanel[] {};
        try {
            ISubpanelProvider model = getLauncherModel(selectedLauncher);
            if (model != null)
                return model.getSubPanels(parent);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(parent, "Could not find launcher: " + selectedLauncher, "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (InstantiationException e) {
            JOptionPane.showMessageDialog(parent, "Could not find launcher", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            JOptionPane.showMessageDialog(parent, "Could not find launcher", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
        return new ISubPropertiesPanel[] {};
    }

    protected ISubpanelProvider getLauncherModel(String launcher)
            throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (launcher == null || launcher.equals(""))
            return null;
        Class<?> klass = Class.forName(launcher);
        return (ISubpanelProvider) klass.newInstance();
    }

    abstract public String getName();

    abstract public Icon getIcon();

    public void getProperties(Properties props) {
        props.setProperty(getClassProperty(), getClassName());
        for (IPropertiesPanel p : launcherPanels) {
            p.getProperties(props);
        }
    }

    abstract protected String getClassProperty();

    public String getClassName() {
        if (launcherField.getSelectedItem() == null)
            return "";
        return ((PlugInModelInfo) launcherField.getSelectedItem()).className;
    }

    public void setProperties(Properties props) {
        setPlugInSelection(launcherField, launcherModels, props, getClassProperty());
        updateLauncher(getClassName());
        for (IPropertiesPanel p : launcherPanels) {
            p.setProperties(props);
        }
    }

    private void setPlugInSelection(JComboBox<PlugInModelInfo> comboBox, ModelInfo models, Properties props, String key) {
        String model = (String) props.get(key);
        if (model == null) {
            comboBox.setSelectedIndex(-1);
        } else {
            comboBox.setSelectedItem(models.getPluginModel(model));
            if (!isSelectable())
                comboBox.setEnabled(false);
        }
    }

    protected boolean isSelectable() {
        return true;
    }

    public boolean isValidInput() {
        if (launcherField.getSelectedItem() == null) {
            errorMessage();
            launcherField.requestFocus();
            return false;
        }
        for (IPropertiesPanel p : launcherPanels) {
            if (!p.isValidInput())
                return false;
        }
        return true;
    }

    protected abstract void errorMessage();
}
