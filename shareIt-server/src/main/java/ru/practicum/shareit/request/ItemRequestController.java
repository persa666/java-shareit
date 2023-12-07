package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForSend;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItem;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ItemRequestDtoForSend createRequest(@RequestHeader("X-Sharer-User-Id") int userId,
                                               @RequestBody ItemRequestDto itemRequestDto) {
        return itemRequestService.createRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDtoWithItem> getAllRequestsUser(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemRequestService.getAllRequestsUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoWithItem> getAllRequestOtherUsers(@RequestHeader("X-Sharer-User-Id") int userId,
                                                                @RequestParam int from, @RequestParam int size) {
        return itemRequestService.getAllRequestOtherUsers(userId, PageRequest.of(from / size, size,
                Sort.by("created")));
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoWithItem getRequestById(@RequestHeader("X-Sharer-User-Id") int userId,
                                                 @PathVariable(name = "requestId") int requestId) {
        return itemRequestService.getRequestById(userId, requestId);
    }
}
