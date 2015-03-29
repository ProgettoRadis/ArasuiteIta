package tico.components;

import com.cloudgarden.speech.userinterface.SpeechEngineChooser;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import tico.board.TBoardConstants;
import tico.configuration.TLanguage;

/**
 *
 * @author Rodrigo
 */
public class TSynthesizerConfig extends JPanel {
    
    private SpeechEngineChooser chooser = null;
    private JTextField texto;
    private JTextField copiar;
    private JButton sapiBtn;
    private int mode;
    private JRadioButton googleRB;
    private JRadioButton sapiRB;

    /**
     * Creates a panel to configure the synthesizer
     * proyecto = true si se llama desde el pryecto
     */
    public TSynthesizerConfig(boolean proyecto) {
        this.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.WHITE, new Color(165, 163, 151)),
        TLanguage.getString("TSynthesizerConfig.TITLE")));
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        agreagarRadioButtons(); 
        if(!proyecto){
            agregarTextoYBoton(this);
        }
        botonConfigVoz();
    }
    
    public final void botonConfigVoz() {
        //agregar boton dialogo
        sapiBtn = new JButton(TLanguage.getString("TSynthesizerConfig.CONFIGURE_VOICE"));
        this.add(sapiBtn);
        sapiBtn.addActionListener(new mostrarDialogoVoz());
    }    
   
    public void setTexto(String txt) {
        texto.setText(txt);
    }
    
    public String getTexto() {
        if (texto.getText().equals("")) {
            return null;
        } else {
            return texto.getText();
        }
    }
    
    public String getNombreVoz() {
        try {
            String voice = chooser.getVoice().getName();
            return voice;
        } catch (Exception ex) {
            return "";
        }        
    }
    
    public int getMode(){
        return mode;
    }
    
    public void setMode(int m){        
        switch(m){
            case TBoardConstants.GOOGLE_MODE:
                sapiBtn.setVisible(false);
                googleRB.setSelected(true);
                break;
            case TBoardConstants.SAPI_MODE:
                sapiBtn.setVisible(true);
                sapiRB.setSelected(true);
                break;
        }
        mode = m;
    }
    
    
    
    private void agregarTextoYBoton(JPanel dest) {
        JPanel ct = new JPanel();
        texto = new JTextField(20);
        ct.add(texto);
        JButton copiarBt = new JButton(TLanguage.getString("TSynthesizerConfig.COPY_TEXT"));
        copiarBt.addActionListener(new copiar());
        ct.add(copiarBt);
        dest.add(ct);
    }
    
    private void agreagarRadioButtons(){
        JPanel radioPanel = new JPanel();
        
        googleRB = new JRadioButton(TLanguage.getString("TSynthesizerConfig.GOOGLE_VOICE"), true);
        googleRB.setActionCommand(String.valueOf(TBoardConstants.GOOGLE_MODE));
        
        sapiRB = new JRadioButton(TLanguage.getString("TSynthesizerConfig.SYSTEM_VOICE"), false);
        sapiRB.setActionCommand(String.valueOf(TBoardConstants.SAPI_MODE));
        
        googleRB.addActionListener(new elegirModo());
        sapiRB.addActionListener(new elegirModo());
        
        ButtonGroup bg = new ButtonGroup();
        bg.add(googleRB);
        bg.add(sapiRB);        
        
        radioPanel.add(googleRB);
        radioPanel.add(sapiRB);
        
        this.add(radioPanel);     
    }
    
    public void setCopyFrom(JTextField txt) {
        copiar = txt;
    }
    
    private class elegirModo implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            int ac = Integer.parseInt(e.getActionCommand());
            setMode(ac);
        }
        
    }
    
    private class mostrarDialogoVoz implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            //TODO: load the current voice in the dialog
            chooser = SpeechEngineChooser.getSynthesizerDialog();
            chooser.show();
        }
    }
    
    private class copiar implements ActionListener {
        
        @Override
        public void actionPerformed(ActionEvent e) {
            texto.setText(copiar.getText());
        }
    }
}
