/* 
* Copyright Notice
* © [2024 - 2025] Winfxk. All rights reserved.
* The software, its source code, and all related documentation are the intellectual property of Winfxk. Any reproduction or distribution of this software or any part thereof must be clearly attributed to Winfxk and the original author. Unauthorized copying, reproduction, or distribution without proper attribution is strictly prohibited.
* For inquiries, support, or to request permission for use, please contact us at:
* Email: admin@winfxk.cn
* QQ: 2508543202
* Visit our homepage for more information: http://Winfxk.cn
* 
* --------- Create message ---------
* Created by IntelliJ ID
* Author： Winfxk
* Created PCUser: kc4064 
* Web: http://winfxk.com
* Created Date: 2025/8/4  13:01 */
package cn.winfxk.libk.sql.hibernate

import cn.winfxk.libk.log.Log
import jakarta.persistence.Entity
import org.hibernate.SessionFactory
import org.hibernate.boot.MetadataSources
import org.hibernate.boot.registry.StandardServiceRegistryBuilder
import org.reflections.Reflections
import java.util.*

/**
 *```
 *     val sessionFactory = HibernateConfigurator(
 *         url = "jdbc:mysql://localhost:3306/yourdb",
 *         username = "root",
 *         password = "yourpassword"
 *     ).scanPackage("cn.winfxk.entity").buildSessionFactory()
 *```
 * 最后
 * ```
 * sessionFactory.close()
 * ```
 * Hibernate 编程式配置终极工具类 (适配 Hibernate 7+)。
 * 采用构造函数强制传入核心参数，并内置了基于 HikariCP 的推荐默认配置。
 *
 * @param url 数据库连接URL
 * @param username 数据库用户名
 * @param password 数据库密码
 * @param dialect 数据库方言
 * @param driverClass 数据库驱动类
 * @author Winfxk (冰月)
 */
class HibernateConfigurator(
    private val url: String,
    private val username: String,
    private val password: String,
    private val dialect: String = "org.hibernate.dialect.MySQLDialect",
    private val driverClass: String = "com.mysql.cj.jdbc.Driver"
) {
    private val serviceRegistryBuilder = StandardServiceRegistryBuilder()
    private val packagesToScan = mutableSetOf<String>()
    private val annotatedClassesToAdd = mutableSetOf<Class<*>>()
    private val log = Log(this.javaClass.simpleName)
    private var optionalProperties = Properties()

    companion object {
        /**
         * 静态的默认配置。
         */
        private val DEFAULT_PROPERTIES: Properties = Properties().apply {
            // --- 默认使用 HikariCP 作为连接池提供者 ---
            put("hibernate.connection.provider_class", "org.hibernate.hikaricp.internal.HikariCPConnectionProvider")

            // --- HikariCP 自身的推荐默认值 ---
            put("hibernate.hikari.poolName", "WinfxkHikariPool") // 为连接池命名，便于监控
            put("hibernate.hikari.maximumPoolSize", "20")       // 最大连接数
            put("hibernate.hikari.minimumIdle", "5")            // 最小空闲连接数
            put("hibernate.hikari.idleTimeout", "300000")       // 空闲连接最大存活时间 (5分钟)
            put("hibernate.hikari.connectionTimeout", "20000")  // 获取连接的超时时间 (20秒)
            put("hibernate.hikari.maxLifetime", "1800000")      // 连接最大生命周期 (30分钟)
        }
    }

    /**
     * 加载用户自定义的、可选的Hibernate配置。
     * 此处传入的配置项会覆盖掉同名的默认配置。
     *
     * @param properties 包含可选Hibernate属性的Properties对象。
     * @return 返回当前实例以支持链式调用。
     */
    fun withOptionalProperties(properties: Properties) = apply {
        this.optionalProperties = properties
    }

    /**
     * 添加一个需要扫描实体类的包名。
     * @return 返回当前实例以支持链式调用。
     */
    fun scanPackage(packageName: String) = apply {
        packagesToScan.add(packageName)
    }

    /**
     * 手动注册一个或多个实体类。
     * @return 返回当前实例以支持链式调用。
     */
    fun addAnnotatedClasses(vararg entityClasses: Class<*>) = apply {
        annotatedClassesToAdd.addAll(entityClasses)
    }

    /**
     * 构建并返回Hibernate的SessionFactory。
     * 这是一个终止操作，调用此方法后，配置过程结束。
     *
     * @return 配置完成的 SessionFactory 实例。
     */
    fun buildSessionFactory(): SessionFactory {
        log.i("正在合并默认配置...")
        val finalProperties = DEFAULT_PROPERTIES.clone() as Properties
        finalProperties.putAll(optionalProperties)
        finalProperties.put("hibernate.connection.url", this.url)
        finalProperties.put("hibernate.connection.username", this.username)
        finalProperties.put("hibernate.connection.password", this.password)
        finalProperties.put("hibernate.connection.driver_class", this.driverClass)
        finalProperties.put("hibernate.dialect", this.dialect)
        serviceRegistryBuilder.applySettings(finalProperties)
        log.i("配置合并完成。")
        log.i("开始构建 ServiceRegistry...")
        val registry = serviceRegistryBuilder.build()
        return try {
            log.i("开始构建元数据 (Metadata)...")
            val metadataSources = MetadataSources(registry)
            annotatedClassesToAdd.forEach {
                log.i("  -> 手动注册实体: ${it.name}")
                metadataSources.addAnnotatedClass(it)
            }
            if (packagesToScan.isNotEmpty()) {
                log.i("开始扫描包: $packagesToScan")
                val reflections = Reflections(packagesToScan)
                val entityClasses = reflections.getTypesAnnotatedWith(Entity::class.java)
                entityClasses.forEach {
                    log.i("  -> 扫描注册实体: ${it.name}")
                    metadataSources.addAnnotatedClass(it)
                }
            }
            log.i("元数据构建完成，正在构建 SessionFactory...")
            metadataSources.buildMetadata().buildSessionFactory()
        } catch (e: Exception) {
            log.e("构建 SessionFactory 失败：${e.message}", e)
            try {
                registry.close()
            } catch (e2: Exception) {
                log.e("关闭 ServiceRegistry 失败", e2)
            }
            throw e
        }
    }
}