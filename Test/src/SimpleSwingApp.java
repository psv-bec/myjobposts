import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SimpleSwingApp {
    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("Simple Swing Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 1200);
        frame.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10); // Margin around components
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Add label and text field pairs
        JTextField kindTextField = new JTextField("blogger#post", 20);
        addLabelAndField("Kind", kindTextField, frame, gbc, 0);
        JTextField idTextField = new JTextField(20);
        addLabelAndField("ID", idTextField, frame, gbc, 1);
        JTextField blogIdTextField = new JTextField(20);
        addLabelAndField("Blog ID", blogIdTextField, frame, gbc, 2);
        
        // Create the label for Blog Name
        JLabel blogNameLabel = new JLabel("Blog Name");
        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(blogNameLabel, gbc);
        
        // Create the dropdown list for Blog Name with empty list item selected by default
        String[] blogNames = {
            "",
            "https://sriniedibasics.blogspot.com/",
            "https://techquestbank.blogspot.com/",
            "https://srinijobpostings.blogspot.com/"
        };
        JComboBox<String> blogNameComboBox = new JComboBox<>(blogNames);
        blogNameComboBox.setSelectedIndex(0);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        frame.add(blogNameComboBox, gbc);
        gbc.gridwidth = 1;

        JTextField publishedTextField = new JTextField(20);
        addLabelAndField("Published", publishedTextField, frame, gbc, 4);
        JTextField updatedTextField = new JTextField(20);
        addLabelAndField("Updated", updatedTextField, frame, gbc, 5);
        JTextField urlTextField = new JTextField(20);
        addLabelAndField("URL", urlTextField, frame, gbc, 6);
        JTextField selfLinkTextField = new JTextField(20);
        addLabelAndField("SelfLink", selfLinkTextField, frame, gbc, 7);
        JTextField titleTextField = new JTextField(20);
        addLabelAndField("Title", titleTextField, frame, gbc, 8);
        JTextField titleLinkTextField = new JTextField(20);
        addLabelAndField("TitleLink", titleLinkTextField, frame, gbc, 9);
        JTextField authorIdTextField = new JTextField(20);
        addLabelAndField("Author ID", authorIdTextField, frame, gbc, 10);
        JTextField authorDisplayNameTextField = new JTextField(20);
        addLabelAndField("Author Display Name", authorDisplayNameTextField, frame, gbc, 11);
        JTextField authorUrlTextField = new JTextField(20);
        addLabelAndField("Author URL", authorUrlTextField, frame, gbc, 12);
        JTextField authorImageUrlTextField = new JTextField(20);
        addLabelAndField("Author Image URL", authorImageUrlTextField, frame, gbc, 13);
        JTextField repliesTotalItemsTextField = new JTextField(20);
        addLabelAndField("Replies Total Items", repliesTotalItemsTextField, frame, gbc, 14);
        JTextField repliesSelfLinkTextField = new JTextField(20);
        addLabelAndField("Replies SelfLink", repliesSelfLinkTextField, frame, gbc, 15);
        JTextField repliesItemsCommentsTextField = new JTextField(20);
        addLabelAndField("Replies Items Comments", repliesItemsCommentsTextField, frame, gbc, 16);
        JTextField labelsTextField = new JTextField(20);
        addLabelAndField("Labels", labelsTextField, frame, gbc, 17);

        JTextField locationNameTextField = new JTextField(20);
        addLabelAndField("Location Name", locationNameTextField, frame, gbc, 18);
        JTextField locationLatTextField = new JTextField(20);
        addLabelAndField("Location Lat", locationLatTextField, frame, gbc, 19);
        JTextField locationLngTextField = new JTextField(20);
        addLabelAndField("Location Lng", locationLngTextField, frame, gbc, 20);
        JTextField locationSpanTextField = new JTextField(20);
        addLabelAndField("Location Span", locationSpanTextField, frame, gbc, 21);
        JTextField statusTextField = new JTextField(20);
        addLabelAndField("Status", statusTextField, frame, gbc, 22);

        // Create the label for Content
        JLabel contentLabel = new JLabel("Content");
        gbc.gridx = 0;
        gbc.gridy = 23;
        frame.add(contentLabel, gbc);

        // Create the text area for Content
        JTextArea contentTextArea = new JTextArea(10, 20);
        contentTextArea.setLineWrap(true);
        contentTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(contentTextArea);
        gbc.gridx = 1;
        gbc.gridy = 23;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        frame.add(scrollPane, gbc);
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create the label for CustomMetaData
        JLabel customMetaDataLabel = new JLabel("CustomMetaData");
        gbc.gridx = 0;
        gbc.gridy = 24;
        frame.add(customMetaDataLabel, gbc);

        // Create the text field for CustomMetaData
        JTextField customMetaDataTextField = new JTextField(20);
        customMetaDataTextField.setDocument(new JTextFieldLimit(200));
        gbc.gridx = 1;
        gbc.gridy = 24;
        frame.add(customMetaDataTextField, gbc);

        // Create the link button
        JButton openLinkButton = new JButton("Review Page View");
        gbc.gridx = 1;
        gbc.gridy = 25;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        frame.add(openLinkButton, gbc);

        // Create the Post To The Blog button
        JButton postBlogButton = new JButton("Post To The Blog");
        gbc.gridx = 1;
        gbc.gridy = 26;
        frame.add(postBlogButton, gbc);

        // Add action listener to the link button
        openLinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String content = contentTextArea.getText();
                if (!content.trim().isEmpty()) {
                    try {
                        // Save the content to a temporary HTML file
                        File tempFile = File.createTempFile("tempHtml", ".html");
                        try (FileWriter writer = new FileWriter(tempFile)) {
                            writer.write(content);
                        }
                        // Open the temporary HTML file in the default browser
                        Desktop.getDesktop().browse(tempFile.toURI());

                        // Delete the temp file on JVM exit
                        tempFile.deleteOnExit();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(frame, "Error opening browser: " + ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Content is empty. Please enter some text.");
                }
            }
        });

        // Add action listener to the Post To The Blog button
        postBlogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idTextField.getText().trim();
                String blogId = blogIdTextField.getText().trim();
                String customMetaData = customMetaDataTextField.getText().trim();
                String content = contentTextArea.getText().trim();
                String selectedBlog = (String) blogNameComboBox.getSelectedItem();
                String kind = kindTextField.getText().trim();
                String published = publishedTextField.getText().trim();
                String updated = updatedTextField.getText().trim();
                String url = urlTextField.getText().trim();
                String selfLink = selfLinkTextField.getText().trim();
                String title = titleTextField.getText().trim();
                String titleLink = titleLinkTextField.getText().trim();
                String authorId = authorIdTextField.getText().trim();
                String authorDisplayName = authorDisplayNameTextField.getText().trim();
                String authorUrl = authorUrlTextField.getText().trim();
                String authorImageUrl = authorImageUrlTextField.getText().trim();
                String repliesTotalItems = repliesTotalItemsTextField.getText().trim();
                String repliesSelfLink = repliesSelfLinkTextField.getText().trim();
                String repliesItemsComments = repliesItemsCommentsTextField.getText().trim();
                String labels = labelsTextField.getText().trim();
                String locationName = locationNameTextField.getText().trim();
                String locationLat = locationLatTextField.getText().trim();
                String locationLng = locationLngTextField.getText().trim();
                String locationSpan = locationSpanTextField.getText().trim();
                String status = statusTextField.getText().trim();

                // Collect missing information messages
                StringBuilder missingInfo = new StringBuilder();
                if (id.isEmpty()) {
                    missingInfo.append("ID is missing.\n");
                }
                if (blogId.isEmpty()) {
                    missingInfo.append("Blog ID is missing.\n");
                }
                if (customMetaData.isEmpty()) {
                    missingInfo.append("CustomMetaData is missing.\n");
                }
                if (content.isEmpty()) {
                    missingInfo.append("Content is missing.\n");
                }
                if (selectedBlog == null || selectedBlog.isEmpty()) {
                    missingInfo.append("Blog Name is not selected.\n");
                }
                if (kind.isEmpty()) {
                    missingInfo.append("Kind is missing.\n");
                }
                if (published.isEmpty()) {
                    missingInfo.append("Published is missing.\n");
                }
                if (updated.isEmpty()) {
                    missingInfo.append("Updated is missing.\n");
                }
                if (url.isEmpty()) {
                    missingInfo.append("URL is missing.\n");
                }
                if (selfLink.isEmpty()) {
                    missingInfo.append("SelfLink is missing.\n");
                }
                if (title.isEmpty()) {
                    missingInfo.append("Title is missing.\n");
                }
                if (titleLink.isEmpty()) {
                    missingInfo.append("TitleLink is missing.\n");
                }
                if (authorId.isEmpty()) {
                    missingInfo.append("Author ID is missing.\n");
                }
                if (authorDisplayName.isEmpty()) {
                    missingInfo.append("Author Display Name is missing.\n");
                }
                if (authorUrl.isEmpty()) {
                    missingInfo.append("Author URL is missing.\n");
                }
                if (authorImageUrl.isEmpty()) {
                    missingInfo.append("Author Image URL is missing.\n");
                }
                if (repliesTotalItems.isEmpty()) {
                    missingInfo.append("Replies Total Items is missing.\n");
                }
                if (repliesSelfLink.isEmpty()) {
                    missingInfo.append("Replies SelfLink is missing.\n");
                }
                if (repliesItemsComments.isEmpty()) {
                    missingInfo.append("Replies Items Comments is missing.\n");
                }
                if (labels.isEmpty()) {
                    missingInfo.append("Labels is missing.\n");
                }
                if (locationName.isEmpty()) {
                    missingInfo.append("Location Name is missing.\n");
                }
                if (locationLat.isEmpty()) {
                    missingInfo.append("Location Lat is missing.\n");
                }
                if (locationLng.isEmpty()) {
                    missingInfo.append("Location Lng is missing.\n");
                }
                if (locationSpan.isEmpty()) {
                    missingInfo.append("Location Span is missing.\n");
                }
                if (status.isEmpty()) {
                    missingInfo.append("Status is missing.\n");
                }

                // Show popup if there are missing items
                if (missingInfo.length() > 0) {
                    JOptionPane.showMessageDialog(frame, missingInfo.toString(), "Missing Information", JOptionPane.WARNING_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "All necessary information is provided. Ready to post to the blog.");
                }
            }
        });

        // Make the frame visible
        frame.setVisible(true);
    }

    private static void addLabelAndField(String labelText, JTextField textField, JFrame frame, GridBagConstraints gbc, int gridy) {
        JLabel label = new JLabel(labelText);
        gbc.gridx = 0;
        gbc.gridy = gridy;
        gbc.gridwidth = 1;
        frame.add(label, gbc);

        gbc.gridx = 1;
        gbc.gridy = gridy;
        gbc.gridwidth = 2;
        frame.add(textField, gbc);
    }
}

// Class to limit the length of JTextField
@SuppressWarnings("serial")
class JTextFieldLimit extends javax.swing.text.PlainDocument {
    private int limit;

    JTextFieldLimit(int limit) {
        super();
        this.limit = limit;
    }

    public void insertString(int offset, String str, javax.swing.text.AttributeSet attr) throws javax.swing.text.BadLocationException {
        if (str == null) {
            return;
        }

        if ((getLength() + str.length()) <= limit) {
            super.insertString(offset, str, attr);
        }
    }
}
