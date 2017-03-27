package interfaces;

import javax.swing.*;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.io.*;

public class OutputWindow implements Runnable {

    private JFrame Windows;
    private JTextArea outputAreaSender;
    private JTextArea outputAreaReceiverMC;
    private JTextArea outputAreaReceiverMB;
    private JTextArea outputAreaReceiverMR;
    private JScrollPane scrollPaneSender;
    private JScrollPane scrollPaneReceiverMC;
    private JScrollPane scrollPaneReceiverMB;
    private JScrollPane scrollPaneReceiverMR;

    public OutputWindow() {
    	
        this.outputAreaSender = new JTextArea(12, 100);
        this.outputAreaSender.setEditable(false);
        
        this.outputAreaReceiverMC = new JTextArea(12, 100);
        this.outputAreaReceiverMC.setEditable(false);
        
        this.outputAreaReceiverMB = new JTextArea(12, 100);
        this.outputAreaReceiverMB.setEditable(false);
        
        this.outputAreaReceiverMR = new JTextArea(12, 100);
        this.outputAreaReceiverMR.setEditable(false);
    }

    @Override
    public void run() {
        Windows = new JFrame();
        Windows.setAlwaysOnTop(false);
		Windows.setIconImage(Toolkit.getDefaultToolkit().getImage("..\\Upload-to_Cloud-512.png"));
		Windows.setResizable(false);
		Windows.setTitle("SDIS - Distributed Backup Service"); 
		Windows.setBackground(Color.WHITE);
		Windows.setBounds(0, 0, 1125, 840);
		Windows.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Windows.setLayout(null);
		
        scrollPaneSender = new JScrollPane(outputAreaSender);
        JPanel outputPanelSender = new JPanel(new FlowLayout());
        outputPanelSender.setBounds(0, 0, 1120, 200);
        outputPanelSender.add(scrollPaneSender);
        
        scrollPaneReceiverMC = new JScrollPane(outputAreaReceiverMC);
        JPanel outputPanelReceiverMC = new JPanel(new FlowLayout());
        outputPanelReceiverMC.setBounds(0, 200, 1120, 200);
        outputPanelReceiverMC.add(scrollPaneReceiverMC);
        
        scrollPaneReceiverMB = new JScrollPane(outputAreaReceiverMB);
        JPanel outputPanelReceiverMB = new JPanel(new FlowLayout());
        outputPanelReceiverMB.setBounds(0, 400, 1120, 200);
        outputPanelReceiverMB.add(scrollPaneReceiverMB);
        
        scrollPaneReceiverMR = new JScrollPane(outputAreaReceiverMR);
        JPanel outputPanelReceiverMR = new JPanel(new FlowLayout());
        outputPanelReceiverMR.setBounds(0, 600, 1120, 200);
        outputPanelReceiverMR.add(scrollPaneReceiverMR);

        Windows.add(outputPanelSender);
        Windows.add(outputPanelReceiverMC);
        Windows.add(outputPanelReceiverMB);
        Windows.add(outputPanelReceiverMR);
        Windows.setVisible(true);
    }

    private void updateTextSender(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                outputAreaSender.append(text);
            }
        });
    }
    
    private void updateTextReceiverMC(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                outputAreaReceiverMC.append(text);
            }
        });
    }
    
    private void updateTextReceiverMB(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                outputAreaReceiverMB.append(text);
            }
        });
    }
    
    private void updateTextReceiverMR(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                outputAreaReceiverMR.append(text);
            }
        });
    }

    public void printlnSendChannel(String msg) {
        updateTextSender(msg + "\n");
    }
    
    public void printlnReceiverMC(String msg) {
        updateTextReceiverMC(msg + "\n");
    }
    
    public void printlnReceiverMB(String msg) {
        updateTextReceiverMB(msg + "\n");
    }
    
    public void printlnReceiverMR(String msg) {
        updateTextReceiverMR(msg + "\n");
    }
    
    public void printStackTraceSendChannel(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        this.printlnSendChannel(sw.toString());
    }

    public void printStackTraceReceiverMC(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        this.printlnReceiverMC(sw.toString());
    }
    
    public void printStackTraceReceiverMB(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        this.printlnReceiverMB(sw.toString());
    }
    
    public void printStackTraceReceiverMR(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        this.printlnReceiverMR(sw.toString());
    }
}
