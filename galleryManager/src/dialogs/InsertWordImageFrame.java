package dialogs;

import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import configuration.TConfiguration;
import configuration.TLanguage;
import database.DBTasks;

public class InsertWordImageFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnBuscar;
	private String imageName;
	private String newTextImage;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {

        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
					InsertWordImageFrame frame = new InsertWordImageFrame(
							"Castellano", new JLabel(), "");
					frame.setLocationRelativeTo(null);
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

    /**
     * Create the frame.
     */
	public InsertWordImageFrame(String language, JLabel image, String textImage) {
		TConfiguration.setLanguage(language);
		TLanguage.initLanguage(TConfiguration.getLanguage());
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 150, 150);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, TLanguage
				.getString("INSERT_WORD_IMAGE_FRAME.KEY_TERMS"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		JLabel text = new JLabel();
		this.newTextImage = textImage;

		text.setText("Sarà  inserito il termine \"" + newTextImage
				+ "\" all'immagine ");
		panel.add(text);
		
		panel.add(image);

		btnBuscar = new JButton(TLanguage.getString("INSERT_WORD_IMAGE_FRAME.SAVE"));
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (!newTextImage.equals("")) {
                	if (!imageName.equals("")) {
						DBTasks.addImageDB(imageName, TConfiguration.getLanguage(),"nombreComun", newTextImage);
						JOptionPane.showMessageDialog(null,
	                            TLanguage.getString("ADD_IMAGE_FRAME.CHANGES_DONE_WARNING"),
	                            TLanguage.getString("ADD_IMAGE_FRAME.CHANGES_DONE_WARNING_TITLE"),
	                            JOptionPane.INFORMATION_MESSAGE);
						dispose();
                	}
                	else {
                        JOptionPane.showMessageDialog(null,
    							TLanguage
    									.getString("INSERT_WORD_IMAGE_FRAME.IMG_EMPTY_WARNING"),
    							TLanguage
    									.getString("INSERT_WORD_IMAGE_FRAME.TERM_EMPTY_WARNING_TITLE"),
                                JOptionPane.WARNING_MESSAGE);
                        dispose();
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
							TLanguage
									.getString("INSERT_WORD_IMAGE_FRAME.TERM_EMPTY_WARNING"),
							TLanguage
									.getString("INSERT_WORD_IMAGE_FRAME.TERM_EMPTY_WARNING_TITLE"),
                            JOptionPane.WARNING_MESSAGE);
                    dispose();
                }
            }
        });
        panel.add(btnBuscar);

		contentPane.add(panel);
    }

}
