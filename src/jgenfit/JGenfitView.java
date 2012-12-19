/*
 * JGenfitView.java
 */
package jgenfit;

import jgenfit.settings.CompilerJDialog;
import java.io.FileNotFoundException;
import jgenfit.calculation.SccateringParametersJDialog;
import jgenfit.general.GeneralJDialog;
import jgenfit.calculation.SingleExperimentJDialog;
import java.util.logging.Level;
import java.util.logging.Logger;
import jgenfit.bussines.experiment.SingleExperimentSection;
import jgenfit.bussines.experiment.ModelSingleExperiment;
import jgenfit.events.GenfitEvent;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import jgenfit.bussines.GenfitController;
import jgenfit.bussines.GenfitFile;
import jgenfit.bussines.experiment.GeneralSection;
import jgenfit.bussines.experiment.GenfitModel;
import jgenfit.bussines.experiment.Parameter;
import jgenfit.bussines.experiment.SingleExperiment;
import jgenfit.events.GenfitEventListener;
import jgenfit.settings.SettingsJDialog;
import utils.GenfitPropertiesReader;

/**
 * The application's main frame.
 */
public class JGenfitView extends FrameView implements GenfitEventListener {

    /** Variables for controlling the GUI **/
    private int experimentModelSelected = -1;
    /** business variables **/
    private GenfitController genfitController = new GenfitController();
    private int subModelSelected = -1;
    /** Dialogs **/
    private GeneralJDialog generalDialog;
    private SettingsJDialog settingsJDialog;
    private SingleExperimentJDialog singleExperimentDialog;
    private WeightParameterJDialog weightParameterDialog;
    private int experimentModelSelectedingle = -1;
    private SccateringParametersJDialog scatteringParameterDialog;
    private ModelListJDialog modelListDialog;
    private SubmodelDialog submodelDialog;
    private EditModelDialog editModelDialog;
    public File lastFile = null;

