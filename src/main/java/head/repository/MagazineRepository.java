package head.repository;

import head.domain.Magazine;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the Magazine entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MagazineRepository extends JpaRepository<Magazine, Long> {

}
