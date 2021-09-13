import java.io.*;

/**
 * 对文件的读写操作
 */
public class FileUtil {
    //读文件
    public String Read(String path) {
        int len = 0;
        StringBuilder str = new StringBuilder();
        File file = new File(path);
        BufferedReader br = null;
        String line;
        try {
            br = new BufferedReader(new FileReader(file));
            while (((line = br.readLine()) != null)) {
                if (len != 0) {
                    str.append("\r\n").append(line);
                } else {
                    str.append(line);
                }
                len++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return str.toString();
    }
    //写文件
    public void Write(String str,String path){
        File file = new File(path);
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file));
            bw.append(str);
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (null != bw) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    //读文件名
    public String getName(String path){
        File file = new File(path);
        return file.getName();
    }
}
