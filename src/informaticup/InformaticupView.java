package informaticup;

import algorithmen.*;
import informaticup.Parser.ParserException;
import informaticup.datenstruktur.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * GUI: Das Hauptfenster des Programms
 */
public class InformaticupView extends FrameView {

    // Dateiname (Pfad) der Eingabedatei
    private String _dateipfad;
    // Welcher Algorithmus wurde aufgerufen?
    private int _algorithmus;
    // Verbesserungsalgorithmen akiviert? (Simulierte Abkühlung, Tabu, Einzelverschiebung)
    private boolean _verbesserung0;
    private boolean _verbesserung1;
    private boolean _verbesserung2;
    // Approximationsmethode (automatisch, manuell)
    private int _approxmethode;
    // Bei manueller Approximationsmethode: Wert
    private int _approxrate;
    // Zustand (mit oder ohne Automaten)
    private Zustand _zustand;
    // Zeiger auf die Welt
    private Welt _welt;
    // Soll die Gewichtskarte gezeichnet werden?
    private boolean _paintPixels = false;
    // Temperatur für Simulierte Abkühlung
    private int _temperatur = 1000;
    // Initialer Wert für die Anzahl der Iterationen bei der Tabu-Suche
    private int _tabuIterationen = 2500;
    // DEBUG: Apache commons logger
    static final Logger log = Logger.getLogger(InformaticupView.class.getName());
    Status status = new Status();
    org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(informaticup.InformaticupApp.class).getContext().getResourceMap(InformaticupView.class);

