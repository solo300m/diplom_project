package com.example.MyProjectWithSecurity.data_test;

import org.springframework.stereotype.Repository;

@Repository
public class TestEntityDao extends AbstractHibernateDao<TestEntity> {
    public TestEntityDao(){
        super();
        setClazz(TestEntity.class);
    }

}
