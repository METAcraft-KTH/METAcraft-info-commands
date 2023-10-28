package se.datasektionen.mc.infocommands;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.text.Text;
import net.minecraft.util.dynamic.Codecs;

import java.util.HashMap;
import java.util.Map;

public record InfoNode(Text message, Map<String, InfoNode> subCommands) {
	public static final Codec<InfoNode> RECORD_CODEC = RecordCodecBuilder.create(instance -> instance.group(
			Codecs.TEXT.fieldOf("message").forGetter(InfoNode::message),
			Codec.unboundedMap(Codec.STRING, Codecs.createLazy(InfoNode::getCodec)).fieldOf("subCommands")
					.orElse(new HashMap<>()).forGetter(InfoNode::subCommands)
	).apply(instance, InfoNode::new));

	public static final Codec<InfoNode> CODEC = Codec.either(Codecs.TEXT, RECORD_CODEC).xmap(
			either -> either.map(InfoNode::new, node -> node),
			node -> node.subCommands().isEmpty() ? Either.left(node.message()) : Either.right(node)
	);

	private static Codec<InfoNode> getCodec() {
		return CODEC;
	}

	public InfoNode(Text message) {
		this(message, new HashMap<>());
	}
}
