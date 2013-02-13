/*
 * JGenfitView.java
 */
package jgenfit;

import java.awt.Image;
import java.awt.Toolkit;
import jgenfit.dialog.file.OpenJDialog;
import jgenfit.dialog.file.EditFileJDialog;
import jgenfit.settings.CompilerJDialog;
import java.io.FileNotFoundException;
import jgenfit.calculation.SccateringParametersDialog;
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
import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import jgenfit.bussines.GenfitController;
import jgenfit.bussines.GenfitFile;
import jgenfit.bussines.experiment.GeneralSection;
import jgenfit.bussines.experiment.GenfitModel;
import jgenfit.bussines.experiment.Parameter;
import jgenfit.bussines.experiment.SingleExperiment;
import jgenfit.calculation.CurveParameterDialog;
import jgenfit.dialog.file.AdvancedPropertiesEditFileJDialog;
import jgenfit.events.GenfitEventListener;
import jgenfit.settings.SettingsJDialog;
import jgenfit.utils.GenfitLogger;
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
    private WeightParameterDialog weightParameterDialog;
    private int experimentModelSelectedingle = -1;
    private SccateringParametersDialog scatteringParameterDialog;
    private ModelListJDialog modelListDialog;
    private SubmodelDialog submodelDialog;
    private EditModelDialog editModelDialog;
    public File lastFile = null;
    private CurveParameterDialog curveParametersDialog;

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
        //progressBar.setVisible(false);

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
                    //progressBar.setVisible(true);
                   // progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                   // progressBar.setVisible(false);
                    //progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String) (evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer) (evt.getNewValue());
                  //  progressBar.setVisible(true);
                   // progressBar.setIndeterminate(false);
                   // progressBar.setValue(value);
                }
            }
        });
        try {
           /* Image icon = Toolkit.getDefaultToolkit().getImage("C:/Users/demariaa/jgenfit/src/images/splash.jpg"); // specify the correct url path here
		
        
             this.getFrame().setIconImage(icon);*/
            /** Checking if SAS home has already been set **/
            if (new File(GenfitPropertiesReader.getGenfitFolderAbsolutePath()).exists()){
                GenfitLogger.info("SAS home set: " + GenfitPropertiesReader.getGenfitFolderAbsolutePath());
            }
            else{
                GenfitLogger.info("SAS home doesn't exist ("+ GenfitPropertiesReader.getGenfitFolderAbsolutePath()+"). ");
                /** we open the options window to set the parameter needed to open a file **/
                this.onOptions(null);
            }
            
            String lastFilePath = GenfitPropertiesReader.readLastOpenedFile();            
            if (lastFilePath != null){
                    if (new File(lastFilePath).exists()){ 
                        this.setStatusBarLabel("File loaded: " + lastFilePath);
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

  private void setStatusBarLabel(String message){
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        
        this.jLabelFilePath.setText("[INFO] " + message + " at " + dateFormat.format(cal.getTime()) );
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
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
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
        jMenuItemOpen = new javax.swing.JMenuItem();
        jMenuItem10 = new javax.swing.JMenuItem();
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
        jLabelFilePath = new java.awt.Label();
        statusAnimationLabel = new javax.swing.JLabel();

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
                onCurveParameters(evt);
            }
        });

        jButtonEdit.setText(resourceMap.getString("jButtonEdit.text")); // NOI18N
        jButtonEdit.setName("jButtonEdit"); // NOI18N
        jButtonEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onEditScatteringCurve(evt);
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

        jButton4.setText(resourceMap.getString("jButton4.text")); // NOI18N
        jButton4.setName("jButton4"); // NOI18N
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAddScatteringCurve(evt);
            }
        });
        jButton4.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                addNewCalculation(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });

        jButton5.setText(resourceMap.getString("jButton5.text")); // NOI18N
        jButton5.setName("jButton5"); // NOI18N
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveCalculation(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .add(13, 13, 13)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 575, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButtonEdit, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                    .add(jButton4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                    .add(jButtonScatteringParameters, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                    .add(jButton5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE))
                .addContainerGap())
            .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel1Layout.createSequentialGroup()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jButtonEdit)
                        .add(1, 1, 1)
                        .add(jButtonScatteringParameters)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton4)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jButton5))
                    .add(jScrollPane1, 0, 0, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                onAddModel(evt);
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
                onEditModel(evt);
            }
        });

        jRemoveModel.setText(resourceMap.getString("jRemoveModel.text")); // NOI18N
        jRemoveModel.setName("jRemoveModel"); // NOI18N
        jRemoveModel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onRemoveModel(evt);
            }
        });

        jButton3.setText(resourceMap.getString("jButton3.text")); // NOI18N
        jButton3.setName("jButton3"); // NOI18N
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onWeightParameters(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 578, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jRemoveModel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .add(jButtonAddExperiment, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .add(jButton3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                    .add(jButton1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 190, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(17, 17, 17)
                        .add(jButton1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jButton3)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(jButtonAddExperiment)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jRemoveModel))
                    .add(jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 137, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(72, Short.MAX_VALUE))
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
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTablesubModel.setColumnSelectionAllowed(true);
        jTablesubModel.setName("jTablesubModel"); // NOI18N
        jTablesubModel.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTablesubModel.getTableHeader().setReorderingAllowed(false);
        jTablesubModel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablesubModelMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTablesubModel);
        jTablesubModel.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTablesubModel.getColumnModel().getColumn(0).setMinWidth(300);
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
                onEditModelParameter(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 578, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jButton2)
                    .add(jScrollPane3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 100, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(1154, 1154, 1154))
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        jMenuItemOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemOpen.setText(resourceMap.getString("jMenuItemOpen.text")); // NOI18N
        jMenuItemOpen.setName("jMenuItemOpen"); // NOI18N
        jMenuItemOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onOpen(evt);
            }
        });
        fileMenu.add(jMenuItemOpen);

        jMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem10.setText(resourceMap.getString("jMenuItem10.text")); // NOI18N
        jMenuItem10.setName("jMenuItem10"); // NOI18N
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onSave(evt);
            }
        });
        fileMenu.add(jMenuItem10);

        jMenuItem7.setText(resourceMap.getString("jMenuItem7.text")); // NOI18N
        jMenuItem7.setName("jMenuItem7"); // NOI18N
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onSaveAs(evt);
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
                onGeneral(evt);
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
                onRunExperiment(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem8.setText(resourceMap.getString("jMenuItem8.text")); // NOI18N
        jMenuItem8.setName("jMenuItem8"); // NOI18N
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onExploreResults(evt);
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
                onCompileFortranCode(evt);
            }
        });
        jMenu3.add(jMenuItem4);

        jMenuItem5.setText(resourceMap.getString("jMenuItem5.text")); // NOI18N
        jMenuItem5.setName("jMenuItem5"); // NOI18N
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAdvancedParamteres(evt);
            }
        });
        jMenu3.add(jMenuItem5);

        jMenuItem9.setText(resourceMap.getString("jMenuItem9.text")); // NOI18N
        jMenuItem9.setName("jMenuItem9"); // NOI18N
        jMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onAdvancedCommonParameters(evt);
            }
        });
        jMenu3.add(jMenuItem9);

        jMenuItem2.setText(resourceMap.getString("jMenuItem2.text")); // NOI18N
        jMenuItem2.setName("jMenuItem2"); // NOI18N
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                onOptions(evt);
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
        statusPanel.setRequestFocusEnabled(false);

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        jLabelFilePath.setName("filePath"); // NOI18N
        jLabelFilePath.setText(resourceMap.getString("filePath.text")); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        org.jdesktop.layout.GroupLayout statusPanelLayout = new org.jdesktop.layout.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 825, Short.MAX_VALUE)
            .add(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(statusMessageLabel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 805, Short.MAX_VALUE)
                .add(statusAnimationLabel)
                .addContainerGap())
            .add(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(jLabelFilePath, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 586, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(229, Short.MAX_VALUE))
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(statusPanelLayout.createSequentialGroup()
                .add(statusPanelSeparator, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(statusPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                        .add(statusMessageLabel)
                        .add(statusAnimationLabel))
                    .add(jLabelFilePath, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    /** RUN DIALOG **/
private void onRunExperiment(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRunExperiment
    RunJDialog runJDialog = new RunJDialog(null, true);
    runJDialog.setController(this.genfitController);
    runJDialog.setModal(false);
    runJDialog.setVisible(true);


}//GEN-LAST:event_onRunExperiment

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
        
        for (int i = 0; i < singleExperimentSection.getSingleExperimentCount(); i++) {
            SingleExperiment singleExperiment = singleExperimentSection.getExperiments().get(i);                        
            tableModel.addRow(new Object[]{singleExperiment.getModelsNumber(), singleExperiment.getExperimentalScatteringCurve(), singleExperiment.getDescription()});
        }        
        if (this.experimentModelSelected != -1){
            this.jTableExperiment.setRowSelectionInterval(this.experimentModelSelected, this.experimentModelSelected);
        }

    }

    
    
    /** OPEN FILE DIALOG **/
private void onOpen(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onOpen
    OpenJDialog openJDialog = new OpenJDialog(null, true);
    openJDialog.setGenfitFileController(this.genfitController);
    String sasFolder = null;
        try {
            sasFolder = GenfitPropertiesReader.getGenfitFolderAbsolutePath();
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
    
    this.setStatusBarLabel("File loaded: " + this.lastFile.getAbsolutePath());
    this.fillExperimentTable(this.genfitController);

    if (jTableExperiment.getRowCount() > 0){
        this.jTableExperiment.setRowSelectionInterval(0, 0);
        this.experimentModelSelected = 0;        
        this.fillExperimentsModel();
    }
}//GEN-LAST:event_onOpen

    /** Add new experiment **/
private void onAddModel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddModel
    this.modelListDialog = new ModelListJDialog(null, true);
    modelListDialog.setModels(this.genfitController.getModelList());
    modelListDialog.genfitEvent.addListener(this);
    modelListDialog.setVisible(true);

}//GEN-LAST:event_onAddModel

    /** Edit experiment **/
private void onEditScatteringCurve(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onEditScatteringCurve
    this.singleExperimentDialog = new SingleExperimentJDialog(null, true);
    this.singleExperimentDialog.genfitEvent.addListener(this);

   if (this.jTableExperiment.getSelectedRow() != -1){
        this.singleExperimentDialog.setSingleExperiment(this.genfitController.getSingleExperimentSection().getExperiments().get(this.jTableExperiment.getSelectedRow()));
        this.singleExperimentDialog.setTitle(this.genfitController.getSingleExperimentSection().getExperiments().get(this.jTableExperiment.getSelectedRow()).getDescription());
        this.singleExperimentDialog.setVisible(true);
   }
   else{
       GenfitLogger.warn("No experiment selected. Click on an experiment");
   }
    
    

}//GEN-LAST:event_onEditScatteringCurve

private void jTableExperimentPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTableExperimentPropertyChange
    //System.out.println("Property changed");
}//GEN-LAST:event_jTableExperimentPropertyChange

private void jTableExperimentMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTableExperimentMouseClicked
    this.experimentModelSelected = this.jTableExperiment.getSelectedRow();
   
    this.fillExperimentsModel();
    
    if (evt.getClickCount() == 2) {
     //this.jButtonEditActionPerformed(null);
     this.onEditScatteringCurve(null);
   }

}//GEN-LAST:event_jTableExperimentMouseClicked

private void removeExperimentModelRows(){
        DefaultTableModel tableModel = (DefaultTableModel) this.jTableModel.getModel();
        while (tableModel.getRowCount() != 0) {
            tableModel.removeRow(0);
        }
}


    private void fillExperimentsModel() {
        GenfitLogger.info("Model selected on UI: " + this.experimentModelSelected);
        if (this.experimentModelSelected != -1){
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
        //GenfitModel model = this.genfitController.getModelList().getModel(this.genfitController.getSingleExperimentSection().getExperiments().get(this.experimentModelSelected).getModelsNumber().get(this.experimentModelSelectedingle));
        GenfitModel model = this.genfitController.getModel(this.genfitController.getSingleExperimentSection().getExperiments().get(this.experimentModelSelected).getModelsNumber().get(this.experimentModelSelectedingle));
        this.removeParameterRows();
        DefaultTableModel tableSubModel = (DefaultTableModel) this.jTablesubModel.getModel();

        for (int i = 0; i < model.getSubmodelsText().size(); i++) {
            tableSubModel.addRow(new Object[]{model.getSubmodelsText().get(i).replace(".", "").replace(":", ""), model.getSubmodel(i).getStarting(), model.getSubmodel(i).getLower(), model.getSubmodel(i).getUpper(), model.getSubmodel(i).getFlag()});
        }
     }
    catch(Exception exp){
        GenfitLogger.error(exp.getMessage());
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

private void onGeneral(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onGeneral
    this.generalDialog = new GeneralJDialog(null, true);
    GeneralSection general = this.genfitController.getGeneralSection();
    generalDialog.genfitEvent.addListener(this);
    generalDialog.setGeneralSection(general);
    generalDialog.setVisible(true);

}//GEN-LAST:event_onGeneral

private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
   
 
   
    
    
}//GEN-LAST:event_jButton1MouseClicked

private void onWeightParameters(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onWeightParameters
    this.weightParameterDialog = new WeightParameterDialog(null, true);
    this.weightParameterDialog.setModel(this.genfitController.getSingleExperimentSection().getExperiments().get(this.experimentModelSelected).getModels().get(this.experimentModelSelectedingle));
    this.weightParameterDialog.genfitEvent.addListener(this);
    this.weightParameterDialog.setVisible(true);
}//GEN-LAST:event_onWeightParameters

private void onRemoveModel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveModel
    int selectedExperimentModel = this.jTableModel.getSelectedRow();
    this.genfitController.removeExperimentModel(this.jTableExperiment.getSelectedRow(), selectedExperimentModel);
    this.fillExperimentTable(genfitController);
    this.fillExperimentsModel();
    this.jTableExperiment.setRowSelectionInterval(0, 0);
    save();
}//GEN-LAST:event_onRemoveModel

private void onCurveParameters(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCurveParameters
    this.experimentModelSelected = this.jTableExperiment.getSelectedRow();
    /*this.scatteringParameterDialog = new SccateringParametersDialog(null, true);
    this.scatteringParameterDialog.genfitEvent.addListener(this);
    this.scatteringParameterDialog.setExperiment(this.genfitController.getSingleExperimentSection().getExperiments().get(this.experimentModelSelected));
    this.scatteringParameterDialog.setVisible(true);*/
    
    this.curveParametersDialog = new CurveParameterDialog(null, true);
    this.curveParametersDialog.genfitEvent.addListener(this);
    this.curveParametersDialog.setExperiment(this.genfitController.getSingleExperimentSection().getExperiments().get(this.experimentModelSelected));
    this.curveParametersDialog.setVisible(true);
}//GEN-LAST:event_onCurveParameters

    /** EDIT MODEL **/
    private void onEditModel(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onEditModel
       this.editModelDialog = new EditModelDialog(null, true);
       this.editModelDialog.setModel(this.genfitController.getModel(this.getModelSelected()));
       this.editModelDialog.genfitEvent.addListener(this);
       this.editModelDialog.setVisible(true);
        
    }//GEN-LAST:event_onEditModel

    /** SAVE **/
    private void onSaveAs(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onSaveAs
        SaveJDialog saveJDialog = new SaveJDialog(null, true);
        saveJDialog.setGenfitFileController(this.genfitController);
        saveJDialog.setVisible(true);  
    }//GEN-LAST:event_onSaveAs

    private void onEditModelParameter(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onEditModelParameter
        GenfitModel model = this.genfitController.getModel(this.genfitController.getSingleExperimentSection().getExperiments().get(this.experimentModelSelected).getModelsNumber().get(this.jTableModel.getSelectedRow()));
        Parameter submodel = model.getSubmodel(this.subModelSelected);    
        this.submodelDialog = new SubmodelDialog(null, true);
        this.submodelDialog.genfitEvent.addListener(this);
        this.submodelDialog.setSubmodel(submodel);
        this.submodelDialog.setVisible(true);
    }//GEN-LAST:event_onEditModelParameter

private void jMenu3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu3ActionPerformed
   
}//GEN-LAST:event_jMenu3ActionPerformed

