package com.example.demo.repository.repositoryimpl;

import com.example.demo.dto.UserFilter;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.FilterUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class FilterUserRepositoryImpl implements FilterUserRepository {
    private final EntityManager entityManager;

    @Override
    public List<UserEntity> findAllByFilter(UserFilter filter) {
        var cb = entityManager.getCriteriaBuilder();
        var criteria = cb.createQuery(UserEntity.class);

        var user = criteria.from(UserEntity.class);
        criteria.select(user);

        List<Predicate> predicates = new ArrayList<>();
        if(filter.firstname() != null && !filter.firstname().isBlank()) {
            predicates.add(cb.like(cb.lower(user.get("firstname")),
                    "%" + filter.firstname().toLowerCase() + "%"));
        }
        if(filter.lastname() != null && !filter.lastname().isBlank()) {
            predicates.add(cb.like(cb.lower(user.get("lastname")),
                    "%" + filter.lastname().toLowerCase() + "%"));
        }
        if(filter.age() != null && filter.age() > 0){
            predicates.add(cb.equal(user.get("age"), filter.age()));
        }

        criteria.where(predicates.toArray(Predicate[]::new));

        return entityManager.createQuery(criteria).getResultList();
    }
}
