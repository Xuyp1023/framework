package com.betterjr.modules.wechat.data.message;

import java.util.List;

import com.betterjr.common.utils.Collections3;
import com.betterjr.modules.wechat.data.event.BasicEvent;

/**
 * 多图文消息
 * 
 * @author zhoucy
 */
public class NewsMsg extends BasicMsg {

    /**
     * 图文消息个数,限制为10条以内
     */
    private int count;
    /**
     * 多条图文消息信息,默认第一个item为大图,
     * </p>
     * 注意:如果图文数超过10,则将会无响应
     */
    private List<Article> articles;

    public NewsMsg() {
        super();
        this.msgType = "news";
    }

    public NewsMsg(BasicEvent event) {
        super(event);
        this.msgType = "news";
    }

    public NewsMsg(BasicMsg msg) {
        super(msg);
        this.msgType = "news";
    }

    public int getCount() {
        return count;
    }

    protected void setCount(int count) {
        this.count = count;
    }

    public List<Article> getArticles() {
        if (! Collections3.isEmpty(articles) && articles.size() > 10) {
            this.articles = articles.subList(0, 10);
            setCount(10);
        }
        else {
            this.setCount(articles.size());
        }
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    @Override
    public String toString() {
        return "NewsMsg [toUserName="
               + toUserName
               + ", fromUserName="
               + fromUserName
               + ", createTime="
               + createTime
               + ", msgType="
               + msgType
               + ", msgId="
               + msgId
               + ", count="
               + count
               + ", articles="
               + articles
               + "]";
    }
}
