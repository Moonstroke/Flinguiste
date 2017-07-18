---- SCHÉMA DE LA BASE DE DONNÉES CENTRALE DE L'APPLI


-- Les tables

DROP TABLE IF EXISTS android_metadata;
DROP TABLE IF EXISTS Definition;
DROP TABLE IF EXISTS Entree;
DROP TABLE IF EXISTS Niveau;
DROP TABLE IF EXISTS Nature;
DROP TABLE IF EXISTS Type;

-- données nécessaires à Android
CREATE TABLE android_metadata(locale TEXT);
INSERT INTO android_metadata VALUES ('fr_FR'), ('en_US');

-- Mot ou expression
CREATE TABLE Type (
	id_type INTEGER PRIMARY KEY,
	type TEXT NOT NULL
);

-- Nom, adjectif ou verbe
CREATE TABLE Nature (
	id_nat INTEGER NOT NULL,
	id_type INTEGER NOT NULL,
	nature TEXT NOT NULL,
	PRIMARY KEY (id_nat, id_type),
	FOREIGN KEY (id_type) REFERENCES Type(id_type)
);

-- Difficulté de jeu
CREATE TABLE Niveau (
	id_niv INTEGER PRIMARY KEY,
	niveau TEXT NOT NULL
);

CREATE TABLE Entree (
	id_ent INTEGER PRIMARY KEY,
	entree TEXT NOT NULL,
	id_nat INTEGER NOT NULL,
	id_type INTEGER NOT NULL,
	id_niv INTEGER NOT NULL,
	FOREIGN KEY (id_nat, id_type) REFERENCES Nature(id_nat, id_type),
	FOREIGN KEY (id_type) REFERENCES Type (id_type),
	FOREIGN KEY (id_niv) REFERENCES Niveau (id_niv)
);

CREATE TABLE Definition (
	id_def INTEGER PRIMARY KEY,
	definition TEXT NOT NULL,
	id_ent INTEGER DEFAULT 0,
	FOREIGN KEY (id_ent) REFERENCES Entree (id_ent)
);
