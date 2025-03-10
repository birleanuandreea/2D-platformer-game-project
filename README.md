# 2D-platformer-game-project

## Game Overview:

The project is a design for a 2D action-adventure fantasy game where Sir Arthur, a knight, aims to overthrow the evil Lord Malachar. The game features three levels with different environments and enemies: a snowy mountain (polar bears), an abandoned village (ghosts), and the Lord's fortress (dark guards).

## Gameplay Mechanics:

Players control Sir Arthur, battling enemies, collecting potions for health and attack power, and avoiding traps. The goal is to complete each level by defeating all enemies while maintaining health.

## Game Elements:

Characters: Sir Arthur (player), polar bears, ghosts, dark guard warriors.

Objects: Health and power-up potions, a single chest, interactive tiles.

Animations: Walking, sword attack, jumping, being injured, death.

## User Interface:

The game includes menus (main, options, pause), game over, level complete screens, and a main game interface displaying health and power-up status.

## Technical Design:

The game architecture is divided into packages:

entities: Manages game entities like Player, Enemy, and specific enemy types (Polar, Ghost, Warrior).

gamestates: Handles different game states (PLAYING, MENU, OPTIONS) and their logic.

inputs: Manages keyboard and mouse inputs.

levels: Handles level loading, management, and rendering.

main: Initializes the game, runs the main game loop, and manages the game window.

objects: Manages interactive game objects (potions, chest, spikes).

ui: Manages user interface elements like overlays and buttons.

utils: Provides utility methods for level data manipulation.

## Victory/Defeat:

Victory is achieved by completing all three levels and defeating all enemies. Defeat occurs when the player's health reaches zero. The game restarts from the beginning of the level upon death.
