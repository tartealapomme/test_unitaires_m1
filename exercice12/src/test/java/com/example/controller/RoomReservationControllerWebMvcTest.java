package com.example.controller;

import com.example.exception.ReservationConflictException;
import com.example.exception.ReservationNotFoundException;
import com.example.model.Reservation;
import com.example.model.Salle;
import com.example.model.StatutReservation;
import com.example.service.ReservationService;
import com.example.service.SalleService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({SalleController.class, ReservationController.class, ApiExceptionHandler.class})
class RoomReservationControllerWebMvcTest {

    private static final LocalDateTime DEBUT = LocalDateTime.of(2025, 6, 15, 10, 0);
    private static final LocalDateTime FIN = LocalDateTime.of(2025, 6, 15, 11, 0);

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SalleService salleService;

    @MockitoBean
    private ReservationService reservationService;

    @Test
    void shouldCreateRoom_whenRequestIsValid() throws Exception {
        when(salleService.creer("Salle A", 10)).thenReturn(new Salle(1L, "Salle A", 10));

        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"Salle A\",\"capacite\":10}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nom").value("Salle A"))
                .andExpect(jsonPath("$.capacite").value(10));

        verify(salleService).creer("Salle A", 10);
    }

    @Test
    void shouldReturnBadRequest_whenRoomCreationIsInvalid() throws Exception {
        mockMvc.perform(post("/api/rooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nom\":\"\",\"capacite\":0}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));

        verify(salleService, never()).creer(org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.anyInt());
    }

    @Test
    void shouldCreateReservation_whenRequestIsValid() throws Exception {
        when(reservationService.creer(1L, "Alice", DEBUT, FIN))
                .thenReturn(new Reservation(1L, 1L, "Alice", DEBUT, FIN, StatutReservation.CONFIRMED));

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "salleId": 1,
                                  "nomPersonne": "Alice",
                                  "debut": "2025-06-15T10:00:00",
                                  "fin": "2025-06-15T11:00:00"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.statut").value("CONFIRMED"));

        verify(reservationService).creer(1L, "Alice", DEBUT, FIN);
    }

    @Test
    void shouldReturnNotFound_whenReservationDoesNotExist() throws Exception {
        when(reservationService.trouverParId(99L)).thenThrow(new ReservationNotFoundException(99L));

        mockMvc.perform(get("/api/reservations/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));

        verify(reservationService).trouverParId(99L);
    }

    @Test
    void shouldReturnConflict_whenBusinessRulePreventsAction() throws Exception {
        when(reservationService.creer(1L, "Alice", DEBUT, FIN))
                .thenThrow(new ReservationConflictException("Le créneau chevauche une réservation existante"));

        mockMvc.perform(post("/api/reservations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "salleId": 1,
                                  "nomPersonne": "Alice",
                                  "debut": "2025-06-15T10:00:00",
                                  "fin": "2025-06-15T11:00:00"
                                }
                                """))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));

        verify(reservationService).creer(1L, "Alice", DEBUT, FIN);
    }

    @Test
    void shouldListRooms() throws Exception {
        when(salleService.listerToutes()).thenReturn(List.of(new Salle(1L, "Salle A", 10)));

        mockMvc.perform(get("/api/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));

        verify(salleService).listerToutes();
    }
}
