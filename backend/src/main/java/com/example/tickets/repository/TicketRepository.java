package com.example.tickets.repository;

import com.example.tickets.domain.Ticket;
import com.example.tickets.domain.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    List<Ticket> findByStatusOrderByUpdatedAtDesc(TicketStatus status);

    @Query("""
            SELECT t FROM Ticket t
            WHERE (:status IS NULL OR t.status = :status)
            AND (
                :q IS NULL OR :q = '' OR
                LOWER(t.title) LIKE LOWER(CONCAT('%', :q, '%')) OR
                LOWER(COALESCE(t.description, '')) LIKE LOWER(CONCAT('%', :q, '%'))
            )
            ORDER BY t.updatedAt DESC
            """)
    List<Ticket> search(@Param("status") TicketStatus status, @Param("q") String q);
}