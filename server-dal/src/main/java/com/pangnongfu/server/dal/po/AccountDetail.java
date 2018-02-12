package com.pangnongfu.server.dal.po;

import org.hibernate.annotations.Cascade;

import javax.persistence.*;

/**
 * Created by xinghai on 2015/9/20.
 */
@Entity(name="account_detail")
@Table(name="tb_account_detail")
public class AccountDetail {

    public static final int GENDER_MALE=0;
    public static final int GENDER_FEMALE=1;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name="phone")
    private String phone;
    @Column(name="nickname")
    private String nickname;
    @Column(name="avatar_url")
    private String avatar_url;
    @Column(name="gender")
    private int gender;
    @Column(name="district")
    private String district;
    @Column(name="city")
    private String city;
    @Column(name="about")
    private String about;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="account_id")
    private Account account;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountDetail that = (AccountDetail) o;

        if (id != that.id) return false;
        if (gender != that.gender) return false;
        if (phone != null ? !phone.equals(that.phone) : that.phone != null) return false;
        if (nickname != null ? !nickname.equals(that.nickname) : that.nickname != null) return false;
        if (avatar_url != null ? !avatar_url.equals(that.avatar_url) : that.avatar_url != null) return false;
        if (district != null ? !district.equals(that.district) : that.district != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (about != null ? !about.equals(that.about) : that.about != null) return false;
        return !(account != null ? !account.equals(that.account) : that.account != null);

    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (phone != null ? phone.hashCode() : 0);
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + (avatar_url != null ? avatar_url.hashCode() : 0);
        result = 31 * result + gender;
        result = 31 * result + (district != null ? district.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + (about != null ? about.hashCode() : 0);
        return result;
    }

}
