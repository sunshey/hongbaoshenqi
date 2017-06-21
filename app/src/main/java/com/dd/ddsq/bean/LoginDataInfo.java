package com.dd.ddsq.bean;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;


/**
 * Created by zhangkai on 2017/2/21.
 */

public class LoginDataInfo {
    @JSONField(name = "user_info")
    private VipInfo vipInfo;

    @JSONField(name = "share_info")
    private ShareInfo shareInfo;

    @JSONField(name = "pub_key")
    private String publicKey;

    @JSONField(name = "contact_info")
    private ContactInfo contactInfo;

    @JSONField(name = "payway_list")
    private List<PayWayInfo> payWayInfos;

    @JSONField(name = "hongbao_count")
    private HongbaoCountInfo countInfo;

    @JSONField(name = "user_vip_list")
    private List<UserVipInfo> vipListInfo;

    @JSONField(name = "update")
    private UpdateInfo updateInfo;

    @JSONField(name = "hongbao_notice_list")
    private List<String> hongbaoNoticeInfos;

    private String qq;


    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public ShareInfo getShareInfo() {
        return shareInfo;
    }

    public void setShareInfo(ShareInfo shareInfo) {
        this.shareInfo = shareInfo;
    }

    public VipInfo getVipInfo() {
        return vipInfo;
    }

    public void setVipInfo(VipInfo vipInfo) {
        this.vipInfo = vipInfo;
    }

    public List<PayWayInfo> getPayWayInfos() {
        return payWayInfos;
    }

    public void setPayWayInfos(List<PayWayInfo> payWayInfos) {
        this.payWayInfos = payWayInfos;
    }

    public HongbaoCountInfo getCountInfo() {
        return countInfo;
    }

    public void setCountInfo(HongbaoCountInfo countInfo) {
        this.countInfo = countInfo;
    }

    public List<UserVipInfo> getVipListInfo() {
        return vipListInfo;
    }

    public void setVipListInfo(List<UserVipInfo> vipListInfo) {
        this.vipListInfo = vipListInfo;
    }

    public UpdateInfo getUpdateInfo() {
        return updateInfo;
    }

    public void setUpdateInfo(UpdateInfo updateInfo) {
        this.updateInfo = updateInfo;
    }

    public List<String> getHongbaoNoticeInfos() {
        return hongbaoNoticeInfos;
    }

    public void setHongbaoNoticeInfos(List<String> hongbaoNoticeInfos) {
        this.hongbaoNoticeInfos = hongbaoNoticeInfos;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }
}
