package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestBody @Valid ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.createItem(itemDto, userId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto replaceItem(@RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int userId,
                               @PathVariable int itemId) {
        return itemService.replaceItem(itemDto, userId, itemId);
    }

    @GetMapping("/{itemId}")
    public ItemBookingDto getItemById(@RequestHeader("X-Sharer-User-Id") int userId, @PathVariable int itemId) {
        return itemService.getItemById(userId, itemId);
    }

    @GetMapping
    public List<ItemBookingDto> getItemsOwnerById(@RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.getItemsOwnerById(userId);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemBySearch(@RequestHeader("X-Sharer-User-Id") int userId, @RequestParam String text) {
        return itemService.getItemBySearch(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoForSend createComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                           @RequestBody @Valid CommentDto commentDto, @PathVariable int itemId) {
        return itemService.createComment(userId, commentDto, itemId);
    }
}
