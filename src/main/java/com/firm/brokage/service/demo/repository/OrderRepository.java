package com.firm.brokage.service.demo.repository;
import com.firm.brokage.service.demo.entities.Order;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>, JpaSpecificationExecutor<Order> { // Change UUID to Long

  List<Order> findAll();

  Optional<Order> findById(Long id);

  @Modifying
  @Transactional
  @Query("delete from Order o where o.id = ?1")
  void deleteById(Long id); // Change UUID to Long

  @Query("SELECT o FROM Order o WHERE " +
          "(:customerId IS NULL OR o.customer.id = :customerId) AND " +
          "(:startDate IS NULL OR o.createdAt >= :startDate) AND " +
          "(:endDate IS NULL OR o.createdAt <= :endDate)")
  List<Order> findOrdersByCriteria(@Param("customerId") Long customerId,
                                   @Param("startDate") LocalDate startDate,
                                   @Param("endDate") LocalDate endDate);
}
