import javax.swing.*;
import java.util.Scanner;
import java.awt.event.*;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.File;
import org.jnativehook.GlobalScreen;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.keyboard.*;


public class Accents extends JFrame{
    
    static Robot r;  
    private Letter[] letters;
    
    String[][] keybinds;
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (UnsupportedLookAndFeelException|ClassNotFoundException|InstantiationException|IllegalAccessException e) {}

        Accents accents = new Accents();
        
        try{
            accents.Start();
        }catch(Exception e ){}
        
    }

    void Start() throws Exception {
        Scanner cfg = new Scanner(new File("keybinds.cfg"));
        int length = cfg.nextInt();
        
        keybinds = new String[length][2];
        
        for(int i = 0; i < length; i++){
            keybinds[i][0] = cfg.next();
            keybinds[i][1] = cfg.next();
        }        
        
        letters = new Letter[]{
            new Letter("á", new int[]{0, 2, 2, 5}, new int[]{0, 1, 9, 3}),
            new Letter("é", new int[]{0, 2, 3, 3}, new int[]{0, 2, 0, 1}),
            new Letter("í", new int[]{0, 2, 3, 7}, new int[]{0, 2, 0, 5}),
            new Letter("ó", new int[]{0, 2, 4, 3}, new int[]{0, 2, 1, 1}),
            new Letter("ú", new int[]{0, 2, 5, 0}, new int[]{0, 2, 1, 8}),
            new Letter("ñ", new int[]{0, 2, 4, 1}, new int[]{0, 2, 0, 9})
        };
        
        setAlwaysOnTop(true);
        setLocationByPlatform(true);
        setTitle("Spanish Accents");
        setVisible(true);
        
        setSize(375, 68);
        
        setResizable(false);
        setFocusableWindowState(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
        
        JPanel panel = new JPanel();
        panel.setVisible(true); 
        add(panel);
        
        
        for (int i = 0; i < letters.length; i++) {
            JButton j = newButton(letters[i].text);
            j.addActionListener(new ListenerOverride(letters[i]));
            panel.add(j);
        }
        
        panel.add(new JLabel("Shift: Uppercase"));
        
        GlobalScreen.addNativeKeyListener(new NativeKeyListener(){
            @Override  
            public void nativeKeyPressed(NativeKeyEvent e) {
                int key = e.getKeyCode();
                System.out.println("recieved");
                for(int i = 0; i < keybinds.length; i++){
                    try{
                        if(key == NativeKeyEvent.class.getField("VC_" + keybinds[i][0].toUpperCase()).getInt(null)){
                            for(Letter let : letters)
                                if(keybinds[i][1].equals(let.text)){
                                    System.out.println("sent");
                                    sendKey(let.lowerKey, false);
                                }
                        }
                    }catch(NoSuchFieldException|IllegalAccessException asd) {System.out.println("caught");}
                }
            }
             @Override
            public void nativeKeyReleased(NativeKeyEvent e) {}
             @Override  
            public void nativeKeyTyped(NativeKeyEvent e) {}
        }); 
        
    }
 
    public static void sendKey(int[] keys, boolean isUpper) {
        try {
            if (r == null) 
                r = new Robot();
            
            r.keyRelease(KeyEvent.VK_SHIFT);
            
            r.keyPress(KeyEvent.VK_ALT);

            for (int key : keys) {
                key = KeyEvent.class.getField("VK_NUMPAD" + key).getInt(null);
                r.keyPress(key);
                r.keyRelease(key);
                System.out.println(key);
            }

            r.keyRelease(KeyEvent.VK_ALT);   
            
            if(isUpper)
                r.keyPress(KeyEvent.VK_SHIFT);
            
        } catch (AWTException|NoSuchFieldException|IllegalAccessException e) {System.out.println("caught");}
    }

    private JButton newButton(String text) {
        JButton a = new JButton();
        a.setSize(100, 100);
        a.setVisible(true);
        a.setText(text);
        return a;
    }
}
