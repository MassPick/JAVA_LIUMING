package cn.com.szgao.lm.test_c;

import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.collection.AhoCorasick.AhoCorasickDoubleArrayTrie;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dictionary.BaseSearcher;
import com.hankcs.hanlp.dictionary.CoreDictionary;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.CRF.CRFSegment;
import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.suggest.Suggester;
import com.hankcs.hanlp.tokenizer.IndexTokenizer;
import com.hankcs.hanlp.tokenizer.NLPTokenizer;
import com.hankcs.hanlp.tokenizer.SpeedTokenizer;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;

public class HanlpTestDome01  {

	private static Logger log = LogManager.getLogger(HanlpTestDome01.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 
		 
		PropertyConfigurator.configure("D:\\data\\git\\MassPick\\WebContent\\WEB-INF\\log4j.properties"); 
		//Segment segment = HanLP.newSegment().enableNameRecognize(true);
		/*List<Term> termList2 = HanLP.segment("宿东 220102197801213112 13701377376 13843150707");
		System.out.println(termList2);
		for (Term term : termList2) {
			
			if(term.nature==Nature.nr){
				System.out.println("人名："+term.word);
			}else if(term.nature==Nature.m){
				System.out.println("数字："+term.word);
			}
			else{
			System.out.println(term.word);
			}			
		}*/
		
		//--nt  vn  v  n
		
		System.out.println(HanLP.segment("空调、制冷设备的销售、上门安装及上门维修；国内贸易；经营电子商务；经营进出口业务"));		
		
		//[电子, 产品, 的, 研发, 及, 销售, ；, 经营, 电子, 商务, ；, 信息, 咨询,  , ；, 企业, 形象, 策划, 、, 市场, 营销, 策划, ；, 网页, 设计, 、, 平面, 设计, ；, 国内, 贸易, 。]
		List<Term> termList2 = StandardTokenizer.segment("电子产品的研发及销售；经营电子商务；信息咨询 ；企业形象策划、市场营销策划；网页设计、平面设计；国内贸易。");
		System.out.println(termList2);
		
		List<Term> termList3 = NLPTokenizer.segment("电子产品的研发及销售；经营电子商务；信息咨询 ；企业形象策划、市场营销策划；网页设计、平面设计；国内贸易。");
		System.out.println(termList3);
		for (Term term : termList3) {
			System.out.println("word："+term.word  +"" +term.nature);
		}
		
		List<Term> termList = StandardTokenizer.segment("商品和服务");
		System.out.println(termList);
		
		
		
		/*
		 zdy( );
		 System.out.println("----------------------------\n");
		 sy();
		 System.out.println("----------------------------\n");
		 System.out.println("\n");
		 n();
		 System.out.println("----------------------------\n");
		 crf();
		 System.out.println("----------------------------\n");
		 
		 rm();//中国人名识别
		 System.out.println("----------------------------\n");
		 jx();
		 System.out.println("----------------------------\n");
		 wbtj();//文本推荐
		 System.out.println("----------------------------\n");
		 yc();//21. 依存句法解析
		 System.out.println("----------------------------\n");
		 
		 System.out.println("----------------------------\n");
		 
		 System.out.println("----------------------------\n");
		 
		 System.out.println("----------------------------\n");
		 
		 System.out.println("----------------------------\n");
		 
		 System.out.println("----------------------------\n");
		 */
		 
		 
	}
	
	/**
	 * 4. 索引分词
	 * 
	 * 主副食品/n [0:4]
	主副食/j [0:3]
	副食/n [1:3]
	副食品/n [1:4]
	食品/n [2:4]
索引分词IndexTokenizer是面向搜索引擎的分词器，能够对长词全切分，另外通过term.offset可以获取单词在文本中的偏移量。
	 */
	public static void sy(){
	List<Term> termList = IndexTokenizer.segment("主副食品");
	for (Term term : termList)
	{
	    System.out.println(term + " [" + term.offset + ":" + (term.offset + term.word.length()) + "]");
	    
	    System.out.println(term + " [" + term.offset + ":" + (term.offset + term.word.length()) + "]");
	    
	}
}
	
	/**
	 * 5. N-最短路径分词
	 * N最短路分词器NShortSegment比最短路分词器慢，但是效果稍微好一些，对命名实体识别能力更强。
一般场景下最短路分词的精度已经足够，而且速度比N最短路分词器快几倍，请酌情选择。
	 */
	public static void n(){
		Segment nShortSegment = new NShortSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
		Segment shortestSegment = new DijkstraSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
		String[] testCase = new String[]{
		        "今天，刘志军案的关键人物,山西女商人丁书苗在市二中院出庭受审。",
		        "刘喜杰石国祥会见吴亚琴先进事迹报告团成员",
		        };
		for (String sentence : testCase)
		{
		    System.out.println("N-最短分词：" + nShortSegment.seg(sentence) + "\n最短路分词：" + shortestSegment.seg(sentence));
		    for (Term term :  nShortSegment.seg(sentence)) {
//				if(term.nature== HanLP.  "nr"){
					System.out.println("人名：" + term.word);
//				}
			}
		}
		
	}
	
	/**
	 * 6. CRF分词
	 * CRF对新词有很好的识别能力，但是无法利用自定义词典。
	 */
	public static void crf(){
		Segment segment = new CRFSegment();
		segment.enablePartOfSpeechTagging(true);
		List<Term> termList = segment.seg("你看过穆赫兰道吗");
		System.out.println(termList);
		for (Term term : termList)
		{
		    if (term.nature == null)
		    {
		        System.out.println("识别到新词：" + term.word);
		    }
		}
	}
	
