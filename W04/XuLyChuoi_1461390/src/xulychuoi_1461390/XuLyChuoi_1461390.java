package xulychuoi_1461390;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author vmh20
 */
public class XuLyChuoi_1461390 {

//    public static void GetFood(String f) {
//        String t = f;
//        while (f != null) {
//            t = f.substring(0)
//        }
//    }
    public static void main(String[] args) {
        try {
            //Tham khảo: http://giasutinhoc.vn/lap-trinh-java-co-ban/doc-va-ghi-file-trong-java-bai-5/
            //Bước 1: Tạo đối tượng luồng và liên kết nguồn dữ liệu
            FileReader fr = null;
            File f1 = new File("D:\\1000data.txt");
            fr = new FileReader(f1);

            File f2 = new File("D:\\data.csv");
            FileWriter fw = new FileWriter(f2);

            String line;
            //Bước 2: Đọc dữ liệu
            BufferedReader br = new BufferedReader(fr);

            //Bước 2: Xử lý - Ghi file:
            @SuppressWarnings("MismatchedReadAndWriteOfArray")
            String[] arr = {"no", "no", "no", "no", "no", "no", "no", "no"};
            String[] words;
            fw.write("Bread,Cheese,Chips,Fruit,Jam,Milk,Peanut,Soda\n");
            while ((line = br.readLine()) != null) {
                words = line.split(",\\s");
                for (int i = 0; i < words.length; i++) {
                    switch (words[i]) {
                        case "Bread":
                            arr[0] = "yes";
                            break;
                        case "Cheese":
                            arr[1] = "yes";
                            break;
                        case "Chips":
                            arr[2] = "yes";
                            break;
                        case "Fruit":
                            arr[3] = "yes";
                            break;
                        case "Jam":
                            arr[4] = "yes";
                            break;
                        case "Milk":
                            arr[5] = "yes";
                            break;
                        case "Peanut":
                            arr[6] = "yes";
                            break;
                        case "Soda":
                            arr[7] = "yes";
                            break;
                    }

                }
                fw.write(arr[0]);
                arr[0] = "no";
                for (int j = 1; j < arr.length; j++) {
                    fw.write(",");
                    fw.write(arr[j]);
                    arr[j] = "no";
                }
                fw.write("\n");
            }

            //Bước 4: Đóng luồng
            fr.close();
            fw.close();
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XuLyChuoi_1461390.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XuLyChuoi_1461390.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
        }
    }

}
