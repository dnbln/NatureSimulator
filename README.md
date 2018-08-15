![Logo](https://raw.githubusercontent.com/NonNullDinu/NatureSimulator/master/gameData/textures/logo.png)

# Description
This application is an open-source attempt to simulate nature.
Still has a long way to go before the objective is reached

# System requirements
Only requires JRE 10.0.2 or later to be installed to run

###### Developed on Fedora 28

# How to update?

###### Note: This is feature still under development.
#### Updates will be available from v.1.3.3-alpha.
### Linux
On Linux, just run the following and the application will update:<br>
<code>sh ~/.ns-install/update.sh</code><br>
I would suggest doing it this way, but, it can be updated through .nus files as well

### MacOS 
You can run <code>sh ~/.ns-install/update.sh</code><br> on Mac to, 
but make sure your terminal can run the command wget, or replace 
it with [alternatives](https://alternativeto.net/software/wget/?platform=mac)

### Windows
Get the .nus file and run patch.jar with the name of the .nus file as the argument

# OS dependent features

| Feature                            | Linux x32_64     | macOS x64                     | Windows 7+ x64            |
|------------------------------------|------------------|-------------------------------|---------------------------|
| **N**S<br>**U**pdate<br>**S**cript | ✓                | ✓                             | ✓                         |
| BASH-based update                  | ✓ 4.1+           |   Need linux "wget"           |                           |
| WinCMD-based update                |                  |                               | ✓                         |
| Dependency native libraries        | ✓                |   @FIXME                      | ✓                         |

# Update log
* NS v.1.3.3.-alpha
	* BASH4.1-based updating engine v.1.0
		* Downloads update script
		* Runs update script
		* After it's done, deletes the update script
	
	* WinCMD-based updating engine v.1.0
		* Runs the .nus file