package com.betterjr.common.notification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mq.exception.BetterMqException;
import com.betterjr.common.utils.Collections3;

/**
 * 消息通知模型
 * 
 * @author liuwl
 *
 */
public class NotificationModel implements Serializable {
    private static final long serialVersionUID = 1237632554334957458L;

    private String profileName;

    private Long sendCustomer;

    private Long sendOperator;

    private List<Long> receiveOperators = new ArrayList<>();

    private List<Long> receiveCustomers = new ArrayList<>();
    
    private Map<String, Object> param = new HashMap<>();

    private BetterjrEntity entity;

    private NotificationModel(String anProfileName, Long anSendCustomer, Long anSendOperator) {
        this.profileName = anProfileName;
        this.sendCustomer = anSendCustomer;
        this.sendOperator = anSendOperator;
    }

    /**
     * 消息模型建造器 
     */
    public static class NotificationBuilder {
        private final NotificationModel model;

        public NotificationBuilder(String anProfileName, Long anSendCustomer, Long anSendOperator) {
            model = new NotificationModel(anProfileName, anSendCustomer, anSendOperator);
        }

        public NotificationBuilder addParam(String anKey, Object anValue) {
            model.param.put(anKey, anValue);
            return this;
        }

        public NotificationBuilder addReceiveOperator(Long anReceiveOperator) {
            model.receiveOperators.add(anReceiveOperator);
            return this;
        }

        public NotificationBuilder addRecevieCustomer(Long anRecevieCustomer) {
            model.receiveCustomers.add(anRecevieCustomer);
            return this;
        }

        public NotificationBuilder setEntity(BetterjrEntity anEntity) {
            model.entity = anEntity;
            return this;
        }

        public NotificationModel build() {
            if (Collections3.isEmpty(model.receiveOperators) && Collections3.isEmpty(model.receiveCustomers)) {
                throw new BetterMqException("请指定接收人!");
            }

            return model;
        }
    }

    public String getProfileName() {
        return profileName;
    }

    public Long getSendOperator() {
        return sendOperator;
    }

    public Long getSendCustomer() {
        return sendCustomer;
    }
    
    public List<Long> getReceiveOperators() {
        return receiveOperators;
    }

    public List<Long> getReceiveCustomers() {
        return receiveCustomers;
    }

    public BetterjrEntity getEntity() {
        return entity;
    }

    public Map<String, Object> getParam() {
        return param;
    }

}
