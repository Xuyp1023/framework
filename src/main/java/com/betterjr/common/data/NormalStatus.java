package com.betterjr.common.data;

public enum NormalStatus {
    
   INVALID_STATUS("0", "停用"), VALID_STATUS("1", "启用 "), FALSE_STATUS("9", "失败");

   public final String value;
   
   public final String title;
   NormalStatus(String anValue, String anTitle){
      this.value = anValue;
      this.title = anTitle;
   }   
}