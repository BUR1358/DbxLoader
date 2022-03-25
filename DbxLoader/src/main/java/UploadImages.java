//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.Locale;
//
//import com.dropbox.core.DbxAppInfo;
//import com.dropbox.core.DbxAuthFinish;
//
//import com.dropbox.core.DbxException;
//import com.dropbox.core.DbxRequestConfig;
//import com.dropbox.core.DbxWebAuthNoRedirect;
//
//
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.util.Locale;
//
//import com.dropbox.core.DbxException;
//import com.dropbox.core.DbxRequestConfig;
//import com.dropbox.core.v1.DbxAccountInfo;
//import com.dropbox.core.v1.DbxClientV1;
//import com.dropbox.core.v1.DbxEntry;
//import com.dropbox.core.v1.DbxWriteMode;
//import com.dropbox.core.v2.DbxClientV2;
//
//
//import com.dropbox.core.*;
//public class UploadImages
//{
//    public static void main(String[] args) throws IOException, DbxException
//    {
//        final String DROP_BOX_APP_KEY = "APPKEY";
//        final String DROP_BOX_APP_SECRET = "SECRETKEY";
//
//        String rootDir = "C:\\Users\\Downloads\\";
//
//        DbxAppInfo dbxAppInfo = new DbxAppInfo(DROP_BOX_APP_KEY, DROP_BOX_APP_SECRET);
//
//        DbxRequestConfig reqConfig = new DbxRequestConfig("javarootsDropbox/1.0",
//                Locale.getDefault().toString());
//        DbxWebAuthNoRedirect webAuth = new DbxWebAuthNoRedirect(reqConfig, dbxAppInfo);
//
//
//        String authorizeUrl = webAuth.start();
//        System.out.println("1. Go to this URL : " + authorizeUrl);
//        System.out.println("2. Click \"Allow\" (you might have to log in first)");
//        System.out.println("3. Copy the authorization code and paste here ");
//        String code = new BufferedReader(new InputStreamReader(System.in)).readLine().trim();
//
//
//
//        DbxAuthFinish authFinish = webAuth.finish(code);
//        String accessToken = authFinish.getAccessToken();
//
//        DbxClientV1 client = new DbxClientV1(reqConfig, accessToken);
//
//        System.out.println("account name is : " + client.getAccountInfo().displayName);
//
//
//
//        File inputFile = new File(rootDir +"images\\"+ "javaroots.jpg");
//        FileInputStream inputStream = new FileInputStream(inputFile);
//        try {
//
//            DbxEntry.File uploadedFile = client.uploadFile("/javaroots.jpg",
//                    DbxWriteMode.add(), inputFile.length(), inputStream);
//            String sharedUrl = client.createShareableUrl("/javaroots.jpg");
//            System.out.println("Uploaded: " + uploadedFile.toString() + " URL " + sharedUrl);
//        } finally {
//            inputStream.close();
//        }
//    }
//}