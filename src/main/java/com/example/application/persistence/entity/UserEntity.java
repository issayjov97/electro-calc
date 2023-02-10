package com.example.application.persistence.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity extends AbstractEntity {
    private static Long oneTimePasswordDuration = 5 * 60 * 1000L; // 5 minutes
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private Boolean enabled = true;
    private String password;

    @ManyToOne
    private FirmEntity firmEntity;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    Set<AuthorityEntity> authorityEntities;

    @OneToOne(cascade = {CascadeType.ALL}, mappedBy = "userEntity", fetch = FetchType.LAZY)
    private OneTimePasswordEntity oneTimePassword;

    public FirmEntity getFirmEntity() {
        return firmEntity;
    }

    public void setFirmEntity(FirmEntity firmEntity) {
        this.firmEntity = firmEntity;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setAuthorityEntities(Set<AuthorityEntity> authorityEntities) {
        this.authorityEntities = authorityEntities;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public Set<AuthorityEntity> getAuthorityEntities() {
        return authorityEntities;
    }

    public OneTimePasswordEntity getOneTimePassword() {
        return oneTimePassword;
    }

    public void setOneTimePassword(OneTimePasswordEntity oneTimePassword) {
        this.oneTimePassword = oneTimePassword;
    }

    public boolean isOTPValid(String value) {
        if (this.oneTimePassword != null) {
            if (oneTimePassword.getCreatedAt().getTime() + (oneTimePasswordDuration) < System.currentTimeMillis())
                throw new RuntimeException("One time password has expired");
            else if (!oneTimePassword.getValue().equals(value))
                throw new RuntimeException("Wrong one time password value");
            return true;
        } else {
            throw new RuntimeException("One time password not found");
        }
    }
}
