package dialogs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.DefaultCellEditor;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import components.ThumbnailPanel;

import configuration.TConfiguration;
import configuration.TLanguage;
import database.DB;
import database.DBTasks;
import database.ImageManager;

public class AddImageFrame extends JDialog {

    private JPanel contentPane;
    private JTextField imageTextField;
    private JPanel panel_1;
    private DefaultTableModel termTableModel;
    private JButton addRowTable;
    private JButton removeRowTable;
    private JTable termTable;
    private boolean replaceImage = false;
    private AddImageFrame mainFrame;
    private String nameNN = null;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AddImageFrame frame = new AddImageFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public AddImageFrame() {
        init();
    }

    public AddImageFrame(String nameNN) {
        this.nameNN = nameNN;
        init();
    }

    /**
     * Create the frame.
     */
    public void init() {
		TLanguage.initLanguage(TConfiguration.getLanguage());
        mainFrame = this;
        if (nameNN == null)
            setTitle(TLanguage.getString("ADD_IMAGE_FRAME.TITLE"));
        else
            setTitle(TLanguage.getString("ADD_IMAGE_FRAME.EDIT_IMAGE_TITLE") + " " + nameNN);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 450, 300);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, TLanguage.getString("ADD_IMAGE_FRAME.IMAGE"), TitledBorder.LEADING,
                TitledBorder.TOP, null,
                null));
        contentPane.add(panel);

        imageTextField = new JTextField();
        imageTextField.setEditable(false);
        imageTextField.setColumns(24);

        JButton btnSeleccionar = new JButton(TLanguage.getString("ADD_IMAGE_FRAME.SELECT"));
        if (nameNN != null)
            btnSeleccionar.setEnabled(false);

        btnSeleccionar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser chooser = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"Images", "jpg", "gif", "png");
                chooser.setFileFilter(filter);
                ThumbnailPanel preview = new ThumbnailPanel(chooser);
                chooser.addPropertyChangeListener(preview);
                chooser.setAccessory(preview);
                int returnVal = chooser.showOpenDialog(contentPane);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    imageTextField.setText(chooser.getSelectedFile().getAbsolutePath());

                    // Finding terms associated
                    DB db;
                    try {
                        db = DB.getInstance();
                        PreparedStatement s = db
                                .prepareStatement("SELECT word, language.name AS language, type.name AS type FROM main, language, type WHERE main.name = ? AND main.idL = language.id AND main.idT = type.id");

                        s.setString(1, (new File(chooser.getSelectedFile().getAbsolutePath())).getName());
                        ResultSet rs = s.executeQuery();

                        // The image exists
                        if (rs.isBeforeFirst()) {
                            int i = JOptionPane.showConfirmDialog(null,
                                    TLanguage.getString("ADD_IMAGE_FRAME.REPLACE_IMAGE_WARNING"),
                                    TLanguage.getString("ADD_IMAGE_FRAME.REPLACE_IMAGE_WARNING_TITLE"),
                                    JOptionPane.YES_NO_OPTION);
                            replaceImage = i == 0;
                        }

                        // Obtaining terms
                        while (rs.next() && replaceImage) {
                            Object[] row = { rs.getString("word"), rs.getString("language"), rs.getString("type") };
                            termTableModel.addRow(row);
                        }
                        removeRowTable.setEnabled(true);
                        addRowTable.setEnabled(true);
                        db.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        GroupLayout gl_panel = new GroupLayout(panel);
        gl_panel.setHorizontalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(
                gl_panel.createSequentialGroup()
                        .addGap(3)
                        .addComponent(imageTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
                                GroupLayout.PREFERRED_SIZE).addGap(5).addComponent(btnSeleccionar)));
        gl_panel.setVerticalGroup(gl_panel.createParallelGroup(Alignment.LEADING).addGroup(
                gl_panel.createSequentialGroup()
                        .addGap(5)
                        .addGroup(
                                gl_panel.createParallelGroup(Alignment.LEADING)
                                        .addComponent(imageTextField, GroupLayout.PREFERRED_SIZE,
                                                GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnSeleccionar))));
        panel.setLayout(gl_panel);

        panel_1 = new JPanel();
        panel_1.setBorder(new TitledBorder(null, TLanguage.getString("ADD_IMAGE_FRAME.ASSOCIATED_TERMS"),
                TitledBorder.LEADING, TitledBorder.TOP,
                null, null));
        contentPane.add(panel_1);

        Object[] columnNames = { TLanguage.getString("ADD_IMAGE_FRAME.TERM"),
                TLanguage.getString("ADD_IMAGE_FRAME.LANGUAGE"), TLanguage.getString("ADD_IMAGE_FRAME.TYPE") };
        termTableModel = new DefaultTableModel(null, columnNames);
        panel_1.setLayout(new BorderLayout(0, 0));
        termTable = new JTable(termTableModel);

        // Setting language combobox in the table
        TableColumn languageColumn = termTable.getColumnModel().getColumn(1);
        JComboBox comboBox = new JComboBox();
        try {
            DB db = DB.getInstance();
            ResultSet rs = db.query("SELECT name FROM language ORDER BY id ASC");
            while (rs.next()) {
                comboBox.addItem(rs.getString("name"));
            }
            db.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        languageColumn.setCellEditor(new DefaultCellEditor(comboBox));

        // Setting type combobox in the table
        TableColumn typeColumn = termTable.getColumnModel().getColumn(2);
        JComboBox comboBox1 = new JComboBox();
        try {
            DB db = DB.getInstance();
            ResultSet rs = db.query("SELECT name FROM type ORDER BY id ASC");
            while (rs.next()) {
                comboBox1.addItem(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        typeColumn.setCellEditor(new DefaultCellEditor(comboBox1));

        termTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        termTable.setBackground(Color.WHITE);
        termTable.setGridColor(Color.GRAY);
        termTable.setSize(new Dimension(100, 200));
        JScrollPane scrollPane = new JScrollPane(termTable);
        panel_1.add(scrollPane);

        JPanel panel_2 = new JPanel();
        panel_1.add(panel_2, BorderLayout.SOUTH);

		removeRowTable = new JButton(
				TLanguage.getString("ADD_IMAGE_FRAME.REMOVE_IMAGE_BUTTON"));
		removeRowTable.setToolTipText(TLanguage
				.getString("ADD_IMAGE_FRAME.REMOVE_ROW_TOOLTIP"));
        removeRowTable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                termTableModel.removeRow(termTable.getSelectedRow());
            }
        });
        removeRowTable.setEnabled(false);
		addRowTable = new JButton(
				TLanguage.getString("ADD_IMAGE_FRAME.ADD_IMAGE_BUTTON"));
        addRowTable.setEnabled(false);
		addRowTable.setToolTipText(TLanguage
				.getString("ADD_IMAGE_FRAME.ADD_ROW_TOOLTIP"));
        addRowTable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (nameNN == null && imageTextField.getText().equals("")) {
                    JOptionPane.showMessageDialog(null,
                            TLanguage.getString("ADD_IMAGE_FRAME.IMAGE_NOT_SELECTED_WARNING"),
                            TLanguage.getString("ADD_IMAGE_FRAME.IMAGE_NOT_SELECTED_WARNING_TITLE"),
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    termTableModel.addRow(new Object[] {});
                }
            }
        });
        FlowLayout fl_panel_2 = new FlowLayout(FlowLayout.RIGHT, 5, 5);
        panel_2.setLayout(fl_panel_2);
        panel_2.add(addRowTable);

        panel_2.add(removeRowTable);

        JPanel panel_3 = new JPanel();
        contentPane.add(panel_3);

        JButton btnNewButton = new JButton(TLanguage.getString("ADD_IMAGE_FRAME.SAVE_CHANGES"));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                try {

                    // Checking term table is filled correctly
                    Vector termData = termTableModel.getDataVector();
                    if (termData.size() > 0) {
                        Iterator i = termData.iterator();
                        boolean incorrectTable = false;
                        while (i.hasNext() && !incorrectTable) {
                            Vector term = (Vector) i.next();
                            if (term.get(0).equals("") || term.get(1).equals("") || term.get(2).equals("")) {
                                incorrectTable = true;
                            }
                        }
    
                        if (incorrectTable) {
                            JOptionPane.showMessageDialog(null,
                                    TLanguage.getString("ADD_IMAGE_FRAME.ALL_TERMS_ASSIGNED_WARNING"),
                                    TLanguage.getString("ADD_IMAGE_FRAME.ALL_TERMS_ASSIGNED_WARNING_TITLE"),
                                    JOptionPane.WARNING_MESSAGE);
                        } else {
                            if (nameNN == null) {
                                String imageFileName;
                                if (replaceImage) {
                                    imageFileName = imageTextField.getText().substring(
                                            imageTextField.getText().lastIndexOf(File.separator) + 1);
                                    ImageManager.delete(imageFileName);
                                    ImageManager.add(imageTextField.getText());
                                } else {
                                    imageFileName = ImageManager.add(imageTextField.getText());
                                }
    
                                saveChanges(imageFileName);
                            } else { // Editing image
                                saveChanges(nameNN);
                            }
    
                            JOptionPane.showMessageDialog(null,
                                    TLanguage.getString("ADD_IMAGE_FRAME.CHANGES_DONE_WARNING"),
                                    TLanguage.getString("ADD_IMAGE_FRAME.CHANGES_DONE_WARNING_TITLE"),
                                    JOptionPane.INFORMATION_MESSAGE);
    
                            mainFrame.dispose();
    
                        }
                    } else {
                        JOptionPane.showMessageDialog(null,
                                TLanguage.getString("ADD_IMAGE_FRAME.IMAGE_NOT_SELECTED_WARNING"),
                                TLanguage.getString("ADD_IMAGE_FRAME.IMAGE_NOT_SELECTED_WARNING_TITLE"),
                                JOptionPane.WARNING_MESSAGE);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
        panel_3.add(btnNewButton);

        JButton btnCancelar = new JButton(TLanguage.getString("ADD_IMAGE_FRAME.CANCEL_CHANGES"));
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                mainFrame.dispose();
            }
        });
        panel_3.add(btnCancelar);

		if (this.nameNN != null) {
			JButton btnEliminar = new JButton(
					TLanguage.getString("ADD_IMAGE_FRAME.REMOVE_IMAGE"));
			btnEliminar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int i = JOptionPane.showConfirmDialog(
							null,
							TLanguage
.getString("ADD_IMAGE_FRAME.REMOVE_IMAGE"),
							TLanguage
									.getString("ADD_IMAGE_FRAME.IMAGE_NOT_SELECTED_WARNING_TITLE"),
							JOptionPane.YES_NO_OPTION);

					if (i == 0) {
						String query = "DELETE FROM main WHERE nameNN LIKE '"
								+ nameNN + "'";
						try {
							DB.getInstance().update(query);
							ImageManager.delete(nameNN);
						} catch (SQLException e) {
							e.printStackTrace();
						}

						dispose();
					}
				}
			});
			panel_3.add(btnEliminar);
		}

        // Filling terms when editing image
        if (nameNN != null) {
            DB db;
            try {
                db = DB.getInstance();
                PreparedStatement s = db
                        .prepareStatement("SELECT word, language.name AS language, type.name AS type FROM main, language, type WHERE main.name = ? AND main.idL = language.id AND main.idT = type.id");

                s.setString(1, nameNN);
                ResultSet rs = s.executeQuery();

                // Obtaining terms
                while (rs.next()) {
                    Object[] row = { rs.getString("word"), rs.getString("language"), rs.getString("type") };
                    termTableModel.addRow(row);
                }
                removeRowTable.setEnabled(true);
                addRowTable.setEnabled(true);
                db.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveChanges(String imageFileName) throws SQLException {
        // Deleting image from the DB
        String query = "DELETE FROM main WHERE name = '" + imageFileName + "'";
        DB db = DB.getInstance();
        db.update(query);
        db.close();

        Vector termData = termTableModel.getDataVector();
        termData = termTableModel.getDataVector();
        Iterator j = termData.iterator();
        while (j.hasNext()) {
            Vector term = (Vector) j.next();
            DBTasks.addImageDB(imageFileName, (String) term.get(1), (String) term.get(2), (String) term.get(0));
        }
    }
}
