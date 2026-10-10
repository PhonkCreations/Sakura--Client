package com.sakura.client.modules.misc;

import com.sakura.client.Category;
import com.sakura.client.Module;

public class PacketLogger extends Module {
    public PacketLogger() {
        super("Packet Logger", "Records network packet information.", Category.MISC);
    }
}
