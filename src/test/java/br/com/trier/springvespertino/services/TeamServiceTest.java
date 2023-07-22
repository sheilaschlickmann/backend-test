package br.com.trier.springvespertino.services;

import br.com.trier.springvespertino.models.Team;
import br.com.trier.springvespertino.repositories.TeamRepository;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import br.com.trier.springvespertino.services.impl.TeamServiceImpl;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {
    @InjectMocks
    private TeamServiceImpl teamService;
    @Mock
    private TeamRepository teamRepository;

    @Test
    @DisplayName("Teste inserir time")
    void insertTeamTest() {
        Team team = new Team(1, "teste" );
        when(teamRepository.save(any())).thenReturn(team);
        var user = teamService.salvar(team);
        assertEquals(1, team.getId());
        assertEquals("teste", team.getName());
    }

    @Test
    @DisplayName("Teste atualizar time")
    void updateTeamTest() {
        Team teamToUpdate = new Team(1, "Team Name");
        when(teamRepository.findById(1)).thenReturn(Optional.of(teamToUpdate));
        when(teamRepository.save(teamToUpdate)).thenReturn(teamToUpdate);
        Team updatedTeam = teamService.update(teamToUpdate);
        Assertions.assertEquals("Team Name", updatedTeam.getName());
        verify(teamRepository, times(1)).findById(1);
        verify(teamRepository, times(1)).save(teamToUpdate);
    }

    @Test
    @DisplayName("Teste achar o time que contém uma String")
    public void findByNameContainsTest() {
        String nameToSearch = "Team";
        List<Team> teams = new ArrayList<>();
        teams.add(new Team(1, "Team A"));
        teams.add(new Team(2, "Team B"));
        when(teamRepository.findByNameContains(nameToSearch)).thenReturn(teams);
        List<Team> result = teamService.findByNameContains(nameToSearch);
        Assertions.assertEquals(teams, result);
        verify(teamRepository, times(1)).findByNameContains(nameToSearch);
    }

    @Test
    @DisplayName("Teste achar o time que não contém uma String")
    public void findByNameContainsNotFoundTest() {
        String nameToSearch = "teste";
        List<Team> emptyList = new ArrayList<>();
        when(teamRepository.findByNameContains(nameToSearch)).thenReturn(emptyList);
        var exception = assertThrows(
                ObjectNotFound.class, () -> teamService.findByNameContains(nameToSearch));
        assertEquals("Nome teste não encontrado em nenhuma equipe", exception.getMessage());
        verify(teamRepository, times(1)).findByNameContains(nameToSearch);
    }
}