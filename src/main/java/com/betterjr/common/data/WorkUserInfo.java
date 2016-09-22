package com.betterjr.common.data;

import java.io.Serializable;

public interface WorkUserInfo extends Serializable {
    
    public Long getId();
    
    public String getName();
    
    public String getIdentType();
    
    public String getIdentNo();
    
    public String getValidDate() ;
    
    public String getNickName();
    
    public String getRuleList();
    
}
