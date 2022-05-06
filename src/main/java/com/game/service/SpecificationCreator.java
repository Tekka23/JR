package com.game.service;

import com.game.entity.Player;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;
import java.util.Objects;
import javax.persistence.criteria.Predicate;

public class SpecificationCreator {
    public static Specification<Player> createSpecification(Filter filter){
        return (root, query, cb) -> {
            Predicate nameLike = (Objects.nonNull(filter.name) ? cb.like(root.get("name"), "%" +filter.name+"%") : cb.conjunction());
            Predicate titleLike = (Objects.nonNull(filter.title) ? cb.like(root.get("title"), "%"+filter.title+"%") : cb.conjunction());
            Predicate raceEq = (Objects.nonNull(filter.race) ? cb.equal(root.get("race"), filter.race) : cb.conjunction());
            Predicate professionEq = (Objects.nonNull(filter.profession) ? cb.equal(root.get("profession"), filter.profession) : cb.conjunction());
            Predicate findAfter = (Objects.nonNull(filter.after) ? cb.greaterThanOrEqualTo(root.get("birthday"), new Date(filter.after)) : cb.conjunction());
            Predicate findBefore = Objects.nonNull(filter.before) ? cb.lessThanOrEqualTo(root.get("birthday"), new Date(filter.before)) : cb.conjunction();
            Predicate findBanned = Objects.nonNull(filter.banned) ? (filter.banned ? cb.isTrue(root.get("banned")) : cb.isFalse(root.get("banned"))) : cb.conjunction();
            Predicate findMinExp = Objects.nonNull(filter.minExperience) ? cb.greaterThanOrEqualTo(root.get("experience"), filter.minExperience) : cb.conjunction();
            Predicate findMaxExp = Objects.nonNull(filter.maxExperience) ? cb.lessThanOrEqualTo(root.get("experience"), filter.maxExperience) : cb.conjunction();
            Predicate findMinLvl = Objects.nonNull(filter.minLevel) ? cb.greaterThanOrEqualTo(root.get("level"), filter.minLevel) : cb.conjunction();
            Predicate findMaxLvl = Objects.nonNull(filter.maxLevel) ? cb.lessThanOrEqualTo(root.get("level"), filter.maxLevel) : cb.conjunction();
            return cb.and(nameLike, titleLike, raceEq, professionEq, findAfter, findBefore, findBanned, findMinExp, findMaxExp, findMinLvl, findMaxLvl);
        };
    }
}
