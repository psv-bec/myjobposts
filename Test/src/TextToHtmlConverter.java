import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("serial")
public class TextToHtmlConverter extends JFrame {

    private JTextPane textPane;
    private JButton pasteButton;
    private JButton clearButton;
    private JButton copyButton;
    private JTextArea htmlOutputArea;
    private JLabel instructionLabel;

    public TextToHtmlConverter() {
        // Set up the frame
        setTitle("Text to HTML Converter");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Instruction label
        instructionLabel = new JLabel("Paste your text in the editor, style it, and convert it to HTML!");
        instructionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        instructionLabel.setForeground(new Color(0x3E4A89));
        instructionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        instructionLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Create a text pane for rich text formatting
        textPane = new JTextPane();
        textPane.setContentType("text/plain");
        textPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        textPane.setBackground(new Color(0xFFFFFF));
        textPane.setFont(new Font("Serif", Font.PLAIN, 16));

        JScrollPane textScrollPane = new JScrollPane(textPane);
        textScrollPane.setPreferredSize(new Dimension(380, 300));

        // Create a text area for HTML output
        htmlOutputArea = new JTextArea();
        htmlOutputArea.setEditable(false);
        htmlOutputArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        htmlOutputArea.setForeground(new Color(0x3E4A89));
        htmlOutputArea.setBackground(new Color(0xF4F4F9));
        htmlOutputArea.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));

        JScrollPane htmlScrollPane = new JScrollPane(htmlOutputArea);
        htmlScrollPane.setPreferredSize(new Dimension(380, 200));

        // Create a button to paste and convert to HTML
        pasteButton = new JButton("Paste and Convert to HTML");
        pasteButton.setBackground(new Color(0x3E4A89));
        pasteButton.setForeground(Color.WHITE);
        pasteButton.setFont(new Font("Arial", Font.BOLD, 14));

        pasteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pasteTextAndConvertToHtml();
            }
        });

        // Create a button to clear text
        clearButton = new JButton("Clear Text");
        clearButton.setBackground(new Color(0xFF6B6B));
        clearButton.setForeground(Color.WHITE);
        clearButton.setFont(new Font("Arial", Font.BOLD, 14));

        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearText();
            }
        });

        // Create a button to copy HTML code
        copyButton = new JButton("Copy HTML Code");
        copyButton.setBackground(new Color(0x4CAF50)); // Green color for the copy button
        copyButton.setForeground(Color.WHITE);
        copyButton.setFont(new Font("Arial", Font.BOLD, 14));

        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyHtmlToClipboard();
            }
        });

        // Layout for the components
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.add(instructionLabel, BorderLayout.NORTH);
        panel.add(textScrollPane, BorderLayout.WEST);
        panel.add(htmlScrollPane, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(pasteButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(copyButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    // Function to paste text and convert it to HTML
    private void pasteTextAndConvertToHtml() {
        try {
            // Get the clipboard content
            String clipboardText = (String) Toolkit.getDefaultToolkit()
                    .getSystemClipboard().getData(DataFlavor.stringFlavor);

            // Insert clipboard text into text pane
            textPane.setText(clipboardText);

            // Convert formatted text into HTML
            String htmlContent = convertToHtml(textPane.getStyledDocument());
            htmlOutputArea.setText(htmlContent);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error pasting text.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Function to clear both the text pane and HTML output area
    private void clearText() {
        textPane.setText("");
        htmlOutputArea.setText("");
    }

    // Method to convert styled text into HTML format with proper new lines and without hashtags
    private String convertToHtml(StyledDocument doc) throws BadLocationException {
        StringBuilder html = new StringBuilder("<html><body style=\"font-family:Arial; white-space:pre-wrap;\">\n");

        // Regex patterns for email, phone number, and URL detection
        Pattern emailPattern = Pattern.compile("\\b[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}\\b");
        Pattern phonePattern = Pattern.compile("\\b\\d{10}\\b|\\+?\\d{1,4}?[ -]?\\(?\\d{1,4}?\\)?[ -]?\\d{1,4}[ -]?\\d{1,9}\\b");
        Pattern urlPattern = Pattern.compile("\\bhttps?://[\\w.-/]+\\b");

        String text = doc.getText(0, doc.getLength());
        String[] lines = text.split("\n");

        boolean previousLineEmpty = false;

        for (String line : lines) {
            if (line.isEmpty()) {
                if (previousLineEmpty) {
                    html.append("<br/><br/>\n");
                    previousLineEmpty = false;
                } else {
                    previousLineEmpty = true;
                }
            } else {
                previousLineEmpty = false;
                // Remove "hashtag" from the text
                String processedLine = line.replaceAll("(?i)hashtag", "").trim();
                // Preserve lines starting with "#" as they are
                if (processedLine.startsWith("#")) {
                    html.append(processedLine).append("<br/>\n");
                } else {
                    // Process text normally if not starting with "#"
                    String htmlSegment = formatSpecialText(processedLine, emailPattern, phonePattern, urlPattern);
                    html.append(htmlSegment).append("<br/>\n");
                }
            }
        }

        // Placeholder for LinkedIn post link at the end
        html.append("<p><strong>LinkedIn Post Link:</strong> [Insert your LinkedIn post link here]</p>\n");

        html.append("</body></html>\n");
        return html.toString();
    }

    // Method to format email addresses, phone numbers, and URLs with bold, underline, and color
    private String formatSpecialText(String text, Pattern emailPattern, Pattern phonePattern, Pattern urlPattern) {
        StringBuilder result = new StringBuilder();

        int lastIndex = 0;
        Matcher emailMatcher = emailPattern.matcher(text);
        Matcher phoneMatcher = phonePattern.matcher(text);
        Matcher urlMatcher = urlPattern.matcher(text);

        while (emailMatcher.find() || phoneMatcher.find() || urlMatcher.find()) {
            // Reset the starting positions to find the first match among all matchers
            int emailStart = emailMatcher.find(lastIndex) ? emailMatcher.start() : Integer.MAX_VALUE;
            int phoneStart = phoneMatcher.find(lastIndex) ? phoneMatcher.start() : Integer.MAX_VALUE;
            int urlStart = urlMatcher.find(lastIndex) ? urlMatcher.start() : Integer.MAX_VALUE;

            // Determine which match comes first
            int nextMatchStart = Math.min(emailStart, Math.min(phoneStart, urlStart));

            if (nextMatchStart == Integer.MAX_VALUE) {
                break;  // No more matches found
            }

            // Append the text before the match
            result.append(text.substring(lastIndex, nextMatchStart));

            // Apply formatting based on the type of match (email, phone, or URL)
            if (nextMatchStart == emailStart) {
                result.append("<span style=\"font-weight:bold; text-decoration:underline; color:#1a73e8;\">")
                        .append(emailMatcher.group())
                        .append("</span>");
                lastIndex = emailMatcher.end();
            } else if (nextMatchStart == phoneStart) {
                result.append("<span style=\"font-weight:bold; text-decoration:underline; color:#ff5722;\">")
                        .append(phoneMatcher.group())
                        .append("</span>");
                lastIndex = phoneMatcher.end();
            } else if (nextMatchStart == urlStart) {
                result.append("<span style=\"font-weight:bold; text-decoration:underline; color:#00bfae;\">")
                        .append(urlMatcher.group())
                        .append("</span>");
                lastIndex = urlMatcher.end();
            }
        }

        // Append the remaining text after the last match
        result.append(text.substring(lastIndex));

        return result.toString();
    }

    // Function to copy HTML content to clipboard
    private void copyHtmlToClipboard() {
        String htmlText = htmlOutputArea.getText();
        StringSelection selection = new StringSelection(htmlText);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
        JOptionPane.showMessageDialog(this, "HTML content copied to clipboard!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        // Run the application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TextToHtmlConverter app = new TextToHtmlConverter();
                app.setVisible(true);
            }
        });
    }
}