	/**
	 * 演示极速分词，基于AhoCorasickDoubleArrayTrie实现的词典分词，适用于“高吞吐量”“精度一般”的场合
	 * 极速分词是词典最长分词，速度极其快，精度一般。

在i7上跑出了2000万字每秒的速度。
	 */
	public static void  jx(){
		String text = "江西鄱阳湖干枯，中国最大淡水湖变成大草原";
        System.out.println(SpeedTokenizer.segment(text));
        long start = System.currentTimeMillis();
        int pressure = 1000000;
        for (int i = 0; i < pressure; ++i)
        {
            SpeedTokenizer.segment(text);
        }
        double costTime = (System.currentTimeMillis() - start) / (double)1000;
        System.out.printf("分词速度：%.2f字每秒", text.length() * pressure / costTime);
	}
	
	
	
	/**
	 * 8. 用户自定义词典
	 * 演示用户词典的动态增删
	 * 
	 * CustomDictionary是一份全局的用户自定义词典，可以随时增删，影响全部分词器。

另外可以在任何分词器中关闭它。通过代码动态增删不会保存到词典文件。

追加词典

CustomDictionary主词典文本路径是data/dictionary/custom/CustomDictionary.txt，用户可以在此增加自己的词语（不推荐）；也可以单独新建一个文本文件，通过配置文件CustomDictionaryPath=data/dictionary/custom/CustomDictionary.txt; 我的词典.txt;来追加词典（推荐）。

始终建议将相同词性的词语放到同一个词典文件里，便于维护和分享。

词典格式

每一行代表一个单词，格式遵从[单词] [词性A] [A的频次] [词性B] [B的频次] ...如果不填词性则表示采用词典的默认词性。

词典的默认词性默认是名词n，可以通过配置文件修改：全国地名大全.txt ns;如果词典路径后面空格紧接着词性，则该词典默认是该词性。
	 */
	 
	@SuppressWarnings("rawtypes")
	public static void zdy( )
    {
        // 动态增加
        CustomDictionary.add("孔雀女");
        // 强行插入
        CustomDictionary.insert("码农", "nz 1024");
        // 删除词语（注释掉试试）
//        CustomDictionary.remove("码农");
        System.out.println(CustomDictionary.add("裸婚", "v 2 nz 1"));
        System.out.println(CustomDictionary.get("裸婚"));
        String text = "码农和孔雀女裸婚了";  //  
        // AhoCorasickDoubleArrayTrie自动机分词
        final char[] charArray = text.toCharArray();
        CustomDictionary.parseText(charArray, new AhoCorasickDoubleArrayTrie.IHit<CoreDictionary.Attribute>()
        {
            @Override
            public void hit(int begin, int end, CoreDictionary.Attribute value)
            {
                System.out.printf("[%d:%d]=%s %s\n", begin, end, new String(charArray, begin, end - begin), value);
            }
        });
        
        // trie树分词
        BaseSearcher searcher = CustomDictionary.getSearcher(text);
        Map.Entry entry;
        while ((entry = searcher.next()) != null)
        {
            System.out.println(entry);
        }
        // 标准分词
        System.out.println(HanLP.segment(text));
    }
	
	/**
	 * 
	9. 中国人名识别
	
	目前分词器基本上都默认开启了中国人名识别，比如HanLP.segment()接口中使用的分词器等等，用户不必手动开启；上面的代码只是为了强调。

有一定的误命中率，比如误命中关键年，则可以通过在data/dictionary/person/nr.txt加入一条关键年 A 1来排除关键年作为人名的可能性，也可以将关键年作为新词登记到自定义词典中。

如果你通过上述办法解决了问题，欢迎向我提交pull request，词典也是宝贵的财富。
	 */
	public static void rm( )
    {
		String[] testCase = new String[]{
		        "签约仪式前，秦光荣、李纪恒、仇和等一同会见了参加签约的企业家。",
		        "王国强、高峰、汪洋、张朝阳光着头、韩寒、小四",
		        "张浩和胡健康复员回家了",
		        "王总和小丽结婚了",
		        "编剧邵钧林和稽道青说",
		        "这里有关天培的有关事迹",
		        "龚学平等领导,邓颖超生前",
		        };
		Segment segment = HanLP.newSegment().enableNameRecognize(true);
		for (String sentence : testCase)
		{
		    List<Term> termList = segment.seg(sentence);
		    System.out.println(termList);
		}
    }

	
	
	/**19. 文本推荐
	 * 在搜索引擎的输入框中，用户输入一个词，搜索引擎会联想出最合适的搜索词，HanLP实现了类似的功能。

可以动态调节每种识别器的权重
	 */
	public static void wbtj()
    {
		Suggester suggester = new Suggester();
        String[] titleArray =
        (
                "威廉王子发表演说 呼吁保护野生动物\n" +
                "《时代》年度人物最终入围名单出炉 普京马云入选\n" +
                "“黑格比”横扫菲：菲吸取“海燕”经验及早疏散\n" +
                "日本保密法将正式生效 日媒指其损害国民知情权\n" +
                "英报告说空气污染带来“公共健康危机”"
        ).split("\\n");
        for (String title : titleArray)
        {
            suggester.addSentence(title);
        }
        System.out.println(suggester.suggest("发言", 1));       // 语义
        System.out.println(suggester.suggest("危机公共", 1));   // 字符
        System.out.println(suggester.suggest("mayun", 1));      // 拼音
    }
	/**
	 * 21. 依存句法解析
	 */
	public static void yc()
    {
		 System.out.println(HanLP.parseDependency("把市场经济奉行的等价交换原则引入党的生活和国家机关政务活动中"));
		   
    }

}
