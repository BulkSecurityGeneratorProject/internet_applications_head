package head.repository;

import head.domain.GenericShoes;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the GenericShoes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GenericShoesRepository extends JpaRepository<GenericShoes, Long> {

}
