package uk.hotten.herobrine.game.runnables;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import uk.hotten.herobrine.game.GameManager;
import uk.hotten.herobrine.utils.GameState;
import uk.hotten.herobrine.utils.PlayerUtil;
import uk.hotten.herobrine.utils.ShardState;

public class NarrationRunnable extends BukkitRunnable {

	public double timer = 0;
	private GameManager gm;

	public NarrationRunnable(GameManager gm) {
		this.gm = gm;
	}

	@Override
	public void run() {
		if (gm.getGameState() != GameState.LIVE) {
			cancel();
			return;
		}

		if (gm.getShardCount() == 0 && gm.getShardState() == ShardState.WAITING && !gm.isShardPreviousDestroyed()) // I dunno i think it looks better without this at the start, and i think this was a bug on the hive too
			return;

		if (timer > 10)
			timer = 0;

		switch (gm.getShardState()) {
		case WAITING: {
			if (firstMsg())
				all("&bShard " + (gm.getShardCount() + 1) + " shall be summoned soon!");
			else
				separate("&cBEWARE: The Herobrine is still a cloud of smoke!", "&bTry eliminate all the Survivors", "&eYou're out of the game! Left-click to spectate.");
			break;
		}
		case SPAWNED: {
			all("&bUse your compass to find the shard!");
			break;
		}
		case CARRYING: {
			if (firstMsg())
				all("&a&l" + gm.getShardCarrier().getName() + "&3 has the shard!");
			else
				separate("&aProtect your shard carrier", "&bKill the shard carrier first!", "&eYou're out of the game! Left-click to spectate.");
			break;
		}
		case INACTIVE: {
			if (gm.getShardCount() != 3)
				return;

			separate("&cKill the Herobrine!", "&aYou are now visible!", "&eYou're out of the game! Left-click to spectate.");
			break;
		}
		default:
			break;
		}
		timer += 0.5;
	}

	private boolean firstMsg() {
		return timer <= 5;
	}

	private void all(String msg) {
		PlayerUtil.broadcastActionbar(gm.getGameLobby(), msg);
	}

	private void separate(String survivors, String herobrine, String spectators) {
		for (Player p : gm.getSurvivors())
			PlayerUtil.sendActionbar(p, survivors);
		for (Player p : gm.getSpectators())
			PlayerUtil.sendActionbar(p, spectators);
		PlayerUtil.sendActionbar(gm.getHerobrine(), herobrine);
	}
}