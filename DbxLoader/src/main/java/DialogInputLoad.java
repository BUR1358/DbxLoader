import com.dropbox.core.DbxException;

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
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;


class DialogInputLoad extends JFrame implements ActionListener {
    static JTextField textField;
    static JFrame frame;
    static JButton button;
    static JLabel label;

    public static void Dialog(String dir_tree) {
        frame = new JFrame("Загрузить в");
        label = new JLabel();
        button = new JButton("Загрузить");
        DialogInputLoad uploadDialog = new DialogInputLoad();

        JPanel panel = new JPanel();
        textField = new JTextField(16);
        label.setText("Укажите путь для сохранения в формате: \"/Моя папка/папка 1\"");

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
            if (!(DbxExampleIn4Tech.dbx_path.contains("."))) {
                FileInputStream inputStream = new FileInputStream(new File(DbxExampleIn4Tech.returnDir.get(0) + ""));
                DbxExampleIn4Tech.client_load.files().uploadBuilder(DbxExampleIn4Tech.dbx_path + "/" + DbxExampleIn4Tech.returnDir.get(1)).uploadAndFinish(inputStream);
                JOptionPane.showMessageDialog(frame, "Ваш файл загружен.\n" + DbxExampleIn4Tech.dbx_path + "/" + DbxExampleIn4Tech.returnDir.get(1));
                frame.setVisible(false);
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Путь выбран не корректно. Попробуйте снова");
            }
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage() + "\nПопробуйте снова");
            frame.setVisible(false);
            frame.dispose();
            try {
                DbxExampleIn4Tech.loadORdownloadMENU();
            } catch (IOException | URISyntaxException | DbxException ex) {
                ex.printStackTrace();
            }
        } catch (DbxException | IOException | IllegalArgumentException | StringIndexOutOfBoundsException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Загрузка файла не удалась. Проверьте правильность указанной директории. Для загрузки в корневую использовать \"/\"");
        }
    }
}
