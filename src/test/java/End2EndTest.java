import com.fasterxml.jackson.core.JsonProcessingException;
import de.fraunhofer.iais.eis.Message;
import de.fraunhofer.iais.eis.MessageBuilder;
import de.fraunhofer.iais.eis.ids.jsonld.Serializer;
import it.eng.idsa.clearinghouse.service.impl.ClearingHouseService;
import it.eng.idsa.clearinghouse.service.impl.ClearingHouseServiceImpl;
import org.hyperledger.fabric.gateway.ContractException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.concurrent.TimeoutException;

public class End2EndTest {
    private ClearingHouseService clearingHouseService;
    private Random random;

    @Before
    public void setup() {
        try {
            random = new Random();
            ClearingHouseService clearingHouseService = new ClearingHouseServiceImpl();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @After
    public void tearDown() {
        clearingHouseService = null;
        random = null;
    }

    @Test
    public void register() {
        try {
            GregorianCalendar gcal = new GregorianCalendar();
            XMLGregorianCalendar xgcal = DatatypeFactory.newInstance()
                    .newXMLGregorianCalendar(gcal);
            String id_ = "https://w3id.org/idsa/autogen/brokerQueryMessage/6bed5855-489b-4f47-82dc-08c5f1656101" + random.nextInt(100);
            URI id = new URI(id_);
            URI connector = new URI("https://ids.tno.nl/test");
            Message message = new MessageBuilder()
                    ._modelVersion_("1.0.3")
                    ._issued_(xgcal)
                    ._correlationMessage_(id)
                    ._issuerConnector_(connector)
                    ._recipientConnector_(null)
                    ._senderAgent_(null)
                    ._recipientAgent_(null)
                    ._transferContract_(null)
                    .build();
            String msgSerialized = new Serializer().serializePlainJson(message);
            clearingHouseService.registerTransaction(msgSerialized);
            Assert.assertTrue("Succesfully completed", true);
        } catch (JsonProcessingException e) {
            Assert.assertTrue(e.getMessage(), false);
        } catch (URISyntaxException e) {
            Assert.assertTrue(e.getMessage(), false);

        } catch (DatatypeConfigurationException e) {
            Assert.assertTrue(e.getMessage(), false);
        } catch (IOException e) {
            Assert.assertTrue(e.getMessage(), false);
        } catch (TimeoutException e) {
            Assert.assertTrue(e.getMessage(), false);
        } catch (InterruptedException e) {
            Assert.assertTrue(e.getMessage(), false);
        } catch (ContractException e) {
            Assert.assertTrue(e.getMessage(), false);
        } catch (Exception e) {
            Assert.assertTrue(e.getMessage(), false);
        }
    }
}