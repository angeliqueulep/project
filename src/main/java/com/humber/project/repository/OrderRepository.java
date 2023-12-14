package com.humber.project.repository;

import com.humber.project.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer>{
    List<Order> findByStatusIsNotNull();
}
