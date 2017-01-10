/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * newCalculation.java
 *
 */
package informaticup;

import informaticup.Parser.ParserException;
import informaticup.datenstruktur.Stadtteil;
import informaticup.datenstruktur.Welt;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.jdesktop.application.Action;

/**
 *
 *
 */
public class createCalcFrame extends javax.swing.JFrame {

    private InformaticupView mainFrame;
    // Zeiger auf die Welt
    private Welt _welt;

    public void setArgs(String[] args) {
        filePathTextField.setText(args[1]);

        switch (Integer.parseInt(args[2])) {
            case 0:
                greedyRadioButton.setSelected(true);
                break;
            case 1:
                greedy2RadioButton.setSelected(true);
                break;
            case 2:
                noneRadioButton.setSelected(true);
                break;
            case 3:
                mapRadioButton.setSelected(true);
                break;
            case 4:
                randomRadioButton.setSelected(true);
                break;
        }

        if (args[3].equals("0")) {
            autoRadioButton.setSelected(true);
        } else {
            manRadioButton.setSelected(true);
            approxRateTextField.setText(args[3]);
        }

        if (args[4].equals("1")) {
            coolingCheckBox.setSelected(true);
        }

        if (args[5].equals("1")) {
            tabuCheckBox.setSelected(true);
        }

        if (args[6].equals("1")) {
            moverCheckBox.setSelected(true);
        }

        //lese datei ein und parse sie zur _welt
        try {
            this.startParsing(args[1]);
        } catch (ParserException ex) {
            Logger.getLogger(createCalcFrame.class.getName()).log(Level.SEVERE, null, ex);
            Nachrichten.WarnungAusgeben("3");
            return;
        }

        // Attribute in der Tabelle erzeugen
        for (int i = 0; i < _welt.getStadtteil(0).getGewichtCount(); i++) {
            AttributHinzufuegen(_welt.getStadtteil(0).getGewichtname(i), Byte.parseByte("1"));
        }

        startButton1.doClick();
    }

    /** Creates new form newCalculation */
    public createCalcFrame() {
        initComponents();

        algoButtonGroup.add(greedyRadioButton);
        algoButtonGroup.add(noneRadioButton);
        algoButtonGroup.add(mapRadioButton);
        algoButtonGroup.add(greedy2RadioButton);
        algoButtonGroup.add(randomRadioButton);

        approxButtonGroup.add(autoRadioButton);
        approxButtonGroup.add(manRadioButton);

        getRootPane().setDefaultButton(startButton1);

        // Sicherstellen, dass der Benutzer keine falsche Eingaben machen kann!
        temperaturTextField.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                eingabenPruefen();
            }

            public void removeUpdate(DocumentEvent e) {
                eingabenPruefen();
            }

