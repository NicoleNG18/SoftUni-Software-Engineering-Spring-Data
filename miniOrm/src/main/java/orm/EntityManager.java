package orm;

import orm.annotations.Column;
import orm.annotations.Entity;
import orm.annotations.Id;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EntityManager<E> implements ORMContext<E> {
    private Connection connection;

    private static final String SQL_QUERY="INSERT INTO %s (%s) VALUES (%s)";

    public EntityManager(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean persist(E entity) throws SQLException, IllegalAccessException {
        String tableName = this.getTableName(entity.getClass());
        String fieldList = this.getFieldsWithouthId(entity);
        String valueList = this.getValues(entity);

        return this.connection.prepareStatement(String.format(SQL_QUERY
                , tableName, fieldList, valueList)).execute();
    }

    @Override
    public Iterable<E> find(Class<E> entityType) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return find(entityType,"");
    }

    @Override
    public Iterable<E> find(Class<E> entityType, String whereClause) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String tableName = this.getTableName(entityType);

        String sql = String.format("SELECT * FROM %s %s"
                , tableName, whereClause.equals("") ? "" : "WHERE " + whereClause);

        ResultSet resultSet = this.connection.prepareStatement(sql).executeQuery();

        List<E> result=new ArrayList<>();

        E lastResult=this.createEntity(entityType,resultSet);

        while (lastResult!=null){
            result.add(lastResult);
            lastResult=this.createEntity(entityType,resultSet);
        }

        return result;
    }

    @Override
    public E findFirst(Class<E> entityType) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return findFirst(entityType, "");
    }

    @Override
    public E findFirst(Class<E> entityType, String whereClause) throws SQLException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        String tableName = this.getTableName(entityType);

        String sql = String.format("SELECT * FROM %s %s LIMIT 1"
                , tableName, whereClause.equals("") ? "" : "WHERE " + whereClause);

        ResultSet resultSet = this.connection.prepareStatement(sql).executeQuery();

        return this.createEntity(entityType, resultSet);
    }

    private E createEntity(Class<E> entityType, ResultSet resultSet) throws SQLException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (!resultSet.next()) {
            return null;
        }

        E entity = entityType.getDeclaredConstructor().newInstance();

        Field[] declaredFields = entityType.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getClass().isAnnotationPresent(Column.class)) {

                String fieldName = field.getAnnotation(Column.class).name();
                String value = resultSet.getString(fieldName);

                this.fillData(entity, field, value);
            } else if (field.getClass().isAnnotationPresent(Id.class)) {
                //can get out the repeated strings
                String fieldName = field.getName();
                String value = resultSet.getString(fieldName);

                this.fillData(entity, field, value);
            }
        }

        return entity;
    }

    private E fillData(E entity, Field field, String value) throws IllegalAccessException {
        field.setAccessible(true);

        if (field.getType() == long.class || field.getType() == Long.class) {
            field.setLong(entity, Long.parseLong(value));
        } else if (field.getType() == LocalDate.class) {
            field.set(entity, LocalDate.parse(value));
        } else if (field.getType() == int.class || field.getType() == Integer.class) {
            field.setInt(entity, Integer.parseInt(value));
        } else if (field.getType() == String.class) {
            field.set(entity, value);
        } else {
            throw new ORMException("Unsupported type: " + field.getType());
        }
        return  entity;
    }

    private String getTableName(Class<?> clazz) {
        Entity annotation = clazz.getAnnotation(Entity.class);
        if (annotation == null) {
            throw new ORMException("The class doesn't have that annotation");
        }
        return annotation.name();
    }

    private String getFieldsWithouthId(E entity) {

        return Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(f -> f.getAnnotation(Column.class) != null)
                .map(f -> f.getAnnotation(Column.class).name())
                .collect(Collectors.joining(", "));
    }

    private String getValues(E entity) throws IllegalAccessException {
        Field[] fields = entity.getClass().getDeclaredFields();

        List<String> result = new ArrayList<>();

        for (Field field : fields) {
            if (field.getAnnotation(Column.class) == null) {
                continue;
            }

            field.setAccessible(true);
            Object value = field.get(entity);
            result.add("\"" + value.toString() + "\"");
        }
        return String.join(", ", result);
    }
}
