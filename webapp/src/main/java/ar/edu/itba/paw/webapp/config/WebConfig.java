package ar.edu.itba.paw.webapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

@EnableAsync
@EnableTransactionManagement
@EnableWebMvc
@ComponentScan({ "ar.edu.itba.paw.webapp.controller", "ar.edu.itba.paw.services", "ar.edu.itba.paw.persistence",
        "ar.edu.itba.paw.webapp.config", "ar.edu.itba.paw.webapp.constants",})
@Configuration
@PropertySources({
        @PropertySource(value = "classpath:properties/db.properties"),
        @PropertySource(value = "classpath:properties/email.properties"),
        @PropertySource(value = "classpath:properties/url.properties"),
        @PropertySource(value = "classpath:properties/token.properties"),
        //If it is conflict in properties it keep the last one
})
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private Environment environment;

    @Value("classpath:schema.sql")
    private org.springframework.core.io.Resource schemaSQL;

    /* Production */
//    @Bean
//    public DataSource dataSource() {
//        final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
//        dataSource.setDriverClass(org.postgresql.Driver.class);
//        dataSource.setUrl("jdbc:postgresql://10.16.1.110:5432/paw-2018b-04");
//        dataSource.setUsername("paw-2018b-04");
//        dataSource.setPassword("oc7Yzau4N");
//
//        return dataSource;
//    }

    @Bean
    public DataSource dataSource() {
        String dbHost  = environment.getRequiredProperty("db.host");
        Integer dbPort = environment.getRequiredProperty("db.port", Integer.class);
        String dbName  = environment.getRequiredProperty("db.name");
        String dbUsername = environment.getRequiredProperty("db.username");
        String dbPassword = environment.getRequiredProperty("db.pass");

        final SimpleDriverDataSource dataSource = new SimpleDriverDataSource();
        dataSource.setDriverClass(org.postgresql.Driver.class);
        dataSource.setUrl("jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);

        return dataSource;
    }

//    @Bean
//    public DataSourceInitializer dataSourceInitializer(final DataSource ds) {
//        final DataSourceInitializer dsi = new DataSourceInitializer();
//        dsi.setDataSource(ds);
//        dsi.setDatabasePopulator(databasePopulator());
//        return dsi;
//    }

//    DatabasePopulator databasePopulator() {
//        final ResourceDatabasePopulator dbp = new ResourceDatabasePopulator();
//        dbp.setSeparator("/;");
//        dbp.addScript(schemaSQL);
//        return dbp;
//    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/media/**").addResourceLocations("/static/media/");
        registry.addResourceHandler("/static/css/**").addResourceLocations("/static/css/");
        registry.addResourceHandler("/static/js/**").addResourceLocations("/static/js/");
        registry.addResourceHandler("/index.html").addResourceLocations("/index.html");
    }

    @Bean
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.displayName());
        messageSource.setCacheSeconds(600); /* Production */
        return messageSource;
    }

//    @Bean
//    public PlatformTransactionManager transactionManager(final DataSource ds) {
//        return new DataSourceTransactionManager(ds);
//    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }


    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(environment.getRequiredProperty("email.username"));
        mailSender.setPassword(environment.getRequiredProperty("email.pass"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
        final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan("ar.edu.itba.paw.models", "ar.edu.itba.paw.persistence");

        factoryBean.setDataSource(dataSource());

        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(vendorAdapter);

        final Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL92Dialect");

        factoryBean.setJpaProperties(properties);

        return factoryBean;
    }
}
