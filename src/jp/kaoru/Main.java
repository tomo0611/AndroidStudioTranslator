package jp.kaoru;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        System.out.print("AndroidStudio 日本語化ツール\n\n");
        System.out.print("作成者:かおる\n");
        System.out.print("Google+ : https://plus.google.com/+%E3%81%8B%E3%81%8A%E3%82%8B%E3%82%93\n\n");
        ReadProperties();

    }

    public static void ReadProperties(){
        Properties pro = new Properties();
        try {
            pro.load(new FileInputStream("/home/kaoru/Documents/翻訳/test/JavaErrorMessages.properties"));
            for(Enumeration<Object> enumeration = pro.keys(); enumeration.hasMoreElements();){
                String name = (String) enumeration.nextElement();
                System.out.println(name + "=" + callPost(pro.getProperty(name)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String callPost(String text) {

        HttpURLConnection con = null;
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL("http://kaorun.dip.jp/translate.php?source_langcode=en&target_langcode=ja&text="+ URLEncoder.encode(text, "UTF-8"));
            con = (HttpURLConnection) url.openConnection();
            con.connect();
            final int status = con.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                final InputStream in = con.getInputStream();
                String encoding = con.getContentEncoding();
                if (null == encoding) {
                    encoding = "UTF-8";
                }
                final InputStreamReader inReader = new InputStreamReader(in, encoding);
                final BufferedReader bufReader = new BufferedReader(inReader);
                String line = null;
                while ((line = bufReader.readLine()) != null) {
                    result.append(line);
                }
                bufReader.close();
                inReader.close();
                in.close();
            } else {
                System.out.println(status);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (con != null) {
                // コネクションを切断
                con.disconnect();
            }
        }
        JSONObject jo = new JSONObject(result.toString());
        return jo.getJSONArray("sentences").getJSONObject(0).getString("trans");
    }

}
