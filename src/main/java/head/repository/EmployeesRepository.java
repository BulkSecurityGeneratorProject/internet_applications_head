package head.repository;

import head.domain.Employees;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;

/**
 * Spring Data JPA repository for the Employees entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmployeesRepository extends JpaRepository<Employees, Long> {
    @Query("select distinct employees from Employees employees left join fetch employees.roles")
    List<Employees> findAllWithEagerRelationships();

    @Query("select employees from Employees employees left join fetch employees.roles where employees.id =:id")
    Employees findOneWithEagerRelationships(@Param("id") Long id);

}
