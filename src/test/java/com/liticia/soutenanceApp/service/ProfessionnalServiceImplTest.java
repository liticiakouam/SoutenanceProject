package com.liticia.soutenanceApp.service;

import com.liticia.soutenanceApp.model.User;
import com.liticia.soutenanceApp.repository.ProfessionnalRepository;
import com.liticia.soutenanceApp.service.serviceImpl.ProfessionnalServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ProfessionnalServiceImplTest {
    private final ProfessionnalRepository professionnalRepository = Mockito.mock(ProfessionnalRepository.class);

    private final ProfessionnalService professionnalService = new ProfessionnalServiceImpl(professionnalRepository);

    @Test
    void testShouldReturnUsers() {
        List<User> users = Arrays.asList(
                User.builder().firstName("liticia").lastName("anzwe").build(),
                User.builder().firstName("momo").build()
        );
        Pageable pageable = PageRequest.of(1, 2);
        Page<User> page = new PageImpl<>(users);

        when(professionnalRepository.findAllByRolesIdOrderByCreatedAtDesc(pageable, 3)).thenReturn(page);

        Page<User> userList = professionnalService.findAll(pageable);
        assertEquals(2, userList.getTotalElements());
        assertEquals(1, userList.getTotalPages());
        verify(professionnalRepository, times(1)).findAllByRolesIdOrderByCreatedAtDesc(pageable,3);
    }

    @Test
    void testShouldSearchUsers() {
        List<User> users = Arrays.asList(
                User.builder().firstName("liti").lastName("anzwe").build(),
                User.builder().firstName("liti").build()
        );
        when(professionnalRepository.searchUsers("douala","informatique","liti")).thenReturn(users);

        List<User> searchUser = professionnalService.searchUser("douala","informatique","liti");
        assertEquals(2, searchUser.size());
        verify(professionnalRepository, times(1)).searchUsers("douala","informatique","liti");
    }

    @Test
    void testShouldFindUserById() {
        User user = User.builder().firstName("liti").build();
        when(professionnalRepository.findById(3L)).thenReturn(Optional.ofNullable(user));

        Optional<User> optionalUser = professionnalService.findById(3L);
        assertTrue(optionalUser.isPresent());
        assertEquals("liti", optionalUser.get().getFirstName());
        verify(professionnalRepository, times(1)).findById(3L);
    }
}
