
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import javax.swing.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Crawler {

    ArrayList<Chap> listChap = new ArrayList<>();
    String url;

    public Crawler(String url) {
        this.url = url;
    }

    // Print all chapter numbers
    public void GetListChap() throws IOException {
        listChap = getAllChapInPage(url);
        System.out.println("List Chaps : ");
        for (Chap chap : listChap) {
            System.out.println("Chap : " + chap.getChap_number());
        }
    }

    public void SaveChap() {
        String chapNumber = JOptionPane.showInputDialog("Enter Chap number : ");
        String directory = "";
        JFileChooser file_chooser = new JFileChooser();
        file_chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        if (file_chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            directory = file_chooser.getCurrentDirectory().getPath();
        }
        Chap chap = getChapByChapNumber(chapNumber);
        System.out.println("Chap : " + Objects.requireNonNull(chap).getChap_number());
        saveFile(chap, directory);
    }

    // Get chapter by number
    private Chap getChapByChapNumber(String chapNumber) {
        for (Chap chap : listChap) if (chap.getChap_number().equalsIgnoreCase(chapNumber)) return chap;
        System.out.println("No chap has name : " + chapNumber);
        return null;
    }

    // Get all chapters
    private ArrayList<Chap> getAllChapInPage(String urls) throws IOException {
        ArrayList<Chap> list_chap = new ArrayList<>();
        Document document = Jsoup.connect(urls).get();
        Elements elms = document.getElementsByClass("chapter-name");
        for (int i = 0; i < elms.size(); i++) {
            Elements elm_row = elms.get(i).getElementsByTag("a");
            for (int j = 0; j < elm_row.size(); j++) {
                String link_chap = elm_row.first().absUrl("href");
                list_chap.add(new Chap(link_chap));
            }
        }
        return list_chap;
    }

    // Get all images in a chap
    private ArrayList<String> listImgOnPage(String pageURL) throws IOException {
        Document document = Jsoup.connect(pageURL).get();
        ArrayList<String> list_img = new ArrayList<>();
        Elements elms = document.getElementsByClass("chapter-content");
        for (int i = 0; i < elms.size(); i++) {
            Elements e = elms.get(i).getElementsByTag("img");
            for (int j = 0; j < e.size(); j++) {
                String url = e.get(j).absUrl("data-src");
                if (url.equals("")) {
                    continue;
                }
                list_img.add(url);
            }
        }
        return list_img;
    }

    // Save images
    private void saveImg(String src, String name, String dir) {
        try {
            URL url = new URL(src);
            InputStream in = url.openStream();
            OutputStream out = new BufferedOutputStream(new FileOutputStream(dir + "/" + name));
            for (int b; (b = in.read()) != -1; ) {
                out.write(b);
            }
            out.close();
            in.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Can not Download File !");
        }
    }

    private void saveFile(Chap chap, String dir) {
        try {
            ArrayList<String> list_img = listImgOnPage(chap.getUrl());
            for (int i = 0; i < list_img.size(); i++) {
                String name = i + ".jpg";
                saveImg(list_img.get(i), name, dir);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error", "Can not save file !", JOptionPane.ERROR_MESSAGE);
        }
        JOptionPane.showMessageDialog(null, "Download chap " + chap.getChap_number() + " successful");
    }
}
