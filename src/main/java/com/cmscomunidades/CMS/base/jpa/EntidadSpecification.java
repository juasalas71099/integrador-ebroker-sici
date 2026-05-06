package com.cmscomunidades.CMS.base.jpa;

import com.cmscomunidades.CMS.base.Entidad;
import jakarta.persistence.criteria.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@CommonsLog
public class EntidadSpecification<T extends Entidad> implements Specification<T> {

    @Getter
    @Setter
    private List<SearchCriteria> criterias = new ArrayList<>();

    public void addCriteria(SearchCriteria criteria) {
        criterias.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        List<Predicate> predicates = new ArrayList<>();
        NumberFormat numberFormat = NumberFormat.getInstance();

        for (SearchCriteria criteria : criterias) {
            try {
                Path<?> path = getPath(root, criteria.getKey());
                List<MatchCriteria> matchCriterias = criteria.getMatchCriterias();
                List<Predicate> matchPredicate = new ArrayList<>();

                for (MatchCriteria matchCriteria : matchCriterias) {

                    Object value = matchCriteria.getValue();

                    if (value.equals("isNull")) { //isNull
                        matchPredicate.add(builder.isNull(path));

                    } else if (value.equals("isNotNull")) { //isNotNull
                        matchPredicate.add(builder.isNotNull(path));

                    } else {
                        if (Boolean.class.equals(path.getJavaType())) {
                            value = Boolean.valueOf((String) matchCriteria.getValue());
                        }
                        if (Long.class.equals(path.getJavaType()) || Integer.class.equals(path.getJavaType())
                                || Double.class.equals(path.getJavaType())) {
                            try {
                                value = numberFormat.parse((String) value);
                            } catch (ParseException e) {
                                log.error(e);
                            }
                        }
                        if (LocalDate.class.equals(path.getJavaType())) {
                            LocalDate localDate = LocalDate.parse((String) value);
                            value = localDate;
                        }

                        if (LocalDateTime.class.equals(path.getJavaType())) {
                            LocalDateTime localDateTime = LocalDateTime.parse((String) value);
                            value = localDateTime;
                        }


                        if (MatchMode.EQUALS.equals(matchCriteria.getMatchMode())) {
                            matchPredicate.add(builder.equal(path, value));
                        } else if (MatchMode.NOT_EQUALS.equals(matchCriteria.getMatchMode())) {
                            matchPredicate.add(builder.notEqual(path, value));
                        } else if (MatchMode.CONTAINS.equals(matchCriteria.getMatchMode())) {
                            matchPredicate.add(builder.like(builder.lower((Expression<String>) path),
                                    StringUtils.join("%", value.toString().toLowerCase(), "%")));
                        } else if (MatchMode.NOT_CONTAINS.equals(matchCriteria.getMatchMode())) {
                            matchPredicate.add(builder.notLike(builder.lower((Expression<String>) path),
                                    StringUtils.join("%", value.toString().toLowerCase(), "%")));
                        } else if (MatchMode.GREATER_THAN.equals(matchCriteria.getMatchMode())) {
                            if (Long.class.equals(path.getJavaType())) {
                                matchPredicate.add(
                                        builder.greaterThan(path.as(Long.class), NumberUtils.toLong(value.toString())));
                            } else if (Integer.class.equals(path.getJavaType())) {
                                matchPredicate.add(
                                        builder.greaterThan(path.as(Integer.class), NumberUtils.toInt(value.toString())));
                            } else if (Double.class.equals(path.getJavaType())) {
                                matchPredicate.add(
                                        builder.greaterThan(path.as(Double.class), NumberUtils.toDouble(value.toString())));
                            } else if (BigDecimal.class.equals(path.getJavaType())) {
                                matchPredicate.add(builder.greaterThan(path.as(BigDecimal.class),
                                        NumberUtils.toScaledBigDecimal(value.toString())));
                            } else if (LocalDate.class.equals(path.getJavaType())) {
                                matchPredicate.add(
                                        builder.greaterThan(path.as(LocalDate.class), LocalDate.parse(value.toString())));
                            } else if (LocalDateTime.class.equals(path.getJavaType())) {
                                matchPredicate.add(builder.greaterThan(path.as(LocalDateTime.class),
                                        LocalDateTime.parse(value.toString())));
                            }
                        } else if (MatchMode.LESS_THAN_EQUAL.equals(matchCriteria.getMatchMode())) {
                            if (Long.class.equals(path.getJavaType())) {
                                matchPredicate.add(
                                        builder.lessThanOrEqualTo(path.as(Long.class), NumberUtils.toLong(value.toString())));
                            } else if (Integer.class.equals(path.getJavaType())) {
                                matchPredicate.add(
                                        builder.lessThanOrEqualTo(path.as(Integer.class), NumberUtils.toInt(value.toString())));
                            } else if (Double.class.equals(path.getJavaType())) {
                                matchPredicate.add(
                                        builder.lessThanOrEqualTo(path.as(Double.class), NumberUtils.toDouble(value.toString())));
                            } else if (BigDecimal.class.equals(path.getJavaType())) {
                                matchPredicate.add(builder.lessThanOrEqualTo(path.as(BigDecimal.class),
                                        NumberUtils.toScaledBigDecimal(value.toString())));
                            }
                            if (LocalDate.class.equals(path.getJavaType())) {
                                matchPredicate.add(builder.lessThanOrEqualTo(path.as(LocalDate.class), LocalDate.parse(value.toString())));
                            } else if (LocalDateTime.class.equals(path.getJavaType())) {
                                matchPredicate.add(builder.lessThanOrEqualTo(path.as(LocalDateTime.class), LocalDateTime.parse(value.toString())));
                            }
                        } else if (MatchMode.LESS_THAN.equals(matchCriteria.getMatchMode())) {
                            if (Long.class.equals(path.getJavaType())) {
                                matchPredicate.add(
                                        builder.lessThan(path.as(Long.class), NumberUtils.toLong(value.toString())));
                            } else if (Integer.class.equals(path.getJavaType())) {
                                matchPredicate.add(
                                        builder.lessThan(path.as(Integer.class), NumberUtils.toInt(value.toString())));
                            } else if (Double.class.equals(path.getJavaType())) {
                                matchPredicate.add(
                                        builder.lessThan(path.as(Double.class), NumberUtils.toDouble(value.toString())));
                            } else if (BigDecimal.class.equals(path.getJavaType())) {
                                matchPredicate.add(builder.lessThan(path.as(BigDecimal.class),
                                        NumberUtils.toScaledBigDecimal(value.toString())));
                            }
                            if (LocalDate.class.equals(path.getJavaType())) {
                                matchPredicate.add(builder.lessThan(path.as(LocalDate.class), LocalDate.parse(value.toString())));
                            } else if (LocalDateTime.class.equals(path.getJavaType())) {
                                matchPredicate.add(builder.lessThan(path.as(LocalDateTime.class), LocalDateTime.parse(value.toString())));
                            }
                        } else if (MatchMode.GREATER_THAN_EQUAL.equals(matchCriteria.getMatchMode())) {
                            if (Long.class.equals(path.getJavaType())) {
                                matchPredicate.add(
                                        builder.greaterThanOrEqualTo(path.as(Long.class), NumberUtils.toLong(value.toString())));
                            } else if (Integer.class.equals(path.getJavaType())) {
                                matchPredicate.add(
                                        builder.greaterThanOrEqualTo(path.as(Integer.class), NumberUtils.toInt(value.toString())));
                            } else if (Double.class.equals(path.getJavaType())) {
                                matchPredicate.add(
                                        builder.greaterThanOrEqualTo(path.as(Double.class), NumberUtils.toDouble(value.toString())));
                            } else if (BigDecimal.class.equals(path.getJavaType())) {
                                matchPredicate.add(builder.greaterThanOrEqualTo(path.as(BigDecimal.class),
                                        NumberUtils.toScaledBigDecimal(value.toString())));
                            }
                            if (LocalDate.class.equals(path.getJavaType())) {
                                matchPredicate.add(builder.greaterThanOrEqualTo(path.as(LocalDate.class), LocalDate.parse(value.toString())));
                            } else if (LocalDateTime.class.equals(path.getJavaType())) {
                                matchPredicate.add(builder.greaterThanOrEqualTo(path.as(LocalDateTime.class), LocalDateTime.parse(value.toString())));
                            }
                        }
                    }


                }

                if (SearchCriteria.OPERATOR_AND.equals(criteria.getOperator())) {
                    predicates.add(builder.and(matchPredicate.toArray(new Predicate[0])));
                } else if (SearchCriteria.OPERATOR_OR.equals(criteria.getOperator())) {
                    predicates.add(builder.or(matchPredicate.toArray(new Predicate[0])));
                }

                log.info("Predicate: " + predicates);

            } catch (IllegalArgumentException e) {
                log.error(e);

            }

        }
        return builder.and(predicates.toArray(new Predicate[0]));
    }

    /**
     * @param root
     * @param field
     * @return
     */
    private Path<?> getPath(Root<T> root, String field) throws IllegalArgumentException {
        if (!field.contains(".")) {
            return root.get(field);
        } else {
            String[] pathElements = StringUtils.split(field, ".");
            Path<?> path = root;
            for (String element : pathElements) {
                path = path.get(element);
            }
            return path;
        }
    }


}