    public JGenfitView(SingleFrameApplication app) {
        super(app);

        initComponents();

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
        try {
            String lastFilePath = GenfitPropertiesReader.readLastOpenedFile();
            if (lastFilePath != null){
                    if (new File(lastFilePath).exists()){ 
                        this.lastFile = new File(lastFilePath);
                        this.genfitController.setGenfitFile(new GenfitFile(lastFilePath));
                        this.fillExperimentTable(this.genfitController);
                    }
                }
                
     
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JGenfitView.class.getName()).log(Level.SEVERE, "No settings.properties file found.");
            
        } catch (IOException ex) {
            Logger.getLogger(JGenfitView.class.getName()).log(Level.SEVERE, null, ex);
        }
         
       
       
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = JGenfitApp.getApplication().getMainFrame();
            aboutBox = new JGenfitAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        JGenfitApp.getApplication().show(aboutBox);
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
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableExperiment = new javax.swing.JTable();
        jButtonScatteringParameters = new javax.swing.JButton();
        jButtonEdit = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTableModel = new javax.swing.JTable();
        jButtonAddExperiment = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jRemoveModel = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTablesubModel = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jMenuItem7 = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setPreferredSize(new java.awt.Dimension(840, 550));

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(jgenfit.JGenfitApp.class).getContext().getResourceMap(JGenfitView.class);
        jPanel1.setBackground(resourceMap.getColor("jPanel1.background")); // NOI18N
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel1.setForeground(resourceMap.getColor("jPanel1.foreground")); // NOI18N
        jPanel1.setName("jPanel1"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jTableExperiment.setForeground(resourceMap.getColor("jTableExperiment.foreground")); // NOI18N
        jTableExperiment.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Models", "Scattering curve files", "Description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTableExperiment.setName("jTableExperiment"); // NOI18N
        jTableExperiment.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTableExperiment.getTableHeader().setReorderingAllowed(false);
        jTableExperiment.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableExperimentMouseClicked(evt);
            }
        });
        jTableExperiment.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTableExperimentPropertyChange(evt);
            }
        });
        jScrollPane1.setViewportView(jTableExperiment);
        jTableExperiment.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTableExperiment.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTableExperiment.columnModel.title1")); // NOI18N
        jTableExperiment.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jTableExperiment.columnModel.title2")); // NOI18N
        jTableExperiment.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("jTableExperiment.columnModel.title0")); // NOI18N

        jButtonScatteringParameters.setText(resourceMap.getString("jButtonScatteringParameters.text")); // NOI18N
        jButtonScatteringParameters.setName("jButtonScatteringParameters"); // NOI18N
        jButtonScatteringParameters.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonScatteringParametersActionPerformed(evt);
            }
        });

        jButtonEdit.setText(resourceMap.getString("jButtonEdit.text")); // NOI18N
        jButtonEdit.setName("jButtonEdit"); // NOI18N
        jButtonEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonEditActionPerformed(evt);
            }
        });

        jPanel2.setBackground(resourceMap.getColor("jPanel2.background")); // NOI18N
        jPanel2.setName("jPanel2"); // NOI18N

        jLabel1.setFont(jLabel1.getFont());
        jLabel1.setForeground(resourceMap.getColor("jLabel1.foreground")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 151, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(634, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLabel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(13, 13, 13)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 575, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButtonScatteringParameters, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                    .add(jButtonEdit, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))
                .addContainerGap())
            .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jButtonEdit)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButtonScatteringParameters))
                    .add(jScrollPane1, 0, 0, Short.MAX_VALUE))
                .add(208, 208, 208))
        );

        jPanel3.setBackground(resourceMap.getColor("jPanel3.background")); // NOI18N
        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel3.setName("jPanel3"); // NOI18N

        jPanel4.setBackground(resourceMap.getColor("jPanel4.background")); // NOI18N
        jPanel4.setName("jPanel4"); // NOI18N

        jLabel2.setFont(jLabel2.getFont());
        jLabel2.setForeground(resourceMap.getColor("jLabel2.foreground")); // NOI18N
        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .add(10, 10, 10)
                .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 151, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(633, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLabel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        jTableModel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Model"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jTableModel.setName("jTableModel"); // NOI18N
        jTableModel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTableModelMouseClicked(evt);
            }
        });
        jTableModel.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTableModelKeyPressed(evt);
            }
        });
        jScrollPane2.setViewportView(jTableModel);
        jTableModel.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTableModel.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTableModel.columnModel.title0")); // NOI18N

        jButtonAddExperiment.setText(resourceMap.getString("jButtonAddExperiment.text")); // NOI18N
        jButtonAddExperiment.setName("jButtonAddExperiment"); // NOI18N
        jButtonAddExperiment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonAddExperimentActionPerformed(evt);
            }
        });

        jButton1.setText(resourceMap.getString("jButton1.text")); // NOI18N
        jButton1.setName("jButton1"); // NOI18N
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jRemoveModel.setText(resourceMap.getString("jRemoveModel.text")); // NOI18N
        jRemoveModel.setName("jRemoveModel"); // NOI18N
        jRemoveModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRemoveModelActionPerformed(evt);
            }
        });

        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 561, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jRemoveModel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                    .add(jButtonAddExperiment, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                    .add(jButton3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jButton1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE))
                .addContainerGap())
            .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 137, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(11, 11, 11)
                        .add(jButton1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jButton3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jButtonAddExperiment)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jRemoveModel)))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5.setBackground(resourceMap.getColor("jPanel5.background")); // NOI18N
        jPanel5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel5.setName("jPanel5"); // NOI18N

        jPanel6.setBackground(resourceMap.getColor("jPanel6.background")); // NOI18N
        jPanel6.setName("jPanel6"); // NOI18N

        jLabel3.setFont(jLabel3.getFont());
        jLabel3.setForeground(resourceMap.getColor("jLabel3.foreground")); // NOI18N
        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 151, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(622, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 30, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );

        jScrollPane3.setAlignmentY(0.0F);
        jScrollPane3.setName("jScrollPane3"); // NOI18N

        jTablesubModel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Starting", "Lower", "Upper", "Flag"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTablesubModel.setName("jTablesubModel"); // NOI18N
        jTablesubModel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablesubModelMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTablesubModel);
        jTablesubModel.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTablesubModel.getColumnModel().getColumn(0).setHeaderValue(resourceMap.getString("jTablesubModel.columnModel.title0")); // NOI18N
        jTablesubModel.getColumnModel().getColumn(1).setHeaderValue(resourceMap.getString("jTablesubModel.columnModel.title1")); // NOI18N
        jTablesubModel.getColumnModel().getColumn(2).setHeaderValue(resourceMap.getString("jTablesubModel.columnModel.title2")); // NOI18N
        jTablesubModel.getColumnModel().getColumn(3).setHeaderValue(resourceMap.getString("jTablesubModel.columnModel.title3")); // NOI18N
        jTablesubModel.getColumnModel().getColumn(4).setHeaderValue(resourceMap.getString("jTablesubModel.columnModel.title4")); // NOI18N

        jButton2.setText(resourceMap.getString("jButton2.text")); // NOI18N
        jButton2.setName("jButton2"); // NOI18N
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 559, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jButton2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButton2)
                    .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 118, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 116, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(1201, 1201, 1201))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setText(resourceMap.getString("jMenuItem6.text")); // NOI18N
        jMenuItem6.setName("jMenuItem6"); // NOI18N
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem6);

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem7.setText(resourceMap.getString("jMenuItem7.text")); // NOI18N
        jMenuItem7.setName("jMenuItem7"); // NOI18N
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        fileMenu.add(jMenuItem7);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(jgenfit.JGenfitApp.class).getContext().getActionMap(JGenfitView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N

        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem1MouseClicked(evt);
            }
        });
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        menuBar.add(jMenu1);

        jMenu2.setText(resourceMap.getString("jMenu2.text")); // NOI18N
        jMenu2.setName("jMenu2"); // NOI18N

        jMenuItem3.setText(resourceMap.getString("jMenuItem3.text")); // NOI18N
        jMenuItem3.setName("jMenuItem3"); // NOI18N
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem8.setText(resourceMap.getString("jMenuItem8.text")); // NOI18N
        jMenuItem8.setName("jMenuItem8"); // NOI18N
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem8);

        menuBar.add(jMenu2);

        jMenu3.setText(resourceMap.getString("jMenu3.text")); // NOI18N
        jMenu3.setName("jMenu3"); // NOI18N
        jMenu3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu3ActionPerformed(evt);
            }
        });

        jMenuItem4.setText(resourceMap.getString("jMenuItem4.text")); // NOI18N
        jMenuItem4.setName("jMenuItem4"); // NOI18N
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuItem5.setText(resourceMap.getString("jMenuItem5.text")); // NOI18N
        jMenuItem5.setName("jMenuItem5"); // NOI18N
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuItem9.setText(resourceMap.getString("jMenuItem9.text")); // NOI18N
        jMenuItem9.setName("jMenuItem9"); // NOI18N
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem9ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem9);

        jMenuItem2.setText(resourceMap.getString("jMenuItem2.text")); // NOI18N
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem2);

        menuBar.add(jMenu3);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        org.jdesktop.layout.GroupLayout statusPanelLayout = new org.jdesktop.layout.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 840, Short.MAX_VALUE)
            .add(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(statusMessageLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 670, Short.MAX_VALUE)
                .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelLayout.createSequentialGroup()
                .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(statusMessageLabel)
                    .add(statusAnimationLabel)
                    .add(progressBar, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    /** RUN DIALOG **/
private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
    RunJDialog runJDialog = new RunJDialog(null, true);
    runJDialog.setController(this.genfitController);
    runJDialog.setModal(false);
    runJDialog.setVisible(true);


}//GEN-LAST:event_jMenuItem3ActionPerformed

private void RemoveExperimentTableRows(){
        DefaultTableModel tableModel = (DefaultTableModel) this.jTableExperiment.getModel();
        while (tableModel.getRowCount() != 0) {
            tableModel.removeRow(0);
        }

}


    private void fillExperimentTable(GenfitController genfitController) {
        
        SingleExperimentSection singleExperimentSection = genfitController.getSingleExperimentSection();
        
        this.RemoveExperimentTableRows();
        DefaultTableModel tableModel = (DefaultTableModel) this.jTableExperiment.getModel();
/*
        while (tableModel.getRowCount() != 0) {
            tableModel.removeRow(0);
        }
*/
        for (int i = 0; i < singleExperimentSection.getSingleExperimentCount(); i++) {
            SingleExperiment singleExperiment = singleExperimentSection.getExperiments().get(i);
            tableModel.addRow(new Object[]{singleExperiment.getModelsNumber(), singleExperiment.getExperimentalScatteringCurve(), singleExperiment.getDescription()});
        }
        
        if (this.experimentModelSelected != -1)
            this.jTableExperiment.setRowSelectionInterval(this.experimentModelSelected, this.experimentModelSelected);

    }

    
    
    /** OPEN FILE DIALOG **/
private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem6ActionPerformed
    OpenJDialog openJDialog = new OpenJDialog(null, true);
    openJDialog.setGenfitFileController(this.genfitController);
    String sasFolder = null;
        try {
            sasFolder = GenfitPropertiesReader.getGenfitFolder();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(JGenfitView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(JGenfitView.class.getName()).log(Level.SEVERE, null, ex);
        }
            
     
    if (this.lastFile != null){
        openJDialog.setSelectedFile(this.lastFile);
    }
    else{
      if (new File(sasFolder).exists()){
                openJDialog.setSelectedFile(new File(sasFolder));
        }
    }
    openJDialog.setVisible(true);
    
    
    this.removeParameterRows();
    this.removeExperimentModelRows();
    this.RemoveExperimentTableRows();
    
    this.lastFile = this.genfitController.getGenfitFile();
    this.fillExperimentTable(this.genfitController);

    if (jTableExperiment.getRowCount() > 0){
        this.jTableExperiment.setRowSelectionInterval(0, 0);
        this.experimentModelSelected = 0;        
        this.fillExperimentsModel();
    }
}//GEN-LAST:event_jMenuItem6ActionPerformed

    /** Add new experiment **/
private void jButtonAddExperimentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonAddExperimentActionPerformed
    this.modelListDialog = new ModelListJDialog(null, true);
    modelListDialog.setModels(this.genfitController.getModelList());
    modelListDialog.genfitEvent.addListener(this);
    modelListDialog.setVisible(true);

}//GEN-LAST:event_jButtonAddExperimentActionPerformed

    /** Edit experiment **/
