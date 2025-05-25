import javax.swing.*;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@SuppressWarnings("serial")
public class HtmlTextPaster extends JFrame {

    private JEditorPane editorPane;
    private JButton pasteButton;

    public HtmlTextPaster() {
        // Set up the frame
        setTitle("HTML Text Paster");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create the editor pane and enable HTML content
        editorPane = new JEditorPane();
        editorPane.setContentType("text/html");
        editorPane.setEditorKit(new HTMLEditorKit());
        editorPane.setText("<html><body><h3>Paste your text here</h3></body></html>");
        JScrollPane scrollPane = new JScrollPane(editorPane);

        // Create a paste button
        pasteButton = new JButton("Paste HTML Text");
        pasteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pasteHtmlContent();
            }
        });

        // Add components to the frame
        add(scrollPane, BorderLayout.CENTER);
        add(pasteButton, BorderLayout.SOUTH);
    }

    // Function to paste HTML formatted content
    private void pasteHtmlContent() {
        try {
            // Get the clipboard content in HTML format
            String clipboardHtml = (String) Toolkit.getDefaultToolkit()
                    .getSystemClipboard().getData(DataFlavor.stringFlavor);

            // Insert the HTML content into the editor pane
            editorPane.setText(clipboardHtml);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error pasting HTML content.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Run the application
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                HtmlTextPaster app = new HtmlTextPaster();
                app.setVisible(true);
            }
        });
    }
}
