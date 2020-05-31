package mlcs;

import mlcs.np.Crawler;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;


public class Main {

    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            System.out.println(
                    "Usage: /path/to/your/data/file -Dmlcs.max-thread=2 -Djava.util.Arrays.useLegacyMergeSort=true -Dmlcs.rt=10");
            return;
        }
        if (System.getProperty("mlcs.rt") == null) {
            System.out.println("You should set a threshold for the number of reserve points (e.g. -Dmlcs.rt=10).");
            return;
        } else {
            Crawler.reserveThreshold = Integer.parseInt(System.getProperty("mlcs.rt"));
        }

        Crawler.main(args);

    }

    public static void writeFile(String fileName, List<String> stringList) {
        try {
            FileWriter writer = new FileWriter(fileName);
            for (String str : stringList) {
                writer.write(str + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
