package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {

    List<ItemRequest> findByRequestorId(int userId);

    Page<ItemRequest> findByRequestorIdNot(int userId, Pageable pageable);

}
