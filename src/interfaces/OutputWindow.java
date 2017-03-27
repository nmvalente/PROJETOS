package interfaces;
// source from http://stackoverflow.com/questions/28109509/java-start-sub-process-with-own-terminal

import javax.swing.*;
import java.awt.FlowLayout;
import java.io.*;

public class OutputWindow implements Runnable {

    private String title;
    private JFrame frame;
    private JTextArea outputArea;
    private JScrollPane scrollPane;

    public OutputWindow(String title, boolean redirectStreams) {
        this.title = title;
        this.outputArea = new JTextArea(12, 100);
        this.outputArea.setEditable(false);

        if (redirectStreams)
            redirectSystemStreams();
    }

    public OutputWindow(String title) {
        this(title, false);
    }

    @Override
    public void run() {
        frame = new JFrame(this.title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        scrollPane = new JScrollPane(outputArea);
        JPanel outputPanel = new JPanel(new FlowLayout());
        outputPanel.add(scrollPane);

        frame.add(outputPanel);
        frame.pack();
        frame.setVisible(true);
    }

    private void updateTextArea(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                outputArea.append(text);
            }
        });
    }

    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                updateTextArea(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                updateTextArea(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };

        System.setOut(new PrintStream(out, true));
        System.setErr(new PrintStream(out, true));
    }

    public void println(String msg) {
        updateTextArea(msg + "\n");
    }

    public void println(Throwable t) {
        println(t.toString());
    }

    public void print(String msg) {
        updateTextArea(msg);
    }

    public void printStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        this.println(sw.toString());
    }
}
