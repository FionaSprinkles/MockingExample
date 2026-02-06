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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingSystemTest {

    @Mock
    TimeProvider timeProvider;

    @Mock
    RoomRepository roomRepository;

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
}