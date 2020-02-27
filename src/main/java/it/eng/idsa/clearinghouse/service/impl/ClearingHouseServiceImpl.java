package it.eng.idsa.clearinghouse.service.impl;

import de.fraunhofer.iais.eis.Message;
import de.fraunhofer.iais.eis.ids.jsonld.Serializer;
import it.eng.idsa.clearinghouse.user.EnrollAdmin;
import it.eng.idsa.clearinghouse.user.RegisterUser;
import org.hyperledger.fabric.gateway.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;


public class ClearingHouseServiceImpl implements ClearingHouseService {

    public static final String CONTRACT_NAME = "oor-chaincode-go";
    public static final String USER = "user1";

    static {
        System.setProperty("org.hyperledger.fabric.sdk.service_discovery.as_localhost", "true");
    }

    public static final String CHANNEL = "ledgerchannel"; //TODO pom.xml
    private Wallet wallet;
    private Gateway.Builder gatewayBuilder;
    private Path networkConfigFile;


    public ClearingHouseServiceImpl() throws Exception {
        EnrollAdmin.main(null);
        RegisterUser.main(null);
        connect();
    }


    private void connect() throws IOException {
        //Path walletDirectory = Paths.get("wallet");
        Path walletPath = Paths.get("wallet");
        wallet = Wallet.createFileSystemWallet(walletPath);
        networkConfigFile = Paths.get("connection.json");
        gatewayBuilder = Gateway.createBuilder()
                .identity(wallet, USER)
                .networkConfig(networkConfigFile);
    }

    @Override
    public void registerTransaction(String message) throws InterruptedException, TimeoutException, ContractException, IOException {
        try (Gateway gateway = gatewayBuilder.connect()) {

            Network network = gateway.getNetwork(CHANNEL);
            Contract contract = network.getContract(CONTRACT_NAME);

            Map map = parseMessage(message);
            String messageId = map.keySet().toArray()[0].toString();
            byte[] createCarResult = contract.createTransaction("edit")
                    .submit(messageId, map.values().toArray()[0].toString());
            System.out.println(new String(createCarResult, StandardCharsets.UTF_8));

            byte[] queryAllCarsResult = contract.evaluateTransaction("get", messageId );
            System.out.println(new String(queryAllCarsResult, StandardCharsets.UTF_8));
        }
    }

    private Map parseMessage(String message) throws IOException {
        Message messageObj = new Serializer().deserialize(message, Message.class);
        Map<String, Message> messageMap = new HashMap<>();
        messageMap.put(messageObj.getId().toString(), messageObj); //USE ID ad key
        return messageMap;
    }

}
