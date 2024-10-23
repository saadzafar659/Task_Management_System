package tms.task_management_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tms.task_management_system.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

}
