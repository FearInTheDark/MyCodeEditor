package CustomizeUI;

import javax.swing.plaf.basic.BasicTextAreaUI;
import java.awt.*;

public class MyTextAreaUI extends BasicTextAreaUI {
    @Override
    protected void paintBackground(Graphics g) {
        g.setColor(new Color(0x464646));
        g.fillRect(0, 0, getComponent().getWidth(), getComponent().getHeight());
    }

    @Override
    protected void paintSafely(Graphics g) {
        g.setColor(new Color(0xFFFFFF));
        super.paintSafely(g);
    }

    @Override
    protected void installDefaults() {
        super.installDefaults();
        super.getComponent().setFont(new Font("JetBrains Mono", Font.PLAIN, 20));
    }

//    @Override
//    protected void installListeners() {
//        super.installListeners();
//        super.getComponent().addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyTyped(KeyEvent e) {
//                if (e.getKeyChar() == ' ') {
//                    e.consume();
//                }
//            }
//        });
//    }


}
