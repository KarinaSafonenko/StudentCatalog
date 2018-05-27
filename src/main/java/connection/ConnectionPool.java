package connection;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public final class ConnectionPool implements Serializable, Cloneable {
    private static Logger logger = LogManager.getLogger(ConnectionPool.class);
    private static ConnectionPool instance;
    private static ReentrantLock lock = new ReentrantLock();
    private static AtomicBoolean instanceCreated = new AtomicBoolean();
    private BlockingQueue<ProxyConnection> connectionQueue;

    private enum Property{

        PROPERTY_PATH("/property/database.properties"), URL_PROPERTY("url"), POOL_SIZE_PROPERTY("poolSize");

        private String value;

        Property(String value){
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private ConnectionPool(){
        if (instanceCreated.get()) {
            logger.log(Level.FATAL, "Reflection API attack attempt");
            throw new RuntimeException("Reflection API attack attempt");
        }
        registerDriver();
        init();
    }

    public static ConnectionPool getInstance() {
        if (!instanceCreated.get()) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = new ConnectionPool();
                    instanceCreated.set(true);
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    public ProxyConnection getConnection() {
        ProxyConnection connection = null;
        try {
            connection = connectionQueue.take();
        } catch (InterruptedException e) {
            logger.catching(Level.ERROR, e);
        }
        return connection;
    }

    public void releaseConnection(ProxyConnection connection){
        try {
            connection.setAutoCommit(false);
            connection.rollback();
            connection.setAutoCommit(true);
            connectionQueue.put(connection);
        } catch (InterruptedException|SQLException e) {
            logger.catching(Level.ERROR, e);
        }
    }

    public void closeConnections(){
        final int poolSize = connectionQueue.size();
        for (int index = 0; index < poolSize; index++){
            ProxyConnection connection = getConnection();
            if (connection != null){
                connection.closeConnection();
            }
        }
        deregisterDrivers();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    protected Object readResolve() {
        return instance;
    }

    private void deregisterDrivers(){
        Enumeration<Driver> drivers = DriverManager.getDrivers();
        while (drivers.hasMoreElements()) {
            Driver driver = drivers.nextElement();
            try {
                DriverManager.deregisterDriver(driver);
            } catch (SQLException e){
                logger.catching(Level.ERROR,e);
            }
        }
    }

    private void registerDriver() {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
        } catch (SQLException e) {
            logger.catching(Level.FATAL, e);
            throw new RuntimeException(e);
        }
    }

    private void init(){
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL propertyURL = classLoader.getResource(Property.PROPERTY_PATH.getValue());
        if (propertyURL == null) {
            logger.log(Level.FATAL, "Database property file hasn't been found");
            throw new RuntimeException();
        }

        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(propertyURL.toURI())));
        } catch (URISyntaxException | IOException e) {
            logger.log(Level.FATAL, e);
            throw new RuntimeException(e);
        }

        int poolSize = Integer.parseInt(properties.getProperty(Property.POOL_SIZE_PROPERTY.getValue()));
        String url = properties.getProperty(Property.URL_PROPERTY.getValue());
        properties.remove(Property.URL_PROPERTY.getValue());
        properties.remove(Property.POOL_SIZE_PROPERTY.getValue());

        connectionQueue = new ArrayBlockingQueue<>(poolSize);

        for (int index = 0; index < poolSize; index++) {
            Connection connection;
            try {
                connection = DriverManager.getConnection(url, properties);
            } catch (SQLException e) {
                logger.log(Level.FATAL, e);
                throw new RuntimeException(e);
            }

            ProxyConnection proxyConnection = new ProxyConnection(connection);
            try {
                connectionQueue.put(proxyConnection);
            } catch (InterruptedException e) {
                logger.catching(Level.WARN, e);
            }
        }
    }
}

