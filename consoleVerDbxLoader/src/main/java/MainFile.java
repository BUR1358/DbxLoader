import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.DbxAppInfo;
import com.dropbox.core.DbxWebAuth;
import com.dropbox.core.DbxAuthFinish;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;

import java.awt.Desktop;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

public class MainFile {
    private static final String APP_KEY = "5xbz20e2y4bn3ds";
    private static final String APP_SECRET = "hli7ivrj4lfzkmb";
    static DbxClientV2 client;

    static String enter;
    static String input_access_code;
    static String input_dir;
    static int begin_src;
    static int end_scr;
    static int end_dst;
    static String src_path;
    static String dst_path;
    static FileMetadata metadata;

    public static void main(String[] args) throws IOException, URISyntaxException, DbxException {
        loadORdownloadMENU();
    }

    private static void loadORdownloadMENU() throws IOException, URISyntaxException, DbxException {
        Scanner scan = new Scanner(System.in, "Cp866");
        System.out.print("Для вызов утилиты введите запрос в формате:\n" +
                "testtool ACCESS CODE: *********\n" +
                "testtool: put/get -s (src_path) -d (dst_path)\n\n" +
                "После вызова put соответствующий файл src_path должен загружаться на сервер по пути dst_path.\n" +
                "После вызова get файл, находящийся на сервере под именем src_path скачивается и сохраняется по локальному пути dst_path.\n" +
                "Пример (загрузить): put -s \"C:\\folder\\file.png\" -d /folder/folder2\n" +
                "Пример (скачать): get -s /folder/folder2/file.png -d \"C:\\folder\\file.png\"\n\n" +
                "Нажмите enter чтобы продолжить. Вас перенаправит на сайт,\n" +
                "где вы сможете войти в аккаунт и скопировать свой код доступа.");
        enter = scan.nextLine();
        if (enter.equals("")) {
            try {
                client = authentication();
                if (client != null) {
                    System.out.print("testtool: put/get -s (src_path) -d (dst_path) ");
                    input_dir = scan.nextLine();
                    if (input_dir.contains("put")) {
                        directory_setting(input_dir);
                        if (src_path.contains(".")) {
                            if ((src_path.charAt(0)) == ('"')) {
                                src_path = src_path.substring(1, src_path.length() - 1); //проверка если ввели "C:\file.png" вместо C:\file.png
                            }
                            try {
                                //src_path - локальный файл
                                //dst_path - путь на сервере
                                FileInputStream inputStream = new FileInputStream(new File(src_path));
                                client.files().uploadBuilder(dst_path + "/" + src_path.substring(src_path.lastIndexOf("\\") + 1)).uploadAndFinish(inputStream);
                                System.out.println("Ваш файл загружен\n" + dst_path + "/" + src_path.substring(src_path.lastIndexOf("\\") + 1));
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("\n\n\n\n\n\n\nДанной директории не существует или она указана некорректно. Попробуйте снова или закройте окно.\n");
                                loadORdownloadMENU();
                            }
                        } else {
                            System.out.println("\n\n\n\n\n\n\nФайл выбран некорректно. Выберите файл в формате .../файл.расширение. Попробуйте снова или закройте окно.\n");
                            loadORdownloadMENU();
                        }

                        //--------------------------------------------------------------------------------------------------
                    } else if (input_dir.contains("get")) {
                        directory_setting(input_dir);
                        if (src_path.contains(".")) {
                            if ((dst_path.charAt(0)) == ('"')) {
                                dst_path = dst_path.substring(1, dst_path.length() - 2); //проверка если ввели "C:\file.png" вместо C:\file.png

                            }
                            try {
                                //src_path - файл, находящийся на сервере
                                //dst_path -сохраняется по локальному пути
                                String name_dbx_file = "/" + src_path.substring(src_path.lastIndexOf("/") + 1);
                                System.out.println(name_dbx_file);
                                OutputStream downloadFile = new FileOutputStream(new File(dst_path + name_dbx_file));
                                metadata = client.files().downloadBuilder(src_path)
                                        .download(downloadFile);
                                System.out.println("Ваш файл загружен\n" + dst_path + "\\" + name_dbx_file.substring(1));
                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("\n\n\n\n\n\n\nДанной директории не существует или она указана некорректно. Попробуйте снова или закройте окно.\n");
                                loadORdownloadMENU();
                            }
                        } else {
                            System.out.println("\n\n\n\n\n\n\nФайл выбран некорректно. Выберите файл в формате .../файл.расширение. Попробуйте снова или закройте окно.\n");
                            loadORdownloadMENU();
                        }
                    } else {
                        System.out.println("\n\n\n\n\n\n\nВы ввели некорректную директорию. Попробуйте снова или закройте окно.\n");
                        loadORdownloadMENU();
                    }
                }
            } catch (DbxException e) {
                System.out.println("\n\n\n\n\n\n\nАвторизация не удалась. Попробуйте снова или закройте окно.\n");
                loadORdownloadMENU();
            }
        } else {
            System.out.println("\n\n\n\n\n\n\nВы не нажали enter. Попробуйте снова или закройте окно.\n");
            loadORdownloadMENU();
        }
    }

    public static void directory_setting(String input_dir) throws IOException, URISyntaxException, DbxException {
        try {
            begin_src = input_dir.indexOf("-s");
            end_scr = input_dir.lastIndexOf("-d") - 1;
            end_dst = input_dir.length() - 1;
            src_path = input_dir.substring(begin_src + 3, end_scr).trim();
            dst_path = input_dir.substring(end_scr + 4, end_dst + 1).trim();
            //System.out.println("src_path " + src_path);
            //System.out.println("dst_path " + dst_path);
        } catch (Exception e) {
            System.out.println("\n\n\n\n\n\n\nКоманда указана некорректно. Попробуйте снова или закройте окно.\n");
            loadORdownloadMENU();
        }
    }

    public static DbxClientV2 authentication() throws IOException, URISyntaxException, DbxException {
        DbxRequestConfig config = new DbxRequestConfig("DbxExampleIn4Tech");
        DbxAppInfo appInfo = new DbxAppInfo(APP_KEY, APP_SECRET);
        DbxWebAuth webAuth = new DbxWebAuth(config, appInfo);
        DbxWebAuth.Request webAuthRequest = DbxWebAuth.newRequestBuilder().withNoRedirect().build();
        String url = webAuth.authorize(webAuthRequest);
        Desktop.getDesktop().browse(new URL(url).toURI());
        Scanner scan_input = new Scanner(System.in,"Cp866");
        System.out.print("testtool ACCESS CODE: ");
        input_access_code = scan_input.nextLine();
        try {
            DbxAuthFinish authFinish = webAuth.finishFromCode(input_access_code);
            String accessToken = authFinish.getAccessToken();
            DbxClientV2 client = new DbxClientV2(config, accessToken);
            return client;
        } catch (NullPointerException e) {
            return null;
        }
    }


}
