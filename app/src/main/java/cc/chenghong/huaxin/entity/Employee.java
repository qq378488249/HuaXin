package cc.chenghong.huaxin.entity;

import java.io.Serializable;

/** 医生实体类
 * Created by hcl on 2016/3/28.
 */
public class Employee implements Serializable {
    private String id;

    private String account;

    private String password;

    private String nickname;

    private String mobile;

    private String locked;

    private String created;

    private String specialty;

    private String subject;

    private String head_url;

    private String is_expert;

    private String is_doctor;

    private String organization_id;

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setAccount(String account){
        this.account = account;
    }
    public String getAccount(){
        return this.account;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return this.password;
    }
    public void setNickname(String nickname){
        this.nickname = nickname;
    }
    public String getNickname(){
        return this.nickname;
    }
    public void setMobile(String mobile){
        this.mobile = mobile;
    }
    public String getMobile(){
        return this.mobile;
    }
    public void setLocked(String locked){
        this.locked = locked;
    }
    public String getLocked(){
        return this.locked;
    }
    public void setCreated(String created){
        this.created = created;
    }
    public String getCreated(){
        return this.created;
    }
    public void setSpecialty(String specialty){
        this.specialty = specialty;
    }
    public String getSpecialty(){
        return this.specialty;
    }
    public void setSubject(String subject){
        this.subject = subject;
    }
    public String getSubject(){
        return this.subject;
    }
    public void setHead_url(String head_url){
        this.head_url = head_url;
    }
    public String getHead_url(){
        return this.head_url;
    }
    public void setIs_expert(String is_expert){
        this.is_expert = is_expert;
    }
    public String getIs_expert(){
        return this.is_expert;
    }
    public void setIs_doctor(String is_doctor){
        this.is_doctor = is_doctor;
    }
    public String getIs_doctor(){
        return this.is_doctor;
    }
    public void setOrganization_id(String organization_id){
        this.organization_id = organization_id;
    }
    public String getOrganization_id(){
        return this.organization_id;
    }

}
