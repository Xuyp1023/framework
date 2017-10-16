package com.betterjr.modules.notification;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.betterjr.common.entity.BetterjrEntity;
import com.betterjr.common.mq.exception.BetterMqException;
import com.betterjr.common.utils.BTAssert;
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

    public static class CustOperPair implements Serializable {
        /**
         *
         */
        private static final long serialVersionUID = 916552259919057616L;

        private final Long receiveOperator;
        private final Long receiveCustomer;

        public CustOperPair(final Long receiveCustomer, final Long receiveOperator) {
            BTAssert.isTrue(!(receiveCustomer == null && receiveOperator == null), "接收人和收接公司不允许同时为空！");
            this.receiveCustomer = receiveCustomer;
            this.receiveOperator = receiveOperator;
        }

        public Long getOperator() {
            return this.receiveOperator;
        }

        public Long getCustomer() {
            return this.receiveCustomer;
        }
    }

    private final String profileName;

    private CustOperatorInfo sendOperator;

    private CustInfo sendCustomer;

    private final List<CustOperPair> receivers = new ArrayList<>();

    private final List<String> receiveEmails = new ArrayList<>();

    private final List<String> receiveMobiles = new ArrayList<>();

    private final Map<String, Object> param = new HashMap<>();

    private final List<Long> fileItems = new ArrayList<>();

    private BetterjrEntity entity;

    private NotificationModel(final String anProfileName, final CustInfo anSendCustomer,
            final CustOperatorInfo anSendOperator) {
        this.profileName = anProfileName;
        this.sendCustomer = anSendCustomer;
        this.sendOperator = anSendOperator;
    }

    public static Builder newBuilder(final String anProfileName, final CustInfo anSendCustomer,
            final CustOperatorInfo anSendOperator) {
        return new Builder(anProfileName, anSendCustomer, anSendOperator);
    }

    /**
     * 消息模型建造器
     */
    public static class Builder {
        private final NotificationModel model;

        private Builder(final String anProfileName, final CustInfo anSendCustomer,
                final CustOperatorInfo anSendOperator) {
            model = new NotificationModel(anProfileName, anSendCustomer, anSendOperator);
        }

        public Builder addParam(final String anKey, final Object anValue) {
            model.param.put(anKey, anValue);
            return this;
        }

        public Builder addReceiver(final Long anReceiveCustomer, final Long anReceiveOperator) {
            model.receivers.add(new CustOperPair(anReceiveCustomer, anReceiveOperator));
            return this;
        }

        public Builder addReceiveEmail(final String anReceiveEmail) {
            model.receiveEmails.add(anReceiveEmail);
            return this;
        }

        public Builder addReceiveMobile(final String anReceiveMobile) {
            model.receiveMobiles.add(anReceiveMobile);
            return this;
        }

        public Builder addFileItem(final Long anFileItemId) {
            model.fileItems.add(anFileItemId);
            return this;
        }

        public Builder addFileItems(final List<Long> anFileItemIds) {
            model.fileItems.addAll(anFileItemIds);
            return this;
        }

        public Builder setEntity(final BetterjrEntity anEntity) {
            model.entity = anEntity;
            return this;
        }

        public NotificationModel build() {
            BTAssert.notNull(model.sendCustomer, "发送机构不允许为空！");
            BTAssert.notNull(model.sendOperator, "发送人不允许为空！");

            if (Collections3.isEmpty(model.receivers) && Collections3.isEmpty(model.receiveEmails)
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

    public void setSendOperator(final CustOperatorInfo anSendOperator) {
        sendOperator = anSendOperator;
    }

    public void setSendCustomer(final CustInfo anSendCustomer) {
        sendCustomer = anSendCustomer;
    }

    public List<CustOperPair> getReceivers() {
        return receivers;
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
