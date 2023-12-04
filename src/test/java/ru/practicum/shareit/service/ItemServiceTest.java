package ru.practicum.shareit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exp.CommentException;
import ru.practicum.shareit.exp.NonExistentItemException;
import ru.practicum.shareit.item.*;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private ItemRequestRepository itemRequestRepository;

    @InjectMocks
    private ItemServiceImpl itemService;

    @Test
    void createItemTest() {
        ItemDtoForRequest itemDtoForRequest = new ItemDtoForRequest();
        itemDtoForRequest.setName("Test Item");
        itemDtoForRequest.setRequestId(1);
        int userId = 1;

        ItemRequest itemRequest = new ItemRequest();

        User user = new User();
        user.setId(userId);

        Item item = new Item();
        item.setId(1);
        item.setName("Test Item");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.save(any(Item.class))).thenReturn(item);
        when(itemRequestRepository.findById(any(Integer.class))).thenReturn(Optional.of(itemRequest));

        ItemDtoForRequest result = itemService.createItem(itemDtoForRequest, userId);

        assertNotNull(result);
        assertEquals(itemDtoForRequest.getName(), result.getName());

        verify(userRepository, times(1)).findById(userId);
        verify(itemRequestRepository, times(1)).findById(any(Integer.class));
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void createItemNotSuccessTest() {
        ItemDtoForRequest itemDtoForRequest = new ItemDtoForRequest();
        itemDtoForRequest.setName("Test Item");
        itemDtoForRequest.setRequestId(1);
        int userId = 1;

        User user = new User();
        user.setId(userId);

        Item item = new Item();
        item.setId(1);
        item.setName("Test Item");

        when(userRepository.findById(any(Integer.class)))
                .thenThrow(new NonExistentItemException("Вещь с таким id не найден."));

        assertThrows(NonExistentItemException.class, () -> itemService.createItem(itemDtoForRequest, userId));

        verify(userRepository, times(1)).findById(userId);
        verify(itemRequestRepository, times(0)).findById(any(Integer.class));
        verify(itemRepository, times(0)).save(any(Item.class));
    }

    @Test
    void replaceItemTest() {
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Updated Item");
        itemDto.setDescription("Updated Description");
        itemDto.setAvailable(false);

        int userId = 1;
        int itemId = 1;

        User user = new User();
        user.setId(userId);

        Item existingItem = new Item();
        existingItem.setId(itemId);
        existingItem.setName("Test Item");
        existingItem.setDescription("Test Description");
        existingItem.setAvailable(true);
        existingItem.setOwner(user);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));
        when(itemRepository.save(any(Item.class))).thenReturn(existingItem);

        ItemDto result = itemService.replaceItem(itemDto, userId, itemId);

        assertNotNull(result);
        assertEquals(itemDto.getName(), result.getName());
        assertEquals(itemDto.getDescription(), result.getDescription());
        assertEquals(itemDto.getAvailable(), result.getAvailable());

        verify(itemRepository, times(1)).findById(itemId);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void replaceItemWithNonExistentItemTest() {
        ItemDto itemDto = new ItemDto();
        int userId = 1;
        int itemId = 1;

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        assertThrows(NonExistentItemException.class, () -> itemService.replaceItem(itemDto, userId, itemId));

        verify(itemRepository, times(1)).findById(itemId);
        verify(itemRepository, times(0)).save(any(Item.class));
    }

    @Test
    void replaceItemWithNonExistentUserTest() {
        ItemDto itemDto = new ItemDto();
        int userId = 1;
        int itemId = 1;

        User user = new User();
        user.setId(2);

        Item existingItem = new Item();
        existingItem.setId(itemId);
        existingItem.setName("Test Item");
        existingItem.setDescription("Test Description");
        existingItem.setAvailable(true);
        existingItem.setOwner(user);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(existingItem));

        NonExistentItemException exception = assertThrows(NonExistentItemException.class,
                () -> itemService.replaceItem(itemDto, userId, itemId));

        assertEquals("Вещь с таким id не найдена.", exception.getMessage());
        verify(itemRepository, times(1)).findById(itemId);
        verify(itemRepository, times(0)).save(any(Item.class));
    }

    @Test
    void getItemByIdExceptionTest() {
        int userId = 1;
        int itemId = 1;

        when(itemRepository.findById(itemId)).thenReturn(Optional.empty());

        NonExistentItemException exception = assertThrows(NonExistentItemException.class,
                () -> itemService.getItemById(userId, itemId));

        assertEquals("Вещь с таким id не найдена.", exception.getMessage());

        verify(itemRepository, times(1)).findById(itemId);
    }

    @Test
    void getItemByIdSuccessTest() {
        int userId = 1;
        int itemId = 1;
        User user = new User(userId, "name", "email@mail.com");
        User booker = new User(2, "booker", "asd@mail.com");
        ItemDtoForRequest itemDto =
                new ItemDtoForRequest(1, "name", "desc", true, null);
        Item item = new Item(1, user, "name", "desc", true, null);
        Booking lastBooking = new Booking(1, LocalDateTime.of(2020, 10, 10, 10, 10),
                LocalDateTime.of(2020, 10, 10, 10, 15), item, booker, Status.APPROVED);
        Booking nextBooking = new Booking(1, LocalDateTime.of(2020, 10, 10, 10, 10),
                LocalDateTime.of(2020, 10, 10, 10, 15), item, booker, Status.APPROVED);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(itemRepository.findById(itemDto.getId())).thenReturn(Optional.of(item));
        when(itemRepository.save(any(Item.class)))
                .thenReturn(new Item(itemId, user, "name", "desc", true, null));
        when(bookingRepository
                .findFirstByItemIdAndItemOwnerIdAndStartBeforeAndStatusOrderByEndDesc(any(Integer.class),
                        any(Integer.class), any(LocalDateTime.class), any(Status.class))).thenReturn(lastBooking);
        when(bookingRepository
                .findFirstByItemIdAndItemOwnerIdAndStartAfterAndStatusOrderByStartAsc(any(Integer.class),
                        any(Integer.class), any(LocalDateTime.class), any(Status.class))).thenReturn(nextBooking);
        itemService.createItem(itemDto, userId);
        ItemBookingDto itemFind = itemService.getItemById(userId, itemDto.getId());
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
        assertEquals(itemDto.getAvailable(), item.getAvailable());

        verify(userRepository, times(1)).findById(userId);
        verify(itemRepository, times(1)).findById(itemDto.getId());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void getItemBySearchBlankTest() {
        int userId = 1;
        int from = 1;
        int size = 5;
        String text = "";

        List<ItemDto> items = itemService.getItemBySearch(userId, text, PageRequest.of(from / size, size));
        assertTrue(items.isEmpty(), "Список должен быть пустым");
        assertEquals(0, items.size(), "Список должен содержать 0 элементов");
    }

    @Test
    void getItemBySearchTest() {
        int userId = 1;
        int from = 1;
        int size = 5;
        String text = "name";
        List<ItemDto> listDto = List.of(
                new ItemDto(1, "name", "desc", true),
                new ItemDto(2, "name2", "deqwe", true)
        );
        List<Item> list = List.of(
                new Item(1, null, "name", "desc", true, null),
                new Item(2, null, "name2", "deqwe", true, null)
        );
        Page<Item> page = new PageImpl<>(list);

        when(itemRepository.search(eq(text), any(Pageable.class))).thenReturn(page);

        List<ItemDto> items = itemService.getItemBySearch(userId, text, PageRequest.of(from / size, size));

        assertFalse(items.isEmpty(), "Список не должен быть пустым");
        assertEquals(listDto.size(), items.size(), "Размеры списков должны совпадать");

        for (int i = 0; i < listDto.size(); i++) {
            ItemDto expectedItemDto = listDto.get(i);
            ItemDto actualItemDto = items.get(i);

            assertEquals(expectedItemDto.getId(), actualItemDto.getId(),
                    "ID элементов должны совпадать");
            assertEquals(expectedItemDto.getName(), actualItemDto.getName(),
                    "Имена элементов должны совпадать");
            assertEquals(expectedItemDto.getDescription(), actualItemDto.getDescription(),
                    "Описания элементов должны совпадать");
            assertEquals(expectedItemDto.getAvailable(), actualItemDto.getAvailable(),
                    "Доступность элементов должна совпадать");
        }
    }

    @Test
    void createCommentExceptionTest() {
        int count = 0;
        int userId = 1;
        int itemId = 1;
        CommentDto commentDto = new CommentDto();
        when(bookingRepository
                .existsByItemIdAndBookerIdAndEndBeforeAndStatus(any(Integer.class), any(Integer.class),
                        any(LocalDateTime.class), any(Status.class))).thenReturn(count);

        assertThrows(CommentException.class, () -> itemService.createComment(userId, commentDto, itemId));
    }

    @Test
    void createCommentTest() {
        int count = 1;
        int userId = 1;
        int itemId = 1;
        CommentDto commentDto = new CommentDto("text");
        User user = new User(userId, "name", "email@mail.com");
        Item item = new Item(1, user, "name", "desc", true, null);
        Comment com = new Comment(1, "text", item, user, LocalDateTime.now());

        when(bookingRepository
                .existsByItemIdAndBookerIdAndEndBeforeAndStatus(any(Integer.class), any(Integer.class),
                        any(LocalDateTime.class), any(Status.class))).thenReturn(count);
        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(commentRepository.findByAuthorNameIdAndItemId(userId, itemId)).thenReturn(com);

        CommentDtoForSend comment = itemService.createComment(userId, commentDto, itemId);

        assertNotNull(comment, "Созданный комментарий не должен быть null");
        assertEquals(commentDto.getText(), comment.getText(), "Текст комментария должен совпадать");
        assertEquals(user.getName(), comment.getAuthorName(), "Имя автора комментария должно совпадать");
        assertNotNull(comment.getCreated(), "Дата создания комментария не должна быть null");
    }
}
