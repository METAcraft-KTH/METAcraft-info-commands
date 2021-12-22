import com.hampushallkvist.metacraftinfocommands.commands.Command
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import net.minecraft.command.CommandSource
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.LiteralText
import net.minecraft.util.Formatting

class Rules : Command {

    companion object {
        const val pageCount = 5
        val rules = arrayOf(
            arrayOf(
                "Global Rules",
                """
                    (1) Intentionally crashing the server or creating lag is strictly prohibited. 
                    (2) Some client-side mods are allowed (ask mods before entering the server with them).
                    (3) The use botting or cheat-clients is strictly forbidden, even in the Wilderness. 
                    (4) Any forms of duping are forbidden (with the sole exception of TNT-duping).
                """.trimIndent()
            ),
            arrayOf(
                "Civilization",
                """
                    The Civilization is located at the positive z-coords. Fire tick and TNT block-breaking is disabled. 
                    (1) Sneaking into peoples bases and looking in chests is allowed. Stealing is prohibited. 
                    (2) Trap installation is permitted: Killing someone sneaking around your base with a trap is allowed.
                    (3) Pranks are allowed, but griefing, stealing or randomly killing players without reason is forbidden.
                    (4) Using someone else’s farms without permission is forbidden.
                    (5) You are also expected to follow good Minecraft etiquette. Don’t leave floating blocks etc. Keep it tidy.  
                """.trimIndent()
            ),
            arrayOf(
                "META-borg",
                """
                    META-borg is the capital city of the Civilization. It contains the spawn (0, 0). 
                    (1) Within the city, griefing and stealing is strictly prohibited.
                    (2) Larger build projects will require permits. 
                    (3) Chunk loading on the server requires a permit from Borgarrådet. 
                    (4) Repeatedly breaking the rules of the server will lead to exile into the Wilderness. Exiles have red names and anyone is allowed to kill them.
                """.trimIndent()
            ),
            arrayOf(
                "Great Wall",
                """
                    The Great Wall is the last defence measure of the Civilization and it exerts a magical force over exiles so that they cannot physically pass it. Yet, nothing prevents exiles from shooting toward you if you are close enough. Hang around close to the Great Wall at your own peril.   
                """.trimIndent()
            ),
            arrayOf(
                "Wilderness",
                """
                    In the Wilderness Chaos rules. It is located at the negative z-coords and the Great Wall separates it from the Civilization. Apart from hacking, botting, duping, or intentionally lagging the server, ANYTHING is permitted.
                    (1) PvP is encouraged.
                    (2) The killing of exiles will be rewarded by the Borgarråd.
                    (3) Brave adventurers will be able to form bands and head out in search of power and glory (TBA).
                """.trimIndent()
            )
        )
    }

    override fun register(dispatcher: CommandDispatcher<ServerCommandSource>, dedicated: Boolean) {

        val command: LiteralArgumentBuilder<ServerCommandSource> = CommandManager.literal("rules")
            .executes(this::menu)
            .then(CommandManager.argument("page", IntegerArgumentType.integer(1, pageCount))
                .suggests { _, b -> CommandSource.suggestMatching(Array(pageCount, init = {
                        i -> (i + 1).toString()
                }), b) }
                .executes(this::run))

        dispatcher.register(command)
    }

    private fun menu(context: CommandContext<ServerCommandSource>): Int {
        val string = buildString {
            append("""
                At positive z-coords the Civilization stands, ruled by the benevolent bureaucracy of the Borgarråd. A great procedurally generated infinite wall along the z=0 line separates it from the deadly steppes of the Wilderness, a place filled with danger and chaos.
                Do /rules <page_nr> to go to page.
            """.trimIndent())
            append("\n")
            for (i in rules.indices) {
                var newLine = "\n"
                if (i == rules.size - 1)
                    newLine = ""
                append("[${i + 1}] ${rules[i][0]}$newLine")
            }
        }
        printMenu(context, "Rules", 0, string)
        return 0
    }

    private fun run(context: CommandContext<ServerCommandSource>): Int {
        val page = IntegerArgumentType.getInteger(context, "page")
        printMenu(context, rules[page - 1][0], page, rules[page - 1][1])
        return 0
    }

    private fun printMenu(context: CommandContext<ServerCommandSource>, title: String, page: Int, text: String) {
        context.source.player.sendMessage(
            LiteralText("================== $title [$page/$pageCount] ==================")
                .append("\n")
                .append(text)
                .formatted(Formatting.GOLD),
            false
        )
    }
}