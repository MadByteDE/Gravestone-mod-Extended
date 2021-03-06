package nightkosh.gravestone_extended.core;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.SkeletonType;
import net.minecraft.entity.monster.ZombieType;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import nightkosh.gravestone.block.enums.EnumGraves;
import nightkosh.gravestone_extended.block.enums.EnumSpawner;
import nightkosh.gravestone_extended.config.ExtendedConfig;
import nightkosh.gravestone_extended.core.logger.GSLogger;
import nightkosh.gravestone_extended.entity.monster.EntityGSSkeleton;
import nightkosh.gravestone_extended.entity.monster.crawler.EntitySkullCrawler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class MobSpawn {

    private static final int HELL_HEIGHT = 51;
    private static final Random RANDOM = new Random();
    /**
     * Provides a mapping between entity classes and a string
     */
    public static Map<String, Constructor<EntityLiving>> mobNameToClassMapping = new HashMap<>();
    public static List<String> MOB_ID = new ArrayList<>(Arrays.asList(Entity.MINECRAFT_ZOMBIE_ID, Entity.SKELETON_ID));
    public static List<String> DOG_ID = new ArrayList<>(Arrays.asList(Entity.ZOMBIE_DOG_ID, Entity.SKELETON_DOG_ID));
    public static List<String> CAT_ID = new ArrayList<>(Arrays.asList(Entity.ZOMBIE_CAT_ID, Entity.SKELETON_CAT_ID));
    public static List<String> HORSE_ID = new ArrayList<>(Arrays.asList(Entity.ZOMBIE_HORSE_ID, Entity.SKELETON_HORSE_ID));
    public static List<String> HELL_MOB_ID = new ArrayList<>(Arrays.asList(Entity.MINECRAFT_PIGZOMBIE_ID, Entity.SKELETON_ID));
    // spawner mobs
    public static List<String> skeletonSpawnerMobs = new ArrayList<>(Arrays.asList(
            Entity.SKELETON_ID, Entity.SKELETON_ID, Entity.SKELETON_ID, Entity.SKELETON_ID,
            Entity.SKELETON_DOG_ID,
            Entity.SKELETON_CAT_ID,
            Entity.SKELETON_HORSE_ID,
            Entity.SKELETON_RAIDER_ID));
    public static List<String> zombieSpawnerMobs = new ArrayList<>(Arrays.asList(
            Entity.MINECRAFT_ZOMBIE_ID, Entity.MINECRAFT_ZOMBIE_ID, Entity.MINECRAFT_ZOMBIE_ID, Entity.MINECRAFT_ZOMBIE_ID,
            Entity.ZOMBIE_DOG_ID,
            Entity.ZOMBIE_CAT_ID,
            Entity.ZOMBIE_HORSE_ID,
            Entity.ZOMBIE_RAIDER_ID));
    public static List<String> spiderSpawnerMobs = new ArrayList<>(Arrays.asList(
            Entity.MINECRAFT_SPIDER_ID, Entity.MINECRAFT_CAVE_SPIDER_ID, Entity.MINECRAFT_SPIDER_ID));
    // catacombs statues mobs
    public static List<String> catacombsStatuesMobs = new ArrayList<>(Arrays.asList(
            Entity.SKELETON_ID, Entity.MINECRAFT_ZOMBIE_ID));


    /**
     * Check can grave spawn hell creature or not
     *
     * @param world
     * @param x     X coordinate
     * @param y     Y coordinate
     * @param z     Z coordinate
     */
    private static boolean canSpawnHellCreatures(World world, int x, int y, int z) {
        if (world != null) {
            return y < HELL_HEIGHT && world.getBlockState(new BlockPos(x, y - 1, z)).getBlock().equals(Blocks.NETHER_BRICK);
        } else {
            return false;
        }
    }

    /**
     * will create the entity from the internalID the first time it is accessed
     */
    public static net.minecraft.entity.Entity getMobEntity(World world, EnumGraves graveType, int x, int y, int z) {
        String id;

        switch (graveType.getGraveType()) {
            case DOG_STATUE:
                id = getMobID(world.rand, EnumMobType.UNDEAD_DOGS);
                break;
            case CAT_STATUE:
                id = getMobID(world.rand, EnumMobType.UNDEAD_CATS);
                break;
            case HORSE_STATUE:
                id = getMobID(world.rand, EnumMobType.UNDEAD_HORSES);
                break;
            default:
                if (canSpawnHellCreatures(world, x, y, z) && world.rand.nextInt(10) == 0) {
                    id = getMobID(world.rand, EnumMobType.HELL_MOBS);

                    if (id.equals(Entity.SKELETON_ID)) {
                        EntityGSSkeleton skeleton = getSkeleton(world, RANDOM.nextBoolean());
                        skeleton.setSkeletonType(SkeletonType.WITHER);
                        return skeleton;
                    }
                } else {
                    id = getMobID(world.rand, EnumMobType.DEFAULT_MOBS);

                    if (id.equals(Entity.SKELETON_ID)) {
                        EntityGSSkeleton skeleton = getSkeleton(world, RANDOM.nextBoolean());
                        if (RANDOM.nextInt(5) == 0) {
                            skeleton.setSkeletonType(SkeletonType.STRAY);
                        }
                        return skeleton;
                    }

                    if (id.equals(Entity.MINECRAFT_ZOMBIE_ID) && world.rand.nextInt(5) == 0) {
                        EntityZombie zombie = (EntityZombie) EntityList.createEntityByName(Entity.MINECRAFT_ZOMBIE_ID, world);
                        zombie.setZombieType(ZombieType.HUSK);
                        return zombie;
                    }
                }
                break;
        }

        EntityLiving entity = (EntityLiving) EntityList.createEntityByName(id, world);

        if (entity == null) {
            entity = getForeinMob(world, id);
        }

        try {
            entity.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(x, y, z)), (IEntityLivingData) null);
        } catch (Exception e) {
            GSLogger.logError("getMobEntity exception with onSpawnWithEgg");
            e.printStackTrace();
        }

        return entity;
    }

    /**
     * will create the entity from the internalID the first time it is accessed
     */
    public static net.minecraft.entity.Entity getMobEntityForSpawner(World world, EnumSpawner spawnerType, int x, int y, int z) {
        String mobId;

        switch (spawnerType) {
            case WITHER_SPAWNER:
                mobId = Entity.MINECRAFT_WITHER_ID;
                break;
            case SKELETON_SPAWNER:
                mobId = skeletonSpawnerMobs.get(world.rand.nextInt(skeletonSpawnerMobs.size()));

                if (mobId.equals(Entity.SKELETON_ID)) {
                    EntityGSSkeleton skeleton = (EntityGSSkeleton) EntityList.createEntityByName(Entity.SKELETON_ID, world);
                    if (world.rand.nextInt(5) == 0) {
                        skeleton.setSkeletonType(SkeletonType.STRAY);
                    } else if (world.rand.nextInt(10) == 0) {
                        skeleton.setSkeletonType(SkeletonType.WITHER);
                    }
                    return skeleton;
                }
                break;
            case SPIDER_SPAWNER:
                mobId = spiderSpawnerMobs.get(world.rand.nextInt(spiderSpawnerMobs.size()));
                break;
            case ZOMBIE_SPAWNER:
            default:
                mobId = zombieSpawnerMobs.get(world.rand.nextInt(zombieSpawnerMobs.size()));

                if (mobId.equals(Entity.MINECRAFT_ZOMBIE_ID) && world.rand.nextInt(5) == 0) {
                    EntityZombie zombie = (EntityZombie) EntityList.createEntityByName(Entity.MINECRAFT_ZOMBIE_ID, world);
                    zombie.setZombieType(ZombieType.HUSK);
                    return zombie;
                }
                break;
        }

        EntityLiving entity = (EntityLiving) EntityList.createEntityByName(mobId, world);

        if (entity == null) {
            entity = getForeinMob(world, mobId);
        }

        try {
            entity.onInitialSpawn(world.getDifficultyForLocation(new BlockPos(x, y, z)), (IEntityLivingData) null);
        } catch (Exception e) {
            GSLogger.logError("getMobEntity exception with onSpawnWithEgg");
            e.printStackTrace();
        }

        return entity;
    }

    /**
     * Return Skeleton with bow/sword
     */
    public static EntityGSSkeleton getSkeleton(World world, boolean withBow) {
        EntityGSSkeleton skeleton = (EntityGSSkeleton) EntityList.createEntityByName(Entity.SKELETON_ID, world);

        if (withBow) {
            skeleton.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.BOW, 1));
        } else {
            skeleton.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, new ItemStack(Items.STONE_SWORD, 1));
        }

        return skeleton;
    }

    public static boolean isWitherSkeleton(EntitySkeleton skeleton) {
        return skeleton.getSkeletonType() == SkeletonType.WITHER;
    }

    /**
     * Create and return instance for forein mobs
     *
     * @param world
     * @param mobName
     */
    private static EntityLiving getForeinMob(World world, String mobName) {
        EntityLiving mob = null;

        try {
            mob = mobNameToClassMapping.get(mobName).newInstance(new Object[]{world});
        } catch (InstantiationException e) {
            GSLogger.logError("getForeinMob InstantiationException. mob name " + mobName);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            GSLogger.logError("getForeinMob IllegalAccessException. mob name " + mobName);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            GSLogger.logError("getForeinMob InvocationTargetException. mob name " + mobName);
            e.getCause().printStackTrace();
        } catch (NullPointerException e) {
            GSLogger.logError("getForeinMob NullPointerException. mob name " + mobName);
            e.getCause().printStackTrace();
        }

        return mob;
    }

    /**
     * Return random mob id from list
     *
     * @param random
     * @param mobType
     */
    public static String getMobID(Random random, EnumMobType mobType) {
        switch (mobType) {
            case HELL_MOBS:
                return HELL_MOB_ID.get(random.nextInt(HELL_MOB_ID.size()));
            case UNDEAD_DOGS:
                return DOG_ID.get(random.nextInt(DOG_ID.size()));
            case UNDEAD_CATS:
                return CAT_ID.get(random.nextInt(CAT_ID.size()));
            case UNDEAD_HORSES:
                return HORSE_ID.get(random.nextInt(HORSE_ID.size()));
            case DEFAULT_MOBS:
            default:
                return MOB_ID.get(random.nextInt(MOB_ID.size()));
        }
    }

    public static boolean spawnMob(World world, net.minecraft.entity.Entity mob, double x, double y, double z, boolean checkSpawn) {
        float rotation = world.rand.nextFloat() * 360;
        return spawnMob(world, mob, x, y, z, rotation, checkSpawn);
    }

    /**
     * Spawn mob in world
     *
     * @param world World object
     * @param mob   Spawned mob
     * @param x     X coordinate
     * @param y     Y coordinate
     * @param z     Z coordinate
     */
    public static boolean spawnMob(World world, net.minecraft.entity.Entity mob, double x, double y, double z, float rotation, boolean checkSpawn) {
        EntityLiving livingEntity = (EntityLiving) mob;
        boolean canSpawn = false;
        double xPosition = x + 0.5;
        double yPosition = y;
        double zPosition = z + 0.5;
        mob.setLocationAndAngles(xPosition, yPosition, zPosition, rotation, 0);

        if (!checkSpawn || livingEntity.getCanSpawnHere()) {
            canSpawn = true;
        } else {
            if (!(mob instanceof EntityZombie)) {
                xPosition += 1;
                mob.setLocationAndAngles(xPosition, yPosition, zPosition, rotation, 0);

                if (livingEntity.getCanSpawnHere()) {
                    canSpawn = true;
                } else {
                    xPosition -= 1;
                    zPosition += 1;
                    mob.setLocationAndAngles(xPosition, yPosition, zPosition, rotation, 0);

                    if (livingEntity.getCanSpawnHere()) {
                        canSpawn = true;
                    } else {
                        zPosition -= 2;
                        mob.setLocationAndAngles(xPosition, yPosition, zPosition, rotation, 0);

                        if (livingEntity.getCanSpawnHere()) {
                            canSpawn = true;
                        } else {
                            xPosition -= 1;
                            zPosition += 1;
                            mob.setLocationAndAngles(xPosition, yPosition, zPosition, rotation, 0);

                            if (livingEntity.getCanSpawnHere()) {
                                canSpawn = true;
                            }
                        }
                    }
                }
            }
        }

        if (canSpawn) {
            xPosition = x + world.rand.nextFloat();
            yPosition = y + world.rand.nextFloat();
            zPosition = z + world.rand.nextFloat();
            world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, xPosition, yPosition + 2, zPosition, 0.0D, 0.0D, 0.0D);
            world.spawnParticle(EnumParticleTypes.FLAME, xPosition, yPosition + 1, zPosition, 0.0D, 0.0D, 0.0D);
            world.spawnEntityInWorld(mob);
            world.playEvent(2004, new BlockPos(x, y, z), 0);
            return true;
        } else {
            return false;
        }
    }

    /**
     * Check spawn mob or
     */
    public static boolean checkChance(Random random) {
        return random.nextInt(100) < ExtendedConfig.spawnChance;
    }

    /**
     * Return random mob for spawner
     */
    public static String getMobForSkeletonSpawner(Random random) {
        return skeletonSpawnerMobs.get(random.nextInt(skeletonSpawnerMobs.size()));
    }

    public static String getMobForZombieSpawner(Random random) {
        return zombieSpawnerMobs.get(random.nextInt(zombieSpawnerMobs.size()));
    }

    /**
     * Return random mob for spawner
     */
    public static String getMobForStatueSpawner(Random random) {
        return catacombsStatuesMobs.get(random.nextInt(catacombsStatuesMobs.size()));
    }

    public static void spawnCrawler(net.minecraft.entity.Entity entity, EntitySkullCrawler crawler) {
        if (entity.worldObj.rand.nextInt(10) == 0) {
            MobSpawn.spawnMob(entity.worldObj, crawler,
                    (int) Math.floor(entity.posX), entity.posY + 1.5, (int) Math.floor(entity.posZ),
                    entity.rotationYaw, false);
        }
    }

    public enum EnumMobType {

        DEFAULT_MOBS,
        HELL_MOBS,
        UNDEAD_DOGS,
        UNDEAD_CATS,
        UNDEAD_HORSES
    }
}