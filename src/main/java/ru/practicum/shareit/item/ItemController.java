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
    public ItemDtoForRequest createItem(@RequestBody @Valid ItemDtoForRequest itemDtoForRequest,
                                        @RequestHeader("X-Sharer-User-Id") int userId) {
        return itemService.createItem(itemDtoForRequest, userId);
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
    public List<ItemBookingDto> getItemsOwnerById(@RequestHeader("X-Sharer-User-Id") int userId,
                                                  @RequestParam(name = "from", defaultValue = "0") int from,
                                                  @RequestParam(name = "size", defaultValue = "30") int size) {
        return itemService.getItemsOwnerById(userId, from, size);
    }

    @GetMapping("/search")
    public List<ItemDto> getItemBySearch(@RequestHeader("X-Sharer-User-Id") int userId, @RequestParam String text,
                                         @RequestParam(name = "from", defaultValue = "0") int from,
                                         @RequestParam(name = "size", defaultValue = "30") int size) {
        return itemService.getItemBySearch(userId, text, from, size);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDtoForSend createComment(@RequestHeader("X-Sharer-User-Id") int userId,
                                           @RequestBody @Valid CommentDto commentDto, @PathVariable int itemId) {
        return itemService.createComment(userId, commentDto, itemId);
    }
}
