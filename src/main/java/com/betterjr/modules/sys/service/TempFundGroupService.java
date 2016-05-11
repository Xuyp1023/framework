package com.betterjr.modules.sys.service;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.betterjr.common.service.BaseService;
import com.betterjr.modules.sys.dao.*;
import com.betterjr.modules.sys.entity.*;

@Service
public class TempFundGroupService extends BaseService<TempFundGroupInfoMapper, TempFundGroupInfo> {
    @Autowired
    private TempFundGroupInfoMapper tempFundGroupInfoMapper;
    
    
    public void checkProtFund(Map map,String fundDay,String type){
        
        Double fundGroupIncome=Double.valueOf("0");
        Iterator ite = map.entrySet().iterator();
        while(ite.hasNext()){
            Entry entry = (Entry)ite.next();
            String fundCode = entry.getKey().toString(); 
            String rateFee = entry.getValue().toString();
            
            Map propMap = new HashMap<String, String>();
            propMap.put("fundCode", fundCode);
            propMap.put("fundDay", fundDay);
            
            List<TempFundGroupInfo> list = this.selectByProperty(propMap);
            TempFundGroupInfo fundCodeInfo = list.get(0);
            BigDecimal rate = new BigDecimal(Double.valueOf(rateFee));
            BigDecimal income = new BigDecimal(Double.valueOf(fundCodeInfo.getIncome()));
            if("G".equals(type)){
                BigDecimal multiply = rate.multiply(income);
                fundGroupIncome+=multiply.doubleValue();
            }else{
                fundGroupIncome = income.doubleValue();
            }
            
        }
        BigDecimal setScale = BigDecimal.valueOf(fundGroupIncome).setScale(5, BigDecimal.ROUND_HALF_UP);
        if("G".equals(type)){
            System.out.println(fundDay+":"+setScale);
        }else{
            System.out.println(setScale);
        }
        
    }
    
