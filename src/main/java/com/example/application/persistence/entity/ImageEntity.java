package com.example.application.persistence.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "images")
public class ImageEntity extends AbstractEntity {
    private String name;
    private String description;
    @Lob
    private byte[] data;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.MERGE
    })
    @JoinTable(
            name = "image_pattern",
            joinColumns = @JoinColumn(name = "image_id"),
            inverseJoinColumns = @JoinColumn(name = "pattern_id"))
    private Set<PatternEntity> imagePatterns = new HashSet<>();

    public Set<PatternEntity> getImagePatterns() {
        return imagePatterns;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImagePatterns(Set<PatternEntity> imagePatterns) {
        this.imagePatterns = imagePatterns;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}
