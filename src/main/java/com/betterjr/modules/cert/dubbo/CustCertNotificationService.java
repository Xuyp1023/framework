// Copyright (c) 2014-2016 Bytter. All rights reserved.
// ============================================================================
// CURRENT VERSION
// ============================================================================
// CHANGE LOG
// V2.0 : 2016年10月18日, liuwl, creation
// ============================================================================
package com.betterjr.modules.cert.dubbo;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.config.annotation.Reference;
import com.betterjr.common.utils.BetterDateUtils;
import com.betterjr.common.utils.BetterStringUtils;
import com.betterjr.common.utils.Collections3;
import com.betterjr.common.utils.DictUtils;
import com.betterjr.modules.account.entity.CustInfo;
import com.betterjr.modules.account.entity.CustOperatorInfo;
import com.betterjr.modules.account.service.CustAccountService;
import com.betterjr.modules.account.service.CustOperatorService;
import com.betterjr.modules.cert.entity.CustCertInfo;
import com.betterjr.modules.notification.INotificationSendService;
import com.betterjr.modules.notification.NotificationModel;
import com.betterjr.modules.notification.NotificationModel.Builder;

/**
 * @author liuwl
 *
 */
@Service
public class CustCertNotificationService {
    @Reference(interfaceClass=INotificationSendService.class)
    INotificationSendService notificationSendService;

    @Resource
    private CustAccountService accountService;

    @Resource
    private CustOperatorService custOperatorService;

    public void sendNotification(final CustCertInfo anCertInfo, final String anPublishMode, final String anPassword) {
        final Long platformCustNo = Long.valueOf(Collections3.getFirst(DictUtils.getDictList("PlatformGroup")).getItemValue());
        final CustInfo platformCustomer = accountService.findCustInfo(platformCustNo);
        final CustOperatorInfo platformOperator = Collections3.getFirst(custOperatorService.queryOperatorInfoByCustNo(platformCustomer.getCustNo()));

        // 根据发布模式 发送证书及密码
        if (BetterStringUtils.equals("0", anPublishMode)) { // 客户的邮箱收证书 //客户的手机收密码
            final Builder builder = NotificationModel.newBuilder("证书颁发安全发送通知模板", platformCustomer, platformOperator);

            builder.addParam("custName", anCertInfo.getCustName());
            builder.addParam("token", anCertInfo.getToken());
            builder.addParam("createDate", BetterDateUtils.formatDispDate(anCertInfo.getCreateDate()));
            builder.addParam("validDate", BetterDateUtils.formatDispDate(anCertInfo.getValidDate()));
            builder.addParam("phone", anCertInfo.getContPhone());
            builder.addParam("password", anPassword);

            builder.addReceiveEmail(anCertInfo.getEmail());
            builder.addReceiveMobile(anCertInfo.getContPhone());

            notificationSendService.sendNotification(builder.build());
        } else if (BetterStringUtils.equals("1", anPublishMode)) { // 发给当前操作员 站内消息
            final Builder builder = NotificationModel.newBuilder("证书颁发站内发送通知模板", platformCustomer, platformOperator);
            builder.addReceiver(platformCustomer.getCustNo(), platformOperator.getId());

            builder.addParam("custName", anCertInfo.getCustName());
            builder.addParam("token", anCertInfo.getToken());
            builder.addParam("createDate", BetterDateUtils.formatDispDate(anCertInfo.getCreateDate()));
            builder.addParam("validDate", BetterDateUtils.formatDispDate(anCertInfo.getValidDate()));
            builder.addParam("phone", anCertInfo.getContPhone());
            builder.addParam("password", anPassword);

            notificationSendService.sendNotification(builder.build());
        }
    }
}