private void jButtonEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonEditActionPerformed
    this.singleExperimentDialog = new SingleExperimentJDialog(null, true);
    this.singleExperimentDialog.genfitEvent.addListener(this);

    this.singleExperimentDialog.setSingleExperiment(this.genfitController.getSingleExperimentSection().getExperiments().get(this.jTableExperiment.getSelectedRow()));
    this.singleExperimentDialog.setTitle(this.genfitController.getSingleExperimentSection().getExperiments().get(this.jTableExperiment.getSelectedRow()).getDescription());
    this.singleExperimentDialog.setVisible(true);
    
    

}//GEN-LAST:event_jButtonEditActionPerformed

private void jTableExperimentPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTableExperimentPropertyChange
    //System.out.println("Property changed");
}//GEN-LAST:event_jTableExperimentPropertyChange

private void jTableExperimentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableExperimentMouseClicked
    this.experimentModelSelected = this.jTableExperiment.getSelectedRow();
   
    this.fillExperimentsModel();
    
    if (evt.getClickCount() == 2) {
     this.jButtonEditActionPerformed(null);
   }

}//GEN-LAST:event_jTableExperimentMouseClicked

private void removeExperimentModelRows(){
        DefaultTableModel tableModel = (DefaultTableModel) this.jTableModel.getModel();
        while (tableModel.getRowCount() != 0) {
            tableModel.removeRow(0);
        }
}


    private void fillExperimentsModel() {
        List<String> models = this.genfitController.getModelList().getModelName(this.genfitController.getSingleExperimentSection().getExperiments().get(this.experimentModelSelected).getModelsNumber());
        DefaultTableModel tableModel = (DefaultTableModel) this.jTableModel.getModel();
        this.removeExperimentModelRows();
        for (int i = 0; i < models.size(); i++) {
            tableModel.addRow(new Object[]{models.get(i)});
        }
        
        if (models.size() > 0){
            if (this.experimentModelSelectedingle != -1){
                this.jTableModel.setRowSelectionInterval(this.experimentModelSelectedingle, this.experimentModelSelectedingle);
            }
        }
        

    }

