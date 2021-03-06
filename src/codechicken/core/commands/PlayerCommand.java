package codechicken.core.commands;

import codechicken.lib.raytracer.RayTracer;
import net.minecraft.util.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.command.ICommandSender;
import net.minecraft.world.WorldServer;

public abstract class PlayerCommand extends CoreCommand
{
    @Override
    public boolean canCommandSenderUse(ICommandSender var1) {
        return super.canCommandSenderUse(var1) && var1 instanceof EntityPlayer;
    }

    @Override
    public void handleCommand(String command, String playername, String[] args, ICommandSender listener) {
        EntityPlayerMP player = (EntityPlayerMP) listener;
        handleCommand(getWorld(player), player, args);
    }

    public abstract void handleCommand(WorldServer world, EntityPlayerMP player, String[] args);

    public static BlockPos traceBlock(EntityPlayerMP player, float reach) {
        return RayTracer.retrace(player, reach).getBlockPos();
    }

    public static Entity traceEntity(EntityPlayerMP player, float reach) {
        return RayTracer.retrace(player, reach).entityHit;
    }
}
