package br.com.trier.springvespertino.services;

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.models.Speedway;
import br.com.trier.springvespertino.repositories.SpeedwayRepository;
import br.com.trier.springvespertino.services.exceptions.ObjectNotFound;
import br.com.trier.springvespertino.services.impl.SpeedwayServiceImpl;
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
class SpeedwayServiceTest {
    @InjectMocks
    private SpeedwayServiceImpl speedwayService;
    @Mock
    private SpeedwayRepository speedwayRepository;

    @Test
    @DisplayName("Teste inserir SpeedWay")
    void insertSpeedWayTest() {
        Speedway speedway = new Speedway(1, "teste", 10, new Country(1, "Texas") );
        when(speedwayRepository.save(any())).thenReturn(speedway);
        speedwayService.insert(speedway);
        assertEquals(1, speedway.getId());
        assertEquals("teste", speedway.getName());
        assertEquals(10, speedway.getSize());
        assertEquals(new Country(1, "Texas"), speedway.getCountry());
    }

    @Test
    @DisplayName("Teste Deletar SpeedWay")
    void deleteSpeedWay() {
        Speedway speedway = new Speedway(1, "teste", 10, new Country(1, "Texas"));
        when(speedwayRepository.findById(1)).thenReturn(Optional.of(speedway));
        speedwayService.delete(1);
        verify(speedwayRepository, times(1)).delete(any(Speedway.class));
    }

    @Test
    @DisplayName("Procurar tamanho entre")
    public void findBySizeBetweenTest() {
        int sizeIn = 10;
        int sizeFin = 20;
        List<Speedway> speedways = new ArrayList<>();
        speedways.add(new Speedway(1, "teste", 10, new Country(1, "Texas")));
        speedways.add(new Speedway(1, "teste", 10, new Country(1, "Texas")));
        when(speedwayRepository.findBySizeBetween(sizeIn, sizeFin)).thenReturn(speedways);
        List<Speedway> result = speedwayService.findBySizeBetween(sizeIn, sizeFin);
        Assertions.assertEquals(speedways, result);
        verify(speedwayRepository, times(1)).findBySizeBetween(sizeIn, sizeFin);
    }

    @Test
    @DisplayName("Procurar País ordenado por tamanho da pista decrescente")
    public void findByCountryOrderBySizeDescTest() {
        Country country = new Country(2, "Belgica");
        Speedway speedway1 = new Speedway(1, "Teste 1", 1, country);
        Speedway speedway2 = new Speedway(2, "Teste 2", 2, country);
        List<Speedway> speedways = new ArrayList<>();
        speedways.add(speedway1);
        speedways.add(speedway2);
        when(speedwayRepository.findByCountryOrderBySizeDesc(country)).thenReturn(speedways);
        List<Speedway> result = speedwayService.findByCountryOrderBySizeDesc(country);
        Assertions.assertEquals(speedways, result);
        verify(speedwayRepository, times(1)).findByCountryOrderBySizeDesc(country);
    }

    @Test
    @DisplayName("Não encontra País ordenado por tamanho da pista decrescente")
    public void findByCountryOrderBySizeDescNotFoundTest() {
        Country country = new Country(2, "Belgica");
        List<Speedway> emptyList = new ArrayList<>();
        when(speedwayRepository.findByCountryOrderBySizeDesc(country)).thenReturn(emptyList);
        var exception = assertThrows(
                ObjectNotFound.class, () -> speedwayService.findByCountryOrderBySizeDesc(country));
        assertEquals("Nenhuma pista cadastrada no país: Belgica", exception.getMessage());
        verify(speedwayRepository, times(1)).findByCountryOrderBySizeDesc(country);
    }
}