package br.com.trier.springvespertino.services;

import br.com.trier.springvespertino.models.Country;
import br.com.trier.springvespertino.repositories.CountryRepository;
import br.com.trier.springvespertino.services.impl.CountryServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {
    @InjectMocks
    private CountryServiceImpl countryService;
    @Mock
    private CountryRepository countryRepository;

    @Test
    @DisplayName("Teste inserir País")
    void insertCountryTest() {
        Country country = new Country(1, "Belgica" );
        when(countryRepository.save(any())).thenReturn(country);
        var user = countryService.salvar(country);
        assertEquals(1, country.getId());
        assertEquals("Belgica", country.getName());
    }

    @Test
    @DisplayName("Teste deletar País")
    void deleteCountryTest() {
        Country country = new Country(1, "Belgica" );
        when(countryRepository.findById(1)).thenReturn(Optional.of(country));
        countryService.delete(1);
        verify(countryRepository, times(1)).delete(any(Country.class));
    }

    @Test
    @DisplayName("Teste atualizar País")
    void updateCountryTest() {
        Country country = new Country(1, "Belgica");
        when(countryRepository.save(country)).thenReturn(country);
        Country updatedCountry = countryService.update(country);
        Assertions.assertEquals("Belgica", updatedCountry.getName());
        verify(countryRepository, times(1)).save(country);
    }
}