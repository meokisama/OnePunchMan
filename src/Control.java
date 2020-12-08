
import java.io.IOException;
import java.util.Scanner;

public class Control {

    public static void main(String[] args) throws IOException {
        String url = "https://truyentranh24.com/onepunch-man";
        Crawler crawler = new Crawler(url);
        Scanner input = new Scanner(System.in);
        while (true) {
            System.out.println("\n\n----- Manga Crawler -----");
            System.out.println("1. Get chapters list.");
            System.out.println("2. Download chap");
            System.out.println("3. Exit\n");
            System.out.print("Enter your choice: ");
            int choose = input.nextInt();
            switch (choose) {
                case 1:
                    crawler.GetListChap();
                    break;
                case 2:
                    crawler.SaveChap();
                    break;
                case 3:
                    return;
            }
        }

    }

}
