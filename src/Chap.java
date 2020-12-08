
public class Chap {

    private String url;
    private String chap_number;

    public Chap(String url) {
        this.url = url;
        chap_number = chapNumber(url);
    }

    public String getUrl() {
        return url;
    }

    public String getChap_number() {
        return chap_number;
    }

    private String chapNumber(String url) {
        int idx = 0;
        for (int i = 0; i < url.length(); i++) {
            if (Character.isDigit(url.charAt(i))) {
                idx = i;
                break;
            }
        }
        return url.substring(idx, url.length() - 1);
    }
}
