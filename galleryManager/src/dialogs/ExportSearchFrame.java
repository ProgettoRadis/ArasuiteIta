package dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import components.ImageTableModel;
import components.PictogramTableCellRenderer;

import configuration.TConfiguration;
import configuration.TLanguage;
import database.DB;
import database.DBTasks;

public class ExportSearchFrame extends JDialog {

    private JPanel contentPane;
    private JTextField term1TextField, term2TextField, term3TextField, imageNameTextField;
	private ImageTableModel imageTableModel;
    private String[] imageTableHeader;
    private JTable table;
    private JComboBox AndOr1ComboBox, AndOr2ComboBox;
    public final static int IS_INTEGRATED = 0;
    private int mode = -1;
    private String selectedImageName;
    private ExportSearchFrame mainFrame;
    private JProgressBar progressBar;
    private String queryLoaded;
	private JComboBox comboBox;
	private JLabel labelTotalImages;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ExportSearchFrame frame = new ExportSearchFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public ExportSearchFrame(int mode) {
        this.mode = mode;
        init();
    }

    public ExportSearchFrame() {
        init();
    }

    /**
     * Create the frame.
     */
    private void init() {
		TLanguage.initLanguage(TConfiguration.getLanguage());
		/*
		 * imageTableHeader = new String[] {
		 * TLanguage.getString("EXPORT_SEARCH_FRAME.TITLE"),
		 * TLanguage.getString("EXPORT_SEARCH_FRAME.FILENAME"),
		 * TLanguage.getString("EXPORT_SEARCH_FRAME.ASSOCIATED_TERMS") };
		 */
		imageTableHeader = new String[] { "", "", "", "", "", "" };
        this.mainFrame = this;
        setTitle(TLanguage.getString("EXPORT_SEARCH_FRAME.TITLE"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 720, 430);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(null, TLanguage.getString("EXPORT_SEARCH_FRAME.KEY_TERMS"),
                TitledBorder.LEADING,
                TitledBorder.TOP, null,
                null));
        contentPane.add(panel);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        term1TextField = new JTextField();
        panel.add(term1TextField);
        term1TextField.setColumns(10);

        AndOr1ComboBox = new JComboBox();
		AndOr1ComboBox.setModel(new DefaultComboBoxModel(new String[] {
				TLanguage.getString("EXPORT_SEARCH_FRAME.AND"),
                TLanguage.getString("EXPORT_SEARCH_FRAME.OR") }));
        panel.add(AndOr1ComboBox);

        term2TextField = new JTextField();
        panel.add(term2TextField);
        term2TextField.setColumns(10);

        AndOr2ComboBox = new JComboBox();
        AndOr2ComboBox.setModel(new DefaultComboBoxModel(new String[] { TLanguage.getString("EXPORT_SEARCH_FRAME.AND"),
                TLanguage.getString("EXPORT_SEARCH_FRAME.OR") }));
        panel.add(AndOr2ComboBox);

        term3TextField = new JTextField();
        term3TextField.setColumns(10);
        panel.add(term3TextField);

        JButton btnBuscar = new JButton(TLanguage.getString("EXPORT_SEARCH_FRAME.FIND"));
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (!term1TextField.getText().equals("")) {
					String query = "SELECT COUNT(*) AS row_count, GROUP_CONCAT(main.word) AS terms, main.nameNN AS name FROM main WHERE main.name IN (SELECT main.name FROM main WHERE word LIKE '"
							+ term1TextField.getText() + "'";

					// Adding language when is not all
					if (comboBox.getSelectedItem().toString()
							.compareTo("Todos") != 0) {
						query += " AND idL = (SELECT id FROM language WHERE name LIKE '"
								+ comboBox.getSelectedItem().toString() + "')";
					}

					query += ")";

                    // Second textField
                    if (!term2TextField.getText().equals("")) {
                        query += AndOr1ComboBox.getSelectedIndex() == 0 ? " AND " : " OR ";
						query += "main.name IN (SELECT main.name FROM main WHERE word LIKE '"
								+ term2TextField.getText() + "'";

						// Adding language when is not all
						if (comboBox.getSelectedItem().toString()
								.compareTo("Todos") != 0) {
							query += " AND idL = (SELECT id FROM language WHERE name LIKE '"
									+ comboBox.getSelectedItem().toString()
									+ "')";
						}

						query += ")";
                    }

                    if (!term3TextField.getText().equals("")) {
                        query += AndOr2ComboBox.getSelectedIndex() == 0 ? " AND " : " OR ";
						query += "main.name IN (SELECT main.name FROM main WHERE word LIKE '"
								+ term3TextField.getText() + "'";

						// Adding language when is not all
						if (comboBox.getSelectedItem().toString()
								.compareTo("Todos") != 0) {
							query += " AND idL = (SELECT id FROM language WHERE name LIKE '"
									+ comboBox.getSelectedItem().toString()
									+ "')";
						}

						query += ")";
                    }

                    query += " GROUP BY main.name";

                    queryLoaded = query;

                    try {
                        DB db = DB.getInstance();
                        ResultSet rs = db.query(query);
                        prepareTermsTable(rs);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            TLanguage.getString("EXPORT_SEARCH_FRAME.AT_LEAST_THREE_CHARACTERS_WARNING"),
                            TLanguage.getString("EXPORT_SEARCH_FRAME.AT_LEAST_THREE_CHARACTERS_WARNING_TITLE"),
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        panel.add(btnBuscar);

        JPanel panel_1 = new JPanel();
        panel_1.setBorder(new TitledBorder(null, TLanguage.getString("EXPORT_SEARCH_FRAME.BY_IMAGE_NAME"),
                TitledBorder.LEADING, TitledBorder.TOP,
                null, null));
        contentPane.add(panel_1);

        JLabel lblNombre = new JLabel(TLanguage.getString("EXPORT_SEARCH_FRAME.NAME"));
        panel_1.add(lblNombre);

        imageNameTextField = new JTextField();
        panel_1.add(imageNameTextField);
        imageNameTextField.setColumns(30);

        JButton btnBuscar_1 = new JButton(TLanguage.getString("EXPORT_SEARCH_FRAME.FIND"));
        btnBuscar_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (imageNameTextField.getText().length() >= 3) {
                    try {
                        DB db = DB.getInstance();
						String query = "SELECT COUNT(*) AS row_count, GROUP_CONCAT(main.word) AS terms, main.nameNN AS name FROM main WHERE main.name LIKE '"
								+ imageNameTextField.getText()
								+ "' GROUP BY nameNN";

                        queryLoaded = query;

                        ResultSet rs = db.query(query);
                        prepareTermsTable(rs);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            TLanguage.getString("EXPORT_SEARCH_FRAME.AT_LEAST_THREE_CHARACTERS_WARNING"),
                            TLanguage.getString("EXPORT_SEARCH_FRAME.AT_LEAST_THREE_CHARACTERS_WARNING_TITLE"),
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        panel_1.add(btnBuscar_1);

		JPanel panel_5 = new JPanel();
		contentPane.add(panel_5);

		JLabel lblIdiomaDeLa = new JLabel(
				TLanguage.getString("FIND_IMAGE_FRAME.LANGUAGE_OF_THE_SEARCH"));
		panel_5.add(lblIdiomaDeLa);

		comboBox = new JComboBox();

		// We insert all languages selected
		comboBox.addItem("Todos");
		comboBox.setSelectedIndex(0);

		// Filling languages
		String query = "SELECT name FROM language ORDER BY id asc";
		try {
			ResultSet rs = DB.getInstance().query(query);
			int pos = 1;
			while (rs.next()) {
				comboBox.addItem(rs.getString("name"));
				if (rs.getString("name")
						.compareTo(TConfiguration.getLanguage()) == 0) {
					comboBox.setSelectedIndex(pos);
				}
				pos++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		panel_5.add(comboBox);

		JPanel panel_6 = new JPanel();
		contentPane.add(panel_6);

		JLabel lblNewLabel = new JLabel(
				TLanguage.getString("FIND_IMAGE_FRAME.TOTAL_IMAGES_FOUND"));
		panel_6.add(lblNewLabel);

		labelTotalImages = new JLabel("0");
		panel_6.add(labelTotalImages);

        JPanel panel_2 = new JPanel();
        panel_2.setLayout(new BorderLayout(0, 0));
        panel_2.setBorder(new TitledBorder(null, TLanguage.getString("EXPORT_SEARCH_FRAME.RESULTS"),
                TitledBorder.LEADING,
                TitledBorder.TOP, null, null));
        contentPane.add(panel_2);

		/*
		 * imageTableModel = new DefaultTableModel(new Object[][] {},
		 * imageTableHeader) { public java.lang.Class<?> getColumnClass(int
		 * columnIndex) { return getValueAt(0, columnIndex).getClass(); };
		 * 
		 * public boolean isCellEditable(int arg0, int arg1) { return false; };
		 * };
		 */

		imageTableModel = new ImageTableModel();
        table = new JTable(imageTableModel);
		table.setRowSelectionAllowed(false);
		table.setDefaultRenderer(String.class, new PictogramTableCellRenderer());
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
				int row = table.getSelectedRow();
				int column = table.getSelectedColumn();
				String imageNameNN = ((ImageTableModel) table.getModel())
						.getImageName(row, column);
                selectedImageName = imageNameNN;
				if (imageNameNN != null) {

					if (e.getClickCount() == 2) {
						if (mode == ExportSearchFrame.IS_INTEGRATED) {
							mainFrame.dispose();
						} else {
							AddImageFrame f = new AddImageFrame(imageNameNN);
							f.setVisible(true);
							f.pack();
						}
					}
                }
            }
        });

        // Setting with & height
		table.setRowHeight(100);

        table.setSize(new Dimension(500, 100));
        JScrollPane scrollPane = new JScrollPane(table);
        panel_2.add(scrollPane);

        JPanel panel_3 = new JPanel();
        contentPane.add(panel_3);

        if (mode == ExportSearchFrame.IS_INTEGRATED) {
            JButton btnInsertar = new JButton(TLanguage.getString("EXPORT_SEARCH_FRAME.INSERT"));
            panel_3.add(btnInsertar);

            btnInsertar.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    if (selectedImageName == null) {
                        JOptionPane.showMessageDialog(null,
                                TLanguage.getString("EXPORT_SEARCH_FRAME.AT_LEAST_THREE_CHARACTERS_WARNING"),
                                TLanguage.getString("EXPORT_SEARCH_FRAME.AT_LEAST_THREE_CHARACTERS_WARNING_TITLE"),
                                JOptionPane.WARNING_MESSAGE);
                    } else {
                        mainFrame.dispose();
                    }
                }
            });
        }

        JButton btnCancelar = new JButton(TLanguage.getString("EXPORT_SEARCH_FRAME.CANCEL"));
        btnCancelar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                mainFrame.dispose();
            }
        });

        JButton btnExportarBsqueda = new JButton(TLanguage.getString("EXPORT_SEARCH_FRAME.EXPORT_SEARCH"));
        panel_3.add(btnExportarBsqueda);
        panel_3.add(btnCancelar);

        JPanel panel_4 = new JPanel();
        contentPane.add(panel_4);

        btnExportarBsqueda.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (queryLoaded == null) {
                    JOptionPane.showMessageDialog(null,
                            TLanguage.getString("EXPORT_SEARCH_FRAME.YOU_SHOULD_SELECT_AN_IMAGE"),
                            TLanguage.getString("EXPORT_SEARCH_FRAME.AT_LEAST_THREE_CHARACTERS_WARNING_TITLE"),
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    final JFileChooser f = new JFileChooser();
                    f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    int returnVal = f.showOpenDialog(contentPane);
                    if (returnVal == JFileChooser.APPROVE_OPTION) {
                        new Thread(new Runnable() {

                            @Override
                            public void run() {
                                mainFrame.setEnabled(false);
                                DBTasks.exportDB(f.getSelectedFile().getAbsolutePath(), progressBar, queryLoaded);
                                JOptionPane.showMessageDialog(contentPane,
                                        TLanguage.getString("EXPORT_SEARCH_FRAME.IMPORT_OK"));
                                mainFrame.setEnabled(true);
								progressBar.setValue(0);
                            }
                        }).start();
                    }
                }
            }
        });

        progressBar = new JProgressBar();
        progressBar.setPreferredSize(new Dimension(680, 30));
        panel_4.add(progressBar);
    }

    private void prepareTermsTable(ResultSet rs) {
        try {
			imageTableModel.setDataVector(null, imageTableHeader);
			// Image resizedImage;
            String imagePath = DB.getInstance().getImagesPath();

			Vector<String> listNames = new Vector<String>();
			int totalImages = 0;
            while (rs.next()) {
				listNames
						.add(imagePath + File.separator + rs.getString("name"));
				totalImages++;
            }

			// Updating total images found
			this.labelTotalImages.setText(String.valueOf(totalImages));
			imageTableModel.setData(listNames);
			imageTableModel.fireTableDataChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getSelectedImageName() {
        return this.selectedImageName;
    }

}
