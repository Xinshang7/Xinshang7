/**
 * 主类
 */
public class Main {
    public static void main(String[] args) {
        String orig = FileUtil.Read(args[0]);
        String copy = FileUtil.Read(args[1]);
        String SimilarPercent = SimhashUtil.getSimilar(orig,copy);
        String str = "原文：" + FileUtil.getName(args[0]) +
                "\r\n" +
                "抄袭版论文：" + FileUtil.getName(args[1]) +
                "\r\n" +
                "查重率为：" + SimilarPercent + "%";
        FileUtil.Write(str,args[2]);
        System.out.println("文件输出到路径：" + FileUtil.getPath(args[2]));
    }
}
