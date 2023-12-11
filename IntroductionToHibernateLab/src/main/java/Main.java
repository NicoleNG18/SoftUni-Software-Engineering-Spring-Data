import entities.Student;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Main {
    public static void main(String[] args) {

        Configuration configuration=new Configuration();
        configuration.configure();

        SessionFactory sessionFactory= configuration.buildSessionFactory();

        Session session= sessionFactory.openSession();

        session.beginTransaction();

        Student example=new Student();
        example.setName("ivan");
        session.persist(example);

//        Student fromDb = session.get(Student.class, 0);
//        int id = fromDb.getId();
//        String name = fromDb.getName();
//        System.out.printf("%d %s",id,name);


        session.getTransaction().commit();
        session.close();
    }
}