private void onOptions(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onOptions
 this.settingsJDialog = new SettingsJDialog(null, true);
    this.settingsJDialog.setVisible(true);  
   
}//GEN-LAST:event_onOptions

private void onExploreResults(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onExploreResults
    ExploreResultsJDialog dialog = new ExploreResultsJDialog(null, true);
    dialog.setVisible(true);
}//GEN-LAST:event_onExploreResults

private void onAdvancedParamteres(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAdvancedParamteres
    EditFileJDialog editFileJDialog = new EditFileJDialog(null, true);
    
   
        try {
            String advancedFileName = GenfitPropertiesReader.readAdvancedSettingsfile();
            String sasPath = GenfitPropertiesReader.getGenfitFolderAbsolutePath();

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
}//GEN-LAST:event_onAdvancedParamteres

private void onCompileFortranCode(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onCompileFortranCode
            CompilerJDialog dialog = new CompilerJDialog(null, true);
            dialog.setVisible(true);  
      
}//GEN-LAST:event_onCompileFortranCode

private void onAdvancedCommonParameters(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAdvancedCommonParameters
        AdvancedPropertiesEditFileJDialog editFileJDialog = new AdvancedPropertiesEditFileJDialog(null, true, this.genfitController);       
        try {
            String advancedFileName = GenfitPropertiesReader.readAdvancedCommonsSettingsfile();
            String sasPath = GenfitPropertiesReader.getGenfitFolderAbsolutePath();
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
      
}//GEN-LAST:event_onAdvancedCommonParameters

private void onSave(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onSave
    if (this.lastFile != null ){
        try{
            String message = this.lastFile.getAbsolutePath() + " saved";
            GenfitLogger.info(message);
            this.setStatusBarLabel(message);
            this.genfitController.save(this.lastFile.getAbsolutePath());
        }
        catch(Exception exp){
            GenfitLogger.error(exp.getMessage());
        }
    }
}//GEN-LAST:event_onSave

private void addNewCalculation(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_addNewCalculation
    /*this.genfitController.addNewCalculation();
       this.fillExperimentTable(this.genfitController);
        this.fillExperimentsModel();
        this.fillParameters();*/
}//GEN-LAST:event_addNewCalculation

private void onAddScatteringCurve(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onAddScatteringCurve
        this.genfitController.addNewCalculation();
        this.fillExperimentTable(this.genfitController);
        this.fillExperimentsModel();
        this.fillParameters();
}//GEN-LAST:event_onAddScatteringCurve

private void onRemoveCalculation(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_onRemoveCalculation
        GenfitLogger.info("Remove calculation index: " + this.experimentModelSelected);
        if (this.genfitController.getSingleExperimentSection().getExperiments().size()> 1){
            if (this.experimentModelSelected != -1){
                this.genfitController.removeCalculation(this.experimentModelSelected);
                this.fillExperimentTable(this.genfitController);
                this.removeExperimentModelRows();
                this.removeParameterRows();
            }
            else{
                GenfitLogger.warn("Not in range: " + this.experimentModelSelected);
            }
        }
        else{
            GenfitLogger.warn("At least it should be one calculation");
        }
}//GEN-LAST:event_onRemoveCalculation
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButtonAddExperiment;
    private javax.swing.JButton jButtonEdit;
    private javax.swing.JButton jButtonScatteringParameters;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private java.awt.Label jLabelFilePath;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JMenuItem jMenuItemOpen;
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
        GenfitLogger.debug("EVENT: " + e.getType() );
       // System.out.println("A.- " + this.experimentModelSelected);
        switch (e.getType()) {
            case GENERAL_SAVE:
                genfitController.save(this.generalDialog.generalSection);
                return;               

            case SINGLE_EXPERIMENT_SAVE:
                SingleExperiment singleExperiment = this.singleExperimentDialog.singleExperiment;               
                genfitController.save(singleExperiment, this.jTableExperiment.getSelectedRow());
                this.fillExperimentTable(this.genfitController);                
                return;

            case WEIGHT_PARAMETERS_SAVE:
                ModelSingleExperiment model = this.weightParameterDialog.getModel();
                genfitController.save(this.jTableExperiment.getSelectedRow(), this.jTableModel.getSelectedRow(), model);
                this.weightParameterDialog.setVisible(false);
                
                break;
            case SCATTERING_PARAMETERS_SAVE:
                SingleExperiment singleExperiment2 = this.curveParametersDialog.getExperiment();
                genfitController.save(singleExperiment2, this.jTableExperiment.getSelectedRow());
                break;
                
            case ADD_NEW_EXPERIMENT_MODEL:
                int modelSelected = this.modelListDialog.getModelSelected();
                SingleExperiment single = this.genfitController.getSingleExperimentSection().getExperiments().get(this.jTableExperiment.getSelectedRow());                
                single.addNewModel(modelSelected);    
                genfitController.save(single, this.jTableExperiment.getSelectedRow());

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
                genfitController.save(path + "\\jgenfitdebug.txt");
            }
            
        } catch (Exception ex) {
            Logger.getLogger(JGenfitView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
