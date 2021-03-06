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
package net.sourceforge.marathon.display;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import net.sourceforge.marathon.Version;

public class SplashScreen extends JDialog {
    public static final Icon SPLASH = new ImageIcon(
            SplashScreen.class.getClassLoader().getResource("net/sourceforge/marathon/display/images/marathon.png"));

    private static final long serialVersionUID = 1L;
    private static final int SPLASH_DISPLAY_TIME = 2000;

    public SplashScreen() {
        setUndecorated(true);
        setModal(true);
        try {
            getContentPane().add(new JLabel(SPLASH), BorderLayout.NORTH);
            JPanel versionPanel = new JPanel();
            versionPanel.setAlignmentX(0.5f);
            JLabel version = new JLabel(Version.blurbTitle() + " Version: " + Version.id());
            version.setFont(version.getFont().deriveFont(11.0f));
            versionPanel.add(version);
            getContentPane().add(versionPanel, BorderLayout.SOUTH);
            pack();
        } catch (Exception e) {
            e.printStackTrace();
        }
        centerScreen();
        Timer timer = new Timer(SPLASH_DISPLAY_TIME, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        timer.setRepeats(false);
        timer.start();
        setVisible(true);
    }

    private void centerScreen() {
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((size.width - getWidth()) / 2, (size.height - getHeight()) / 2);
    }
}
