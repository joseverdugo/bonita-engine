package com.bonitasoft.engine.bdm;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.bonitasoft.engine.bdm.model.BusinessObject;
import com.bonitasoft.engine.bdm.model.Query;
import com.bonitasoft.engine.bdm.model.UniqueConstraint;
import com.bonitasoft.engine.bdm.model.field.Field;
import com.bonitasoft.engine.bdm.model.field.FieldType;
import com.bonitasoft.engine.bdm.model.field.RelationField;
import com.bonitasoft.engine.bdm.model.field.RelationField.FetchType;
import com.bonitasoft.engine.bdm.model.field.RelationField.Type;
import com.bonitasoft.engine.bdm.model.field.SimpleField;

/**
 * @author Romain Bioteau
 */
public class BDMQueryUtilTest {

    @Test
    public void should_createQueryNameForUniqueConstraint_return_queryname() throws Exception {
        final UniqueConstraint uniqueConstraint = new UniqueConstraint();
        uniqueConstraint.setFieldNames(Arrays.asList("name"));
        final String queryNameForUniqueConstraint = BDMQueryUtil.createQueryNameForUniqueConstraint(uniqueConstraint);
        assertThat(queryNameForUniqueConstraint).isEqualTo("findByName");

    }

    @Test
    public void should_createQueryContentForUniqueConstraint_return_query_content_with_parameters() throws Exception {
        final UniqueConstraint uniqueConstraint = new UniqueConstraint();
        uniqueConstraint.setFieldNames(Arrays.asList("name"));
        final String queryContentForUniqueConstraint = BDMQueryUtil.createQueryContentForUniqueConstraint("org.bonita.Employee", uniqueConstraint);
        assertThat(queryContentForUniqueConstraint).isNotEmpty().isEqualTo("SELECT e\nFROM Employee e\nWHERE e.name= :name\n");
    }

    @Test
    public void createProvidedQueriesForBOShouldNotGenerateGetAllForUniqueConstraint() throws Exception {
        // given:
        final BusinessObject bo = new BusinessObject();
        bo.setQualifiedName("com.corp.Arrival");
        final SimpleField field = new SimpleField();
        field.setName("people");
        field.setType(FieldType.INTEGER);
        bo.addField(field);
        bo.addUniqueConstraint("someName", "people");

        // when:
        final List<Query> queries = BDMQueryUtil.createProvidedQueriesForBusinessObject(bo);

        // then:
        assertThat(getQueryNames(queries)).doesNotContain("getAllArrivalByPeople");
    }

    @Test
    public void createProvidedQueriesForBOWithOneUniqueAndOneNonUniqueFieldShouldGenerate3Queries() throws Exception {
        // given:
        final BusinessObject bo = new BusinessObject();
        bo.setQualifiedName("com.corp.Arrival");
        bo.addField(aStringField("unikAttr"));
        bo.addField(aStringField("unconstrainedAttr"));
        bo.addUniqueConstraint("someConstraintName", "unikAttr");

        // when:
        final List<Query> queries = BDMQueryUtil.createProvidedQueriesForBusinessObject(bo);

        // then:
        assertThat(queries).hasSize(3);
    }

    @Test
    public void createProvidedQueriesForShouldNotGenerateAQueryForRelationField() throws Exception {
        // given:
        final BusinessObject bo = new BusinessObject();
        bo.setQualifiedName("com.corp.Arrival");
        bo.addField(aStringField("unikAttr"));
        bo.addField(aRelationField("employee"));

        // when:
        final List<Query> queries = BDMQueryUtil.createProvidedQueriesForBusinessObject(bo);

        // then:
        assertThat(queries).hasSize(2);
    }

    protected SimpleField aStringField(final String name) {
        final SimpleField field = new SimpleField();
        field.setName(name);
        field.setType(FieldType.STRING);
        return field;
    }

    protected RelationField aRelationField(final String name) {
        final RelationField field = new RelationField();
        field.setName(name);
        field.setType(Type.COMPOSITION);
        field.setReference(null);
        return field;
    }

    private List<String> getQueryNames(final List<Query> queries) {
        return extract(queries, on(Query.class).getName());
    }

    @Test
    public void should_createQuerForUniqueConstraint_return_query_with_parameters() throws Exception {
        final BusinessObject bo = new BusinessObject();
        bo.setQualifiedName("org.bonita.Employee");
        final SimpleField field = new SimpleField();
        field.setName("name");
        field.setType(FieldType.STRING);
        bo.addField(field);
        final UniqueConstraint uniqueConstraint = new UniqueConstraint();
        uniqueConstraint.setFieldNames(Arrays.asList("name"));

        final Query query = BDMQueryUtil.createQueryForUniqueConstraint(bo, uniqueConstraint);
        assertThat(query).isNotNull();
        assertThat(query.getContent()).isEqualTo("SELECT e\nFROM Employee e\nWHERE e.name= :name\n");
        assertThat(query.getName()).isEqualTo("findByName");
        assertThat(query.getReturnType()).isEqualTo(bo.getQualifiedName());
        assertThat(query.getQueryParameters()).hasSize(1);
    }

