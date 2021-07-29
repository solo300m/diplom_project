package com.example.MyProjectWithSecurity.Service;

import com.example.MyProjectWithSecurity.data_test.Book2Authors;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

@Service
public class HibernateService {
    EntityManagerFactory entityManagerFactory;

    @Autowired
    public HibernateService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public List<Book2Authors> getBookData2(){
        List<Object[]>list = new ArrayList<>();
        List<Book2Authors>rezList = new ArrayList<>();
        Session session = entityManagerFactory.createEntityManager().unwrap(Session.class);
        EntityManager em = entityManagerFactory.createEntityManager();
        Transaction tx = null;
        try{
            tx = session.beginTransaction();
            list = em.createQuery("select b.id,b.author.id,a.author,b.title from Book b, Authors a where b.author.id=a.id").getResultList();
            for(Object[] obj:list){
                rezList.add(new Book2Authors((Integer) obj[0], (Integer) obj[1], (String) obj[2], (String) obj[3]));
            }
            tx.commit();
        }catch (HibernateException hex){
            if(tx != null){
                tx.rollback();
            }else{
                hex.printStackTrace();
            }
        }finally {
            session.close();
        }
        /*for (Book2Authors b:rezList){
            Logger.getLogger(HibernateService.class.getName()).info(b.toString());
        }*/

        return rezList;
    }
}
