package nightkosh.gravestone_extended.structures.catacombs;

import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.init.Blocks;
import net.minecraft.world.gen.structure.StructureComponent;

import java.util.Random;

/**
 * GraveStone mod
 *
 * @author NightKosh
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 */
public class CatacombsStonesBlocks extends StructureComponent.BlockSelector {

    public CatacombsStonesBlocks() {
    }

    /**
     * Picks Block Ids and Metadata (Silverfish)
     */
    @Override
    public void selectBlocks(Random random, int par2, int par3, int par4, boolean par5) {
        if (par5) {
            float randFloat = random.nextFloat();

            if (randFloat < 0.2F) {
                this.blockstate = Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.CRACKED_META);
            } else if (randFloat < 0.4F) {
                this.blockstate = Blocks.stonebrick.getStateFromMeta(BlockStoneBrick.MOSSY_META);
            } else if (randFloat < 0.45F) {
                this.blockstate = Blocks.monster_egg.getStateFromMeta(BlockSilverfish.EnumType.STONEBRICK.getMetadata());
            } else {
                this.blockstate = Blocks.stonebrick.getDefaultState();
            }
        } else {
            this.blockstate = Blocks.air.getDefaultState();
        }
    }
}