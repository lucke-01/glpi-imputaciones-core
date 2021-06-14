package utilidades;

import es.alfatec.imputaciones.util.fichero.extractor.ExtractorFicheroTareas;
import es.alfatec.imputaciones.util.utilidades.FicheroUtil;
import java.io.File;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author ricardo
 */
public class LogTest {
    
    public LogTest() {
    }
    @Test
    public void testLogging() {
        Logger logger = LoggerFactory.getLogger(LogTest.class);
        logger.info("Hello World");
    }
   
}
