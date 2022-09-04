package com.example.application.persistence.entity;


import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "firms")
public class FirmEntity extends AbstractEntity {

    private String  name;
    private String  street;
    private String  postCode;
    private String  city;
    private String  state;
    private String  CIN;
    private String  VATIN;
    private String  phone;
    private String  mobile;
    private String  email;
    private boolean copyDefaultPatterns = true;


    @OneToMany(
            mappedBy = "firmEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final Set<CustomerEntity> customerEntities = new HashSet<>();

    @OneToMany(
            mappedBy = "firmEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private final Set<UserEntity> users = new HashSet<>();

    @OneToMany(
            mappedBy = "firmEntity",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private Set<OfferEntity> offers;

    @OneToMany(
            mappedBy = "firmEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    Set<PatternEntity> patterns = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "firm_pattern",
            joinColumns = @JoinColumn(name = "firm_id"),
            inverseJoinColumns = @JoinColumn(name = "pattern_id"))
    private Set<PatternEntity> defaultPatterns;

    @OneToOne(cascade = {CascadeType.ALL}, mappedBy = "firmEntity")
    private FirmSettingsEntity firmSettings;

    public Set<PatternEntity> getDefaultPatterns() {
        return defaultPatterns;
    }

    public void setDefaultPatterns(Set<PatternEntity> defaultPatterns) {
        this.defaultPatterns = defaultPatterns;
    }

    public void addFirmSettings(FirmSettingsEntity firmSettingsEntity) {
        this.firmSettings = firmSettingsEntity;
        firmSettingsEntity.setFirmEntity(this);
    }

    public void addOffer(OfferEntity offerEntity) {
        this.offers.add(offerEntity);
        offerEntity.setFirmEntity(this);
    }

    public void removeOffer(OfferEntity offerEntity) {
        this.offers.remove(offerEntity);
        offerEntity.setFirmEntity(null);
    }

    public void addPattern(PatternEntity patternEntity) {
        this.patterns.add(patternEntity);
        patternEntity.setFirmEntity(this);
    }

    public void removePattern(PatternEntity patternEntity) {
        this.patterns.remove(patternEntity);
        patternEntity.setFirmEntity(null);
    }

    public void addDefaultPattern(PatternEntity patternEntity) {
        this.defaultPatterns.add(patternEntity);
        patternEntity.setFirmEntity(this);
    }

    public void removeDefaultPattern(PatternEntity patternEntity) {
        this.defaultPatterns.remove(patternEntity);
        patternEntity.getFirmEntities().remove(this);
    }

    public Set<CustomerEntity> getCustomerEntities() {
        return customerEntities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCIN() {
        return CIN;
    }

    public void setCIN(String CIN) {
        this.CIN = CIN;
    }

    public String getVATIN() {
        return VATIN;
    }

    public void setVATIN(String VATIN) {
        this.VATIN = VATIN;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<UserEntity> getUsers() {
        return users;
    }

    public Set<OfferEntity> getOffers() {
        return offers;
    }

    public void setOffers(Set<OfferEntity> offers) {
        this.offers = offers;
    }

    public Set<PatternEntity> getPatterns() {
        return patterns;
    }

    public void setPatterns(Set<PatternEntity> patterns) {
        this.patterns = patterns;
    }

    public FirmSettingsEntity getFirmSettings() {
        return firmSettings;
    }

    public void setFirmSettings(FirmSettingsEntity firmSettings) {
        this.firmSettings = firmSettings;
    }

    public boolean isCopyDefaultPatterns() {
        return copyDefaultPatterns;
    }

    public void setCopyDefaultPatterns(boolean copyDefaultPatterns) {
        this.copyDefaultPatterns = copyDefaultPatterns;
    }
}
