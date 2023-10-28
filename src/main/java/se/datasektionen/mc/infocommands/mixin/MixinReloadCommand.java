package se.datasektionen.mc.infocommands.mixin;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ReloadCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import se.datasektionen.mc.infocommands.Info;
import se.datasektionen.mc.infocommands.Helper;

@Mixin(ReloadCommand.class)
public class MixinReloadCommand {

	@Inject(method = "method_13530", at = @At("HEAD")) //Lambda in register
	private static void method_13530Pre(CommandContext<ServerCommandSource> context, CallbackInfoReturnable<Integer> cir) {
		Info.reloadConfig();
	}

	@Inject(method = "method_13530", at = @At("RETURN")) //Lambda in register
	private static void method_13530Post(CommandContext<ServerCommandSource> context, CallbackInfoReturnable<Integer> cir) {
		if (Info.getConfig().resendCommandTreeOnReload()) {
			Helper.resendCommandTreeToAllPlayers(context.getSource().getServer().getPlayerManager());
		}
	}

}