private void jTableModelKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTableModelKeyPressed
}//GEN-LAST:event_jTableModelKeyPressed

private void removeParameterRows(){
        DefaultTableModel tableSubModel = (DefaultTableModel) this.jTablesubModel.getModel();

    while (tableSubModel.getRowCount() != 0) {
        tableSubModel.removeRow(0);
    }
}

private void fillParameters(){
    try{
        this.experimentModelSelectedingle = this.jTableModel.getSelectedRow();
        GenfitModel model = this.genfitController.getModelList().getModel(this.genfitController.getSingleExperimentSection().getExperiments().get(this.experimentModelSelected).getModelsNumber().get(this.experimentModelSelectedingle));
        this.removeParameterRows();
        DefaultTableModel tableSubModel = (DefaultTableModel) this.jTablesubModel.getModel();

        for (int i = 0; i < model.getSubmodelsText().size(); i++) {
            tableSubModel.addRow(new Object[]{model.getSubmodelsText().get(i).replace(".", "").replace(":", ""), model.getSubmodel(i).getStarting(), model.getSubmodel(i).getLower(), model.getSubmodel(i).getUpper(), model.getSubmodel(i).getFlag()});
        }
     }
    catch(Exception exp){
        System.out.println(exp.getMessage());
    }
}


private void jTableModelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableModelMouseClicked
    this.fillParameters();
    
}//GEN-LAST:event_jTableModelMouseClicked

