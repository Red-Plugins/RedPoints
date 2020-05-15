<img src="https://avatars2.githubusercontent.com/u/63077065?s=400&u=738f37e1c06c85fa9dafe798c2f23123fea0ed89&v=4" alt="Red-Plugins" title="Red-Plugins" align="right" height="96" width="96"/>

# RedPoints

[![Servers](https://img.shields.io/bstats/servers/7513)]()
[![Players](https://img.shields.io/bstats/players/7513)]()

## Introduction

A custom plugin focused on points for players.

Supported in only one version at the moment, which would be: spigot-1.8.8

**Note**: I'm sorry for my bad English, I'm Brazilian.

## Commands

- `/redpoints` - Show all available commands.
- `/redpoints backup (forced/true/false)` - Backup functions.
- `/redpoints remove (player) (amount)` - Remove a specific value.
- `/redpoints create (player)` - Create an account for the specified player.
- `/redpoints set (player) (amount)` - Modify the amount of a specific player.
- `/redpoints donate (player) (amount)` - Donate a specified amount.
- `/redpoints add (player) (amount)` - Add a specific value for a player.
- `/redpoints delete (player)` - Delete the account of specified player.
- `/redpoints leaderboard` - See the list of best.
- `/redpoints reset (player)` - Restore the account of the specified player.
- `/redpoints show` - Show your account value.
- `/redpoints reload` - Reload all settings.

## Permissions

- `redpoints.*` - Inherits all plugin permissions
- `redpoints.command.(name_commands_here)` - Replace this (name_commands_here) with the name of the command. For example: `redpoints.command.show`.
- `repoints.backupalert` - Permission to view the alert message.

## Configuration 

```yaml
  #Settings for a connection to your database
Database:
  #To use Mysql set to 'true', if you don't want to use it, set to 'false'.
  Use: true
  #Your host ip adress
  Host: ""
  Port: 3306
  #Your username
  Username: ""
  #Your password
  Password: ""
  #Your database name
  Database: ""
  #Your custom table name
  Table: ""
 
#Send an alert to all players when an automatic backup is performed 
backupAlertForPlayers: true 
#Automatic backup enabled or disabled
backupState: true
```

## Storage Type

This plugin has two forms of storage, either through a yaml configuration or through Mysql. To enable mysql, put 'true' in 'Use' located in 'config.yml', if you want storage by Yaml, just put 'false'.

## Api

***Example***:

## Events

**Example**:


## Curiosities

**LeaderBoard**:

<img src="https://raw.githubusercontent.com/Red-Plugins/RedPoints/master/assets/leaderboard.png" alt="Red-Plugins" title="Red-Plugins" align="center" height="496" width="992"/>

**Automatic Backup**:

<img src="https://raw.githubusercontent.com/Red-Plugins/RedPoints/master/assets/automaticbackup.png" alt="Red-Plugins" title="Red-Plugins" align="center" height="46" width="992"/>
