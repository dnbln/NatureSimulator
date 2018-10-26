#!/bin/bash
# The nature simulator commands

update(){
	sh "$HOME/.ns-install/update.sh"
	return $?
}

app_err(){
	echo "Error: application error, printing log" 1>&2
	cat err*.log 1>&2
	rm err*.log
	echo "Please report this error at https://www.github.com/NonNullDinu/NatureSimulator/issues" 1>&2
	exit 2
}

check(){
	wget -O valid_hashes https://raw.githubusercontent.com/NonNullDinu/NatureSimulator/master/.data/valid_hashes
	hashes="$(cat valid_hashes)"
	jar_content="$(cat NatureSimulator.jar)"
	printf "%s" "$(cat NatureSimulator.jar)" | md5sum > temp-hash
	jar_hash="$(cat temp-hash)"
	jar_hash=${jar_hash:0:32}
	rm temp-hash
	rm valid_hashes
	if [[ "$hashes" == *"$jar_hash"* ]] ;
	then	return 0
	else	return 1
	fi
}

navigate_error(){
	echo "Error: could not navigate to install directory" 1>&2
	exit 1
}

check_failed(){
	echo "Jar hash is not a valid hash."
	exit 5
}

if [[ "$1" = "update" ]] ;
then	update
	exit $?
fi

if [[ "$1" = "run" ]] ;
then	cd "$(cat ~/.ns-install/target-dir)" || navigate_error
	check || check_failed
	if [[ ! $? ]] ;
	then	exit 5
	fi
	if [[ ! -f NatureSimulator.jar ]] || [[ ! -d gameData ]] ;
	then	printf "Core program files not found, repair?y/n:"
		read repair
		if [[ "$repair" = "y" || "$repair" = "Y" ]] ;
		then	if [[ ! -f NatureSimulator.jar ]] ;
			then	curl -o jr.tar.xz https://raw.githubusercontent.com/NonNullDinu/NatureSimulator/master/updates/jar.tar.xz
				tar -xJf jr.tar.xz
				rm jr.tar.xz
			fi
			if [[ ! -d gameData ]] ;
			then	mkdir gameData
				cd gameData
				curl -o gameData.tar.xz https://raw.githubusercontent.com/NonNullDinu/NatureSimulator/master/updates/gameData.tar.xz
				tar -xJf gameData.tar.xz
				rm gameData.tar.xz
				cd ..
			fi
		else exit 6
		fi
	fi
	if [[ ! -d lib ]] ;
	then	echo "Natives folder not found." 1>&2
		exit 4
	fi
	java -jar NatureSimulator.jar 2> error.log || app_err
	exit 0
fi

if [[ "$1" = "--delete-save" ]] ;
then	cd "$(cat ~/.ns-install/target-dir)" || navigate_error
	rm saveData/save0.nssv
fi

if [[ "$1" = "--version" || "$1" = "-v" ]] ;
then	cd "$(cat ~/.ns-install/target-dir)" || navigate_error
	cat version || exit 3
	printf "\n"
	exit 0
fi
