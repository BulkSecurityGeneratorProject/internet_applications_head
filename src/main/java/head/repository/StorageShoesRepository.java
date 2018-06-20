package head.repository;

import head.domain.StorageShoes;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;


/**
 * Spring Data JPA repository for the StorageShoes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StorageShoesRepository extends JpaRepository<StorageShoes, Long> {

}
