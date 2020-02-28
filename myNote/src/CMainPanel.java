import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class CMainPanel extends JPanel implements ActionListener {

    private JTabbedPane tabbedPane;
    private JList<File> directoryList;
    JButton button = new JButton("ok");
    String sdf ;

    public CMainPanel() {

        setLayout(new BorderLayout());

        initDirectoryList(); // add JList to main Panel

        initTabbedPane(); // add TabbedPane to main panel

        addNewTab(); // open new empty tab when user open the application
    }

    private void initTabbedPane() {
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void initDirectoryList() {
        File[] files = FileUtils.getFilesInDirectory();
        directoryList = new JList<>(files);

        directoryList.setBackground(new Color(211, 211, 211));
        directoryList.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        directoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        directoryList.setVisibleRowCount(-1);
        directoryList.setMinimumSize(new Dimension(130, 100));
        directoryList.setMaximumSize(new Dimension(130, 100));
        directoryList.setFixedCellWidth(130);
        directoryList.setCellRenderer(new MyCellRenderer());
        directoryList.addMouseListener(new MyMouseAdapter());

        add(new JScrollPane(directoryList), BorderLayout.WEST);
    }


    public void addNewTab() {
        JTextArea textPanel = createTextPanel();
        textPanel.setText("Write Something here...");
        tabbedPane.addTab("Tab " + (tabbedPane.getTabCount() + 1), textPanel);
    }

    public void openExistingNote(String content) {
        JTextArea existPanel = createTextPanel();
        existPanel.setText(content);

        int tabIndex = tabbedPane.getTabCount() + 1;
        tabbedPane.addTab("Tab " + tabIndex, existPanel);
        tabbedPane.setSelectedIndex(tabIndex - 1);
    }

    public void saveNote() throws IOException {
        JFrame jFrame = new JFrame("name ");
        jFrame.setLocation(400,400);
        jFrame.setSize(200,300);
        jFrame.setLayout(new GridLayout(2,1));
        JTextArea textPanel = (JTextArea) tabbedPane.getSelectedComponent();
        String note = textPanel.getText();
        if (!note.isEmpty()) {

            JTextArea a = new JTextArea();

            jFrame.add(a);
            jFrame.add(button);
            jFrame.setVisible(true);

            FileUtils.fileWriter(note);
            String str = textPanel.getText();
            sdf = str;
            /*jFrame.add(a);
            jFrame.add(button);
            jFrame.setVisible(true);

            FileUtils.fileWriter(note);
            String str = textPanel.getText();
            BufferedWriter writer = new BufferedWriter(new FileWriter( a.getText()+".txt"));
            writer.write(str);
            writer.close();*/


        }
        //jFrame.dispose();
        updateListGUI();
    }

    private JTextArea createTextPanel() {
        JTextArea textPanel = new JTextArea();
        textPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return textPanel;
    }

    private void updateListGUI() {
        File[] newFiles = FileUtils.getFilesInDirectory();
        directoryList.setListData(newFiles);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button){
            JTextArea a = new JTextArea();


            BufferedWriter writer = null;
            try {
                writer = new BufferedWriter(new FileWriter( a.getText()+".txt"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                writer.write(sdf);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            try {
                writer.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }


    private class MyMouseAdapter extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent eve) {
            // Double-click detected
            if (eve.getClickCount() == 2) {
                int index = directoryList.locationToIndex(eve.getPoint());
                System.out.println("Item " + index + " is clicked...");
                //TODO: Phase1: Click on file is handled... Just load content into JTextArea
                File curr[] = FileUtils.getFilesInDirectory();
                String content = FileUtils.fileReader(curr[index]);
                openExistingNote(content);
            }
        }
    }


    private class MyCellRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList list, Object object, int index, boolean isSelected, boolean cellHasFocus) {
            if (object instanceof File) {
                File file = (File) object;
                setText(file.getName());
                setIcon(FileSystemView.getFileSystemView().getSystemIcon(file));
                if (isSelected) {
                    setBackground(list.getSelectionBackground());
                    setForeground(list.getSelectionForeground());
                } else {
                    setBackground(list.getBackground());
                    setForeground(list.getForeground());
                }
                setEnabled(list.isEnabled());
            }
            return this;
        }
    }


}