private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked

 
    
   

}//GEN-LAST:event_jButton2MouseClicked

private void jTablesubModelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablesubModelMouseClicked
    this.subModelSelected = this.jTablesubModel.getSelectedRow();
}//GEN-LAST:event_jTablesubModelMouseClicked

private void jMenuItem1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem1MouseClicked
}//GEN-LAST:event_jMenuItem1MouseClicked

    /** LISTENERS **/
    public void actionPerformed(ActionEvent e) {
        //System.out.println("Click " + e.getActionCommand());
    }

private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
    this.generalDialog = new GeneralJDialog(null, true);
    GeneralSection general = this.genfitController.getGeneralSection();
    generalDialog.genfitEvent.addListener(this);
    generalDialog.setGeneralSection(general);
    generalDialog.setVisible(true);

}//GEN-LAST:event_jMenuItem1ActionPerformed

private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
   
 
   
    
    
}//GEN-LAST:event_jButton1MouseClicked

private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    this.weightParameterDialog = new WeightParameterJDialog(null, true);
    this.weightParameterDialog.setModel(this.genfitController.getSingleExperimentSection().getExperiments().get(this.experimentModelSelected).getModels().get(this.experimentModelSelectedingle));
    this.weightParameterDialog.genfitEvent.addListener(this);
    this.weightParameterDialog.setVisible(true);
}//GEN-LAST:event_jButton3ActionPerformed

