package cn.com.szgao.clean.court;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import cn.com.szgao.clean.court.CourtUtils;
public class ExtractthepeopleText {
	private static Logger logger = LogManager.getLogger(ExtractthepeopleText.class.getName());
	public static String[] getDataCompanys(){
		return COMPANYS.split(",");
	}
	public static String[] getDataSurNames(){	
		return SURNAMES.split(",");
	}
	public static  String[] REPACLEALLKEY={"被","申","原审","反诉","一审"};
	private static String SURNAMES="徐,缪,楼,阴,幸,练,释,朱,司,苟,涂,买,华,拜,摆,兰,肖,旷,曲,黎,覃,付,丘,谌,锦,兴,叶,俞,赵,钱,孙,李,周,吴,郑,王,冯,陈,褚,卫,蒋,沈,韩,杨,朱,秦,尤,许,何,吕,施,张,孔,曹,严,华,"+
            "金,魏,陶,姜,戚,谢,邹,喻,柏,水,窦,章,云,苏,潘,葛,奚,范,彭,郎,鲁,韦,昌,马,苗,凤,花,方,钟,迟,"+
            "俞,任,袁,柳,酆,鲍,史,唐,费,廉,岑,薛,雷,贺,倪,汤,滕,殷,罗,毕,郝,邬,安,常,乐,于,时,傅,"+
			"皮,卞,齐,康,伍,余,元,卜,顾,孟,平,黄,和,穆,萧,尹,姚,邵,湛,汪,祁,毛,禹,狄,米,贝,明,臧,"+
			"计,伏,成,戴,谈,宋,茅,庞,熊,纪,舒,屈,项,祝,董,梁,杜,阮,蓝,闵,席,季,麻,强,贾,路,娄,危,"+
			"江,童,颜,郭,梅,盛,林,刁,锺,徐,邱,骆,高,夏,蔡,田,樊,胡,凌,霍,虞,万,支,柯,昝,管,卢,莫,"+
			"房,裘,干,解,应,宗,丁,宣,贲,邓,郁,单,杭,洪,包,诸,左,石,崔,吉,钮,龚,程,嵇,邢,滑,裴,"+
			"陆,荣,翁,荀,羊,於,惠,甄,麴,家,封,芮,羿,储,靳,汲,邴,糜,松,井,段,富,巫,乌,焦,巴,弓,牧,"+
			"隗,山,谷,车,侯,宓,蓬,全,郗,班,仰,秋,仲,伊,宫,宁,仇,栾,暴,甘,钭,历,戎,祖,武,符,刘,景,"+
			"詹,束,龙,印,宿,白,怀,蒲,邰,从,鄂,索,咸,籍,赖,卓,蔺,屠,蒙,池,乔,阳,郁,胥,能,苍,双,闻,"+
			"莘,党,翟,谭,贡,劳,逄,姬,申,扶,堵,冉,宰,郦,雍,却,璩,桑,桂,濮,牛,寿,边,扈,燕,冀,僪,冶,"+
			"浦,尚,农,温,别,庄,晏,柴,瞿,阎,充,慕,连,茹,习,宦,艾,鱼,容,向,古,易,慎,戈,廖,庾,终,暨,"+
			"居,衡,步,都,耿,满,弘,匡,国,文,寇,广,禄,阙,东,欧,殳,沃,利,蔚,越,夔,隆,师,巩,厍,聂,晁,"+
			"勾,敖,融,冷,訾,辛,阚,那,简,饶,空,曾,毋,沙,乜,鞠,须,丰,巢,关,蒯,相,查,后,荆,红,游,竺,"+
			"权,逮,盍,益,桓,公,万俟,司马,上官,欧阳,夏侯,诸葛,闻人,东方,东皇,赫连,皇甫,尉迟,公羊,澹台,"+
			"公冶,宗政,濮阳,淳于,单于,太叔,申屠,公孙,仲孙,轩辕,令狐,钟离,宇文,长孙,慕容,司徒,司空,尉,"+
			"召,南宫,赏,伯,佴,佘,牟,商,西门,东门,左丘,梁丘,琴,公良,段干,开,光,丑,那拉,叶赫那拉,星,子车,"+
			"瑞,眭,泥,运,摩,伟,铁,迮,刑,秘,鲜,寒";
private static String COMPANYS="公司,厅,机构,部,委员会,局,总署,银行,集团,店,酒楼,酒家,馆,餐厅,府,厅,社,学,事务所,厂,会,队,分行,支行,商行,办事处,行,场,中心,院,组,古寺,学";
public static  String[] PLDEF={"原告","被告","被上诉人","上诉人","被申诉人","申诉人","被执行人","执行人","原审原告","原审被告"};
//public static  String[] PLAINTIFF={"第三人","诉讼代理人","辩护人","上诉人","申诉人","申请执行人","申请人","执行人","原审被告","赔偿请求人","原公诉机关","公诉机关","执行机构","原告","复议机关","申请复议人","一审原告","委托代理人","法定代表人","起诉人","移送执行机构","二审上诉人","原审第三人","负责人","抗诉机关","申请再审人","委托代理","四被上诉人委托代理人","两上诉人的委托代理人"};
public static  String[] PLAINTIFF2={"原告"};
//public static  String[] DEFENDANT={"被上诉人","被执行人","被申诉人","被申请人","被申请执行人","原审被告人","原审原告","罪犯","被告","赔偿义务机关","一审被告","二审被上诉人"};
public static  String[] DELFEIN={"委托代理人","法定代表人","赔偿请求人"};
public static  String[] REPACLE={"上诉人","申诉人","申请执行人","执行人"};
public static  String[] REPACLE2={"被告","原告"};
public static  String[] KEYHEAVY={"被上诉人","被执行人","被申诉人","被申请人","被申请执行人"};
public static  String[] KEYWORDKE={"申请再审人","被上诉人","二审被上诉人","原审被告人","原审第三人","二审上诉人","一审被告","一审原告","被申请人","赔偿请求人","被告人","原告","执行机构","被申请人","申请执行人","申请人","辩护人","被申请执行人","赔偿请求人","赔偿义务机关","原公诉机关","抗诉机关","公诉机关","复议机关","委托代理人","委托代理","特别授权代理","四被上诉人委托代理人","两上诉人的委托代理人","移送执行机构","诉讼代理人","法定代表人","申请复议人","被上诉人","被申诉人","被执行人","反诉被告","反诉原告","原审被告","原审原告","执行人","负责人","上诉人","起诉人","申诉人","被告人","原告人","被告","原告","罪犯","第三人"}; 
public static  String[] KEYWORDKE2={"被申请人","申请人","被申请执行人","赔偿请求人","赔偿义务机关","公诉机关","复议机关","申请执行人","申请复议人","被上诉人","被申诉人","被执行人","原审被告","原审原告","执行人","上诉人","申诉人","被告","原告","罪犯"}; 
public static  String[] CAUSE={"驳回申诉通知","赔偿决定书","争议一案","纠纷一案","通告一案","违法一案","执行一案","赔偿一案","劫罪一案","确认一案","危险驾驶","诈骗","盗窃","死亡","强奸","聚众斗殴","寻衅滋事","贩卖毒品","运输毒品","故意伤害","涉嫌诽谤","抢劫","绑架","勒索","杀人","纠纷","非法拘禁","运输毒品","破坏电力设备","一案","违法","非法","犯罪"};
public static  String[] THEVERDICT={"裁定如下","决定如下","判决如下","协议如下","调解协议","如下协议","决定"};
public static  String[] BOOKCLASS={"委员会","院"};
public static  String[] THECOURT ={"国","省","市","法院","院"};
public static  String[] DATESTATUS={"提交时间","提交日期","发布时间","发布日期","编辑时间","编辑日期","发表时间","录入时间","更新时间","点击率","点击数","发表于","阅读","点击","日期","作者","时间","今天是","小"};
public static  String[] PARENTHESES={"（","[","\\(","）","(","书"};
public static  String[] CODING={"UTF-8","gb2312","gbk"};
public static  String[] ERCOEDING={"�","й","෨","Ժ"};
public static  String[] PARENTH={"（","("};
public static Map<String,String> MAP=new HashMap<String,String>();
static{
MAP.put("（", "）");
MAP.put("(", ")");
} 
	