    //生成投资组合
    public Map crateFundGroup(String createDay){
        // 1.最近三个月平均年化收益率排名       条件1筛选20只，如果有收益率相同的，一并排序取
        List<Map> avgList = tempFundGroupInfoMapper.findYieldAvgByFundDay(createDay);
        //2.最近七日年化收益率排名
        List<Map> yieldList = tempFundGroupInfoMapper.findYieldByFundDay(createDay);
        //3.最近7日 每万份单位收益方差排名（由小至大）
        List<TempFundGroupInfo> incomeList = tempFundGroupInfoMapper.findIncomeByFundDay(createDay);
        Map<String,String> map = new HashMap<String,String>();
           for(int i=0;i<incomeList.size();i++){
               TempFundGroupInfo fundInfo = incomeList.get(i);
               String key = fundInfo.getFundCode();
               String value =fundInfo.getIncome();
               String dataValue = map.get(key);
               if(dataValue == null || "".equals(dataValue)){
                   map.put(key,Double.valueOf(value)+"");
               }else{
                   map.put(key,dataValue+","+Double.valueOf(value));
             }
         }
         //计算方差
         Map<String, Double> valueMap = new HashMap<String, Double>();
         for(TempFundGroupInfo fundInfo:incomeList){
             if(valueMap.get(fundInfo.getFundCode())!=null){
                 continue;
             }
             String value = map.get(fundInfo.getFundCode());
             String [] incomeValue = value.trim().split(",");
             ArrayList<Double> allIncome = new ArrayList<Double>();
             for(String income:incomeValue){
                 allIncome.add(Double.valueOf(income));
             }
             valueMap.put(fundInfo.getFundCode(), VarianceValue(allIncome));
         }
//         System.out.println("最近7日 每万份单位收益方差排名："+valueMap.toString());
         
         
          //条件1筛选20只，如果有收益率相同的，一并排序取
         Map oneSortMap = new HashMap<String, String>();
         String avgFundCode ="";
         for(int i=0;i<avgList.size();i++){
              Map avgInfo = avgList.get(i);
              if(i==0){
                  avgFundCode=avgInfo.get("FUNDCODE").toString();
              }else{
                  avgFundCode += ","+avgInfo.get("FUNDCODE").toString();
              }
              oneSortMap.put(avgInfo.get("FUNDCODE").toString(), avgInfo.get("RANKING").toString());
         }
         
//         System.out.println("满足条件1的20只："+avgFundCode);
         System.out.println("条件1的排序情况:"+oneSortMap.toString());
         
         //条件2在条件1的基础上筛选10只
        
         Map yiledInfoMap = new HashMap<String, String>();
         for(int i=0;i<yieldList.size();i++){
             Map yieldInfo = yieldList.get(i);
             String fundcode = yieldInfo.get("FUNDCODE").toString();
            
            if(avgFundCode.contains(fundcode)){
                yiledInfoMap.put(fundcode, Double.valueOf(yieldInfo.get("YIELD").toString()));
               
            }
         }
         //排序取10只
         Map twoSortMap = new HashMap<String, String>();
         Map yiledMap = sortMapD(yiledInfoMap,10);
//         System.out.println("满足条件2在条件1的基础上的10只"+yiledMap.toString());
         String yiledFundCode="";
         Iterator it = yiledMap.entrySet().iterator();
         int two=1;
         while(it.hasNext()){
             Entry entry = (Entry)it.next();
             yiledFundCode+=entry.getKey().toString()+",";
             twoSortMap.put( entry.getKey().toString(),two++);
         }
//         System.out.println("满足条件2在条件1的基础上的10只"+yiledFundCode);
         System.out.println("条件2的排序情况:"+twoSortMap.toString());
         
         
         //条件3在条件2的基础上筛选5只
         
         Map incomeInfoMap = new HashMap<String, String>();
         Iterator ite = valueMap.entrySet().iterator();
         while(ite.hasNext()){
             Entry entry = (Entry)ite.next();
             String incomeFundCode = entry.getKey().toString(); //返回与此项对应的键
             if(yiledFundCode.contains(incomeFundCode)){
                 incomeInfoMap.put(incomeFundCode, Double.valueOf(entry.getValue().toString()));
             }
         }
         String protFund = "";
         //排序取5只
         Map incomeMap = sortMap(incomeInfoMap,5);
         int three=1;
         Map threeSortMap = new HashMap<String, String>();
         Iterator iter = incomeMap.entrySet().iterator();
         while(iter.hasNext()){
             Entry entry = (Entry)iter.next();
             threeSortMap.put(entry.getKey().toString(),three++);
             protFund+=entry.getKey().toString()+",";
         }
         
//         System.out.println("满足条件3在条件2的基础上筛选5只:"+incomeMap.toString());
         System.out.println("条件3的排序情况:"+threeSortMap.toString());
             
         
        //5只基金重新按条件筛选
        // 1、最近七日年化收益率排名    
        //计分情况：第1名   5分、第2名4分、第3名3分、第4名2分、第5名1分
        // 2、最近30天每万份收益年化收益率排名
        //第1名   5分、第2名4分、第3名3分、第4名2分、第5名1分
        // 3、最近三个月平均年化收益率排名
        //第1名   5分、第2名4分、第3名3分、第4名2分、第5名1分
        //
       
         System.out.println("经筛选得出最后五只基金如下:"+protFund);
         Map protFundMap =  new HashMap<String, String>();
         String [] protFunds = protFund.trim().split(",");
         for(int p = 0;p<protFunds.length;p++){
             if(protFunds[p]==null){
                 continue;
             }
             //条件1 得分
             int oneScore = 0;
             String oneRank = oneSortMap.get(protFunds[p]).toString();
             if(oneRank.equals("1")){
                 oneScore=5;
             }else if(oneRank.equals("2")){
                 oneScore=4;
             }else if(oneRank.equals("3")){
                 oneScore=3;
             }else if(oneRank.equals("4")){
                 oneScore=2;
             }else if(oneRank.equals("5")){
                 oneScore=1;
             }
             //条件2 得分
             int twoScore=0;
             String twoRank = twoSortMap.get(protFunds[p]).toString();
             if(twoRank.equals("1")){
                 twoScore=5;
             }else if(twoRank.equals("2")){
                 twoScore=4;
             }else if(twoRank.equals("3")){
                 twoScore=3;
             }else if(twoRank.equals("4")){
                 twoScore=2;
             }else if(twoRank.equals("5")){
                 twoScore=1;
             }
             //条件3 得分
             int threeScore=0;
             String threeRank = threeSortMap.get(protFunds[p]).toString();
             if(threeRank.equals("1")){
                 threeScore=5;
             }else if(threeRank.equals("2")){
                 threeScore=4;
             }else if(threeRank.equals("3")){
                 threeScore=3;
             }else if(threeRank.equals("4")){
                 threeScore=2;
             }else if(threeRank.equals("5")){
                 threeScore=1;
             }
             
             protFundMap.put(protFunds[p], Double.valueOf(oneScore+twoScore+threeScore));
         }
         System.out.println("最后得分情况："+protFundMap.toString());
         //第1名占比40%
         //第2名占比30%
         //第3名占比15%
         //第4名占比10%
         //第5名占比5%
         
        Map protFundScoreMap = sortMapD(protFundMap, 5);
        int protFundScore=1;
        Map protFundSortMap = new HashMap<String, String>();
        Map protFundRateMap = new HashMap<String, String>();//占比例
        Iterator itor = protFundScoreMap.entrySet().iterator();
        while(itor.hasNext()){
            Entry entry = (Entry)itor.next();
            double rate = 0;
            if(protFundScore == 1){
                rate = 0.4;
            }else if(protFundScore == 2){
                rate = 0.3;
            }else if (protFundScore == 3){
                rate = 0.15;
            }else if (protFundScore == 4){
                rate = 0.1;
            }else{
                rate = 0.05;
            }
            protFundRateMap.put(entry.getKey().toString(), rate);
            protFundSortMap.put(entry.getKey().toString(),protFundScore++);
           
        }
        System.out.println("按照最后得分情况排序如下："+protFundSortMap.toString());
        System.out.println("最后的占比例情况如下："+protFundRateMap.toString());
        
        return protFundRateMap;
    }
    
    
    public List getFundDay(String beginDate,String endDate){
        return tempFundGroupInfoMapper.getFundDay(beginDate,endDate);
      }
      
    
    /**
     * map排序（由小到大）
     * @param oldMap
     * @param number
     * @param type
     * @return
     */
    public  Map sortMap(Map oldMap,int number) {  
        ArrayList<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(oldMap.entrySet());  
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {  
  
            @Override  
            public int compare(Entry<java.lang.String, Double> arg0,  
                    Entry<java.lang.String, Double> arg1) {  
                return arg0.getValue().compareTo(arg1.getValue());  
            }  
        });  
            Map newMap = new LinkedHashMap();  
            for (int i = 0; i <list.size(); i++) {  
                   if(i<number){
                       newMap.put(list.get(i).getKey(), list.get(i).getValue()); 
                  }
            }  
        return newMap;  
    }  
    
    /**
     * map排序 （由大到小）
     * @param oldMap
     * @param number
     * @param type
     * @return
     */
    public  Map sortMapD(Map oldMap,int number) {  
        ArrayList<Map.Entry<String, Double>> list = new ArrayList<Map.Entry<String, Double>>(oldMap.entrySet());  
        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {  
  
            @Override  
            public int compare(Entry<java.lang.String, Double> arg0,  
                    Entry<java.lang.String, Double> arg1) {  
                return arg1.getValue().compareTo(arg0.getValue());  
            }  
        });  
        Map newMap = new LinkedHashMap();  
        for (int i = 0; i <list.size(); i++) {  
            if(i<number){
                    newMap.put(list.get(i).getKey(), list.get(i).getValue()); 
            }
            
        }  
        return newMap;  
    }  
    

    //计算方差
    public double VarianceValue(ArrayList<Double> allNumber) {
        double value = 0;
        double variance = meanValue(allNumber);

        for (int i = 0; i < allNumber.size(); i++) {
            double x = (allNumber.get(i) - variance) * (allNumber.get(i) - variance);
            value += x;
        }
        value /= allNumber.size();
        return value;

    }
    public double meanValue(ArrayList<Double> allNumber) {
        double value = 0;
        for (int i = 0; i < allNumber.size(); i++) {
            value +=allNumber.get(i);
        }
        value /= allNumber.size();
        return value;
    }
    
}
