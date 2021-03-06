package nightkosh.gravestone_extended.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import nightkosh.gravestone_extended.core.Tabs;
import nightkosh.gravestone_extended.core.TimeHelper;
import nightkosh.gravestone_extended.particle.EntityGreenFlameFX;
import nightkosh.gravestone_extended.tileentity.TileEntityCandle;

import java.util.Random;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class BlockCandle extends BlockContainer {

    public BlockCandle() {
        super(Material.CARPET);
        this.setSoundType(SoundType.CLOTH);
        this.setUnlocalizedName("candle");
        this.setHardness(0);
        this.setLightLevel(1);
        this.setResistance(0);
        this.setCreativeTab(Tabs.otherItemsTab);
    }

    /**
     * A randomly called display update to be able to add particles or other
     * items for display
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random random) {
        double xPos = pos.getX() + 0.5;
        double yPos = pos.getY() + 0.5;
        double zPos = pos.getZ() + 0.5;

        long dayTime = TimeHelper.getDayTime(world);
        if (dayTime < TimeHelper.SUN_SET || dayTime > TimeHelper.SUN_RISING) {
            world.spawnParticle(EnumParticleTypes.FLAME, xPos, yPos, zPos, 0, 0, 0);
        } else {
            Particle entityfx = new EntityGreenFlameFX(world, xPos, yPos, zPos, 0, 0, 0);
            Minecraft.getMinecraft().effectRenderer.addEffect(entityfx);
        }
        world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, xPos, yPos, zPos, 0, 0, 0);
    }

    /**
     * Returns a bounding box from the pool of bounding boxes (this means this
     * box can change after the pool has been cleared to be reused)
     */
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, World world, BlockPos pos) {
        return null;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube? This determines whether
     * or not to render the shared face of two adjacent blocks and also whether
     * the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    private static final AxisAlignedBB BB = new AxisAlignedBB(0.4F, 0, 0.4F, 0.6F, 0.6F, 0.6F);

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess access, BlockPos pos) {
        return BB;
    }

    /**
     * Checks to see if its valid to put this block at the specified
     * coordinates. Args: world, x, y, z
     */
    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return canPlaceCandleOn(world, pos.down());
    }

    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which
     * neighbor changed (coordinates passed are their own) Args: x, y, z,
     * neighbor blockID
     */
    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block) {
        if (!this.canPlaceCandleOn(world, pos.down())) {
            this.dropBlockAsItem(world, pos, state, 0);
            world.setBlockToAir(pos);
        }
    }

    /**
     * Gets if we can place a torch on a block.
     */
    private boolean canPlaceCandleOn(World world, BlockPos pos) {
        if (world.isSideSolid(pos, EnumFacing.UP)) {
//        if (world.doesBlockHaveSolidTopSurface(world, pos)) {
            return true;
        } else {
            IBlockState state = world.getBlockState(pos);
            return (state.getBlock() != null && state.getBlock().canPlaceTorchOnTop(state, world, pos));
        }
    }

    @Override
    public TileEntity createNewTileEntity(World world, int var2) {
        return new TileEntityCandle();
    }
}
