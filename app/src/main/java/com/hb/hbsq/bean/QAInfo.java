package com.hb.hbsq.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by zhangkai on 2017/2/18.
 */

public class QAInfo {


    private int qid;

    @JSONField(name = "title")
    private String question;

    @JSONField(name = "body")
    private String answer;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;

    public int getQid() {
        return qid;
    }

    public void setQid(int qid) {
        this.qid = qid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "QAInfo{" +
                "qid=" + qid +
                ", question='" + question + '\'' +
                ", answer='" + answer + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
