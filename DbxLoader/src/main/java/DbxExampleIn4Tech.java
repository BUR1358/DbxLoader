import java.awt.Desktop;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

import com.dropbox.core.DbxException;
import com.dropbox.core.BadRequestException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.Metadata;

import javax.swing.JOptionPane;

public class DbxExampleIn4Tech {
    private static final String APP_KEY = "5xbz20e2y4bn3ds";
    private static final String APP_SECRET = "hli7ivrj4lfzkmb";

    static int loadORdownload;
    static ArrayList<String> returnDir;
    static DbxClientV2 client_download;
    static DbxClientV2 client_load;
    static String dir_tree = "Дерево каталогов:\n";
    static String save_path;
    static String dbx_path;


    public static void main(String[] args) throws IOException, URISyntaxException, DbxException {
        loadORdownloadMENU();
    }

    public static void loadORdownloadMENU() throws IOException, URISyntaxException, DbxException {
        loadORdownload = DialogWindows.сhoiceWindow(); //выбор действия (кнопки скачать/загрузить (вернет 0/1))
        switch (loadORdownload) {
            case 0: {
                try {
                    client_load = authentication(); //аунтификация в Dbx
                    if (client_load != null) {
                        returnDir = DialogWindows.fileSelection(); //Выбор файла. Вернет путь и имя файла [getAbsolutePath, getName]
                        if (!(returnDir.isEmpty())) {
                            DialogInputLoad.Dialog(dir_tree_paint(client_load));
                        } else {
                            loadORdownloadMENU(); //возврат в меню
                        }
                    }
                } catch (BadRequestException e) {
                    JOptionPane.showMessageDialog(DialogInputLoad.frame, "Аутентификация не удалась. Попробуйте снова");
                    loadORdownloadMENU();
                }
                break;
            }

            case 1: {
                try {
                    client_download = authentication(); //аунтификация в Dbx
                    if (client_download != null) {
                        save_path = DialogWindows.saveDirDialog();
                        if (!save_path.equals("")) {
                            DialogInputDownload.Dialog(dir_tree_paint(client_download));
                        } else {
                            loadORdownloadMENU();
                        }
                    }
                } catch (BadRequestException e) {
                    JOptionPane.showMessageDialog(DialogInputDownload.frame, "Аутентификация не удалась. Попробуйте снова");
                    loadORdownloadMENU();
                }
                break;
            }
        }
    }

    public static void Search(ListFolderResult root_dir, ArrayList<String> files, StringBuilder deep, DbxClientV2 client) throws DbxException {
        for (Metadata metadata : root_dir.getEntries()) {
            if (metadata.getClass().toString().contains("FolderMetadata")) {
                files.add(deep + "" + metadata.getPathLower());
                ListFolderResult new_dir = client.files().listFolder(metadata.getPathLower() + "/");
                deep.append("—");
                Search(new_dir, files, deep, client);
                deep.deleteCharAt(0);
            } else if (metadata.getClass().toString().contains("FileMetadata")) {
                files.add(deep + "" + metadata.getPathLower());
            }
        }
    }

    public static String dir_tree_paint(DbxClientV2 client) throws DbxException {
        ListFolderResult root_dir = client.files().listFolder(""); //ROOT DIRECTORY
        ArrayList<String> files = new ArrayList<>();
        StringBuilder deep = new StringBuilder("");
        Search(root_dir, files, deep, client);
        for (String dir : files) {
            dir_tree = dir_tree + "\n" + dir;
        }
        return dir_tree;
    }

    public static DbxClientV2 authentication() throws IOException, URISyntaxException, DbxException {
        DbxRequestConfig config = new DbxRequestConfig("DbxExampleIn4Tech");
        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        DbxWebAuth webAuth = new DbxWebAuth(config, appInfo);
        DbxWebAuth.Request webAuthRequest = DbxWebAuth.newRequestBuilder().withNoRedirect().build();
        String url = webAuth.authorize(webAuthRequest);
        Desktop.getDesktop().browse(new URL(url).toURI());
        String code = JOptionPane.showInputDialog("Нажмите разрешить, скопируйте\nи вставьте сюда свой код доступа :");
        try {
            DbxAuthFinish authFinish = webAuth.finishFromCode(code);
            String accessToken = authFinish.getAccessToken();
            DbxClientV2 client = new DbxClientV2(config, accessToken);
            return client;
        } catch (NullPointerException e) {
            return null;
        }
    }
}
