package com.betterjr.common.utils;

import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import com.betterjr.common.data.NotificationAttachment;
import com.sun.mail.util.MailSSLSocketFactory;

/**
 *
 * @author liuwl
 *
 */
public class MailUtils {
    private static final String charSet = "utf-8";

    private static String protocol = "smtp";
    private static String from = "admin@qiejf.com";
    private static String host = "127.0.0.1";
    private static String port = "25";
    private static String username = "admin@qiejf.com";
    private static String password = "admin@qiejf";

    public MailUtils(final String form, final String host, final String port, final String username, final String password) {
        MailUtils.from = form;
        MailUtils.host = host;
        MailUtils.port = port;
        MailUtils.username = username;
        MailUtils.password = password;
    }

    public static MimeMessage createMessage(final Session anSession, final String anSubject, final String anContent, final Collection<NotificationAttachment> anAttachments) {
        try {
            final MimeMessage mimeMessage = new MimeMessage(anSession);
            mimeMessage.setFrom(new InternetAddress(from));

            mimeMessage.setSubject(MimeUtility.encodeText(anSubject, "utf-8", "B"));

            final Multipart multipart = new MimeMultipart();

            final MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(anContent, "text/html;charset=utf-8");

            multipart.addBodyPart(mimeBodyPart);

            if (Collections3.isEmpty(anAttachments) == false) {
                for (final NotificationAttachment attachment : anAttachments) {
                    final FileDataSource fileDataSource = new FileDataSource(attachment.getFilePath());
                    final MimeBodyPart attachmentBodyPart = new MimeBodyPart();
                    attachmentBodyPart.setDataHandler(new DataHandler(fileDataSource));
                    attachmentBodyPart.setFileName(attachment.getFileName());
                    multipart.addBodyPart(attachmentBodyPart);
                }
            }

            mimeMessage.setContent(multipart);
            mimeMessage.setSentDate(new Date());
            mimeMessage.saveChanges();

            return mimeMessage;
        }
        catch (final Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Session createSession() {
        final Properties props = new Properties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        // 开启安全协议
        MailSSLSocketFactory sf = null;
        try {
            sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
        }
        catch (final GeneralSecurityException e1) {
            e1.printStackTrace();
        }
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.ssl.socketFactory", sf);
        final Session session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        return session;
    }

    public static boolean sendMail(final Session anSession, final MimeMessage anMimeMessage, final String anTo) {
        try {
            anMimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(anTo));
            anMimeMessage.saveChanges();

            final Transport transport = anSession.getTransport(protocol);
            transport.connect(host, username, password);
            transport.sendMessage(anMimeMessage, anMimeMessage.getAllRecipients());
            transport.close();
        }
        catch (final Exception mex) {
            mex.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 发送邮件
     */
    public static boolean sendMail(final String anTo, final String anSubject, final String anContent, final Collection<NotificationAttachment> anAttachments) {
        final Session session = createSession();

        try {
            final MimeMessage mimeMessage = createMessage(session, anSubject, anContent, anAttachments);
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(anTo));
            mimeMessage.saveChanges();

            final Transport transport = session.getTransport(protocol);
            transport.connect(host, username, password);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();
        }
        catch (final Exception mex) {
            mex.printStackTrace();
            return false;
        }
        return true;
    }

}