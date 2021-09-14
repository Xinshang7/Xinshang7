import cn.hutool.core.lang.hash.MurmurHash;
import com.alibaba.fastjson.JSONObject;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.common.Term;

import java.util.*;
import java.util.stream.Collectors;
import java.util.ArrayList;

import static com.hankcs.hanlp.HanLP.segment;

/**
 * Simhash算法计算文本相似度
 */
public class SimhashUtil {
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
    private String getSimhash(String s) {
        //分词
        List<Term> segment = segment(s);
        Map<String, WordTerm> wordMap = new HashMap<>();
        //计算词频
        for (Term term : segment) {
            if (term.nature == Nature.w){
                continue;
            }
            WordTerm wordTerm = wordMap.get(term.word);
            if (wordTerm == null) {
                wordTerm = new WordTerm(term.word, term.nature);
                //词频
                wordTerm.frequency = 1;
                //hash值
                wordTerm.hash = MurmurHash.hash64(term.word.getBytes());
                wordMap.put(term.word, wordTerm);
            } else {
                wordTerm.frequency += 1;
            }
        }
        //加权hash
        for(Map.Entry<String ,WordTerm>wordTermEntry:wordMap.entrySet()){
            WordTerm wordTerm = wordTermEntry.getValue();
            final int frequency = wordTerm.frequency;
            long hash = wordTerm.hash;
            String hashBinaryString = Long.toBinaryString(hash);
            String[] hashArray = hashBinaryString.split("");
            List<Integer> collect = new ArrayList<>(Arrays.asList(hashArray)).stream().map(x ->
            {
                if (x.equals("0")) {
                    return -frequency;
                } else return frequency;
            }).collect(Collectors.toList());
            //剩余位补0
            int len = 64 - collect.size();
            for (int i = 0; i < len; i++) {
                collect.add(i,-frequency);
            }
            wordTerm.weightedHash = collect;
        }
        //生成64位simhash
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 64; i++) {
            //合并
            int sum = 0;
            for (Map.Entry<String,WordTerm> wordTermEntry:wordMap.entrySet()){
                WordTerm wordTerm = wordTermEntry.getValue();
                sum += wordTerm.weightedHash.get(i);
            }
            //降维
            if (sum > 0) {
                sb.append(1);
            }else{
                sb.append(0);
            }
        }
        return sb.toString();
    }
    //计算海明距离与文本相似度
    public String getSimilar(String str1,String str2){
        double distance;
        String d1 = getSimhash(str1);
        String d2 = getSimhash(str2);
        int len;
        if (d1.length() != d2.length()) {
            distance = -1;
        }else {
            distance = 0;
            len = d1.length();
            for (int i = 0; i < len; i++) {
                if (d1.charAt(i) != d2.charAt(i)) {
                    distance++;
                }
            }
        }
        return String.format("%.2f",100-distance*100/64);
    }
}