private void jRemoveModelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRemoveModelActionPerformed
    int selectedExperimentModel = this.jTableModel.getSelectedRow();
    this.genfitController.removeExperimentModel(this.jTableExperiment.getSelectedRow(), selectedExperimentModel);
    this.fillExperimentTable(genfitController);
    this.fillExperimentsModel();
    this.jTableExperiment.setRowSelectionInterval(0, 0);
    save();
}//GEN-LAST:event_jRemoveModelActionPerformed

private void jButtonScatteringParametersActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonScatteringParametersActionPerformed
    this.experimentModelSelected = this.jTableExperiment.getSelectedRow();
    this.scatteringParameterDialog = new SccateringParametersJDialog(null, true);
    this.scatteringParameterDialog.genfitEvent.addListener(this);
    this.scatteringParameterDialog.setExperiment(this.genfitController.getSingleExperimentSection().getExperiments().get(this.experimentModelSelected));
    this.scatteringParameterDialog.setVisible(true);
}//GEN-LAST:event_jButtonScatteringParametersActionPerformed

    /** EDIT MODEL **/
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
       this.editModelDialog = new EditModelDialog(null, true);
       System.out.println(this.genfitController.getModelList().getModel(this.getModelSelected()));
       this.editModelDialog.setModel(this.genfitController.getModelList().getModel(this.getModelSelected()));
       this.editModelDialog.genfitEvent.addListener(this);
       this.editModelDialog.setVisible(true);
        
    }//GEN-LAST:event_jButton1ActionPerformed

    /** SAVE **/
    private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem7ActionPerformed
        SaveJDialog saveJDialog = new SaveJDialog(null, true);
        saveJDialog.setGenfitFileController(this.genfitController);
        saveJDialog.setVisible(true);     
    }//GEN-LAST:event_jMenuItem7ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
         GenfitModel model = this.genfitController.getModelList().getModel(this.genfitController.getSingleExperimentSection().getExperiments().get(this.experimentModelSelected).getModelsNumber().get(this.jTableModel.getSelectedRow()));
            Parameter submodel = model.getSubmodel(this.subModelSelected);
    
    this.submodelDialog = new SubmodelDialog(null, true);
    this.submodelDialog.genfitEvent.addListener(this);
    this.submodelDialog.setSubmodel(submodel);
    this.submodelDialog.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

private void jMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu3ActionPerformed
   
}//GEN-LAST:event_jMenu3ActionPerformed

private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
 this.settingsJDialog = new SettingsJDialog(null, true);
    this.settingsJDialog.setVisible(true);  
   
}//GEN-LAST:event_jMenuItem2ActionPerformed

private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem8ActionPerformed
    ExploreResultsJDialog dialog = new ExploreResultsJDialog(null, true);
    dialog.setVisible(true);
}//GEN-LAST:event_jMenuItem8ActionPerformed

private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem5ActionPerformed
    EditFileJDialog editFileJDialog = new EditFileJDialog(null, true);
    
   
        try {
            String advancedFileName = GenfitPropertiesReader.readAdvancedSettingsfile();
            String sasPath = GenfitPropertiesReader.getGenfitFolder();

            File file = new File(sasPath);
            for (File child : file.listFiles()) {
                if (child.getName().equals(advancedFileName)){
                     editFileJDialog.setFile(child);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(JGenfitView.class.getName()).log(Level.SEVERE, null, ex);
        }

        editFileJDialog.setVisible(true);
}//GEN-LAST:event_jMenuItem5ActionPerformed

private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem4ActionPerformed
            CompilerJDialog dialog = new CompilerJDialog(null, true);
            dialog.setVisible(true);  
      
}//GEN-LAST:event_jMenuItem4ActionPerformed