            public void changedUpdate(DocumentEvent e) {
                eingabenPruefen();
            }
        });
        iterationenTextField.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                eingabenPruefen();
            }

            public void removeUpdate(DocumentEvent e) {
                eingabenPruefen();
            }

            public void changedUpdate(DocumentEvent e) {
                eingabenPruefen();
            }
        });
        tableGewicht.getModel().addTableModelListener(new TableModelListener() {

            public void tableChanged(TableModelEvent e) {
                eingabenPruefen();
            }
        });

        startButton1.setEnabled(false);
    }

    public void setMain(InformaticupView aThis) {
        mainFrame = aThis;

        //setzt action starte Berechnung aus View für Berechnen Button
        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(informaticup.InformaticupApp.class).getContext().getActionMap(InformaticupView.class, aThis);
        startButton1.setAction(actionMap.get("startCalculation")); // NOI18N

        //setzt text
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(informaticup.InformaticupApp.class).getContext().getResourceMap(createCalcFrame.class);
        startButton1.setText(resourceMap.getString("startCalculation.text")); // NOI18N

        eingabenPruefen();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        algoButtonGroup = new javax.swing.ButtonGroup();
        approxButtonGroup = new javax.swing.ButtonGroup();
        improveButtonGroup = new javax.swing.ButtonGroup();
        standardPanel = new javax.swing.JPanel();
        fileChooserButton = new javax.swing.JButton();
        algoPanel = new javax.swing.JPanel();
        noneRadioButton = new javax.swing.JRadioButton();
        greedyRadioButton = new javax.swing.JRadioButton();
        mapRadioButton = new javax.swing.JRadioButton();
        greedy2RadioButton = new javax.swing.JRadioButton();
        randomRadioButton = new javax.swing.JRadioButton();
        temperaturTextField = new javax.swing.JTextField();
        coolingCheckBox = new javax.swing.JCheckBox();
        moverCheckBox = new javax.swing.JCheckBox();
        tabuCheckBox = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        iterationenTextField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        cancelButton = new javax.swing.JButton();
        startButton1 = new javax.swing.JButton();
        filePathTextField = new javax.swing.JTextField();
        approxPanel = new javax.swing.JPanel();
        approxRateTextField = new javax.swing.JTextField();
        autoRadioButton = new javax.swing.JRadioButton();
        manRadioButton = new javax.swing.JRadioButton();
        parameterPanel = new javax.swing.JPanel();
        scrollPanel = new javax.swing.JScrollPane();
        tableGewicht = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        toZeroButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(650, 520));
        setName("Form"); // NOI18N

        standardPanel.setName("standardPanel"); // NOI18N

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(informaticup.InformaticupApp.class).getContext().getActionMap(createCalcFrame.class, this);
        fileChooserButton.setAction(actionMap.get("loadFile")); // NOI18N
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(informaticup.InformaticupApp.class).getContext().getResourceMap(createCalcFrame.class);
        fileChooserButton.setText(resourceMap.getString("fileChooserButton.text")); // NOI18N
        fileChooserButton.setName("fileChooserButton"); // NOI18N
        fileChooserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileChooserButtonActionPerformed(evt);
            }
        });

        algoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createTitledBorder(""), resourceMap.getString("algoPanel.border.title"))); // NOI18N
        algoPanel.setName("algoPanel"); // NOI18N

        noneRadioButton.setSelected(true);
        noneRadioButton.setText(resourceMap.getString("noneRadioButton.text")); // NOI18N
        noneRadioButton.setName("noneRadioButton"); // NOI18N

        greedyRadioButton.setText(resourceMap.getString("greedyRadioButton.text")); // NOI18N
        greedyRadioButton.setName("greedyRadioButton"); // NOI18N

        mapRadioButton.setText(resourceMap.getString("mapRadioButton.text")); // NOI18N
        mapRadioButton.setName("mapRadioButton"); // NOI18N

        greedy2RadioButton.setText(resourceMap.getString("greedy2RadioButton.text")); // NOI18N
        greedy2RadioButton.setName("greedy2RadioButton"); // NOI18N

        randomRadioButton.setText(resourceMap.getString("randomRadioButton.text")); // NOI18N
        randomRadioButton.setName("randomRadioButton"); // NOI18N

        temperaturTextField.setText(resourceMap.getString("temperaturTextField.text")); // NOI18N
        temperaturTextField.setName("temperaturTextField"); // NOI18N

        coolingCheckBox.setText(resourceMap.getString("coolingCheckBox.text")); // NOI18N
        coolingCheckBox.setName("coolingCheckBox"); // NOI18N

        moverCheckBox.setText(resourceMap.getString("moverCheckBox.text")); // NOI18N
        moverCheckBox.setName("moverCheckBox"); // NOI18N

        tabuCheckBox.setText(resourceMap.getString("tabuCheckBox.text")); // NOI18N
        tabuCheckBox.setName("tabuCheckBox"); // NOI18N

        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        iterationenTextField.setText(resourceMap.getString("iterationenTextField.text")); // NOI18N
        iterationenTextField.setName("iterationenTextField"); // NOI18N

        jLabel4.setText(resourceMap.getString("jLabel4.text")); // NOI18N
        jLabel4.setName("jLabel4"); // NOI18N

        jLabel5.setText(resourceMap.getString("jLabel5.text")); // NOI18N
        jLabel5.setName("jLabel5"); // NOI18N

        javax.swing.GroupLayout algoPanelLayout = new javax.swing.GroupLayout(algoPanel);
        algoPanel.setLayout(algoPanelLayout);
        algoPanelLayout.setHorizontalGroup(
            algoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(algoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(algoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(randomRadioButton)
                    .addComponent(jLabel3)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, algoPanelLayout.createSequentialGroup()
                        .addGroup(algoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(coolingCheckBox)
                            .addComponent(moverCheckBox)
                            .addComponent(tabuCheckBox))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 129, Short.MAX_VALUE)
                        .addGroup(algoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(algoPanelLayout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(jLabel4))
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(algoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(iterationenTextField)
                            .addComponent(temperaturTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(algoPanelLayout.createSequentialGroup()
                        .addGroup(algoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(greedyRadioButton)
                            .addComponent(greedy2RadioButton)
                            .addComponent(jLabel1))
                        .addGap(18, 18, 18)
                        .addGroup(algoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(mapRadioButton)
                            .addComponent(noneRadioButton, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        algoPanelLayout.setVerticalGroup(
            algoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(algoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(algoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(algoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(algoPanelLayout.createSequentialGroup()
                        .addComponent(greedyRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(greedy2RadioButton))
                    .addGroup(algoPanelLayout.createSequentialGroup()
                        .addComponent(mapRadioButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(noneRadioButton)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(randomRadioButton)
                .addGap(64, 64, 64)
                .addComponent(jLabel3)
                .addGap(2, 2, 2)
                .addGroup(algoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(algoPanelLayout.createSequentialGroup()
                        .addComponent(coolingCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tabuCheckBox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(moverCheckBox))
                    .addGroup(algoPanelLayout.createSequentialGroup()
                        .addGroup(algoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(temperaturTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(algoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(iterationenTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(69, Short.MAX_VALUE))
        );

        cancelButton.setAction(actionMap.get("closeCalcFrame")); // NOI18N
        cancelButton.setText(resourceMap.getString("cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N

        startButton1.setText(resourceMap.getString("startButton1.text")); // NOI18N
        startButton1.setName("startButton1"); // NOI18N
        startButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startButton1ActionPerformed(evt);
            }
        });

        filePathTextField.setText(resourceMap.getString("filePathTextField.text")); // NOI18N
        filePathTextField.setEnabled(false);
        filePathTextField.setName("filePathTextField"); // NOI18N
        filePathTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filePathTextFieldActionPerformed(evt);
            }
        });

        approxPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("approxPanel.border.title"))); // NOI18N
        approxPanel.setName("approxPanel"); // NOI18N

        approxRateTextField.setText(resourceMap.getString("approxRateTextField.text")); // NOI18N
        approxRateTextField.setName("approxRateTextField"); // NOI18N
        approxRateTextField.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                approxRateTextFieldMouseClicked(evt);
            }
        });
        approxRateTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                approxRateTextFieldKeyTyped(evt);
            }
        });

        autoRadioButton.setSelected(true);
        autoRadioButton.setText(resourceMap.getString("autoRadioButton.text")); // NOI18N
        autoRadioButton.setName("autoRadioButton"); // NOI18N

        manRadioButton.setText(resourceMap.getString("manRadioButton.text")); // NOI18N
        manRadioButton.setName("manRadioButton"); // NOI18N

        javax.swing.GroupLayout approxPanelLayout = new javax.swing.GroupLayout(approxPanel);
        approxPanel.setLayout(approxPanelLayout);
        approxPanelLayout.setHorizontalGroup(
            approxPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, approxPanelLayout.createSequentialGroup()
                .addComponent(autoRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 165, Short.MAX_VALUE)
                .addComponent(manRadioButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(approxRateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        approxPanelLayout.setVerticalGroup(
            approxPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(approxPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(autoRadioButton)
                .addComponent(manRadioButton)
                .addComponent(approxRateTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout standardPanelLayout = new javax.swing.GroupLayout(standardPanel);
        standardPanel.setLayout(standardPanelLayout);
        standardPanelLayout.setHorizontalGroup(
            standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(standardPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(standardPanelLayout.createSequentialGroup()
                        .addComponent(startButton1)
                        .addGap(18, 18, 18)
                        .addComponent(cancelButton))
                    .addComponent(approxPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
            .addGroup(standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, standardPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(algoPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(standardPanelLayout.createSequentialGroup()
                            .addComponent(fileChooserButton)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(filePathTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE)))
                    .addContainerGap()))
        );
        standardPanelLayout.setVerticalGroup(
            standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, standardPanelLayout.createSequentialGroup()
                .addContainerGap(433, Short.MAX_VALUE)
                .addComponent(approxPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(startButton1)
                    .addComponent(cancelButton))
                .addGap(9, 9, 9))
            .addGroup(standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(standardPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(standardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(fileChooserButton)
                        .addComponent(filePathTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(algoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(110, 110, 110)))
        );

        parameterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(resourceMap.getString("parameterPanel.border.title"))); // NOI18N
        parameterPanel.setName("parameterPanel"); // NOI18N

        scrollPanel.setName("scrollPanel"); // NOI18N

        tableGewicht.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Attribute name", "Attribute weight (quantifier)"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Byte.class
            };
            boolean[] canEdit = new boolean [] {
                false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableGewicht.setName("tableGewicht"); // NOI18N
        tableGewicht.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tableGewichtKeyTyped(evt);
            }
        });
        scrollPanel.setViewportView(tableGewicht);
        tableGewicht.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("tableGewicht.columnModel.title0")); // NOI18N
        tableGewicht.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("tableGewicht.columnModel.title1")); // NOI18N

        jPanel1.setName("jPanel1"); // NOI18N

        toZeroButton.setAction(actionMap.get("setAllToZero")); // NOI18N
        toZeroButton.setText(resourceMap.getString("toZeroButton.text")); // NOI18N
        toZeroButton.setName("toZeroButton"); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(143, Short.MAX_VALUE)
                .addComponent(toZeroButton))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(toZeroButton)
        );

        javax.swing.GroupLayout parameterPanelLayout = new javax.swing.GroupLayout(parameterPanel);
        parameterPanel.setLayout(parameterPanelLayout);
        parameterPanelLayout.setHorizontalGroup(
            parameterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(parameterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(parameterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(parameterPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 287, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        parameterPanelLayout.setVerticalGroup(
            parameterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, parameterPanelLayout.createSequentialGroup()
                .addContainerGap(466, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(parameterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(parameterPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(scrollPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 443, Short.MAX_VALUE)
                    .addGap(32, 32, 32)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(standardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(parameterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(parameterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(standardPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * setzt den radiobutton für manuell, wenn in das textfeld geklickt wird
     * @param evt
     */
    private void approxRateTextFieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_approxRateTextFieldMouseClicked
        if (approxRateTextField.isEnabled()) {
            manRadioButton.setSelected(true);
        }
    }//GEN-LAST:event_approxRateTextFieldMouseClicked

    private void filePathTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filePathTextFieldActionPerformed
    }//GEN-LAST:event_filePathTextFieldActionPerformed

    private void fileChooserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileChooserButtonActionPerformed
    }//GEN-LAST:event_fileChooserButtonActionPerformed

    private void startButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startButton1ActionPerformed
        autoRadioButton.setEnabled(false);
        manRadioButton.setEnabled(false);
        approxRateTextField.setEnabled(false);
    }//GEN-LAST:event_startButton1ActionPerformed

    private void approxRateTextFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_approxRateTextFieldKeyTyped
        manRadioButton.setSelected(true);
    }//GEN-LAST:event_approxRateTextFieldKeyTyped

    private void tableGewichtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tableGewichtKeyTyped
        noneRadioButton.setSelected(true);
    }//GEN-LAST:event_tableGewichtKeyTyped

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new createCalcFrame().setVisible(true);
            }
        });
    }

    @Action
    public void closeCalcFrame() {
        this.dispose();
    }

    /**
     * Lädt eine Eingabedatei (Probleminstanz).
     */
    @Action
    public void loadFile() {
        System.out.println("Datei laden...");
        JFileChooser fc = new JFileChooser();

        // Zeigt nur die angegebenen Dateien an
        FileNameExtensionFilter filter = new FileNameExtensionFilter("txt", "txt");
        fc.setFileFilter(filter);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Zeigt den Dialog an
        int returnVal = fc.showOpenDialog(null);

        // Wurde "Öffnen" gedrückt?
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            // Tabelle leeren
            for (int i = 0; i < ((DefaultTableModel) tableGewicht.getModel()).getRowCount(); i++) {
                ((DefaultTableModel) tableGewicht.getModel()).removeRow(0);
            }

            filePathTextField.setText(fc.getSelectedFile().getPath());
            //filePathTextField.setEnabled(true);

            //lese datei ein und parse sie zur _welt
            try {
                this.startParsing(fc.getSelectedFile().getPath());
            } catch (ParserException ex) {
                Logger.getLogger(createCalcFrame.class.getName()).log(Level.SEVERE, null, ex);
                Nachrichten.WarnungAusgeben("3");
                return;
            }

            // Attribute in der Tabelle erzeugen
            for (int i = 0; i < _welt.getStadtteil(0).getGewichtCount(); i++) {
                AttributHinzufuegen(_welt.getStadtteil(0).getGewichtname(i), Byte.parseByte("1"));
            }

            eingabenPruefen();

            fileChooserButton.setEnabled(false);

            System.out.println("Datei geladen, welt gesetzt");
            System.out.println("welt: " + _welt);
        }
    }

    public Welt bereiteBerechnungVor() {
        System.out.println("beginne Berechnung");

        //setzt die gewichte nach gewählten parametern (gewichtung)
        DefaultTableModel model = (DefaultTableModel) tableGewicht.getModel();

        // Setze Gewichtung der Stadtteile
        for (Stadtteil teil : _welt.getStadtteile()) {
            int teilGewicht = 0;

            // Alle Attribute durchgehen
            for (int i = 0; i < model.getRowCount(); i++) {
                // Name als Feld ignorieren, da irrelevant!
                if (!((String) model.getValueAt(i, 0)).equalsIgnoreCase("name")) {
                    int gewichtung = (Byte) model.getValueAt(i, 1);
                    gewichtung *= teil.getGewicht(i);
                    teilGewicht += gewichtung;
                }
            }

            teil.setGewichtNachFkt(teilGewicht);
        }

        this.setVisible(false);

        return _welt;
    }

    /**
     * Prüft die Eingaben des Benutzers auf Gültigkeit.
     */
    private void eingabenPruefen() {
        // Hat der Benutzer gültige Zahlen eingegeben?
        if (istNumerischInteger(iterationenTextField.getText()) && istNumerischInteger(temperaturTextField.getText()) && Integer.parseInt(temperaturTextField.getText()) > 0 && Integer.parseInt(iterationenTextField.getText()) > 0) {
            // Tabelle überprüfen: es dürfen nicht nur Nullen eingetragen sein!
            boolean nichtNullGefunden = false;
            DefaultTableModel model = (DefaultTableModel) tableGewicht.getModel();

            for (int i = 0; i < model.getRowCount(); i++) {
                byte wert = (Byte) model.getValueAt(i, 1);
                // Name als Feld ignorieren, da irrelevant!
                if (!((String) model.getValueAt(i, 0)).equalsIgnoreCase("name")) {
                    if (wert != 0) {
                        nichtNullGefunden = true;
                    }

                    if (wert < 0) {
                        Nachrichten.WarnungAusgeben("7");
                        startButton1.setEnabled(false);
                        return;
                    }
                }
            }

            if (nichtNullGefunden) {
                startButton1.setEnabled(true);
            } else {
                startButton1.setEnabled(false);
            }
        } else {
            startButton1.setEnabled(false);
        }
    }

    /**
     * Überprüft, ob ein String ein gültiger Integer ist.
     * @param text String
     * @return Integer ja/nein?
     */
    private boolean istNumerischInteger(String text) {
        try {
            int x = Integer.parseInt(text);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * Übergibt der Klasse einen Zeiger auf die Welt.
     * @param welt Welt
     */
    public void setWelt(Welt welt) {
        _welt = welt;

        /*
        // Attribute in der Tabelle erzeugen
        for (int i = 0; i < welt.getStadtteil(0).getGewichtCount(); i++) {
        AttributHinzufuegen(welt.getStadtteil(0).getGewichtname(i), Byte.parseByte("1"));
        } */
    }

    public JTextField getIterationenTextField() {
        return iterationenTextField;
    }

    /**
     * Fügt ein Attribut in die Tabelle hinzu.
     * @param attributName Name des Attributes
     * @param attributGewicht Wert/Gewichtung des Attributes
     */
    public void AttributHinzufuegen(String attributName, byte attributGewicht) {
        ((DefaultTableModel) tableGewicht.getModel()).addRow(new Object[]{attributName, attributGewicht});
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup algoButtonGroup;
    private javax.swing.JPanel algoPanel;
    private javax.swing.ButtonGroup approxButtonGroup;
    private javax.swing.JPanel approxPanel;
    public javax.swing.JTextField approxRateTextField;
    public javax.swing.JRadioButton autoRadioButton;
    private javax.swing.JButton cancelButton;
    public javax.swing.JCheckBox coolingCheckBox;
    private javax.swing.JButton fileChooserButton;
    public javax.swing.JTextField filePathTextField;
    public javax.swing.JRadioButton greedy2RadioButton;
    public javax.swing.JRadioButton greedyRadioButton;
    private javax.swing.ButtonGroup improveButtonGroup;
    private javax.swing.JTextField iterationenTextField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    public javax.swing.JRadioButton manRadioButton;
    public javax.swing.JRadioButton mapRadioButton;
    public javax.swing.JCheckBox moverCheckBox;
    public javax.swing.JRadioButton noneRadioButton;
    private javax.swing.JPanel parameterPanel;
    public javax.swing.JRadioButton randomRadioButton;
    private javax.swing.JScrollPane scrollPanel;
    private javax.swing.JPanel standardPanel;
    private javax.swing.JButton startButton1;
    private javax.swing.JTable tableGewicht;
    public javax.swing.JCheckBox tabuCheckBox;
    public javax.swing.JTextField temperaturTextField;
    private javax.swing.JButton toZeroButton;
    // End of variables declaration//GEN-END:variables

    private void startParsing(String path) throws ParserException {
        // Datei laden (Parser)
        String datei = "";
        try {
            datei = Dateiimport.dateiEinlesen(path);
        } catch (IOException ex) {
            Logger.getLogger(InformaticupApp.class.getName()).log(Level.SEVERE, null, ex);
        }
        _welt = Parser.Parse(datei);
    }

    /**
     * Wird aufgerufen, wenn der Benutzer alle Gewichtungen auf Null setzen will.
     */
    @Action
    public void setAllToZero() {
        // Setze alle Attribut Gewichtungen auf "0"
        DefaultTableModel model = (DefaultTableModel) tableGewicht.getModel();

        for (int i = 0; i < model.getRowCount(); i++) {
            model.setValueAt(Byte.parseByte("0"), i, 1);
        }
    }
}
