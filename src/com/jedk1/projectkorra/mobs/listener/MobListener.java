package com.jedk1.projectkorra.mobs.listener;

import com.jedk1.projectkorra.mobs.MobMethods;
import com.jedk1.projectkorra.mobs.ProjectKorraMobs;
import com.jedk1.projectkorra.mobs.manager.EntityManager;
import com.projectkorra.projectkorra.event.BendingReloadEvent;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityTargetEvent;

public class MobListener implements Listener {

	ProjectKorraMobs plugin;

	public static boolean airFallDamage = ProjectKorraMobs.plugin.getConfig().getBoolean("Properties.Air.NoFallDamage");
	
	public MobListener(ProjectKorraMobs plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onFireAttack(EntityTargetEvent event) {
		if (event.getTarget() == null) {
			return;
		}
		if (event.getEntity() instanceof LivingEntity && event.getTarget() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getEntity();
			if (MobMethods.canBend(entity.getType())) {
				EntityManager.addEntity(entity, (LivingEntity) event.getTarget());
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onMobSpawn(CreatureSpawnEvent event) {
		LivingEntity entity = event.getEntity();
		if (MobMethods.isDisabledWorld(entity.getWorld())) {
			return;
		}
		if (MobMethods.canBend(entity.getType())) {
			MobMethods.assignElement(entity);
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onEntityDamage(EntityDamageEvent event) {
		if (event.getEntity() instanceof LivingEntity) {
			LivingEntity entity = (LivingEntity) event.getEntity();
			if (MobMethods.canBend(entity.getType())) {
				if ((MobMethods.isAirBender(entity) || MobMethods.isAvatar(entity))  && airFallDamage) {
					if (event.getCause() == DamageCause.FALL) {
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void onPKReload(BendingReloadEvent event) {
		ProjectKorraMobs.plugin.reloadConfig();
		MobMethods.registerDisabledWorlds();
		MobMethods.registerEntityTypes();
	}
}
