package br.com.trier.springvespertino.services;

import br.com.trier.springvespertino.models.*;
import br.com.trier.springvespertino.repositories.PilotRaceRepository;
import br.com.trier.springvespertino.services.impl.PilotRaceServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PilotRaceServiceTest {

    @InjectMocks
    private PilotRaceServiceImpl pilotRaceService;
    @Mock
    private PilotRaceRepository pilotRaceRepository;

    @Test
    @DisplayName("Teste inserir Colocação do Piloto")
    void insertPilotRaceTest() {
        PilotRace pilotRace = new PilotRace(1, 2, new Pilot(1, "pilot", new Country(1, "Brasil"), new Team(20, "team")), new Race(1, ZonedDateTime.parse("2023-07-22T18:00:00Z"), new Speedway(1, "teste", 10, new Country(1, "Texas") ), new Championship(1, "teste", 2023)));
        when(pilotRaceRepository.save(any())).thenReturn(pilotRace);
        pilotRaceService.insert(pilotRace);
        assertEquals(1, pilotRace.getId());
        assertEquals(new Pilot(1, "pilot", new Country(1, "Brasil"), new Team(20, "team")), pilotRace.getPilot());
    }

    @Test
    @DisplayName("Teste Deletar Colocação")
    void deletePilotRaceTest() {
        PilotRace pilotRace = new PilotRace(1, 2, new Pilot(1, "pilot", new Country(1, "Brasil"), new Team(20, "team")), new Race(1, ZonedDateTime.parse("2023-07-22T18:00:00Z"), new Speedway(1, "teste", 10, new Country(1, "Texas") ), new Championship(1, "teste", 2023)));
        when(pilotRaceRepository.findById(1)).thenReturn(Optional.of(pilotRace));
        pilotRaceService.delete(1);
        verify(pilotRaceRepository, times(1)).delete(any(PilotRace.class));
    }

    @Test
    @DisplayName("Teste encontrar colocação")
    public void findByPlacementTest() {
        int placementToSearch = 10;
        List<PilotRace> pilotRaces = new ArrayList<>();
        pilotRaces.add(new PilotRace(1, null, new Pilot(1, "pilot", new Country(1, "Brasil"), new Team(20, "team")), new Race(1, ZonedDateTime.parse("2023-07-22T18:00:00Z"), new Speedway(1, "teste", 10, new Country(1, "Texas") ), new Championship(1, "teste", 2023))));
        pilotRaces.add(new PilotRace(2, null, new Pilot(1, "pilot", new Country(1, "Brasil"), new Team(20, "team")), new Race(1, ZonedDateTime.parse("2023-07-22T18:00:00Z"), new Speedway(1, "teste", 10, new Country(1, "Texas") ), new Championship(1, "teste", 2023))));
        when(pilotRaceRepository.findByPlacement(placementToSearch)).thenReturn(pilotRaces);
        List<PilotRace> result = pilotRaceService.findByPlacement(placementToSearch);
        Assertions.assertEquals(pilotRaces, result);
        verify(pilotRaceRepository, times(1)).findByPlacement(placementToSearch);
    }

}