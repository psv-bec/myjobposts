import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

@SuppressWarnings("serial")
public class HTMLFetcher extends JFrame {

    private JTextField urlField;
    private JTextArea htmlArea;
    private JButton fetchButton;

    public HTMLFetcher() {
        setTitle("LinkedIn Post Fetcher");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        urlField = new JTextField(50);
        fetchButton = new JButton("Fetch Post");
        htmlArea = new JTextArea();
        htmlArea.setFont(new Font("Serif", Font.PLAIN, 14));
        htmlArea.setLineWrap(true);
        htmlArea.setWrapStyleWord(true);
        htmlArea.setBackground(new Color(230, 230, 250)); // Light lavender background

        fetchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchLinkedInPost();
            }
        });

        JPanel panel = new JPanel();
        panel.add(new JLabel("Enter LinkedIn Post URL:"));
        panel.add(urlField);
        panel.add(fetchButton);

        add(panel, BorderLayout.NORTH);
        add(new JScrollPane(htmlArea), BorderLayout.CENTER);
    }

    private void fetchLinkedInPost() {
        String urlString = urlField.getText();
        try {
            Document doc = Jsoup.connect(urlString).get();
            // Adjust the selector to match LinkedIn's HTML structure for a post
            Element postContent = doc.select("div[data-test-post-content]").first();

            if (postContent != null) {
                // Extract text and format HTML
                String postText = postContent.text();
                String modifiedHTML = modifyHTML(postText);
                htmlArea.setText(modifiedHTML);
            } else {
                htmlArea.setText("No post content found.");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to fetch LinkedIn post. Please check the URL.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private String modifyHTML(String content) {
        // Example: Add a style tag to change background color and modify the look and feel
        String style = "<style>" +
                "body { background-color: #f0f8ff; font-family: Arial, sans-serif; line-height: 1.6; }" +
                "p { margin: 10px 0; }" +
                "</style>";
        return "<html><head>" + style + "</head><body><p>" + content + "</p></body></html>";
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new HTMLFetcher().setVisible(true);
            }
        });
    }
}
