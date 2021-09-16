import java.io.*;

/**
 * 对文件的读写操作
 */
public class FileUtil {
    /**
     * 读取文件
     * @param path 文件绝对路径
     * @return 文件内容
     */
    public static String Read(String path) {
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

    /**
     * 将字符串写入文件
     * @param str 需要写入文件的内容
     * @param path 存储文件的绝对路径
     */
    public static void Write(String str,String path){
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

    /**
     * 获取文件名
     * @param path 文件的路径
     * @return 文件名
     */
    public static String getName(String path){
        File file = new File(path);
        return file.getName();
    }

    /**
     * 获取文件绝对路径
     * @param path 文件的路径
     * @return 文件的绝对路径
     */
    public static String getPath(String path){
        File file = new File(path);
        return file.getPath();
    }
}