	//根据。分段
	 public static String[] getSentences2(String input) {
	        if (input == null) {
	            return null;
	        } else {	        	
	        	String[] inputs=input.split("[。]");
	        	String[] inputs2=new String[inputs.length];
	        	int index=0;
	        	for(String val:inputs){
	        		if(null!=val&&!"".equals(val)){
	        			inputs2[index++]=val;
	        		}
	        	}
	        	inputs=null;
	            return inputs2;
	        }
	 
	 }
	 //匹配关键字取段
     public static String getListTakelong(String[] valueSipts,String key){
    	 if(null==valueSipts){return null;}
    	 String keys=null;
    	 StringBuffer sb=null;
    	 try {
    		 int index=0;
        	 int count=0;
        	 for(String val:valueSipts){
        		 if(null==val||"".equals(val)){continue;}
        		 //val=getReplaceAll(val);  
        		 val=getSpecialStringALL(val);
        		 val=getRpacleRllKey(val,key);
        		 if((val.length()-key.length())>=2){
        			 if(index==0){
        				index=val.lastIndexOf(key);
        				if(index!=-1){
        					if(sb==null){sb=new StringBuffer();}
        					keys=val.substring(index, val.length());
        					index=2;
        					sb.append(keys);
        					continue;
        				}
        				else{
        					index=0;
        					++count;
        					if(count>=2){index=1;}
        					continue;
        				}    					
        			 }
        			 else
        			 keys=val.substring(0,key.length());   			
        			 if(key.equals(keys)){  				 
        				 if(sb==null)
        					 sb=new StringBuffer(val);
        				 else
        					 sb.append("。").append(val);
        			 }
        		 }
        	 }
        	 return sb==null?null:sb.toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
    	 return null;
     }
     //根据关键字分段
   	 public static List<String> getPlaintiffText(String[] values,String key){		 
   		 List<String> list=null;
   		 String keys=null;
   		 try {
   			for(String val:values){			 
   	   			if((val.length()-key.length())>=2){
   	   			 keys=val.substring(0,key.length());
   	   			 if(key.equals(keys)){  				 
   	   				 if(list==null){list=new ArrayList<String>();}
   	   				 list.add(val);	 
   	   			 }
   	   		   }
   	   		 }
   	   		 if(list==null||list.size()==0){return null;}
   	   		 List<String> compNamelsit=null;
   	   		 List<String> resultList=null;
   	   		 String reuslt=null;
   	   		 for(String val:list){  			 
   	   			resultList=getSpiltComPanys(getParenthesesAll(val));
   	   			if(null!=resultList&&resultList.size()>0){
   	   			    if((reuslt=getCompanys(resultList))==null){
   	   			    	resultList=getSpiltNames(getParenthesesAll(val));
   	   	   				if(null!=resultList&&resultList.size()>0){
   	   	   					 if((reuslt=getSurName(resultList))==null){continue;};
   	   	   					   if(compNamelsit==null){compNamelsit=new ArrayList<String>();}  					  
   	   	   					   if(restultList(compNamelsit,reuslt)){compNamelsit.add(reuslt);}
   	   	   					   else
   	   	   	   			        compNamelsit.add(reuslt);
   	   	   	   			}
   	   			    }
   	   				if(compNamelsit==null){compNamelsit=new ArrayList<String>();} 
   	   				if(compNamelsit.size()>0){
   	   					if(restultList(compNamelsit,reuslt)){compNamelsit.add(reuslt);}
   	   				}
   	   				else
   	   				compNamelsit.add(reuslt);
   	   			}  			
   	   		 }  			 
   	   		 return compNamelsit;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
   		 return null;
   	 } 
   //拆分人名
 	public static List<String> getSpiltNames(String value){	
 		if(null==value||"".equals(value)){return null;}
 		List<String> list=null;
 		try {
 			int index=value.indexOf("、");
 	         if(index==-1){
 	         	list=new ArrayList<String>();
 	         	list.add(value);
 	         	return list;
 	         }
 	         else{
 	        	 String[] values=value.split("、");
 	        	 for(String val:values){
 	        		 if(null!=val&&!"".equals(val)){
 	        			 if(null==list){list=new ArrayList<String>();}
 	        			 list.add(val);
 	        		 }
 	        	 }
 	         }
 	 		return list;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
         return null;
 	}
    //拆分公司
 	public static List<String> getSpiltComPanys(String value){
 	 try {
	 		if(null==value||"".equals(value)){return null;}
	 		List<String> list=null;
	         int index=value.indexOf("、");
	         if(index==-1){
	         	list=new ArrayList<String>();
	         	list.add(value);
	         	return list;
	         }
	         else{
	        	 String[] values=value.split("、");
	        	 for(String val:values){
	        		 if(null!=val&&!"".equals(val)){
	        			 if(null==list){list=new ArrayList<String>();}
	        			 list.add(val);
	        		 }
	        	 }
         }
 			return list;
 		} catch (Exception e) {
 			logger.error("拆分公司出错:"+e.getMessage());
 		}
 		 return null;
 		
 	}
	 //过虑关键字
	 public static String getParenthesesAll(String value){
		 String replace=value;
		 for(String val:KEYWORDKE){
			 replace=replace.replace(val,"");
		 }
		/* replace=replace.replaceAll("\\（(.*)\\）","");
		 replace=replace.replaceAll("\\((.*)\\)","");*/
		 replace=replace.replaceAll("[\\(,\\),）,（]","");//去掉单括号
		 return replace;
	 }
	 
     //接接多个公司
	 public static String getCompanys(List<String> list){
		 if(null==list||list.size()==0)
			 return null;
		 try {
			 StringBuffer sb=null;
			 String value=null;
			 List<String> valKey=null;
			 for(String val:list){
				 for(String val2:getDataCompanys()){				
					 if((val.length()-val2.length())>=2){
						 value=val.substring((val.length()-val2.length()),val.length());
						 if(val2.equals(value)){
							 if(val.length()-val2.length()>20){break;}
							 if(null==valKey){valKey=new ArrayList<String>();}
							 if(valKey.size()>0){
								 if(restultList(valKey,val)){valKey.add(val);break;}
								 break;
							 }
							 else{valKey.add(val);break;}						 
						 }
					 }
				 }
			 }
			 if(null==valKey){return null;}
			 for(String val:valKey){
				 if(sb==null)
					 sb=new StringBuffer(val);
				 else
					 sb.append("、").append(val);
			 }
			 return sb==null?null:sb.toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		 return null;
	 }
	//接接多个姓名
	 public static String getSurName(List<String> list){
		 if(null==list||list.size()==0)
			 return null;
		 try {
			 String[] listSurName=getDataSurNames();
			 StringBuffer sb=null;
			 String surname =null;
			 List<String> nameList=null;
			 for(String val:list){			
				 for(String val2:listSurName){				 
					 if((val.length()-val2.length())>=1){
						 if(val2.length()>=2){surname=val.substring(0,val2.length());}
						 else
						 surname=String.valueOf(val.charAt(0));
						 if(val2.equals(surname)&&(val.length()-val2.length())<=3){
							 if(null==nameList){nameList=new ArrayList<String>();}
							 if(nameList.size()>0){
								 if(restultList(nameList,val)){nameList.add(val);break;}
								 break;
							 }
							 else{nameList.add(val);break;}
						 }
					 }
				 }			
			 }
			 if(null==nameList){return null;}
			 for(String val:nameList){
				 if(sb==null)
					 sb=new StringBuffer(val);
				 else
					 sb.append("、").append(val);
			 }
			 return sb==null?null:sb.toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		 return null;
	 }
	 //比较集合是否存在相同数据
	 public static boolean restultList(List<String> list,String value){
		 for(String val:list){
			 if(val.equals(value)){return false;}
		 }
		 return true;
	 }
	 //根据。,，,；分段
	 public static String[] getSentences(String input) {
	        if (input == null) {
	            return null;
	        } else {	            
	            return input.split("[。,，,；,，]");
	        }
	 
	 }
	 
	   //判断是否存在乱码
	    public static boolean getErrorCode(String title){
	    	for(String er:ERCOEDING){
	    		if(title.lastIndexOf(er)>=0){
	    			return false;
	    		}
	    	}
	    	return true;
	    }
	    //去掉无用字符
	    public static String getReplaceAll(String value){
			 StringBuffer sb=null;				  
			   if(value!=null&&!"".equals(value)){
//				    sb=new StringBuffer();
				    value=value.replaceAll(",","，");
				    value=value.replaceAll("�","O");
				    value=value.replaceAll("[×,X,Ｘ,x,╳,＊,\\*]","某");
				    value=value.replaceAll("[\n,\t,\r,\\s,&nbsp; ,：,“,”,・ ,:,<,/>,</,>,a-z,A-Z,-,+,=,},{,.,#,\",',-,%,^,*]","");
					value=getSpecialStringALL(value);
					if(value == null || "".equals(value)){return null;}
				    value=value.trim();
					sb=new StringBuffer();
					sb.append(value);
			   }
			 return sb==null?null:sb.toString();
		 }
	    //提取所有人
	    public static Map<String,List<String>> getPersonName(String html){
	    	if(null==html || "".equals(html)){return null;}
	    	html=getReplaceAll(html);
	    	List<String> defendant=null;	
			String[] values=getSentences2(html);
			Map<String,List<String>> map=new LinkedHashMap<String,List<String>>();
			String[] values2=null;
			String value=null;
			for(String val:KEYWORDKE){
				 value=getListTakelong(values,val);
				 values2=getSentences(value);
				 if(null==values2){continue;}
				 defendant=getPlaintiffText(values2,val);
				if(null!=defendant&&defendant.size()>0){
					map.put(val, defendant);
				}
			}
	    	return map;
	    }
	    //根据关键字提取人
	    public static String getKeyName(Map<String,List<String>> map,int status){
	    	if(map==null){return null;}
	    	String[] keys=null;
	    	 if(status==1)
				 keys=CourtUtils. PLAINTIFF;
			 else
				 keys=CourtUtils. DEFENDANT;
	    	 Set<String> setNames=null;
	    	 List<String> list=null;
	    	 String[] vals=null;
	    	 for(String key:keys){
	    		 list=map.get(key);
	    		 if(null!=list){
	    			 for(String val:list){
	    				 if(null==setNames){setNames=new HashSet<String>();}
	    				 if(val.indexOf("、")>=0){
	    					 vals=val.split("、");
	    					 for(String va:vals){
	    						 setNames.add(va);	 
	    					 }
	    				 }
	    				 else
	    				 setNames.add(val);
	    			 }
	    		 }
	    	 }
	    	 if(setNames==null||setNames.size()==0){return null;}
	    	 StringBuffer sb=null;
	    	 for(String val:setNames){
	    		 if(sb==null)
	    			 sb=new StringBuffer(val);
	    		 else
	    			 sb.append("、").append(val);
	    	 }
	    	 return sb==null?null:sb.toString();
	    }
	    //去掉特殊字符
	    public static String getSpecialStringALL(String value){
	    	if(null==value||"".equals(value)){return null;} 
	    	char[] chs=value.toCharArray();
	    	 StringBuffer sb=new StringBuffer();
	    	 for(char c:chs){
	    		 if(((int)c)!=12288&&((int)c)!=160){
	    			 sb.append(String.valueOf(c));
	    		 }
	    	 }
	    	 return sb.toString();
	    } 
	    //过滤关键字
	    public static String getRpacleRllKey(String value, String key){
	    	if(null==value||"".equals(value)){return null;}
	    	for(String val:REPACLEALLKEY){
	    		value=value.replaceAll(val+key,"");
	    	}
	    	return interception(value);
	    }
	    
	    //截取括号中的数据
	    public static String interception(String value){
	    	if(null==value||"".equals(value)){return null;}
	    	int index=0;
	    	int index2=0;
	    	StringBuffer sb=null;
	    	for(String val:PARENTH){
				while((index=value.indexOf(val))!=-1){
					sb=new StringBuffer();
					index2=value.indexOf(MAP.get(val));
					if(index2==-1){					
						break;
					}
					else{
						if(index2<index){break;}
						sb.append(value.substring(0,index));
						sb.append(value.substring(index2+1,value.length()));
						value=sb.toString();						
					}
				}
			}
	    	return value;
	    } 

}
