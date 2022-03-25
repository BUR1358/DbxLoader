import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;

public class DialogWindows extends Component {
    private static final String FILE_SELECTION_DIALOG_WIN_NAME = "Выберите файл";
    private static final String CHOICE_DIALOG_WIN_NAME = "DropBoxLoader";

    public static int сhoiceWindow() {
        Object[] options = {"Загрузить", "Скачать"};
        int choiceWindow = JOptionPane.showOptionDialog(null, "Что вы хотите сделать?",
                CHOICE_DIALOG_WIN_NAME,
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        return choiceWindow;
    }

    public static ArrayList<String> fileSelection() {
        JFileChooser fileopen = new JFileChooser();
        ArrayList<String> dir = new ArrayList<String>();
        if (fileopen.showDialog(null, FILE_SELECTION_DIALOG_WIN_NAME) == JFileChooser.APPROVE_OPTION) {
            dir.add(fileopen.getSelectedFile().getAbsolutePath()); //путь к выбранному файлу
            dir.add(fileopen.getSelectedFile().getName()); //имя файла
            return dir;
        }
        return dir;
    }

    static public String saveDirDialog() throws IOException {
        String save_path = "";
        JFileChooser filesave = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        filesave.setDialogTitle("Сохранение: ");
        filesave.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = filesave.showSaveDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            if (filesave.getSelectedFile().isDirectory()) {
                save_path = "" + filesave.getSelectedFile().getAbsolutePath();
                return save_path;
            }
        }
        return save_path;
    }
}
