A basic home plugin that is written for Landania.net as an application.
A funny side note: I wrote this first try. I'm not sure how I did that. Another unfortunate note is that i could not find the skull texture for the delete item provided in the docs, so its gonna be a Pepe the frog head for now.

The commons library contains a simple wrapper around a platform, and a configuration system used by other modules

## API
Integration with the API comes from a static class HomeAPI
HomePlayer is an object assigned to each player by the used server platform.
HomePlayers contain a list of homes and an ability to teleport to and create homes.

Examples of fetching a HomePlayer

```java
HomePlayer player = HomeAPI.getPlatform().getPlayer(UUID)
```

Examples of teleporting and creating new homes

```java
HomePlayer player = HomeAPI.getPlatform().getPlayer(UUID)
if (player.hasHome("homeName")) {
    player.createHome(HomeName, HomeWorld, HomeX, HomeY, HomeZ, HomeYaw, HomePitch)
}
player.teleport("homeName")
```

Creation of homes using #createHome also returns a boolean and quits the process early if a home with a specified name already exists for the player.
