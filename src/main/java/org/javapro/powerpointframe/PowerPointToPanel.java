/*
 * Copyright (C) 2023 javatlacati
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.javapro.powerpointframe;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.sl.usermodel.Slide;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.xslf.usermodel.XMLSlideShow;

/**
 *
 * @author javatlacati
 */
public class PowerPointToPanel extends javax.swing.JFrame {
    
    private static final Logger logger = Logger.getLogger(PowerPointToPanel.class.getName());

    private List<SlideComponent> sliderComponents = new ArrayList<>();
    private int currentSlide = -1;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton jButton1;
    private JPanel jPanel1;
    private JScrollPane jspanel;
    // End of variables declaration//GEN-END:variables

    /**
     * Creates new form PowerPointToPanel
     */
    public PowerPointToPanel() {
        initComponents();
        jButton1.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent evt) {
                panelKeyTyped(evt);
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new JButton();
        jspanel = new JScrollPane();
        jPanel1 = new JPanel();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Presentación de PowerPoint");
        setMinimumSize(new Dimension(2500, 2500));
        setPreferredSize(new Dimension(2500, 2500));

        jButton1.setText("load slideshow");
        jButton1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1, BorderLayout.SOUTH);

        jspanel.setAutoscrolls(true);
        jspanel.setMinimumSize(new Dimension(2500, 2500));
        jspanel.setPreferredSize(new Dimension(2500, 2500));
        jspanel.setViewportView(jPanel1);

        getContentPane().add(jspanel, BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        loadPresentation();
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            logger.log(java.util.logging.Level.SEVERE, "class does not exists", ex);
        } catch (InstantiationException ex) {
            logger.log(java.util.logging.Level.SEVERE, "can't instantiate class", ex);
        } catch (IllegalAccessException ex) {
            logger.log(java.util.logging.Level.SEVERE, "illegal access", ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, "Nimbus look and feel is not supported", ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new PowerPointToPanel().setVisible(true);
        });
    }



    private void loadPresentation() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Power point presentation", "ppt", "pptx"));
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File presentationppt = fileChooser.getSelectedFile();
            sliderComponents = new ArrayList<>();
            jspanel.setViewportView(jPanel1);
            try (FileInputStream inputStream = new FileInputStream(presentationppt)) {

                SlideShow<?,?> slideShow = presentationppt.getName().endsWith(".ppt") ? new HSLFSlideShow(inputStream) : new XMLSlideShow(inputStream);
//                inputStream.close();
                // Obtiene las diapositivas de la presentación
                List<? extends Slide<?, ?>> slides = slideShow.getSlides();
                for (Slide<?, ?> slide : slides) {
                    SlideComponent slideComponent = new SlideComponent(slide);
                    addListenersToSlideComponent(slideComponent);
                    sliderComponents.add(slideComponent);
                }
                logger.info("loaded powerpoint presentation");
                currentSlide = 0;
                SwingUtilities.invokeLater(() -> {
                    displaySlide();
                    this.invalidate();
                    this.repaint();
                });

            } catch (FileNotFoundException ex) {
                logger.log(Level.SEVERE, "can't find the specified file", ex);
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "I/O problem", ex);
            }
        }
    }

    private void addListenersToSlideComponent(SlideComponent slideComponent) {
        slideComponent.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                panelMouseClicked(evt);
            }
        });
    }

    private void displaySlide() {
        logger.log(Level.FINE, "replacing component:{0}", currentSlide);
        SlideComponent firstSlide = sliderComponents.get(currentSlide);

        jspanel.setViewportView(firstSlide);
    }

    private void panelMouseClicked(MouseEvent evt) {
        int mouseButtonSelected = evt.getButton();
        if (evt.getButton() == 1) {
            moveRightLogic();
        } else if (mouseButtonSelected == 3) {
            moveLeftLogic();
        }
    }

    private void panelKeyTyped(KeyEvent evt) {

        char keyChar = evt.getKeyChar();
        logger.log(Level.FINE, "key typed:{0}", keyChar);
        if ('d' == keyChar) {
            moveRightLogic();
        } else if ('a' == keyChar) {
            moveLeftLogic();
        }
    }

    private void moveRightLogic() {
        if (currentSlide == -1) {
            logger.fine("no presentation loaded");
        } else {
            if ((currentSlide + 1) < sliderComponents.size()) {
                currentSlide++;
            } else {
                currentSlide = 0;
            }
            displaySlide();
        }
    }

    private void moveLeftLogic() {
        if (currentSlide == -1) {
            logger.fine("no presentation loaded");
        } else {
            if ((currentSlide - 1) > -1) {
                currentSlide--;
            } else {
                currentSlide = (sliderComponents.size() - 1);
            }
            displaySlide();
        }
    }
}
