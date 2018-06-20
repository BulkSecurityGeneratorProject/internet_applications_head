package head.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A StorageShoes.
 */
@Entity
@Table(name = "storage_shoes")
public class StorageShoes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "amount", nullable = false)
    private Integer amount;

    @OneToMany(mappedBy = "storageShoes")
    @JsonIgnore
    private Set<GenericShoes> shoes = new HashSet<>();

    @OneToMany(mappedBy = "storageShoes")
    @JsonIgnore
    private Set<Magazine> magazines = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public StorageShoes amount(Integer amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Set<GenericShoes> getShoes() {
        return shoes;
    }

    public StorageShoes shoes(Set<GenericShoes> genericShoes) {
        this.shoes = genericShoes;
        return this;
    }

    public StorageShoes addShoe(GenericShoes genericShoes) {
        this.shoes.add(genericShoes);
        genericShoes.setStorageShoes(this);
        return this;
    }

    public StorageShoes removeShoe(GenericShoes genericShoes) {
        this.shoes.remove(genericShoes);
        genericShoes.setStorageShoes(null);
        return this;
    }

    public void setShoes(Set<GenericShoes> genericShoes) {
        this.shoes = genericShoes;
    }

    public Set<Magazine> getMagazines() {
        return magazines;
    }

    public StorageShoes magazines(Set<Magazine> magazines) {
        this.magazines = magazines;
        return this;
    }

    public StorageShoes addMagazine(Magazine magazine) {
        this.magazines.add(magazine);
        magazine.setStorageShoes(this);
        return this;
    }

    public StorageShoes removeMagazine(Magazine magazine) {
        this.magazines.remove(magazine);
        magazine.setStorageShoes(null);
        return this;
    }

    public void setMagazines(Set<Magazine> magazines) {
        this.magazines = magazines;
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
        StorageShoes storageShoes = (StorageShoes) o;
        if (storageShoes.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), storageShoes.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StorageShoes{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            "}";
    }
}
