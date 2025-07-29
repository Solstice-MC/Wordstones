package org.solstice.wordstones.content.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.solstice.wordstones.content.Word;
import org.solstice.euclidsElements.util.Location;
import org.solstice.wordstones.registry.WordstoneAttachmentTypes;
import org.solstice.wordstones.registry.WordstoneBlockEntityTypes;

import java.util.Map;

public class WordstoneEntity extends BlockEntity {

    @Nullable
	protected Word word;

    public WordstoneEntity(BlockPos pos, BlockState state) {
        super(WordstoneBlockEntityTypes.WORDSTONE, pos, state);
    }

	public static boolean wordExists(World world, Word word) {
		return getGlobalWordstones(world).containsKey(word);
	}

	@Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);
        if (nbt.contains("word")) this.word = new Word(nbt.getString("word"));
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
        if (this.word != null) nbt.putString("word", this.word.value());
    }

	public boolean hasWord() {
		return this.word != null;
	}

	@Nullable
    public Word getWord() {
        return this.word;
    }

    public void setWord(Word word) {
        this.word = word;
        markDirty();
    }

	public static Map<Word, Location> getGlobalWordstones(World world) {
		return world.getAttachedOrCreate(WordstoneAttachmentTypes.GLOBAL_WORDSTONES);
	}

    public static boolean teleportPlayerToWord(PlayerEntity player, Word word) {
		Location location = getGlobalWordstones(player.getWorld()).getOrDefault(word, Location.ZERO);
		if (location.isZero()) return false;

		World world = player.getWorld().getServer().getWorld(location.worldKey());

		Vec3d vec = location.getVec().add(0.5, 1, 0.5);
		player.requestTeleport(vec.x, vec.y, vec.z + 1);

		world.playSound(null, player.getX(), player.getY(), player.getZ(),
			SoundEvents.ENTITY_ENDERMAN_TELEPORT,
			SoundCategory.PLAYERS,
			1, 1
		);

		for (Direction direction : Direction.Type.HORIZONTAL) {
			BlockPos pos = location.pos().offset(direction);
			BlockEntity entity = world.getBlockEntity(pos);
			if (entity instanceof DropBoxEntity dropBox && !dropBox.hasPlayerInventory(player)) {
				dropBox.depositItems(player);
				return true;
			}
		}

		player.dropInventory();
		return true;
    }

    @Override
    public void markRemoved() {
        if (this.word != null)
			this.getWorld().getAttachedOrCreate(WordstoneAttachmentTypes.GLOBAL_WORDSTONES).remove(this.word);
        super.markRemoved();
    }

	public boolean isPlayerTooFar(PlayerEntity player) {
		return !player.canInteractWithBlockAt(this.getPos(), 4);
	}

}
