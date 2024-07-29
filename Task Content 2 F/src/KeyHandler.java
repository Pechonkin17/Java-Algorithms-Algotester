import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
    DemoPanel demoPanel;
    public KeyHandler(DemoPanel demoPanel) {
        this.demoPanel = demoPanel;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {

    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        int code = keyEvent.getKeyCode();
        if (code == KeyEvent.VK_ENTER){
//            demoPanel.search();
            demoPanel.autoSearch();
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {

    }
}