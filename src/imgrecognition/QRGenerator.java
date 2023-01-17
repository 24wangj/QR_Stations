package imgrecognition;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

public class QRGenerator {

    private JFrame frame;
    private BufferedImage qrImage;
    private String qrText;

//    private String wordList = "apricot, avocado, biscuit, brisket, brownie, burrito, caramel, cheddar, cobbler, coconut, cupcake, dessert, freezer, glasses, granola, ketchup, lasagna, lettuce, mustard, noodles, oatmeal, oregano, pancake, parsley, popcorn, pretzel, pumpkin, ravioli, rhubarb, romaine, saffron, sherbet, spatula, spinach, strudel, tapioca, toaster, vanilla, vinegar, vitamin, DONE";
//    private String wordList = "sparrow, raccoon, wallaby, panther, peacock, pelican, penguin, piranha, pointer, octopus, opossum, ostrich, maltese, manatee, meerkat, leopard, lobster, hamster, giraffe, dolphin, catfish, cheetah, chicken, buffalo, bulldog, vitamin, DONE";
    private String wordList = "Placebo, ability, academy, account, achieve, acquire, Address, Advance, Airport, Balance, Banking, Barrier, Battery, Billion, Capable, Capital, Captain, Correct, Council, Default, Diamond, Digital, Element, Fiction, Fifteen, Formula, Fortune, Forward, Founder, Freedom, Gallery, Gateway, General, Holiday, Hundred, Imagine, Kingdom, Kitchen, Maximum, Million, Musical, Mystery, Natural, Nuclear, Obvious, Pattern, Phoenix, Premium, Program, Project, Quarter, Reverse, Sixteen, Seventh, Speaker, Special, Success, Theatre, Victory, Virtual, Visible, Welcome, DONE";
        
    private String[] words; 
    private int wordIndex = 0;
    public QRGenerator() {
        words = wordList.split(", ");
        
        // Initialize frame
        frame = new JFrame("QR Code Generator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(650, 395));
        frame.getContentPane().setLayout(null);
        frame.setVisible(true);

        // Set system theme
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error setting system theme!");
        }

        // Display default QR image (or blank white image if unable to load)
        String defaultImgName = "QR_Goes_Here.png";
        try {
            qrImage = ImageIO.read(new File("src/imgrecognition/images/" + defaultImgName));
        } catch (IOException e) {
            System.out.println("LOAD IMAGE FAILED!! " + defaultImgName);
            qrImage = new BufferedImage(QRUtil.IMAGE_SIZE, QRUtil.IMAGE_SIZE, BufferedImage.TYPE_BYTE_BINARY);
            Graphics2D g2d = qrImage.createGraphics();
            g2d.setColor(Color.WHITE);
            g2d.fillRect(0, 0, QRUtil.IMAGE_SIZE, QRUtil.IMAGE_SIZE);
            g2d.dispose();
        }

        // QR code display
        JPanel imagePanel = new JPanel();
        imagePanel.add(new JLabel(new ImageIcon(qrImage)));
        imagePanel.setBounds(10, 11, 330, 330);
        frame.getContentPane().add(imagePanel);

        // Text input instruction label
        JLabel inputLabel = new JLabel("Text to encrypt:");
        inputLabel.setBounds(350, 143, 84, 14);
        frame.getContentPane().add(inputLabel);

        // Text input field
        JTextField inputField = new JTextField();
        inputField.setLocation(350, 162);
        inputField.setSize(132, 23);
        inputField.setColumns(15);
        inputField.setText(words[wordIndex]);
        frame.getContentPane().add(inputField);

        // Generate QR code button
        JButton generateButton = new JButton("Generate QR code");
        generateButton.setBounds(492, 162, 125, 23);
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                qrText = inputField.getText();
                qrText = words[wordIndex].toLowerCase();
                qrText = randomCase(qrText);
                System.out.println(qrText);
                wordIndex++;
                inputField.setText(words[wordIndex].toLowerCase());
                int bitlyIndex = qrText.indexOf("bit.ly/");
                if (bitlyIndex >= 0) {
                    qrText = qrText.substring(bitlyIndex + 7);
                }

                boolean[][] qrGrid = QRUtil.encode(qrText);
                qrImage = QRUtil.booleanGridToQR(qrGrid);

                imagePanel.removeAll();
                imagePanel.add(new JLabel(new ImageIcon(qrImage)));

                frame.repaint();
                frame.revalidate();
            }
        });
        frame.getContentPane().add(generateButton);

        // Save QR code button
        JButton saveButton = new JButton("Save as PNG");
        saveButton.setBounds(492, 196, 125, 23);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (qrText == null) {
                    JOptionPane.showMessageDialog(null, "No QR code generated yet!");
                } else {
//                    String fileName = JOptionPane.showInputDialog(null,
//                            "Please enter a file name for the QR code image:\n(file extension not needed)");
//                    if (fileName == null) {
//                        return;
//                    }
//                    if (fileName.length() == 0) {
                        String fileName = qrText;
//                    }

                    try {
                        ImageIO.write(qrImage, "PNG", new File("src/imgrecognition/images7/" + fileName + ".png"));
                    } catch (IOException ex) {
                        System.out.println("WRITE IMAGE FAILED!! " + fileName);
                        ex.printStackTrace();
                    }
                }
            }
        });
        frame.getContentPane().add(saveButton);

        frame.pack();

    }
    
    public String randomCase(String in)
    {
        String out="";
        for(int z=0; z<in.length(); z++)
        {
            char c= in.charAt(z);
            char upper = (char)(c-32);
            if(Math.random()>.67)
               out += upper;
            else
                out+= c;
        }
        return out;
    }

    public static void main(String[] args) {
        new QRGenerator();
    }
}
