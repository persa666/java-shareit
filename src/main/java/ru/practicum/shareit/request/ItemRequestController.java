package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForSend;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItem;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
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
    public ItemRequestDtoForSend createRequest(@RequestHeader("X-Sharer-User-Id") @Positive int userId,
                                               @RequestBody @Valid ItemRequestDto itemRequestDto) {
        return itemRequestService.createRequest(userId, itemRequestDto);
    }

    @GetMapping
    public List<ItemRequestDtoWithItem> getAllRequestsUser(@RequestHeader("X-Sharer-User-Id") @Positive int userId) {
        return itemRequestService.getAllRequestsUser(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestDtoWithItem> getAllRequestOtherUsers(@RequestHeader("X-Sharer-User-Id") @Positive int userId,
                                                                @RequestParam(name = "from", defaultValue = "0") int from,
                                                                @RequestParam(name = "size", defaultValue = "30") int size) {
        return itemRequestService.getAllRequestOtherUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoWithItem getRequestById(@RequestHeader("X-Sharer-User-Id") @Positive int userId,
                                                 @PathVariable(name = "requestId") @Positive int requestId) {
        return itemRequestService.getRequestById(userId, requestId);
    }
}
