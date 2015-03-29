package dialogs;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import configuration.TConfiguration;
import configuration.TLanguage;

public class mainFrame extends JFrame {

    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
					mainFrame frame = new mainFrame("Castellano");
					frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
	public mainFrame(String language) {
		TConfiguration.setLanguage(language);
		TLanguage.initLanguage(TConfiguration.getLanguage());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 150, 150);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        JButton btnNewButton = new JButton(TLanguage.getString("MAIN_FRAME.ADD_IMAGE"));
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                AddImageFrame f = new AddImageFrame();
                f.setLocationRelativeTo(null);
                f.pack();
                f.setVisible(true);
            }
        });

        JButton btnNewButton_1 = new JButton(TLanguage.getString("MAIN_FRAME.EDIT_IMAGE"));
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                FindImageFrame f = new FindImageFrame();
                f.setLocationRelativeTo(null);
                f.pack();
                f.setVisible(true);
            }
        });

        JButton btnNewButton_2 = new JButton(TLanguage.getString("MAIN_FRAME.EXPORT_DB"));
        btnNewButton_2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                ExportSearchFrame f = new ExportSearchFrame();
                f.setLocationRelativeTo(null);
                f.pack();
                f.setVisible(true);
            }
        });

        JButton btnNewButton_3 = new JButton(TLanguage.getString("MAIN_FRAME.IMPORT_DB"));
        btnNewButton_3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                ImportDatabaseFrame f = new ImportDatabaseFrame();
                f.setLocationRelativeTo(null);
                f.pack();
                f.setVisible(true);
            }
        });
        contentPane.setLayout(new GridLayout(0, 1, 0, 0));
        contentPane.add(btnNewButton);
        contentPane.add(btnNewButton_1);
        contentPane.add(btnNewButton_2);
        contentPane.add(btnNewButton_3);

		JButton btnCheckForUpdates = new JButton(
				TLanguage.getString("MAIN_FRAME.CHECK_FOR_UPDATES"));
		btnCheckForUpdates.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				UpdaterFrame f = new UpdaterFrame();
				f.setLocationRelativeTo(null);
				f.pack();
			}
		});
		contentPane.add(btnCheckForUpdates);
    }

}
