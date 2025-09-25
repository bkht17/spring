package com.example.demo.service.serviceimpl;

import com.example.demo.dto.PageResponse;
import com.example.demo.dto.UserFilter;
import com.example.demo.entity.UploadedFileEntity;
import com.example.demo.model.User;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.UsersRepository;
import com.example.demo.dto.UserDto;
import com.example.demo.dto.mapping.UserMapping;
import com.example.demo.service.FileStorageService;
import com.example.demo.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final Map<Long, User> users;
    @Autowired
    UserMapping userMapping;
    @Autowired
    UsersRepository usersRepository;
    @Autowired
    FileStorageService fileStorageService;
    @Autowired
    private EntityManager entityManager;

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
    public List<UserDto> getAll(UserFilter filter) {
        List<UserEntity> userEntities = usersRepository.findAllByFilter(filter);

        return userMapping.toUserDtos(userEntities.stream().map(this::toDomain).toList());
    }

    @Override
    public PageResponse<UserDto> getAllWithPagination(UserFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UserEntity> criteria = cb.createQuery(UserEntity.class);
        Root<UserEntity> user = criteria.from(UserEntity.class);

        List<Predicate> predicates = buildPredicates(cb, user, filter);
        criteria.where(predicates.toArray(new Predicate[0]));

        Long totalElements = getTotalElements(filter);

        TypedQuery<UserEntity> query = entityManager.createQuery(criteria);
        query.setFirstResult(filter.page() * filter.size());
        query.setMaxResults(filter.size());

        List<UserEntity> userEntities = query.getResultList();
        List<UserDto> userDtos = userMapping.toUserDtos(userEntities.stream().map(this::toDomain).toList());

        int totalPages = (int) Math.ceil((double) totalElements / filter.size());

        return new PageResponse<>(
                userDtos,
                filter.page(),
                totalPages,
                totalElements,
                filter.size()
        );
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
                user.uploadedFile()
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
                userEntity.getAge(),
                userEntity.getUploadedFile()
        );
    };

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<UserEntity> user, UserFilter filter) {
        List<Predicate> predicates = new ArrayList<>();

        if (filter.firstname() != null && !filter.firstname().isBlank()) {
            predicates.add(cb.like(cb.lower(user.get("firstname")),
                    "%" + filter.firstname().toLowerCase() + "%"));
        }

        if (filter.lastname() != null && !filter.lastname().isBlank()) {
            predicates.add(cb.like(cb.lower(user.get("lastname")),
                    "%" + filter.lastname().toLowerCase() + "%"));
        }

        if (filter.age() != null && filter.age() > 0) {
            predicates.add(cb.equal(user.get("age"), filter.age()));
        }

        return predicates;
    }

    private Long getTotalElements(UserFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<UserEntity> user = countQuery.from(UserEntity.class);

        List<Predicate> predicates = buildPredicates(cb, user, filter);
        countQuery.select(cb.count(user)).where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(countQuery).getSingleResult();
    }

}
