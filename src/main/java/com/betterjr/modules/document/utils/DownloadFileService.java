package com.betterjr.modules.document.utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import com.betterjr.common.mapper.BeanMapper;
import com.betterjr.common.selectkey.SerialGenerator;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.JedisUtils;
import com.betterjr.common.utils.UUIDUtils;
import com.betterjr.modules.document.data.DownloadFileInfo;
import com.betterjr.modules.document.entity.AgencyAuthorFileGroup;
import com.betterjr.modules.document.entity.CustFileItem;

public class DownloadFileService extends Thread {
    private static final String FileGroupCacheName="remote.fileGroup";
    private static final String FileAccesCacheName="remote.fileAccess";

    
    //检查间隔时间默认10秒
    private int innerTime = 10;
    
    public int getInnerTime() {
        return this.innerTime;
    }

    public void setInnerTime(int anInnerTime) {
        this.innerTime = anInnerTime;
    }

    public DownloadFileService(){
        
    }
    
    public static Map<String, FileAccessInfo> retrieveCacheDataMap(){
        Map<String, FileAccessInfo> cacheData= JedisUtils.getObjectMap(FileAccesCacheName);
        if(cacheData==null){
            return Collections.EMPTY_MAP;
        }
        return cacheData;
    }
    
    public static FileAccessInfo retrieveCacheDataMapField(String field){
        return JedisUtils.getObjectMapField(FileAccesCacheName, field);
    }
    
    public static void removeCacheDataMapField(String field){
        JedisUtils.mapObjectRemove(FileAccesCacheName, field);
    }
    
    public static Map<String, AgencyAuthorFileGroup> retrieveFileGroupMap(){
        Map<String, AgencyAuthorFileGroup> fileGroupMap= JedisUtils.getObjectMap(FileGroupCacheName);
        if(fileGroupMap==null){
            return Collections.EMPTY_MAP;
        }
        return fileGroupMap;
    }
    
    public static AgencyAuthorFileGroup retrieveFileGroupMapField(String field){
        return JedisUtils.getObjectMapField(FileGroupCacheName, field);
    }
    
    public static void storeCacheData( Map<String, FileAccessInfo> map){
        JedisUtils.setObjectMap(FileAccesCacheName, map, 0);
    }
    
    public static void storeCacheDataMapField(String field,FileAccessInfo value){
        JedisUtils.mapFieldObjectPut(FileAccesCacheName, field, value);
    }
    
    public static void storeFileGroupMap(Map<String, AgencyAuthorFileGroup> map){
        JedisUtils.setObjectMap(FileGroupCacheName, map, 0);
    }
    
    public static DownloadFileInfo exactDownloadFile(String anToken){
       if( BetterStringUtils.isNotBlank(anToken)){
           FileAccessInfo fileAccessInfo = retrieveCacheDataMapField(anToken);
           if (fileAccessInfo != null){
               
               return fileAccessInfo.exactFile();
           }
       }
       
       return null;
    }
    
    public static void addDownloadFile(DownloadFileInfo anFileInfo){
        AgencyAuthorFileGroup fileGroup = retrieveFileGroupMapField(anFileInfo.findComposeKey());
        Integer invalidTime = null;
        if (fileGroup == null){
            invalidTime = new Integer(30);
        }
        else{
            invalidTime = fileGroup.getInvalidTime();
        }
        FileAccessInfo acccessInfo = new FileAccessInfo(anFileInfo, invalidTime);
        
        storeCacheDataMapField(anFileInfo.getAccessToken(), acccessInfo);
    }
    
    public static DownloadFileInfo createInstance(CustFileItem anFileItem, Long anCustNo, String anPartnerCode, String anBusingType){
        DownloadFileInfo fileInfo = BeanMapper.map(anFileItem, DownloadFileInfo.class);
        fileInfo.setBusinType(anBusingType);
        fileInfo.setCustNo(anCustNo);
        fileInfo.setPartnerCode(anPartnerCode);
        String tmpToken = UUIDUtils.uuid().concat(Long.toHexString(System.currentTimeMillis())).concat(SerialGenerator.randomBase62(20));
        fileInfo.setAccessToken(tmpToken);
        DownloadFileService.addDownloadFile(fileInfo);
        return fileInfo;
    }
    
    public DownloadFileService(Map anMap){
        
        if(!Collections3.isEmpty(anMap)){
            storeFileGroupMap(anMap);
        }
    }
    
    @Override
    public void run() {
        long tmpInnerTime = this.innerTime * 1000;
        List<String> tmpInvalidKeyList = new ArrayList();
        while(true){
            tmpInvalidKeyList.clear();
            for(Map.Entry<String, FileAccessInfo> ent : retrieveCacheDataMap().entrySet()){
                if (ent.getValue().valid() == false){
                    tmpInvalidKeyList.add(ent.getKey());
                }
            }
            for(String invalidKey : tmpInvalidKeyList){
                removeCacheDataMapField(invalidKey); 
            }
            try {
                Thread.sleep(tmpInnerTime);
            }
            catch (InterruptedException e) { 
                break;
            }
        }
    }

    protected static class FileAccessInfo implements java.io.Serializable{
 
        private static final long serialVersionUID = 3687687291699715190L;
    private final long createTime;
       private final DownloadFileInfo  fileInfo;
       private final long validTime;
       public FileAccessInfo(DownloadFileInfo anFileInfo, Integer anValidTime){
           this.createTime = System.currentTimeMillis();
           this.fileInfo = anFileInfo;
           this.validTime = anValidTime.intValue() * 60 * 1000;
       }
       
       protected boolean valid(){
          long tmpTime = this.createTime + validTime;
          
          return tmpTime > System.currentTimeMillis();
       }
       
       protected DownloadFileInfo exactFile(){
           if (valid()){
               return this.fileInfo;
           }
           else{
              return null; 
           }
       }
    }
}
