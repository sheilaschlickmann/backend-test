package br.com.trier.springvespertino.services;

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Pilot;
import br.com.trier.springvespertino.models.Team;
import br.com.trier.springvespertino.repositories.PilotRepository;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import br.com.trier.springvespertino.services.impl.PilotServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PilotServiceTest {

    @InjectMocks
    private PilotServiceImpl pilotService;
    @Mock
    private PilotRepository pilotRepository;

    @Test
    @DisplayName("Teste inserir piloto")
    void insertTeamTest() {
        Pilot pilot = new Pilot(1, "pilot", new Country(1, "Brasil"), new Team(20, "team"));
        when(pilotRepository.save(any())).thenReturn(pilot);
        var user = pilotService.insert(pilot);
        assertEquals(1, pilot.getId());
        assertEquals("pilot", pilot.getName());
        assertEquals(new Country(1, "Brasil"), pilot.getCountry());
        assertEquals(new Team(20, "team"), pilot.getTeam());
    }

    @Test
    public void listAllTest() {
        List<Pilot> pilots = new ArrayList<>();
        pilots.add(new Pilot(1, "pilot", new Country(1, "Brasil"), new Team(20, "team")));
        pilots.add(new Pilot(1, "pilot", new Country(1, "Brasil"), new Team(20, "team")));
        when(pilotRepository.findAll()).thenReturn(pilots);
        List<Pilot> result = pilotService.listAll();
        Assertions.assertEquals(pilots, result);
        verify(pilotRepository, times(1)).findAll();
    }

    @Test
    public void testListAllNotFound() {
        List<Pilot> emptyList = new ArrayList<>();
        when(pilotRepository.findAll()).thenReturn(emptyList);
        Assertions.assertThrows(ObjectNotFound.class, () -> {
            pilotService.listAll();
        });
        verify(pilotRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Teste deletar Piloto")
    void deletePilotTest() {
        Pilot pilot = new Pilot(1, "pilot", new Country(1, "Brasil"), new Team(20, "team"));
        when(pilotRepository.findById(1)).thenReturn(Optional.of(pilot));
        pilotService.delete(1);
        verify(pilotRepository, times(1)).delete(any(Pilot.class));
    }

    @Test
    @DisplayName("Teste atualizar piloto")
    void updateTeamTest() {
        Pilot pilotToUpdate = new Pilot(1, "Luiz", new Country(1, "Brasil"), new Team(20, "team"));
        when(pilotRepository.findById(1)).thenReturn(Optional.of(pilotToUpdate));
        when(pilotRepository.save(pilotToUpdate)).thenReturn(pilotToUpdate);
        Pilot updatedPilot = pilotService.update(pilotToUpdate);
        Assertions.assertEquals("Luiz", updatedPilot.getName());
        verify(pilotRepository, times(1)).findById(1);
        verify(pilotRepository, times(1)).save(pilotToUpdate);
    }

}