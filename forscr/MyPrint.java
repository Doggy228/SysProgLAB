import java.io.*;

public class MyPrint {
    public static void printDir(Writer wr, File d) throws IOException {
        for(File f: d.listFiles()){
            System.out.println(f.getName());
            if(f.isDirectory()){
                printDir(wr, f);
            } else if(f.isFile()){
                wr.write(f.getName()+"\r\n======================================================================\r\n");
                BufferedReader br = new BufferedReader(new FileReader(f));
                String S = br.readLine();
                while(S!=null){
                    wr.write(S);
                    wr.write("\r\n");
                    S = br.readLine();
                }
                br.close();;
            }
        }
    }
    public static void main(String argc[]) {
        try {
            BufferedWriter wr = new BufferedWriter(new FileWriter(argc[1]));
            printDir(wr, new File(argc[0]));
            wr.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
