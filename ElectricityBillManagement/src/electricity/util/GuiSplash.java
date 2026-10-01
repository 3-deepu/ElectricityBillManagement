package electricity.util;

import javax.swing.*;
import java.awt.*;

public final class GuiSplash {
    private GuiSplash() {}

    public static void showSplash() {
        final JWindow window = new JWindow();
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 35, 25, 35));
        panel.setBackground(new Color(20, 30, 48));

        JLabel title = new JLabel("ELECTRICITY BILL MANAGEMENT", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 22));

        JLabel status = new JLabel("Starting services...", SwingConstants.CENTER);
        status.setForeground(new Color(180, 220, 255));
        status.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JProgressBar bar = new JProgressBar(0, 100);
        bar.setStringPainted(true);

        panel.add(title, BorderLayout.NORTH);
        panel.add(status, BorderLayout.CENTER);
        panel.add(bar, BorderLayout.SOUTH);

        window.add(panel);
        window.setSize(520, 190);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation((screen.width - window.getWidth()) / 2,
                           (screen.height - window.getHeight()) / 2);
        window.setAlwaysOnTop(true);
        window.setVisible(true);

        for (int i = 0; i <= 100; i += 5) {
            final int value = i;
            SwingUtilities.invokeLater(() -> {
                bar.setValue(value);
                status.setText(value < 35 ? "Loading billing engine..."
                        : value < 70 ? "Loading customer and payment services..."
                        : "Preparing secure console...");
            });
            Animation.sleep(45);
        }

        window.dispose();
    }
}
