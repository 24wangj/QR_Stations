package server;

import shared.Roster;
import imgrecognition.QRHelper;
import imgrecognition.QRUtil;

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
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import shared.Player;

public class ServerEngine 
{

    private JFrame frame;
    private BufferedImage qrImage;
    private String qrText;
    
    private Roster roster = new Roster();
    private QRHelper qrHelper;
    
    public static boolean debug = true;
    public static boolean cameraOn = false;

    public ServerEngine() 
    {
        if(cameraOn)
        {
            qrHelper = new QRHelper();
        }

        loadFakeRoster();
        // Initialize frame
        frame = new JFrame("Server-side Monitor");
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
        JLabel inputLabel = new JLabel("Name to add to system:");
        inputLabel.setBounds(350, 143, 84, 14);
        frame.getContentPane().add(inputLabel);

        // Text input field
        JTextField inputField = new JTextField();
        inputField.setLocation(350, 162);
        inputField.setSize(132, 23);
        inputField.setColumns(15);
        frame.getContentPane().add(inputField);

        // Generate QR code button
        JButton generateButton = new JButton("Scan QR code");
        generateButton.setBounds(492, 162, 125, 23);
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                qrImage = qrHelper.getQRImage();
                qrText = qrHelper.convertImageToText(qrImage);
                System.out.println("qrText = "+qrText);
                String name = inputField.getText();
                if(qrText != null && !qrText.equals("QR Not found"))
                {
                    imagePanel.removeAll();              
                    imagePanel.add(new JLabel(new ImageIcon(qrImage)));
                }

                frame.repaint();
                frame.revalidate();
            }
        });
        frame.getContentPane().add(generateButton);

        // Add QR-Name combo to Roster button
        JButton saveButton = new JButton("Add to Roster");
        saveButton.setBounds(492, 196, 125, 23);
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                qrText = qrHelper.convertImageToText(qrImage);
                String name = inputField.getText();
                if(!qrText.equals("QR Not found") && !name.isEmpty())
                {
                    Player p = new Player(qrText,name);
                    roster.addEntry(qrText, p);
                    debugMsg("Added to roster: "+name+" with QRtext "+qrText);
                    saveButton.setBackground(Color.LIGHT_GRAY);
                }
                else
                {
                    saveButton.setBackground(Color.red);
                }
            }
        });
        frame.getContentPane().add(saveButton);

        frame.pack();

    }

//    public static void main(String[] args) {
//        new QRGenerator();
//    }


    public void addNewStation(int num, String name)
    {
        
    }
     
    public String processInput(String theInput, int stationNum) 
    {        
        debugMsg("@ processInput, theInput:" + theInput + " at stationNum: " + stationNum);
        if(theInput.startsWith("CHECKIN"))
        {
            debugMsg("Got a CHECKIN message");
            String[] parts = theInput.split(",");
            
            Player p = roster.getPlayerFromCode(parts[2]);
            if(p != null)
            {
                p.updateLocation(parts[1]);
                return p.pack();
            }
        }
        return "Couldn't decide what to say!";
    }
    
    
    public void loadFakeRoster()
    {
        Player p = new Player("Emma","Emma1");
        roster.addEntry(p.getQRtext(), p);
        p = new Player("Lisa","Lisa2");
        roster.addEntry(p.getQRtext(), p);
        p = new Player("Mike","Mike3");
        roster.addEntry(p.getQRtext(), p);
        p = new Player("virtual","Adam");
        roster.addEntry(p.getQRtext(), p);
        p = new Player("tapioca","Beth");
        roster.addEntry(p.getQRtext(), p);
        p = new Player("victory","Charles");
        roster.addEntry(p.getQRtext(), p);
        p = new Player("wallaby","David");
        roster.addEntry(p.getQRtext(), p);
        p = new Player("freedom","Ethan");
        roster.addEntry(p.getQRtext(), p);
        p = new Player("general","Fiona");
        roster.addEntry(p.getQRtext(), p);
        p = new Player("hamster","Greg");
        roster.addEntry(p.getQRtext(), p);
        p = new Player("ketchup","Hannah");
        roster.addEntry(p.getQRtext(), p);
        p = new Player("leopard","Isaac");
        roster.addEntry(p.getQRtext(), p);
        p = new Player("success","Jennifer");
        roster.addEntry(p.getQRtext(), p);
        p = new Player("vanilla","Keegan");
        roster.addEntry(p.getQRtext(), p);
        p = new Player("visible","Lucy");
        roster.addEntry(p.getQRtext(), p);

    }
    
    
    public void debugMsg(String msg)
    {
        if(debug)
            System.out.println(msg);
    }
}
