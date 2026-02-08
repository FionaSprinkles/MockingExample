package com.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Stack;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.configuration.GlobalConfiguration.validate;

@ExtendWith(MockitoExtension.class)
class BookingSystemTest {

    @Mock
    TimeProvider timeProvider;

    @Mock
    RoomRepository roomRepository;

    @Mock
    Room room;

    @Mock
    NotificationService notificationService;

    @InjectMocks
    BookingSystem bookingSystem;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest (name = "{0}")
    @MethodSource ("shouldThrowExceptionIfInvalidBookingInput")
    void shouldThrowException (
        String comment,
        String roomId,
        LocalDateTime startTime,
        LocalDateTime endTime
        ){

        assertThrows(IllegalArgumentException.class, () -> bookingSystem.bookRoom(roomId, startTime, endTime));
    }
     static Stream<Arguments> shouldThrowExceptionIfInvalidBookingInput() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
                Arguments.of("No input in start time", "Standard" , null , now.plusHours(24)),
                Arguments.of("No input in end time" , "Standard" , now , null ),
                Arguments.of("RoomId is null" , null , now , now.plusHours(24))
        );
    }

    @Test
    void shouldThrowExceptionIfStartTimeIsBeforeCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusMinutes(10);
        LocalDateTime endTime = now.plusHours(24);

        when(timeProvider.getCurrentTime()).thenReturn(now);

        assertThrows(IllegalArgumentException.class, () -> bookingSystem.bookRoom("RoomId", startTime, endTime));

    }

    @Test
    void shouldThrowExceptionIfEndTimeIsBeforeStartTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now;
        LocalDateTime endTime = now.minusMinutes(10);

        when(timeProvider.getCurrentTime()).thenReturn(now);
        assertThrows(IllegalArgumentException.class, () -> bookingSystem.bookRoom("RoomId", startTime, endTime));
    }

    @Test
    void shouldAddNewBooking() throws NotificationException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now;
        LocalDateTime endTime = now.plusHours(24);

        when(timeProvider.getCurrentTime()).thenReturn(now);
        when(roomRepository.findById("Standard")).thenReturn(Optional.of(room));
        when(room.isAvailable(startTime, endTime)).thenReturn(true);

        boolean result = bookingSystem.bookRoom("Standard", startTime, endTime);
        assertThat(result).isTrue();

        verify(room).addBooking(any());
        verify(roomRepository).save(room);
        verify(notificationService).sendBookingConfirmation(any());
    }

    @ParameterizedTest (name = "{0}")
    @MethodSource ("invalidAvailableRoomArguments")
    void shouldThrowExceptionIfInvalidAvailableRoom (
            String comment,
            LocalDateTime startTime,
            LocalDateTime endTime
    ){
        assertThrows(IllegalArgumentException.class, () -> bookingSystem.getAvailableRooms(startTime, endTime));
    }
    static Stream<Arguments> invalidAvailableRoomArguments() {
        LocalDateTime now = LocalDateTime.now();
        return Stream.of(
                Arguments.of("No input in start time" , null , now.plusHours(24)),
                Arguments.of("No input in end time" , now , null),
                Arguments.of("End time is before start time" , now.plusHours(24), now)
        );
    }

    @Test
    void shouldReturnListOfAvailableRooms() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.plusHours(24);
        LocalDateTime endTime = now.plusHours(72);

        Room room1 = mock(Room.class);
        Room room2 = mock(Room.class);

        when(roomRepository.findAll()).thenReturn(List.of(room1, room2));
        when(room1.isAvailable(startTime, endTime)).thenReturn(true);
        when(room2.isAvailable(startTime, endTime)).thenReturn(false);

        List<Room> result = bookingSystem.getAvailableRooms(startTime, endTime);

        assertThat(result).containsExactly(room1);
    }

    @Test
    void cancelBookingArgument(){
        assertThrows(IllegalArgumentException.class, () -> bookingSystem.cancelBooking(null));
    }

    @Test
    void cancelBooking(){

        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(room.hasBooking(any())).thenReturn(false);

        boolean result = bookingSystem.cancelBooking("bookingId");

        assertThat(result).isFalse();
    }

    @Test
    void shouldThrowExceptionWhenBookingIsAlreadyStarted(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusHours(1);

        Booking booking = mock(Booking.class);
        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(room.hasBooking("bookingId")).thenReturn(true);
        when(room.getBooking("bookingId")).thenReturn(booking);

        when(booking.getStartTime()).thenReturn(startTime);
        when(timeProvider.getCurrentTime()).thenReturn(now);

        assertThrows(IllegalStateException.class, () -> bookingSystem.cancelBooking("bookingId"));
    }

    @Test
    void shouldRemoveBooking(){

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.plusHours(24);

        Booking booking = mock(Booking.class);
        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(room.hasBooking("bookingId")).thenReturn(true);
        when(room.getBooking("bookingId")).thenReturn(booking);

        when(booking.getStartTime()).thenReturn(startTime);
        when(timeProvider.getCurrentTime()).thenReturn(now);

        assertThat(bookingSystem.cancelBooking("bookingId")).isTrue();
    }

}