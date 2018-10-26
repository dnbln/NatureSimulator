#!bin/bash
update () {
	curl -o update.sh https://raw.githubusercontent.com/NonNullDinu/NatureSimulator/master/updates/latestUpdate.sh || return 2
	sh update.sh || return 3
	rm update.sh || return 4
	return 0
};
cd "$(cat ~/.ns-install/target-dir)" || exit 1
update
exit $?