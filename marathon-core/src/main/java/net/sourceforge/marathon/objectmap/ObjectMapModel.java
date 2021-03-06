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
package net.sourceforge.marathon.objectmap;

import java.beans.IntrospectionException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.swing.tree.TreeNode;

import net.sourceforge.marathon.runtime.api.Constants;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.DumperOptions.FlowStyle;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;

public class ObjectMapModel implements TreeNode {

    protected List<OMapContainer> data;
    private transient List<OMapContainer> deletedContainers = new ArrayList<OMapContainer>();

    private boolean dirty = false;

    private final static Logger logger = Logger.getLogger(ObjectMapModel.class.getName());

    public ObjectMapModel() {
        load();
    }

    @SuppressWarnings("unchecked") private void load() {
        try {
            data = (List<OMapContainer>) loadYaml(getOMapFile());
            for (OMapContainer container : data) {
                container.setParent(this);
            }
        } catch (FileNotFoundException e) {
            data = new ArrayList<OMapContainer>();
            setDirty(true);
            logger.info("Creating a new ObjectMap");
        }
    }

    private Object loadYaml(File file) throws FileNotFoundException {
        FileReader reader = new FileReader(file);
        try {
            Constructor constructor = new Constructor();
            PropertyUtils putils = new PropertyUtils();
            putils.setSkipMissingProperties(true);
            constructor.setPropertyUtils(putils);
            Yaml yaml = new Yaml(constructor);
            return yaml.load(reader);
        } catch (Throwable t) {
            throw new RuntimeException("Error loading yaml from: " + file.getAbsolutePath() + "\n" + t.getMessage(), t);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void add(OMapContainer container) {
        data.add(container);
    }

    public TreeNode getChildAt(int childIndex) {
        if (childIndex < data.size())
            return data.get(childIndex);
        return null;
    }

    public int getChildCount() {
        return data.size();
    }

    public TreeNode getParent() {
        return null;
    }

    public int getIndex(TreeNode node) {
        if (node == null || node.getParent() != this)
            return -1;
        return data.indexOf(node);
    }

    public boolean getAllowsChildren() {
        return true;
    }

    public boolean isLeaf() {
        return false;
    }

    public Enumeration<TreeNode> children() {
        return new Enumeration<TreeNode>() {
            int index = 0;

            public boolean hasMoreElements() {
                return index < data.size();
            }

            public TreeNode nextElement() {
                return data.get(index++);
            }
        };
    }

    public void save() {
        logger.info("Saving object map");
        if (!isDirty()) {
            logger.info("Object map is not modified. Skipping save.");
            return;
        }
        try {
            DumperOptions options = new DumperOptions();
            options.setIndent(4);
            options.setDefaultFlowStyle(FlowStyle.AUTO);

            Representer representer = new Representer() {
                @Override protected Set<Property> getProperties(Class<? extends Object> type) throws IntrospectionException {
                    Set<Property> properties = super.getProperties(type);
                    Property parentProperty = null;
                    for (Property property : properties) {
                        if (property.getName().equals("parent"))
                            parentProperty = property;
                    }
                    if (parentProperty != null)
                        properties.remove(parentProperty);
                    return properties;
                }
            };
            new Yaml(representer, options).dump(data, new FileWriter(getOMapFile()));
            for (OMapContainer container : data) {
                container.save();
            }
        } catch (IOException e) {
            logger.warning("Unable to save object map");
            e.printStackTrace();
        } catch (YAMLException e1) {
            logger.warning("Unable to save object map: " + this);
            e1.printStackTrace();
            throw e1;
        }
        for (OMapContainer oc : deletedContainers) {
            oc.deleteFile();
        }
        setDirty(false);
    }

    public File getOMapFile() {
        return new File(System.getProperty(Constants.PROP_PROJECT_DIR),
                System.getProperty(Constants.PROP_OMAP_FILE, Constants.FILE_OMAP));
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean b) {
        dirty = b;
    }

    @Override public String toString() {
        return "{objectMap: " + data + "}";
    }

    public void loadAll() {
        for (OMapContainer container : data) {
            try {
                container.load();
            } catch (FileNotFoundException e) {
                logger.warning("Unable to find file for container: " + container + " Ignoring it.");
                data.remove(container);
            }
        }
    }

    public void removeComponent(OMapComponent oc) {
        TreeNode parent = oc.getParent();
        if (parent instanceof OMapContainer) {
            ((OMapContainer) parent).removeComponent(oc);
        }
    }

    public void remove(OMapContainer oc) {
        if (data.contains(oc)) {
            deletedContainers.add(oc);
            data.remove(oc);
        } else
            logger.warning("Container " + oc + " does not exist in the objectmap");
    }
}
