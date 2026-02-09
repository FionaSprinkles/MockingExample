package com.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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


    /**
     * Verifies that bookRoom throws IllegalArgumentException
     * when required booking input is missing.
     *
     * Tested scenarios:
     * - startTime is null
     * - endTime is null
     * - roomId is null
     */

    @ParameterizedTest (name = "{0}")
    @MethodSource ("shouldThrowExceptionIfInvalidBookingInput")
    @DisplayName("Verify that bookRoom throws IllegalArgumentException")
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

    /**
     * Verifies that bookRoom throws IllegalArgumentException
     * when you try to book a room before current time
     *
     * Tested scenarios:
     * - start time is before current time
     */

    @Test
    @DisplayName("Cannot book a room if start time is before current time")
    void shouldThrowExceptionIfStartTimeIsBeforeCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now.minusMinutes(10);
        LocalDateTime endTime = now.plusHours(24);

        when(timeProvider.getCurrentTime()).thenReturn(now);

        assertThrows(IllegalArgumentException.class, () -> bookingSystem.bookRoom("RoomId", startTime, endTime));

    }

    /**
     * Verifies that bookRoom throws IllegalArgumentException
     * when the bookings ending time is before its starting time.
     *
     * Tested scenarios:
     * - end time is before start time
     */
    @Test
    @DisplayName("Cannot book room if end time is before start time")
    void shouldThrowExceptionIfEndTimeIsBeforeStartTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startTime = now;
        LocalDateTime endTime = now.minusMinutes(10);

        when(timeProvider.getCurrentTime()).thenReturn(now);
        assertThrows(IllegalArgumentException.class, () -> bookingSystem.bookRoom("RoomId", startTime, endTime));
    }

    /**
     * Verifies that bookRoom adds a new booking
     * when booking a room.
     *
     * Verifies that you get a notification when booking is complete.
     *
     * Tested scenarios:
     * - room is booked and saved in roomRepository.
     * - notificationService sends notification when room is booked.
     */

    @Test
    @DisplayName("Should book a room and send a notification to confirm")
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

    /**
     * Verifies that getAvailableRooms throws IllegalArgumentException
     * when required input is missing.
     *
     * Tested scenarios:
     * - startTime is null
     * - endTime is null
     * - endTime is before startTime
     */
    @ParameterizedTest (name = "{0}")
    @MethodSource ("invalidAvailableRoomArguments")
    @DisplayName("Verify that getAvailableRooms throw IllegalArgumentException")
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
    /**
     * Verifies that getAvailableRooms returns a list
     * of available rooms.
     *
     * Tested scenarios:
     * - find all available rooms whithin a specific timeslot and return them
     */

    @Test
    @DisplayName("Should return a list of available rooms within specific time slot")
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
    /**
     * Verifies that cancelBooking throws IllegalArgumentException
     * when required cancelling input is missing.
     *
     * Tested scenarios:
     * - bookingId is null
     */

    @Test
    @DisplayName("Should throw IllegalArgumentException if bookingId is null when trying to cancel booking")
    void cancelBookingArgument(){
        assertThrows(IllegalArgumentException.class, () -> bookingSystem.cancelBooking(null));
    }

    /**
     * Verifies that cancelBooking returns false
     * when the booking does not exist.
     *
     * Tested scenarios:
     * - bookingId is not found in any room
     */

    @Test
    @DisplayName("Should return false when booking is not found")
    void cancelBooking(){

        when(roomRepository.findAll()).thenReturn(List.of(room));
        when(room.hasBooking(any())).thenReturn(false);

        boolean result = bookingSystem.cancelBooking("bookingId");

        assertThat(result).isFalse();
    }
    /**
     * Verifies that cancelBooking throws IllegalStateExpression
     * when you try to cancel a booking that already started.
     *
     * Tested scenarios:
     * - cancelBooking when startTime is before current time
     */

    @Test
    @DisplayName("Should throw IllegalStateException when you try to cancel a booking after start time")
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
    /**
     * Verifies that cancelBooking removes a booking
     *
     * Tested scenarios:
     * - cancelBooking removes booking if startTime is after curent time
     */

    @Test
    @DisplayName("Should cancel a booking if booking hasnt started yet")
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