/**
 * 主类
 */
public class Main {
    private static final FileUtil file = new FileUtil();
    private static final SimhashUtil hash = new SimhashUtil();

    public static void main(String[] args) {
        String orig = file.Read(args[0]);
        String copy = file.Read(args[1]);
        String SimilarPercent = hash.getSimilar(orig,copy);
        String str = "原文：" + file.getName(args[0]) +
                "\r\n" +
                "抄袭版论文：" + file.getName(args[1]) +
                "\r\n" +
                "查重率为：" + SimilarPercent + "%";
        file.Write(str,args[2]);
    }


}
