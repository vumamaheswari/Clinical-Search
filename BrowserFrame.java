package org.apache.nutch.LuceneSearchingScore;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event .*;
import java.io.IOException;
import java.util.logging.*;

public class BrowserFrame extends JFrame  implements HyperlinkListener {

    /** Creates new form BrowserFrame */
    public BrowserFrame() {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form. */
    private void initComponents() {
        GridBagConstraints gridBagConstraints;

        jPanel1 = new JPanel();
        btnBack = new JButton();
        btnForward = new JButton();
        btnRefresh = new JButton();
        tfURL = new JTextField();
        btnGo = new JButton();
        jScrollPane1 = new JScrollPane();
        epBrowser = new JEditorPane();

        // this is the trick!
        epBrowser.setEditable(false);
        epBrowser.addHyperlinkListener(this);    

        jMenuBar1 = new JMenuBar();
        jMenu1 = new JMenu();
        jMenu2 = new JMenu();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Java Browser");
        setMinimumSize(new Dimension(300, 300));
        setName("fMain"); // NOI18N
        getContentPane().setLayout(new GridBagLayout());

        jPanel1.setPreferredSize(new Dimension(100, 25));
        jPanel1.setLayout(new GridBagLayout());

        btnBack.setText("<");
        jPanel1.add(btnBack, new GridBagConstraints());

        btnForward.setText(">");
        jPanel1.add(btnForward, new GridBagConstraints());

        btnRefresh.setText("Odswiez");
        jPanel1.add(btnRefresh, new GridBagConstraints());

        tfURL.setText("http://www.google.com");
        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(tfURL, gridBagConstraints);

        btnGo.setText("Idz");
        btnGo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnGoActionPerformed(evt);
            }
        });
        jPanel1.add(btnGo, new GridBagConstraints());

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        getContentPane().add(jPanel1, gridBagConstraints);

        epBrowser.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        jScrollPane1.setViewportView(epBrowser);

        gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.gridheight = GridBagConstraints.REMAINDER;
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jScrollPane1, gridBagConstraints);

        jMenu1.setText("Plik");
        jMenuBar1.add(jMenu1);

        jMenu2.setText("Historia");
        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>


    private void btnGoActionPerformed(ActionEvent evt) {
        String url;
        epBrowser.setContentType("text/html");
        try {
            url = tfURL.getText();
            if (url.startsWith("http://"))
            url = url.substring(7);
            epBrowser.setPage("http://"+url);

        } catch (IOException ex) {
            Logger.getLogger(BrowserFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

   public void hyperlinkUpdate(HyperlinkEvent event) {
    if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                epBrowser.setPage(event.getURL());
                tfURL.setText(event.getURL().toExternalForm());
            } catch (IOException ex) {
                Logger.getLogger(BrowserFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
  }

    public static void main(String args[]) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BrowserFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private JButton btnBack;
    private JButton btnForward;
    private JButton btnGo;
    private JButton btnRefresh;
    private JEditorPane epBrowser;
    private JMenu jMenu1;
    private JMenu jMenu2;
    private JMenuBar jMenuBar1;
    private JPanel jPanel1;
    private JScrollPane jScrollPane1;
    private JTextField tfURL;
    // End of variables declaration

}


