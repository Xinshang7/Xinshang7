import cn.hutool.core.lang.hash.MurmurHash;
import com.alibaba.fastjson.JSONObject;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;

import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;

import static com.hankcs.hanlp.HanLP.segment;

public class SimhashUtil {
    /**
     * 数据结构：继承Term
     * 保存词频以及字符串的Hash值
     */
    static class WordTerm extends Term{
        int frequency;
        long hash;
        List<Integer> weightedHash;

        public WordTerm(String word, Nature nature) {
            super(word, nature);
        }

        @Override
        public String toString() {
            return JSONObject.toJSONString(this);
        }
    }

    /**
     * 计算文本的SimHash值
     * @param s 输入字符串
     * @return 返回64位Simhash
     */
    private static String getSimhash(String s) {
        //文本为空则返回空
        if(s == ""){
            return "";
        }
        //分词
        List<Term> segment = segment(s);
        Map<String, WordTerm> wordMap = new HashMap<>();
        //计算词频
        for (Term term : segment) {
            if (term.nature == Nature.w){
                continue;
            }
            WordTerm wordTerm = wordMap.get(term.word);
            if (wordTerm == null) {//词首次出现
                wordTerm = new WordTerm(term.word, term.nature);
                //词频初始为1
                wordTerm.frequency = 1;
                //计算hash值
                wordTerm.hash = MurmurHash.hash64(term.word.getBytes());
                wordMap.put(term.word, wordTerm);
            } else {
                //词已存在，词频+1
                wordTerm.frequency += 1;
            }
        }
        //以词频为标准进行加权
        for(Map.Entry<String ,WordTerm>wordTermEntry:wordMap.entrySet()){
            WordTerm wordTerm = wordTermEntry.getValue();
            final int frequency = wordTerm.frequency;
            long hash = wordTerm.hash;
            String hashBinaryString = Long.toBinaryString(hash);
            String[] hashArray = hashBinaryString.split("");
            //与Hash队列每一位比较，为0则权重为负，1则为正
            List<Integer> collect = new ArrayList<>(Arrays.asList(hashArray)).stream().map(x ->
            {
                if (x.equals("0")) {
                    return -frequency;
                } else return frequency;
            }).collect(Collectors.toList());
            //补齐剩余位
            int len = 64 - collect.size();
            for (int i = 0; i < len; i++) {
                collect.add(i,-frequency);
            }
            wordTerm.weightedHash = collect;
        }
        //生成64位simhash
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 64; i++) {
            //合并：将Hash值累加
            int sum = 0;
            for (Map.Entry<String,WordTerm> wordTermEntry:wordMap.entrySet()){
                WordTerm wordTerm = wordTermEntry.getValue();
                sum += wordTerm.weightedHash.get(i);
            }
            //降维：大于0则为1，小于0为0
            if (sum > 0) {
                sb.append(1);
            }else{
                sb.append(0);
            }
        }
        return sb.toString();
    }

    /**
     * 计算文本间的海明距离得出文本相似度
     * @param str1 文本1
     * @param str2 文本2
     * @return 返回文本相似度
     */
    public static String getSimilar(String str1,String str2){
        //文本为空则返回0
        if (str1 == "" || str2 == "") {
            return "0";
        }
        double distance;
        String d1 = getSimhash(str1);
        String d2 = getSimhash(str2);
        int len;
        //若SimHash位数不相等则返回-1
        if (d1.length() != d2.length()) {
            distance = -1;
        }else {
            distance = 0;
            len = d1.length();
            //每有一位SimHash值相等，则海明距离+1
            for (int i = 0; i < len; i++) {
                if (d1.charAt(i) != d2.charAt(i)) {
                    distance++;
                }
            }
        }
        return String.format("%.2f",100-distance*100/64);
    }
}
