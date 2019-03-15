package cn.edu.swufe.bean;

import java.io.Serializable;

public class Email implements Serializable {
    private String from;
    private String subject;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String sentdata;
    private String content;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSentdata() {
        return sentdata;
    }

    public void setSentdata(String sentdata) {
        this.sentdata = sentdata;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