    /**
     * GUI: Variablen initialisieren.
     */
    public InformaticupView(SingleFrameApplication app) {
        super(app);
        initComponents();

        menuAddNewATM.setState(false);
        menuLockUnlockATM.setState(false);
        menuRemoveATM.setState(false);

        // Setze Logging Level
        log.setLevel(Level.ALL);
        log.info("Initialisierung der Komponenten beendet");

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });

        String[] args = InformaticupApp.p_args;
        if (args.length > 0) {
            if (args[0].equals("-a")) {
                editCalcMenuItem.setEnabled(true);
                _zustand = null;
                ((drawingPanel) drawingArea).repaint();
                newCalcFrame = new createCalcFrame();
                newCalcFrame.setMain(this);
                InformaticupApp.getApplication().show(newCalcFrame);
                _zustand = null;

                newCalcFrame.setArgs(args);
                try {
                    startCalculation();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * Zeigt die About-Box mit den Namen der Autoren an.
     */
    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = InformaticupApp.getApplication().getMainFrame();
            aboutBox = new InformaticupAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        InformaticupApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jSlider1 = new javax.swing.JSlider();
        jScrollPane1 = new javax.swing.JScrollPane();
        drawingArea = new drawingPanel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        SaveMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        newCalcMenuItem = new javax.swing.JMenuItem();
        editCalcMenuItem = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        menuAddNewATM = new javax.swing.JCheckBoxMenuItem();
        menuRemoveATM = new javax.swing.JCheckBoxMenuItem();
        menuLockUnlockATM = new javax.swing.JCheckBoxMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();
        jCheckName = new javax.swing.JCheckBox();

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                mainPanelComponentResized(evt);
            }
        });

        jSlider1.setMaximum(500);
        jSlider1.setMinimum(50);
        jSlider1.setName("jSlider1"); // NOI18N
        jSlider1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                jSlider1StateChanged(evt);
            }
        });
        jSlider1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jSlider1PropertyChange(evt);
            }
        });

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        drawingArea.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        drawingArea.setPreferredSize(new java.awt.Dimension(0, 0));

        javax.swing.GroupLayout drawingAreaLayout = new javax.swing.GroupLayout(drawingArea);
        drawingArea.setLayout(drawingAreaLayout);
        drawingAreaLayout.setHorizontalGroup(
            drawingAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 446, Short.MAX_VALUE)
        );
        drawingAreaLayout.setVerticalGroup(
            drawingAreaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 251, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(drawingArea);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSlider1, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSlider1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        menuBar.setName("menuBar"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(informaticup.InformaticupApp.class).getContext().getResourceMap(InformaticupView.class);
        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(informaticup.InformaticupApp.class).getContext().getActionMap(InformaticupView.class, this);
        SaveMenuItem.setAction(actionMap.get("Save")); // NOI18N
        SaveMenuItem.setText(resourceMap.getString("SaveMenuItem.text")); // NOI18N
        SaveMenuItem.setName("SaveMenuItem"); // NOI18N
        SaveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(SaveMenuItem);

        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setText(resourceMap.getString("exitMenuItem.text")); // NOI18N
        exitMenuItem.setToolTipText(resourceMap.getString("exitMenuItem.toolTipText")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText(resourceMap.getString("editMenu.text")); // NOI18N
        editMenu.setName("editMenu"); // NOI18N

        newCalcMenuItem.setAction(actionMap.get("showNewBerechnungDialog")); // NOI18N
        newCalcMenuItem.setText(resourceMap.getString("newCalcMenuItem.text")); // NOI18N
        newCalcMenuItem.setName("newCalcMenuItem"); // NOI18N
        editMenu.add(newCalcMenuItem);

        editCalcMenuItem.setAction(actionMap.get("showBerechnungsDialog")); // NOI18N
        editCalcMenuItem.setText(resourceMap.getString("editCalcMenuItem.text")); // NOI18N
        editCalcMenuItem.setName("editCalcMenuItem"); // NOI18N
        editCalcMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editCalcMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(editCalcMenuItem);

        menuBar.add(editMenu);

        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N

        menuAddNewATM.setSelected(true);
        menuAddNewATM.setText(resourceMap.getString("menuAddNewATM.text")); // NOI18N
        menuAddNewATM.setName("menuAddNewATM"); // NOI18N
        menuAddNewATM.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                menuAddNewATMStateChanged(evt);
            }
        });
        menuAddNewATM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuAddNewATMActionPerformed(evt);
            }
        });
        jMenu1.add(menuAddNewATM);

        menuRemoveATM.setSelected(true);
        menuRemoveATM.setText(resourceMap.getString("menuRemoveATM.text")); // NOI18N
        menuRemoveATM.setName("menuRemoveATM"); // NOI18N
        menuRemoveATM.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                menuRemoveATMStateChanged(evt);
            }
        });
        menuRemoveATM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuRemoveATMActionPerformed(evt);
            }
        });
        jMenu1.add(menuRemoveATM);

        menuLockUnlockATM.setSelected(true);
        menuLockUnlockATM.setText(resourceMap.getString("menuLockUnlockATM.text")); // NOI18N
        menuLockUnlockATM.setName("menuLockUnlockATM"); // NOI18N
        menuLockUnlockATM.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                menuLockUnlockATMStateChanged(evt);
            }
        });
        menuLockUnlockATM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuLockUnlockATMActionPerformed(evt);
            }
        });
        jMenu1.add(menuLockUnlockATM);

        menuBar.add(jMenu1);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setText(resourceMap.getString("aboutMenuItem.text")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        jCheckName.setSelected(true);
        jCheckName.setText(resourceMap.getString("jCheckName.text")); // NOI18N
        jCheckName.setName("jCheckName"); // NOI18N
        jCheckName.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckNameActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 177, Short.MAX_VALUE)
                .addComponent(jCheckName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(statusPanelLayout.createSequentialGroup()
                        .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(statusMessageLabel)
                            .addComponent(statusAnimationLabel)
                            .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(3, 3, 3))
                    .addComponent(jCheckName)))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void editCalcMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editCalcMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_editCalcMenuItemActionPerformed

    private void jSlider1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jSlider1PropertyChange
    }//GEN-LAST:event_jSlider1PropertyChange

    /**
     * Zeichnet die Karte neu, wenn der Skalierungswert gesetzt wurde.
     */
    private void jSlider1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_jSlider1StateChanged
        ((drawingPanel) drawingArea).setScale((double) jSlider1.getValue() / 50);
        ((drawingPanel) drawingArea).repaint();
    }//GEN-LAST:event_jSlider1StateChanged

    /**
     * Zeichnet die Karte neu, wenn die Größe des Fensters verändert wurde.
     */
    private void mainPanelComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_mainPanelComponentResized
        ((drawingPanel) drawingArea).setScale((double) jSlider1.getValue() / 50);
    }//GEN-LAST:event_mainPanelComponentResized

    /**
     * Speichert den aktuellen Zustand mit den Automaten ein einer Ausgabedatei.
     */
    private void SaveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveMenuItemActionPerformed
    }//GEN-LAST:event_SaveMenuItemActionPerformed

    private void menuAddNewATMStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_menuAddNewATMStateChanged
    }//GEN-LAST:event_menuAddNewATMStateChanged

    private void menuRemoveATMStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_menuRemoveATMStateChanged
    }//GEN-LAST:event_menuRemoveATMStateChanged

    private void menuLockUnlockATMStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_menuLockUnlockATMStateChanged
    }//GEN-LAST:event_menuLockUnlockATMStateChanged

    private void DrawingPanelAllesAusschalten() {
        ((drawingPanel) drawingArea).SetSetzenModus(false);
        ((drawingPanel) drawingArea).setGesperrtModus(false);
        ((drawingPanel) drawingArea).setLoeschenModus(false);
    }

    private void menuAddNewATMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuAddNewATMActionPerformed
        DrawingPanelAllesAusschalten();
        if (menuAddNewATM.getState() == true) {
            menuLockUnlockATM.setState(false);
            menuRemoveATM.setState(false);

            ((drawingPanel) drawingArea).SetSetzenModus(true);
        }
    }//GEN-LAST:event_menuAddNewATMActionPerformed

    private void menuLockUnlockATMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuLockUnlockATMActionPerformed
        DrawingPanelAllesAusschalten();
        if (menuLockUnlockATM.getState() == true) {
            menuAddNewATM.setState(false);
            menuRemoveATM.setState(false);

            ((drawingPanel) drawingArea).setGesperrtModus(true);
        }
    }//GEN-LAST:event_menuLockUnlockATMActionPerformed

    private void menuRemoveATMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuRemoveATMActionPerformed
        DrawingPanelAllesAusschalten();
        if (menuRemoveATM.getState() == true) {
            menuLockUnlockATM.setState(false);
            menuAddNewATM.setState(false);

            ((drawingPanel) drawingArea).setLoeschenModus(true);
        }
    }//GEN-LAST:event_menuRemoveATMActionPerformed

    private void jCheckNameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckNameActionPerformed
        // TODO add your handling code here:
        ((drawingPanel)drawingArea)._zeichneNamen = jCheckName.isSelected();
    }//GEN-LAST:event_jCheckNameActionPerformed

    /**
     * Startet die Berechnung und die Algorithmen.
     * @return Task (Thread)
     * @throws informaticup.Parser.ParserException Ausnahme, die ausgelöst wird, wenn die Eingabedatei nicht eingelesen werden konnte
     * @throws Exceptionn Ausnahme, die ausgelöst wird, wenn ein Berechnungsschritt fehlschlägt
     */
    @Action(block = Task.BlockingScope.APPLICATION)
    public Task startCalculation() throws ParserException, Exception {

        _welt = newCalcFrame.bereiteBerechnungVor();

        System.out.println("initalisiere Berechnung");

        _dateipfad = newCalcFrame.filePathTextField.getText();

        if (newCalcFrame.greedyRadioButton.isSelected()) {
            _algorithmus = 2;
        } else if (newCalcFrame.greedy2RadioButton.isSelected()) {
            _algorithmus = 3;
        } else if (newCalcFrame.randomRadioButton.isSelected()) {
            _algorithmus = 4;
        } else if (newCalcFrame.noneRadioButton.isSelected()) {
            //precalculation
            _algorithmus = 0;
        } else if (newCalcFrame.mapRadioButton.isSelected()) {
            //nur karte anzeigen
            _algorithmus = -1;
        } else {
            _algorithmus = -1;
        }

        _verbesserung0 = newCalcFrame.coolingCheckBox.isSelected();

        _verbesserung1 = newCalcFrame.moverCheckBox.isSelected();

        _verbesserung2 = newCalcFrame.tabuCheckBox.isSelected();

        if (newCalcFrame.autoRadioButton.isSelected()) {
            _approxmethode = 0;
        } else if (newCalcFrame.manRadioButton.isSelected()) {
            _approxmethode = 1;
            _approxrate = Integer.parseInt(newCalcFrame.approxRateTextField.getText());
        }

        if (_verbesserung0) {
            _temperatur = Integer.parseInt(newCalcFrame.temperaturTextField.getText());
        }

        if (_verbesserung2) {
            _tabuIterationen = Integer.parseInt(newCalcFrame.getIterationenTextField().getText());
        }


        newCalcFrame.dispose();

        //gewichtskarte zeichnen zurücksetzen
        _paintPixels = false;

        //infos rücksetzen
        if (_zustand != null) {
            _zustand.setInfos("");
        }

        return new StartCalculationTask(getApplication());
    }

    private class StartCalculationTask extends org.jdesktop.application.Task<Object, Void> {

        StartCalculationTask(org.jdesktop.application.Application app) {
            // Runs on the EDT.  Copy GUI state that
            // doInBackground() depends on from parameters
            // to StartCalculationTask fields, here.
            super(app);
        }

        public void publicSetProgress(int i, int min, int max) {
            setProgress(i, min, max);
        }

        @Override
        protected Object doInBackground() throws ParserException, Exception {
            // Your Task's code here.  This method runs
            // on a background thread, so don't reference
            // the Swing GUI from here.

            setMessage("started the calculation");
            Welt welt = _welt;

            setProgress(0, 0, 100);

            long start = System.currentTimeMillis();
            long start_alg = 0;

            log.log(Level.INFO, "F\u00fchre aus: {0} mit dem Algorithmus {1}", new Object[]{_dateipfad, _algorithmus});

            int schritt = 0;
            int numSchritte = 0;

            numSchritte += 1; // parsen
            numSchritte += (_algorithmus >= 0 ? 2 : 0); // leerer Algorithmus od. Algorithmus
            numSchritte += (_algorithmus >= 1 ? 2 : 0); // Algorithmus + Bewertung
            numSchritte += (_verbesserung0 ? 1 : 0); // Verbesserung0
            numSchritte += (_verbesserung1 ? 1 : 0); // Verbesserung1

            System.out.println("Gewichte berechnet.");
            Weltfunktion weltfkt = new Weltfunktion(welt);

            if (welt.getApproxrate() == 1) {
                if (_approxmethode == 1) {
                    welt.setApproxrate(_approxrate);
                    weltfkt.Approximation();
                } else {
                    // die Heatmap ist ein long-Array, damit benoetigt jeder Punkt 64 bit = 8 byte im RAM.
                    long ramFrei = Runtime.getRuntime().freeMemory();
                    // soll maximal 80% des freien RAMs nutzen, aber nicht mehr als 200000 pixel
                    long zielgroesse = (long) Math.min(300000, (ramFrei / 8) * 0.8);
                    double flaechenfaktor;
                    flaechenfaktor = weltfkt.getMaximaleBreiteHoehe(true);
                    flaechenfaktor /= zielgroesse;
                    int neuApprox = (int) Math.sqrt(flaechenfaktor);
                    welt.setApproxrate(Math.max(1, neuApprox));
                    System.out.println("Approximation automatisch gesetzt: " + Math.max(1, neuApprox));
                    weltfkt.Approximation();
                    System.out.println("Neue Flaeche: " + weltfkt.getMaximaleBreiteHoehe(true));
                }
            }

            setMessage(Status.statustext("parsed") + " [" + schritt++ + "/" + numSchritte + "]");
            setProgress(0, 0, 100);

            //leerer Zustand
            Zustand zustand = _zustand;

            /*
             * nach jeweiligem Algorithmus verfahren
             */

            System.out.println("Algorithmus: " + _algorithmus);

            Algorithmus.ProgressCallbackDelegate progressDelegate = new Algorithmus.ProgressCallbackDelegate() {

                public void fortschritt(int pos, int min, int max) {
                    publicSetProgress(pos, min, max);
                }

                public void formRepaint() {
                    ((drawingPanel) drawingArea).repaint();
                }
            };

            if (_algorithmus == 0) {
                //kein algorithmus, vorberechnung

                setMessage(Status.statustext("creatingMap") + " [" + schritt++ + "/" + numSchritte + "]");
                setProgress(0, 0, 100);

                //erstellt pixelkarte
                weltfkt.erstellePixelkarte(progressDelegate);

                //zeigt die pixelkarte
                //System.out.println("Karte");
                //welt.getPixelkarte().schreibe();

                setMessage(Status.statustext("creatingWeightMap") + " [" + schritt++ + "/" + numSchritte + "]");
                setProgress(0, 0, 100);

                //erstellt zustand
                zustand = new Zustand(weltfkt, welt.getSollAnzahlAutomaten());

                //erstelle gewichtskarte
                zustand.erzeugeGewichtskarte(progressDelegate);
                weltfkt.setPixelUndGewichtskarteVorhanden(true);

                zustand.setInfos(zustand.getInfos() + Status.statustext("approxrate") + ": " + welt.getApproxrate() + ", ");

                //zeigt die gewichtskarte
                //System.out.println("Gewichtskarte:");
                //zustand.getGewichtskarte().schreibe();
                _paintPixels = true;

            } else if (_algorithmus == -1) {
                //nur welt zeichnen
                zustand = new Zustand(weltfkt, welt.getSollAnzahlAutomaten());
                weltfkt.setPixelUndGewichtskarteVorhanden(false);
                _paintPixels = false;
                zustand.setInfos(zustand.getInfos() + Status.statustext("approxrate") + ": " + welt.getApproxrate() + ", ");
            } else {
                //algoithmen, die was machen

                // Algorithmus aufrufen
                setMessage(Status.statustext("creatingMap") + " [" + schritt++ + "/" + numSchritte + "]");
                setProgress(0, 0, 100);

                //erstellt pixelkarte
                weltfkt.erstellePixelkarte(progressDelegate);

                //zeigt die pixelkarte
                //System.out.println("Karte");
                //welt.getPixelkarte().schreibe();

                setMessage(Status.statustext("creatingWeightMap") + " [" + schritt++ + "/" + numSchritte + "]");
                setProgress(0, 0, 100);

                //erstellt zustand
                if (zustand == null) {
                    zustand = new Zustand(weltfkt, welt.getSollAnzahlAutomaten());
                    // Erstelle gewichtskarte
                    zustand.erzeugeGewichtskarte(progressDelegate);
                    weltfkt.setPixelUndGewichtskarteVorhanden(true);
                }

                // DEBUG: Alle Automaten ausgeben
                for (int i = 0; i < zustand.getAnzahlAutomaten(); i++) {
                    Automat automat = zustand.getAutomat(i);

                    if (automat != null) {
                        // DEBUG: Automat ausgeben
                        // System.out.println(automat.getPosition().getX() + "," + automat.getPosition().getY() + " gesperrt: " + automat.getGesperrt());
                    }
                }

                zustand.setInfos(zustand.getInfos() + Status.statustext("approxrate") + ": " + welt.getApproxrate() + ", ");

                //zeigt die gewichtskarte
                //System.out.println("Gewichtskarte:");
                //zustand.getGewichtskarte().schreibe();

                Algorithmus algorithmus = null;

                setMessage(Status.statustext("firstAlgo") + " [" + schritt++ + "/" + numSchritte + "]");
                setProgress(0, 0, 100);

                start_alg = System.currentTimeMillis();

                // Eröffnungsalgorithmus auswählen und ausführen
                switch (_algorithmus) {
                    case 2:
                        log.info("Greedy");
                        // Greedy-Algorithmus aufrufen
                        algorithmus = new Greedy(weltfkt, zustand);
                        algorithmus.Berechne(progressDelegate);
                        zustand = algorithmus.getErgebnis();
                        zustand.setInfos(zustand.getInfos() + "Greedy, ");
                        break;
                    case 3:
                        log.info("Greedy mit Stichprobe");
                        //greedy mit stichprobe
                        algorithmus = new GreedyStichprobe(weltfkt, zustand);
                        algorithmus.Berechne(progressDelegate);
                        zustand = algorithmus.getErgebnis();
                        zustand.setInfos(zustand.getInfos() + "Greedy Stichproben, ");
                        break;
                    case 4:
                        log.info("Zufall");
                        //zufall
                        algorithmus = new Zufall(weltfkt, zustand);
                        algorithmus.Berechne(progressDelegate);
                        zustand = algorithmus.getErgebnis();
                        zustand.setInfos(zustand.getInfos() + "Zufall, ");
                        break;
                }

                // Finalen Verbesserungsalgorithmus/ Optimierungsalgorithmus drüberlaufen lassen
                if (_verbesserung0) {
                    setMessage(Status.statustext("optiAlgo") + " [" + schritt++ + "/" + numSchritte + "]");
                    setProgress(0, 0, 100);
                    log.info("Abkühlungsalgorithmus");
                    algorithmus = new Abkuehlung(weltfkt, zustand);
                    ((Abkuehlung) algorithmus).setInitTemperatur(_temperatur);
                    algorithmus.Berechne(progressDelegate);
                    zustand = algorithmus.getErgebnis();
                    zustand.setInfos(zustand.getInfos() + "Abkühlung, ");
                }
                if (_verbesserung2) {
                    setMessage(Status.statustext("optiAlgo") + " [" + schritt++ + "/" + numSchritte + "]");
                    setProgress(0, 0, 100);
                    log.info("Tabu-Suche");
                    algorithmus = new Tabu(weltfkt, zustand);
                    ((Tabu) algorithmus).setAnzahlDerIterationen(_tabuIterationen);
                    algorithmus.Berechne(progressDelegate);
                    zustand = algorithmus.getErgebnis();
                    zustand.setInfos(zustand.getInfos() + "Tabu, ");
                }
                if (_verbesserung1) {
                    setMessage(Status.statustext("optiAlgo") + " [" + schritt++ + "/" + numSchritte + "]");
                    setProgress(0, 0, 100);
                    log.info("Einzelverschiebung");
                    algorithmus = new Einzelverschiebung(weltfkt, zustand);
                    algorithmus.Berechne(progressDelegate);
                    zustand = algorithmus.getErgebnis();
                    zustand.setInfos(zustand.getInfos() + "Einzelverschiebung, ");
                }

                setMessage(Status.statustext("calcRating") + " [" + schritt++ + "/" + numSchritte + "]");
                setProgress(0, 0, 100);

                long bewertung = zustand.getBewertung();

                System.out.println("#################");
                System.out.println("Bewertung: " + bewertung);
                System.out.println("#################");


                _paintPixels = true;

            }

            _zustand = zustand;
            _welt = welt;
            //Dateiexport.dateiAusgeben("./Ausgabe.txt", _zustand, _approxrate);

            setMessage("finished");
            setProgress(100, 0, 100);

            long end = System.currentTimeMillis();

            setMessage("finished (time: " + String.valueOf((float) (end - start) / 1000) + " seconds )");

            System.out.println(Status.statustext("timeAlgo") + ": ");
            System.out.println((float) (end - start_alg) / 1000);

            System.out.println(Status.statustext("time") + ": ");
            System.out.println((float) (end - start) / 1000);

            if (_algorithmus > 1) {
                zustand.setInfos(zustand.getInfos() + Status.statustext("timeAlgo") + ": " + ((end - start_alg) / 1000) + "s ");
            } else {
                zustand.setInfos(zustand.getInfos() + Status.statustext("time") + ": " + ((end - start) / 1000) + "s ");
            }

            return null;  // return your result
                /*
            } catch (Exception e) {
            Nachrichten.WarnungAusgeben("2");
            return null;
            }
             *
             */
        }

        @Override
        protected void succeeded(Object result) {
            // Runs on the EDT.  Update the GUI based on
            // the result computed by doInBackground().

            if (_zustand != null && _paintPixels) {
                ((drawingPanel) drawingArea).paintGewichtskarte(_zustand);
            } else {
                ((drawingPanel) drawingArea).noPixelmap();
            }
            ((drawingPanel) drawingArea).paintVectorKarte(_welt);
            ((drawingPanel) drawingArea).paintAutomat(_welt, _zustand);
            ((drawingPanel) drawingArea).repaint();

            ((drawingPanel) drawingArea).setScale((double) jSlider1.getValue() / 50);
        }
    }

    /**
     * Zeigt den Berechnungsdialog an.
     */
    @Action
    public void showBerechnungsDialog() {
        newCalcFrame.setMain(this);
        if (newCalcFrame == null) {
            //JFrame mainFrame = InformaticupApp.getApplication().getMainFrame();
            //newCalcFrame = new createCalcFrame(mainFrame);

            newCalcFrame = new createCalcFrame();
            newCalcFrame.setMain(this);
        }

        InformaticupApp.getApplication().show(newCalcFrame);
    }

    @Action
    public void Save() {
        try {
            // Dateiauswahlfenster anzeigen
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int returnVal = fc.showSaveDialog(null);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                // Als txt speichern
                String dateipfad = (fc.getSelectedFile().getPath());
                Dateiexport.dateiAusgeben(dateipfad, _zustand);
            }
        } catch (Exception e) {
            Nachrichten.WarnungAusgeben("1");
        }
    }

    @Action
    public void showNewBerechnungDialog() {
        if (editCalcMenuItem.isEnabled()) {
            //Custom button text
            Object[] options = {resourceMap.getString("yes"), resourceMap.getString("noEdit"),
                resourceMap.getString("cancel")};
            int n = JOptionPane.showOptionDialog(mainPanel,
                    resourceMap.getString("newCalcDiagText"),
                    resourceMap.getString("newCalcDiagHead"),
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.WARNING_MESSAGE,
                    null,
                    options,
                    options[2]);
            switch (n) {
                case 0:
                    //ja
                    editCalcMenuItem.setEnabled(true);
                    _zustand = null;
                    ((drawingPanel) drawingArea).repaint();
                    newCalcFrame = new createCalcFrame();
                    newCalcFrame.setMain(this);
                    InformaticupApp.getApplication().show(newCalcFrame);
                    _zustand = null;
                    break;
                case 1:
                    //nein
                    newCalcFrame.setMain(this);
                    InformaticupApp.getApplication().show(newCalcFrame);
                    break;
                case 2:
                    //abbrechen
                    break;
            }
        } else {
            editCalcMenuItem.setEnabled(true);
            _zustand = null;
            ((drawingPanel) drawingArea).repaint();
            newCalcFrame = new createCalcFrame();
            newCalcFrame.setMain(this);
            InformaticupApp.getApplication().show(newCalcFrame);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem SaveMenuItem;
    private javax.swing.JPanel drawingArea;
    private javax.swing.JMenuItem editCalcMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JCheckBox jCheckName;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSlider jSlider1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JCheckBoxMenuItem menuAddNewATM;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JCheckBoxMenuItem menuLockUnlockATM;
    private javax.swing.JCheckBoxMenuItem menuRemoveATM;
    private javax.swing.JMenuItem newCalcMenuItem;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    // End of variables declaration//GEN-END:variables
    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;
    private JDialog aboutBox;
    private createCalcFrame newCalcFrame = new createCalcFrame();
}
