package br.com.trier.springvespertino.services;

import br.com.trier.springvespertino.models.Championship;
import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Race;
import br.com.trier.springvespertino.models.Speedway;
import br.com.trier.springvespertino.repositories.RaceRepository;
import br.com.trier.springvespertino.services.exceptions.IntegrityViolation;
import br.com.trier.springvespertino.services.impl.RaceServiceImpl;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RaceServiceTest {

    @InjectMocks
    private RaceServiceImpl raceService;

    @Mock
    private RaceRepository raceRepository;

    @Test
    @DisplayName("Teste inserir corrida")
    void insertTeamTest() {
        ZonedDateTime data = ZonedDateTime.parse("2023-07-22T18:00:00Z");
        Race race = new Race(1, data, new Speedway(1, "teste", 10, new Country(1, "Texas") ), new Championship(1, "teste", 2023));
        when(raceRepository.save(any())).thenReturn(race);
        raceService.insert(race);
        assertEquals(1, race.getId());
        assertEquals(ZonedDateTime.parse("2023-07-22T18:00:00Z"), race.getDate());
        assertEquals(new Speedway(1, "teste", 10, new Country(1, "Texas") ), race.getSpeedway());
        assertEquals(new Championship(1, "teste", 2023), race.getChampionship());
    }

    @Test
    @DisplayName("Teste procurar corrida por data")
    public void findByDateTest() {
        ZonedDateTime data = ZonedDateTime.parse("2023-07-21T12:00:00Z");
        Race race1 = new Race(1, data, new Speedway(1, "teste", 10, new Country(1, "Texas") ), new Championship(1, "teste", 2023));
        Race race2 = new Race(3, data, new Speedway(8, "teste8", 7, new Country(1, "Texas") ), new Championship(9, "teste9", 2022));
        List<Race> races = new ArrayList<>();
        races.add(race1);
        when(raceRepository.findByDate(data)).thenReturn(races);
        List<Race> result = raceService.findByDate(data);
        Assertions.assertEquals(races, result);
        verify(raceRepository, times(1)).findByDate(data);
    }

    @Test
    @DisplayName("Criar corrida sem campeonato")
    public void createRaceNullChampionshipTest() {
        Race race = new Race(1, ZonedDateTime.parse("2023-07-22T18:00:00Z"), new Speedway(1, "teste", 10, new Country(1, "Texas") ), null);
        var exception = assertThrows(
                IntegrityViolation.class, () -> raceService.insert(race));
        assertEquals("Campeonato não pode ser nulo", exception.getMessage());
    }
}