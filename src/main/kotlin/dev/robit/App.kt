package dev.robit

import dev.robit.model.User
import org.hibernate.SessionFactory
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.hibernate.cfg.Configuration
import javax.persistence.EntityManagerFactory
import javax.persistence.Persistence

fun main(args: Array<String>) {
    createSessionFactory().use { sessionFactory ->
        sessionFactory.openSession().use { session ->
            session.beginTransaction()
            val user = User()
            user.firstName = "first_name"
            user.lastName = "last_name"
            user.username = "username"
            session.persist(user)
            session.transaction.commit()
        }
    }
    println("Ok !")
}


fun getSessionFactory(e: EntityManagerFactory): SessionFactory {
    return e.unwrap(SessionFactory::class.java)
}

fun createSessionFactory(): SessionFactory {
    val configuration = Configuration()
    configuration.configure().addAnnotatedClass(User::class.java)
    val registry = StandardServiceRegistryBuilder().applySettings(configuration.properties).build()
    return configuration.buildSessionFactory(registry)
}

fun createEntityManagerFactory(): EntityManagerFactory {
    return Persistence.createEntityManagerFactory("persistence_unit")
}

fun createEntityManagerFactoryFromHibernateConfig(): EntityManagerFactory {
    val configuration = Configuration()
    configuration.configure().addAnnotatedClass(User::class.java)
    val map = mutableMapOf<String, String>()
    val names = configuration.properties.propertyNames()
    while (names.hasMoreElements()) {
        val element = names.nextElement() as String
        map[element] = configuration.properties.getProperty(element)
    }
    return Persistence.createEntityManagerFactory("persistence_unit", map)
}
