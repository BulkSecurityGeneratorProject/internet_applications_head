package head.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import head.domain.enumeration.MODELTYPES;

/**
 * A Models.
 */
@Entity
@Table(name = "models")
public class Models implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "model_name", nullable = false)
    private String modelName;

    @Enumerated(EnumType.STRING)
    @Column(name = "model_type")
    private MODELTYPES modelType;

    @ManyToOne
    private GenericShoes genericShoes;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModelName() {
        return modelName;
    }

    public Models modelName(String modelName) {
        this.modelName = modelName;
        return this;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public MODELTYPES getModelType() {
        return modelType;
    }

    public Models modelType(MODELTYPES modelType) {
        this.modelType = modelType;
        return this;
    }

    public void setModelType(MODELTYPES modelType) {
        this.modelType = modelType;
    }

    public GenericShoes getGenericShoes() {
        return genericShoes;
    }

    public Models genericShoes(GenericShoes genericShoes) {
        this.genericShoes = genericShoes;
        return this;
    }

    public void setGenericShoes(GenericShoes genericShoes) {
        this.genericShoes = genericShoes;
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
        Models models = (Models) o;
        if (models.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), models.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Models{" +
            "id=" + getId() +
            ", modelName='" + getModelName() + "'" +
            ", modelType='" + getModelType() + "'" +
            "}";
    }
}
