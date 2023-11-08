# METAcraft-info-commands

A mod used to add informative commands that can be used by players to get information about a server.

Everything is specified in the `metacraftinfocommands.json` config file.
Under `commands` you specify all the commands you want with the name as the key and what it should display as the value.

The display value can either be [raw JSON text](https://minecraft.wiki/w/Raw_JSON_text_format), 
or it can be a json node containing the `message` field which is 
[raw JSON text](https://minecraft.wiki/w/Raw_JSON_text_format) and an optional
`subCommands` which contains a map of sub-commands in the same format.

A default config file will be generated with a few examples.

You can use the /reload command to reload the config file at runtime.  
By default, this mod makes the /reload command resend the command tree to all players.  
This can be turned off by toggling `resendCommandTreeOnReload` config option.  
Alternatively, the `meta-info-resend-command-tree` may be used, which can be toggled via `enableResendCommandTreeCommand` (disabled by default).
This command has the permission node `se.datasektionen.mc.infocommands.meta-info-resend-command-tree`.