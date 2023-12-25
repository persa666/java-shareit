package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.dto.ItemDtoForRequest;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Validated
public class ItemControllerGateway {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestBody @Valid ItemDtoForRequest itemDtoForRequest,
                                             @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemClient.createItem(userId, itemDtoForRequest);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> replaceItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id")
    int userId, @PathVariable int itemId) {
        return itemClient.replaceItem(userId, itemDto, itemId);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int itemId) {
        return itemClient.getItemById(userId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getItemsOwnerById(@RequestHeader("X-Sharer-User-Id") int userId,
                                                    @RequestParam(name = "from", defaultValue = "0")
                                                    @PositiveOrZero int from,
                                                    @RequestParam(name = "size", defaultValue = "30")
                                                    @Positive int size) {
        return itemClient.getItemsOwnerById(userId, from, size);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getItemBySearch(@RequestHeader("X-Sharer-User-Id") int userId,
                                                  @RequestParam String text,
                                                  @RequestParam(name = "from", defaultValue = "0")
                                                  @PositiveOrZero int from,
                                                  @RequestParam(name = "size", defaultValue = "30")
                                                  @Positive int size) {
        return itemClient.getItemBySearch(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                                @RequestBody @Valid CommentDto commentDto, @PathVariable int itemId) {
        return itemClient.createComment(userId, commentDto, itemId);
    }
}
