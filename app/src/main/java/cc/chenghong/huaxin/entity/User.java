package cc.chenghong.huaxin.entity;

import java.io.Serializable;

import cc.chenghong.huaxin.response.ObjectResponse;

/**
 * Created by Administrator on 2016/3/16.
 */
public class User extends ObjectResponse<User>{

    private String id;

    private String account;

    private String mobile;

    private String nick_name;

    private String email;

    private String password;

    private String gender;

    private String head_url;

    private String locked;

    private String created;

    private String age;

    private String weight;

    private String xy;

    private String xt;

    private String tz;

    private String height;

    private String jgid;

    private String organization_id;

    private Organization organization;

    private String habit;

    private String urgent_mobile;

    private String labour;

    private String card;

    private String equipment;

    public String getHabit() {
        return habit;
    }

    public void setHabit(String habit) {
        this.habit = habit;
    }

    public String getUrgent_mobile() {
        return urgent_mobile;
    }

    public void setUrgent_mobile(String urgent_mobile) {
        this.urgent_mobile = urgent_mobile;
    }

    public String getLabour() {
        return labour;
    }

    public void setLabour(String labour) {
        this.labour = labour;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

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
    public void setMobile(String mobile){
        this.mobile = mobile;
    }
    public String getMobile(){
        return this.mobile;
    }
    public void setNick_name(String nick_name){
        this.nick_name = nick_name;
    }
    public String getNick_name(){
        return this.nick_name;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return this.password;
    }
    public void setGender(String gender){
        this.gender = gender;
    }
    public String getGender(){
        return this.gender;
    }
    public void setHead_url(String head_url){
        this.head_url = head_url;
    }
    public String getHead_url(){
        return this.head_url;
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
    public void setAge(String age){
        this.age = age;
    }
    public String getAge(){
        return this.age;
    }
    public void setWeight(String weight){
        this.weight = weight;
    }
    public String getWeight(){
        return this.weight;
    }
    public void setXy(String xy){
        this.xy = xy;
    }
    public String getXy(){
        return this.xy;
    }
    public void setXt(String xt){
        this.xt = xt;
    }
    public String getXt(){
        return this.xt;
    }
    public void setTz(String tz){
        this.tz = tz;
    }
    public String getTz(){
        return this.tz;
    }
    public void setHeight(String height){
        this.height = height;
    }
    public String getHeight(){
        return this.height;
    }
    public void setJgid(String jgid){
        this.jgid = jgid;
    }
    public String getJgid(){
        return this.jgid;
    }
    public void setOrganization_id(String organization_id){
        this.organization_id = organization_id;
    }
    public String getOrganization_id(){
        return this.organization_id;
    }
    public void setOrganization(Organization organization){
        this.organization = organization;
    }
    public Organization getOrganization(){
        return this.organization;
    }
}
