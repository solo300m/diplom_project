package com.example.MyProjectWithSecurity.config;

import com.example.MyProjectWithSecurity.data_test.TestEntity;

import com.example.MyProjectWithSecurity.data_test.TestEntityCrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

import java.util.logging.Logger;

//@Configuration
public class CommandLineRunnerImpl implements CommandLineRunner {

    TestEntityCrudRepository testEntityCrudRepository;

    @Autowired
    public CommandLineRunnerImpl(TestEntityCrudRepository testEntityCrudRepository) {
        this.testEntityCrudRepository = testEntityCrudRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        for (int i=0;i<5;i++){
            createTestEntity(new TestEntity());
        }

        TestEntity readTestEntity = readTestEntityById(3L);
        if(readTestEntity != null){
            Logger.getLogger(CommandLineRunnerImpl.class.getSimpleName()).info(readTestEntity.toString());
        }else{
            throw new NullPointerException();
        }

        TestEntity updateTestEntity = updateTestEntityById(5L);
        if(readTestEntity != null){
            Logger.getLogger(CommandLineRunnerImpl.class.getSimpleName()).info(updateTestEntity.toString());
        }else{
            throw new NullPointerException();
        }
    }

    private TestEntity updateTestEntityById(Long id) {
       TestEntity testEntity = testEntityCrudRepository.findById(id).get();
       testEntity.setData("NEW DATA");
       testEntityCrudRepository.save(testEntity);
       return testEntity;
    }

    private TestEntity readTestEntityById(Long id) {
        return testEntityCrudRepository.findById(id).get();
    }

    private void createTestEntity(TestEntity entity) {
        entity.setData(entity.getClass().getSimpleName()+entity.hashCode());
        testEntityCrudRepository.save(entity);
    }

    private void deleteTestEntityById(Long id){
        TestEntity testEntity = testEntityCrudRepository.findById(id).get();
        testEntityCrudRepository.delete(testEntity);
    }
}
