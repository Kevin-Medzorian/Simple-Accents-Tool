import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ListenerOverride implements ActionListener{

    private final int[] upper;
    private final int[] lower;
    
    public ListenerOverride(Letter l) {
        upper = l.upperKey;
        lower = l.lowerKey;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        
        if ((e.getModifiers() & ActionEvent.SHIFT_MASK) == 1) {
            Accents.sendKey(upper, true);
        } else {
            Accents.sendKey(lower, false);
        }
    }

}
