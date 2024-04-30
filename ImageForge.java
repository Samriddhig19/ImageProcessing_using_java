import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ImageForge {

    private JLabel originalImageLabel;
    private JLabel processedImageLabel;
    private JButton chooseImageButton;
    private JButton grayscaleButton;
    private JButton redFilterButton;
    private JButton logButton;
    private JButton greenFilterButton;

    public ImageForge() {
        JFrame frame = new JFrame("Image Filter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        
        JPanel mainPanel = new JPanel(new BorderLayout());
        JPanel imagePanel = new JPanel(new GridLayout(1, 2, 50, 50));
        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));

        
        imagePanel.setBorder(new EmptyBorder(50, 50, 50, 50));

        
        originalImageLabel = new JLabel();
        processedImageLabel = new JLabel();
        imagePanel.add(originalImageLabel);
        imagePanel.add(processedImageLabel);

        chooseImageButton = new JButton("Choose Image");
        chooseImageButton.setFont(new Font("Arial", Font.BOLD, 14));
        chooseImageButton.setBackground(Color.WHITE);
        chooseImageButton.setForeground(Color.BLACK);

        grayscaleButton = new JButton("Greyscale");
        grayscaleButton.setFont(new Font("Arial", Font.BOLD, 14));
        grayscaleButton.setBackground(Color.GRAY);
        grayscaleButton.setForeground(Color.BLACK);

       	redFilterButton = new JButton("Red Filter");
        redFilterButton.setFont(new Font("Arial", Font.BOLD, 14));
        redFilterButton.setBackground(Color.RED);
        redFilterButton.setForeground(Color.BLACK);

        logButton = new JButton("Log");
        logButton.setFont(new Font("Arial", Font.BOLD, 14));
        logButton.setBackground(Color.LIGHT_GRAY);
        logButton.setForeground(Color.BLACK);

	greenFilterButton = new JButton("Green Filter");
    	greenFilterButton.setFont(new Font("Arial", Font.BOLD, 14));
    	greenFilterButton.setBackground(Color.GREEN);
    	greenFilterButton.setForeground(Color.BLACK);

       
        chooseImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png"));
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    try {
                        BufferedImage originalImage = ImageIO.read(selectedFile);
                        ImageIcon originalIcon = new ImageIcon(originalImage);
                        originalImageLabel.setIcon(originalIcon);
                        processedImageLabel.setIcon(originalIcon);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Error loading image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        grayscaleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GrayscaleWorker worker = new GrayscaleWorker();
                worker.execute();
            }
        });

        redFilterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RedFilterWorker worker = new RedFilterWorker();
                worker.execute();
            }
        });

        logButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LogTransformationWorker worker = new LogTransformationWorker();
                worker.execute();
            }
        });
	
	greenFilterButton.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            GreenFilterWorker worker = new GreenFilterWorker();
            worker.execute();
        }
    });

        
        buttonPanel.add(chooseImageButton);
        buttonPanel.add(grayscaleButton);
        buttonPanel.add(redFilterButton);
        buttonPanel.add(logButton);
	buttonPanel.add(greenFilterButton);

        mainPanel.add(imagePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        
        frame.add(mainPanel);

        
        frame.setVisible(true);
    }

    private class GrayscaleWorker extends SwingWorker<BufferedImage, Void> {

        @Override
        protected BufferedImage doInBackground() throws Exception {
            ImageIcon icon = (ImageIcon) processedImageLabel.getIcon();
            return convertToGrayscale(icon);
        }

        @Override
        protected void done() {
            try {
                ImageIcon processedIcon = new ImageIcon(get());
                processedImageLabel.setIcon(processedIcon);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error converting to grayscale: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class RedFilterWorker extends SwingWorker<BufferedImage, Void> {

        @Override
        protected BufferedImage doInBackground() throws Exception {
            ImageIcon icon = (ImageIcon) processedImageLabel.getIcon();
            return applyRedFilter(icon);
        }

        @Override
        protected void done() {
            try {
                ImageIcon processedIcon = new ImageIcon(get());
                processedImageLabel.setIcon(processedIcon);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error applying red filter: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private class LogTransformationWorker extends SwingWorker<BufferedImage, Void> {

        @Override
        protected BufferedImage doInBackground() throws Exception {
            ImageIcon icon = (ImageIcon) processedImageLabel.getIcon();
            return applyLogTransformation(icon);
        }

        @Override
        protected void done() {
            try {
                ImageIcon processedIcon = new ImageIcon(get());
                processedImageLabel.setIcon(processedIcon);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error applying log transformation: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

	private class GreenFilterWorker extends SwingWorker<BufferedImage, Void> {
    @Override
    protected BufferedImage doInBackground() throws Exception {
        ImageIcon icon = (ImageIcon) processedImageLabel.getIcon();
        return applyGreenFilter(icon);
    }

    @Override
    protected void done() {
        try {
            ImageIcon processedIcon = new ImageIcon(get());
            processedImageLabel.setIcon(processedIcon);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Error applying green filter: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}


    private BufferedImage convertToGrayscale(Icon icon) {
        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        ((ImageIcon) icon).paintIcon(null, g, 0, 0);
        g.dispose();

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color color = new Color(image.getRGB(x, y));
                int gray = (int) (color.getRed() * 0.299 + color.getGreen() * 0.587 + color.getBlue() * 0.114);
                image.setRGB(x, y, new Color(gray, gray, gray).getRGB());
            }
        }

        return image;
    }

    private BufferedImage applyRedFilter(Icon icon) {
        BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        ((ImageIcon) icon).paintIcon(null, g, 0, 0);
        g.dispose();

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color color = new Color(image.getRGB(x, y));
                int red = color.getRed();
                int green = color.getGreen();
                int blue = color.getBlue();
                // Apply red filter by increasing red channel intensity
                int newRed = Math.min(255, red + 50); // Increase red channel by 50
                image.setRGB(x, y, new Color(newRed, green, blue).getRGB());
            }
        }

        return image;
    }

private BufferedImage applyLogTransformation(Icon icon) {
    BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
    Graphics2D g = image.createGraphics();
    ((ImageIcon) icon).paintIcon(null, g, 0, 0);
    g.dispose();

    for (int x = 0; x < image.getWidth(); x++) {
        for (int y = 0; y < image.getHeight(); y++) {
            Color color = new Color(image.getRGB(x, y));
            double logRed = Math.log(1 + color.getRed()) / Math.log(1 + 255); // Adjust the scaling factor for red
            double logGreen = Math.log(1 + color.getGreen()) / Math.log(1 + 255); // Adjust the scaling factor for green
            double logBlue = Math.log(1 + color.getBlue()) / Math.log(1 + 255); // Adjust the scaling factor for blue

            int newRed = (int) (255 * logRed);
            int newGreen = (int) (255 * logGreen);
            int newBlue = (int) (255 * logBlue);

            image.setRGB(x, y, new Color(newRed, newGreen, newBlue).getRGB());
        }
    }

    return image;
}

private BufferedImage applyGreenFilter(Icon icon) {
    BufferedImage image = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
    Graphics2D g = image.createGraphics();
    ((ImageIcon) icon).paintIcon(null, g, 0, 0);
    g.dispose();

    for (int x = 0; x < image.getWidth(); x++) {
        for (int y = 0; y < image.getHeight(); y++) {
            Color color = new Color(image.getRGB(x, y));
            int red = color.getRed();
            int green = color.getGreen();
            int blue = color.getBlue();

            // Apply green filter by increasing green channel intensity
            int newGreen = Math.min(255, green + 50); // Increase green channel by 50

            image.setRGB(x, y, new Color(red, newGreen, blue).getRGB());
        }
    }

    return image;
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ImageForge::new);
    }
}
