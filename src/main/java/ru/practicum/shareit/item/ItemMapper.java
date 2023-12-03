package ru.practicum.shareit.item;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                null,
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                null
        );
    }

    public static Item toItem(ItemDtoForRequest itemDtoForRequest) {
        return new Item(
                itemDtoForRequest.getId(),
                null,
                itemDtoForRequest.getName(),
                itemDtoForRequest.getDescription(),
                itemDtoForRequest.getAvailable(),
                null
        );
    }

    public static ItemBookingDto toItemBookingDto(Item item) {
        return new ItemBookingDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest(),
                null,
                null,
                null
        );
    }

    public static ItemDtoForRequest toItemDtoForRequest(Item item) {
        return new ItemDtoForRequest(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequestIdOrNull()
        );
    }
}