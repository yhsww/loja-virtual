package kyj.poo;

import javax.swing.SwingUtilities;
import kyj.poo.ui.Aplicacao;
import kyj.poo.ui.Tema;

public class Main {
    public static void main(String[] args) {
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");
        Tema.aplicar();
        SwingUtilities.invokeLater(() -> new Aplicacao().setVisible(true));
    }
}