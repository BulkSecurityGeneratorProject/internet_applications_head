package head.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Magazine.
 */
@Entity
@Table(name = "magazine")
public class Magazine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "location", nullable = false)
    private String location;

    @NotNull
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @ManyToOne
    private StorageShoes storageShoes;

    @OneToMany(mappedBy = "magazine")
    @JsonIgnore
    private Set<Employees> supervisors = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public Magazine location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public Magazine capacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public StorageShoes getStorageShoes() {
        return storageShoes;
    }

    public Magazine storageShoes(StorageShoes storageShoes) {
        this.storageShoes = storageShoes;
        return this;
    }

    public void setStorageShoes(StorageShoes storageShoes) {
        this.storageShoes = storageShoes;
    }

    public Set<Employees> getSupervisors() {
        return supervisors;
    }

    public Magazine supervisors(Set<Employees> employees) {
        this.supervisors = employees;
        return this;
    }

    public Magazine addSupervisor(Employees employees) {
        this.supervisors.add(employees);
        employees.setMagazine(this);
        return this;
    }

    public Magazine removeSupervisor(Employees employees) {
        this.supervisors.remove(employees);
        employees.setMagazine(null);
        return this;
    }

    public void setSupervisors(Set<Employees> employees) {
        this.supervisors = employees;
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
        Magazine magazine = (Magazine) o;
        if (magazine.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), magazine.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Magazine{" +
            "id=" + getId() +
            ", location='" + getLocation() + "'" +
            ", capacity=" + getCapacity() +
            "}";
    }
}
