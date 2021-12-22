package com.hampushallkvist.metacraftinfocommands

import com.hampushallkvist.metacraftinfocommands.commands.CommandRegister
import net.fabricmc.api.ModInitializer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

class Main : ModInitializer {

    companion object {
        const val MOD_ID = "metacraftinfocommands"
        val LOGGER: Logger = LogManager.getLogger(MOD_ID)
    }

    override fun onInitialize() {

        CommandRegister().init()

        LOGGER.info("metacraftinfocommands server mod by Hampus Hallkvist initialized")
    }
}