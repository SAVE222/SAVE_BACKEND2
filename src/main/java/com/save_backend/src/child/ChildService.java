package com.save_backend.src.child;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChildService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final ChildDao childDao;
    private final ChildProvider childProvider;

    public ChildService(ChildDao childDao, ChildProvider childProvider) {
        this.childDao = childDao;
        this.childProvider = childProvider;
    }
}
