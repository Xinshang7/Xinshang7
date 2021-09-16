
import com.hankcs.hanlp.HanLP;
import org.junit.Test;


public class AllTest {
    @Test
    public void SimhashUtilTest(){
        //测试文本
        String orig = FileUtil.Read("D:/test/orig.txt");
        String copy1 = FileUtil.Read("D:/test/orig_0.8_add.txt");
        String copy2 = FileUtil.Read("D:/test/orig_0.8_del.txt");
        String copy3 = FileUtil.Read("D:/test/orig_0.8_dis_1.txt");
        String copy4 = FileUtil.Read("D:/test/orig_0.8_dis_10.txt");
        String copy5 = FileUtil.Read("D:/test/orig_0.8_dis_15.txt");
        //测试文本为空
        String copy6 = "";
        //文本相同
        System.out.println(SimhashUtil.getSimilar(orig,orig));
        System.out.println(SimhashUtil.getSimilar(orig,copy1));
        System.out.println(SimhashUtil.getSimilar(orig,copy2));
        System.out.println(SimhashUtil.getSimilar(orig,copy3));
        System.out.println(SimhashUtil.getSimilar(orig,copy4));
        System.out.println(SimhashUtil.getSimilar(orig,copy5));
        System.out.println(SimhashUtil.getSimilar(orig,copy6));
    }

    @Test
    public void FileUtilTest(){
        //路径为空
        String str1 = FileUtil.Read("");
        FileUtil.Write("str1","");
        //路径存在
        String str2 = FileUtil.Read("D:/test/orig.txt");
        FileUtil.Write("str2","D:/test/ans.txt");
        //路径不存在或路径错误
        String str3 = FileUtil.Read("D:/test/none.txt");
        FileUtil.Write("str3","User:/test/ans.txt");
    }

    @Test
    public void HanLPTest(){
        //一般字符串
        String str1 = "今天天气真好，我想出去看电影!";
        //字符串为空
        String str2 = "";
        //字符串中的标点符号
        String str3 = "!,。《：》’——‘“” 今天天气真好，我想出去看电影!";
        //乱序
        String str4 = "好今天天真气，出去想我影电看";
        //短文本
        String str5 = "我";
        System.out.println(HanLP.segment(str1));
        System.out.println(HanLP.segment(str2));
        System.out.println(HanLP.segment(str3));
        System.out.println(HanLP.segment(str4));
        System.out.println(HanLP.segment(str5));
    }
}
