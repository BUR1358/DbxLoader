import com.dropbox.core.DbxException;
import com.dropbox.core.v2.files.FileMetadata;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;


class DialogInputDownload extends JFrame implements ActionListener {
    static JTextField textField;
    static JFrame frame;
    static JButton button;
    static JLabel label;
    StringBuilder dbx_path_file_namedbx_path;
    FileMetadata metadata;
    OutputStream downloadFile;

    public static void Dialog(String dir_tree) {
        frame = new JFrame("Загрузка файла");
        label = new JLabel();
        button = new JButton("Скачать");
        DialogInputDownload uploadDialog = new DialogInputDownload();

        JPanel panel = new JPanel();
        textField = new JTextField(16);
        label.setText("Укажите путь к файлу в формате: \"/папка/файл.расширение\"");

        JTextArea area = new JTextArea(30, 45);
        area.setEditable(false);
        area.setFont(new Font("", Font.PLAIN, 14));     // Шрифт и табуляция
        area.setText(dir_tree);


        panel.add(textField);
        panel.add(label);
        panel.add(button);
        panel.add(new JScrollPane(area));
        button.addActionListener(uploadDialog);

        frame.add(panel);
        frame.setSize(550, 700);
        frame.setVisible(true);
        frame.show();
    }

    public void actionPerformed(ActionEvent event) {
        String s = event.getActionCommand();
        try {
            DbxExampleIn4Tech.dbx_path = textField.getText();
            dbx_path_file_namedbx_path = new StringBuilder(DbxExampleIn4Tech.dbx_path.substring(DbxExampleIn4Tech.dbx_path.lastIndexOf("/")));
            downloadFile = new FileOutputStream(DbxExampleIn4Tech.save_path + dbx_path_file_namedbx_path);
            metadata = DbxExampleIn4Tech.client_download.files().downloadBuilder(DbxExampleIn4Tech.dbx_path)
                    .download(downloadFile);
            downloadFile.close();
            JOptionPane.showMessageDialog(frame, "Файл загружен\n" + DbxExampleIn4Tech.save_path + "\\" + dbx_path_file_namedbx_path.deleteCharAt(0));
            frame.setVisible(false);
            frame.dispose();
        } catch (DbxException | IOException | IllegalArgumentException | StringIndexOutOfBoundsException e) {
            try {
                downloadFile.close();
                Files.delete(Paths.get(DbxExampleIn4Tech.save_path + dbx_path_file_namedbx_path));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            JOptionPane.showMessageDialog(frame, "Выберите конкретный файл. Загрузка нескольких файлов или папок невозможна");
        }
    }
}
