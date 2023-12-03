package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingForItemDTO;
import ru.practicum.shareit.exp.*;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;

    @Override
    public ItemDtoForRequest createItem(ItemDtoForRequest itemDtoForRequest, int userId) {
        Item item = ItemMapper.toItem(itemDtoForRequest);
        item.setOwner(userRepository.findById(userId).orElseThrow(() ->
                new NonExistentItemException("Вещь с таким id не найден.")));
        if (itemDtoForRequest.getRequestId() != null) {
            item.setRequest(itemRequestRepository.findById(itemDtoForRequest.getRequestId())
                    .orElseThrow(() -> new NonExistentItemRequestException("Запроса с таким id нет")));
        }
        return ItemMapper.toItemDtoForRequest(itemRepository.save(item));
    }

    @Override
    public ItemDto replaceItem(ItemDto itemDto, int userId, int itemId) {
        try {
            Item item = itemRepository.findById(itemId).orElseThrow(() ->
                    new NonExistentUserException("Пользователь с таким id не найден."));
            if (item.getOwner().getId() != userId) {
                throw new NonExistentUserException("Пользователь не является владельцем вещи.");
            }
            if (itemDto.getAvailable() != null) {
                item.setAvailable(itemDto.getAvailable());
            }
            if (itemDto.getDescription() != null) {
                item.setDescription(itemDto.getDescription());
            }
            if (itemDto.getName() != null) {
                item.setName(itemDto.getName());
            }
            return ItemMapper.toItemDto(itemRepository.save(item));

        } catch (RuntimeException e) {
            throw new NonExistentItemException("Вещь с таким id не найдена.");
        }
    }

    @Override
    public ItemBookingDto getItemById(int userId, int itemId) {
        try {
            ItemBookingDto itemBookingDto = ItemMapper.toItemBookingDto(itemRepository.findById(itemId)
                    .orElseThrow(() -> new NonExistentUserException("Вещь с таким id не найден.")));
            Booking lastBooking = bookingRepository
                    .findFirstByItemIdAndItemOwnerIdAndStartBeforeAndStatusOrderByEndDesc(itemBookingDto.getId(),
                            userId, LocalDateTime.now(), Status.APPROVED);
            if (lastBooking != null) {
                itemBookingDto.setLastBooking(new BookingForItemDTO(lastBooking.getId(),
                        lastBooking.getBooker().getId()));
            }
            Booking nextBooking = bookingRepository
                    .findFirstByItemIdAndItemOwnerIdAndStartAfterAndStatusOrderByStartAsc(itemBookingDto.getId(),
                            userId, LocalDateTime.now(), Status.APPROVED);
            if (nextBooking != null) {
                itemBookingDto.setNextBooking(new BookingForItemDTO(nextBooking.getId(),
                        nextBooking.getBooker().getId()));
            }
            itemBookingDto.setComments(commentRepository.findByItemId(itemId)
                    .stream()
                    .map(CommentMapper::toCommentDtoForSend)
                    .collect(Collectors.toList()));
            return itemBookingDto;
        } catch (RuntimeException e) {
            throw new NonExistentItemException("Вещь с таким id не найдена.");
        }
    }

    @Override
    public List<ItemBookingDto> getItemsOwnerById(int userId, int from, int size) {
        checkFromAndSize(from, size);
        return itemRepository.findByOwnerId(userId, PageRequest.of(from / size, size))
                .stream()
                .map(
                        item -> {
                            ItemBookingDto itemBookingDto = ItemMapper.toItemBookingDto(item);
                            Booking lastBooking = bookingRepository
                                    .findFirstByItemIdAndItemOwnerIdAndStartBeforeAndStatusOrderByEndDesc(
                                            itemBookingDto.getId(), userId, LocalDateTime.now(), Status.APPROVED);
                            if (lastBooking != null) {
                                itemBookingDto.setLastBooking(new BookingForItemDTO(lastBooking.getId(),
                                        lastBooking.getBooker().getId()));
                            }
                            Booking nextBooking = bookingRepository
                                    .findFirstByItemIdAndItemOwnerIdAndStartAfterAndStatusOrderByStartAsc(
                                            itemBookingDto.getId(), userId, LocalDateTime.now(), Status.APPROVED);
                            if (nextBooking != null) {
                                itemBookingDto.setNextBooking(new BookingForItemDTO(nextBooking.getId(),
                                        nextBooking.getBooker().getId()));
                            }
                            itemBookingDto.setComments(commentRepository.findByItemId(itemBookingDto.getId())
                                    .stream()
                                    .map(CommentMapper::toCommentDtoForSend)
                                    .collect(Collectors.toList()));
                            return itemBookingDto;
                        }
                )
                .sorted(Comparator.comparing(ItemBookingDto::getId))
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> getItemBySearch(int userId, String text, int from, int size) {
        checkFromAndSize(from, size);
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.search(text, PageRequest.of(from / size, size))
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDtoForSend createComment(int userId, CommentDto commentDto, int itemId) {
        if (bookingRepository.existsByItemIdAndBookerIdAndEndBeforeAndStatus(userId, itemId, LocalDateTime.now(),
                Status.APPROVED) == 0) {
            throw new CommentException("Пользователь не брал вещь в аренду или срок аренды еще не прошел.");
        }
        commentRepository.save(new Comment(0, commentDto.getText(), itemRepository.findById(itemId)
                .orElseThrow(() -> new NonExistentItemException("Вещь с таким id не найдена.")), userRepository
                .findById(userId)
                .orElseThrow(() -> new NonExistentUserException("Пользователь с таким id не найден.")),
                LocalDateTime.now()));
        return CommentMapper.toCommentDtoForSend(commentRepository.findByAuthorNameIdAndItemId(userId, itemId));
    }

    private void checkFromAndSize(int from, int size) {
        if (from < 0 || size <= 0) {
            throw new CountsException("Параметр(ы) неудовлетворяют условиям пагинации.");
        }
    }
}