package com.vertex.item;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ItemSearchRepositoryImpl implements ItemSearchRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Item> search(String query, SourceType source, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Item> criteria = builder.createQuery(Item.class);
        Root<Item> root = criteria.from(Item.class);
        Predicate predicate = buildPredicate(builder, root, query, source);
        criteria.where(predicate);
        criteria.orderBy(builder.desc(root.get("score")), builder.desc(root.get("createdAt")));

        TypedQuery<Item> typedQuery = entityManager.createQuery(criteria);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Item> items = typedQuery.getResultList();

        CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
        Root<Item> countRoot = countCriteria.from(Item.class);
        countCriteria.select(builder.count(countRoot)).where(buildPredicate(builder, countRoot, query, source));
        Long total = entityManager.createQuery(countCriteria).getSingleResult();
        return new PageImpl<>(items, pageable, total);
    }

    private Predicate buildPredicate(CriteriaBuilder builder, Root<Item> root, String query, SourceType source) {
        String pattern = "%" + query.toLowerCase() + "%";
        List<Predicate> predicates = new ArrayList<>();
        /*
         * The PRD requires Postgres search while disallowing raw SQL strings in application code,
         * so V1 uses JPA Criteria against indexed item text instead of native query literals.
         */
        predicates.add(builder.or(
                builder.like(builder.lower(root.get("title")), pattern),
                builder.like(builder.lower(root.get("summary")), pattern)
        ));
        if (source != null) {
            predicates.add(builder.equal(root.get("source"), source));
        }
        return builder.and(predicates.toArray(Predicate[]::new));
    }
}

