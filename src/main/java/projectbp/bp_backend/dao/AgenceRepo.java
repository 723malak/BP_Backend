package projectbp.bp_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projectbp.bp_backend.bean.Agence;

@Repository
public interface AgenceRepo extends JpaRepository<Agence, Long> {
}
