package nightkosh.gravestone_extended.tileentity;

import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import nightkosh.gravestone_extended.block.enums.EnumHauntedChest;
import nightkosh.gravestone_extended.config.ExtendedConfig;
import nightkosh.gravestone_extended.core.GSBlock;
import nightkosh.gravestone_extended.core.MobSpawn;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class TileEntityHauntedChest extends TileEntity implements ITickable {

    private int openTicks = 0;
    private boolean isOpen = false;
    /**
     * The current angle of the lid (between 0 and 1)
     */
    public float lidAngle;
    /**
     * The angle of the lid last tick
     */
    public float prevLidAngle;
    private EnumHauntedChest chestType = EnumHauntedChest.BATS_CHEST;

    public TileEntityHauntedChest() {
    }

    /**
     * Allows the entity to update its state. Overridden in most subclasses,
     * e.g. the mob spawner uses this to count ticks and creates a new spawn
     * inside its implementation.
     */
    @Override
    public void update() {
        float f;

        if (openTicks > 0) {
            openTicks--;
        }

        if (this.worldObj.isRemote) {
            this.prevLidAngle = this.lidAngle;
            f = 0.1F;
            double d0;

            if (this.openTicks > 0 && this.lidAngle == 0) {
                double d1 = (double) this.pos.getX() + 0.5D;
                d0 = (double) this.pos.getZ() + 0.5D;

                this.worldObj.playSound(null, d1, (double) this.pos.getY() + 0.5D, d0, SoundEvents.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.openTicks == 0 && this.lidAngle > 0 || this.openTicks > 0 && this.lidAngle < 1) {
                float f1 = this.lidAngle;

                if (this.openTicks > 0) {
                    this.lidAngle += f;
                } else {
                    this.lidAngle -= f;
                }

                if (this.lidAngle > 1) {
                    this.lidAngle = 1;
                }

                float f2 = 0.5F;
                if (this.lidAngle < f2 && f1 >= f2) {
                    d0 = (double) this.pos.getX() + 0.5D;
                    double d2 = (double) this.pos.getZ() + 0.5D;

                    this.worldObj.playSound(null, d0, (double) this.pos.getY() + 0.5D, d2, SoundEvents.BLOCK_CHEST_CLOSE, SoundCategory.BLOCKS, 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
                }

                if (this.lidAngle < 0) {
                    this.lidAngle = 0;
                }
            }
        } else {
            if (openTicks == 45) {
                spawnMobs(this.worldObj);
            }
        }

        if (openTicks == 0) {
            if (this.isOpen && ExtendedConfig.replaceHauntedChest) {
                this.worldObj.removeTileEntity(this.pos);
                this.worldObj.setBlockState(this.pos, Blocks.CHEST.getStateFromMeta(GSBlock.hauntedChest.getMetaFromState(worldObj.getBlockState(this.pos))));
            }
            this.isOpen = false;
        }
    }

    public void openChest() {
        if (openTicks == 0) {
            this.openTicks = 50;
            this.isOpen = true;
        }
    }

    public EnumHauntedChest getChestType() {
        return chestType;
    }

    public void setChestType(EnumHauntedChest chestType) {
        this.chestType = chestType;
    }

    /**
     * Reads a tile entity from NBT.
     */
    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);

        chestType = EnumHauntedChest.getById(nbt.getInteger("ChestType"));
    }

    /**
     * Writes a tile entity to NBT.
     */
    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt = super.writeToNBT(nbt);

        nbt.setInteger("ChestType", chestType.ordinal());

        return nbt;
    }

    public void spawnMobs(World world) {
        switch (getChestType()) {
            case SKELETON_CHEST:
                EntitySkeleton skeleton = MobSpawn.getSkeleton(world, false);
                skeleton.setLocationAndAngles(this.pos.getX() + 0.5, this.pos.getY(), this.pos.getZ() + 0.5, 0, 0);
                world.spawnEntityInWorld(skeleton);
                break;
            case BATS_CHEST:
            default:
                EntityBat bat;
                int batsCount = 15;

                for (byte i = 0; i < batsCount; i++) {
                    bat = new EntityBat(world);
                    bat.setLocationAndAngles(this.pos.getX() + 0.5, this.pos.getY() + 0.7, this.pos.getZ() + 0.5, 0, 0);

                    world.spawnEntityInWorld(bat);
                }
                break;
        }
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        readFromNBT(packet.getNbtCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 1, this.getUpdateTag());
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }
}
