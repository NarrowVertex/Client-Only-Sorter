package com.narrowvertex.cos;

import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

public class ClientOnlySorterClient {

    private static ClientOnlySorterClient instance;

    public static Logger LOGGER = LogUtils.getLogger();

    public ClientOnlySorterClient() {
        instance = this;
    }

    public void init() {

    }

    public void sort() {

    }

    public static ClientOnlySorterClient getInstance() {
        return instance;
    }
}