private void jMenuItem9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem9ActionPerformed
  EditFileJDialog editFileJDialog = new EditFileJDialog(null, true);
    
   
        try {
            String advancedFileName = GenfitPropertiesReader.readAdvancedCommonsSettingsfile();
            String sasPath = GenfitPropertiesReader.getGenfitFolder();

            File file = new File(sasPath);
            for (File child : file.listFiles()) {
                if (child.getName().equals(advancedFileName)){
                     editFileJDialog.setFile(child);
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(JGenfitView.class.getName()).log(Level.SEVERE, null, ex);
        }

        editFileJDialog.setVisible(true);
}//GEN-LAST:event_jMenuItem9ActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButtonAddExperiment;
    private javax.swing.JButton jButtonEdit;
    private javax.swing.JButton jButtonScatteringParameters;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JButton jRemoveModel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTableExperiment;
    private javax.swing.JTable jTableModel;
    private javax.swing.JTable jTablesubModel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
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

    //
    public int getModelSelected(){
        return this.genfitController.getSingleExperimentSection().getExperiments().get(this.experimentModelSelected).getModelsNumber().get(this.jTableModel.getSelectedRow());
    }
    
    
    public void handleGenfitEvent(GenfitEvent e) {               
        
       // System.out.println("A.- " + this.experimentModelSelected);
        switch (e.getType()) {
            case GENERAL_SAVE:
                genfitController.save(this.generalDialog.generalSection);
                return;
               

            case SINGLE_EXPERIMENT_SAVE:

                SingleExperiment singleExperiment = this.singleExperimentDialog.singleExperiment;
                genfitController.save(singleExperiment);
               
                this.fillExperimentTable(this.genfitController);
                
                return;

            case WEIGHT_PARAMETERS_SAVE:
                ModelSingleExperiment model = this.weightParameterDialog.getModel();
                genfitController.save(this.jTableExperiment.getSelectedRow(), this.jTableModel.getSelectedRow(), model);
                this.weightParameterDialog.setVisible(false);
                
                break;
            case SCATTERING_PARAMETERS_SAVE:
                SingleExperiment singleExperiment2 = this.scatteringParameterDialog.getExperiment();
                genfitController.save(singleExperiment2);

                break;
                
            case ADD_NEW_EXPERIMENT_MODEL:
                int modelSelected = this.modelListDialog.getModelSelected();
                SingleExperiment single = this.genfitController.getSingleExperimentSection().getExperiments().get(this.jTableExperiment.getSelectedRow());                
                single.addNewModel(modelSelected);    
                genfitController.save(single);

                break;
            case PARAMETER_SAVE:                                
                this.genfitController.save(this.submodelDialog.getSubmodel(), this.getModelSelected(), this.subModelSelected);
                
                break;
            
            case SAVE_EXPERIMENT_MODEL:
                
                this.genfitController.save(this.getModelSelected(), this.editModelDialog.getModel());
                break;
                
            default:
                break;

        }

      
        this.fillExperimentTable(this.genfitController);
        this.fillExperimentsModel();
        this.fillParameters();
        /*
         System.out.println(this.experimentModelSelected);
         System.out.println(this.experimentModelSelectedingle);
         System.out.println(this.subModelSelected);
         */
         
        /* 
        if (this.experimentModelSelected < 0){
            this.jTableExperiment.setRowSelectionInterval(this.experimentModelSelected, this.experimentModelSelected);
        }
        else{
            this.experimentModelSelected = 0;
        }
        
        if (this.experimentModelSelectedingle < 0){
            this.jTableModel.setRowSelectionInterval(this.experimentModelSelectedingle, this.experimentModelSelectedingle);
        }
        
        if (this.subModelSelected < 0){
            this.jTablesubModel.setRowSelectionInterval(this.subModelSelected, this.subModelSelected);
        }*/
        save();

    }

    private void save() {
      
        try {
            //String path = "/Users/demalhi";
            String path = "c:\\demalhi";
            
            if (new File(path).exists()){
                genfitController.save(path + "\\t.txt");
            }
            
        } catch (Exception ex) {
            Logger.getLogger(JGenfitView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
