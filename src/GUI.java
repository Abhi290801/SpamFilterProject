import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.Random;

public class GUI {
    private EmailClassifier classifier;
    private JFrame frame;
    private JTextField senderField;
    private JTextField subjectField;
    private JTextArea bodyArea;
    private JLabel resultLabel;
    private JProgressBar progressBar;
    private JPanel mainPanel;
    private JPanel resultPanel;
    private Color accentColor = new Color(41, 128, 185); // Nice blue color

    public GUI() {
        try {
            // Set look and feel to system default
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Initialize the classifier
        try {
            classifier = new EmailClassifier("data/training_data.csv");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, 
                "Failed to initialize classifier: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
        
        // Create the main frame
        frame = new JFrame("Spam Filter Pro");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 600);
        frame.setLocationRelativeTo(null);
        
        // Set custom icon if available
        try {
            frame.setIconImage(createMailIcon(32).getImage());
        } catch (Exception e) {
            // Fallback to default icon
        }
        
        // Create main panel with gradient background
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                Color color1 = new Color(240, 248, 255);
                Color color2 = new Color(220, 237, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, w, h, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setLayout(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Create header panel with logo and title
        JPanel headerPanel = createHeaderPanel();
        
        // Create form panel
        JPanel formPanel = createFormPanel();
        
        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        
        // Create result panel
        resultPanel = createResultPanel();
        
        // Add panels to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel and result panel to frame
        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(resultPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout(10, 10));
        headerPanel.setOpaque(false);
        
        // Create logo
        JLabel logoLabel = new JLabel(createMailIcon(48));
        logoLabel.setBorder(new EmptyBorder(0, 0, 0, 10));
        
        // Create title
        JLabel titleLabel = new JLabel("Spam Filter Pro");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(accentColor);
        
        // Create subtitle
        JLabel subtitleLabel = new JLabel("Advanced Email Classification System");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        subtitleLabel.setForeground(new Color(100, 100, 100));
        
        // Add components to header
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);
        
        headerPanel.add(logoLabel, BorderLayout.WEST);
        headerPanel.add(titlePanel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // Create styled labels
        JLabel senderLabel = createStyledLabel("From:");
        JLabel subjectLabel = createStyledLabel("Subject:");
        JLabel bodyLabel = createStyledLabel("Body:");
        
        // Sender field
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(senderLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        senderField = createStyledTextField();
        formPanel.add(senderField, gbc);
        
        // Subject field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0;
        formPanel.add(subjectLabel, gbc);
        
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        subjectField = createStyledTextField();
        formPanel.add(subjectField, gbc);
        
        // Email body
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0.0;
        formPanel.add(bodyLabel, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        bodyArea = new JTextArea(10, 20);
        bodyArea.setLineWrap(true);
        bodyArea.setWrapStyleWord(true);
        bodyArea.setFont(new Font("Arial", Font.PLAIN, 14));
        bodyArea.setForeground(Color.BLACK);
        bodyArea.setBackground(Color.WHITE);
        bodyArea.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        
        JScrollPane scrollPane = new JScrollPane(bodyArea);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 1),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)
        ));
        formPanel.add(scrollPane, gbc);
        
        return formPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton clearButton = new JButton("Clear");
        JButton classifyButton = new JButton("Classify Email");
        
        // Style buttons
        styleButton(clearButton, new Color(190, 190, 190), Color.WHITE);
        styleButton(classifyButton, accentColor, Color.WHITE);
        
        buttonPanel.add(clearButton);
        buttonPanel.add(classifyButton);
        
        // Add action listeners
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                senderField.setText("");
                subjectField.setText("");
                bodyArea.setText("");
                resultLabel.setText("Enter email details and click 'Classify Email'");
                resultLabel.setForeground(Color.DARK_GRAY);
                resultPanel.setBackground(new Color(240, 240, 240));
                progressBar.setIndeterminate(false);
                progressBar.setValue(0);
            }
        });
        
        classifyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String sender = senderField.getText().trim();
                String subject = subjectField.getText().trim();
                String body = bodyArea.getText().trim();
                
                if (sender.isEmpty() || subject.isEmpty() || body.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, 
                        "Please fill in all fields", 
                        "Missing Information", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Show progress
                progressBar.setIndeterminate(true);
                resultLabel.setText("Classifying...");
                resultPanel.setBackground(new Color(240, 240, 240));
                
                // Use SwingWorker to perform classification in background
                SwingWorker<Double, Void> worker = new SwingWorker<Double, Void>() {
                    @Override
                    protected Double doInBackground() throws Exception {
                        // Simulate some processing time for better UX
                        Thread.sleep(new Random().nextInt(500) + 500);
                        Email email = new Email(sender, subject, body);
                        return classifier.classifyEmail(email);
                    }
                    
                    @Override
                    protected void done() {
                        try {
                            double result = get();
                            if (result == 1.0) {
                                resultLabel.setText("SPAM DETECTED");
                                resultLabel.setForeground(new Color(180, 0, 0));
                                resultPanel.setBackground(new Color(255, 235, 235));
                                progressBar.setIndeterminate(false);
                                progressBar.setValue(100);
                            } else {
                                resultLabel.setText("NOT SPAM (HAM)");
                                resultLabel.setForeground(new Color(0, 100, 0));
                                resultPanel.setBackground(new Color(235, 255, 235));
                                progressBar.setIndeterminate(false);
                                progressBar.setValue(100);
                            }
                        } catch (Exception ex) {
                            resultLabel.setText("Error: " + ex.getMessage());
                            resultLabel.setForeground(Color.RED);
                            progressBar.setIndeterminate(false);
                        }
                    }
                };
                
                worker.execute();
            }
        });
        
        return buttonPanel;
    }
    
    private JPanel createResultPanel() {
        JPanel resultPanel = new JPanel(new BorderLayout(10, 10));
        resultPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        resultPanel.setBackground(new Color(240, 240, 240));
        
        resultLabel = new JLabel("Enter email details and click 'Classify Email'");
        resultLabel.setHorizontalAlignment(JLabel.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 18));
        resultLabel.setForeground(Color.DARK_GRAY);
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(100, 5));
        progressBar.setBorderPainted(false);
        progressBar.setForeground(accentColor);
        
        resultPanel.add(resultLabel, BorderLayout.CENTER);
        resultPanel.add(progressBar, BorderLayout.SOUTH);
        
        return resultPanel;
    }
    
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(80, 80, 80));
        return label;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setForeground(Color.BLACK);
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 1),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        return field;
    }
    
    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });
    }
    
    private ImageIcon createMailIcon(int size) {
        // Create a simple mail icon
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        
        // Enable anti-aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw envelope body
        g2d.setColor(accentColor);
        g2d.fillRoundRect(0, size/4, size, size*3/4, 8, 8);
        
        // Draw envelope flap
        int[] xPoints = {0, size/2, size};
        int[] yPoints = {size/4, 0, size/4};
        g2d.fillPolygon(xPoints, yPoints, 3);
        
        // Draw envelope line
        g2d.setColor(new Color(255, 255, 255, 100));
        g2d.setStroke(new BasicStroke(2));
        g2d.drawLine(0, size/4, size/2, size/2);
        g2d.drawLine(size/2, size/2, size, size/4);
        
        g2d.dispose();
        return new ImageIcon(image);
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI().show();
            }
        });
    }
}