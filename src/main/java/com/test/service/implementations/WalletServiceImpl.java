package com.test.service.implementations;

import com.test.model.Wallet;
import com.test.repository.WalletRepository;
import com.test.service.interfaces.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;


    public void create(Wallet wallet){
        walletRepository.save(wallet);
    }
}
