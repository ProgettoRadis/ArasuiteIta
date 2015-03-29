package dialogs;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

import database.DB;

public class AddTermFrame extends JFrame {

    private Hashtable<String, String> termAdded = new Hashtable<String, String>();

    private JPanel contentPane;
    private JTextField termTextField;
    private JFrame mainFrame;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AddTermFrame frame = new AddTermFrame("");
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     * 
     * @param string
     */
    public AddTermFrame(final String imageName) {

        mainFrame = this;
        setTitle("A\u00F1adir un t\u00E9rmino para la im\u00E1gen " + imageName);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 350, 200);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

        JPanel panel = new JPanel();
        panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null), "Datos del t\u00E9rmino",
                TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panel.setLayout(new FormLayout(new ColumnSpec[] { FormFactory.RELATED_GAP_COLSPEC, FormFactory.DEFAULT_COLSPEC,
                FormFactory.RELATED_GAP_COLSPEC, ColumnSpec.decode("default:grow"), }, new RowSpec[] {
                FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC,
                FormFactory.DEFAULT_ROWSPEC, FormFactory.RELATED_GAP_ROWSPEC, FormFactory.DEFAULT_ROWSPEC, }));

        JLabel lblTrmino = new JLabel("T\u00E9rmino:");
        panel.add(lblTrmino, "2, 2, right, default");

        termTextField = new JTextField();
        termTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                try {
                    if (e.getKeyChar() != 8) {
                        DB db = DB.getInstance();
                        ResultSet rs = db.query("SELECT word FROM main WHERE name = '" + imageName
                                + "' AND word LIKE '" + termTextField.getText() + "%' LIMIT 0,1");
                        if (rs.next()) {
                            int oldSelection = termTextField.getText().length();
                            termTextField.setText(rs.getString("word"));
                            termTextField.select(oldSelection, termTextField.getText().length());
                        }
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        panel.add(termTextField, "4, 2, fill, default");
        termTextField.setColumns(10);

        JLabel lblIdioma = new JLabel("Idioma");
        panel.add(lblIdioma, "2, 4, right, default");

        final JComboBox languageComboBox = new JComboBox();
        panel.add(languageComboBox, "4, 4, fill, default");

        JLabel lblTipo = new JLabel("Tipo:");
        panel.add(lblTipo, "2, 6, right, default");

        final JComboBox typeComboBox = new JComboBox();
        panel.add(typeComboBox, "4, 6, fill, default");
        contentPane.add(panel);

        JPanel panel_1 = new JPanel();
        contentPane.add(panel_1);

        JButton btnNewButton = new JButton("Guardar");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                if (termTextField.getText().length() > 0) {
                    termAdded.put("term", termTextField.getText());
                    termAdded.put("language", (String) languageComboBox.getModel().getSelectedItem());
                    termAdded.put("type", (String) typeComboBox.getModel().getSelectedItem());
                    mainFrame.setVisible(false);
                    mainFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Debe introducir al menos un termino",
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        panel_1.add(btnNewButton);

        // Filling combobox with data from DB
        DB db;
        try {
            db = DB.getInstance();
            ResultSet rsL = db.query("SELECT name FROM language ORDER BY id");
            while (rsL.next()) {
                languageComboBox.addItem(rsL.getString("name"));
            }

            ResultSet rsT = db.query("SELECT name FROM type ORDER BY id");
            while (rsT.next()) {
                typeComboBox.addItem(rsT.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Hashtable<String, String> getTerm() {
        return termAdded;
    }
}
