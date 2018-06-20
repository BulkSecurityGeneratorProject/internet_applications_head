package head.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import head.domain.enumeration.SHOETYPES;

import head.domain.enumeration.SHOEBRANDS;

import head.domain.enumeration.SHOECOLORS;

/**
 * A GenericShoes.
 */
@Entity
@Table(name = "generic_shoes")
public class GenericShoes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "shoe_type")
    private SHOETYPES shoeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "brand")
    private SHOEBRANDS brand;

    @Enumerated(EnumType.STRING)
    @Column(name = "color")
    private SHOECOLORS color;

    @NotNull
    @Column(name = "jhi_size", nullable = false)
    private Integer size;

    @OneToMany(mappedBy = "genericShoes")
    @JsonIgnore
    private Set<Models> models = new HashSet<>();

    @ManyToOne
    private StorageShoes storageShoes;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public SHOETYPES getShoeType() {
        return shoeType;
    }

    public GenericShoes shoeType(SHOETYPES shoeType) {
        this.shoeType = shoeType;
        return this;
    }

    public void setShoeType(SHOETYPES shoeType) {
        this.shoeType = shoeType;
    }

    public SHOEBRANDS getBrand() {
        return brand;
    }

    public GenericShoes brand(SHOEBRANDS brand) {
        this.brand = brand;
        return this;
    }

    public void setBrand(SHOEBRANDS brand) {
        this.brand = brand;
    }

    public SHOECOLORS getColor() {
        return color;
    }

    public GenericShoes color(SHOECOLORS color) {
        this.color = color;
        return this;
    }

    public void setColor(SHOECOLORS color) {
        this.color = color;
    }

    public Integer getSize() {
        return size;
    }

    public GenericShoes size(Integer size) {
        this.size = size;
        return this;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Set<Models> getModels() {
        return models;
    }

    public GenericShoes models(Set<Models> models) {
        this.models = models;
        return this;
    }

    public GenericShoes addModel(Models models) {
        this.models.add(models);
        models.setGenericShoes(this);
        return this;
    }

    public GenericShoes removeModel(Models models) {
        this.models.remove(models);
        models.setGenericShoes(null);
        return this;
    }

    public void setModels(Set<Models> models) {
        this.models = models;
    }

    public StorageShoes getStorageShoes() {
        return storageShoes;
    }

    public GenericShoes storageShoes(StorageShoes storageShoes) {
        this.storageShoes = storageShoes;
        return this;
    }

    public void setStorageShoes(StorageShoes storageShoes) {
        this.storageShoes = storageShoes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GenericShoes genericShoes = (GenericShoes) o;
        if (genericShoes.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), genericShoes.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "GenericShoes{" +
            "id=" + getId() +
            ", shoeType='" + getShoeType() + "'" +
            ", brand='" + getBrand() + "'" +
            ", color='" + getColor() + "'" +
            ", size=" + getSize() +
            "}";
    }
}
