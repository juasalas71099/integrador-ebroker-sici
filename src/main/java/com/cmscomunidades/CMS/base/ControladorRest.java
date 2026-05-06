package com.cmscomunidades.CMS.base;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.cmscomunidades.CMS.base.exception.MensajeErrorUsuario;
import com.cmscomunidades.CMS.base.exception.ResultadoException;
import com.cmscomunidades.CMS.base.jpa.EntidadSpecification;
import com.cmscomunidades.CMS.base.jpa.SearchCriteria;

import jakarta.validation.ConstraintViolation;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.BooleanUtils;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@CommonsLog
public abstract class ControladorRest<E extends Entidad, ID extends Serializable> {

  @SuppressWarnings("unchecked")
  protected final Class<E> type =
          (Class<E>)
                  ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

  private Map<String, String> fieldTypes = new HashMap<>();

  @Autowired(required = false)
  protected JpaRepository<E, ID> repository;

   private static final  String isNotNull = "isNotNull";
   private static final  String isNull = "isNull";

  /**
   * Recupera un objeto con el identificador especificado como parámetro
   *
   * @param id
   * @return
   */
  @GetMapping(
          value = "/{id}",
          produces = {"application/json"})
  public Optional<E> get(@PathVariable ID id) {
    return repository.findById(id);
  }

  @GetMapping(
          value = "/all",
          produces = {"application/json"})
  public List<E> findAll(@RequestParam MultiValueMap<String, String> queryPath, Sort sort) {
    Predicate predicate = buildPredicate(queryPath);
    QuerydslPredicateExecutor querydslPredicateExecutor = (QuerydslPredicateExecutor) repository;
    List<E> listado = null;
    if (sort != null) {
      listado = (List<E>) querydslPredicateExecutor.findAll(predicate, sort);
    } else {
      listado = (List<E>) querydslPredicateExecutor.findAll(predicate);
    }
    return listado;
  }

  @GetMapping(
          value = "/find",
          produces = {"application/json"})
  @ResponseBody
  public List<E> find(@RequestParam MultiValueMap<String, String> queryPath, Pageable pageable) {
    Predicate predicate = buildPredicate(queryPath);
    QuerydslPredicateExecutor querydslPredicateExecutor = (QuerydslPredicateExecutor) repository;
    return querydslPredicateExecutor.findAll(predicate, pageable).getContent();
  }

  @GetMapping(
          value = "/count",
          produces = {"application/json"})
  @ResponseBody
  public long getCount(@RequestParam MultiValueMap<String, String> queryPath) {
    Predicate predicate = buildPredicate(queryPath);
    QuerydslPredicateExecutor<E> querydslPredicateExecutor = (QuerydslPredicateExecutor<E>) repository;
    return querydslPredicateExecutor.count(predicate);
  }

  protected void preGuardar(E entidad) {}

  protected void postGuardar(E entidad) {}

  protected void preActualizar(E entidad) {}

  protected void postActualizar(E entidad) {}

  protected void preEliminar(ID id) {}

  protected void postEliminar(ID id) {}

  protected ResultadoException validaGuardar(E entidad) {
    return new ResultadoException();
  }

  protected ResultadoException validaActualizar(E entidad) {
    return new ResultadoException();
  }

  protected ResultadoException validaEliminar(E object) {
    return new ResultadoException();
  }

  protected ResultadoException validaEliminar(ID id) {
    return new ResultadoException();
  }