    @Test
    public void should_createQueryForField_return_query_with_parameters() throws Exception {
        final BusinessObject bo = new BusinessObject();
        bo.setQualifiedName("org.bonita.Employee");
        final SimpleField field = new SimpleField();
        field.setName("name");
        field.setType(FieldType.STRING);
        bo.addField(field);

        final Query query = BDMQueryUtil.createQueryForField(bo, field);
        assertThat(query).isNotNull();
        assertThat(query.getContent()).contains("SELECT e\nFROM Employee e\nWHERE e.name= :name\nORDER BY e.persistenceId");
        assertThat(query.getName()).isEqualTo("findByName");
        assertThat(query.getReturnType()).isEqualTo(List.class.getName());
        assertThat(query.getQueryParameters()).hasSize(1);
    }

    @Test
    public void should_createSelectAllQueryreturn_query_without_parameters() throws Exception {
        final BusinessObject bo = new BusinessObject();
        bo.setQualifiedName("org.bonita.Employee");
        final SimpleField field = new SimpleField();
        field.setName("name");
        field.setType(FieldType.STRING);
        bo.addField(field);

        final Query query = BDMQueryUtil.createSelectAllQueryForBusinessObject(bo);
        assertThat(query).isNotNull();
        assertThat(query.getContent()).contains("SELECT e\nFROM Employee e");
        assertThat(query.getContent()).doesNotContain("WHERE");
        assertThat(query.getName()).isEqualTo("find");
        assertThat(query.getReturnType()).isEqualTo(List.class.getName());
        assertThat(query.getQueryParameters()).isEmpty();
    }

    @Test
    public void should_getAllProvidedQueriesNameForBusinessObject_not_return_query_for_RelationFields() throws Exception {
        final BusinessObject bo = new BusinessObject();
        bo.setQualifiedName("org.bonita.Employee");
        final SimpleField field = new SimpleField();
        field.setName("name");
        field.setType(FieldType.STRING);
        bo.addField(field);

        final RelationField f2 = new RelationField();
        f2.setName("name2");
        f2.setType(Type.COMPOSITION);
        f2.setReference(bo);
        bo.addField(f2);

        assertThat(BDMQueryUtil.getAllProvidedQueriesNameForBusinessObject(bo)).containsExactly("findByName", "find");
    }

    @Test
    public void createSelectAllQueryShouldGenerateOrderByPersistenceId() throws Exception {
        // when:
        final String queryContent = BDMQueryUtil.createSelectAllQueryContent("MyBizObject");
        // then:
        assertThat(queryContent).contains("ORDER BY m.persistenceId");
    }

    @Test
    public void createQueryContentForFieldShouldGenerateOrderByPersistenceId() throws Exception {
        // when:
        final String queryContent = BDMQueryUtil.createQueryContentForField("NerfSurvey", new SimpleField());
        // then:
        assertThat(queryContent).contains("ORDER BY n.persistenceId");
    }

    @Test
    public void createDefaultQueryForLazyFieldShouldGenerateValidQuery() throws Exception {
        //given:

        final BusinessObject addressBo = new BusinessObject();
        addressBo.setQualifiedName("org.bonita.Address");
        final SimpleField streetField = new SimpleField();
        streetField.setName("street");
        streetField.setType(FieldType.STRING);
        addressBo.addField(streetField);

        final BusinessObject bo = new BusinessObject();
        bo.setQualifiedName("org.bonita.Employee");
        final SimpleField field = new SimpleField();
        field.setName("name");
        field.setType(FieldType.STRING);
        bo.addField(field);

        final RelationField f2 = new RelationField();
        f2.setName("manager");
        f2.setType(Type.AGGREGATION);
        f2.setReference(bo);
        f2.setFetchType(FetchType.LAZY);
        bo.addField(f2);

        final RelationField f3 = new RelationField();
        f3.setName("addresses");
        f3.setType(Type.COMPOSITION);
        f3.setCollection(true);
        f3.setReference(addressBo);
        f3.setFetchType(FetchType.LAZY);
        bo.addField(f3);

        // when:
        Query query = BDMQueryUtil.createQueryForLazyField(bo, f2);
        // then:
        assertThat(query).isNotNull();
        assertThat(query.getName()).isEqualTo("findManagerByEmployeePersistenceId");
        assertThat(query.getContent()).isEqualTo("SELECT e.manager\nFROM Employee e\nWHERE e.persistenceId= :persistenceId");
        assertThat(query.getReturnType()).isEqualTo(bo.getQualifiedName());
        assertThat(query.getQueryParameters()).extracting("name", "className").contains(tuple(Field.PERSISTENCE_ID, Long.class.getName()));

        // when:
        query = BDMQueryUtil.createQueryForLazyField(bo, f3);
        // then:
        assertThat(query).isNotNull();
        assertThat(query.getName()).isEqualTo("findAddressesByEmployeePersistenceId");
        assertThat(query.getContent()).isEqualTo("SELECT e.addresses\nFROM Employee e\nWHERE e.persistenceId= :persistenceId");
        assertThat(query.getReturnType()).isEqualTo(List.class.getName());
        assertThat(query.getQueryParameters()).extracting("name", "className").contains(tuple(Field.PERSISTENCE_ID, Long.class.getName()));
    }


}
