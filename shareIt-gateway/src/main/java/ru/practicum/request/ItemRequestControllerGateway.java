package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Validated
public class ItemRequestControllerGateway {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader("X-Sharer-User-Id") @Positive int userId,
                                                @RequestBody @Valid ItemRequestDto itemRequestDto) {
        return itemRequestClient.createRequest(userId, itemRequestDto);
    }

    @GetMapping
    public ResponseEntity<Object> getAllRequestsUser(@RequestHeader("X-Sharer-User-Id") @Positive int userId) {
        return itemRequestClient.getAllRequestsUser(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequestOtherUsers(@RequestHeader("X-Sharer-User-Id") @Positive int userId,
                                                          @RequestParam(name = "from", defaultValue = "0")
                                                          @PositiveOrZero int from,
                                                          @RequestParam(name = "size", defaultValue = "30")
                                                          @Positive int size) {
        return itemRequestClient.getAllRequestOtherUsers(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") @Positive int userId,
                                                 @PathVariable(name = "requestId") @Positive int requestId) {
        return itemRequestClient.getRequestById(userId, requestId);
    }
}
