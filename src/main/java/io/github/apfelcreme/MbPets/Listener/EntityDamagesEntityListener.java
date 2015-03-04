package io.github.apfelcreme.MbPets.Listener;

import io.github.apfelcreme.MbPets.MbPets;
import io.github.apfelcreme.MbPets.MbPetsConfig;
import io.github.apfelcreme.MbPets.Pets.AngelPet;
import io.github.apfelcreme.MbPets.Pets.DevilPet;
import io.github.apfelcreme.MbPets.Pets.Pet;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EntityDamagesEntityListener implements Listener {

	/**
	 * cancels all damage events to make pets invulnerable
	 * 
	 * @param event
	 */
	@EventHandler
	public void onEntityDamage(EntityDamageByEntityEvent event) {
		Pet pet = MbPets.getInstance().getPetByEntity(event.getDamager());
		if (pet != null) {
			if (!(event.getEntity() instanceof Player) && event.getEntity() instanceof LivingEntity) {
				if (pet instanceof AngelPet || pet instanceof DevilPet) {
					event.setDamage(MbPetsConfig.getPetAttackStrength(pet.getType())+1);
					if (pet instanceof AngelPet) {
						((LivingEntity)event.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 2));
					} else if (pet instanceof DevilPet) {
						event.getEntity().setFireTicks(200);
					}
				} else {
					event.setDamage(MbPetsConfig.getPetAttackStrength(pet.getType()));
				}
			} else {
				event.setCancelled(true);
			}
		}
	}
}
