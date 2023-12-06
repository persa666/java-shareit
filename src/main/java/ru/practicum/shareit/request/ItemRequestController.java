package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestDtoForSend;
import ru.practicum.shareit.request.dto.ItemRequestDtoWithItem;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Validated
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
                                                                @RequestParam(name = "from", defaultValue = "0")
                                                                @PositiveOrZero int from,
                                                                @RequestParam(name = "size", defaultValue = "30")
                                                                @Positive int size) {
        return itemRequestService.getAllRequestOtherUsers(userId, PageRequest.of(from / size, size,
                Sort.by("created")));
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoWithItem getRequestById(@RequestHeader("X-Sharer-User-Id") @Positive int userId,
                                                 @PathVariable(name = "requestId") @Positive int requestId) {
        return itemRequestService.getRequestById(userId, requestId);
    }
}
