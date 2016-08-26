package com.betterjr.common.utils;

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

import org.apache.commons.lang3.tuple.Pair;

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

    public static String getProtocol() {
        return protocol;
    }

    public void setProtocol(String anProtocol) {
        protocol = anProtocol;
    }

    public static String getFrom() {
        return from;
    }

    public void setFrom(String anFrom) {
        from = anFrom;
    }

    public static String getHost() {
        return host;
    }

    public void setHost(String anHost) {
        host = anHost;
    }

    public static String getPort() {
        return port;
    }

    public void setPort(String anPort) {
        port = anPort;
    }

    public static String getUsername() {
        return username;
    }

    public void setUsername(String anUsername) {
        username = anUsername;
    }

    public static String getPassword() {
        return password;
    }

    public void setPassword(String anPassword) {
        password = anPassword;
    }

    public MailUtils() {
    }

    public static MimeMessage createMessage(Session anSession, 
            String anSubject, 
            String anContent, Collection<Pair<String, String>> anAttachments) {
        try {
            MimeMessage mimeMessage = new MimeMessage(anSession);
            mimeMessage.setFrom(new InternetAddress(from));

            mimeMessage.setSubject(MimeUtility.encodeText(anSubject, "utf-8", "B"));

            Multipart multipart = new MimeMultipart();

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(anContent, "text/html;charset=utf-8");

            multipart.addBodyPart(mimeBodyPart);

            for (Pair<String, String> attachment: anAttachments) {
                FileDataSource fileDataSource = new FileDataSource(attachment.getRight());
                MimeBodyPart attachmentBodyPart = new MimeBodyPart(); 
                attachmentBodyPart.setDataHandler(new DataHandler(fileDataSource));
                attachmentBodyPart.setFileName(attachment.getLeft());
                multipart.addBodyPart(attachmentBodyPart);
            }
            // 向Multipart添加附件
            /*
              Enumeration efile = file.elements(); 
             while (efile.hasMoreElements()) { 
             
             filename =efile.nextElement().toString(); 
             FileDataSource fds = new FileDataSource(filename); 
             mbpFile.setDataHandler(new DataHandler(fds)); // 这个方法可以解决附件乱码问题。 
             // String filename = new String(fds.getName().getBytes(), "ISO-8859-1");
               mbpFile.setFileName(filename); 
               // 向MimeMessage添加（Multipart代表附件） 
              mp.addBodyPart(mbpFile);
             
              } 
              file.removeAllElements();
             */

            mimeMessage.setContent(multipart);
            mimeMessage.setSentDate(new Date());
            mimeMessage.saveChanges();

            return mimeMessage;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static Session createSession() {
        Properties props = new Properties();
        props.put("mail.transport.protocol", protocol);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props, new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        return session;
    }

    public static boolean sendMail(Session anSession, MimeMessage anMimeMessage, String anTo) {
        try {
            anMimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(anTo));
            anMimeMessage.saveChanges();

            Transport transport = anSession.getTransport(protocol);
            transport.connect(host, username, password);
            transport.sendMessage(anMimeMessage, anMimeMessage.getAllRecipients());
            transport.close();
        }
        catch (Exception mex) {
            mex.printStackTrace();
            return false;
        }
        return true;
    }
    
    /**
     * 发送邮件
     */
    public static boolean sendMail(String anTo, String anSubject, String anContent, Collection<Pair<String, String>> anAttachments) {
        Session session = createSession();

        try {
            MimeMessage mimeMessage = createMessage(session, anSubject, anContent, anAttachments);
            mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(anTo));
            mimeMessage.saveChanges();

            Transport transport = session.getTransport(protocol);
            transport.connect(host, username, password);
            transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
            transport.close();
        }
        catch (Exception mex) {
            mex.printStackTrace();
            return false;
        }
        return true;
    }

}