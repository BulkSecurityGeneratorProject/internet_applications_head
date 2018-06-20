package head.repository;

import head.domain.Models;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Models entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ModelsRepository extends JpaRepository<Models, Long> {

}
