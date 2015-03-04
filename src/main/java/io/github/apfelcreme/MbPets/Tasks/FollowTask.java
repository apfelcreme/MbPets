package io.github.apfelcreme.MbPets.Tasks;

import io.github.apfelcreme.MbPets.Pets.Pet;

import org.bukkit.entity.Creature;
public class FollowTask implements Runnable {

	Pet pet;

	public FollowTask(Pet pet) {
		this.pet = pet;
	}

	@Override
	public void run() {
		Creature entity = (Creature) pet.getEntity();
		if (entity.getWorld().equals(pet.getOwner().getPlayer().getWorld())) {
//			if (pet.getOwner().getPlayer().getLocation()
//					.distance(entity.getLocation()) > 10) {
//				entity.setTarget(pet.getOwner().getPlayer());
//			} else {
//				entity.setTarget(null);
//			}
			// >20 = port 
			if (pet.getOwner().getPlayer().getLocation()
					.distance(entity.getLocation()) > 25) {
				entity.teleport(pet.getOwner().getPlayer().getLocation());
			}
		}
	}

}
