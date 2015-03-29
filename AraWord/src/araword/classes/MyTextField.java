// The textfield of each pictogram, with constructor and some default configuration.

package araword.classes;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;

import araword.G;

public class MyTextField extends JTextField {

	private static final long serialVersionUID = 1L;

	public MyTextField (String text) {
        super();
        this.setHorizontalAlignment(SwingConstants.CENTER);
        this.setText(text);
        this.setFont(G.font);
        this.setForeground(G.color);
        this.setHighlighter(null);
        // Depending on font configuration, borders of letters could be slightly truncated. Not solved yet.
        
        this.addKeyListener(G.keyListener);
        this.addFocusListener(G.focusListener);
        this.addMouseListener(G.mouseListener);
//        this.addMouseMotionListener(G.mouseMotionListener); // Commented for simplicity, may be further improved.
        
        this.setFocusTraversalKeysEnabled(false);
        this.setRequestFocusEnabled(true);
        this.setBorder(G.myTextFieldBorder);
        // This is to remove cut, copy and paste default functionality and override it with our implementation.
        JTextComponent.KeyBinding[] newBindings = {
		    new JTextComponent.KeyBinding(
		      KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK),
		      DefaultEditorKit.beepAction),
		    new JTextComponent.KeyBinding(
		      KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK),
		      DefaultEditorKit.beepAction),
		    new JTextComponent.KeyBinding(
		        KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK),
		        DefaultEditorKit.beepAction)
		  };              
		Keymap k = this.getKeymap();
		JTextComponent.loadKeymap(k, newBindings, this.getActions());
    }
}
