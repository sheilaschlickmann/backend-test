package br.com.trier.springvespertino.services;

import br.com.trier.springvespertino.models.Championship;
import br.com.trier.springvespertino.repositories.ChampionshipRepository;
import br.com.trier.springvespertino.services.impl.ChampionshipServiceImpl;
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
class ChampionshipServiceTest {

    @InjectMocks
    private ChampionshipServiceImpl championshipService;
    @Mock
    private ChampionshipRepository championshipRepository;

    @Test
    @DisplayName("Teste inserir Campeonato")
    void insertChampionshipTest() {
        Championship championship = new Championship(1, "Teste",2023 );
        when(championshipRepository.save(any())).thenReturn(championship);
        championshipService.insert(championship);
        assertEquals(1, championship.getId());
        assertEquals("Teste", championship.getDescription());
        assertEquals(2023, championship.getYear());
    }

    @Test
    @DisplayName("Teste deletar Campeonato")
    void deleteChampionshipTest() {
        Championship championship = new Championship(1, "Teste",2023 );
        when(championshipRepository.findById(1)).thenReturn(Optional.of(championship));
        championshipService.delete(1);
        verify(championshipRepository, times(1)).delete(any(Championship.class));
    }

    @Test
    @DisplayName("Teste atualizar Campeonato")
    void updateChampionshipTest() {
        Championship championship = new Championship(1, "Teste",2023 );
        when(championshipRepository.save(championship)).thenReturn(championship);
        Championship updatedChampionship = championshipService.update(championship);
        Assertions.assertEquals("Teste", updatedChampionship.getDescription());
        verify(championshipRepository, times(1)).save(championship);
    }

    @Test
    @DisplayName("Teste procurar ano entre")
    public void findByYearBetweenTest() {
        int year1 = 2022;
        int year2 = 2023;
        List<Championship> championshipList = new ArrayList<>();
        championshipList.add(new Championship(1, "Campeonato A", 2022));
        championshipList.add(new Championship(2, "Campeonato B", 2023));
        when(championshipRepository.findByYearBetween(year1, year2)).thenReturn(championshipList);
        List<Championship> result = championshipService.findByYearBetween(year1, year2);
        Assertions.assertEquals(championshipList, result);
        verify(championshipRepository, times(1)).findByYearBetween(year1, year2);
    }

    @Test
    @DisplayName("Teste procurar descrição que contenha")
    public void findByDescriptionContainsIgnoreCaseTest() {
        String description = "teste";
        List<Championship> championshipList = new ArrayList<>();
        championshipList.add(new Championship(1, "teste  b", 2022));
        when(championshipRepository.findByDescriptionContainsIgnoreCase(description)).thenReturn(championshipList);
        List<Championship> result = championshipService.findByDescriptionContainsIgnoreCase(description);
        Assertions.assertEquals(championshipList, result);
        verify(championshipRepository, times(1)).findByDescriptionContainsIgnoreCase(description);
    }
}

