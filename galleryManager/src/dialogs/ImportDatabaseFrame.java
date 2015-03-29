package dialogs;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import configuration.TConfiguration;
import configuration.TLanguage;
import database.DBTasks;

public class ImportDatabaseFrame extends JFrame {

    private JPanel contentPane;
    private JTextField textFieldPath;
    private JProgressBar progressBarStatus;
    private JFrame mainFrame;
    private JCheckBox chckbxReemplazar;
	private JButton btnNewButton_1;
	private String pathToImport;
	private Date releaseDate;


    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    ImportDatabaseFrame frame = new ImportDatabaseFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

	public ImportDatabaseFrame(String pathToImport, Date releaseDate) {
		this.pathToImport = pathToImport;
		this.releaseDate = releaseDate;
		this.init();
	}

    /**
     * Create the frame.
     */
    public ImportDatabaseFrame() {
		this.init();
	}

	private void init() {
		TLanguage.initLanguage(TConfiguration.getLanguage());
        mainFrame = this;
        setTitle(TLanguage.getString("IMPORT_DATABASE_FRAME.TITLE"));
        setResizable(false);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 210);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null),
 TLanguage
                .getString("IMPORT_DATABASE_FRAME.SELECT_SOURCE_FILE"),
                TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        contentPane.add(panel);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JButton btnNewButton = new JButton(TLanguage.getString("IMPORT_DATABASE_FRAME.SELECT"));
        btnNewButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                JFileChooser f = new JFileChooser();
                f.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int returnVal = f.showOpenDialog(contentPane);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    textFieldPath.setText(f.getSelectedFile().getAbsolutePath());
                }
            }
        });
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
            }
        });
        panel.add(btnNewButton);

        textFieldPath = new JTextField();

		// We are importing a DB
		if (pathToImport != null) {
			textFieldPath.setText(pathToImport);
			btnNewButton.setEnabled(false);
		}

        textFieldPath.setEditable(false);
        panel.add(textFieldPath);
        textFieldPath.setColumns(24);

        JPanel panel_3 = new JPanel();
        contentPane.add(panel_3);
        panel_3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

        chckbxReemplazar = new JCheckBox(TLanguage.getString("IMPORT_DATABASE_FRAME.REPLACE"));
        panel_3.add(chckbxReemplazar);

		if (pathToImport != null) {
			chckbxReemplazar.setSelected(true);
			chckbxReemplazar.setEnabled(false);
		}

        JPanel panel_1 = new JPanel();
        contentPane.add(panel_1);

		btnNewButton_1 = new JButton(
				TLanguage.getString("IMPORT_DATABASE_FRAME.IMPORT"));

		if (pathToImport != null) {
			btnNewButton_1.setEnabled(false);
		}

        btnNewButton_1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                if (textFieldPath.getText().equals("")) {
                    JOptionPane.showMessageDialog(contentPane,
                            TLanguage.getString("IMPORT_DATABASE_FRAME.DIRECTORY_MUST_BE_SELECTED_WARNING"));
                } else {
					launchImport();
                }
            }
        });
        panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        panel_1.add(btnNewButton_1);

        JPanel panel_2 = new JPanel();
        contentPane.add(panel_2);

        progressBarStatus = new JProgressBar();
        progressBarStatus.setPreferredSize(new Dimension(420, 30));
        panel_2.add(progressBarStatus);
    }

	public void launchImportFromUpdate() {
		this.launchImport();
	}

	private void launchImport() {
		Thread t = new Thread(new Runnable() {
		    public void run() {
		        mainFrame.setEnabled(false);
		        try {
					DBTasks.importDB(textFieldPath.getText(), progressBarStatus, chckbxReemplazar.isSelected());
					JOptionPane
					.showMessageDialog(contentPane,
		                    TLanguage.getString("IMPORT_DATABASE_FRAME.IMPORT_OK"));
		            mainFrame.setEnabled(true);
					if (pathToImport != null) {
						// We are importing
						SimpleDateFormat ft = new SimpleDateFormat(
								"dd-MM-yyyy");
						TConfiguration.setProperty(
								TConfiguration.KEY_DB_RELEASE_DATE,
								ft.format(releaseDate));
					}
					mainFrame.dispose();
				} catch (Exception e) {								
					JOptionPane
					.showMessageDialog(contentPane,
		                    TLanguage.getString("IMPORT_DATABASE_FRAME.IMPORT_ERROR"));
					mainFrame.dispose();
				}
		    }
		});
		t.start();
	}
}
