package com.cnepay.android.swiper.bean;

/**
 * Created by wjl on 2017/5/15.
 */

public class MessageContentBean {

    String businessType; //消息所属业务(0：理财, 1：vcPos, 2：Pos)
    String content; //消息内容
    long createTime; //创建时间
    String hasLink;
    int isRead; //是否已阅读(0:"否", 1:"是")
    String linkAddress;
    String linkText;
    String newsId; //消息id
    int newsType; //消息类型(0：公告, 1：通知)
    long readTime;
    String title; //消息标题

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getHasLink() {
        return hasLink;
    }

    public void setHasLink(String hasLink) {
        this.hasLink = hasLink;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public String getLinkAddress() {
        return linkAddress;
    }

    public void setLinkAddress(String linkAddress) {
        this.linkAddress = linkAddress;
    }

    public String getLinkText() {
        return linkText;
    }

    public void setLinkText(String linkText) {
        this.linkText = linkText;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }

    public int getNewsType() {
        return newsType;
    }

    public void setNewsType(int newsType) {
        this.newsType = newsType;
    }

    public long getReadTime() {
        return readTime;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "MessageContentBean{" +
                "businessType='" + businessType + '\'' +
                ", content='" + content + '\'' +
                ", createTime=" + createTime +
                ", hasLink='" + hasLink + '\'' +
                ", isRead=" + isRead +
                ", linkAddress='" + linkAddress + '\'' +
                ", linkText='" + linkText + '\'' +
                ", newsId='" + newsId + '\'' +
                ", newsType='" + newsType + '\'' +
                ", readTime=" + readTime +
                ", title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MessageContentBean that = (MessageContentBean) o;

        return newsId.equals(that.newsId);

    }

    @Override
    public int hashCode() {
        return newsId.hashCode();
    }
}