  @PostMapping(consumes = {"application/json", "multipart/form-data"})
  @Transactional
  public ResponseEntity<?> save(@RequestBody E entidad) {
    ResultadoException resultadoException = validaGuardar(entidad);
    try {
      preGuardar(entidad);

      if (!resultadoException.debeArrojarse()) {
        HttpHeaders header = new HttpHeaders();
        ResponseEntity<?> response =
                new ResponseEntity<>(repository.save(entidad), header, HttpStatus.CREATED);
        postGuardar(entidad);
        return response;
      } else {
        StringBuilder stb = new StringBuilder();
        for (MensajeErrorUsuario error : resultadoException.getErrores()) {
          stb.append(error.getMsgId());
        }
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, stb.toString());
      }

    } catch (DataIntegrityViolationException ex) {
      if (ex.getCause() instanceof ConstraintViolationException) {
        ConstraintViolationException constraintViolationEx =
                (ConstraintViolationException) ex.getCause();
        throw new ResponseStatusException(
                HttpStatus.NOT_ACCEPTABLE,
                constraintViolationEx.getSQLException().getLocalizedMessage());
      }
      throw new ResponseStatusException(
              HttpStatus.NOT_ACCEPTABLE, ex.getCause().getLocalizedMessage());
    } catch (jakarta.validation.ConstraintViolationException ex) {
      StringBuilder stb = new StringBuilder();
      for (ConstraintViolation<?> constraintViolation : ex.getConstraintViolations()) {
        stb.append(constraintViolation.getPropertyPath()).append(":");
        stb.append(constraintViolation.getMessage()).append("\n");
      }

      throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, stb.toString());
    }
  }

  @PostMapping("/all")
  @Transactional
  public ResponseEntity<?> save(@RequestBody List<E> entidades) {
    try {
      entidades.stream().forEach(entidad -> preGuardar(entidad));
      HttpHeaders header = new HttpHeaders();
      ResponseEntity<?> response = new ResponseEntity<>(repository.saveAll(entidades), header, HttpStatus.CREATED);
      entidades.stream().forEach(entidad -> postGuardar(entidad));
      return response;
    } catch (DataIntegrityViolationException ex) {
      if (ex.getCause() instanceof ConstraintViolationException) {
        ConstraintViolationException constraintViolationEx =
                (ConstraintViolationException) ex.getCause();
        throw new ResponseStatusException(
                HttpStatus.NOT_ACCEPTABLE,
                constraintViolationEx.getSQLException().getLocalizedMessage());
      }
      throw new ResponseStatusException(
              HttpStatus.NOT_ACCEPTABLE, ex.getCause().getLocalizedMessage());
    } catch (jakarta.validation.ConstraintViolationException ex) {
      StringBuilder stb = new StringBuilder();
      for (ConstraintViolation<?> constraintViolation : ex.getConstraintViolations()) {
        stb.append(constraintViolation.getPropertyPath()).append(":");
        stb.append(constraintViolation.getMessage()).append("\n");
      }
      throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, stb.toString());
    }
  }


  @PutMapping
  @Transactional
  public ResponseEntity<?> update(@RequestBody E entidad) {
    ResultadoException resultadoException = validaActualizar(entidad);
    try {
      preActualizar(entidad);

      if (!resultadoException.debeArrojarse()) {
        HttpHeaders header = new HttpHeaders();
        ResponseEntity<?> response =
                new ResponseEntity<>(repository.save(entidad), header, HttpStatus.CREATED);
        postActualizar(entidad);
        return response;
      } else {
        StringBuilder stb = new StringBuilder();
        for (MensajeErrorUsuario error : resultadoException.getErrores()) {
          stb.append(error.getMsgId());
        }
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, stb.toString());
      }

    } catch (DataIntegrityViolationException ex) {
      if (ex.getCause() instanceof ConstraintViolationException) {
        ConstraintViolationException constraintViolationEx =
                (ConstraintViolationException) ex.getCause();
        throw new ResponseStatusException(
                HttpStatus.NOT_ACCEPTABLE,
                constraintViolationEx.getSQLException().getLocalizedMessage());
      }
      throw new ResponseStatusException(
              HttpStatus.NOT_ACCEPTABLE, ex.getCause().getLocalizedMessage());
    }
  }

  @PutMapping("/all")
  @Transactional
  public ResponseEntity<?> update(@RequestBody List<E> entidades) {
    try {
      entidades.stream().forEach(entidad -> preActualizar(entidad));
      HttpHeaders header = new HttpHeaders();
      ResponseEntity<?> response = new ResponseEntity<>(repository.saveAll(entidades), header, HttpStatus.CREATED);
      entidades.stream().forEach(entidad -> postActualizar(entidad));
      return response;
    } catch (DataIntegrityViolationException ex) {
      if (ex.getCause() instanceof ConstraintViolationException) {
        ConstraintViolationException constraintViolationEx =
                (ConstraintViolationException) ex.getCause();
        throw new ResponseStatusException(
                HttpStatus.NOT_ACCEPTABLE,
                constraintViolationEx.getSQLException().getLocalizedMessage());
      }
      throw new ResponseStatusException(
              HttpStatus.NOT_ACCEPTABLE, ex.getCause().getLocalizedMessage());
    } catch (jakarta.validation.ConstraintViolationException ex) {
      StringBuilder stb = new StringBuilder();
      for (ConstraintViolation<?> constraintViolation : ex.getConstraintViolations()) {
        stb.append(constraintViolation.getPropertyPath()).append(":");
        stb.append(constraintViolation.getMessage()).append("\n");
      }
      throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, stb.toString());
    }
  }

  /**
   * Elimina el objeto que tenga el identificador especificado como parámetro
   *
   * @param id
   * @return
   */
  @DeleteMapping(value = "/{id}")
  @Transactional
  public ResponseEntity<?> delete(@PathVariable ID id) {
    validaEliminar(id);
    preEliminar(id);
    try {
      repository.deleteById(id);
      postEliminar(id);
      return ResponseEntity.noContent().build();
    } catch (DataIntegrityViolationException ex) {
      if (ex.getCause() instanceof ConstraintViolationException) {
        ConstraintViolationException constraintViolationEx =
                (ConstraintViolationException) ex.getCause();
        String constraintName = constraintViolationEx.getConstraintName();
        if (constraintName == null) {

          String detailMessage = constraintViolationEx.getSQLException().getLocalizedMessage();
          int fkStart = detailMessage.indexOf("fk_");
          constraintName = detailMessage.substring(fkStart);
          constraintName = constraintName.substring(0, constraintName.indexOf("'"));
        }
        throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, constraintName);
      }
      throw new ResponseStatusException(
              HttpStatus.NOT_ACCEPTABLE, ex.getCause().getLocalizedMessage());
    } catch (Exception e) {
      throw new ResponseStatusException(
              HttpStatus.NOT_ACCEPTABLE, e.getCause().getLocalizedMessage());
    }
  }

  //  @Deprecated
  public Predicate buildPredicate(MultiValueMap<String, String> map) {
    String className = StringUtils.uncapitalize(type.getSimpleName());

    PathBuilder<E> entityPath = new PathBuilder<>(type, className);
    Predicate predicate = null;

    NumberFormat numberFormat = NumberFormat.getInstance();

    for (Map.Entry<String, List<String>> entry : map.entrySet()) {
      Predicate predicateTmp = null;

      Logger.getLogger(className).log(Level.INFO, entry.toString());

      getAllFields(type); //Agrego los atributos de su clase y padres
      String filedType = fieldTypes.get(entry.getKey());

      if (entry.getKey().contains(".")) {
        String stringValue = entry.getValue().get(0);
        String stringValue2 = null;
        if(entry.getValue().size()>1){
          stringValue2 = entry.getValue().get(1);
        }
        if (stringValue.equals(isNull)) {
          predicateTmp = entityPath.get(entry.getKey()).isNull();
        }else if(stringValue.equals(isNotNull)){
          predicateTmp = entityPath.get(entry.getKey()).isNotNull();
        }else if (stringValue.equals("true") || stringValue.equals("false")) {
          predicateTmp = entityPath.getBoolean(entry.getKey()).eq(BooleanUtils.toBoolean(stringValue));
        } else { //Por ejemplo para filtros como proyectoEmpleado.proyecto.gastos - Control de numeros, fechas y strings
          boolean isNumeric = (stringValue != null && stringValue.matches("[0-9]+"));
          boolean isDate =false;
          SimpleDateFormat formatLocalDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
          formatLocalDateTime.setLenient(false);
          try{
            formatLocalDateTime.parse(stringValue);
            isDate=true;
          }catch (ParseException e){
            Logger.getLogger(e.getMessage());
          }
          if(isNumeric){

            Class number2Class = stringValue.getClass();
            predicateTmp = entityPath.getNumber(entry.getKey(), number2Class).eq(Double.parseDouble(stringValue));

          }else if(isDate){
            predicateTmp = entityPath.getDateTime(entry.getKey(), LocalDateTime.class).goe(LocalDateTime.parse(stringValue));
            if(stringValue2!=null){
              predicateTmp = entityPath.getDateTime(entry.getKey(), LocalDateTime.class).between(LocalDateTime.parse(stringValue),LocalDateTime.parse(stringValue2));
            }
          }else{
            predicateTmp = entityPath.getString(entry.getKey()).containsIgnoreCase(stringValue);

          }
        }
      } else {

        if (entry.getKey().equals("page")||entry.getKey().equals("size")||entry.getKey().equals("sort")) {
            continue;
        }

        if(filedType != null) {

          switch (filedType) {
            case "long","integer","double","float","bigdecimal","biginteger":

              if (entry.getValue().size() == 1) {
                // Único valor de consulta
                String stringValue = entry.getValue().get(0);

                if (stringValue.equals(isNull)) {
                  predicateTmp = entityPath.get(entry.getKey()).isNull();
                  break;
                } else if (stringValue.equals(isNotNull)) {
                  predicateTmp = entityPath.get(entry.getKey()).isNotNull();
                  break;
                }

                Number number = null;

                try {
                  if (stringValue.length() > 2 && stringValue.charAt(1) == '=') {
                    number = numberFormat.parse(stringValue.substring(2));
                  } else if (stringValue.length() > 1) {
                    number = numberFormat.parse(stringValue.substring(1));
                  }
                } catch (ParseException ignored) {
                  Logger.getLogger(ignored.getMessage());

                }

                Class numberClass = number != null ? number.getClass() : null;

                switch (stringValue.charAt(0)) {
                  case '<':
                    if (stringValue.charAt(1) == '=') {
                      predicateTmp = entityPath.getNumber(entry.getKey(), numberClass).loe(number);
                    } else {
                      predicateTmp = entityPath.getNumber(entry.getKey(), numberClass).lt(number);
                    }
                    break;
                  case '>':
                    if (stringValue.charAt(1) == '=') {
                      predicateTmp = entityPath.getNumber(entry.getKey(), numberClass).goe(number);
                    } else {
                      predicateTmp = entityPath.getNumber(entry.getKey(), numberClass).gt(number);
                    }
                    break;
                  case '!':
                    predicateTmp = entityPath.getNumber(entry.getKey(), numberClass).ne(number);
                    break;
                  default:
                    Number number2 = null;
                    try {
                      number2 = numberFormat.parse(stringValue);
                      Class number2Class = number2.getClass();
                      predicateTmp = entityPath.getNumber(entry.getKey(), number2Class).eq(number2);
                      break;
                    }catch (NullPointerException exception){
                      Logger.getLogger(exception.getMessage());
                    } catch (ParseException ignored) {
                      Logger.getLogger(ignored.getMessage());

                    }

                }
              } else {
                // Múltiples valores de consulta
                List<Number> valuesNumberIn = new ArrayList<>();
                List<Number> valuesNumberNotIn = new ArrayList<>();
                boolean nulles = false;
                boolean noNulles = false;

                for (String value : entry.getValue()) {
                  if (value.equals(isNull)) {
                    nulles = true;
                  } else if (value.equals(isNotNull)) {
                    noNulles = true;
                  } else if (value.charAt(0) == '!') {
                    Number number = null;
                    try {
                      number = numberFormat.parse(value.substring(1));
                    } catch (ParseException ignored) {
                      Logger.getLogger(ignored.getMessage());

                    }
                    valuesNumberNotIn.add(number);
                  } else {
                    Number number = null;
                    try {
                      number = numberFormat.parse(value);
                    } catch (ParseException ignored) {
                      Logger.getLogger(ignored.getMessage());

                    }
                    valuesNumberIn.add(number);
                  }
                }

                if (filedType.equals(Integer.class.getSimpleName().toLowerCase())) {
                  List<Integer> intList =
                          valuesNumberIn.stream()
                                  .mapToInt(number -> number.intValue())
                                  .boxed()
                                  .collect(Collectors.toList());
                  predicateTmp = entityPath.get(entry.getKey(), Integer.class).in(intList);
                } else if (filedType.equals(Long.class.getSimpleName().toLowerCase())) {
                  List<Long> longList;
                  if (!valuesNumberIn.isEmpty()) {
                    longList =
                            valuesNumberIn.stream()
                                    .mapToLong(number -> number.longValue())
                                    .boxed()
                                    .collect(Collectors.toList());
                    predicateTmp = entityPath.get(entry.getKey(), Long.class).in(longList);
                  } else {
                    if (!valuesNumberNotIn.isEmpty()) {
                      longList =
                              valuesNumberNotIn.stream()
                                      .mapToLong(number -> number.longValue())
                                      .boxed()
                                      .collect(Collectors.toList());
                      predicateTmp = entityPath.get(entry.getKey(), Long.class).notIn(longList);
                    }
                  }

                } else if (filedType.equals(BigInteger.class.getSimpleName().toLowerCase())) {
                  List<BigInteger> bigIntegerList =
                          valuesNumberIn.stream()
                                  .map(number -> BigInteger.valueOf(number.longValue()))
                                  .collect(Collectors.toList());
                  predicateTmp = entityPath.get(entry.getKey(), BigInteger.class).in(bigIntegerList);
                } else if (filedType.equals(Float.class.getSimpleName().toLowerCase())) {
                  List<Float> floatList =
                          valuesNumberIn.stream()
                                  .map(number -> number.floatValue())
                                  .collect(Collectors.toList());
                  predicateTmp = entityPath.get(entry.getKey(), Float.class).in(floatList);
                } else if (filedType.equals(Double.class.getSimpleName().toLowerCase())) {
                  List<Double> doubleList =
                          valuesNumberIn.stream()
                                  .mapToDouble(number -> number.doubleValue())
                                  .boxed()
                                  .collect(Collectors.toList());
                  predicateTmp = entityPath.get(entry.getKey(), Double.class).in(doubleList);
                } else if (filedType.equals(BigDecimal.class.getSimpleName().toLowerCase())) {
                  List<BigDecimal> bigDecimalList =
                          valuesNumberIn.stream()
                                  .map(number -> BigDecimal.valueOf(number.doubleValue()))
                                  .collect(Collectors.toList());
                  predicateTmp = entityPath.get(entry.getKey(), BigDecimal.class).in(bigDecimalList);
                }

                if (nulles) {
                  predicateTmp =
                          ExpressionUtils.or(predicateTmp, entityPath.get(entry.getKey()).isNull());
                } else if (noNulles) {
                  predicateTmp =
                          ExpressionUtils.or(predicateTmp, entityPath.get(entry.getKey()).isNotNull());
                }
              }
              break;
            case "string":
              if (entry.getValue().size() == 1) {
                // Único valor de consulta
                String stringValue = entry.getValue().get(0);

                if (stringValue.equals(isNull)) {
                  predicateTmp = entityPath.get(entry.getKey()).isNull();
                  break;
                } else if (stringValue.equals(isNotNull)) {
                  predicateTmp = entityPath.get(entry.getKey()).isNotNull();
                  break;
                }

                switch (stringValue.charAt(0)) {
                  case '<':
                    predicateTmp =
                            stringValue.charAt(1) == '='
                                    ? entityPath.getString(entry.getKey()).loe(stringValue.substring(2))
                                    : entityPath.getString(entry.getKey()).lt(stringValue.substring(1));
                    break;
                  case '>':
                    predicateTmp =
                            stringValue.charAt(1) == '='
                                    ? entityPath.getString(entry.getKey()).goe(stringValue.substring(2))
                                    : entityPath.getString(entry.getKey()).gt(stringValue.substring(1));
                    break;
                  case ':':
                    predicateTmp =
                            stringValue.charAt(1) == '!'
                                    ? entityPath.getString(entry.getKey()).ne(stringValue.substring(2))
                                    : entityPath.getString(entry.getKey()).eq(stringValue.substring(1));
                    break;
                  case '!':
                    predicateTmp =
                            entityPath
                                    .getString(entry.getKey())
                                    .notLike("%" + stringValue.substring(1) + "%");
                    break;
                  default:
                    predicateTmp =
                            entityPath.getString(entry.getKey()).containsIgnoreCase(stringValue);
                    break;
                }
              } else {

                Map<Boolean, List<String>> asdf =
                        entry.getValue().stream()
                                .collect(Collectors.partitioningBy(o -> o.charAt(0) != '!'));

                if (!asdf.get(true).isEmpty()) {
                  predicateTmp = entityPath.get(entry.getKey()).in(asdf.get(true));
                }
                if (!asdf.get(false).isEmpty()) {
                  predicateTmp = entityPath.get(entry.getKey()).notIn(asdf.get(true));
                }
              }
              break;

            case "localdate":

              if (entry.getValue().size() == 1) {
                String stringValue = entry.getValue().get(0);

                if (stringValue.equals(isNull)) {
                  predicateTmp = entityPath.get(entry.getKey()).isNull();
                  break;
                } else if (stringValue.equals(isNotNull)) {
                  predicateTmp = entityPath.get(entry.getKey()).isNotNull();
                  break;
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                LocalDate localDateValue1 = LocalDate.now();

                try {
                  localDateValue1 = LocalDate.parse(stringValue, formatter);
                } catch (DateTimeParseException ex) {
                  Logger.getLogger(ex.getMessage());
                }

                switch (stringValue.charAt(0)) {
                  case '<':
                    predicateTmp =
                            stringValue.charAt(1) == '='
                                    ? entityPath.getDate(entry.getKey(), LocalDate.class).loe(localDateValue1)
                                    : entityPath.getDate(entry.getKey(), LocalDate.class).lt(localDateValue1);
                    break;
                  case '>':
                    predicateTmp =
                            stringValue.charAt(1) == '='
                                    ? entityPath.getDate(entry.getKey(), LocalDate.class).goe(localDateValue1)
                                    : entityPath.getDate(entry.getKey(), LocalDate.class).gt(localDateValue1);
                    break;
                  case '!':
                    predicateTmp =
                            entityPath.getDate(entry.getKey(), LocalDate.class).ne(localDateValue1);
                    break;
                  default:
                    LocalDate localDateValue = LocalDate.parse(stringValue, formatter);
                    predicateTmp =
                            entityPath.getDate(entry.getKey(), LocalDate.class).eq(localDateValue);
                    break;
                }
              } else if (entry.getValue().size() == 2) {
                String stringDate1 = entry.getValue().get(0);
                String stringDate2 = entry.getValue().get(1);

                List<LocalDate> dates = new ArrayList<>();
                try {
                  LocalDate date1 = LocalDate.parse(stringDate1);
                  LocalDate date2 = LocalDate.parse(stringDate2);

                  dates.add(date1);
                  dates.add(date2);
                  Collections.sort(dates);

                  predicateTmp =
                          entityPath
                                  .getDateTime(entry.getKey(), LocalDate.class)
                                  .between(dates.get(0), dates.get(1));

                  break;
                } catch (DateTimeParseException ignored) {
                  Logger.getLogger(ignored.getMessage());

                }

              }
              break;

            case "datetime","localdatetime","date":
              String[] parsePatterns = {"dd-MM-yyyy hh:mm", "dd-MM-yyyy"};

              if (entry.getValue().size() == 1) {
                String stringValue = entry.getValue().get(0);

                if (stringValue.equals(isNull)) {
                  predicateTmp = entityPath.get(entry.getKey()).isNull();
                  break;
                } else if (stringValue.equals(isNotNull)) {
                  predicateTmp = entityPath.get(entry.getKey()).isNotNull();
                  break;
                }

                Date dateValue1 = null;
                Date dateValue2 = null;

                for (String pattern : parsePatterns) {
                  SimpleDateFormat parser = new SimpleDateFormat(pattern);
                  try {
                    dateValue1 = parser.parse(stringValue.substring(1));
                    dateValue2 = parser.parse(stringValue.substring(2));
                    break;
                  } catch (ParseException ignored) {
                    Logger.getLogger(ignored.getMessage());

                  }
                }

                switch (stringValue.charAt(0)) {
                  case '<':
                    predicateTmp =
                            stringValue.charAt(1) == '='
                                    ? entityPath.getDate(entry.getKey(), Date.class).loe(dateValue2)
                                    : entityPath.getDate(entry.getKey(), Date.class).lt(dateValue1);
                    break;
                  case '>':
                    predicateTmp =
                            stringValue.charAt(1) == '='
                                    ? entityPath.getDate(entry.getKey(), Date.class).goe(dateValue2)
                                    : entityPath.getDate(entry.getKey(), Date.class).gt(dateValue1);
                    break;
                  case '!':
                    predicateTmp = entityPath.getDate(entry.getKey(), Date.class).ne(dateValue1);
                    break;
                  default:
                    for (String pattern : parsePatterns) {
                      SimpleDateFormat parser = new SimpleDateFormat(pattern);
                      try {
                        predicateTmp =
                                entityPath
                                        .getDate(entry.getKey(), Date.class)
                                        .eq(parser.parse(stringValue));
                        break;
                      } catch (ParseException ignored) {
                        Logger.getLogger(ignored.getMessage());
                      }
                    }
                    break;
                }
              } else if (entry.getValue().size() == 2) {
                String stringDate1 = entry.getValue().get(0);
                String stringDate2 = entry.getValue().get(1);

                List<LocalDateTime> dates = new ArrayList<>();

                LocalDateTime date1 = LocalDateTime.parse(stringDate1);
                LocalDateTime date2 = LocalDateTime.parse(stringDate2);

                dates.add(date1);
                dates.add(date2);
                Collections.sort(dates);

                predicateTmp =
                        entityPath
                                .getDateTime(entry.getKey(), LocalDateTime.class)
                                .between(dates.get(0), dates.get(1));
                break;
              } else {
                // Múltiples valores de consulta
                List<Date> valuesDateIn = new ArrayList<>();
                List<Date> valuesDateNotIn = new ArrayList<>();
                for (String value : entry.getValue()) {

                  value = value.replaceAll(">", "");
                  value = value.replaceAll(">=", "");
                  value = value.replaceAll("<", "");
                  value = value.replaceAll("<=", "");

                  Date dateValue = null;
                  Date dateValue1 = null;

                  for (String pattern : parsePatterns) {
                    SimpleDateFormat parser = new SimpleDateFormat(pattern);
                    try {
                      dateValue = parser.parse(value.substring(1));
                      dateValue1 = parser.parse(value.substring(1).substring(2));
                      break;
                    } catch (ParseException ignored) {
                      Logger.getLogger(ignored.getMessage());
                    }
                  }

                  if (value.charAt(0) == '!') {
                    valuesDateNotIn.add(dateValue1);
                  } else {
                    valuesDateIn.add(dateValue);
                  }
                }
                if (!valuesDateIn.isEmpty()) {
                  predicateTmp = entityPath.get(entry.getKey()).in(valuesDateIn);
                }
                if (!valuesDateNotIn.isEmpty()) {
                  predicateTmp = entityPath.get(entry.getKey()).notIn(valuesDateNotIn);
                }
              }
              break;
            case "boolean":
              String stringValue = entry.getValue().get(0);
              boolean booleanValue = Boolean.parseBoolean(stringValue);

              if (stringValue.equals(isNull)) {
                predicateTmp = entityPath.get(entry.getKey()).isNull();
              } else if (stringValue.equals(isNotNull)) {
                predicateTmp = entityPath.get(entry.getKey()).isNotNull();
              } else if (booleanValue) {
                predicateTmp = entityPath.getBoolean(entry.getKey()).isTrue();
              } else {
                predicateTmp = entityPath.getBoolean(entry.getKey()).isFalse();
              }
              break;
            default:
              String stringObjectValue = entry.getValue().get(0);
              if (stringObjectValue.equals(isNull)) {
                predicateTmp = entityPath.get(entry.getKey()).isNull();
              } else if (stringObjectValue.equals(isNotNull)) {
                predicateTmp = entityPath.get(entry.getKey()).isNotNull();
              } else {
                predicateTmp = customBuildPrecicate(entry.getKey(), stringObjectValue);
              }
          }
        } else {
          String stringObjectValue = entry.getValue().get(0);
          predicateTmp = customBuildPrecicate(entry.getKey(), stringObjectValue);
        }
      }

      if (predicate == null) {
        predicate = predicateTmp;
      } else {
        predicate = ExpressionUtils.and(predicate, predicateTmp);
      }
    }
    // Spring Boot 2.2.3, requiere que el predicado sea no nulo
    if (predicate == null) {
      predicate = Expressions.asBoolean(true).isTrue();
    }

    return predicate;
  }

  public void getAllFields(Class<?> type){

    for (Field field : type.getDeclaredFields()) {
      fieldTypes.put(field.getName(), field.getType().getSimpleName().toLowerCase());
    }
    if(type.getSuperclass() !=null){
      getAllFields(type.getSuperclass());
    }
  }

  protected Predicate customBuildPrecicate(String property, String value) {
    Logger.getLogger(property+" "+value);
    return null;
  }

  @GetMapping(value = "/search")
  @ResponseBody
  public Page<E> search(@RequestParam MultiValueMap<String, String> queryPath, Pageable pageable) {
    EntidadSpecification<E> spec = new EntidadSpecification<>();
    log.info(queryPath);

    List<SearchCriteria> criterias = SearchCriteria.fromQueryPath(queryPath);
    spec.setCriterias(criterias);

    JpaSpecificationExecutor<E> executor = (JpaSpecificationExecutor) repository;
    return executor.findAll(spec, pageable);
  }

}
