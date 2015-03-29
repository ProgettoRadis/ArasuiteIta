package dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import components.Updater;

import configuration.TConfiguration;
import configuration.TLanguage;

public class UpdaterFrame extends JFrame {

	private JPanel contentPane;
	private JFrame mainFrame;
	private Updater u;
	private JProgressBar progressBar;
	private JButton btnCancel, btnUpdate;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UpdaterFrame frame = new UpdaterFrame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public UpdaterFrame() {
		try {
			u = new Updater();
			u.launchUpdate();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Date releaseDate;
			releaseDate = sdf.parse(u.getProperty(Updater.KEY_RELEASE_DATE));
			Date lastImportedDate = TConfiguration
					.getProperty(TConfiguration.KEY_DB_RELEASE_DATE) != null ? sdf
					.parse(TConfiguration
							.getProperty(TConfiguration.KEY_DB_RELEASE_DATE))
					: null;
			if (lastImportedDate == null || releaseDate.after(lastImportedDate)) {
				init();
				this.setVisible(true);
			} else {
				TLanguage.initLanguage(TConfiguration.getLanguage());
				JOptionPane
						.showMessageDialog(
								mainFrame,
								TLanguage
										.getString("UPDATER_FRAME.LAST_VERSION_ALREADY_INSTALLED"));
				this.dispose();
			}
		} catch (Exception e) {
			TLanguage.initLanguage(TConfiguration.getLanguage());
			JOptionPane.showMessageDialog(mainFrame,
					TLanguage.getString("UPDATER_FRAME.ERROR_MESSAGE"));
			this.dispose();
		}
	}

	private void init() {
		TLanguage.initLanguage(TConfiguration.getLanguage());
		mainFrame = this;

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		JPanel panel_1 = new JPanel();
		contentPane.add(panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JLabel lblThereIsA = new JLabel(
				TLanguage.getString("UPDATER_FRAME.INFORMATION"));
		panel_1.add(lblThereIsA);

		JPanel panel = new JPanel();
		contentPane.add(panel);
		panel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane);

		JTextArea changeLog = new JTextArea();
		scrollPane.setViewportView(changeLog);
		changeLog.setLineWrap(true);
		changeLog.setRows(6);
		String text = u.getProperty(Updater.KEY_CHANGELOG);
		changeLog.setText(text);
		changeLog.setEditable(false);
		changeLog.setColumns(34);

		JPanel panel_2 = new JPanel();
		contentPane.add(panel_2);

		this.btnCancel = new JButton(
				TLanguage.getString("UPDATER_FRAME.CANCEL"));
		this.btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainFrame.dispose();
			}
		});

		this.btnUpdate = new JButton(
				TLanguage.getString("UPDATER_FRAME.UPDATE"));
		this.btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mainFrame.setEnabled(false);
				Thread t = new Thread(new Runnable() {
					public void run() {
						doUpdate(progressBar);
					}
				});
				t.start();
			}
		});
		panel_2.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		panel_2.add(btnCancel);

		JLabel label = new JLabel("");
		panel_2.add(label);
		panel_2.add(btnUpdate);

		JPanel panel_3 = new JPanel();
		contentPane.add(panel_3);

		this.progressBar = new JProgressBar();
		this.progressBar.setStringPainted(true);
		progressBar.setPreferredSize(new Dimension(420, 30));
		panel_3.add(progressBar);
	}

	private void doUpdate(JProgressBar progressBar) {
		mainFrame.setEnabled(false);
		try {
			String pathToImport = u.performUpdate(progressBar);
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			Date releaseDate = sdf.parse(u
					.getProperty(Updater.KEY_RELEASE_DATE));
			ImportDatabaseFrame f = new ImportDatabaseFrame(pathToImport,
					releaseDate);
			f.setLocationRelativeTo(null);
			f.pack();
			f.setVisible(true);

			f.launchImportFromUpdate();

			f.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					mainFrame.dispose();
					u.removeTempFolder();
				}
			});
		} catch (Exception e) {
			JOptionPane.showMessageDialog(mainFrame,
					TLanguage.getString("UPDATER_FRAME.ERROR_MESSAGE"));
			this.dispose();
		}
		mainFrame.setEnabled(true);
	}

	@Override
	public void setEnabled(boolean b) {
		super.setEnabled(b);
		this.btnCancel.setEnabled(b);
		this.btnUpdate.setEnabled(b);
	}

}
