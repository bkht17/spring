package com.example.demo.service.serviceimpl;

import com.example.demo.entity.UploadedFileEntity;
import com.example.demo.model.User;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UsersRepository;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.mapping.UserMapping;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserServiceImpl implements UserService {

    private final Map<Long, User> users;
    @Autowired
    UserMapping userMapping;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    FileStorageService fileStorageService;

    public UserServiceImpl(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
        users = new HashMap<>();

    }

    @Override
    public UserDto getUserById(Long id) {
        UserEntity userEntity = usersRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));
        return userMapping.toUserDto(toDomain(userEntity));
    }


    @Override
    public List<UserDto> getAll() {

        List<UserEntity> userEntities = usersRepository.findAll();

        return userMapping.toUserDtos(userEntities.stream().map(this::toDomain).toList());
    }

    @Override
    public User create(User user) {
        var entityToSave = new UserEntity(
                null,
                user.firstname(),
                user.lastname(),
                user.email(),
                user.password(),
                user.age(),
                null
        );

        var savedEntity = usersRepository.save(entityToSave);
        return toDomain(savedEntity);
    }

    @Override
    public User update(Long id, User user) {
        var userEntity = usersRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found with id " + id));

        var oldUser = users.get(id);
        var newUser = new UserEntity(
                userEntity.getId(),
                user.firstname(),
                user.lastname(),
                user.email(),
                user.password(),
                user.age(),
                null
        );
        var updatedUser = usersRepository.save(newUser);

        return toDomain(updatedUser);
    }

    @Override
    public void delete(Long id) {
        if (!usersRepository.existsById(id)) {
            throw new EntityNotFoundException("User not found with id " + id);
        }
        usersRepository.deleteById(id);
    }

    @Override
    public void saveFileData(InputStream file, Long fileId) throws IOException {
        UploadedFileEntity uploadedFile = fileStorageService.getFileById(fileId);

        List<UserEntity> entities = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(file);
        Sheet sheet = workbook.getSheetAt(0);

        sheet.forEach(row -> {
            if (row.getRowNum() == 0) return;

            String firstname = row.getCell(0).getStringCellValue();
            String lastname  = row.getCell(1).getStringCellValue();
            String email     = row.getCell(2).getStringCellValue();
            String password  = row.getCell(3).getStringCellValue();
            int age          = (int) row.getCell(4).getNumericCellValue();

            UserEntity entity = new UserEntity(
                    null,
                    firstname,
                    lastname,
                    email,
                    password,
                    age,
                    uploadedFile
            );

            entities.add(entity);
        });

        usersRepository.saveAll(entities);
    }

    private User toDomain(UserEntity userEntity){
        return new User(
                userEntity.getId(),
                userEntity.getFirstname(),
                userEntity.getLastname(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.getAge()
        );
    };

}
