package com.example.bootIML.service;

import com.example.bootIML.model.Client;
import com.example.bootIML.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.bootIML.interpretator.StatD;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    ClientServiceImpl () {StatD.clientServiceImpl = this;}

    @Override
    public void create(Client client) {
        clientRepository.save(client);
    }

    @Override
    public List<Client>  readAll() {
        return clientRepository.findAll();
    }

    @Override
    public Client read(int id) {
        System.out.println("clientServiceImpl.read");
        return clientRepository.getOne(id);
    }

    @Override
    public boolean update(Client client, int id) {
        if (clientRepository.existsById(id)) {
            client.setId(id);
            clientRepository.save(client);
            return true;
        }

        return false;
    }

    @Override
    public boolean delete(int id) {
        if (clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
