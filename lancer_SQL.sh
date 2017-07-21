#!/bin/bash

d="${0%/*}" # <=> "$(dirname $0)"

doss_bd="$d/app/src/main/assets/databases"
nom_bd="flinguiste.db"
schema="$d/sql/flinguiste_schéma.sql"
donnees="$d/sql/flinguiste_données.sql"


# USAGE : mourir message code
mourir() {
	printf "$1\n" >&2
	exit $2
}

# USAGE : executer sqlite(3?) fichier.db fichier1.sql...
executer() {
	for f_sql in ${@:3}
	do
		if [ -e "$f_sql" ]
		then
			$1 $2 < $f_sql || mourir "Une erreur est survenue pendant l'exécution des scripts SQL" 2
		else
			(echo "Le fichier \"$f_sql\" n'existe pas" >&2)
		fi
	done
}


touch "$doss_bd/$nom_bd" || mourir "Impossible de créer le fichier \"$doss_bd/$nom_bd\"" 3

if [ -n "$(which sqlite3)" ]
then
	sqlite_x="sqlite3"
else
	(echo "'sqlite3' non trouvé, essayons avec 'sqlite'" >&2)
	if [ -n "$(which sqlite)" ]
	then
		sqlite_x="sqlite"
	else
		mourir "'sqlite' non trouvé, impossible de continuer !" 255
	fi
fi

if [ "$1" = "recreer" ]
then
	executer $sqlite_x "$doss_bd/$nom_bd" $schema $donnees
elif [ "$1" = "reremplir" ]
then
	executer $sqlite_x "$doss_bd/$nom_bd" $donnees
else
	mourir "USAGE : $0 OPÉRATION\nOPÉRATION peut être :\n\t- \"recreer\" pour supprimer les tables et les recréer\n\t- \"reremplir\" pour uniquement renouveler les contenu des tables" 1
fi
