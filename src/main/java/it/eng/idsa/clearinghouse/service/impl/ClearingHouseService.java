package it.eng.idsa.clearinghouse.service.impl;

import org.hyperledger.fabric.gateway.ContractException;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public interface ClearingHouseService {

    void registerTransaction(String message) throws IOException, InterruptedException, TimeoutException, ContractException;
}
