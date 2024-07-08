package projectbp.bp_backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import projectbp.bp_backend.bean.Demande;
import projectbp.bp_backend.bean.User;

import java.util.List;
import java.util.Optional;

public interface DemandeRepo extends JpaRepository<Demande, Long> {


    List<Demande> findBySender(User sender);


}
