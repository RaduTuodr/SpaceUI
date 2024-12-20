package com.example.spaceui.repository;

import com.example.spaceui.model.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE LOWER(CONCAT(e.name, ' ', e.description)) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    public Page<Event> search(String keyword, Pageable pageable);
}
