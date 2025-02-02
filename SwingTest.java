import javax.swing.*;

public class SwingTest {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Swing Test");
            JButton button = new JButton("Нажми меня");

            button.addActionListener(e -> JOptionPane.showMessageDialog(frame, "Привет, Swing!"));

            frame.add(button);
            frame.setSize(300, 200);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
