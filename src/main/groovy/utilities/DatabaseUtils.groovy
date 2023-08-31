package utilities

import groovy.sql.Sql
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class DatabaseUtils {

    private static Logger logger = LogManager.getLogger(DatabaseUtils.class)
    def static dataSource

    static {
        def sql = Sql.newInstance(dataSource)
        logger.info("DatabaseUtils:: DB Version: " + sql.rows("select version();").get(0))
    }
}
