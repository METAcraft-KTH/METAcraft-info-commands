package com.hampushallkvist.metacraftinfocommands.commands

import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.LiteralText

class WorldMap : Command {
    override fun register(dispatcher: CommandDispatcher<ServerCommandSource>, dedicated: Boolean) {
        val command: LiteralArgumentBuilder<ServerCommandSource> = CommandManager.literal("worldmap")
            .executes(this::printMap)

        dispatcher.register(command)
    }

    private fun printMap(context: CommandContext<ServerCommandSource>): Int {
        context.source.player.sendMessage(LiteralText("World map: http://mc.datasektionen.se:5555"), false)
        return 0
    }
}