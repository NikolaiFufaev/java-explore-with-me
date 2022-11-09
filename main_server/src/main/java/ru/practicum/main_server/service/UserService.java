package ru.practicum.main_server.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.main_server.dto.NewUserRequest;
import ru.practicum.main_server.dto.UserDto;
import ru.practicum.main_server.exception.ObjectNotFoundException;
import ru.practicum.main_server.exception.RejectedRequestException;
import ru.practicum.main_server.mapper.UserMapper;
import ru.practicum.main_server.model.User;
import ru.practicum.main_server.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserDto> getUsers(List<Long> ids, int from, int size) {
        if (ids.isEmpty()) {
            return userRepository.findAll(PageRequest.of(from / size, size))
                    .stream()
                    .map(UserMapper::toUserDto)
                    .collect(Collectors.toList());
        }
        return userRepository.findAllByIdIn(ids, PageRequest.of(from / size, size))
                .stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public UserDto saveUser(NewUserRequest newUserRequest) {
        if(newUserRequest.getName()==null || newUserRequest.getEmail()==null){
            throw new RejectedRequestException("user name and email must not be empty");
        }
        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(newUserRequest)));
    }

    @Transactional
    public void deleteUser(Long userId) {
        userRepository.delete(checkAndGetUser(userId));
    }

    public User checkAndGetUser(long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new ObjectNotFoundException("user with id = " + userId + " not found"));
    }

    public User findById(Long id) {
        return userRepository.findUserById(id);
    }
}
