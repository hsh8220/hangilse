package com.erp.hangilse.client.service;

import com.erp.hangilse.client.domain.Client;
import com.erp.hangilse.client.domain.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    public Client getCientById(Long id) {
        return this.clientRepository.findById(id).get();
    }

    public Client saveClient(Client client) {
        return this.clientRepository.save(client);
    }
}
