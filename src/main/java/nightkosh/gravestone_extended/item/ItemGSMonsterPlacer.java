package nightkosh.gravestone_extended.item;

import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import nightkosh.gravestone_extended.core.Entity;
import nightkosh.gravestone_extended.core.ModInfo;
import nightkosh.gravestone_extended.core.Tabs;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * GraveStone mod
 *
 * @author Portablejim
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class ItemGSMonsterPlacer extends ItemMonsterPlacer {

    public static final int WITHER_BACKGROUND_COLOR = 0x000000;
    public static final int ZOMBIE_BACKGROUND_COLOR = 0x00AFAF;
    public static final int SKELETON_BACKGROUND_COLOR = 0xC1C1C1;
    public static final int SKELETON_FOREGROUND_COLOR = 4802889;
    public static final int CAT_BACKGROUND_COLOR = 0xEFDE7D;
    public static final int DOG_BACKGROUND_COLOR = 0xD7D3D3;
    public static final int HORSE_FOREGROUND_COLOR = 15656192;
    public static final int RAVEN_BACKGROUND_COLOR = 14144467;
    public static final int RAVEN_FOREGROUND_COLOR = 11013646;
    public static final int CRAWLER_FOREGROUND_COLOR = 0xA80E0E;
    public static final int SKELETON_PET_FOREGROUND_COLOR = 0x494949;
    public static final int ZOMBIE_PET_FOREGROUND_COLOR = 0x799c65;

    public static enum EnumEggs {
        ZOMBIE_DOG(Entity.ZOMBIE_DOG_NAME, DOG_BACKGROUND_COLOR, ZOMBIE_PET_FOREGROUND_COLOR),
        ZOMBIE_CAT(Entity.ZOMBIE_CAT_NAME, CAT_BACKGROUND_COLOR, ZOMBIE_PET_FOREGROUND_COLOR),
        SKELETON_DOG(Entity.SKELETON_DOG_NAME, DOG_BACKGROUND_COLOR, SKELETON_PET_FOREGROUND_COLOR),
        SKELETON_CAT(Entity.SKELETON_CAT_NAME, CAT_BACKGROUND_COLOR, SKELETON_PET_FOREGROUND_COLOR),
        SKULL_CRAWLER(Entity.SKULL_CRAWLER_NAME, SKELETON_BACKGROUND_COLOR, CRAWLER_FOREGROUND_COLOR),
        WITHER_SKULL_CRAWLER(Entity.WITHER_SKULL_CRAWLER_NAME, WITHER_BACKGROUND_COLOR, CRAWLER_FOREGROUND_COLOR),
        ZOMBIE_SKULL_CRAWLER(Entity.ZOMBIE_SKULL_CRAWLER_NAME, ZOMBIE_BACKGROUND_COLOR, CRAWLER_FOREGROUND_COLOR),
        SKELETON(Entity.SKELETON_NAME, SKELETON_BACKGROUND_COLOR, SKELETON_FOREGROUND_COLOR, Entity.MINECRAFT_SKELETON_ID),
        ZOMBIE_HORSE(Entity.ZOMBIE_HORSE_NAME, ZOMBIE_BACKGROUND_COLOR, HORSE_FOREGROUND_COLOR, Entity.MINECRAFT_ZOMBIE_HORSE_NAME),
        SKELETON_HORSE(Entity.SKELETON_HORSE_NAME, SKELETON_BACKGROUND_COLOR, HORSE_FOREGROUND_COLOR, Entity.MINECRAFT_SKELETON_HORSE_NAME),
        ZOMBIE_RAIDER(Entity.ZOMBIE_RAIDER_NAME, ZOMBIE_BACKGROUND_COLOR, HORSE_FOREGROUND_COLOR),
        SKELETON_RAIDER(Entity.SKELETON_RAIDER_NAME, SKELETON_BACKGROUND_COLOR, HORSE_FOREGROUND_COLOR);//,
//        DAMNED_WARRIOR(Entity.DAMNED_WARRIOR_NAME, SKELETON_BACKGROUND_COLOR, SKELETON_FOREGROUND_COLOR),
//        RAVEN(nightkosh.gravestone_extended.core.Entity.RAVEN_NAME, RAVEN_BACKGROUND_COLOR, RAVEN_FOREGROUND_COLOR);

        private String name;
        private String customName;
        private int backgroundColor;
        private int foregroundColor;

        private EnumEggs(String name, int backgroundColor, int foregroundColor) {
            this.name = name;
            this.backgroundColor = backgroundColor;
            this.foregroundColor = foregroundColor;
        }

        private EnumEggs(String name, int backgroundColor, int foregroundColor, String customName) {
            this(name, backgroundColor, foregroundColor);
            this.customName = customName;
        }

        public static EnumEggs getById(int id) {
            if (id > EnumEggs.values().length || id < 0) {
                return ZOMBIE_DOG;
            }
            return EnumEggs.values()[id];
        }

        public String getName() {
            return name;
        }

        public String getCustomName() {
            return customName;
        }

        public int getBackgroundColor() {
            return backgroundColor;
        }

        public int getForegroundColor() {
            return foregroundColor;
        }
    }

    public ItemGSMonsterPlacer() {
        this.setHasSubtypes(true);
        this.setCreativeTab(Tabs.otherItemsTab);
        this.setUnlocalizedName("monsterPlacer");
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemStack) {
        StringBuilder str = new StringBuilder();
        str.append(I18n.translateToLocal(this.getUnlocalizedName() + ".name").trim());

        EnumEggs egg = EnumEggs.getById(itemStack.getItemDamage());
        String name = (StringUtils.isBlank(egg.getCustomName())) ? egg.getName() : egg.getCustomName();
        if (StringUtils.isNotBlank(name)) {
            str.append(" ").append(I18n.translateToLocal("entity." + name + ".name"));
        }

        return str.toString();
    }

    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    @Override
    public EnumActionResult onItemUse(ItemStack item, EntityPlayer player, World world, BlockPos blockPos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (world.isRemote) {
            return EnumActionResult.SUCCESS;
        } else {
            IBlockState block = world.getBlockState(blockPos);
            double d0 = 0;
            if (facing == EnumFacing.UP && block instanceof BlockFence) {
                d0 = 0.5;
            }

            blockPos = blockPos.offset(facing);
            net.minecraft.entity.Entity entity = spawnCreature(world, item.getItemDamage(), blockPos.getX() + 0.5, blockPos.getY() + d0, blockPos.getZ() + 0.5);
            if (entity != null) {
                if (entity instanceof EntityLivingBase && item.hasDisplayName()) {
                    ((EntityLiving) entity).setCustomNameTag(item.getDisplayName());
                }

                if (!player.capabilities.isCreativeMode) {
                    --item.stackSize;
                }
            }

            return EnumActionResult.SUCCESS;
        }
    }

    /**
     * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
     */
    @Override
    public ActionResult<ItemStack> onItemRightClick(ItemStack item, World world, EntityPlayer player, EnumHand hand) {
        if (world.isRemote) {
            return new ActionResult(EnumActionResult.PASS, item);
        } else {
            RayTraceResult movingobjectposition = this.rayTrace(world, player, true);

            if (movingobjectposition == null) {
                return new ActionResult(EnumActionResult.PASS, item);
            } else {
                if (movingobjectposition.typeOfHit == RayTraceResult.Type.BLOCK) {
                    if (!world.canMineBlockBody(player, movingobjectposition.getBlockPos())) {
                        return new ActionResult(EnumActionResult.PASS, item);
                    }

                    if (!player.canPlayerEdit(movingobjectposition.getBlockPos(), movingobjectposition.sideHit, item)) {
                        return new ActionResult(EnumActionResult.PASS, item);
                    }

                    if (world.getBlockState(movingobjectposition.getBlockPos()).getBlock() instanceof BlockLiquid) {
                        net.minecraft.entity.Entity entity = spawnCreature(world, item.getItemDamage(), movingobjectposition.getBlockPos().getX(),
                                movingobjectposition.getBlockPos().getY(), movingobjectposition.getBlockPos().getZ());

                        if (entity != null) {
                            if (entity instanceof EntityLivingBase && item.hasDisplayName()) {
                                entity.setCustomNameTag(item.getDisplayName());
                            }

                            if (!player.capabilities.isCreativeMode) {
                                --item.stackSize;
                            }
                        }
                    }
                }

                return new ActionResult(EnumActionResult.PASS, item);
            }
        }
    }

    public static net.minecraft.entity.Entity spawnCreature(World world, int damageValue, double x, double y, double z) {
        if (world.isRemote || damageValue < 0 || damageValue >= EnumEggs.values().length) {
            return null;
        }

        String fullEntityName = String.format("%s.%s", ModInfo.ID, EnumEggs.getById(damageValue).getName());
        net.minecraft.entity.Entity entity = EntityList.createEntityByName(fullEntityName, world);

        if (entity != null && entity instanceof EntityLivingBase) {
            EntityLiving entityliving = (EntityLiving) entity;
            entity.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(world.rand.nextFloat() * 360), 0);
            entityliving.rotationYawHead = entityliving.rotationYaw;
            entityliving.renderYawOffset = entityliving.rotationYaw;
            entityliving.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData) null);
            world.spawnEntityInWorld(entity);
            entityliving.playLivingSound();
        }

        return entity;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List subItems) {
        for (int i = 0; i < EnumEggs.values().length; i++) {
            subItems.add(new ItemStack(item, 1, i));
        }
    }
}
