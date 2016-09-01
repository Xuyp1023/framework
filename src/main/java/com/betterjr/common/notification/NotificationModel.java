package com.betterjr.common.notification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mq.exception.BetterMqException;
import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;

/**
 * 消息通知模型
 * 
 * @author liuwl
 *
 */
public class NotificationModel implements Serializable {
    private static final long serialVersionUID = 1237632554334957458L;

    private String profileName;

    private CustOperatorInfo sendOperator;

    private CustInfo sendCustomer;

    private List<Long> receiveOperators = new ArrayList<>();

    private List<Long> receiveCustomers = new ArrayList<>();
    
    private List<String> receiveEmails = new ArrayList<>();
    
    private List<String> receiveMobiles = new ArrayList<>();
    
    private Map<String, Object> param = new HashMap<>();
    
    private List<Long> fileItems = new ArrayList<>();

    private BetterjrEntity entity;

    private NotificationModel(String anProfileName, CustInfo anSendCustomer, CustOperatorInfo anSendOperator) {
        this.profileName = anProfileName;
        this.sendCustomer = anSendCustomer;
        this.sendOperator = anSendOperator;
    }
    
    public static Builder newBuilder(String anProfileName, CustInfo anSendCustomer, CustOperatorInfo anSendOperator) {
        return new Builder(anProfileName, anSendCustomer, anSendOperator);
    }

    /**
     * 消息模型建造器 
     */
    public static class Builder {
        private final NotificationModel model;

        private Builder(String anProfileName, CustInfo anSendCustomer, CustOperatorInfo anSendOperator) {
            model = new NotificationModel(anProfileName, anSendCustomer, anSendOperator);
        }
        
        public Builder addParam(String anKey, Object anValue) {
            model.param.put(anKey, anValue);
            return this;
        }

        public Builder addReceiveOperator(Long anReceiveOperator) {
            model.receiveOperators.add(anReceiveOperator);
            return this;
        }

        public Builder addRecevieCustomer(Long anRecevieCustomer) {
            model.receiveCustomers.add(anRecevieCustomer);
            return this;
        }
        
        public Builder addReceiveEmail(String anReceiveEmail) {
            model.receiveEmails.add(anReceiveEmail);
            return this;
        }
        
        public Builder addReceiveMobile(String anReceiveMobile) {
            model.receiveMobiles.add(anReceiveMobile);
            return this;
        }
        
        public Builder addFileItem(Long anFileItemId) {
            model.fileItems.add(anFileItemId);
            return this;
        }

        public Builder setEntity(BetterjrEntity anEntity) {
            model.entity = anEntity;
            return this;
        }

        public NotificationModel build() {
            if (Collections3.isEmpty(model.receiveOperators) 
                    && Collections3.isEmpty(model.receiveCustomers)
                    && Collections3.isEmpty(model.receiveEmails)
                    && Collections3.isEmpty(model.receiveMobiles)) {
                throw new BetterMqException("请指定接收人!");
            }

            return model;
        }
    }

    public String getProfileName() {
        return profileName;
    }

    public CustOperatorInfo getSendOperator() {
        return sendOperator;
    }

    public CustInfo getSendCustomer() {
        return sendCustomer;
    }
    
    public List<Long> getReceiveOperators() {
        return receiveOperators;
    }

    public List<Long> getReceiveCustomers() {
        return receiveCustomers;
    }
    
    public List<String> getReceiveEmails() {
        return receiveEmails;
    }
    
    public List<String> getReceiveMobiles() {
        return receiveMobiles;
    }

    public BetterjrEntity getEntity() {
        return entity;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public List<Long> getFileItems() {
        return fileItems;
    }
}
