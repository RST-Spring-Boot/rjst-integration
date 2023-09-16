package de.rjst.rjstintegration;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    List<UserEntity> findByStatusAndFlowType(Integer status, FlowType flowType);



}